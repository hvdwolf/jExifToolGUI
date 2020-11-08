package org.hvdw.jexiftoolgui.model;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.CompareImagesWindow;

import javax.swing.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.slf4j.LoggerFactory.getLogger;

public class CompareImages {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(CompareImages.class);

    public final static class ArrayListHolder {
        private ArrayList<String[]> tmpMetadata = new ArrayList<String[]>();
        private ArrayList<String> Cat_Tag_List = new ArrayList<String>();

        public ArrayList<String[]> getTmpMetadata() {
            return tmpMetadata;
        }
        public void setTmpMetadata(ArrayList<String[]> tmpMdata) {
            tmpMetadata = tmpMdata;
        }

        public ArrayList<String> getCat_Tag_List() {
            return  Cat_Tag_List;
        }
        public void setCat_Tag_List(ArrayList<String> CT_List) {
            Cat_Tag_List = CT_List;
        }
    }

    private static void dbAction(String sql, String comment) {
        String qr = SQLiteJDBC.insertUpdateQuery(sql, "disk");
        if (!"".equals(qr)) { //means we have an error
            JOptionPane.showMessageDialog(null, "Encountered an error creating " + comment);
            logger.error("Encountered an error creating {}", comment);
        } else { // we were successful
            logger.debug("Successfully did {}", comment);
        }
    }

    private static void dbBulkAction(String[] sql, String comment) {
        String qr = SQLiteJDBC.bulkInsertUpdateQuery(sql, "disk");
        if (!"".equals(qr)) { //means we have an error
            JOptionPane.showMessageDialog(null, "Encountered an error creating " + comment);
            logger.error("Encountered an error creating {}", comment);
        } else { // we were successful
            logger.debug("Successfully did {}", comment);
        }
    }


    private static void Initialize() {
        // On every start of this Function
        CleanUp();
        String sql = "Create table if not exists ImageInfoRows (\n" +
                "    id integer primary key autoincrement,\n" +
                "    imgindex int NOT NULL,\n" +
                "    filename text NOT NULL,\n" +
                "    category text NOT NULL,\n" +
                "    tagname text NOT NULL,\n" +
                "    value text)";
        dbAction(sql, "create ImageInfoRows");
        sql = "create view if not exists category_tagname as select distinct category, tagname from imageinforows";
        //dbAction(sql, "Create view category_tagname");
    }

    /**
     * Used on every new compare and when closing the app
     */
    public static void CleanUp() {
        //dbAction("drop view if exists category_tagname", "drop view if exists category_tagname");
        dbAction("drop table if exists ImageInfoRows","drop table if exists ImageInfoRows");
    }

    /**
     * Method that stores all metadat into a table for later retrieval
     * @param index
     * @param filename
     * @param exiftoolInfo
     */
    private static void AddToImageInfoRows(Integer index, String filename, String exiftoolInfo) {
        String sql;

        if (exiftoolInfo.length() > 0) {
            if (exiftoolInfo.trim().startsWith("Warning")) {
                sql = "Insert into ImageInfoRows(imgindex,filename,category,tagname,value) values(" +
                        index + ",\"" + filename + "\",\"ExifTool Warning\",\"Invalid Metadata data\",\"Error\")";
                dbAction(sql, "Add warning row for " + filename);
            } else if (exiftoolInfo.trim().startsWith("Error")) {
                sql = "Insert into ImageInfoRows(imgindex,filename,category,tagname,value) values(" +
                        index + ",\"" + filename + "\",\"ExifTool Error\",\"Invalid Metadata data\",\"Error\")";
                dbAction(sql, "Add error row for " + filename);
            } else {
                String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                String[] sqls = new String[lines.length];
                int counter = 0;
                for (String line : lines) {
                    String[] imgInfoRow = line.split("\\t", 3);
                    sql = "Insert into ImageInfoRows(imgindex,filename,category,tagname,value) values(" +
                            index + ",\"" + filename + "\",\"" + imgInfoRow[0].trim() + "\",\"" + imgInfoRow[1].trim() + "\",\"" +imgInfoRow[2].trim() + "\")";
                    sqls[counter] = sql;
                    counter++;
                }
                dbBulkAction(sqls, "bulk sql");
            }
        }
    }

