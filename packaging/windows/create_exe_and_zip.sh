#!/bin/bash

# Download launch4j from http://launch4j.sourceforge.net/

launch4jPATH="../../../launch4j/launch4j"
Version="0.1"

$launch4jPATH ./jexiftoolgui.xml

mkdir "jExifToolGUI-$Version"
mv *.exe "jExifToolGUI-$Version"
cp ../../src/org/hvdw/jexiftoolgui/resources/COPYING "jExifToolGUI-$Version"

zip -9 -r "jExifToolGUI-$Version.zip" "jExifToolGUI-$Version"
rm -rf "jExifToolGUI-$Version"

