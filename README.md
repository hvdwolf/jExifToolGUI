# jExifToolGUI
**jExifToolGUI is a java/Swing graphical frontend for the excellent command-line ExifTool application by Phil Harvey.**


**ExifTool** is a platform-independent Perl command-line application and library for reading, writing and editing meta information in a wide variety of files.<br>
[Exiftool](https://www.sno.phy.queensu.ca/~phil/exiftool/) is by far the best and most extensive metadata tool available!<br>
It reads and writes metadata information from/to many files, but primarily focuses on photos/images. It reads/writes exif, gps, xmp, maker notes for many cameras, JFIF, GeoTIFF, ICC Profile, Photoshop IRB, FlashPix and many, many more tags.<br>
It can use a "reference" image to write the tags to a multiple set of photos or a complete directory containing photos.<br>
Author/creator/maintainer of ExifTool: **Phil Harvey**.
  
**jExifToolGui** is a java/Swing program that functions as a graphical frontend (GUI) for [exiftool](http://www.sno.phy.queensu.ca/~phil/exiftool/).<br><br>
Many thanks go to Phil Harvey for his excellent tool. Without ExifTool, this Graphical frontend for ExifTool would never have existed.<br><br>
jExifToolGUI is (just) a graphical frontend for ExifTool. It can use a "reference" image to write the tags to a multiple set of photos, like gps tags for photos that were taken at the same location but somehow miss or contain the incorrect gps info.<br>
jExifToolGUI also contains extensive renaming functionality based on the exif info in your photos.<br>
This jExifToolGUI program is free, Open Source software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
<br><hr>

<br><br>jExifToolGUI is a remake of [pyExifToolGUI](https://github.com/hvdwolf/pyExifToolGUI) ([website](https://hvdwolf.github.io/pyExifToolGUI/)). I discontinued pyExifToolGUI for a couple of reasons:
* pyExifToolGUI uses python/pySide/QT4. pySide/QT have undergone some changes over the years that require quite some rework. pySide has also changed the license. The combination is cross-platform but not so easy to package for Windows and MacOS.
* I started programming in java because I wanted "things" on my Android car head unit that nobody else would make. And now I prefer java for "bigger things" over python.
* Java comes with the builtin Swing gui. I don't need to package any dependencies. The user downloads java and jExiftoolGUI and that's all.
* Java/Swing supports many more platforms than pySide/QT.

## Developers
The UI is designed with the GUI Designer from IntelliJ IDEA. Read [readme-intelliJ.md](readme-intellIJ.md)
