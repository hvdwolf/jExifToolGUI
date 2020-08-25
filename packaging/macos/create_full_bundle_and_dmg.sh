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
rm -rvf jExifToolGUI.app tmp *.dmg *.zip
# Create the app bundle
# full bundle with jre
printf "Create the bundle from the base bundle\n\n"
cp -a $BaseApp $App

printf "Update the VersionString to $Version\n\n"
sed -i "s+VersionString+$Version+" $App/Contents/Info.plist

printf "Now copy the jre and the jar into the app\n\n" 
cp -a jre $App/Contents/MacOS/jre
cp ../../jExifToolGUI.jar $App/Contents/MacOS/

printf "Create the 140MB (full) dmg\n\n"
mkdir -p tmp/dmg
# 125MB dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=140

mkfs.hfsplus -v "jExifToolGUI-x86_64 $Version" tmp/jExifToolGUI.dmg

sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
sudo cp -a  jExifToolGUI.app tmp/dmg/
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../LICENSE tmp/dmg/

sudo umount tmp/dmg
mv tmp/jExifToolGUI.dmg ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
zip -9 ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg.zip" ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
rm -rf tmp 
