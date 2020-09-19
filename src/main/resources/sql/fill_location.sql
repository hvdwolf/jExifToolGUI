--
-- Sql query for 1.6
-- This will prepopulate the CustomMetadataset and CustomMetadatasetLines with the location data
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='gps_location';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('gps_location','');

delete from CustomMetadatasetLines where customset_name='gps_location';
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',0,'GPS Version ID','EXIF:GPSVersionID','2.3.0.0');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',1,'Latitude ref','EXIF:GPSLatitudeRef','North');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',2,'Latitude','EXIF:GPSLatitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',3,'Longitude ref','EXIF:GPSLongitudeRef','West');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',4,'Longitude','EXIF:GPSLongitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',5,'Altitude ref','EXIF:GPSAltitudeRef','Above Sea Level');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',6,'Altitude ref','EXIF:GPSAltitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',7,'GPSMapDatum','EXIF:GPSMapDatum','WGS-84');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',8,'Location','XMP:Location','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',9,'xmp altitude','XMP:GPSAltitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',10,'xmp latitude','XMP:GPSLatitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',11,'xmp longitude','XMP:GPSLongitude','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',12,'City','XMP:City','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',13,'Country','XMP:Country','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',14,'State','XMP:State','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',15,'iptc City','IPTC:City','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',16,'sub-location','IPTC:Sub-location','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',17,'Province-State','IPTC:Province-State','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('gps_location',18,'Country Primairy name','IPTC:Country-PrimaryLocationName','');