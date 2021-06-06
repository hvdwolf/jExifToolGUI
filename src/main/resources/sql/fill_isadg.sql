--
-- Sql query for 1.6
-- This will prepopulate the CustomMetadataset and CustomMetadatasetLines with the isadg data
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='isadg';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg','isadg-struct.cfg');

delete from CustomMetadatasetLines where customset_name='isadg';
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',0,'Identity Reference','xmp-isadg:Reference','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',1,'Identity Title','xmp-isadg:Title','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',2,'Identity Date','xmp-isadg:Date','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',3,'Identity DescriptionLevel','xmp-isadg:DescriptionLevel','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',4,'Identity Extent','xmp-isadg:Extent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',5,'Context Creator','xmp-isadg:Creator','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',6,'Context Adminbiohistory','xmp-isadg:Adminbiohistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',7,'Context Archivalhistory','xmp-isadg:Archivalhistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',8,'Context Acqinfo','xmp-isadg:Acqinfo','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',9,'Contentstructure Scopecontent','xmp-isadg:ScopeContent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',10,'Contentstructure Appraisaldestruction','xmp-isadg:Appraisaldestruction','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',11,'Contentstructure Accruals','xmp-isadg:Accruals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',12,'Contentstructure Arrangement','xmp-isadg:Arrangement','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',13,'Conditionsaccessuse Accessrestrictions','xmp-isadg:Accessrestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',14,'Conditionsaccessuse Reprorestrictions','xmp-isadg:Reprorestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',15,'Conditionsaccessuse Languagescripts','xmp-isadg:Languagescripts','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',16,'Conditionsaccessuse Phystech','xmp-isadg:Phystech','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',17,'Conditionsaccessuse Findingaids','xmp-isadg:Findingaids','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',18,'Alliedmaterials Existencelocationoriginals','xmp-isadg:Existencelocationoriginals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',19,'Alliedmaterials Existencelocationcopies','xmp-isadg:Existencelocationcopies','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',20,'Alliedmaterials Relatedunits','xmp-isadg:Relatedunits','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',21,'Alliedmaterials Publication','xmp-isadg:Publication','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',22,'Notes Note','xmp-isadg:NotesNote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',23,'Descriptioncontrol Archivistsnote','xmp-isadg:Archivistsnote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',24,'Descriptioncontrol Rulesconventions','xmp-isadg:Rulesconventions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:Descriptionsdate','');
