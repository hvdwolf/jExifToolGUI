# Changelog


## 2021-10-22 1.9.0
* DROP SUPPORT FOR JAVA-8. java-8 does not support tiff. I used an external lib (twelvemonkeys) to support tif, but twelvemonkeys also moves on and tiff support using java-8 gets more and more cumbersome. Java-11 and up supports tiff out of the box. Dropping Twelvemonkeys also means that pgm/pict/tga is no longer supported (but who uses that anyway).
* ["feature request" #156](https://github.com/hvdwolf/jExifToolGUI/issues/156): Create setting where users can define their own font and font size for the application to accomodate higher (hdpi) resolutions.
* ["feature request" #117](https://github.com/hvdwolf/jExifToolGUI/issues/117): Option to "switch of" thumbnail generation and metadata reading when loading images. This can speed up the image loading 400% to almost instantly. When switching off "Create previews", always one preview is created in the bottom-left. Either the first of the loaded set, or the image selected from the list.
* Added option in the "Export/Import" pane and the "Exiftool Commands" pane to specify a folder instead of loading previews. Especially when dealing with large amounts of images, this is really fast when comparing it with first loading the images, creating previews. Next to that: some exiftool commands work faster when done on a folder in comparison to "image by image".
* Display the source folder in the bottom notification bar. It already displayed the number of loaded files. It now displays the folder as well. Note: This will not work correctly when dropping files from multiple folders on the program, or specifying multiple folders/files on the command line.
* Support fast-scaling when resizing. As java-8 is no longer supported I can use higher level image resizing/rescaling functionality. This should give some 5-15% performance improvement on loading.
* Support for photoshop thumbnails added. Photoshop tiffs containing these thumbnails now load the preview at least 25~50x faster (because we no longer need to resize the entire sometimes 45+ MB image).
* Search in metadata of loaded images (added a magnifying glass button). You can search on tag and on value. Images that contain search results can be reloaded.
* Add vrae config and vrae custom metadataset
* Display filename/path above metadata panel
* Add menu option for "set file date to create date" for mp4/mov movieclips
* [MacOS] Fix ["issue #118"](https://github.com/hvdwolf/jExifToolGUI/issues/170): "sips conversion of heic images". Only the first occurrence of multiple spaces in path/filename were escaped.
* Updated splash logo to display Exif, GPS and XMP in uppercase instead of Exif, gps and xmp.
* Fix null pointer assignment in geotagging
* Fix Set to DateTimeOriginal: spaces in exiftool path caused error
* Fix ["issue" #110](https://github.com/hvdwolf/jExifToolGUI/issues/110) (windows): If PATH contains multiple exiftool versions, jExifToolGUI crashes. Fixed now.
* Fix ["issue" #116](https://github.com/hvdwolf/jExifToolGUI/issues/116) (windows): ExifTool command will not run if exiftool.exe directory contains a space.
* ["issue" #100](https://github.com/hvdwolf/jExifToolGUI/issues/100): Add "-m" parameter to Edit->GPS tab to allow for longer than 32 character strings for IPTC. Make it checked by default.
* ["feature request" #124](https://github.com/hvdwolf/jExifToolGUI/issues/124): Convenient localization (translation) using Weblate
* GPS: Add "Add location to makernotes". Many camera's have location,country,state,city as makernotes. Exiftool will automatically put it in the correct Panasonic/Canon/Nikon/etcetera tags.

## 2020-12-09 1.8.1
* [issue #99](https://github.com/hvdwolf/jExifToolGUI/issues/99): 3 issues > GPS edition.
* [issue #98](https://github.com/hvdwolf/jExifToolGUI/issues/98): Focal length in 35 mm shown as null. Fixed for cameras that do not provide the "FocalLengthIn35mmFormat". All cameras provide the "ScaleFactor35efl". Combined with the "FocalLength" one can calculate the "FocalLengthIn35mmFormat".

## 2020-12-04 1.8.0
* GPS: Add internal OpenStreetMap Nominatim "Find location" with map viewer with "copy coordinates" option back to main GPS tab. (External web search still available)
    * Search method for address/POI/place etc. Reverse search via coordinates when right-clicked on map.
* GPS: make "mini-"tab on GPS tab to enable input of decimal degrees (primary tab) or Deg-Min-Sec coordinates. (Coordinates are copied back as decimal degrees and dec-min-sec)
* Fix part 1 of [issue #95](https://github.com/hvdwolf/jExifToolGUI/issues/95): Some commands resulted in errors.
* Fix part 2 of [issue #95](https://github.com/hvdwolf/jExifToolGUI/issues/95): Fix (again) issue with spaces in paths/names in "Renaming images".
* "User defined Metadata Combis" always started with alphabetically first retrieved set from the database. Now starts with 10 empty rows.
* Add "Preferences -> system ->Sort Categories/Tags in View tab alphabetically". Exiftool reads the tags in the order they are stored in the image. This option will allow you to sort them first before display.
* Show popup to users which files have been created upon exporting the several options.
* Fix lens functionality in case users use spaces (by accident) before or after a name. And "fix the fix" (from 1.6.3.1) in the "no description" field.

## 2020-11-14 1.7.1
* Add "Remove metadata by category". Use the same drop-down as in the main screen to be able to select any possible category and delete the metadata in that category.
* Add Spanish online manual (Thanks to mrtngrsbch martingggg)
* Fix (Linux) [issue #84](https://github.com/hvdwolf/jExifToolGUI/issues/84): Fix (stupid) typo in appImage PATH statement.
* Fix/enhancement (MacOS): [issue #85](https://github.com/hvdwolf/jExifToolGUI/issues/85): decoupled menu behavior on MacOS. The menu is now (finally) correctly integrated with the MacOS system menu bar.
* Fix [issue #83](https://github.com/hvdwolf/jExifToolGUI/issues/83): Export sidecar metadata failed for images with spaces in paths/file names.
* Move Sidecar exports from menu to "Export/Import" tab.

## 2020-11-10 1.7.0
* Add new "compare images metadata screen" screen where you can [compare the metadata of images](https://youtu.be/TvvtIIa50QA). The selection options on the main screen (all, common tags, by group, etc.) can be used. You can select and compare up to 25 images.
    * From compare screen an "Export to pdf" and "Export to csv" option. The second with "one csv per image" or "all images into one csv".
* Text buttons changed to "iconified" buttons with "tooltips" when you hover over them (material design icons).
* Add "Help -> Youtube instruction videos". Opens browser to playlist with sort 1-3 minutes instruction videos. Will be expanded in future.
* Add command line parameters/arguments handling. Supports (multiple) folder(s), multiple file(s) and combinations. Supports releative paths and symbolic links (Linux/MacOS). **Note:** The MacOS app bundle does not support command line arguments as Apple is really picky on this.
See also the Youtube video [Loading images](https://youtu.be/ytgnbJBAQ4g) about the several ways of loading images.
This functionality does NOT support recursively parsing sub-directories as that could lead to tens of thousands of files which might not be a problem for a commandline tool, but is a problem for a Gui needing to load the files.
Note: In windows this also means that you can drag&drop images onto the "closed" jExifToolGUI.exe in the file manager, and it will open and load the files.
    Examples: 
    `java -jar jExifToolGUI.jar ../../Pictures/Summer2015  a.jpg  b.png  /home/pipodeclown/screenshots/*.jpg  ../c.cr2  d.nef`<br>
    `jExifToolGUI.exe Pictures\Malaga2012 c:\tmp\pics\*.jpg c:\tmp\pics\*.RAF a.jpg d:\stuff\b.jpg`<br>
* Change/Expand default imageviewer. Now always fullscreen (also for smaller than screen images) with "close/previous/next" buttons and "Escape/key-left/key-right" keylistener to loop through the loaded images (loaded images, not (only) selected images).
* Option to select ["dual column" or "single column" thumbnail panel](https://youtu.be/SMv1IFesJcA) (from Preferences -> Look & Feel). The left thumbnail panel will auto-size to "two-column width" when necessary.
    * single column: thumbnail plus file name (top - bottom)
    * dual column: thumbnail left, basic image data right (basic image data is new, was only file name).
* Add "SplitPane" bar to divide left & right screen. User can shift the bar and the left/right/top/bottom according to the users preferences.
* Remember GUI width/height and "splitPane" settings upon exit.
* Renamed tab "Your Commands" to "Exiftool Commands"
* Remove "Exiftool Database" from the tab in the main screen as it does not act on images. Put it under the "Tools" menu and give it its own screen.
* Restructure and expand "Copy Data" tab
    * Sub-tab "From image to image": This is the original tab where you can copy metadata from one image to multiple images.
    * Sub-tab "Inside same image" (new)
        * All metadata to xmp: This copies all tags from all categories to xmp format, when possible (Moved from (menu) "Metadata -> Copy all metadata to XMP format" to here).
        * From multiple categories to category (To Exif, To XMP, To IPTC, To GPS, To PDF)
* Add new Export/Import tab (currently only Export)
    * Move (menu) Other -> export metadata to new Export tab under "General Export"
    * Create "Export to PDF"
* [Export to pdf](https://youtu.be/N0yQNEDC-q4). The image plus metadata is written to a pdf. One image + metadata per pdf, or everything in one big pdf document.
* Fix: [issue #80](https://github.com/hvdwolf/jExifToolGUI/issues/80) Spaces in tags in "Your Commands"  lead to incomplete tags.
* Program checked for selected images, but did not always check if images had been loaded at all before menu selection or button push: resulted in null value errors.
* Fix Linux/MacOs: "Copy all tags to XMP": Fix issue with spaces in path.
* Fix: rotation of RAW images in thumbnails.




## 2020-10-23 1.6.3.1
Bugfix release only:
* Fix: [issue #78](https://github.com/hvdwolf/jExifToolGUI/issues/78) and [issue #57](https://github.com/hvdwolf/jExifToolGUI/issues/57) the default translations file contained a number of incorrect characters which made it impossible to use the "Load Images" and "Load Folder" options on all systems NOT running English, German, Spanish or Dutch. This seems like some sort of text file corruption but I do not know how it occurred. It is fixed now.
* Lens configurations: Saving of lens descriptions is optional, but retrieval expected it to be there. That now works correct.

## 2020-10-04 1.6.3
* MacOS Feature request: [issue #35](https://github.com/hvdwolf/jExifToolGUI/issues/35) heic (heif) thumbnails and full screen / full size view are now supported using sips as converting engine. Unfortunately it is not really fast (I am now on MacOS Catalina and could finally test myself and make it work).
* Feature request: [issue #72](https://github.com/hvdwolf/jExifToolGUI/issues/72) Preserve file modification date/time. (Preferences -> system)
* MacOS: Fix/enhancement: [issue #74](https://github.com/hvdwolf/jExifToolGUI/issues/74) Improve working of "Load Folder".
* Feature request: [issue #70](https://github.com/hvdwolf/jExifToolGUI/issues/70) Add user defined file filter (Preferences -> general). If you add one or more extensions, the user defined filter becomes the default. Note: The jfilechooser supports "dynamic filters" which means you can change it on the fly. The old Awt Filedialog requires you to remove the user defined filter values to regain "normal" behavior.
* Feature request: [issue #68](https://github.com/hvdwolf/jExifToolGUI/issues/68) Do not write to "exif:Software". Some camera brands use this for own purposes.
* Fix: Restore proper functioning choosing JFilechooser/AWt dialog in system Preferences (bug "introduced" in 1.6.0).

## 2020-10-01 1.6.2
* Speed up loading of thumbnails with approx. 25% for JPGs by first trying to use an embedded thumbnail.
* Display preview for MP4 movies as recorded by modern cameras/phones as they contain a preview image (Not for any other (downloaded) MP4 or mkv, or created from a Video editor)
* Fix: Edit tab -> Lens tab: "Focal length" and "focal length in 35 mm" were not correctly trimmed. (Some cameras place spaces before and after the focal length to make sure that programs can read it as a string.)

## 2020-09-28 1.6.1
* (Menu) Help -> Online manual added. I'm slowly working on an online manual (in English).
* Added sidecar exports of MIE (["The only metadata format that doesn't suck"](https://exiftool.org/commentary.html)) and EXV (next to xmp and exif). *(All Sidecar files can be read as "images". This makes it possible to read (for example) a .mie file together with a number of images. You use the .mie metadata file as "Copy from selected image" and use it to populate one or more images. For this reason there is no import function for these Sidecar files as you can use them as just described.)*
* Fix Preferences screen: Somehow 2 options were "falling off the screen": "Credits" under "Always add.." and "Check for new jExifToolGUI version on program start" (under System).

## 2020-09-20 1.6.0
* New menu "Tools"
    * User defined Metadata combis
    * delete Lenses
    * delete Favorites
* Add option to create your own combination of tags in "Tools -> User defined Metadata combis" to write to your images (see help page [here](https://hvdwolf.github.io/jExifToolGUI/manual/jexiftoolgui_usercombis.html) or via the help button. This has been extended with the option to also use tags that are not known to Exiftool via a custom config file: Create your custom config file, Create your own custom tag combination and add the config file when saving your combination. This allows for example, to add your own set of tags to XMP, Exif, IPTC, Composite, etc. Exiftool and jExifToolGUI will displays the tags, but you need to have the custom config file to write them as well. As examples: [see the Exiftool example](https://exiftool.org/config.html) or [see the isadg-struct.cfg](https://raw.githubusercontent.com/hvdwolf/jExifToolGUI/master/src/main/resources/isadg-struct.cfg) online to add ISAD(g) data to the XMP set as new category xmp-isadg. (ISAD(G) is General International Standard Archival Description)
    * "pre-installed" custom metadata sets (for use, as example and for modification (save as)):
        * isadg: like above mentioned
        * gps_location: All gps and location tags in the 3 categories EXIF,XMP and IPTC.
        * Google Photos: All tags that Google Photos uses or recognizes.
    * Common Tags in the View Tab now also include your own defined "User defined Metadata combis" by name. 
        * Note: If you added your custom tags under one of the existing categories like XMP, Exif, IPTC, etcetera, they will also be displayed there.
    * Export Metadata (Menu Metadata -> Export Metadata) now supports the export by category (exif, xmp, etc.) but also from our user defined metadata combi set.
* Add drag & drop of images. Drop them anywhere on the main program window and they will be loaded into the application.
* Preferences -> System: "Show coordinates in decimal degrees (Exiftool defaults to Deg Min Sec)"
* Preferences -> System: "Use G1 group instead of G for viewing": G will show main category like EXIF or XMP. G1 will show sub groups like for EXIF it will show ExifIFD, IFD0, etc. and for XMP like XMP-x, XMP-exif, XMP-dc, XMP-isadg, etcetera.
* Preferences -> System: Make log level user definable for troubleshooting.
* In the tab "Your commands": If you specify "-h" or "-htmlFormat", the output will switch to html instead of "plain text"
* MacOS: Apple likes it big. The application starts with a bigger window on MacOS to show all GUI components. 
* Feature request: [issue #66](https://github.com/hvdwolf/jExifToolGUI/issues/66): images count. An image counter is now displayed in the bottom left showing how much images have been loaded.
* (Internal) Better separation between Gui elements and program logic.
* Fix: [issue #60](https://github.com/hvdwolf/jExifToolGUI/issues/60) Fix 2 combined errors. There was a check whether exiftool "delivers" data, but not if the first line contains a warning or error in case of faulty metadata data. This resulted in "hanging" on loading a file, or hanging when requesting specific info from a file.
* Fix: fix export metadata (again).

## 2020-08-26 1.5.2
* Fix: (old) error in exiftool detection when exiftool is removed or moved to other folder.
* "Fix" (Linux KDE/QT): KDE/QT based window managers deliver "strange" strings for imagewidth and imageheight out of exiftool. The values are not integers and can't be converted to integers either. Now using java based approach, and better "error trapping" in case it still errors out.
* Logging to file enabled. In the folder where jExifToolGUI is started, a folder "logs" is created where log files are stored per jExifToolGUI session. This does not work for MacOS bundles. Therefore the logs are written inside a logs folder into the user HOME folder. In case of issues extensive logging can be started to pinpoint the issue.

## 2020-08-16 1.5.1
* Fix error in git repository. No influence on released apps.

## 2020-08-15 1.5.0
* Make app translatable: Change (almost) all "fixed" texts to "property based" translatable texts.
    * Translated into German => Big thanks to Karsten Günther.
    * Translated into Spanish => Big thanks to Martin Gersbach.
    * Translated into Dutch.
    * Preferences Language tab: Select your language of preference. By default in English if jExifToolGUI is not translated for your language yet.  But you can select Spanish, German or Dutch if your system is in Chinese ;) 
* Preferences -> Look & Feel tab: File dialog (open/select files/folder) Option to select JFileChooser (shows same dialog on all systems) and Awt Filedialog (looks more "native Operating system" like)
* Added the TwelveMonkeys imageIO library. Adds (additional) display support for bmp/jpeg/jpeg-2000/PNM (PBM/PGM/PPM/PAM)/PSD/TIFF/HDR/IFF/PCX/PICT/SGI/TGA/SVG/WMF
* Complete rewrite of previews (with correct aspect ratio and rotation) and image view. Change default viewer to internal java viewer ("Display Image" or double-click preview). It will show the image resized to full-screen (unless smaller). RAW images are displayed in "external" RAW Image viewer (if configured in Preferences). 
* MacOS: Use sips on "High Sierra" and newer to convert heic to a jpg preview (and image view). This is very alpha and mostly doesn't work.
* Preferences window now "tabbed". Currently pretty empty but supposed to grow.
* Fix: [issue #53](https://github.com/hvdwolf/jExifToolGUI/issues/53) xmpexport missing? Over the years this has changed to "Sidecar exports". Now added under (menu) Metadata: "Export Exif sidecar file(s)" and "Export XMP sidecar file(s)".(Known shortcoming: currently only works on paths and images without spaces. It does work on other places: why not here? something in exiftool?)
* Fix: [Issue #54](https://github.com/hvdwolf/jExifToolGUI/issues/54) Repair csv output (export metadata).
* Implemented [Issue #51](https://github.com/hvdwolf/jExifToolGUI/issues/51) Reverse buttons in Rename Photos window
* Fix (MacOS): [Issue #40](https://github.com/hvdwolf/jExifToolGUI/issues/40) Load directory don't work on OSX.
* Implemented [Issue #43](https://github.com/hvdwolf/jExifToolGUI/issues/43) Edit Data->Gpano layout
* Fix: RAW viewer could not be called correctly when path or file name contains spaces.
* Fix: UTF-8 was not used in Preferences dropdown for "Language to use to display metadata tag names/descriptions".
* Fix: UTF-8 was not used for output in "View data" tab in the table table and neither in "Your commands".


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
