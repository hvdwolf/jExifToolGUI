--
-- Sql query for 1.6
-- This will prepopulate the CustomMetadataset and CustomMetadatasetLines with the Google Photos data
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='Google Photos';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('Google Photos','');

delete from CustomMetadatasetLines where customset_name='Google Photos';
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',0,'IPTC:DateCreated','IPTC:DateCreated','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',1,'IPTC:TimeCreated','IPTC:TimeCreated','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',2,'EXIF:DateTimeOriginal','EXIF:DateTimeOriginal','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',3,'XMP-photoshop:DateCreated','XMP-photoshop:DateCreated','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',4,'XMP-exif:DateTimeOriginal','XMP-exif:DateTimeOriginal','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',5,'IPTC:DigitalCreationDate','IPTC:DigitalCreationDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',6,'IPTC:DigitalCreationTime','IPTC:DigitalCreationTime','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',7,'EXIF:CreateDate','EXIF:CreateDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',8,'XMP-xmp:CreateDate','XMP-xmp:CreateDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',9,'XMP-exif:DateTimeDigitized','XMP-exif:DateTimeDigitized','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',10,'EXIF:ModifyDate','EXIF:ModifyDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',11,'XMP-xmp:ModifyDate','XMP-xmp:ModifyDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',12,'GPS:GPSDateStamp','GPS:GPSDateStamp','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',13,'GPS:GPSTimeStamp','GPS:GPSTimeStamp','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',14,'System:FileModifyDate','System:FileModifyDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',15,'EXIF:Model','EXIF:Model','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',16,'EXIF:FNumber','EXIF:FNumber','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',17,'XMP-exif:FNumber','XMP-exif:FNumber','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',18,'XMP-exif:ApertureValue','XMP-exif:ApertureValue','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',19,'EXIF:MaxApertureValue','EXIF:MaxApertureValue','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',20,'XMP-exif:MaxApertureValue','XMP-exif:MaxApertureValue','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',21,'EXIF:ExposureTime','EXIF:ExposureTime','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',22,'XMP-exif:ExposureTime','XMP-exif:ExposureTime','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',23,'EXIF:ShutterSpeedValue','EXIF:ShutterSpeedValue','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',24,'XMP-exif:ShutterSpeedValue','XMP-exif:ShutterSpeedValue','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',25,'EXIF:FocalLength','EXIF:FocalLength','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',26,'XMP-exif:FocalLength','XMP-exif:FocalLength','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',27,'EXIF:ISO','EXIF:ISO','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',28,'XMP-exif:ISO','XMP-exif:ISO','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',29,'EXIF:GPSVersionID','EXIF:GPSVersionID','2.3.0.0');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',30,'EXIF:GPSLatitudeRef','EXIF:GPSLatitudeRef','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',31,'EXIF:GPSLatitude','EXIF:GPSLatitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',32,'EXIF:GPSLongitudeRef','EXIF:GPSLongitudeRef','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',33,'EXIF:GPSLongitude','EXIF:GPSLongitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',34,'EXIF:GPSAltitudeRef','EXIF:GPSAltitudeRef','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',35,'EXIF:GPSAltitude','EXIF:GPSAltitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',36,'EXIF:GPSMapDatum','EXIF:GPSMapDatum','WGS-84');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('Google Photos',37,'EXIF:Orientation','EXIF:Orientation','');