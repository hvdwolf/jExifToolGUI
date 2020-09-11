package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.view.SelectFavorite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class YourCommands {

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(YourCommands.class);

    private SelectFavorite SelFav = new SelectFavorite();

    public void executeCommands(String Commands, JTextArea Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
    //public void executeCommands(String Commands, JTextArea Output, int[] selectedIndices, File[] files) {
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File[] files = MyVariables.getSelectedFiles();
        String fpath ="";
        String TotalOutput = "";
        List<String> cmdparams = new ArrayList<String>();

        if (UseNonPropFontradioButton.isSelected()) {
            Output.setFont(new Font("monospaced", Font.PLAIN, 12));
        } else {
            Output.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        Commands = Commands.trim();
        String exiftool = Utils.platformExiftool();

        for (int index: selectedIndices) {
            // we do this image by image to get proper output
            cmdparams.clear();
            cmdparams.add(exiftool);
            //cmdparams.add("-h");
            if (Commands.contains(" ")) {
                String[] splitCommands = Commands.split("\\s+");
                Collections.addAll(cmdparams, splitCommands);
            } else {
                cmdparams.add(Commands);
            }

            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    String res = CommandRunner.runCommand(cmdparams);
                    //String res = CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
                    Output.append("============= \"");
                    Output.append(files[index].getPath());
                    Output.append("\" =============" + System.lineSeparator());
                    Output.append(res);
                    //Output.setText( Output.getText() + res + "<br><br>");
                    Output.append(System.lineSeparator() + System.lineSeparator());
                    // progressbar enabled immedately after this void run starts in the InvokeLater, so I disable it here at the end of this void run
                    progressBar.setVisible(false);
                } catch(IOException | InterruptedException ex) {
                    logger.error("Error executing command", ex);
                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setVisible(true);
                }
            });

        }
    }

    public void LoadCommandFavorite(JPanel rootpanel, JTextField CommandsParameterstextField) {
        String queryresult = "";

        String favName = SelFav.showDialog(rootpanel, "Exiftool_Command");
        logger.debug("returned selected favorite: " + favName);
        if (!"".equals(favName)) {
            String sql = "select command_query from userFavorites where favorite_type='Exiftool_Command' and favorite_name='" + favName + "' limit 1";
            queryresult = SQLiteJDBC.generalQuery(sql);
            logger.debug("returned command: " + queryresult);

            CommandsParameterstextField.setText(queryresult);
        }
    }
}
