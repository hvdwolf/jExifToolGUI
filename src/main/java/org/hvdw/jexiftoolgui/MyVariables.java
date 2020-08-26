package org.hvdw.jexiftoolgui;

// Actually this shouldn't be done but as I'm such a lousy programmer and don't know how to do it otherwise I do it anyway
// The only option I know is to drag these variables through all methods

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyVariables {

    private final static MyVariables staticInstance = new MyVariables();

    private MyVariables() {

    }

    private int SelectedRow;
    private int SelectedColumn;
    private String SelectedImagePath;
    private File[] selectedFiles;
    private int[] selectedFilenamesIndices;
    private String jexiftoolguiDBPath;
    private String cantdisplaypng;
    private String selectedLensConfig;
    private String tmpWorkFolder;
    private File CurrentWorkFile;
    private String ExiftoolVersion;
    private List<List> tableRowsCells;

    // The actual getters and setters
    public static int getSelectedRow() { return staticInstance.SelectedRow;}
    public static void setSelectedRow(int index) {staticInstance.SelectedRow = index; }

    public static int getSelectedColumn() {
        return staticInstance.SelectedColumn;
    }
    public static void setSelectedColumn(int num) {
        staticInstance.SelectedColumn = num;
    }

    public static String getSelectedImagePath() {
        return staticInstance.SelectedImagePath;
    }
    public static void setSelectedImagePath(String selImgPath) {
        staticInstance.SelectedImagePath = selImgPath;
    }

    public static String getjexiftoolguiDBPath() {
        return staticInstance.jexiftoolguiDBPath;
    }
    public static void setjexiftoolguiDBPath(String selDBPath) {
        staticInstance.jexiftoolguiDBPath = selDBPath;
    }

    public static String getcantdisplaypng() {
        return staticInstance.cantdisplaypng;
    }
    public static void setcantdisplaypng(String pngPath) {
        staticInstance.cantdisplaypng = pngPath;
    }

    public static String getselectedLensConfig() {
        return staticInstance.selectedLensConfig;
    }
    public static void setselectedLensConfig(String sLC) {
        staticInstance.selectedLensConfig = sLC;
    }

    public static void setSelectedFiles(File[] openedFiles) {
        staticInstance.selectedFiles = Arrays.copyOf(openedFiles, openedFiles.length);
    }
    public static File[] getSelectedFiles() {
        return Arrays.copyOf(staticInstance.selectedFiles, staticInstance.selectedFiles.length);
    }

    public static String gettmpWorkFolder() {
        return staticInstance.tmpWorkFolder;
    }
    public static void settmpWorkFolder( String tmpworkfldr) {
        staticInstance.tmpWorkFolder = tmpworkfldr;
    }

    public static File getCurrentWorkFile() {
        return staticInstance.CurrentWorkFile;
    }
    public static void setCurrentWorkFile(File file) {
        staticInstance.CurrentWorkFile = file;
    }

    public static int[] getSelectedFilenamesIndices() {
        return Arrays.copyOf(staticInstance.selectedFilenamesIndices, staticInstance.selectedFilenamesIndices.length);
    }
    public static void setSelectedFilenamesIndices(int[] selectedTableIndices) {
        staticInstance.selectedFilenamesIndices = Arrays.copyOf(selectedTableIndices,selectedTableIndices.length);
    }
    public static String getExiftoolVersion() {
        return staticInstance.ExiftoolVersion;
    }
    public static void setExiftoolVersion(String exv) {
        staticInstance.ExiftoolVersion = exv;
    }

    public static List<List> gettableRowsCells() { return staticInstance.tableRowsCells; }
    public static void settableRowsCells (List<List> tblRwsClls) {staticInstance.tableRowsCells = tblRwsClls; }

}
