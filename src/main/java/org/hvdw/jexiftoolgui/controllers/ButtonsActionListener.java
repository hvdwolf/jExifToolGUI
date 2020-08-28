package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.view.DatabasePanel;
import org.hvdw.jexiftoolgui.view.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class ButtonsActionListener implements ActionListener {
    private final static Logger logger = LoggerFactory.getLogger(ButtonsActionListener.class);

    private DatabasePanel DBP = new DatabasePanel();
    private WebView WV = new WebView();

    public JLabel OutputLabel;
    public JPanel rootPanel;
    public ButtonsActionListener(JPanel rootPanel, JLabel OutputLabel) {
        this.rootPanel = rootPanel;
        this.OutputLabel = OutputLabel;
    }

    @Override
    public void actionPerformed(ActionEvent gav) { // gav = gui ActionEvent

        if (gav.getActionCommand().equals("bSI") ) {
            logger.debug("button buttonShowImage pressed");
            Utils.displaySelectedImageInExternalViewer();
        } else if (gav.getActionCommand().equals("CommandshB")) {
            WV.HTMLView(ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommandstitle"), ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommands"), 700, 500);
            logger.debug("button CommandshelpButton pressed");
        } else if ( (gav.getActionCommand().equals("ExifhB")) || (gav.getActionCommand().equals("xmpHB")) ) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exifandxmphelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("exifhelptitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button Exifhelp or xmpHelp pressed");
        } else if (gav.getActionCommand().equals("geotHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelptitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button geotagginghelpButton pressed");
        } else if (gav.getActionCommand().equals("gpsMcb")) {
            Utils.openBrowser("https://www.mapcoordinates.net/en");
            logger.debug("button gpsMapcoordinatesButton pressed");
        } else if (gav.getActionCommand().equals("gpsHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelptitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button gpsHelpbutton pressed");
        } else if (gav.getActionCommand().equals("CopyHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatext")), ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button CopyHelpbutton pressed");
        } else if (gav.getActionCommand().equals("gpanoHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelptitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button gpanoHelpbutton pressed");
        } else if (gav.getActionCommand().equals("lensHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button lensHelpbutton pressed");
        } else if (gav.getActionCommand().equals("sPHb")) {
            logger.debug("button stringPlusHelpbutton pressed");
        } else if (gav.getActionCommand().equals("edbHb")) {
            JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbhelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbtitle"), JOptionPane.INFORMATION_MESSAGE);
            logger.debug("button edbHelpbutton pressed");
        } else if (gav.getActionCommand().equals("bDBb")) {
            DBP.DisplayDiagram();
            logger.debug("button buttonDBdiagram pressed");
        }

    }
}
