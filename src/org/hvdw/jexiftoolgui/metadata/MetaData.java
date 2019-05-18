package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.programTexts;
import org.hvdw.jexiftoolgui.progressDialog;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetaData {

    Utils myUtils = new Utils();
    progressDialog progdlg = new progressDialog();

    public void copytoxmp(int[] selectedIndices, File[] files) {
        String fpath ="";
        String TotalOutput = "";
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {"No", "Yes"};
        System.out.println("Copy all metadata to xmp format");
        int choice = JOptionPane.showOptionDialog(null, programTexts.copymetadatatoxmp,"Copy all metadata to xmp format",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            // Do something
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            String exiftool = myUtils.platformExiftool();

            //JDialog prgdlg = progdlg.displayProgressDialog();
            for (int index: selectedIndices) {
                // Unfortunately we need to do this file by file. It also means we need to initialize everything again and again
                //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                cmdparams.clear();
                //cmdparams.add(exiftool);
                //cmdparams.add("-TagsFromfile" );
                if (isWindows) {
                    cmdparams.add(exiftool);
                    cmdparams.add("-TagsFromfile");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                    cmdparams.add("\"-all>xmp:all\"");
                    cmdparams.add("-overwrite_original");
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add("/bin/sh");
                    cmdparams.add("-c");
                    cmdparams.add(exiftool + " -TagsFromfile " + files[index].getPath() + " '-all:all>xmp:all' -overwrite_original  " + files[index].getPath());
                }
                try {
                    String res = myUtils.runCommand(cmdparams);
                    System.out.println(res);
                    TotalOutput += res;
                } catch(IOException | InterruptedException ex) {
                    System.out.println("Error executing command");
                }
            }
            //progdlg.closeProgressDialog(prgdlg);
            myUtils.runCommandOutput(TotalOutput);
        }

    }

    public void repairJPGmetadata(int[] selectedIndices, File[] files, JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        String[] options = {"No", "Yes"};
        System.out.println("Repair corrupted metadata in JPG(s)");
        int choice = JOptionPane.showOptionDialog(null, String.format(programTexts.HTML, 450, programTexts.repairJPGmetadata),"Repair corrupted metadata in JPG(s)",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 1) { //Yes
            //System.out.println("Yes");
            cmdparams.add(myUtils.platformExiftool());
            cmdparams.add("-overwrite_original");
            for (String s : MyConstants.repairJPGmetadata) {
                cmdparams.add(s);
            }
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            for (int index: selectedIndices) {
                //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                if (isWindows) {
                    cmdparams.add(files[index].getPath().replace("\\", "/"));
                } else {
                    cmdparams.add(files[index].getPath());
                }
            }
            // export metadata
            try {
                String res = myUtils.runCommand(cmdparams);
                System.out.println(res);
                myUtils.runCommandOutput(res);
            } catch(IOException | InterruptedException ex) {
                System.out.println("Error executing command");
            }
        }
    }

    public void CopyMetaData(JRadioButton[] CopyMetaDataRadiobuttons, JCheckBox[] CopyMetaDataCheckBoxes, int selectedRow, int[] selectedIndices, File[] files) {
        boolean atLeastOneSelected = false;
        String fpath ="";

        List<String> params = new ArrayList<String>();
        params.add(myUtils.platformExiftool());
        params.add("-TagsFromfile");
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            fpath = files[selectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[selectedRow].getPath();
        }

        params.add(fpath);
        // which options selected?
        StringBuilder Message = new StringBuilder("<html>You have selected to copy:<br>");
        if (CopyMetaDataRadiobuttons[0].isSelected()) {
            Message.append("All metadata writing info to same named tags to preferred groups<br><br>");
            atLeastOneSelected = true;
        } else if (CopyMetaDataRadiobuttons[1].isSelected()) {
            Message.append("All metadata preserving the original tag groups<br><br>");
            params.add("-all:all");
            atLeastOneSelected = true;
        } else { // The copySelectiveMetadataradioButton
            Message.append("<ul>");
            if (CopyMetaDataCheckBoxes[0].isSelected()) {
                Message.append("<li>the exif data</li>");
                params.add("-exif:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[1].isSelected()) {
                Message.append("<li>the xmp data</li>");
                params.add("-xmp:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[2].isSelected()) {
                Message.append("<li>the iptc data</li>");
                params.add("-iptc:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[3].isSelected()) {
                Message.append("<li>the ICC data</li>");
                params.add("-icc_profile:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[4].isSelected()) {
                Message.append("<li>the gps data</li>");
                params.add("-gps:all");
                atLeastOneSelected = true;
            }
            if (CopyMetaDataCheckBoxes[5].isSelected()) {
                Message.append("<li>the jfif (header) data</li>");
                params.add("-jfif:all");
                atLeastOneSelected = true;
            }
            Message.append("</ul><br><br>");
        }
        if (!CopyMetaDataCheckBoxes[6].isSelected()) {
            params.add("-overwrite_original");
        }
        Message.append("Is this correct?</html>");
        if (atLeastOneSelected) {
            String[] options = {"Cancel", "Continue"};
            int choice = JOptionPane.showOptionDialog(null, Message,"You want to copy metadata",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 1) { //Yes
                // Copy metadata
                String[] etparams = params.toArray(new String[0]);
                for (int index: selectedIndices) {
                    //System.out.println("index: " + index + "  image path:" + files[index].getPath());
                    if (isWindows) {
                        params.add(files[index].getPath().replace("\\", "/"));
                    } else {
                        params.add(files[index].getPath());
                    }
                }
                // export metadata
                try {
                    String res = myUtils.runCommand(params);
                    System.out.println(res);
                    myUtils.runCommandOutput(res);
                } catch(IOException | InterruptedException ex) {
                    System.out.println("Error executing command");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, programTexts.NoOptionSelected,"No copy option selected",JOptionPane.WARNING_MESSAGE);
        }
    }


}
