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
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('isadg','isadg-struct.cfg');
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('gps_location','');
INSERT INTO CustomMetadataset(customset_name,custom_config) VALUES('Google Photos','');

INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',0,'Identity Reference','xmp-isadg:IdentityReference','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',1,'Identity Title','xmp-isadg:IdentityTitle','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',2,'Identity Date','xmp-isadg:IdentityDate','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',3,'Identity DescriptionLevel','xmp-isadg:IdentityDescriptionLevel','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',4,'Identity Extent','xmp-isadg:IdentityExtent','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',5,'Context Creator','xmp-isadg:ContextCreator','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',6,'Context Adminbiohistory','xmp-isadg:ContextAdminbiohistory','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',7,'Context Archivalhistory','xmp-isadg:ContextArchivalhistory','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',8,'Context Acqinfo','xmp-isadg:ContextAcqinfo','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',9,'Contentstructure Scopecontent','xmp-isadg:ContentstructureScopeContent','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',10,'Contentstructure Appraisaldestruction','xmp-isadg:ContentstructureAppraisaldestruction','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',11,'Contentstructure Accruals','xmp-isadg:ContentstructureAccruals','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',12,'Contentstructure Arrangement','xmp-isadg:ContentstructureArrangement','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',13,'Conditionsaccessuse Accessrestrictions','xmp-isadg:ConditionsaccessuseAccessrestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',14,'Conditionsaccessuse Reprorestrictions','xmp-isadg:ConditionsaccessuseReprorestrictions','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',15,'Conditionsaccessuse Languagescripts','xmp-isadg:ConditionsaccessuseLanguagescripts','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',16,'Conditionsaccessuse Phystech','xmp-isadg:ConditionsaccessusePhystech','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',17,'Conditionsaccessuse Findingaids','xmp-isadg:ConditionsaccessuseFindingaids','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',18,'Alliedmaterials Existencelocationoriginals','xmp-isadg:AlliedmaterialsExistencelocationoriginals','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',19,'Alliedmaterials Existencelocationcopies','xmp-isadg:AlliedmaterialsExistencelocationcopies','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',20,'Alliedmaterials Relatedunits','xmp-isadg:AlliedmaterialsRelatedunits','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',21,'Alliedmaterials Publication','xmp-isadg:AlliedmaterialsPublication','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',22,'Notes Note','xmp-isadg:NotesNote','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',23,'Descriptioncontrol Archivistsnote','xmp-isadg:DescriptioncontrolArchivistsnote','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',24,'Descriptioncontrol Rulesconventions','xmp-isadg:DescriptioncontrolRulesconventions','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('isadg',25,'Descriptioncontrol Descriptionsdate','xmp-isadg:DescriptioncontrolDescriptionsdate','');

INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',0,'GPS Version ID','EXIF:GPSVersionID','2.3.0.0');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',1,'Latitude ref','EXIF:GPSLatitudeRef','North');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',2,'Latitude','EXIF:GPSLatitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',3,'Longitude ref','EXIF:GPSLongitudeRef','West');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',4,'Longitude','EXIF:GPSLongitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',5,'Altitude ref','EXIF:GPSAltitudeRef','Above Sea Level');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',6,'Altitude ref','EXIF:GPSAltitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',7,'GPSMapDatum','EXIF:GPSMapDatum','WGS-84');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',8,'Location','XMP:Location','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',9,'xmp altitude','XMP:GPSAltitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',10,'xmp latitude','XMP:GPSLatitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',11,'xmp longitude','XMP:GPSLongitude','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',12,'City','XMP:City','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',13,'Country','XMP:Country','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',14,'State','XMP:State','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',15,'iptc City','IPTC:City','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',16,'sub-location','IPTC:Sub-location','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',17,'Province-State','IPTC:Province-State','');
INSERT INTO CustomMetadatasetLines(customset_name, rowcount, screen_label, tag, default_value) VALUES('gps_location',18,'Country Primairy name','IPTC:Country-PrimaryLocationName','');

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
