# About the database

**SQLite** is used as database for this application via the sqlitejdbc java driver.

**Why** a database?
I have a long history in databases and always struggle somewhat with the struture of how exiftool displays all its families, groups and tags via
the several "-list" options. So I put it in a database to be used within this application.
*Note: The database only contains the metadata tags. Not the translated descriptions*

**Why** SQLite?
SQLite, and in this case SQLite3, is a very simple, yet powerful local database. It is easy to copy around. There is the SQLite3 cli client for many 
platforms, and the official cross-platform graphical sqlitebrowser (DB browser for SQLite). There are a few more graphical browsers.<br>
Next to that: the sqlitejdbc jar is well-known, be it not the smallest.

**How is the database built?**
There is a "create_fill_db.sh" shell script that calls 2 "fixed" sql scripts (create_jexiftoolgui_db.sql and custom.sql), and builds and uses several dynamic sql scripts to build the database.
requirements: sqlite3, exiftool, bash shell. Of course the database is built based on the exiftool version and its added features.<br>
*Note: Creating the database can take anything from 15 minutes to 60 minutes (or more) depending on CPU and "old fashioned" spinning hard-disks versus SSDs.*

**Querying the database**
This is of course done inside the application but can also be done outside the application simply using the jexiftoolgui.db and sqlite3 cli of graphical browser.

**Some query examples**
*Note: below queries are "old fashioned" queries. They could also be built as inner/outer/left/right join queries.*<br>
= Show tags for a certain group like APE or EXIF or XMP or Panasonic<br>
`select taggroup,tagname from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and Groups.taggroup='APE';`

= Find to which group(s) a tag belongs. Use wild cards as we do not exactly know what the tagname is.<br>
`select taggroup,tagname,tagtype,writable from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and tags.tagname like '%aperture%';`

= Show writable tags for a certain group like APE or EXIF or XMP or Panasonic<br>
`select taggroup,tagname from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and Tags.writable=1 and Groups.taggroup='APE';`<br>
= Or the "Show writable tags for a certain group" in advanced form<br>
`select taggroup,tagname,case when tags.writable is not null then 'Yes' else 'No' end as isWritable from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and Groups.taggroup='APE';`
which will show a "Yes" or "No" in the column "isWritable" like<br>
```
taggroup|tag|isWritable
APE|APEVersion|No
APE|Album|Yes
APE|Artist|Yes
``` 

= Show whichs groups belongs to which family<br>
`select family,famgroup from family,familygroups where family.id=familygroups.family_id and family='g2';`

= Show all tags in a family (= all tags from all groups belonging to family xy)<br>
`select family,famgroup,tagname from family,familygroups,groups,tags,tagsingroups where family.id=familygroups.family_id and tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and family='g2';`


**Build the databse yourself**
Requirements: exiftool, sqlite3, some linux/unix (MacOS)
files: create_fill_full_db.sh, create_exiftoolgui_db.sql, convertxml2csv.py


