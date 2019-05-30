#!/usr/bin/python

import sys, os, csv
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
    print("You need to give the folder containing the xml files")
    sys.exit()

fpath = sys.argv[1]
print("Directory given: " + fpath)

for filename in os.listdir(fpath):
    if filename.endswith(".xml"):
        #print("filename: " + filename)
        tree = ET.parse(fpath + os.path.sep + filename)
        root = tree.getroot()

        print("basename : " + os.path.basename(filename))
        basename = os.path.basename(filename)
        prefix_basename = os.path.splitext(basename)

        with open(prefix_basename[0] + '.csv', mode='w') as tag_csv_file:
            tag_csv_file_writer = csv.writer(tag_csv_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            for child in root:  # The child is the table inside the xml
                for tags in child.iter('tag'):
                    tag_name = tags.get('name')
                    tag_type = tags.get('type')
                    tag_writable = tags.get('writable')
                    #if tag_writable == 'true' and tag_type <> 'undef':
                    #    print("name: " + tag_name + "; type: " + tag_type)
                    if tag_type <> 'undef':
		                tag_csv_file_writer.writerow([tag_name, tag_type, tag_writable])