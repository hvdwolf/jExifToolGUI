package org.hvdw.jexiftoolgui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.Executor;

public class CommandRunner {

    /*
     * All exiftool commands go through this method
     */
    public static String runCommand(List<String> cmdparams) throws InterruptedException, IOException {

        String res = "";
        DataOutputStream outputStream = null;
        InputStream response = null;
        System.out.println(cmdparams.toString());

        ProcessBuilder builder = new ProcessBuilder(cmdparams);
        //System.out.println("Did ProcessBuilder builder = new ProcessBuilder(cmdparams);");
        try {
            builder.redirectErrorStream(true);
            Process process = builder.start();
            //Use a buffered reader to prevent hangs on Windows
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                res = res + line + System.lineSeparator();
            //System.out.println("tasklist: " + line);
            int errCode = process.waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = e.getMessage();
        }
        return res;
    }

    /*
    * This shows the output of exiftool after it has run
     */
    public static void OutputAfterCommand (String output) {
        JTextArea textArea = new JTextArea(output);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
        JOptionPane.showMessageDialog(null, scrollPane,"Output from the given command",JOptionPane.INFORMATION_MESSAGE);
    }

    /*
    * This executes the commands via runCommand and shows/hides the progress bar
     */
    public static void RunCommandWithProgress (List<String> cmdparams, JProgressBar progressBar) {
        // Create executor thread to be able to update my gui when longer methods run
        Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = runCommand(cmdparams);
                    System.out.println(res);
                    progressBar.setVisible(false);
                    OutputAfterCommand(res);
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
