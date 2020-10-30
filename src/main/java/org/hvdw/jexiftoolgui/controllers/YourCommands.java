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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class YourCommands {

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(YourCommands.class);

    private SelectFavorite SelFav = new SelectFavorite();

    public void executeCommands(String Commands, JEditorPane Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
//    public void executeCommands(String Commands, JTextArea Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getLoadedFiles();
        String fpath ="";
        String Result = "";
        StringBuilder commandOutput = new StringBuilder("");
        //AtomicReference<String> commandOutput = new AtomicReference<>("");
        boolean htmlOutput = false;
        boolean htmlDump = false;
        List<String> cmdparams = new ArrayList<String>();

        if (UseNonPropFontradioButton.isSelected()) {
            Output.setFont(new Font("monospaced", Font.PLAIN, 12));
        } else {
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        Commands = Commands.trim();
        logger.info(" Commands {}", Commands);
        String exiftool = Utils.platformExiftool();

        if (Commands.contains("-t") || Commands.contains("-tab")) {
            logger.debug("tabbed output requested, overrule font setting");
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        } else if (Commands.contains("-htmlDump")) {
            logger.debug("htmlDump requested");
            htmlDump = true;
            Output.setContentType("text/html");
        } else if (Commands.contains("-h") || Commands.contains("-htmlFormat")) {
            logger.debug("html output requested");
            htmlOutput = true;
            Output.setContentType("text/html");
            commandOutput.append("<html><body><font face=\"Helvetica\">");
        }



        int counter = 1;
        for (int index: selectedIndices) {
            int finalIMG = selectedIndices.length;
            logger.debug("finalIMG {}", finalIMG);
            // we do this image by image to get proper output
            cmdparams.clear();
            cmdparams.add(exiftool);
            //cmdparams.add("-h");
            if (Commands.contains(" -")) {
                //String[] splitCommands = Commands.split("\\s+"); //split also on multiple spaces
                //String[] splitCommands = Commands.split("\\s-");
                List<String> tmpcommands = Arrays.asList(Commands.split("\\s-"));
                //logger.info("splitCommands {}", splitCommands.toString());
                String tmp;
                for (int i = 1; i < tmpcommands.size(); i++) {
                    tmp = tmpcommands.get(i).trim();
                    if (tmp.startsWith("-")) { // first element starts with -, or maybe with <space>- if the user is sloppy
                        tmpcommands.set(i, tmpcommands.get(i).trim());
                    } else {
                        tmpcommands.set(i, "-" + tmpcommands.get(i).trim());
                    }
                    logger.debug("command after split {}", tmpcommands.get(i));
                }
                //String[] splitCommands = tmpcommands.toArray(new String[tmpcommands.size()]);
                //logger.info("splitCommands {}", splitCommands.toString());
                //Collections.addAll(cmdparams, splitCommands);
                cmdparams.addAll(tmpcommands);
            } else {
                cmdparams.add(Commands);
            }

            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }

            Executor executor = Executors.newSingleThreadExecutor();
            boolean finalHtmlOutput = htmlOutput;
            boolean finalHtmlDump = htmlDump;
            int threadCounter = counter;
            logger.debug("threadCounter {}", threadCounter);
            executor.execute(() -> {
                try {
                    String res = CommandRunner.runCommand(cmdparams);
                    //String res = CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
                    if (finalHtmlOutput) {
                        commandOutput.append(res);
                        commandOutput.append("<br><br>");
                        progressBar.setVisible(false);

                        if (threadCounter == finalIMG) {
                            commandOutput.append("</body></html>");
                            Output.setText(commandOutput.toString());
                        }
                    } else if (finalHtmlDump) {
                        commandOutput.append(res);
                        Output.setText(commandOutput.toString());
                    } else {
                        Output.setContentType("text/plain");
                        commandOutput.append("============= \"" + files[index].getPath() + "\" =============\n");
                        commandOutput.append(res);
                        //Output.append(res);
                        commandOutput.append("\n\n");
                        //Output.append("\n\n");
                        Output.setText(commandOutput.toString());
                        // progressbar enabled immedately after this void run starts in the InvokeLater, so I disable it here at the end of this void run
                        progressBar.setVisible(false);
                    }
                } catch(IOException | InterruptedException ex) {
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
