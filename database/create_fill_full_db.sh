#!/bin/bash

curdir=$(pwd)

if [[ "$1" != "" ]]
then
        WORKDIR="${1}"
        printf "\n\nYou gave folder ${1} to work in.\n\n"
        mkdir -p ${1}
        cd ${1}
else
        WORKDIR=${curdir}
fi

printf "Start time:\n" > time.txt
echo $(date) >> time.txt

XMLDIR="${WORKDIR}/xml-dir"
csvdir="${WORKDIR}/csv-dir"
rm -rf "${XMLDIR}"
mkdir -p "${XMLDIR}"

DB="${WORKDIR}/jexiftoolgui.db"
ET="/BigData/Image-ExifTool-12.01/exiftool"

printf "\n\nfirst delete current database\n\n"
rm -rf $DB

printf "Create blank database\n\n"
sqlite3 $DB < ${curdir}/create_jexiftoolgui_db.sql

printf "fill database\n\n"

printf "Add exiftool version that was used to create this db\n"
version=$(${ET} -ver)
printf "It was version $version\n\n"
sqlite3 $DB "insert into ExiftoolVersion values('$version');"

printf  "\n\nWorking on exiftool family groups ${families}\n"
families="g0 g1 g2 g3 g4"
echo "-- "$families > tmp.sql
for family in ${families};
do
  printf "working on ${family}\n"
#  groupString=$(exiftool "-list"${group} | grep -v Groups | sed 's/ /\n/g' | sed '/^[[:space:]]*$/d')
  groupString=$(${ET} "-list"${family} | grep -v Groups)
  #echo $groupString
  for group in $groupString;
  do
   echo "insert into FamilyGroups(family_id, famgroup) select id,'$group' from Family where Family.Family='${family}';" >> tmp.sql
   done
done
printf "Add families plus groups to database\n\n"
sqlite3 $DB < tmp.sql

printf "Add deletable groups\n"
echo "-- Add deletable groups to database" > tmp.sql
dgroups=$(${ET} "-listwf" | grep -v Deletable)
for dgroup in $dgroups;
do
  echo "update familygroups set deletable=1 where famgroup='$dgroup';" >> tmp.sql
done
sqlite3 $DB < tmp.sql

printf "Add unique groups to Groups\n\n"
sqlite3 $DB "insert into Groups(id, taggroup) select id,famgroup from familygroups;"
# Now simply remove duplicates by keeping the ones with the lowest rowid (= lowest family)
sqlite3 $DB "delete from Groups where rowid not in (select min(rowid) from Groups group by taggroup);"
##########################
printf "\n\nAdd supported file extensions\n"
echo "-- Add supported file extensions" > tmp.sql
extensions=$(${ET} "-listf" | grep -v Supported)
for extension in $extensions;
do
  echo "insert into FileExtensions(extension) values('$extension');" >> tmp.sql
done
sqlite3 $DB < tmp.sql

printf "Add writable file extensions\n\n"
echo "-- Add writable file extensions" > tmp.sql
extensions=$(${ET} "-listwf" | grep -v Writable)
for extension in $extensions;
do
  echo "update FileExtensions set writable=1 where extension='$extension';" >> tmp.sql
done
sqlite3 $DB < tmp.sql


##########################
printf "\n\nAdd all tags to the database (this can take 3-6 minutes. Please be patient.\n"
echo "-- Add supported tags to the database"> tmp.sql
tags=$(${ET} -list | grep -v Available | grep -v "Command-line")
for tagname in $tags;
do 
  echo "insert into Tags(tagname) values('$tagname');" >> tmp.sql
done
sqlite3 $DB < tmp.sql
printf "We have one duplicate record. Delete it! (bug in exiftool?)\n"
sqlite3 $DB "delete from Tags where rowid not in (select min(rowid) from Tags group by tagname);"

printf "Now, after inital filling, create the unique index on Tags\n"
sqlite3 $DB "create unique index if not exists Tags_Index on Tags(tag);"

printf "Add all writable tags to the database (this can also take a few minutes. Please be patient).\n"
echo "-- Add writable tags to the database" > tmp.sql
tags=$(${ET} -listw | grep -v Writable)
for tagname in $tags;
do 
  echo "update Tags set writable=1 where tagname='$tagname';" >> tmp.sql
