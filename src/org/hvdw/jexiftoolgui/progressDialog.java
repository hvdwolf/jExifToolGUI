package org.hvdw.jexiftoolgui;

import javax.swing.*;

public class progressDialog {
    public JDialog progressdialog;

    public JDialog displayProgressDialog () {
        JProgressBar progressBar;
        JOptionPane progressPane = new JOptionPane();

        progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);

        progressPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
        progressPane.setMessage("The actions are currently being processed.\nPlease be patient");
        progressPane.add(progressBar);
        JDialog dialog = progressPane.createDialog(null, "Executing ...");
        dialog.setVisible(true);
        return dialog;
    }

    public void closeProgressDialog(JDialog progdlg) {
        progdlg.dispose();
    }

/*    public JPanel displayProgressPanel () {

        JPanel returnprogressPanel = new JPanel();

        Thread t = new Thread(new Runnable() {
            public void run() {
                JProgressBar progressBar;
                JPanel progressPanel = new JPanel();
                progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                progressBar.setStringPainted(true);

                //progressPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
                //progressPane.setMessage("The actions are currently being processed.\nPlease be patient");
                progressPanel.add(new JLabel("The actions are currently being processed.\\nPlease be patient", SwingConstants.CENTER));
                progressPanel.add(progressBar);
                //JDialog dialog = progressPanel.createDialog(null, "Executing ...");
                //dialog.setVisible(true);
                progressPanel.setVisible(true);
            }
        }
    } */


}
