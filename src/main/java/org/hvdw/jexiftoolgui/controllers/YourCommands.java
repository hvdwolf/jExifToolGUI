package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class YourCommands {

    public void ExecuteCommands(String Commands, JTextArea Output, JRadioButton UseNonPropFontradioButton, JProgressBar progressBar) {
    //public void ExecuteCommands(String Commands, JTextArea Output, int[] selectedIndices, File[] files) {
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
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        String exiftool = Utils.platformExiftool();

        //JDialog prgdlg = progdlg.displayProgressDialog();
        //Output.append("<html>");
        //Output.setText("<html");
        for (int index: selectedIndices) {
            // we do this image by image to get proper output
            cmdparams.clear();
            cmdparams.add(exiftool);
            //cmdparams.add("-h");
            if (Commands.contains(" ")) {
                String[] splitCommands = Commands.split("\\s+");
                for (String Command: splitCommands) {
                    cmdparams.add(Command);
                }
                //cmdparams.addAll(splitCommands);
            } else {
                cmdparams.add(Commands);
            }

            if (isWindows) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
            /*try {
                String res = CommandRunner.runCommand(cmdparams);
                //String res = CommandRunner.RunCommandWithProgress(cmdparams, progressBar);
                Output.append("============= \"");
                Output.append(files[index].getPath());
                Output.append("\" =============" + System.lineSeparator());
                Output.append(res);
                //Output.setText( Output.getText() + res + "<br><br>");
                Output.append(System.lineSeparator() + System.lineSeparator());
            } catch(IOException | InterruptedException ex) {
                System.out.println("Error executing command");
            }*/
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String res = CommandRunner.runCommand(cmdparams);
                        //String res = CommandRunner.RunCommandWithProgress(cmdparams, progressBar);
                        Output.append("============= \"");
                        Output.append(files[index].getPath());
                        Output.append("\" =============" + System.lineSeparator());
                        Output.append(res);
                        //Output.setText( Output.getText() + res + "<br><br>");
                        Output.append(System.lineSeparator() + System.lineSeparator());
                        // progressbar enabled immedately after this void run starts in the InvokeLater, so I disable it here at the end of this void run
                        progressBar.setVisible(false);
                    } catch(IOException | InterruptedException ex) {
                        System.out.println("Error executing command");
                    }
                }
            });
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setVisible(true);
                }
            });

        }
    }
}
