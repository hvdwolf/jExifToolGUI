#!/bin/bash

# Some variables
BaseApp="jExifToolGUI.app.base"
AppName="jExifToolGUI"
App="jExifToolGUI.app"
# Now specify where your (unpacked) JRE is located
JRE_x86_64="/mnt/chromeos/removable/128GB/software/java/JREs/macos-jdk-11.0.15+10-jre-x86_64"
JRE_aarch64="/mnt/chromeos/removable/128GB/software/java/JREs/macos-jdk-11.0.15+10-jre-aarch64"
IET="/mnt/chromeos/removable/128GB/software/perl/Image-ExifTool-12.58"

if [ $# -lt 2 ]
then
        printf "\n\nYou have to provide the version as first argument\n"
        printf "and the architecture as 2nd argument, where architecture is\n"
        printf "either \"x86_64\" (\"old\" intel) or \"aarch64\" (new M1)\n\n"
        printf "Like: ./create_macos_bundle.sh 2.0.2 aarch64\n\n" 
        exit
fi

Version="$1"
arch="$2"
if ! [[ "$arch" = "x86_64" ]] && ! [[ "$arch" = "aarch64" ]];
then
    printf "\n\nYou gave a wrong architecture.\n"
    printf "It should be \"x86_64\" (\"old\" intel) or \"aarch64\" (new M1)\n\n"
    exit
fi


printf "Do initial cleanup\n\n"
rm -rf jExifToolGUI.app tmp *.dmg "jExifToolGUI-$arch-macos-$Version-with_jre.zip"
# Create the app bundle
# full bundle with jre
printf "Create the bundle from the base bundle\n\n"
cp -a $BaseApp $App

printf "Update the VersionString to $Version\n\n"
sed -i "s+Version_String+$Version+" $App/Contents/Info.plist

printf "Now copy the jre for $arch and the jar into the app\n\n" 
#cp -a jre $App/Contents/MacOS/jre
cp ../../jExifToolGUI.jar $App/Contents/MacOS/
mkdir -p $App/Contents/MacOS/jre
if [ "$arch" = "x86_64" ];
then
    printf "\ncopying the x86_64 jre into the bundle\n"
    cp -a --preserve=links ${JRE_x86_64}/* $App/Contents/MacOS/jre
else
    printf "\ncopying the aarch64 jre into the bundle\n"
    cp -a --preserve=links ${JRE_aarch64}/* $App/Contents/MacOS/jre
fi

printf "Now copy exiftool into the app\n\n"
# This requires you to download the latest exiftool from https://exiftool.org and untar it
# We simply use the "uninstalled" version. Below line specifies the version and optional path
mkdir -p $App/Contents/MacOS/ExifTool
printf "copy everything, then clean up things not needed"
cp -a ${IET}/* $App/Contents/MacOS/ExifTool
rm -rf $App/Contents/MacOS/ExifTool/t $App/Contents/MacOS/ExifTool/html $App/Contents/MacOS/ExifTool/Changes $App/Contents/MacOS/ExifTool/Makefile.PL


# Now zip it
zip -r ./"jExifToolGUI-$arch-macos-$Version-with_jre.zip" jExifToolGUI.app
