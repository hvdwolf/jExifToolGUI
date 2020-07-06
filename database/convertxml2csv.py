#!/usr/bin/python

import sys, os, csv, shutil
import xml.etree.ElementTree as ET

		
# xml files created with
# exiftool -listx -gps:all > gps.xml
# exiftool -listx -exif:all > exif.xml
# exiftool -listx -xmp:all > xmp.xml
# etc.
#
# Use the "CreateXmls.sh" script to do it for you

#tree = ET.parse('gps.xml')

if len(sys.argv)!=3:
    print("You need to give the folder containing the xml files as first parameter, and the folder saving the csv files to as second parameter.")
    sys.exit()

fpath = sys.argv[1]
#csvPath = 'csv-dir'
csvPath = sys.argv[2]
print("Directory given: " + fpath)
if os.path.exists(csvPath):
    shutil.rmtree(csvPath)
os.makedirs(csvPath)


for filename in os.listdir(fpath):
    if filename.endswith(".xml"):
        #print("filename: " + filename)
        tree = ET.parse(fpath + os.path.sep + filename)
        root = tree.getroot()

        print("basename : " + os.path.basename(filename))
        basename = os.path.basename(filename)
        prefix_basename = os.path.splitext(basename)

        with open(os.path.join(csvPath, prefix_basename[0] + '.csv'), mode='w') as tag_csv_file:
            tag_csv_file_writer = csv.writer(tag_csv_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            # Write header
            tag_csv_file_writer.writerow(["TagNameGroup","TagName","TagType","Writable","G0", "G1", "G2"])
            for child in root:  # The child is the table inside the xml
                for tags in child.iter('tag'):
                    tag_name = tags.get('name')
                    tag_type = tags.get('type')
                    tag_writable = tags.get('writable')
                    tag_g0 = tags.get('g0')
                    tag_g1 = tags.get('g1')
                    tag_g2 = tags.get('g2')
                    if tag_type <> 'undef':
		                tag_csv_file_writer.writerow([prefix_basename[0], tag_name, tag_type, tag_writable, tag_g0, tag_g1, tag_g2])

        with open(os.path.join(csvPath, prefix_basename[0] + '-group.csv'), mode='w') as table_csv_file:
            table_csv_file_writer = csv.writer(table_csv_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            # Write header
            table_csv_file_writer.writerow(["TagNameGroup","G0", "G1", "G2"])
            for child in root:  # The child is the table inside the xml
                for tables in child.iter('table'):
                    #table_name = tables.get('name')
                    table_g0 = tables.get('g0')
                    table_g1 = tables.get('g1')
                    table_g2 = tables.get('g2')
                    table_csv_file_writer.writerow([prefix_basename[0], table_g0, table_g1, table_g2])