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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExifToolCommands {

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ExifToolCommands.class);

    private SelectFavorite SelFav = new SelectFavorite();

    public void executeCommands(String Commands, JEditorPane Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar, String ETCommandsFoldertextField, boolean IncludeSubFolders) {
        int[] selectedIndices = null;
        File[] files = null;
        int counter = 0;
        if ("".equals(ETCommandsFoldertextField)) {
            selectedIndices = MyVariables.getSelectedFilenamesIndices();
            files = MyVariables.getLoadedFiles();
        }
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
        logger.debug(" Commands \"{}\"", Commands);
        String exiftool = Utils.platformExiftool();

        if (Commands.contains("-t ") || Commands.contains("-tab ")) {
            logger.debug("tabbed output requested, overrule font setting");
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        } else if (Commands.contains("-htmlDump ")) {
            logger.debug("htmlDump requested");
            htmlDump = true;
        } else if (Commands.contains("-h ") || Commands.contains("-htmlFormat ")) {
            logger.debug("html output requested");
            htmlOutput = true;
        }


        cmdparams.clear();
        StringBuilder tmpcmpstring = new StringBuilder();
        if (Utils.isOsFromMicrosoft()) {
            cmdparams.add("\"" + Utils.platformExiftool().replace("\\", "/") + "\"" );
            String[] splitCommands = orgCommands.split(" ");
            List<String> listCommands = Arrays.asList(splitCommands);
            cmdparams.addAll(listCommands);
        } else { //Linux & MacOS
            cmdparams.add("/bin/sh");
            cmdparams.add("-c");
            tmpcmpstring = new StringBuilder(Utils.platformExiftool().replaceAll(" ", "\\ ") + " " + orgCommands + " ");
        }

        if ( !("".equals(ETCommandsFoldertextField)) ) { // The folder line is used
            if (IncludeSubFolders) {
                if (Utils.isOsFromMicrosoft()) {
                    cmdparams.add("-r");
                } else {
                    tmpcmpstring.append(" ").append("-r");
                }
            }
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add("\"" + ETCommandsFoldertextField + "\"");
            } else {
                //tmpcmpstring.append(" ").append("\"" + ETCommandsFoldertextField.replaceAll(" ", "\\ ") + "\"");
                tmpcmpstring.append(" \"" + ETCommandsFoldertextField + "\" ");
            }

        } else {
            counter = 1;
            for (int index : selectedIndices) {
                int finalIMG = selectedIndices.length;
                logger.debug("finalIMG {}", finalIMG);

                if (Utils.isOsFromMicrosoft()) {
                    cmdparams.add("\"" + files[index].getPath() + "\"");
                } else {
                    tmpcmpstring.append(" \"" + files[index].getPath() + "\" ");
                }
                //try

            }
        }
        if (!Utils.isOsFromMicrosoft()) { // Do this on linux and MacOS due to bash command
            cmdparams.add(tmpcmpstring.toString());
        }

        logger.info("cmdparams {}", cmdparams.toString());

        Executor executor = Executors.newSingleThreadExecutor();
        boolean finalHtmlOutput = htmlOutput;
        boolean finalHtmlDump = htmlDump;
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
