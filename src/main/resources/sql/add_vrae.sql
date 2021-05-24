--
-- Sql query for 1.9
-- This will add the vrae data to the CustomMetadataset and CustomMetadatasetLines
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='vrae';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('vrae','vrae.config');


-- INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',0,'Identity Reference','xmp-isadg:Reference','');


INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',0,'Work Agent','xmp-vrae:WorkAgent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',1,'Work Title','xmp-vrae:WorkTitle','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',2,'Work Date','xmp-vrae:WorkDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',3,'Work Style Period','xmp-vrae:WorkStylePeriod','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',4,'Work Cultural Context','xmp-vrae:WorkCulturalContext','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',5,'Work Type','xmp-vrae:WorkWorktype','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',6,'Work Material','xmp-vrae:WorkMaterial','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',7,'Work Technique','xmp-vrae:WorkTechnique','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',8,'Work Measurements','xmp-vrae:WorkMeasurements','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',9,'Work Location','xmp-vrae:WorkLocation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',10,'Work Description','xmp-vrae:WorkDescription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',11,'Work Subject','xmp-vrae:WorkSubject','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',12,'Work Inscription','xmp-vrae:WorkInscription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',13,'Work State Edition','xmp-vrae:WorkStateEdition','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',14,'Work Relation','xmp-vrae:WorkRelation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',15,'Work Text Reference','xmp-vrae:WorkTextref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',16,'Work Source','xmp-vrae:WorkSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',17,'Work ID','xmp-vrae:WorkRefid','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',18,'Work Rights','xmp-vrae:WorkRights','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',19,'Image Title','xmp-vrae:ImageTitle','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',20,'Image Agent','xmp:Creator','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',21,'Image Date','IPTC:DateCreated','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',22,'Image Description','xmp-vrae:ImageDescription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',23,'Image Inscription','xmp-vrae:ImageInscription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',24,'Image Subject','xmp-vrae:ImageSubject','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',25,'Image Relation','xmp-vrae:ImageRelation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',26,'Image Source','xmp-vrae:ImageSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',27,'Image ID','xmp-vrae:ImageRefid','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',28,'Image URL','xmp-vrae:ImageHref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',29,'Image Rights','xmp-dc:Rights','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',30,'Work Metadata Date','xmp-vrae:WorkmetadataDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',31,'Work Metadata Id','xmp-vrae:WorkMetadataid','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',32,'Work Metadata Source','xmp-vrae:WorkMetadataSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',33,'Work Metadata Href','xmp-vrae:WorkMetadataHref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',34,'Work Href','xmp-vrae:Workhref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',35,'Admin Collection','xmp-dc:Publisher','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',36,'Admin Cataloguer','IPTC:Writer-Editor','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae',37,'Admin Job ID','xmp-photoshop:TransmissionReference','');

