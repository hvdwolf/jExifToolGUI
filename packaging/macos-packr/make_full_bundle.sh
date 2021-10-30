#!/bin/bash

# Some variables
BaseApp="jExifToolGUI.app.base"
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

printf "do initial cleanup\n\n"
rm -rf $App

## --vmargs Xmx2G -Dlogback.configurationFile=logback.xml \
printf "do the packr stuff to create the app\n\n"
java -jar $Packr \
     --platform mac \
     --jdk ${JRE} \
     --executable jexiftoolgui \
     --classpath ../../jExifToolGUI.jar \
     --mainclass org.hvdw.jexiftoolgui.Application \
     --vmargs Xmx2G \
     --vmargs Dlogback.configurationFile=logback.xml \
     --vmargs XstartOnFirstThread \
     --resources ./logback.xml \
     --minimizejre soft \
     --icon ./appIcon.icns \
     --output $App

printf "Copy info.plist.base and update version string\n\n"
cp Info.plist.base $App/Contents/Info.plist
sed -i "s+Version_String+$Version+" $App/Contents/Info.plist
#cp logback.xml $App/Contents/Resources
#cp jexiftoolgui.json.base $App/Contents/Resources/jexiftoolgui.json
mv $App/Contents/Resources/icons.icns $App/Contents/Resources/appIcon.icns

printf "Create the 135MB (full) dmg\n\n"
mkdir -p tmp/dmg
# 155MB dmg
dd if=/dev/zero of=tmp/jExifToolGUI.dmg bs=1M count=135

mkfs.hfsplus -v "jExifToolGUI-x86_64 $Version" tmp/jExifToolGUI.dmg

sudo mount -o loop tmp/jExifToolGUI.dmg tmp/dmg
sudo cp -a  jExifToolGUI.app tmp/dmg/
#sudo mv  jExifToolGUI.app tmp/dmg/
sudo cp ../../LICENSE tmp/dmg/

sudo umount tmp/dmg
mv tmp/jExifToolGUI.dmg ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
zip -9 ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg.zip" ./"jExifToolGUI-x86_64-macos-$Version-with_jre.dmg"
rm -rf tmp 


