package org.hvdw.jexiftoolgui.model;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import org.hvdw.jexiftoolgui.MyConstants;
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

    private static void dbAction(String sql, String comment) {
        String qr = SQLiteJDBC.insertUpdateQuery(sql, "disk");
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
                for (String line : lines) {
                    String[] imgInfoRow = line.split("\\t", 3);
                    sql = "Insert into ImageInfoRows(imgindex,filename,category,tagname,value) values(" +
                            index + ",\"" + filename + "\",\"" + imgInfoRow[0].trim() + "\",\"" + imgInfoRow[1].trim() + "\",\"" +imgInfoRow[2].trim() + "\")";
                    dbAction(sql, sql);
                }
            }
        }
    }

    //public static List<String[]> CompareImages(List<Integer> selectedIndices, String[] params, JProgressBar progressBar, JLabel outputLabel) {
    public static void CompareImages(List<Integer> selectedIndices, String[] params, JProgressBar progressBar, JLabel outputLabel) {

        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getLoadedFiles();
        List<String[]> category_tagname = new ArrayList<String[]>();
        List<String[]> allMetadata = new ArrayList<String[]>();
        String sql;
        String queryresult;

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
                    for (int index : selectedIndices) {
                        String res = Utils.getImageInfoFromSelectedFile(params, index);
                        String filename = files[index].getName();
                        AddToImageInfoRows(index, filename, res);
                    }

                    // Create the category_tagname hashmap
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

                    // Now get the data per image
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

                    progressBar.setVisible(false);
                    outputLabel.setText("");
                    //Now display our data
                    //CompareImagesWindow.Initialize(allMetadata);
                    logger.debug("Now starting CompareImagesWindow to show the data");
                    CompareImagesWindow.Initialize(allMetadata);

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
