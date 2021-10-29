#!/bin/bash

# Some variables
BaseApp="jExifToolGUI.app.base"
AppName="jExifToolGUI"
App="jExifToolGUI.app"
# Now specify where your (unpacked) JRE is located
JRE="/var/host/media/removable/128GB/software/java/JREs/macos-jdk-11.0.10+9-jre"
IET="/var/host/media/removable/128GB/software/perl/Image-ExifTool-12.30"

if [ "$1" = "" ]
then
        printf "\n\nYou have to provide the version\n\n"
        exit
fi

Version="$1"

printf "Do initial cleanup\n\n"
rm -rvf jExifToolGUI.app tmp *.dmg *.zip
# Create the app bundle
# full bundle with jre
printf "Create the bundle from the base bundle\n\n"
cp -a $BaseApp $App

printf "Update the VersionString to $Version\n\n"
sed -i "s+Version_String+$Version+" $App/Contents/Info.plist

printf "Now copy the jre and the jar into the app\n\n" 
#cp -a jre $App/Contents/MacOS/jre
cp ../../jExifToolGUI.jar $App/Contents/MacOS/
mkdir -p $App/Contents/MacOS/jre
cp -a --preserve=links ${JRE}/* $App/Contents/MacOS/jre

printf "Now copy exiftool into the app\n\n"
# This requires you to download the latest exiftool from https://exiftool.org and untar it
# We simply use the "uninstalled" version. Below line specifies the version and optional path
mkdir -p $App/Contents/MacOS/ExifTool
# copy everything, then clean up things not needed
cp -a ${IET}/* $App/Contents/MacOS/ExifTool
rm -rf $App/Contents/MacOS/ExifTool/t $App/Contents/MacOS/ExifTool/html $App/Contents/MacOS/ExifTool/Changes $App/Contents/MacOS/ExifTool/Makefile.PL



printf "Create the 145MB (full) dmg\n\n"
mkdir -p tmp/dmg
# 125MB dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=145

/sbin/mkfs.hfsplus -v "jExifToolGUI-x86_64 $Version" tmp/jExifToolGUI.dmg

sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
sudo cp -a  jExifToolGUI.app tmp/dmg/
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../LICENSE tmp/dmg/

sudo umount tmp/dmg
mv tmp/jExifToolGUI.dmg ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
zip -9 ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg.zip" ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
#rm -rf tmp 
