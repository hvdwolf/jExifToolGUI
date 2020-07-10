package org.hvdw.jexiftoolgui;


public class HelpTexts {
   /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
   All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
    */

    public static final String ExifAndXmpHelp = "<big>Exif or XMP metadata</big><hr><br>This tab is used to add (a limited amount of) exif or xmp data (depending on the tab) to your selected image(s).</p>"
            + "<p><b>Things to take into account!</b></p><p>As you can see the options all have a \"Save checkbox\" behind their input fields. "
            + "It means that this option/field will be saved when checked, even when the field is empty. This also means that you can:"
            + "<ul><li>overwrite existing data with empty data \"by accident\".</li><li>deliberately clear (overwrite) existing data with an empty string from an empty field.</li></ul>"
            + "<h3>Available buttons:</h3>"
            + "<ul><li><b>Copy from selected image</b>: This will copy all the (available) xmp data from the selected image into the input fields.</li>"
            + "<li><b>Save to selected image(s)</b>: This will save the \"checked\" exif data to your selected image(s).</li>"
            + "<li><b>Copy Defaults</b>: This will copy the default creator and rights data which you provided in the Preferences tab.</li>"
            + "<li><b> Reset fields</b>: This will empty all fields and set checkboxes to their defaults.</li>"
            + "<li><b>Help</b>: This button opens this popup that you are currently reading.</li></ul>";
    public static final String GeotaggingHelp = "<big>Geotagging</big><hr><br>Geotagging adds GPS data to your images based on data from a GPS track log file."
            +"This GPS track file can be used from your phone, gps device, navigation device, or whatever you have providing such a GPS track.<br>"
            + " The GPS track log file is loaded, and linear interpolation is used to determine the GPS position at the time of the image, then the following tags "
            + "are written to the image (if the corresponding information is available). It means that your camera needs to be set correctly with regard to the date/time of the location where you are.<br><br>"
            + "jExifToolGUI also supports the \"Geosync\" feature of ExifTool. The Geosync tag is only needed when the image timestamps are not properly synchronized with GPS time.<br>"
            + "For example, a value of \"+1:20\" specifies that 1 minute and 20 seconds is added to the Geotime value before checking with the GPS track file. This is for a camera running 1 minute 20 seconds slower than the GPS clock.<br>"
            + "The Geosync time is specified as  \"SS\", \"MM:SS\", \"HH:MM:SS\" or \"DD HH:MM:SS\" (where SS=seconds, MM=minutes, HH=hours and DD=days), "
            + "and a leading \"+\" or \"-\" may be added for positive or negative differences.<br><b>Note:</b> Do not use (double) quotes around the geosync time in jExifToolGUI. Simply use something like -25 or +1:20<br><br>"
            + "In jExifToolGUI you have 2 options:"
            + "<ol><li>Use (a selection of) the images you loaded in the left images pane.</li><li>Specify a folder containing a set of images to be tagged.</li></ol></p>"
            + "<p>In case of the first option you need to leave the folder empty. If the \"Folder containing the images:\" is not left empty, it will always be the first option used.<br><br>"
            + "The \"Overwrite Originals\" checkbox (default selected) makes that exiftool does an \"in place\" modification. When unselected new images will be created and the orignals get the extension \".original\"<br><br>"
            + "Note that jExifToolGUI will write both the EXIF GPS tags as well as the XMP GPS tags.";
    public static final String GPSHelp = "<big>GPS and Location</big><hr><br>This tab is used to add GPS data to your images. This tab works on a combination of exif, xmp and iptc gps data as latitude, longitude and altitude are covered in both the exif and in the xmp metadata structure.<br><br>"
            + "The Location metadata details (Country, Province/State, City, Location) are covered by xmp and IPTC.<br><br>This tab therefore acts on multiple tag categories (exif, xmp and iptc) "
            + "at once and simply tries to make your gps/location data as complete as possible in the several tag categories.";
    public static final String YourCommandsHelp = "<big>Your commands</big><hr><br>On this tab you can define your own parameters to \"send to\" exiftool. The parameters will be executed on the images you have selected on the left."
            + "<br>You can both specify read parameters as well as write parameters but not in one command: write actions take precedence. You can also combine several parameters. "
            + "<b>Note:</b> You don't need (must not) provide the exiftool command itself. The Gui will take care of that.<br>"
            + "One thing YOU need to take care of is the use of either single quotes or double quotes around some commands. "
            + "If you want to set the file date to the date the photo was taken, you need the command <tt>-FileModifyDate&lt;DateTimeOriginal</tt>."
            + "On Windows you need to double-quote this and on Linux and MacOS you need to single-quote this, like"
            + "<ul><li>Linux/MacOS: <tt>'-FileModifyDate&lt;DateTimeOriginal'</tt></li><li>Windows: <tt>\"-FileModifyDate&lt;DateTimeOriginal\"</tt></li></ul>"
            + "Other examples are:<table border=\"1\" cellpadding=\"10\">"
            + "<tr><td>-exif:all</td><td>will display all exif tags for the selected image(s)</td></tr>"
            + "<tr><td>-exif:all=</td><td>will remove(<b>!</b>) all exif tags from the selected image(s)</td></tr>"
            + "<tr><td>-Exif:Artist=\"My Name\"</td><td>Will put \"Your Name\" into the selected image(s)</td></tr>"
            + "<tr><td>-Xmp:City=Zwolle -Xmp:Country=Netherlands</td><td>Will write your city and country into the xmp tags of your image(s)</td></tr></table>"
            + "See also the <a href=\"http://www.sno.phy.queensu.ca/~phil/exiftool/exiftool_pod.html\" target=\"_blank\">exiftool Application Documentation</a> which gives extensive information and lots of examples.";
    public static final String CopyMetaDataHelp = "<big>Copy metadata</big><hr><br>This function will copy all or selected metadata from the source image into the target image(s).<br><br>"
            +"This can be done in several ways:<ul>"
            +"<li>all information to same-named tags in the preferred groups</li>"
            +"<li>all info from all tag groups to same tag groups</li>"
            +"<li>selected group to same group</li></ul><br>"
            +"This is NOT an internal \"same image to same image\" copy to the xmp tag group; See menu \"Extra -> Copy all metadata to xmp format\" for that option.<br><br>";
    public static final String GpanoHelp = "<big>Gpano</big><hr><br>This tab is used to add GPano Google PhotoSphere data to your selected image(s).<br><br>"
            + "The Google PhotoSphere functionality has been added in November 2012 to enable the use of auto-rotating panoramic photos inside Google products."
            +" For more info see the Google page: https://developers.google.com/streetview/spherical-metadata<br><br>"
            +"The fields marked with an asterisk are mandatory. The \"Use panorama viewer\" is also mandatory, but prefilled. It is here to show you that \"it is not forgotten\".";
    public static final String RenamingInfoText = "<big>Renaming</big><hr><br>The general date time options will not be mentioned as they are self explanatory.<br><br>"
            +"<b>String:</b> This can be any string you like also with spaces. UTF-8 is used but I can' t predict which \"strange\" characters might cause an error, maybe also OS dependent.<br><br>"
            +"<b>Camera model:</b> This will extract the camera model from the exif data of your image(s).<br><br>"
            +"<b>Location:</b> This will extract the \"xmp:location\" info from your image(s). NOTE: This will only work if your images contain this info. For example because you added that before(!) "
            +"in the \"Edit Data -> gps\" tab.<br><br>"
            +"<b>City Name:</b> This will extract the \"xmp:city\" info from your image(s). NOTE: This will only work if your images contain this info. For example because you added that before(!) "
            +"in the \"Edit Data -> gps\" tab.<br><br>"
            +"<b>ISO value:</b> This will take the \"exif:iso\" value from images. As ISO is just a numeric value like 100, the string ISO will be added, making it 100ISO.<br><br>"
            +"<b>Focal length in 35 mm:</b> For all of us (not) having a full-frame camera, this will add the corresponding focal length for a 35 mm.<br><br>"
            +"<b>Original file name:</b> This is the original name as given by your camera to your image.<br>";
    public static final String lensHelpText = "<big>Edit lens</big><hr><br>This screen has two purposes:<br>"
            +"<ul><li>Add/remove lens data to your photos (first row of buttons)</li>"
            +"<li>Create/Modify a lens configuration (second row of buttons enclosed inside lined framework).</li></ul>"
            +"Both options can be used to add lens data to your image if it is not complete.<br><br>"
            +"Next to that: Still some add-on lenses are not completely recognized by the camera and therefore the info is not added to the image. For these cases you can create lens configs "
            +"and save and load them for your images taken with that specific lens.";
    public static final String ExifToolDBhelptext = "<big>Exiftool database Query screen</big><hr><br>"
            +"exiftool comes with a huge set of tags for the several families (g0, g1, g2, g3, g4) and groups (like exif, xmp, iptc, etc.). All these tags can also be listed from exiftool itself "
            +"but it's less easy to see which tag(s) belong to which group(s). Also in plural: some tags come back multiple times in makernotes for camera's, or specific camera data. "
            +"And some are almost equal to each other.<br><br>"
            +"And why a database?: It's fast! And it can also be used to store \"custom settings\" like lenses (lens configurations) and personal views (ToDo).<br><br>"
            +"This screen comes in 3 \"action rows\":<br>"
            +"<ol><li>1st line: two comboboxes (dropdowns) that you can select to query the tags (do not forget to select the radio button in front of it. "
            +"(This was first automatic but then the query becomes really \"nervous\" as it will act on every change.)<br>"
            +"The second is actually a subset of the first (current 320) options but experience from my previous pyExifToolgUI program learns that most users are interested in their own camera.</li>"
            +"<li>2nd line: This is a \"wildcard\" query. Search for example on \"width\" and it will find all tagnames with \"width\" in the name (and there are a lot).</li>"
            +"<li>3rd line: Here you can put your own query like \"select taggroup,tagname from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and Groups.taggroup='EXIF';\".<br>"
            +"A database diagram is available for those (expert) users wanting to use this option.";
    /*
    */
}
