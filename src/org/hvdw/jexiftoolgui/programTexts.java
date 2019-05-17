package org.hvdw.jexiftoolgui;

public class programTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/pyExifToolGUI";
    public static final String Version = "1";
    public static final String newVersionText = "<html><big>There is a new version available</big><br><br><table width='500'><tr><td>"
            +"I can open the releases webpage so you can download the new version.<br><br>"
            +"Open the website or not?</td></tr></html>";
    public static final String aboutText =
            "<html><big>jExifToolGUI</big><hr><hr>"
            +"<table width='600'><tr><td>"
            +"<strong>jExifToolGui</strong> is a java/Swing program that functions as "
            +"a graphical frontend (GUI) for <a href='http://www.sno.phy.queensu.ca/~phil/exiftool/'>exiftool</a>.<br><br>"
            +"Exiftool is written and maintained by Phil Harvey. Many thanks go to him for this excellent tool. Without ExifTool, "
            +"this Graphical frontend for ExifTool would never have existed.<br><br>"
            +"jExifToolGUI is (just) a graphical frontend for ExifTool. "
            +"It can use a \"reference\" image to write the tags to a multiple set of photos, like gps tags for photos"
            +" that were taken at the same location but somehow miss or contain the incorrect gps info.<br>"
            +"jExifToolGUI also contains extensive renaming functionality based on the exif info in your photos.</p>"
            +"<p>This jExifToolGUI program is free, Open Source software: you can redistribute it and/or "
            +"modify it under the terms of the GNU General Public License "
            +"as published by the Free Software Foundation, either version "
            +"3 of the License, or (at your option) any later version.</p>"
            +"<p>This program is distributed in the hope that it will be useful, "
            +"but WITHOUT ANY WARRANTY; without even the implied "
            +"warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR "
            +"PURPOSE.  See the GNU General Public License for more details.</p>"
            +"<p>You should have received a copy of the GNU General Public "
            +"License along with this program.  If not, see <a href='http://www.gnu.org/licenses'>www.gnu.org/licenses</a>.</p>"
            +"<p></p><p>jExifToolGUI version: " + Version + "."
            +"<p></p><p>Author/creator of ExifTool: Phil Harvey."
            +"<p></p><p>Author/creator of jExifToolGUI: " + Author + "."
            +"</td></tr></html>";
    public static final String aboutExifToolText =
            "<html><big>ExifTool</big><hr><hr>"
            +"<table width='600'><tr><td>"
            +"ExifTool is a platform-independent Perl command-line application and library for reading, writing and editing meta information in a wide variety of files.<br> "
            +"Exiftool is by far the best and most extensive metadata tool available!<br><br>"
            +"It reads and writes metadata information from/to many files, but primarily focuses on photos/images. It reads/writes exif, "
            +"gps, xmp tags and many more.<br>"
            +"It can use a \"reference\" image to write the tags to a multiple set of photos or a complete directory containing photos.<br>"
            +"<p></p><p>Author/creator/maintainer of ExifTool: Phil Harvey.<br><br>"
            +"</td></tr></html>";
    public static final String noExifTool = "<html><b>I can't find exiftool in your PATH and it is not specified in the preferences either.<br>"
            +"I can't continue without it.</b><br><br>"
            +"<table width='600'><tr><td>"
            +"You have a few options:<br>"
            +"<ul><li>Linux: Install it via your distribution's package manager (but this might be a (slightly) older version), or download the latest from Phil Harvey's exiftool website.</li>"
            +"<li>Windows: Download the latest version from Phil Harvey's exiftool website.</li>"
            +"<li>Mac OS/X: Install it via MacPorts or Homebrew (but this might be a (slightly) older version), or download the latest from Phil Harvey's exiftool website.</li></ul>"
            +"Use one of the below options."
            +"</td></tr></html>";
    public static final String cancelledETlocatefromStartup = "<html>You cancelled providing the location of exiftool.<br>I will now exit jExifToolGUI.</html>";
    public static final String cancelledETlocatefromPrefs = "<html>You cancelled providing the location of exiftool.<br>I will leave the current preference for what it is.</html>";
    public static final String downloadInstallET = "<html><table width='600'><tr><td>I will now open the ExifTool website in your browser and then close jExifToolGUI.<br>"
            +"After having downloaded and installed ExifTool you can reopen jExifToolGUI.<br><br>If ExifTool is in your PATH, jExifToolGUI will simply continue.<br><br>"
            +"If ExifTool is NOT in your PATH, you need to specify the location where you installed ExifTool.</td></tr></table></html>";
    public static final String wrongETbinaryfromStartup = "<html>This is not the exiftool executable/binary.<br>I can't use it and will now exit jExifToolGUI.</html>";
    public static final String wrongETbinaryfromPrefs = "<html>This is not the exiftool executable/binary.<br>I can't use it and will will leave the current preference for what it is.</html>";
    public static final String CanETlocation = "You cancelled the option to specify the exiftool location. Without it I can't do anything. I will now exit jExifToolGUI.";
    public static final String NoImgSelected = "No images selected.\nI have nothing to work on.";
    public static final String copymetadatatoxmp = "<html>This function will copy all possible information from exif and other tags into XMP format.<br>"
            +"This is an internal \"same image to same image\" copy, for all the selected images.<br><br>"
            +"Do you want to continue?</html>";
    public static final String repairJPGmetadata = "<html><table width='450'><tr><td>If exiftool can't write to your photo it might be due to corrupted metadata. Exiftool can fix this but only "
            +"for the tags that are still readible.<br>In a jpeg the image data is separated from the meta data. If your "
            +"photo can't be displayed in an image viewer, your image data itself is corrupt. Exiftool can't repair that.<br><br>"
            +"Do you want to continue and repair as much metadata as possible?</td></tr></table></html>";
    public static final String setFileDatetoDateTimeOriginal = "<html><table width='600'><tr><td>If you have modified your images in a \"sloppy\" image editor or copied them around or whatever other action(s), the file "
            +"date/time of your images might have changed to the date your did the action/modification on the image "
            +"where as the real file date (= creation date) of your images is most certainly (much) older.<br>"
            +"This function will take the original date/time when the photo was taken from the exif:DateTimeOriginal "
            +"and use that (again) as file date/time.<br><br>"
            +"Do you want to continue?</td></tr></table></html>";
    public static final String RemoveMetaData = "<html><table width='370'><tr><td>Which metadata do you want to remove from your selected image(s)?<br>"
            +"Note that this screen is meant to move all tags from a certain category from your selected images(s).<br>" +
            "By writing \"clean empty\" fields from the Edit tabs you can more specifically clean that metadata.</td></tr></table></html>";
    public static final String exportMetaDataUiText = "<html><table width='420'><tr><td>This option gives you the possibility to export the metadata from your selected photo(s). A number of formats is supported.<br>"
            +"All formats give an export file per selected photo, apart from csv which will give you one (big) csv file for all selected photos.</td></tr></table></html>";
    public static final String CreateArgsMetaDataUiText = "<html><table width='370'><tr><td>Which metadata from your selected image(s) do you want to add to your args file(s)?</td></tr></table></html>";
    public static final String NoOptionSelected = "You did not select one single option. Cancel would have been the correct option. I will not do anything.";
    public static final String MyCommandsText = "<html>On this tab you can define your own parameters to \"send to\" exiftool. The parameters will be executed on the images you have selected on the left."
            +"<br>You can both specify read parameters as well as write parameters. You can also combine several parameters."
            + " <b>Note:</b> You don\'t need (must not) provide the exiftool command itself. The Gui will take care of that.<br><br></html>"; // need 2 extra lines to create space
    public static final String GeotaggingLeaveFolderEmpty = "<html>Leave the \"Folder containing the images:\"  empty if you want to use (a selection of) the images in the left images pane.</html>";
    public static final String GeotaggingLocationLabel = "<html><table width='800'><tr><td>Geotagging will add the GPS coordinates to your images, but not the location. Using below textfields you can add that as well.</td></tr></table></html>";
    public static final String RenamingGeneralText = "<html><table width='800'><tr><td>If you want to autonumber your images, make sure you have them in the right alphabetical or date order."
            +" This is not a problem if you use the \" source folder\" here, but can be a problem if you use the images from the main screen "
            +"and if they are in \" random\"  order.</tr></td></table></html>";
    public static final String RenamingNoteText = "<html>Note: Some combinations of prefix and suffic are not allowed. Upon the selection "
            +"of the prefix, only the allowed suffixes are enabled.</html>";
    public static final String RenamingDuplicateNames = "<html><table width='500'><trd><td>In case your chosen options lead to duplicate names, the tool will automatically"
            +" autonumber the images.<br>Please specify how the tool should do this.</td></tr></table></html>";
    public static final String gpsCalculatorLabelText = "<html><table width='230'><tr><td>This program wants your latitude and longitude as decimal values for input."
            +" If you have your data in deg-min-sec N(S)/E(W), you can use this calculator to convert the data.";
    public static final String ModifyDateTimeLabel = "<html><table width='420'><tr><td>This dialog gives you the option to modify the several date/time tags"
            +"in the exif (and xmp) information like in the \"Edit Data -> Exif\" tab. It works on the selected images.<br>"
            +"More important here: You can use the shift the date/time forward/backward (future/past) for the selected fields, for the selected images.<br>"
            +"The corresponfing xmp values can be updated at the same time.<br></td></tr></table></html>";
    public static final String ShiftDateTimeLabel = "<html><table width='400'><trd><td>Sometimes your camera's clock is 1-2 hours behind or ahead because you are "
            +"\"suddenly\" in another time zone, or forgot to adjust for summer/winter time. This allow you to shift a bunch of photos all with the same time shift.</td></tr></table></html>";
}
