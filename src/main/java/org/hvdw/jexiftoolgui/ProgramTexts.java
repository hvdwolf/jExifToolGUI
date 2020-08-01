package org.hvdw.jexiftoolgui;

public class ProgramTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
    All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/jExifToolGUI";
    public static final String Version = "1.4.1";
    public static final String HTML = "<html><body style='width: %1spx'>%1s";
    public static final String aboutText =
            "<big>jExifToolGUI</big><hr><br>"
            +"<strong>jExifToolGui</strong> is a java/Swing program that functions as "
            +"a graphical frontend (GUI) for <a href='http://www.sno.phy.queensu.ca/~phil/exiftool/'>exiftool</a>.<br><br>"
            +"Exiftool is written and maintained by Phil Harvey. Many thanks go to him for this excellent tool. Without ExifTool, "
            +"this Graphical frontend for ExifTool would never have existed.<br><br>"
            +"jExifToolGUI is (just) a graphical frontend for ExifTool. "
            +"It can use a \"reference\" image to write the tags to a multiple set of photos, like gps tags for photos"
            +" that were taken at the same location but somehow miss or contain the incorrect gps info.<br>"
            +"jExifToolGUI also contains extensive renaming functionality based on the exif info in your photos."
            +"<br>This jExifToolGUI program is free, Open Source software: you can redistribute it and/or "
            +"modify it under the terms of the GNU General Public License "
            +"as published by the Free Software Foundation, either version "
            +"3 of the License, or (at your option) any later version."
            +"<br>This program is distributed in the hope that it will be useful, "
            +"but WITHOUT ANY WARRANTY; without even the implied "
            +"warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR "
            +"PURPOSE.  See the GNU General Public License for more details."
            +"<br>You should have received a copy of the GNU General Public "
            +"License along with this program. If not, see <a href='http://www.gnu.org/licenses'>www.gnu.org/licenses</a>.</p>"
            +"<br><br>jExifToolGUI version: " + Version + "."
            +"<br><br>Author/creator of ExifTool: Phil Harvey."
            +"<br><br>Author/creator of jExifToolGUI: " + Author + ".";
    public static final String aboutExifToolText =
            "<big>ExifTool</big><hr><br>"
            +"ExifTool is a platform-independent Perl command-line application and library for reading, writing and editing meta information in a wide variety of files.<br> "
            +"Exiftool is by far the best and most extensive metadata tool available!<br><br>"
            +"It reads and writes metadata information from/to many files, but primarily focuses on photos/images. It reads/writes exif, gps, xmp, maker notes for "
            +"many cameras, JFIF, GeoTIFF, ICC Profile, Photoshop IRB, FlashPix and many, many more tags.<br>"
            +"It can use a \"reference\" image to write the tags to a multiple set of photos or a complete directory containing photos.<br>"
            +"<br><br>Author/creator/maintainer of ExifTool: Phil Harvey.<br><br>";
    public static final String CreditsText =
            "<big>Credits</big><hr><br>"
            +"This is currently a very short list, but nonetheless really essential<br><br>"
            +"<b>David Däster:</b> Bringing structure into this project. Building the facades, adding the logger, creating the idea/gradle integration. I started as python programmer, hardly knowing anything of java, "
            +"and he helped, educated and corrected me in many areas (although I undoubtedly made again many errors afterwards).<br>Thanks for all your support!<br><br>";
    public static final String noExifTool = "<b>I can't find exiftool in your PATH and it is not specified in the preferences either.<br>"
            +"I can't continue without it.</b><br><br>"
            +"You have a few options:<br>"
            +"<ul><li>Linux: Install it via your distribution's package manager (but this might be a (slightly) older version), or download the latest from Phil Harvey's exiftool website.</li>"
            +"<li>Windows: Download the latest version from Phil Harvey's exiftool website.</li>"
            +"<li>Mac OS/X: Install it via MacPorts or Homebrew (but this might be a (slightly) older version), or download the latest from Phil Harvey's exiftool website.</li></ul>"
            +"Use one of the below options.";
    public static final String cancelledETlocatefromStartup = "<html>You cancelled providing the location of exiftool.<br>I will now exit jExifToolGUI.</html>";
    public static final String downloadInstallET = "I will now open the ExifTool website in your browser and then close jExifToolGUI.<br>"
            +"After having downloaded and installed ExifTool you can reopen jExifToolGUI.<br><br>If ExifTool is in your PATH, jExifToolGUI will simply continue.<br><br>"
            +"If ExifTool is NOT in your PATH, you need to specify the location where you installed ExifTool.";
    public static final String wrongETbinaryfromStartup = "<html>This is not the exiftool executable/binary.<br>I can't use it and will now exit jExifToolGUI.</html>";
    public static final String CreateArgsMetaDataUiText = "Which metadata from your selected image(s) do you want to add to your args file(s)?";
    public static final String NoOptionSelected = "You did not select one single option. Cancel would have been the correct option. I will not do anything.";
    public static final String GpanoSetSaveCheckboxes = "Do not forget to set the \"Save\" checkboxes\n upon saving, when relevant.";
    public static final String NotAllMandatoryFields = "<html>At least one of the manadatory fields is not complete.<br>Please complete the missing mandatory data.<br><br>I can't continue without it.</html>";
    public static final String ExportPreviewsThumbnails = "This option will export all preview images and thumbnails from the selected images. This can only be done if your image(s) do have them.<br>"
            +"Do you want to continue?";
}
