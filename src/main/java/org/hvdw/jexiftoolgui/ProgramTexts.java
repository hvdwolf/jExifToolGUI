package org.hvdw.jexiftoolgui;

public class ProgramTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
    All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/jExifToolGUI";
    public static final String Version = "1.4.3";
    public static final String HTML = "<html><body style='width: %1spx'>%1s";
    public static final String CreditsText =
            "<big>Credits</big><hr><br>"
            +"This is currently a short list, but nonetheless really essential<br><br>"
            +"<b>David Däster:</b> Bringing structure into this project. Building the facades, adding the logger, creating the idea/gradle integration. I started as python programmer, hardly knowing anything of java, "
            +"and he helped, educated and corrected me in many areas (although I undoubtedly made again many errors afterwards).<br>Thanks for all your support!<br><br>"
            +"<b>Martin Gersbach:</b> Spanish translation (Traducción Española)<br><br>"
            +"<b>Karsten Günther:</b> German translation (Deutsche Übersetzung)<br><br>";
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
