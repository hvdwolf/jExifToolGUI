#!/bin/bash

# Some variables
BaseApp="jExifToolGUI.app.base"
AppName="jExifToolGUI"
App="jExifToolGUI.app"

if [ "$1" = "" ]
then
        printf "\n\nYou have to provide the version\n\n"
        exit
fi

Version="$1"

printf "Do initial cleanup\n\n"
rm -rvf jExifToolGUI.app tmp *.dmg
# Create the app bundle
# full bundle with jre
printf "Create the bundle from the base bundle\n\n"
cp -a $BaseApp $App

printf "Update the VersionString to $Version\n\n"
sed -i "s+Version_String+$Version+" $App/Contents/Info.plist

printf "Now copy the jar and modified script into the app\n\n" 
cp ../../jExifToolGUI.jar $App/Contents/MacOS/
cp jexiftoolgui.jar_only $App/Contents/MacOS/jexiftoolgui

printf "Now copy exiftool into the app\n\n"
# This requires you to download the latest exiftool from https://exiftool.org and untar it
# We simply use the "uninstalled" version. Below line specifies the version and optional path
IET="Image-ExifTool-12.19"
mkdir -p $App/Contents/MacOS/ExifTool
# copy everything, then clean up things not needed
cp -a ${IET}/* $App/Contents/MacOS/ExifTool
rm -rf $App/Contents/MacOS/ExifTool/t $App/Contents/MacOS/ExifTool/html $App/Contents/MacOS/ExifTool/Changes $App/Contents/MacOS/ExifTool/Makefile.PL


printf "Create the 30MB dmg\n\n"
mkdir -p tmp/dmg
# 20MB dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=30

mkfs.hfsplus -v "jExifToolGUI-x86_64 $Version" tmp/jExifToolGUI.dmg

sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
sudo cp -a  jExifToolGUI.app tmp/dmg/
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../LICENSE tmp/dmg/

sudo umount tmp/dmg
mv tmp/jExifToolGUI.dmg ./"jExifToolGUI-x86_64-macos-$Version.dmg"
rm -rf tmp 
