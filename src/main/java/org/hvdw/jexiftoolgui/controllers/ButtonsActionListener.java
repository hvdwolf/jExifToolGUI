package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.editpane.EditGeotaggingdata;
import org.hvdw.jexiftoolgui.view.AddFavorite;
import org.hvdw.jexiftoolgui.view.DatabasePanel;
import org.hvdw.jexiftoolgui.view.MetadataUserCombinations;
import org.hvdw.jexiftoolgui.view.SimpleWebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static org.slf4j.LoggerFactory.getLogger;

public class ButtonsActionListener implements ActionListener {
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ButtonsActionListener.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(ButtonsActionListener.class);

    private DatabasePanel DBP = new DatabasePanel();
    private SimpleWebView WV = new SimpleWebView();
    private AddFavorite AddFav = new AddFavorite();
    private YourCommands YourCmnds = new YourCommands();
    private EditGeotaggingdata EGd = new EditGeotaggingdata();
    private MetadataUserCombinations MD = new MetadataUserCombinations();


    public JLabel OutputLabel;
    public JPanel rootPanel;
    public JTextField CommandsParameterstextField;
    public JTextField geotaggingImgFoldertextField;
    public JTextField geotaggingGPSLogtextField;
    public JTextField sqlQuerytextField;
    public JComboBox UserCombiscomboBox;

    public ButtonsActionListener(JPanel rootPanel, JLabel OutputLabel, JTextField CommandsParameterstextField, JTextField geotaggingImgFoldertextField, JTextField geotaggingGPSLogtextField, JTextField sqlQuerytextField, JComboBox UserCombiscomboBox) {

        this.rootPanel = rootPanel;
        this.OutputLabel = OutputLabel;
        this.CommandsParameterstextField = CommandsParameterstextField;
        this.geotaggingImgFoldertextField = geotaggingImgFoldertextField;
        this.geotaggingGPSLogtextField = geotaggingGPSLogtextField;
        this.sqlQuerytextField = sqlQuerytextField;
        this.UserCombiscomboBox = UserCombiscomboBox;
    }

    @Override
    public void actionPerformed(ActionEvent gav) { // gav = gui ActionEvent

        // This is not nice object oriented programming but gives a nice clear structured overview
        switch (gav.getActionCommand()) {
            case "bSI":
                logger.debug("button buttonShowImage pressed");
                Utils.displaySelectedImageInExternalViewer();
                break;
            case "CommandshB":
                logger.debug("button CommandshelpButton pressed");
                WV.HTMLView(ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommandstitle"), ResourceBundle.getBundle("translations/program_help_texts").getString("yourcommands"), 700, 500);
                break;
            case "ExifhB":
            case "xmpHB":
                logger.debug("button Exifhelp or xmpHelp pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exifandxmphelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("exifhelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "geotHb":
                logger.debug("button geotagginghelpButton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("geotagginghelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "gpsMcb":
                logger.debug("button gpsMapcoordinatesButton pressed");
                Utils.openBrowser("https://www.mapcoordinates.net/en");
                break;
            case "gpsHb":
                logger.debug("button gpsHelpbutton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpshelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "CopyHb":
                logger.debug("button CopyHelpbutton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatext")), ResourceBundle.getBundle("translations/program_help_texts").getString("copymetadatatitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "gpanoHb":
                logger.debug("button gpanoHelpbutton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelp")), ResourceBundle.getBundle("translations/program_help_texts").getString("gpanohelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "lensHb":
                logger.debug("button lensHelpbutton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 450, ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("lenshelptitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "sPHb":
                logger.debug("button stringPlusHelpbutton pressed");
                break;
            case "edbHb":
                logger.debug("button edbHelpbutton pressed");
                JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbhelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbtitle"), JOptionPane.INFORMATION_MESSAGE);
                break;
            case "bDBb":
                logger.debug("button buttonDBdiagram pressed");
                DBP.DisplayDiagram();
                break;
            case "CommandsclearPSFB":
                logger.debug("button CommandsclearParameterSFieldButton pressed");
                CommandsParameterstextField.setText("");
                break;
            case "ACommFavorb":
                logger.debug("button AddCommandFavoritebutton pressed");
                if (CommandsParameterstextField.getText().length()>0) {
                    AddFav.showDialog(rootPanel, "Exiftool_Command", CommandsParameterstextField.getText());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, ResourceBundle.getBundle("translations/program_strings").getString("msd.nocommandparams"), ResourceBundle.getBundle("translations/program_strings").getString("msd.nocommandparams"), JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "LCommFavb":
                logger.debug("button CommandsclearParameterSFieldButton pressed");
                YourCmnds.LoadCommandFavorite(rootPanel, CommandsParameterstextField);
                break;
            case "geoIFb":
                logger.debug("button geotaggingImgFolderbutton pressed");
                String ImgPath = EGd.getImagePath(rootPanel);
                if (!"".equals(ImgPath)) {
                    geotaggingImgFoldertextField.setText(ImgPath);
                }
                break;
            case "geoGPSLb":
                logger.debug("button geotaggingGPSLogbutton pressed");
                String TrackFile = EGd.gpsLogFile(rootPanel);
                if (!"".equals(TrackFile)) {
                    geotaggingGPSLogtextField.setText(TrackFile);
                }
                break;
            case "SQb":
                logger.debug("button SaveQuerybutton pressed");
                if (sqlQuerytextField.getText().length()>0) {
                    AddFav.showDialog(rootPanel, "DB_query", sqlQuerytextField.getText());
                } else {
                    JOptionPane.showMessageDialog(rootPanel, "No query given", "No query", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "lQb":
                logger.debug("button loadQuerybutton pressed");
                DBP.LoadQueryFavorite(rootPanel,sqlQuerytextField);
                break;
            case "udcCNB":
                logger.debug("button udcCreateNewButton pressed");
                MD.showDialog(rootPanel);
                // Update GPS box, no matter whether we were succesful or not
                String sqlsets = SQLiteJDBC.getdefinedCustomSets();
                String[] views = sqlsets.split("\\r?\\n"); // split on new lines
                UserCombiscomboBox.setModel(new DefaultComboBoxModel(views));
                break;
            case "udcHb":
                logger.debug("button udcHelpbutton pressed");
                //Utils.openBrowser(ProgramTexts.ProjectWebSite + "/manual/jexiftoolgui_usercombis.html");
                Utils.openBrowser(ProgramTexts.ProjectWebSite + "/manual/index.html#userdefinedmetadatacombinations");
                break;
        }


    }
}
