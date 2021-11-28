package org.hvdw.jexiftoolgui;

public class ProgramTexts {
    /* HTML in Swing components follows the HTML 3.2 standard from 1996. See https://www.w3.org/TR/2018/SPSD-html32-20180315/
    All strings use "internal" tags, but not begin and end tags as we use the String.format(ProgramTexts.HTML, <width>, helptext)
     */
    public static final String Author = "Harry van der Wolf";
    public static final String ProjectWebSite = "http://hvdwolf.github.io/jExifToolGUI";
    public static final String Version = "1.11.1";
    public static final String HTML = "<html><body style='width: %1spx'>%1s";
    public static final String downloadInstallET = "I will now open the ExifTool website in your browser and then close jExifToolGUI.<br>"
            +"After having downloaded and installed ExifTool you can reopen jExifToolGUI.<br><br>If ExifTool is in your PATH, jExifToolGUI will simply continue.<br><br>"
            +"If ExifTool is NOT in your PATH, you need to specify the location where you installed ExifTool.";
    public static final String CreateArgsMetaDataUiText = "Which metadata from your selected image(s) do you want to add to your args file(s)?";
}