    /**
     * Method that stores all infodata in multiple Lists
     * @param index
     * @param exiftoolInfo
     */
    private static List<String[]> AddToInfoRows(Integer index, String exiftoolInfo) {
        //String sql;
        ArrayListHolder arrayListData = new ArrayListHolder();
        List<String[]> tmpMetadata = new ArrayList<String[]>();
        ArrayList<String> Cat_Tag_List = new ArrayList<String>();
        //String[] oneRow;
        // getter Cat_Tag_List  => Cat_Tag_List = MyVariables.getCat_Tag_List
        Cat_Tag_List = MyVariables.getcategory_tag();
        if (exiftoolInfo.length() > 0) {
            if (exiftoolInfo.trim().startsWith("Warning")) {
                String[] oneRow = { "ExifTool Warning||Invalid Metadata data", String.valueOf(index), "ExifTool Warning", "Invalid Metadata data", "Error" };
                tmpMetadata.add(oneRow);
            } else if (exiftoolInfo.trim().startsWith("Error")) {
                String[] oneRow = { "ExifTool Error||Invalid Metadata data", String.valueOf(index), "ExifTool Error", "Invalid Metadata data", "Error" };
                tmpMetadata.add(oneRow);
            } else {
                String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                for (String line : lines) {
                    String[] imgInfoRow = line.split("\\t", 3);
                    String[] oneRow = { imgInfoRow[0].trim() + "||" + imgInfoRow[1].trim(), String.valueOf(index), imgInfoRow[0].trim(), imgInfoRow[1].trim(), imgInfoRow[2].trim()};
                    tmpMetadata.add(oneRow);
                    String tmp = imgInfoRow[0].trim() + "||" + imgInfoRow[1].trim();
                    //Cat_Tag_List.add( tmp );
                }
            }
        }
        //MyVariables.setcategory_tag(Cat_Tag_List);
        //setCat_Tag_List(Cat_Tag_List);
        return tmpMetadata;
    }

