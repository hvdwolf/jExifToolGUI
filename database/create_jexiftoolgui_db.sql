--
-- Create the metadata part of the DB
--
create table if not exists ExiftoolVersion (
    version text
);

create table if not exists ApplicationVersion (
    version text
);

create table if not exists Family (
    id integer primary key autoincrement,
    family text NOT NULL
);
insert into Family(family) values('g0');
insert into Family(family) values('g1');
insert into Family(family) values('g2');
insert into Family(family) values('g3');
insert into Family(family) values('g4');


create table if not exists FamilyGroups (
    id integer primary key autoincrement,
    family_id integer NOT NULL,
    famgroup text NOT NULL,
    deletable integer,
    unique (family_id, famgroup),
    foreign key(family_id) references Family(id)
);

Create table if not exists Groups (
    id integer NOT NULL,
    taggroup text NOT NULL,
    unique (id, taggroup)
); 

create table if not exists FileExtensions (
    id integer primary key autoincrement,
    extension text NOT NULL,
    writable integer
);

create table if not exists Tags (
    id integer primary key autoincrement, 
    tagname text NOT NULL,
    tagtype text,
    writable text,
    flags text
);
-- Below index will be created AFTER having added everything to Tags as that makes the initial "filling" faster
-- create unique index if not exists Tags_Index on Tags(tag);

create table if not exists TagsInGroups (
    id integer primary key autoincrement,
    groupid integer NOT NULL,
    tagid integer NOT NULL,
    unique (groupid, tagid),
    foreign key(groupid) references Groups(id),
    foreign key(tagid) references Tags(id)
);

create table if not exists tmptags ( 
  TagNameGroup text NOT NULL,
  TagName text NOT NULL,
  TagType text NOT NULL,
  Writable text NOT NULL,
  Flags text NOT NULL,
  G0 text,
  G1 text,
  G2 text
);

create view if not exists v_tags_groups as select taggroup,tagname,tagtype,writable,flags from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id;
--
-- Create DB part for the custom "things"
--
Create table if not exists CustomMetadataset (
    id integer primary key autoincrement,
    customset_name text NOT NULL UNIQUE,
    custom_config text,
    unique (id, customset_name)
);

Create table if not exists CustomMetadatasetLines (
    id integer primary key autoincrement,
    customset_name text NOT NULL,
    rowcount integer,
    screen_label text NOT NULL,
    tag text NOT NULL,
    default_value text,
    UNIQUE (customset_name, tag),
    foreign key(customset_name) references CustomMetadataset(customset_name)
);

--
-- fill tables CustomMetadataset and CustomMetadatasetLines
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg-full','isadg-struct.cfg');
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg-alias','isadg-struct.cfg');

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
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-alias',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:Descriptionsdate','');

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
INSERT INTO CustomMetadatasetLines(customset_name,rowcount,screen_label,tag,default_value) VALUES('isadg-full',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:DescriptioncontrolDescriptionsdate','');



--
-- Create a myLenses table
-- lens_params = '-exif:lensmake -exif:lensmodel -exif:lensserialnumber -exif:focallength -exif:focallengthIn35mmformat -exif:fnumber -exif:maxaperturevalue -exif:meteringmode'

Create table if not exists myLenses (
    id integer primary key autoincrement,
    parent_id integer,
    lens_name text NOT NULL unique,
    lens_description text,
    exif_lensmake text,
    exif_lensmodel text,
    exif_lensserialnumber text,
    exif_focallength text,
    exif_focallengthIn35mmformat text,
    exif_fnumber text,
    exif_maxaperturevalue text,
    exif_meteringmode text,
    makernotes_focusdistance text,
    composite_lensid text,
    makernotes_conversionlens text,
    makernotes_lenstype text,
    makernotes_lensfirmwareversion text,
    UNIQUE (parent_id, lens_name)
);
--
-- Add some example data in the myLenses table
--
insert into myLenses(lens_name, lens_description, exif_lensmake, exif_lensmodel, exif_lensserialnumber, exif_focallength, exif_focallengthIn35mmformat, exif_fnumber, exif_maxaperturevalue, exif_meteringmode, composite_lensid, makernotes_conversionlens, makernotes_lenstype, makernotes_lensfirmwareversion)
values('Panasonic Leica DG Summilux 25/f1.4','some example','Panasonic','Leica DG Summilux 25/f1.4','123456-ABC','25','50','1.4','22','Multi-segment', 'Leica DG Summilux 25/f1.4', ' Off', 'LUMIX G VARIO 14-140/F3.5-5.6', '0.1.0.0' );


--
-- Create table for userfavorites
--
Create table if not exists userFavorites (
    id integer primary key autoincrement,
    favorite_type text NOT NULL,
    favorite_name text NOT NULL,
    command_query text NOT NULL,
    UNIQUE (favorite_type, favorite_name)
);
