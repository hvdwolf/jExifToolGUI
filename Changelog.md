# Changelog

## 2020-07-17 1.3.0
* Under Help menu: add "System/Program Info" to show some info (in case we need to troubleshoot).
* Add "XMP_IPTC_Strings+" tab. Currently contains Keywords, Subject and PersonInImage. Allows for more fine-grained options then now in XMP-tab (and might be extended with more).
* Add "simple"xmp-pdf:keywords to XMP tab
* Fix: multiple errors in XMP tab
* Add xmp:credit to the defaults.
* Fix: Copy defaults on Exif & XMP (copy Artist/Creator, Credit and Copyrights back in if somehow removed)
* embed splash screen in jar; also works on windows

## 2020-07-15 1.2.0
* Add button "Load Directory" and (menu) "File -> Directory"
* [Issue #32](https://github.com/hvdwolf/jExifToolGUI/issues/32): First remark -> field size.

## 2020-07-13 1.1.0
* Internal: Go back to good old Linux/Unix versioning schema.
* Add work-around for displaying RAW images based on Thumbnail/PreviewImage.
* housekeeping: (re)create temp work folder on program start and delete (incl. contents) on program exit.
* Add splash screen to Linux and Mac versions. (Windows versions crash for some reason)
* Fix: debian .deb package. Icon name incorrect in jexiftoolgui.desktop.
* add: Extract all previews/thumbs from selected image(s): "(menu) Other -> Export all previews/thumbs from selected".

## 2020-07-11 1.01-beta
* Fix (stupid) mistake in parameters for gps and location view

## 2020-07-10 1.00-beta
* Add "Edit lens tab". Enables to save (additional) lens data and to create and save lens configurations for future use. 
* add SQLite database with exiftool tags, groups, families.
* Reconfigure menu.
* Make compatible again with java 8. Split MacOS and Windows in a "with jre" and a "without jre" package (for those downloads with included jre, the jre version is still V 11).
* Add "lens data" view option under "Common Tags".
* Add exiftool supported languages for displaying metadata tags (also in "export metadata"). See under Preferences.


## 2020-06-29 0.99-beta
* Initial (beta) release
