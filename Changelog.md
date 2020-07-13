# Changelog

## 2020-07-13 1.1.0 (silent version; no release)
* Add work-around for displaying RAW images based on Thumbnail/PreviewImage.
* housekeeping: (re)create temp work folder on program start and delete (incl. contents) on program exit.
* Add splash screen to Linux and Mac versions. (Windows versions crash for some reason)
* Fix: debian .deb package. Icon name incorrect in jexiftoolgui.desktop.
* add: Extract all previews/thumbs from selected image(s).

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
