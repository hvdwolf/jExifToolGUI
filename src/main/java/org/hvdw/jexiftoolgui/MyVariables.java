package org.hvdw.jexiftoolgui;

// Actually this shouldn't be done but as I'm such a lousy programmer and don't know how to do it otherwise I do it anyway
// The only option I know is to drag these variables through all methods

import java.io.File;

public class MyVariables {

    private int mySelectedRow;
    public int getMySelectedRow() {
        return mySelectedRow;
    }
    public void setMySelectedRow(int num) {
        this.mySelectedRow = num;
    }

    private int SelectedRow;
    public int getSelectedRow() { return this.SelectedRow;}
    public void setSelectedRow(int index) {this.SelectedRow = index; }

    private int SelectedColumn;
    public int getSelectedColumn() {
        return this.SelectedColumn;
    }
    public void setSelectedColumn(int num) {
        this.SelectedColumn = num;
    }

    private String SelectedImagePath;
    public String getSelectedImagePath() {
        return this.SelectedImagePath;
    }
    public void setSelectedImagePath(String selImgPath) {
        this.SelectedImagePath = selImgPath;
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

    private File[] selectedFiles;
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
    }
}
