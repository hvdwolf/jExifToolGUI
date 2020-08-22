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
