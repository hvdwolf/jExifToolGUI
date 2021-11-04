package org.hvdw.jexiftoolgui;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This is the big setter/getter class for the entire program
 */
public class MyVariables {

    private final static MyVariables staticInstance = new MyVariables();

    private MyVariables() {
    }

    private int SelectedRow;
    private int SelectedColumn;
    private String SelectedImagePath;
    private File[] loadedFiles;
    private int[] selectedFilenamesIndices;
    private String jexiftoolguiDBPath;
    private String cantdisplaypng;
    private String cantconvertpng;
    private String selectedLensConfig;
    private String tmpWorkFolder;
    private File CurrentWorkFile;
    private File CurrentFileInViewer;
    private File SinglePreview;
    private String SinglePreviewFileName;
    private String ExiftoolVersion;
    private List<List> tableRowsCells;
    private List<String> userCombiTableValues;
    private List<Integer> selectedIndicesList;
    private String[] CustomCombis;
    private String delayedOutput;
    private HashMap<String, String> imgBasicData;
    private String pdfDocs;
    private String[] commandLineArgs;
    private boolean commandLineArgsgiven = false;
    private int ScreenWidth;
    private int ScreenHeight;
    private String[] mainScreenParams;
    private ArrayList<String> category_tag;
    private String Latitude;
    private String Longitude;
    private HashMap <String, HashMap<String, String> > imagesData;
    private String SearchPhrase;
    private boolean reloadImagesFromSearchResult = false;
    private String ExifToolPath;

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

    public static String getcantconvertpng() { return staticInstance.cantconvertpng; }
    public static void setcantconvertpng(String cpngPath) { staticInstance.cantconvertpng = cpngPath; }

    public static String getselectedLensConfig() {
        return staticInstance.selectedLensConfig;
    }
    public static void setselectedLensConfig(String sLC) {
        staticInstance.selectedLensConfig = sLC;
    }

    public static void setLoadedFiles(File[] loadedFiles) {
        staticInstance.loadedFiles = Arrays.copyOf(loadedFiles, loadedFiles.length);
    }
    public static File[] getLoadedFiles() {
        return Arrays.copyOf(staticInstance.loadedFiles, staticInstance.loadedFiles.length);
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

    public static File getCurrentFileInViewer() { return staticInstance.CurrentFileInViewer; }
    public static void setCurrentFileInViewer(File file) { staticInstance.CurrentFileInViewer = file; }

    public static File getSinglePreview() { return staticInstance.SinglePreview; }
    public static void setSinglePreview(File file) { staticInstance.SinglePreview = file; }

    public static String getSinglePreviewFileName() { return staticInstance.SinglePreviewFileName; }
    public static void setSinglePreviewFileName( String spfn) { staticInstance.SinglePreviewFileName = spfn; }

    public static int[] getSelectedFilenamesIndices() {
        return Arrays.copyOf(staticInstance.selectedFilenamesIndices, staticInstance.selectedFilenamesIndices.length);
    }
    public static void setSelectedFilenamesIndices(int[] selectedTableIndices) {
        staticInstance.selectedFilenamesIndices = Arrays.copyOf(selectedTableIndices,selectedTableIndices.length);
    }

    public static List<Integer> getselectedIndicesList() { return staticInstance.selectedIndicesList; }
    public static void setselectedIndicesList(List<Integer> selIndList) { staticInstance.selectedIndicesList = selIndList; }

    public static String getExiftoolVersion() {
        return staticInstance.ExiftoolVersion;
    }
    public static void setExiftoolVersion(String exv) {
        staticInstance.ExiftoolVersion = exv;
    }

    public static String getExifToolPath() { return staticInstance.ExifToolPath; }
    public static void setExifToolPath(String exp) { staticInstance.ExifToolPath = exp; }

    public static List<List> gettableRowsCells() { return staticInstance.tableRowsCells; }
    public static void settableRowsCells (List<List> tblRwsClls) {staticInstance.tableRowsCells = tblRwsClls; }

    public static List<String> getuserCombiTableValues() { return staticInstance.userCombiTableValues; }
    public static void setuserCombiTableValues (List<String> userCTV) { staticInstance.userCombiTableValues = userCTV; }

    public static String[] getCustomCombis() { return Arrays.copyOf(staticInstance.CustomCombis, staticInstance.CustomCombis.length); }
    public static void setCustomCombis(String[] CustomCombisfromDB) { staticInstance.CustomCombis = Arrays.copyOf(CustomCombisfromDB, CustomCombisfromDB.length); }

    public static String getdelayedOutput() {
        return staticInstance.delayedOutput;
    }
    public static void setdelayedOutput(String deloutput) {
        staticInstance.delayedOutput = deloutput;
    }

    public static HashMap<String, String> getimgBasicData () { return staticInstance.imgBasicData; };
    public static void setimgBasicData( HashMap<String, String> imgBasData) {staticInstance.imgBasicData = imgBasData; }

    public static String getpdfDocs() { return staticInstance.pdfDocs; }
    public static void setpdfDocs(String pdfDcs) { staticInstance.pdfDocs = pdfDcs; }

    public static String[] getcommandLineArgs() { return Arrays.copyOf(staticInstance.commandLineArgs, staticInstance.commandLineArgs.length); }
    public static void setcommandLineArgs(String[] setcmdlnargs) { staticInstance.commandLineArgs = Arrays.copyOf(setcmdlnargs, setcmdlnargs.length); }

    public static boolean getcommandLineArgsgiven() { return staticInstance.commandLineArgsgiven;}
    public static void setcommandLineArgsgiven(boolean cmdlnrgsgvn) {staticInstance.commandLineArgsgiven = cmdlnrgsgvn; }

    public static int getScreenWidth() { return staticInstance.ScreenWidth;}
    public static void setScreenWidth(int width) {staticInstance.ScreenWidth = width; }

    public static int getScreenHeight() { return staticInstance.ScreenHeight;}
    public static void setScreenHeight(int height) {staticInstance.ScreenHeight = height; }

    public static String[] getmainScreenParams() { return Arrays.copyOf(staticInstance.mainScreenParams, staticInstance.mainScreenParams.length); }
    public static void setmainScreenParams(String[] setmnscrnprms) { staticInstance.mainScreenParams = Arrays.copyOf(setmnscrnprms, setmnscrnprms.length); }

    public static ArrayList<String> getcategory_tag() { return staticInstance.category_tag; }
    public static void setcategory_tag (ArrayList<String> ctgr_tg) { staticInstance.category_tag = ctgr_tg; }

    public static String getLatitude() { return staticInstance.Latitude; }
    public static void setLatitude(String lat) { staticInstance.Latitude = lat; }

    public static String getLongitude() { return staticInstance.Longitude; }
    public static void setLongitude(String lng) { staticInstance.Longitude = lng; }

    public static HashMap<String, HashMap<String, String>> getimagesData() { return staticInstance.imagesData; }
    public static void setimagesData(HashMap<String, HashMap<String, String>> imgsData) {staticInstance.imagesData = imgsData; }

    public static String getSearchPhrase() { return staticInstance.SearchPhrase; }
    public static void setSearchPhrase(String srchphrs) { staticInstance.SearchPhrase = srchphrs; }

    public static boolean getreloadImagesFromSearchResult() { return staticInstance.reloadImagesFromSearchResult;}
    public static void setreloadImagesFromSearchResult(boolean rifsr) {staticInstance.reloadImagesFromSearchResult = rifsr; }
}