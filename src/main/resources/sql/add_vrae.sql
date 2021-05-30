--
-- Sql query for 1.9
-- This will add the full vrae metadata to the CustomMetadataset and CustomMetadatasetLines
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='vrae-full';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('vrae-full','vrae.config');

INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',0,'Work Agent','xmp-vrae:WorkAgent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',1,'Work Title','xmp-vrae:WorkTitle','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',2,'Work Date','xmp-vrae:WorkDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',3,'Work Style Period','xmp-vrae:WorkStylePeriod','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',4,'Work Cultural Context','xmp-vrae:WorkCulturalContext','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',5,'Work Type','xmp-vrae:WorkWorktype','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',6,'Work Material','xmp-vrae:WorkMaterial','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',7,'Work Technique','xmp-vrae:WorkTechnique','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',8,'Work Measurements','xmp-vrae:WorkMeasurements','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',9,'Work Location','xmp-vrae:WorkLocation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',10,'Work Description','xmp-vrae:WorkDescription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',11,'Work Subject','xmp-vrae:WorkSubject','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',12,'Work Inscription','xmp-vrae:WorkInscription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',13,'Work State Edition','xmp-vrae:WorkStateEdition','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',14,'Work Relation','xmp-vrae:WorkRelation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',15,'Work Text Reference','xmp-vrae:WorkTextref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',16,'Work Source','xmp-vrae:WorkSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',17,'Work ID','xmp-vrae:WorkRefid','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',18,'Work Rights','xmp-vrae:WorkRights','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',19,'Image Title','xmp-vrae:ImageTitle','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',20,'Image Agent','xmp:Creator','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',21,'Image Date','IPTC:DateCreated','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',22,'Image Description','xmp-vrae:ImageDescription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',23,'Image Inscription','xmp-vrae:ImageInscription','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',24,'Image Subject','xmp-vrae:ImageSubject','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',25,'Image Relation','xmp-vrae:ImageRelation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',26,'Image Source','xmp-vrae:ImageSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',27,'Image ID','xmp-vrae:ImageRefid','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',28,'Image URL','xmp-vrae:ImageHref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',29,'Image Rights','xmp-dc:Rights','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',30,'Work Metadata Date','xmp-vrae:WorkMetadataDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',31,'Work Metadata Id','xmp-vrae:WorkMetadataId','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',32,'Work Metadata Source','xmp-vrae:WorkMetadataSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',33,'Work Metadata Href','xmp-vrae:WorkMetadataHref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',34,'Work Href','xmp-vrae:WorkHref','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',35,'Admin Collection','xmp-dc:Publisher','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',36,'Admin Cataloguer','IPTC:Writer-Editor','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',37,'Admin Job ID','xmp-photoshop:TransmissionReference','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',38,'Work Location Creation','xmp-vrae:WorkLocationCreation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',39,'Work Location Discovery','xmp-vrae:WorkLocationDiscovery','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',40,'Work Location Exhibition','xmp-vrae:WorkLocationExhibition','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',41,'Work Location','xmp-vrae:WorkLocation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',42,'Work Location Site','xmp-vrae:WorkLocationSite','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',43,'Work Location Former Site','xmp-vrae:WorkLocationFormerSite','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',44,'Work Location Installation','xmp-vrae:WorkLocationInstallation','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',45,'Work Location Intended','xmp-vrae:WorkLocationIntended','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',46,'Work Location Other','xmp-vrae:WorkLocationOther','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',47,'Work Location Owner','xmp-vrae:WorkLocationOwner','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',48,'Work Location Former Owner','xmp-vrae:WorkLocationFormerOwner','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',49,'Work Location Performance','xmp-vrae:WorkLocationPerformance','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',50,'Work Location Publication','xmp-vrae:WorkLocationPublication','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',51,'Work Location Repository','xmp-vrae:WorkLocationRepository','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',52,'Work Location Former Repository','xmp-vrae:WorkLocationFormerRepository','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',53,'Work Location Notes','xmp-vrae:WorkLocationNotes','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',54,'Image Metadata Date','xmp-vrae:ImageMetadataDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',55,'Image Metadata Source','xmp-vrae:ImageMetadataSource','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('vrae-full',56,'Image Location','xmp-vrae:ImageLocation','');
