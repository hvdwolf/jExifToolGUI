# Changelog

## 2020-07-26 1.4.1
* Fix: [Issue #38](https://github.com/hvdwolf/jExifToolGUI/issues/38) "Copy from selected image" adds leading space character for strings. All strings now "trim"-med, removing leading/trailing spaces.
* Fix: [Issue #42](https://github.com/hvdwolf/jExifToolGUI/issues/42) When running on java 9 or above. You get a warning: "WARNING: sun.reflect.Reflection.getCallerClass is not supported. This will impact performance". No problem at all and absolutely no performace impact (stupid log4j warning), but now the jar is converted to a "multi-version" jar.

## 2020-07-25 1.4.0
* Add "add favorites/load favorites" to "Your Commands" tab and to "Exiftool Database" tab. You can save your favorite exiftool commands and/or queries to the database.
* Add option to configure and use RAW image viewer: For all images (clunky on McOS as MacOS wants it own viewer), or only for raw images. Other formats (txt/audio/video/etc) will be opened according mime-type with their default app (if installed).
* Linux: Set default font to SansSerif instead of Linux/java default Dialog bold. (Will be configurable for MacOS/Windows/Linux in 1.5)
* Fix: [Issue #36](https://github.com/hvdwolf/jExifToolGUI/issues/36): Fix linewrap in exif:description and xmp:description.
* Fix: [Issue #37](https://github.com/hvdwolf/jExifToolGUI/issues/37): Tif(f) files not previewed. They are now differently handled based on java version.
    * tiff images **with** preview/thumbnail images inside are displayed (this was already working).
    * tiff images **without** preview/thumbnail inside can only be displayed on java V11 or higher. Use a "full jre" version (comes with V11) or install V11 or newer.
* Add "update" function. This new 1.4 does an extension on the database. You don't want to lose your user data (currently only lens configs if applicable)
* Internal: add program icon to all windows (top-left)(not on MacOS as MacOS does not allow it).
* Internal: DB diagram was displayed in external browser window. Is now displayed in internal scroll panel. It is "uncoupled" from the main window so you can keep it open.

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
