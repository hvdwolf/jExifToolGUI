#!/bin/sh

if [ "$1" = "" ]
then
        printf "\n\nYou have to provide which tags you want to save\n\n"
        printf "all => CreateTagsPerGroup.sh all\n"
        printf "or\n"
        printf "writable => CreateTagsPerGroup.sh writable\n\n"
        exit
fi

if [ "$1" = "all" ]
then
        tagoption="-list"
        DIR="all-g1-tags-text-dir"
        printf "Create all the text files for all tags.\nThis will take quite long.\n\n"
else
        tagoption="-listw"
        DIR="writable-g1-tags-text-dir"
        printf "Create all the text files for all writable tags.\nThis will take quite long.\n\n"
fi

rm -rf "$DIR"
mkdir -p "$DIR"
exiftoolpath="/mnt/chromeos/removable/128GB/software/perl/Image-ExifTool-12.58"

$exiftoolpath/exiftool -listg1 > tmp.txt
grep -v "Groups in" tmp.txt > tmp2.txt
all_groups=$(cat tmp2.txt)

for group in $all_groups;
do
    echo "Now working on: ${group}"
    $exiftoolpath/exiftool ${tagoption} -${group}:all > "$DIR"/${group}.txt
done





printf "Now delete the ones that are actually not correctly produced\n\n"
cd "$DIR" 
printf "Delete the 0 byte files"
find . -type f -size 0 -delete

grep -l "empty list" *.txt | xargs rm

printf "Now make the text file have one tag per line; Then remove blank lines\n\n"
for i in *.txt;
do
  if [ "$1" = "all" ]
  then
      # We have the lines with "Available xyz tags:"; change space to underscore\n"
      sed -i '/^Available/d' "$i";
  else
      # We have the lines with "Writable xyz tags:"; change space to underscore\n"
      sed -i '/^Writable/d' "$i";
  fi
  # Change every space for a line feed
  sed -i 's/ /\n/g' "$i";
  # Remove all blank lines
  sed -i '/^[[:space:]]*$/d' "$i";
  
done

printf "\n\nYour tag text files are in folder \"${DIR}\"\n\n"
