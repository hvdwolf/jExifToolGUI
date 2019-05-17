package org.hvdw.jexiftoolgui;

// Actually this shouldn't be done but as I'm such a lousy programmer and don't know how to do it otherwise I do it anyway
// The only option I know is to drag these variables through all methods

import java.io.File;

public class myVariables {

    private int mySelectedRow;
    public int getMySelectedRow() {
        return mySelectedRow;
    }
    public void setMySelectedRow(int num) {
        this.mySelectedRow = num;
    }

    private int mySelectedColumn;
    public int getMySelectedColumn() {
        return this.mySelectedColumn;
    }
    public void setMySelectedColumn(int num) {
        this.mySelectedColumn = num;
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
