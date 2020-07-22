--
-- Create the metadata part of the DB
--
create table if not exists ExiftoolVersion (
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
    writable text
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
  G0 text,
  G1 text,
  G2 text
);

create view if not exists v_tags_groups as select taggroup,tagname,tagtype,writable from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id;
--
-- Create DB part for the custom "things"
--
Create table if not exists CustomView (
    id integer primary key autoincrement,
    viewname text NOT NULL UNIQUE,
    description text,
    unique (id, viewname)
);

Create table if not exists CustomViewLines (
    id integer primary key autoincrement,
    parent_id integer,
    displayname text NOT NULL,
    tag text NOT NULL,
    description text,
    UNIQUE (parent_id, displayname),
    foreign key(parent_id) references CustomView(id)
);

Create table if not exists CustomEdit (
    id integer primary key autoincrement,
    editname text NOT NULL UNIQUE,
    description text,
    unique (id, editname)
);
Create table if not exists CustomEditLines (
    id integer primary key autoincrement,
    parent_id integer,
    displayname text NOT NULL,
    tag text NOT NULL,
    description text,
    foreign key(parent_id) references CustomEdit(id)
    UNIQUE (parent_id, displayname)
);
--
-- Add some example data in the CustomViewLines table
--
insert into CustomView(viewname,description) values('custom','default custom view');
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Make','-exif:Make','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Model','-exif:Model','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'LensModel','-exif:LensModel','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'ExposureTime','-exif:ExposureTime','[1/50] or [0.02]' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'FNumber','-exif:FNumber','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'ISO','-exif:ISO','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'FocalLength','-exif:FocalLength','[28] -mm not necessary' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'FocalLengthin35mmformat','-exif:FocalLengthin35mmformat','[200] -mm not necessary' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Flash#','-exif:Flash','[ 0 ]=>No flash, [ 1 ]=>Flash fired' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Orientation#','-exif:Orientation','[ 1 ]=>0°, [ 3 ]=>180°, [ 6 ]=>+90°, [ 8 ]=>-90°' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'DateTimeOriginal','-exif:DateTimeOriginal','[2012:01:14 20:00:00]' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'CreateDate','-exif:CreateDate','[2012:01:14 20:00:00]' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Artist*','-exif:Artist','Harry van der Wolf' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Copyright','-exif:Copyright','2019-2020©' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Software','-exif:Software','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Latitide','-Gps:-GpsLatitude','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Longitude','-Gps:-GpsLongitude','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Altitude','-Gps:-GpsAltitude','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Orientation/Type±','-xmp-dc:Type','[Landscape] or [Studio+Portrait] ..' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Event','-xmp-iptcExt:Event','[Vacations] or [Trip] ..' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'PersonInImage±','-xmp:PersonInImage','[Phil] or [Harry+Sally] or [-Peter] ..' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Keywords±','-xmp-dc:Subject','[tree] or [flower+rose] or [-fish] or [+bird-fish] ..' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Country','-xmp:Country','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Province','-xmp:State','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'City','-xmp:City','' from CustomView where viewname='custom';
insert into CustomViewLines(parent_id,displayname,tag, description) select CustomView.id,'Location','-xmp:location','' from CustomView where viewname='custom';

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