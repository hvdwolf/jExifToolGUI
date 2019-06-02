package org.hvdw.jexiftoolgui;

// Actually this shouldn't be done but as I'm such a lousy programmer and don't know how to do it otherwise I do it anyway
// The only option I know is to drag these variables through all methods

import java.io.File;
import java.util.Arrays;

public class MyVariables {

    private final static MyVariables myVariables = new MyVariables();

    private MyVariables() {
    }

    private int mySelectedRow;
    private int SelectedRow;

    private int SelectedColumn;

    private String SelectedImagePath;

    private File[] selectedFiles;

    private int[] selectedFilenamesIndices;

    public static int getMySelectedRow() {
        return myVariables.mySelectedRow;
    }

    public static void setMySelectedRow(int num) {
        myVariables.mySelectedRow = num;
    }

    static int getSelectedRow() {
        return myVariables.SelectedRow;
    }

    static void setSelectedRow(int index) {
        myVariables.SelectedRow = index;
    }

    public static int getSelectedColumn() {
        return myVariables.SelectedColumn;
    }

    public static void setSelectedColumn(int num) {
        myVariables.SelectedColumn = num;
    }

    public static String getSelectedImagePath() {
        return myVariables.SelectedImagePath;
    }

    public static void setSelectedImagePath(String selImgPath) {
        myVariables.SelectedImagePath = selImgPath;
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


    static File[] getSelectedFiles() {
        return Arrays.copyOf(myVariables.selectedFiles, myVariables.selectedFiles.length);
    }

    static void setSelectedFiles(File[] openedFiles) {
        myVariables.selectedFiles = Arrays.copyOf(openedFiles, openedFiles.length);
    }


    public static int[] getSelectedFilenamesIndices() {
        return Arrays.copyOf(myVariables.selectedFilenamesIndices, myVariables.selectedFilenamesIndices.length);
    }

    public static void setSelectedFilenamesIndices(int[] selectedTableIndices) {
        myVariables.selectedFilenamesIndices = Arrays.copyOf(selectedTableIndices, selectedTableIndices.length);
    }
}
