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

    /* from: https://www.codejava.net/coding/java-getter-and-setter-tutorial-from-basics-to-best-practices
    private int[] scores;
    public int[] getScores() {
        int[] copy = new int[this.scores.length];
        System.arraycopy(this.scores, 0, copy, 0, copy.length);
        return copy;
    }
    public void setScores(int[] scr) {
        this.scores = new int[scr.length];
        System.arraycopy(scr, 0, this.scores, 0, scr.length);
    }*/

    /*private File[] selectedFiles;
    public File[] getSelectedFiles() {
        File[] copy = new File[this.selectedFiles.length];
        System.arraycopy(this.selectedFiles, 0, copy, 0, copy.length);
        return copy;
    }
    public void setSelectedFiles(File[] openedFiles) {
        this.selectedFiles = new File[openedFiles.length];
        System.arraycopy(openedFiles, 0, this.selectedFiles, 0, openedFiles.length);
    }

    private int[] selectedFilenamesIndices;
    public int[] getSelectedFilenamesIndices() {
        int[] copy = new int[this.selectedFilenamesIndices.length];
        System.arraycopy(this.selectedFilenamesIndices, 0, copy, 0, copy.length);
        return copy;
    }
    public void setSelectedFilenamesIndices(int[] selectedTableIndices) {
        this.selectedFilenamesIndices = new int[selectedTableIndices.length];
        System.arraycopy(selectedTableIndices, 0, this.selectedFilenamesIndices, 0, selectedTableIndices.length);
    } */

    // Or ???

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
