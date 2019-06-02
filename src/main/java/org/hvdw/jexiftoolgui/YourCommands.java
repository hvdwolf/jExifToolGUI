package org.hvdw.jexiftoolgui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YourCommands {

    public void ExecuteCommands(String Commands, JTextArea Output, JRadioButton UseNonPropFontradioButton, int[] selectedIndices, File[] files) {
    //public void ExecuteCommands(String Commands, JTextArea Output, int[] selectedIndices, File[] files) {
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
            } else {
                cmdparams.add(Commands);
            }

            if (isWindows) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
            try {
                String res = CommandRunner.runCommand(cmdparams);
                //System.out.println(res);
                Output.append("============= \"" + files[index].getPath() +  "\" =============\n");
                //Output.setText( Output.getText() + "============= \"" + files[index].getPath() +  "\" =============<br>");
                Output.append(res);
                //Output.setText( Output.getText() + res + "<br><br>");
                Output.append("\n\n");
            } catch(IOException | InterruptedException ex) {
                System.out.println("Error executing command");
            }
        }
        //Output.append("</html>");
        //Output.setText(Output.getText() + "</html>");
    }
}
