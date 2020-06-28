#!/bin/sh

# List the several tag groups

exiftoolpath="/usr/bin"
file="tag_groups.txt"

printf "Do \"exiftool -listg0 -listg1 -listg2 -listg3 -listg4\" to file\n"
$exiftoolpath/exiftool -listg0 -listg1 -listg2 -listg3 -listg4 > ${file}

printf "We have the lines with Groups in family x; change space to underscore\n"
sed -i '/^Group/s/ /_/g' ${file};

printf "Now change every space for a line feed\n"
sed -i 's/ /\n/g' ${file};

printf "Remove all blank lines\n"
sed -i '/^[[:space:]]*$/d' ${file};

printf "Change all underscores back to spaces\n\n"
sed -i '/^Group/s/_/ /g' ${file};

printf "Your tag groups are in file \"${file}\"\n\n"