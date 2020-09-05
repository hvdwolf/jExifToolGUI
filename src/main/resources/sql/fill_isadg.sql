--
-- Sql query for 1.6
-- This will prepopulate the CustomMetadataset and CustomMetadatasetLines with the isadg data
-- The tables themselves are created inside java
--
delete from CustomMetadataset where customset_name='isadg-full' or customset_name='isadg-alias';
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg-full','isadg-struct.cfg');
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg-alias','isadg-struct.cfg');

delete from CustomMetadatasetLines where customset_name='isadg-full' or customset_name='isadg-alias';
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',0,'Identity Reference','xmp-isadg:Reference','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',1,'Identity Title','xmp-isadg:Title','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',2,'Identity Date','xmp-isadg:Date','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',3,'Identity DescriptionLevel','xmp-isadg:DescriptionLevel','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',4,'Identity Extent','xmp-isadg:Extent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',5,'Context Creator','xmp-isadg:Creator','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',6,'Context Adminbiohistory','xmp-isadg:Adminbiohistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',7,'Context Archivalhistory','xmp-isadg:Archivalhistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',8,'Context Acqinfo','xmp-isadg:Acqinfo','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',9,'Contentstructure Scopecontent','xmp-isadg:ScopeContent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',10,'Contentstructure Appraisaldestruction','xmp-isadg:Appraisaldestruction','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',11,'Contentstructure Accruals','xmp-isadg:Accruals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',12,'Contentstructure Arrangement','xmp-isadg:Arrangement','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',13,'Conditionsaccessuse Accessrestrictions','xmp-isadg:Accessrestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',14,'Conditionsaccessuse Reprorestrictions','xmp-isadg:Reprorestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',15,'Conditionsaccessuse Languagescripts','xmp-isadg:Languagescripts','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',16,'Conditionsaccessuse Phystech','xmp-isadg:Phystech','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',17,'Conditionsaccessuse Findingaids','xmp-isadg:Findingaids','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',18,'Alliedmaterials Existencelocationoriginals','xmp-isadg:Existencelocationoriginals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',19,'Alliedmaterials Existencelocationcopies','xmp-isadg:Existencelocationcopies','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',20,'Alliedmaterials Relatedunits','xmp-isadg:Relatedunits','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',21,'Alliedmaterials Publication','xmp-isadg:Publication','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',22,'Notes Note','xmp-isadg:NotesNote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',23,'Descriptioncontrol Archivistsnote','xmp-isadg:Archivistsnote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',24,'Descriptioncontrol Rulesconventions','xmp-isadg:Rulesconventions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:xmp-isadg:Descriptionsdate','');

INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',0,'Identity Reference','xmp-isadg:IdentityReference','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',1,'Identity Title','xmp-isadg:IdentityTitle','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',2,'Identity Date','xmp-isadg:IdentityDate','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',3,'Identity DescriptionLevel','xmp-isadg:IdentityDescriptionLevel','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',4,'Identity Extent','xmp-isadg:IdentityExtent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',5,'Context Creator','xmp-isadg:ContextCreator','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',6,'Context Adminbiohistory','xmp-isadg:ContextAdminbiohistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',7,'Context Archivalhistory','xmp-isadg:ContextArchivalhistory','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',8,'Context Acqinfo','xmp-isadg:ContextAcqinfo','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',9,'Contentstructure Scopecontent','xmp-isadg:ContentstructureScopeContent','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',10,'Contentstructure Appraisaldestruction','xmp-isadg:ContentstructureAppraisaldestruction','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',11,'Contentstructure Accruals','xmp-isadg:ContentstructureAccruals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',12,'Contentstructure Arrangement','xmp-isadg:ContentstructureArrangement','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',13,'Conditionsaccessuse Accessrestrictions','xmp-isadg:ConditionsaccessuseAccessrestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',14,'Conditionsaccessuse Reprorestrictions','xmp-isadg:ConditionsaccessuseReprorestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',15,'Conditionsaccessuse Languagescripts','xmp-isadg:ConditionsaccessuseLanguagescripts','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',16,'Conditionsaccessuse Phystech','xmp-isadg:ConditionsaccessusePhystech','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',17,'Conditionsaccessuse Findingaids','xmp-isadg:ConditionsaccessuseFindingaids','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',18,'Alliedmaterials Existencelocationoriginals','xmp-isadg:AlliedmaterialsExistencelocationoriginals','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',19,'Alliedmaterials Existencelocationcopies','xmp-isadg:AlliedmaterialsExistencelocationcopies','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',20,'Alliedmaterials Relatedunits','xmp-isadg:AlliedmaterialsRelatedunits','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',21,'Alliedmaterials Publication','xmp-isadg:AlliedmaterialsPublication','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',22,'Notes Note','xmp-isadg:NotesNote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',23,'Descriptioncontrol Archivistsnote','xmp-isadg:DescriptioncontrolArchivistsnote','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',24,'Descriptioncontrol Rulesconventions','xmp-isadg:DescriptioncontrolRulesconventions','');
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:Descriptioncontrolxmp-isadg:Descriptionsdate','');

