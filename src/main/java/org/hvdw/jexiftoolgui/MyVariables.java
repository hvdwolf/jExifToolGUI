package org.hvdw.jexiftoolgui;

// Actually this shouldn't be done but as I'm such a lousy programmer and don't know how to do it otherwise I do it anyway
// The only option I know is to drag these variables through all methods

import java.io.File;
import java.util.Arrays;

public class MyVariables {

    private final static MyVariables staticInstance = new MyVariables();

    private MyVariables() {

    }

    private int SelectedRow;
    private int SelectedColumn;
    private String SelectedImagePath;
    private File[] selectedFiles;
    private int[] selectedFilenamesIndices;

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

    public static void setSelectedFiles(File[] openedFiles) {
        staticInstance.selectedFiles = Arrays.copyOf(openedFiles, openedFiles.length);
    }
    public static File[] getSelectedFiles() {
        return Arrays.copyOf(staticInstance.selectedFiles, staticInstance.selectedFiles.length);
    }


    public static int[] getSelectedFilenamesIndices() {
        return Arrays.copyOf(staticInstance.selectedFilenamesIndices, staticInstance.selectedFilenamesIndices.length);
    }
    public static void setSelectedFilenamesIndices(int[] selectedTableIndices) {
        staticInstance.selectedFilenamesIndices = Arrays.copyOf(selectedTableIndices,selectedTableIndices.length);
    }
    

}
