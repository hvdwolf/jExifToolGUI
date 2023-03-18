#!/bin/bash

# This is run from linux. On MacOS itself the dmg creation is much easier

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

printf "remove existing app bundle folder\n"
rm -rf jExifToolGUI.app tmp jExifToolGUI.dmg
mkdir -p tmp
printf "Unzip the zipped appbundle\n"
unzip "jExifToolGUI-$arch-macos-$Version-with_jre.zip"

if [ "$arch" = "x86_64" ];
then
    printf "Create the 170MB (full) dmg\n\n"
    dd if=/dev/zero of=jExifToolGUI.dmg bs=1M count=170
else
    printf "Create the 300MB (full) dmg\n\n"
    dd if=/dev/zero of=jExifToolGUI.dmg bs=1M count=300
fi

printf "do the /sbin/mkfs.hfsplus -v \"jExifToolGUI-$arch $Version\" jExifToolGUI.dmg"
/sbin/mkfs.hfsplus -v "jExifToolGUI-$arch $Version" jExifToolGUI.dmg

printf "do the sudo mount -o loop jExifToolGUI.dmg tmp"
sudo mount -o loop -t hfsplus jExifToolGUI.dmg tmp
sudo cp -a  jExifToolGUI.app tmp
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp LICENSE tmp
printf "\nCreate symbolic link to /Applications\n\n"
cd tmp
sudo ln -s /Applications Applications
cd ..

printf "sudo umount tmp"
sudo umount tmp
mv jExifToolGUI.dmg ./"jExifToolGUI-$arch-macos-$Version-with_jre.dmg"
zip -9 ./"jExifToolGUI-$arch-macos-$Version-with_jre.dmg.zip" ./"jExifToolGUI-$arch-macos-$Version-with_jre.dmg"
#rm -rf tmp
 
