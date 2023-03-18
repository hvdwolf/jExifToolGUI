#!/bin/sh

# List the several tag groups

exiftoolpath="/mnt/chromeos/removable/128GB/software/perl/Image-ExifTool-12.58"
#file="tag_groups.txt"
groups="g0 g1 g2 g3 g4"


for group in $groups;
do
    echo $group
    $exiftoolpath/exiftool -list${group} > tmp.txt
    printf "Remove the line with Groups in family x\nChange every space to line feed\nRemove all blank lines\n"
    file="${group}.txt"
    #sed -i '/Groups/d/' ${file}
    grep -v "Groups in" tmp.txt > ${file}
    sed -i 's/ /\n/g' ${file} 
    sed -i '/^[[:space:]]*$/d' ${file}
done