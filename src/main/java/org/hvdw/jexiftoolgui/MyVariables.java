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

    private File[] selectedFiles;
    public File[] getSelectedFiles() {
        return this.selectedFiles;
    }
    public void setSelectedFiles(File[] openedFiles) { this.selectedFiles = openedFiles; }

    private int[] selectedFilenamesIndices;
    public int[] getSelectedFilenamesIndices() { return this.selectedFilenamesIndices; }
    public void setSelectedFilenamesIndices(int[] selectedTableIndices) { this.selectedFilenamesIndices = selectedTableIndices; }
}
