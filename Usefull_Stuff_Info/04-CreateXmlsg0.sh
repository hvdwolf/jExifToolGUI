#!/bin/sh

DIR="g0-xml-dir"
rm -rf "$DIR"
mkdir -p "$DIR"
exiftoolpath="/mnt/chromeos/removable/128GB/software/perl/Image-ExifTool-12.58"

$exiftoolpath/exiftool -listg0 > tmp.txt
grep -v "Groups in" tmp.txt > tmp2.txt
all_groups=$(cat tmp2.txt)

for group in $all_groups;
do
    echo "Now working on: ${group}"
    $exiftoolpath/exiftool -listx -${group}:all > "$DIR"/${group}.xml
done


cd "$DIR"
printf "Delete the 0 byte files\n"
find . -type f -size 0 -delete


printf "Now delete the ones that are actually not correctly produced\n" 
grep -l "empty list"  *.xml | xargs rm
