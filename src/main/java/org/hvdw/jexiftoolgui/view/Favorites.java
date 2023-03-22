package org.hvdw.jexiftoolgui.view;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class Favorites extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField favoritenametextField;
    private JLabel copiedcommandquerylabel;
    private JScrollPane scrollPane;
    private JTable favoritestable;
    private JLabel lblCommandQuery;
    private JLabel commandqueryTopText;
    private JLabel favoritenamelabel;
    private JLabel favselectlabel;
    private JButton buttonDelete;

    private JPanel jp = null;
    private String favtype = "";
    private String chosenName = "";
    private String favtypeText = "";
    private String cmd_qry = "";
    private String favorite_name = "";
    String writeresult = "";
    String favAction = "";
    HashMap<String, String> loadedFavorites = new HashMap<String, String>();
    private final String commandTxt = "<html>" + ResourceBundle.getBundle("translations/program_strings").getString("fav.commandtext") + "<br><br></html>";
    private final String queryTxt = "<html>" + ResourceBundle.getBundle("translations/program_strings").getString("fav.querytext") + "<br><br></html>";

    String strjexiftoolguifolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER;
    File favoritesFile = new File(strjexiftoolguifolder + File.separator + "favorites.hashmap");

    private final static Logger logger = (Logger) LoggerFactory.getLogger(Favorites.class);


    public Favorites() {
        setContentPane(contentPane);
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        contentPane.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        favoritestable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) favoritestable.getModel();
                int selectedRowIndex = favoritestable.getSelectedRow();
                if (!"SelectFavorite".equals(favAction)) {
                    favoritenamelabel.setVisible(true);
                    favoritenametextField.setText(model.getValueAt(selectedRowIndex, 0).toString());
                    favoritenametextField.setVisible(true);
                    copiedcommandquerylabel.setText(model.getValueAt(selectedRowIndex, 1).toString());
                    copiedcommandquerylabel.setVisible(true);
                }
                chosenName = model.getValueAt(selectedRowIndex, 0).toString();
                favorite_name = model.getValueAt(selectedRowIndex, 1).toString();
            }
        });


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // delete button only active on deletion of favorites
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.info("favorite for deletion {}", favorite_name);
                //loadedFavorites.remove(chosenName, favorite_name);
                loadedFavorites.remove(chosenName);
                writeresult = StandardFileIO.writeHashMapToFile(favoritesFile, loadedFavorites);
                if ("Error saving".contains(writeresult)) { //means we have an error
                    JOptionPane.showMessageDialog(jp, ResourceBundle.getBundle("translations/program_strings").getString("fav.delerror") + favorite_name, ResourceBundle.getBundle("translations/program_strings").getString("fav.delerrorshort"), JOptionPane.ERROR_MESSAGE);
                } else { //success
                    JOptionPane.showMessageDialog(jp, ResourceBundle.getBundle("translations/program_strings").getString("fav.deleted") + " " + favorite_name, ResourceBundle.getBundle("translations/program_strings").getString("fav.deletedshort"), JOptionPane.INFORMATION_MESSAGE);
                }
                setVisible(false);
                dispose();
            }
        });


    }

    private static HashMap<String, String> loadfavorites(String favoriteType) {

        HashMap<String, String> favorites;

        String strjexiftoolguifolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER;
        File favoritesFile = new File(strjexiftoolguifolder + File.separator + "favorites.hashmap");

        favorites = StandardFileIO.readHashMapFromFile(favoritesFile);

        return favorites;
    }


    private void displayfavorites(HashMap<String, String> favorites, String favoriteType) {

        DefaultTableModel model = (DefaultTableModel) favoritestable.getModel();
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("fav.name"), favoriteType});
        //favoritestable.setModel(model);
        favoritestable.getColumnModel().getColumn(0).setPreferredWidth(150);
        favoritestable.getColumnModel().getColumn(1).setPreferredWidth(300);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (favorites.size() > 0) {
            for (Map.Entry<String, String> key_value : favorites.entrySet()) {
                model.addRow(new Object[]{key_value.getKey(), key_value.getValue()});
            }
        }
    }


    private void SaveFavorite() {
        String chosenname = favoritenametextField.getText().trim();

        if (!"".equals(chosenname)) { // user gave a favorites name
            // Check if already exists
            if (loadedFavorites.containsKey(chosenname)) {
                int result = JOptionPane.showConfirmDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.overwrite") + chosenname + "\"?"),
                        ResourceBundle.getBundle("translations/program_strings").getString("fav.overwriteshort"), JOptionPane.OK_CANCEL_OPTION);
                if (result == 0) { //OK
                    // user wants us to overwrite
                    logger.info("user wants to update the favorites with name: " + chosenname);
                    loadedFavorites.put(chosenname, cmd_qry);
                    writeresult = StandardFileIO.writeHashMapToFile(favoritesFile, loadedFavorites);
                    if ("Error saving".contains(writeresult)) { //means we have an error
                        JOptionPane.showMessageDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.updateerror") + chosenname), ResourceBundle.getBundle("translations/program_strings").getString("fav.updateerrshort"), JOptionPane.ERROR_MESSAGE);
                    } else { //success
                        JOptionPane.showMessageDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.saved") + " " + chosenname), ResourceBundle.getBundle("translations/program_strings").getString("fav.savedshort"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } // result 2 means cancel; do nothing
            } else { // No name from hashmap loadedFavorites, so a new favorite record
                logger.debug("insert new favorite named: " + chosenname);
                loadedFavorites.put(chosenname, cmd_qry);
                writeresult = StandardFileIO.writeHashMapToFile(favoritesFile, loadedFavorites);
                if ("Error saving".contains(writeresult)) { //means we have an error
                    JOptionPane.showMessageDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.inserterror") + " " + chosenname), ResourceBundle.getBundle("translations/program_strings").getString("fav.inserterrshort"), JOptionPane.ERROR_MESSAGE);
                } else { //success
                    JOptionPane.showMessageDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.saved") + "" + chosenname), ResourceBundle.getBundle("translations/program_strings").getString("fav.savedshort"), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else { // user did not provide a lensname to insert/update
            JOptionPane.showMessageDialog(jp, String.format(ProgramTexts.HTML, 400, ResourceBundle.getBundle("translations/program_strings").getString("fav.nofavname")), ResourceBundle.getBundle("translations/program_strings").getString("fav.nofavnameshort"), JOptionPane.ERROR_MESSAGE);
        }

        //return queryresult;
    }


    private void onOK() {
        // add your code here
        //chosenName = favoritenametextField.getText();

        // Make difference between Saving and Selecting
        if ("AddFavorite".equals(favAction)) {
            SaveFavorite();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public String showDialog(JPanel rootpanel, String favoriteAction, String favoriteType, String command_query) {
        // Currently favoriteType is ALWAYS like "command", but we leave the entire logic here
        // for maybe more favorite types in the future

        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        favoritenametextField.setText("");
        favtypeText = favoriteType.replace("_", " ");
        favAction = favoriteAction;

        // Make table readonly
        favoritestable.setDefaultEditor(Object.class, null);

        jp = rootpanel; // Need to save the rootpanel for the onOK method
        favtype = favoriteType; // for onOK method
        cmd_qry = command_query; // for onOK method

        // Get current defined favorites
        loadedFavorites = loadfavorites(favoriteType);
        logger.info("retrieved favorites: " + loadedFavorites);
        displayfavorites(loadedFavorites, favoriteType);

        // Now check which action we are going to perform
        if ("AddFavorite".equals(favoriteAction)) {
            favoritenamelabel.setVisible(true);
            favoritenametextField.setVisible(true);
            lblCommandQuery.setVisible(true);
            favselectlabel.setVisible(false);
            buttonOK.setVisible(true);
            getRootPane().setDefaultButton(buttonOK);
            buttonDelete.setVisible(false);
            copiedcommandquerylabel.setText(String.format(ProgramTexts.HTML, 400, command_query));
            if (favtypeText.contains("Command")) {
                setTitle(ResourceBundle.getBundle("translations/program_strings").getString("favoriteaddcommand.title"));
            } else {
                setTitle(ResourceBundle.getBundle("translations/program_strings").getString("favoriteaddquery.title"));
            }
            setTitle("Add " + favtypeText);
            lblCommandQuery.setText(favtypeText + ": ");
            if ("Exiftool_Command".equals(favoriteType)) {
                commandqueryTopText.setText(commandTxt);
            } else {
                commandqueryTopText.setText(queryTxt);
            }
        } else if ("SelectFavorite".equals(favoriteAction)) {
            favoritenamelabel.setVisible(false);
            favoritenametextField.setVisible(false);
            lblCommandQuery.setVisible(false);
            favselectlabel.setVisible(true);
            copiedcommandquerylabel.setVisible(false);
            buttonOK.setVisible(true);
            buttonDelete.setVisible(false);
            getRootPane().setDefaultButton(buttonOK);
            setTitle(ResourceBundle.getBundle("translations/program_strings").getString("favoriteselect.title"));
        } else if ("DeleteFavorite".equals(favoriteAction)) {
            favoritenamelabel.setVisible(false);
            favoritenametextField.setVisible(false);
            lblCommandQuery.setVisible(false);
            favselectlabel.setVisible(true);
            buttonDelete.setVisible(true);
            getRootPane().setDefaultButton(buttonDelete);
            buttonOK.setVisible(false);
            setTitle(ResourceBundle.getBundle("translations/program_strings").getString("favoriteselect.title")); // same title
        }

        setVisible(true);
        return favorite_name;

        //return chosenName;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(15, 15, 15, 15), -1, -1));
        contentPane.setPreferredSize(new Dimension(700, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel2.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonDelete = new JButton();
        this.$$$loadButtonText$$$(buttonDelete, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.delete"));
        panel2.add(buttonDelete, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), 8, 5));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 1, false));
        favoritenamelabel = new JLabel();
        this.$$$loadLabelText$$$(favoritenamelabel, this.$$$getMessageFromBundle$$$("translations/program_strings", "fav.name"));
        panel4.add(favoritenamelabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        favoritenametextField = new JTextField();
        panel4.add(favoritenametextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(450, -1), null, 0, false));
        lblCommandQuery = new JLabel();
        lblCommandQuery.setText("Command:");
        panel4.add(lblCommandQuery, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copiedcommandquerylabel = new JLabel();
        copiedcommandquerylabel.setText("");
        panel4.add(copiedcommandquerylabel, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(450, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "fav.cursavedfavs"));
        panel4.add(label1, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        panel4.add(scrollPane, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        favoritestable = new JTable();
        favoritestable.setPreferredScrollableViewportSize(new Dimension(600, 300));
        scrollPane.setViewportView(favoritestable);
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        favselectlabel = new JLabel();
        this.$$$loadLabelText$$$(favselectlabel, this.$$$getMessageFromBundle$$$("translations/program_strings", "favoriteselect.title"));
        panel4.add(favselectlabel, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commandqueryTopText = new JLabel();
        commandqueryTopText.setText("");
        panel3.add(commandqueryTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(450, -1), null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}