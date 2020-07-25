package org.hvdw.jexiftoolgui;

public class ProgramTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
    All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/jExifToolGUI";
    public static final String Version = "1.4.0";
    public static final String HTML = "<html><body style='width: %1spx'>%1s";
    public static final String newVersionText = "<html><big>There is a new version available</big><br><br>"
            +"I can open the releases webpage so you can download the new version.<br><br>"
            +"Open the website or not?";
    public static final String LatestVersionText = "<html>You are already using the latest version.</html>";
    public static final String aboutText =
            "<big>jExifToolGUI</big><hr><hr>"
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
    public static final String ETpreferenceIncorrect = "<html>The Exiftool specified in your preferences does not exist.<br>Did you move/rename/delete it?</html>";
    public static final String cancelledETlocatefromStartup = "<html>You cancelled providing the location of exiftool.<br>I will now exit jExifToolGUI.</html>";
    public static final String cancelledETlocatefromPrefs = "<html>You cancelled providing the location of exiftool.<br>I will leave the current preference for what it is.</html>";
    public static final String downloadInstallET = "I will now open the ExifTool website in your browser and then close jExifToolGUI.<br>"
            +"After having downloaded and installed ExifTool you can reopen jExifToolGUI.<br><br>If ExifTool is in your PATH, jExifToolGUI will simply continue.<br><br>"
            +"If ExifTool is NOT in your PATH, you need to specify the location where you installed ExifTool.";
    public static final String wrongETbinaryfromStartup = "<html>This is not the exiftool executable/binary.<br>I can't use it and will now exit jExifToolGUI.</html>";
    public static final String wrongETbinaryfromPrefs = "<html>This is not the exiftool executable/binary.<br>I can't use it and will will leave the current preference for what it is.</html>";
    public static final String CanETlocation = "You cancelled the option to specify the exiftool location. Without it I can't do anything. I will now exit jExifToolGUI.";
    public static final String NoImgSelected = "No images selected.\nI have nothing to work on.";
    public static final String copymetadatatoxmp = "<html>This function will copy all possible information from exif and other tags into XMP format.<br>"
            +"This is an internal \"same image to same image\" copy, for all the selected images.<br><br>"
            +"Do you want to continue?</html>";
    public static final String REPAIR_JPG_METADATA = "If exiftool can't write to your photo it might be due to corrupted metadata. Exiftool can fix this but only "
        +"for the tags that are still readible.<br>In a jpeg the image data is separated from the meta data. If your "
        +"photo can't be displayed in an image viewer, your image data itself is corrupt. Exiftool can't repair that.<br><br>"
        +"Do you want to continue and repair as much metadata as possible?";
    public static final String SET_FILEDATETIME_TO_DATETIMEORIGINAL = "If you have modified your images in a \"sloppy\" image editor or copied them around or whatever other action(s), the file "
        +"date/time of your images might have changed to the date your did the action/modification on the image "
        +"where as the real file date (= creation date) of your images is most certainly (much) older.<br>"
        +"This function will take the original date/time when the photo was taken from the exif:DateTimeOriginal "
        +"and use that (again) as file date/time.<br><br>Do you want to continue?";
    public static final String RemoveMetaData = "Which metadata do you want to remove from your selected image(s)?<br>"
            +"Note that this screen is meant to move all tags from a certain category from your selected images(s).<br>" +
            "By writing \"clean empty\" fields from the Edit tabs you can more specifically clean that metadata.";
    public static final String exportMetaDataUiText = "This option gives you the possibility to export the metadata from your selected photo(s). A number of formats is supported.<br>"
            +"All formats give an export file per selected photo, apart from csv which will give you one (big) csv file for all selected photos.<br><br>"
            +"The exported files will be saved to the image folder you loaded your images from.";
    public static final String CreateArgsMetaDataUiText = "Which metadata from your selected image(s) do you want to add to your args file(s)?";
    public static final String NoOptionSelected = "You did not select one single option. Cancel would have been the correct option. I will not do anything.";
    public static final String MyCommandsText = "<html>On this tab you can define your own parameters to \"send to\" exiftool. The parameters will be executed on the images you have selected on the left."
            +"<br>You can both specify read parameters as well as write parameters. You can also combine several parameters."
            + " <b>Note:</b> You don\'t need (must not) provide the exiftool command itself. The Gui will take care of that.<br><br></html>"; // need 2 extra lines to create space
    public static final String XmpTopText = "These are some predefined XMP tags.<br>"
            +"*: These fields are used in all google (image) products<br>"
            +"**: These are the \"normal\" xmp-pdf keywords. You can simply fill in multiple keywords.<br>"
            +"**: These fields are so-called \"string+\" tags."
            +" Multiple Subjects/Persons in those fields need to be separated by a \",\". The program will separate them in multiple strings upon writing to the file."
            +"<br>--Note--: The string+ (***) fields in this (xmp) tab are, like the other fields, always \"overwite\". For more options use the \"XMP_IPTC_String+\" tab<br>";
    public static final String GeotaggingLeaveFolderEmpty = "<html>Leave the \"Folder containing the images:\"  empty if you want to use (a selection of) the images in the left images pane.</html>";
    public static final String GeotaggingLocationLabel = "Geotagging will add the GPS coordinates to your images, but not the location. Using below textfields you can add that as well.";
    public static final String GeotaggingGeosyncExplainLabel = "<html>A positive geosync time will add that time to your image(s), before comparing it/them to the track log.</html>";
    public static final String GPanoTopText = "Tags marked with * are mandatory. They have no \"Save\" checkbox as they need to be populated anyway";
    public static final String GpanoMinVersionText = "\"Pose Heading Degrees\" (**) is necessary to make it function in Google Maps.<br>"
            +"The below mentioned tags marked with *** are only writable with exiftool >= 9.09<br>Your version is: ";
    public static final String GpanoSetSaveCheckboxes = "Do not forget to set the \"Save\" checkboxes\n upon saving, when relevant.";
    public static final String RenamingGeneralText = "If you want to autonumber your images, make sure you have them in the right alphabetical or date order."
            +" This is not a problem if you use the \"source folder\" here, but can be a problem if you use the images from the main screen "
            +"and if they are in \"random\"  order.";
    public static final String RenamingNoteText = "<html>Note: Some combinations of prefix and suffic are not allowed. Upon the selection "
            +"of the prefix, only the allowed suffixes are enabled.</html>";
    public static final String RenamingDuplicateNames = "In case your chosen options lead to duplicate names, the tool will automatically"
            +" autonumber the images.<br>Please specify how the tool should do this.";
    public static final String gpsCalculatorLabelText = "This program wants your latitude and longitude as decimal degrees for input."
            +" If you have your data in degrees-min-sec N(S)/E(W), you can use this calculator to convert the data.";
    public static final String ModifyDateTimeLabel = "This dialog gives you the option to modify the several date/time tags "
            +"in the exif (and xmp) information like in the \"Edit Data -> Exif\" tab. It works on the selected images.<br>"
            +"The corresponding xmp values can be updated at the same time.";
    public static final String ShiftDateTimeLabel = "Sometimes your camera's clock is 1-2 hours behind or ahead because you are "
            +"\"suddenly\" in another time zone, or forgot to adjust for summer/winter time. This allows you to shift a bunch of photos all with the same time shift.";
    public static final String NotAllMandatoryFields = "<html>At least one of the manadatory fields is not complete.<br>Please complete the missing mandatory data.<br><br>I can't continue without it.</html>";
    public static final String lensSaveLoadConfigLabel = "<html>Below two buttons that give you the option to configure one or more of your lenses and to save it/them for later use."
            +" In case of saving a lens, the \"Save\" checkboxes are not used. All fields/values will be saved regardless whether you added data to it or not.";
    public static final String exiftoolDBText = "This tab gives you the option to query the builtin exiftool groups & tags database. You can query by selecting values from the drop-downs or by issueing a direct sql query.<br>"
            +"Note: This will show the available tags (tagnames) that exiftool knows. This tab doesn't do anything with your images.";
    public static final String ExportPreviewsThumbnails = "This option will export all preview images and thumbnails from the selected images. This can only be done if your image(s) do have them.<br>"
            +"Do you want to continue?";
    public static final String StringsTopText = "This tab (currently) combines seveal XMP and one IPTC String+ tags. String+ tags can contain multiple strings."
            +" Multiple Keywords/Subjects/Persons in those fields need to be separated by a \",\". The program will separate them in multiple strings upon writing to the file.<br>"
            +"Note: The xmp keywords in this tab are the xmp-acdsee keywords.<br>"
            +"<br>You can also choose the write option for the specific string+ when writing them to your files.<br>"
            +"Note: When choosing append, only specify the new value and do not include the keywords/subjects/persons already in your file as they will be appended again.<br><br>";
}
