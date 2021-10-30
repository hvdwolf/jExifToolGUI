#!/bin/bash

# Script to create a macOS bundle from a jar
# using packr v4.0 to create the bundle (https://github.com/libgdx/packr)
# using linux-ported dmg to make a compressed dmg (https://github.com/fanquake/libdmg-hfsplus)
# 2021-10-30 v1.0 HvdW

# Some variables
AppName="jExifToolGUI"
App="jExifToolGUI.app"
Packr="../../../packr-all-4.0.0.jar"
JRE="/var/host/media/removable/128GB/software/java/JREs/macos-jdk-11.0.10+9-jre"

if [ "$1" = "" ]
then
        printf "\n\nYou have to provide the version\n\n"
        exit
fi

Version="$1"

printf "do initial cleanup by removing the $App\n\n"
rm -rf $App tmp "jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"

##      --vmargs XstartOnFirstThread \
##      --resources ./jexiftoolgui-splashlogo.png \
##     --splash ./jexiftoolgui-splashlogo.png \
printf "do the packr stuff to create the $App application bundle\n\n"
java -jar $Packr \
     --platform mac \
     --jdk ${JRE} \
     --executable jexiftoolgui \
     --classpath ../../jExifToolGUI.jar \
     --mainclass org.hvdw.jexiftoolgui.Application \
     --resources ./logback.xml \
     --resources ./jexiftoolgui-splashlogo.png \
     --minimizejre soft \
     --icon ./appIcon.icns \
     --vmargs Xmx2G \
     --vmargs Dlogback.configurationFile=logback.xml \
     --vmargs splash:jexiftoolgui-splashlogo.png \
     --output $App

printf "Copy info.plist.base and update version string\n\n"
cp Info.plist.base $App/Contents/Info.plist
sed -i "s+Version_String+$Version+" $App/Contents/Info.plist
# rename my appIcon.icns back from packr's renamed icon.icns
mv $App/Contents/Resources/icons.icns $App/Contents/Resources/appIcon.icns

printf "\nCreate the 135MB (full) dmg\n\n"
mkdir -p tmp/dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=135

printf "\nFormat the dmg raw disk file as hfsplus\n\n"
mkfs.hfsplus -v "jExifToolGUI-x86_64 $Version" tmp/jExifToolGUI.dmg

printf "\nNow we will use sudo to mount the created dmg and copy our stuff in\n\n"
sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
printf "\nCopy $App and LICENSE in\n\n"
sudo cp -a  $App tmp/dmg/
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../LICENSE tmp/dmg/

sudo umount tmp/dmg
if [ -f "./dmg" ];
then 
    printf "\nNow create the compressed dmg from the uncompressed one\n\n"
    ./dmg tmp/jExifToolGUI.dmg ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
else
    printf "\n\nI can't find dmg to compress the dmg file. Please Read the !README.txt first to compile it\n\n"
fi
rm -rf tmp 