    //public static List<String[]> CompareImages(List<Integer> selectedIndices, String[] params, JProgressBar progressBar, JLabel outputLabel) {
    public static void CompareImages(List<Integer> selectedIndices, String[] params, JProgressBar progressBar, JLabel outputLabel) {

        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getLoadedFiles();
        List<String> category_tagname = new ArrayList<String>();
        List<String[]> allMetadata = new ArrayList<String[]>();
        List<String[]> tableMetadata = new ArrayList<String[]>();
        String sql;
        final String[][] values = new String[1][1];
        ArrayListHolder arrayListData = new ArrayListHolder();


        Initialize();
        logger.debug("params for exiftool {}", String.join(",", params));

        // Use background thread to prepare everything
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isWindows = Utils.isOsFromMicrosoft();
                    cmdparams.add(Utils.platformExiftool());
                    // we want short tags to prevent issues with tagnames with spaces, colons and commas. And it makes our column slightly smaller.
                    cmdparams.add("-s");
                    cmdparams.addAll(Arrays.asList(params));
                    long start = System.currentTimeMillis();
                    for (int index : selectedIndices) {
                        List<String[]> tmpMetadata = new ArrayList<String[]>();
                        long astart = System.currentTimeMillis();
                        String res = Utils.getImageInfoFromSelectedFile(params, index);
                        long aend  = System.currentTimeMillis();
                        long bstart  = System.currentTimeMillis();
                        //AddToImageInfoRows(index, filename, res);
                        tmpMetadata = AddToInfoRows(index, res);
                        allMetadata.addAll(tmpMetadata);
                        long bend  = System.currentTimeMillis();
                        logger.debug("exiftool {} ms; addtoListArray {} ms", (astart - aend), (bstart - bend));
                        logger.debug("allMetadata {}", allMetadata.size());
                    }
                    long end = System.currentTimeMillis();
                    logger.debug("Reading exiftool info and adding to List {} ms", (end - start));

                    logger.debug("final allMetadata {}", allMetadata.size());
                    start = System.currentTimeMillis();
                    for (String[] oneRow : allMetadata) {
                        //logger.info(Arrays.toString(oneRow));
                        category_tagname.add(oneRow[0]);
                    }
                    Set<String> unique_cat_tag = new HashSet<String>(category_tagname);
                    end = System.currentTimeMillis();
                    logger.debug("Creating category_tagname list + hashset {} ms", (end - start));


                    // Create the category_tagname hashmap based on DB
                   /* start = System.currentTimeMillis();
                    String sql = "select distinct category,tagname from imageinforows order by category,tagname";
                    String queryresult = SQLiteJDBC.generalQuery(sql, "disk");
                    if (queryresult.length() > 0) {
                        String[] lines = queryresult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                        for (String line : lines) {
                            //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                            String[] fields = line.split("\\t", 3);
                            //category_tagname.put(fields[0], fields[1]);
                            logger.trace("category: {}, tagname {}", fields[0], fields[1]);
                            category_tagname.add(new String[] { fields[0], fields[1] });
                        }
                    }
                    end = System.currentTimeMillis();
                    logger.info("Create the category_tagname hashmap takes {} ms", (end - start)); */

                    // Now get the data per image
                    /*start = System.currentTimeMillis();
                    for (String[] cat_tag: category_tagname) {
                        sql = "select category,tagname,imgindex,filename,value from ImageInfoRows where category='" + cat_tag[0] +"' and tagname='" + cat_tag[1] + "' group by category,tagname,filename order by category,tagname";
                        queryresult = SQLiteJDBC.generalQuery(sql, "disk");
                        if (queryresult.length() > 0) {
                            String[] lines = queryresult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                            String[] values = new String[selectedIndices.size() + 2];
                            for (String line : lines) {
                                logger.debug("line: {}", line);
                                String[] fields = line.split("\\t", 5);
                                values[0] = fields[0];
                                values[1] = fields[1];
                                for (int index : selectedIndices) {
                                    values[ (Integer.parseInt(fields[2]) + 2)] = fields[4];
                                }
                            }
                            allMetadata.add(values);
                            logger.trace("array for table {}",Arrays.toString(values));
                        }
                    }
                    end = System.currentTimeMillis();
                    logger.info("Now get the data per image takes {} ms", (end - start)); */

                    /*for (String[] metadata: allMetadata) {
                        logger.info("metadata {}", Arrays.toString(metadata));
                    }*/

                    start = System.currentTimeMillis();
                    for (String cat_tag : unique_cat_tag) {
                        String[] values = new String[selectedIndices.size() + 2];
                        // Create list with 27 values: more than we will ever process on files: 2 for category and tgname, 25 for images
                        List<String> listvalues = new ArrayList<String>();
                        for (int i = 0; i < 27; i++) {
                            listvalues.add("9999");
                        }
                        for (String[] metadata : allMetadata) {
                            //logger.info("cat_tag {} metadata {}", cat_tag, metadata);
                            int counter = 2;
                            for (int index : selectedIndices) {
                                if ((cat_tag.equals(metadata[0])) && (Integer.valueOf(metadata[1]) == index)) {
                                    listvalues.set(0, metadata[2]);
                                    listvalues.set(1, metadata[3]);
                                    //listvalues.set((Integer.valueOf(index) + 2), metadata[4]);
                                    listvalues.set(counter, metadata[4]);
                                }
                                counter++;
                            }
                        }
                        //logger.info("list values {}", listvalues);
                        for (int i = 0; i < (selectedIndices.size() + 2); i++) {
                            if ((listvalues.get(i)).equals("9999")) {
                                values[i] = "";
                            } else {
                                values[i] = listvalues.get(i);
                            }
                        }
                        //logger.info("values {}", Arrays.toString(values));
                        tableMetadata.add(values);
                    }
                    end = System.currentTimeMillis();
                    logger.debug("raw data to table data {}", (end - start));

                    //Now display our data
                    CompareImagesWindow.Initialize(tableMetadata);
                    logger.debug("Now starting CompareImagesWindow to show the data");
                    progressBar.setVisible(false);
                    outputLabel.setText("");
                    //CompareImagesWindow.Initialize(allMetadata);

                } catch (Exception ex) {
                    logger.debug("Error executing command");
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(true);
                outputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.compare"));
            }
        });
        //return allMetadata;

        //////////////////////////////////////
    }
}
