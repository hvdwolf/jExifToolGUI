package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.hvdw.jexiftoolgui.controllers.CSVUtils;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.ExifToolReferencePanel;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class ExiftoolReference {
    private JScrollPane databaseScrollPanel;
    private JTable DBResultsTable;
    private JLabel exiftoolRefText;
    private JRadioButton radiobuttonQueryByGroup;
    private JComboBox comboBoxQueryByTagName;
    private JRadioButton radiobuttonQueryByCameraMake;
    private JComboBox comboBoxQueryCameraMake;
    private JTextField queryTagLiketextField;
    private JButton searchLikebutton;
    private JButton edbHelpbutton;
    private JLabel exiftoolRefversion;
    private JPanel ExiftoolDBPanel;
    private JPanel rootDBpanel;


    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = (Logger) LoggerFactory.getLogger(ExifToolReferencePanel.class);

    List<String[]> csvGroupsTagsList = new ArrayList<>();


    public ExiftoolReference(JFrame frame) throws IOException, InterruptedException {
        /*setContentPane(rootDBpanel);
        setModal(true);
        this.setIconImage(Utils.getFrameIcon());*/

        String TagGroups = StandardFileIO.readTextFileAsStringFromResource("texts/g1.txt");
        String[] Tags = TagGroups.split("\\r?\\n"); // split on new lines
        comboBoxQueryByTagName.setModel(new DefaultComboBoxModel(Tags));
        String TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CameraTagNames.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxQueryCameraMake.setModel(new DefaultComboBoxModel(Tags));
        // Read all the groups and tags
        //List<String[]> csvGroupsTagsList = new ArrayList<>();
        csvGroupsTagsList = CSVUtils.ReadCSVfromResources("texts/g1_groups_tags.csv");
        if (csvGroupsTagsList.isEmpty()) {
            logger.info("We have an empty list");
        } else {
            logger.info("Rows in list " + csvGroupsTagsList.size());
        }

        exiftoolRefText.setText(String.format(ProgramTexts.HTML, 850, ResourceBundle.getBundle("translations/program_strings").getString("edb.toptext")));
        exiftoolRefversion.setText(String.format(ProgramTexts.HTML, 200, ResourceBundle.getBundle("translations/program_strings").getString("edb.etrefversion") + " " + ProgramTexts.ETrefVersion));
        // Make all tables read-only unless ....
        DBResultsTable.setDefaultEditor(Object.class, null);

        // buttons and the like on database panel
        searchLikebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!"".equals(queryTagLiketextField.getText())) {
                    List<String[]> resultGroupsTagsList = new ArrayList<>();
                    resultGroupsTagsList = queryFullList(csvGroupsTagsList, queryTagLiketextField.getText(), "byTag");
                    displayListQueryResults(resultGroupsTagsList, DBResultsTable);
                } else {
                    JOptionPane.showMessageDialog(ExiftoolDBPanel, "No search string provided!", "No search string", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        comboBoxQueryByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radiobuttonQueryByGroup.isSelected()) {
                    List<String[]> resultGroupsTagsList = new ArrayList<>();
                    resultGroupsTagsList = queryFullList(csvGroupsTagsList, comboBoxQueryByTagName.getSelectedItem().toString(), "byGroup");
                    displayListQueryResults(resultGroupsTagsList, DBResultsTable);
                    //displayQueryResults(queryResult, DBResultsTable);

                }
            }
        });

        edbHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button edbHelpbutton pressed");
                JOptionPane.showMessageDialog(ExiftoolDBPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbhelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbtitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        });

    }

    static List<String[]> queryFullList(List<String[]> csvGroupsTagsList, String queryString, String queryBy) {
        List<String[]> result = new ArrayList<>();

        logger.debug("Used queryBy is: " + queryBy + " number of rows in csvGroupsTagsList " + csvGroupsTagsList.size());
        if (queryBy.equals("byGroup")) {
            logger.debug("Inside queryBy => byGroup");
            for (String[] row : csvGroupsTagsList) {
                if (queryString.equals(row[0])) {
                    result.add(row);
                }
            }
        } else {
            logger.info("Inside queryBy => byTag");
            for (String[] row : csvGroupsTagsList) {
                // Inside csv: TagNameGroup (from g1 group in this case),TagName,TagType,Writable,G0,G1,G2
                if ((row[1].toLowerCase()).contains(queryString.toLowerCase())) {
                    result.add(row);
                }
            }
        }
        logger.info("result list contains " + result.size());
        return result;
    }

    public static void displayListQueryResults(List<String[]> queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        model.setColumnIdentifiers(new String[]{"Group G0", "Group G1", "Group G2", "Tagname", "TagType", "Writable"});
        DBResultsTable.getColumnModel().getColumn(0).setPreferredWidth(125);
        DBResultsTable.getColumnModel().getColumn(1).setPreferredWidth(125);
        DBResultsTable.getColumnModel().getColumn(2).setPreferredWidth(125);
        DBResultsTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        DBResultsTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        DBResultsTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (!queryResult.isEmpty()) {

            for (String line[] : queryResult) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                // Inside csv: TagNameGroup (from g1 group in this case),TagName,TagType,Writable,G0,G1,G2
                // In table: Group G0, Group G1, Group G2, Tagname, TagType, Writable
                model.addRow(new Object[]{line[4], line[5], line[6], line[1], line[2], line[3]});
            }
        }

    }

    public static void displayQueryResults(String queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        model.setColumnIdentifiers(new String[]{"Group", "Tagname", "TagType", "Writable"});
        DBResultsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        DBResultsTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        DBResultsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        DBResultsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String[] cells = line.split("\\t", 4);
                model.addRow(new Object[]{cells[0], cells[1], cells[2], cells[3]});
            }
        }

    }

    public static void displayOwnQueryResults(String sql, String queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        // get the fields that are being queried on and immediately remove spaces for our table header and number of columns
        String queryFields = Utils.stringBetween(sql.toLowerCase(), "select", "from").replaceAll("\\s+", "");  // regex "\s" is space, extra \ to escape the first \;
        String[] headerFields = queryFields.split(",");

        model.setColumnIdentifiers(headerFields);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t");
                model.addRow(cells);
            }
        }
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
        rootDBpanel = new JPanel();
        rootDBpanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootDBpanel.setMinimumSize(new Dimension(850, 500));
        rootDBpanel.setPreferredSize(new Dimension(1150, 700));
        ExiftoolDBPanel = new JPanel();
        ExiftoolDBPanel.setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));
        ExiftoolDBPanel.setPreferredSize(new Dimension(800, 550));
        rootDBpanel.add(ExiftoolDBPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        databaseScrollPanel = new JScrollPane();
        ExiftoolDBPanel.add(databaseScrollPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DBResultsTable = new JTable();
        DBResultsTable.setPreferredScrollableViewportSize(new Dimension(1050, 400));
        databaseScrollPanel.setViewportView(DBResultsTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ExiftoolDBPanel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exiftoolRefText = new JLabel();
        this.$$$loadLabelText$$$(exiftoolRefText, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.toptext"));
        panel1.add(exiftoolRefText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        ExiftoolDBPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        radiobuttonQueryByGroup = new JRadioButton();
        radiobuttonQueryByGroup.setSelected(true);
        this.$$$loadButtonText$$$(radiobuttonQueryByGroup, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.bygroup"));
        panel2.add(radiobuttonQueryByGroup);
        comboBoxQueryByTagName = new JComboBox();
        panel2.add(comboBoxQueryByTagName);
        radiobuttonQueryByCameraMake = new JRadioButton();
        this.$$$loadButtonText$$$(radiobuttonQueryByCameraMake, this.$$$getMessageFromBundle$$$("translations/program_strings", "vdtab.bycamera"));
        radiobuttonQueryByCameraMake.setVisible(false);
        panel2.add(radiobuttonQueryByCameraMake);
        comboBoxQueryCameraMake = new JComboBox();
        comboBoxQueryCameraMake.setVisible(false);
        panel2.add(comboBoxQueryCameraMake);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        ExiftoolDBPanel.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.wheretaglike"));
        panel3.add(label1);
        queryTagLiketextField = new JTextField();
        queryTagLiketextField.setPreferredSize(new Dimension(300, 30));
        panel3.add(queryTagLiketextField);
        searchLikebutton = new JButton();
        this.$$$loadButtonText$$$(searchLikebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.btnsearchlike"));
        panel3.add(searchLikebutton);
        edbHelpbutton = new JButton();
        this.$$$loadButtonText$$$(edbHelpbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "button.help"));
        ExiftoolDBPanel.add(edbHelpbutton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exiftoolRefversion = new JLabel();
        Font exiftoolRefversionFont = this.$$$getFont$$$(null, Font.ITALIC, -1, exiftoolRefversion.getFont());
        if (exiftoolRefversionFont != null) exiftoolRefversion.setFont(exiftoolRefversionFont);
        this.$$$loadLabelText$$$(exiftoolRefversion, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.etrefversion"));
        exiftoolRefversion.setToolTipText("The exiftool version to build the included data set version is not necessarily the same as your installed exiftool version");
        ExiftoolDBPanel.add(exiftoolRefversion, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
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
        return rootDBpanel;
    }


    public static void showDialog() {
/*        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("exiftooldb.title"));
        //initDialog();

        setVisible(true); */
        JFrame frame = new JFrame(ResourceBundle.getBundle("translations/program_strings").getString("exiftooldb.title"));
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        frame.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        frame.setIconImage(Utils.getFrameIcon());
        try {
            frame.setContentPane(new ExiftoolReference(frame).rootDBpanel);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            logger.error("InterruptedException or IOException creating ExiftoolDatabase frame: {}", e);
        }
        //frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
