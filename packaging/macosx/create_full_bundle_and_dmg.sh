#!/bin/bash

# Download jar2app from https://github.com/Jorl17/jar2app
# Download the latest JRE for Mac OS/X if you want to bundle java with the app (-r ....jdk/jre)

# Some variables
jar2appPATH="../../../jar2app/jar2app"
Version="1"

# Do initial cleanup
rm -rf jExifToolGUI.app tmp *.dmg
# Create the app bundle
# full bundle with jdk
$jar2appPATH ../../build/libs/jExifToolGUI.jar jExifToolGUI -n jExifToolGUI -i ./appIcon.icns -r jdk1.8.0_212.jdk.zip
# default bundle without jdk
#$jar2appPATH ../../build/libs/ExifToolGUI.jar jExifToolGUI -n jExifToolGUI -i ./appIcon.icns

# Create the dmg
mkdir -p tmp/dmg
# 1MB dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=360

mkfs.hfsplus -v "jExifToolGUI $Version" tmp/jExifToolGUI.dmg

sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../src/org/hvdw/jexiftoolgui/resources/COPYING tmp/dmg/

sudo umount tmp/dmg
mv tmp/jExifToolGUI.dmg ./"jExifToolGUI-full-$Version.dmg"
rm -rf tmp 
