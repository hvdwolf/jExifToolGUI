#!/usr/bin/env python

import sys
import xml.etree.ElementTree as ET

# xml files created with
# exiftool -listx -gps:all > gps.xml
# exiftool -listx -exif:all > exif.xml
# exiftool -listx -xmp:all > xmp.xml
# etc.
#
# Use the "CreateXmls.sh" script to do it for you

#tree = ET.parse('gps.xml')

if len(sys.argv)!=2:
    print("You need to give the xml file (and path if relevant)")
    sys.exit()

#xmlfile = sys.argv[1]
#print(xmlfile)
tree = ET.parse(sys.argv[1])
root = tree.getroot()

for child in root:  # The child is the table inside the xml
    for tags in child.iter('tag'):
        tag_name = tags.get('name')
        tag_type = tags.get('type')
        tag_writable = tags.get('writable')
        if tag_writable == 'true' and tag_type <> 'undef':
            print("name: " + tag_name + "; type: " + tag_type)
