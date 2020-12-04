package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.view.SelectFavorite;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExifToolCommands {

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExifToolCommands.class);

    private SelectFavorite SelFav = new SelectFavorite();

    public void executeCommands(String Commands, JEditorPane Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
//    public void executeCommands(String Commands, JTextArea Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        String fpath ="";
        String Result = "";
        int firstIndexOf = 0;
        StringBuilder commandOutput = new StringBuilder("");
        boolean htmlOutput = false;
        boolean htmlDump = false;
        List<String> cmdparams = new ArrayList<String>();
        List<String> newCommands = new ArrayList<>();

        if (UseNonPropFontradioButton.isSelected()) {
            Output.setFont(new Font("monospaced", Font.PLAIN, 12));
        } else {
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        Commands = Commands.trim();
        String orgCommands = Commands;
        logger.info(" Commands \"{}\"", Commands);
        String exiftool = Utils.platformExiftool();

        if (Commands.contains("-t ") || Commands.contains("-tab ")) {
            logger.debug("tabbed output requested, overrule font setting");
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        } else if (Commands.contains("-htmlDump ")) {
            logger.debug("htmlDump requested");
            htmlDump = true;
            //Output.setContentType("text/html");
        } else if (Commands.contains("-h ") || Commands.contains("-htmlFormat ")) {
            logger.debug("html output requested");
            htmlOutput = true;
            //Output.setContentType("text/html");
            //commandOutput.append("<html><body><font face=\"Helvetica\">");
        }

        // Build the parameters
        //cmdparams.clear();
        //newCommands.clear();
        //cmdparams.add(exiftool);
        /*
        // Below splits the commands on the exiftool leading - before the tag EXCEPT the starting one as it mostly does not contain a leading space
        // so first check for leading "-"
        if (Commands.startsWith("-")) {
            Commands = " " + Commands;
        }
        // We could have something like: "-FileModifyDate<DateTimeOriginal" or '-FileModifyDate<DateTimeOriginal'
        Commands = Commands.replace("\"-", "-").replace("'-", "-");
        Commands = Commands.replace(" \"", " ").replace(" '", " ");
        // And our total command could also start with or end with " or '
        if (Commands.startsWith("\"") || Commands.startsWith("'")) {
            Commands = Commands.substring(1);
        }
        if (Commands.endsWith("\"") || Commands.endsWith("'")) {
            Commands = Commands.substring(0, (Commands.length()-1));
        }
        logger.info("modded Commands \"{}\"", Commands);
        // Now we continue with the total parsing
        if (Commands.contains(" -")) {
            List<String> tmpcommands = Arrays.asList(Commands.split("\\s-"));
            String tmp;
            for (int i = 0; i < tmpcommands.size(); i++) { // First item (i=0) is empty
                // we removed (split actually) on the " -", so we need to add the "-" again
                // and we trim the command to get rid of leading and trailing spaces
                tmp = tmpcommands.get(i).trim();
                if (tmp.length() > 0) {
                    tmp = "-" + tmpcommands.get(i).trim();
                }
                // We hava a couple of options
                // Not a "combined" string like (only) -b (binary, or -h (html output) etcetera
                // like date format: -d %Y%m%d_%H%M%%-c_%%f.%%e
                // or tag-value: -exif:artist=Peter Parker aka Spiderman
                // or even something like: -FileModifyDate<DateTimeOriginal
                // first check on "="
                if (tmp.contains("=")) {
                    // nothing to do for the command. We can simply add it, like: -exif:artist=Peter Parker aka Spiderman
                    // However, it might still contain a single quote or double quote from a previous command like "-FileModifyDate<DateTimeOriginal"
                    newCommands.add(tmp.replace("\"", "").replace("'", ""));
                } else if (tmp.contains("<") || tmp.contains(">")) {
                    // like -FileModifyDate<DateTimeOriginal
                    // remove everything around it (double or single quotes by the user) and do our thing to it.
                    // Most probably the leading quote or double quote is already missing as we split on the leading -
                    // which could be "- or '-
                    tmp = tmp.replace("\"", "").replace("'", "");
                    if (Utils.isOsFromMicrosoft()) {
                        newCommands.add("\"" + tmp + "\"");
                    } else {
                        newCommands.add("\"" + tmp + "\"");
                    }
                } else if (tmp.contains(" ")) {
                    // We already covered the "=" with a value with lots of spaces
                    //Nowwe have something like: -d %Y%m%d_%H%M%%-c_%%f.%%e
                    // As the value can again contain lots of spaces like in a text, we can't
                    // simply split on space but need the substring to the first space,and then
                    // use everything behind it
                    // Again we need to cehck on a leading single or double quote or trailing single or double quote and remove it
                    if (tmp.startsWith("\"") || tmp.startsWith("'")) {
                        tmp = tmp.substring(1);
                    }
                    if (tmp.endsWith("\"") || tmp.endsWith("'")) {
                        tmp = tmp.substring(0, (tmp.length()-1));
                    }
                    firstIndexOf = tmp.indexOf(" ");
                    newCommands.add(tmp.substring(0, (firstIndexOf - 1) ));
                    newCommands.add(tmp.substring( (firstIndexOf + 1)) );
                } else {
                    // Absolutely nothing of the above, so simply something like -iso or -imagewidth or -tab or -h
                    if (tmp.length() > 0) {
                        newCommands.add(tmp);
                    }
                }
                //logger.info("newCommands {}", newCommands.toString());

            }
            logger.info("new commands after complete parsing {}", newCommands.toString());
            cmdparams.addAll(newCommands);
        } else {
            // The entire command does not contain exiftool options like -d, -exif:<tag> etcetera
            // but it can still contain something like: "-FileModifyDate<DateTimeOriginal" in
            // which we now removed all single or double quotes
            if (Commands.contains("<") || Commands.contains(">")) {
                if (Utils.isOsFromMicrosoft()) {
                    cmdparams.add("\"" + Commands + "\"");
                } else {
                    cmdparams.add("\"" + Commands + "\"");
                }
            }
        } */


        // Try out
        cmdparams.clear();
        StringBuilder tmpcmpstring = new StringBuilder();
        if (Utils.isOsFromMicrosoft()) {
            cmdparams.add("cmd");
            cmdparams.add("/c");
            //cmdparams.add(Utils.platformExiftool());
            //tmpcmpstring = new StringBuilder( " start \"\" " + Utils.platformExiftool() + " " + orgCommands + " ");
            tmpcmpstring = new StringBuilder( " " + Utils.platformExiftool() + " " + orgCommands + " ");
            //cmdparams.add(tmpcmpstring.toString());
        } else { //Linux & MacOS
            cmdparams.add("/bin/sh");
            cmdparams.add("-c");
            tmpcmpstring = new StringBuilder(Utils.platformExiftool() + " " + orgCommands + " ");
        }

        int counter = 1;
        for (int index: selectedIndices) {
            int finalIMG = selectedIndices.length;
            logger.debug("finalIMG {}", finalIMG);

            if (Utils.isOsFromMicrosoft()) {
                //cmdparams.add(files[index].getPath().replace("\\", "/"));
                tmpcmpstring.append(" ").append("\"" + files[index].getPath().replace("\\", "/") + "\"");
            } else {
                tmpcmpstring.append(" ").append(files[index].getPath().replace(" ", "\\ "));
            }
            //try

        }
        // for Windows, linux and MacOS
        cmdparams.add(tmpcmpstring.toString());
        /*if (Utils.isOsFromMicrosoft()) {
        } else {
            cmdparams.add(tmpcmpstring.toString());
        }*/
        logger.info("cmdparams {}", cmdparams.toString());

        Executor executor = Executors.newSingleThreadExecutor();
        boolean finalHtmlOutput = htmlOutput;
        boolean finalHtmlDump = htmlDump;
        int threadCounter = counter;
        logger.debug("threadCounter {}", threadCounter);
        executor.execute(() -> {
            try {
                String res = CommandRunner.runCommand(cmdparams);
                if (finalHtmlOutput) {
                    Output.setContentType("text/html");
                    commandOutput.append("<html><body><font face=\"Helvetica\">");
                    commandOutput.append(res);
                    commandOutput.append("<br><br>");
                    progressBar.setVisible(false);
                    commandOutput.append("</body></html>");
                    Output.setText(commandOutput.toString());
                } else if (finalHtmlDump) {
                    Output.setContentType("text/html");
                    commandOutput.append(res);
                    Output.setText(commandOutput.toString());
                } else {
                    Output.setContentType("text/plain");
                    commandOutput.append(res);
                    commandOutput.append("\n\n");
                    Output.setText(commandOutput.toString());
                    // progressbar enabled immedately after this void run starts in the InvokeLater, so I disable it here at the end of this void run
                    progressBar.setVisible(false);
                }
            } catch (IOException | InterruptedException ex) {
                logger.error("Error executing command", ex);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(true);
            }
        });
        counter++;
    }

    public void LoadCommandFavorite(JPanel rootpanel, JTextField CommandsParameterstextField) {
        String queryresult = "";

        String favName = SelFav.showDialog(rootpanel, "Exiftool_Command");
        logger.debug("returned selected favorite: " + favName);
        if (!"".equals(favName)) {
            String sql = "select command_query from userFavorites where favorite_type='Exiftool_Command' and favorite_name='" + favName + "' limit 1";
            queryresult = SQLiteJDBC.generalQuery(sql, "disk");
            logger.debug("returned command: " + queryresult);

            CommandsParameterstextField.setText(queryresult);
        }
    }
}