done
sqlite3 $DB < tmp.sql

##########################

printf "\n\nNow begin with the time consuming coupling of tags to groups.\n"
printf "Use all unique groups from our Groups table\n"
rm tags_done.txt
allgroups=$(sqlite3 $DB "select taggroup from Groups;")
for group in $allgroups; # for example XMP or EXIF
do
  printf "working on group $group\n"
  groupTags=`${ET} "-list" "-${group}:all" | grep -v Available`
  if [[ $groupTags != *"empty list"* ]];
  then
    echo "--" > tmp.sql
    echo "-- Group $group" >> tmp.sql
    echo "--" >> tmp.sql
    for tag in $groupTags;
    do
      echo "insert into TagsInGroups(groupid, tagid) select Groups.id, Tags.id from Groups,Tags where Groups.taggroup='${group}' and Tags.tagname='${tag}';" >> tmp.sql
    done
    sqlite3 $DB < tmp.sql
    printf "$group added to TagsInGroups.\n"
    echo $group >> tags_done.txt
  fi
done
printf "\nCreated the TagsInGroups\n\n"

##########################
printf "remove temp sql file\n\n"
rm -rvf tmp.sql

##########################
# Do the stuff partly again. This should be optimized but I don't care at the moment,
printf "getting all the groups\n"
g0=$(${ET} -listg0 | grep -v Groups)
g1=$(${ET} -listg1 | grep -v Groups)
g2=$(${ET} -listg2 | grep -v Groups)
g3=$(${ET} -listg3 | grep -v Groups)
g4=$(${ET} -listg4 | grep -v Groups)

printf "merging all the groups\n"
all_groups=("${g0[@]}" "${g1[@]}" "${g2[@]}" "${g3[@]}" "${g4[@]}")
#echo ${all_groups[@]}

printf "sorting all the groups\n"
sorted_unique_groups=($(echo "${all_groups[@]}" | tr ' ' '\n' | sort -u | tr '\n' ' '))
#printf "\n\nAnd now the trick\n\n"
groups=$(echo ${sorted_unique_groups[@]})

#echo $groups

printf "\n\nCreate the xmls for all the groups in ${XMLDIR}\n\n"
for group in $groups;
do
    printf "working on ${group}\n"
    ${ET} -listx "-${group}:all" > "${XMLDIR}"/${group}.xml
done

printf "Remove every xml file smaller than <= 105 bytes\n\n"
find "${XMLDIR}" -name "*.xml" -size -106c -delete

printf "Start pyhon script to parse the xmls\n"
printf "We start it as a background job and wait for it to finish\n"
nohup ${curdir}/convertxml2csv.py ${XMLDIR} ${CSVDIR} &
printf "Now wait\n"
wait
printf "python script ready. Now remove the ABCD-group.csv files\n"
rm -rf ${CSVDIR}/*group.csv
printf "Remove every csv file smaller than <= 51 bytes\n\n"
find "${CSVDIR}" -name "*.csv" -size -50c -delete

#sqlite3 -header -csv $DB tmptags < csv-dir/EXIF.csv tmptags
for csvfile in ${csvdir}/*.csv;
do
    printf "working on ${csvfile}\n"
    echo -e ".separator ","\n.import ${csvfile} tmptags" | sqlite3 $DB
done

sqlite3 $DB "delete from tmptags where rowid not in (select min(rowid) from tmptags group by tagname);"

printf "\n\nNow update table TAGS with our collected tagtypes\n"
sqlite3 $DB "update Tags set tagtype=(select tagtype from tmptags where tmptags.tagname=tags.tagname);"

sqlite3 $DB "delete from tmptags;"

##########################
printf "optimize sqlite database through VACUUM\n\n"
sqlite3 $DB "VACUUM;"

# very last step
if [[ "$1" != "" ]]
then
        printf "\n\nYou can find your jexiftoolgui.db inside folder ${1}.\n\n"
fi

printf "\n\nEnd time\n" >> time.txt
echo $(date) >> time.txt
