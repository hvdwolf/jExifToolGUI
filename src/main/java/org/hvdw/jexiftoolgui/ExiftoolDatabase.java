package org.hvdw.jexiftoolgui;

import ch.qos.logback.classic.Logger;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.model.SQLiteModel;
import org.hvdw.jexiftoolgui.controllers.StandardFileIO;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.view.AddFavorite;
import org.hvdw.jexiftoolgui.view.DatabasePanel;
import org.hvdw.jexiftoolgui.view.SelectFavorite;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class ExiftoolDatabase {
    private JScrollPane databaseScrollPanel;
    private JTable DBResultsTable;
    private JLabel exiftoolDBText;
    private JRadioButton radiobuttonQueryByGroup;
    private JComboBox comboBoxQueryByTagName;
    private JRadioButton radiobuttonQueryByCameraMake;
    private JComboBox comboBoxQueryCameraMake;
    private JTextField queryTagLiketextField;
    private JButton searchLikebutton;
    private JButton edbHelpbutton;
    private JLabel exiftoolDBversion;
    private JButton buttonDBdiagram;
    private JTextField sqlQuerytextField;
    private JButton sqlExecutebutton;
    private JButton SaveQuerybutton;
    private JButton loadQuerybutton;
    private JPanel ExiftoolDBPanel;
    private JPanel rootDBpanel;


    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = (Logger) LoggerFactory.getLogger(DatabasePanel.class);

    private AddFavorite AddFav = new AddFavorite();
    private SelectFavorite SelFav = new SelectFavorite();
    private DiagramPanel contentPane;

    public ExiftoolDatabase(JFrame frame) throws IOException, InterruptedException {
        /*setContentPane(rootDBpanel);
        setModal(true);
        this.setIconImage(Utils.getFrameIcon());*/

        String sqlGroups = SQLiteModel.getGroups();
        String[] Tags = sqlGroups.split("\\r?\\n"); // split on new lines
        comboBoxQueryByTagName.setModel(new DefaultComboBoxModel(Tags));
        String TagNames = StandardFileIO.readTextFileAsStringFromResource("texts/CameraTagNames.txt");
        Tags = TagNames.split("\\r?\\n"); // split on new lines
        comboBoxQueryCameraMake.setModel(new DefaultComboBoxModel(Tags));
        exiftoolDBText.setText(String.format(ProgramTexts.HTML, 600, ResourceBundle.getBundle("translations/program_strings").getString("edb.toptext")));
        // database version
        exiftoolDBversion.setText(String.format(ProgramTexts.HTML, 100, "exiftool DB version:<br>" + SQLiteModel.getDBversion()));
        // Make all tables read-only unless ....
        DBResultsTable.setDefaultEditor(Object.class, null);

        // buttons and the like on database panel
        searchLikebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!"".equals(queryTagLiketextField.getText())) {
                    String queryResult = SQLiteModel.queryByTagname(queryTagLiketextField.getText(), true);
                    displayQueryResults(queryResult, DBResultsTable);
                } else {
                    JOptionPane.showMessageDialog(ExiftoolDBPanel, "No search string provided!", "No search string", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        comboBoxQueryByTagName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radiobuttonQueryByGroup.isSelected()) {
                    String queryResult = SQLiteModel.queryByTagname(comboBoxQueryByTagName.getSelectedItem().toString(), false);
                    displayQueryResults(queryResult, DBResultsTable);
                }
            }
        });
        comboBoxQueryCameraMake.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (radiobuttonQueryByCameraMake.isSelected()) {
                    String queryResult = SQLiteModel.queryByTagname(comboBoxQueryCameraMake.getSelectedItem().toString(), false);
                    displayQueryResults(queryResult, DBResultsTable);
                }
            }
        });
        //edbHelpbutton.setActionCommand("edbHb");
        edbHelpbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button edbHelpbutton pressed");
                JOptionPane.showMessageDialog(ExiftoolDBPanel, String.format(ProgramTexts.HTML, 700, ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbhelptext")), ResourceBundle.getBundle("translations/program_help_texts").getString("exiftooldbtitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        sqlExecutebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!"".equals(sqlQuerytextField.getText())) {
                    String queryResult = SQLiteModel.ownQuery(sqlQuerytextField.getText());
                    if (!queryResult.contains("SQLITE_ERROR")) {
                        displayOwnQueryResults(sqlQuerytextField.getText(), queryResult, DBResultsTable);
                    } else { // We do have an "[SQLITE_ERROR] SQL error or missing database ...."
                        JOptionPane.showMessageDialog(ExiftoolDBPanel, "You have an error in your query.\nPlease check!", "incorrect sql query", JOptionPane.WARNING_MESSAGE);
                    }
                } else { //user did not proved an sql string
                    JOptionPane.showMessageDialog(ExiftoolDBPanel, "No sql query provided!", "No sql query", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //SaveQuerybutton.setActionCommand("SQb");
        SaveQuerybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button SaveQuerybutton pressed");
                if (sqlQuerytextField.getText().length() > 0) {
                    AddFav.showDialog(ExiftoolDBPanel, "DB_query", sqlQuerytextField.getText());
                } else {
                    JOptionPane.showMessageDialog(ExiftoolDBPanel, "No query given", "No query", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //loadQuerybutton.setActionCommand("lQb");
        loadQuerybutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button loadQuerybutton pressed");
                LoadQueryFavorite(ExiftoolDBPanel, sqlQuerytextField);
            }
        });
        //buttonDBdiagram.setActionCommand("bDBb");
        buttonDBdiagram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.debug("button buttonDBdiagram pressed");
                DisplayDiagram();
            }
        });

    }

    static String convertWritable(String writable) {
        // For some stupid reason SQLJDBC always changes "Yes" or "true" to 1, and "false" or "No" to null.
        // So here we have to change it back
        if ("1".equals(writable)) {
            return "Yes";
        } else {
            return "No";
        }
    }

    public static void displayQueryResults(String queryResult, JTable DBResultsTable) {
        DefaultTableModel model = (DefaultTableModel) DBResultsTable.getModel();
        model.setColumnIdentifiers(new String[]{"Group", "Tagname", "TagType", "Writable"});
        DBResultsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        DBResultsTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        DBResultsTable.getColumnModel().getColumn(2).setPreferredWidth(240);
        DBResultsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                String[] cells = line.split("\\t", 4);
                model.addRow(new Object[]{cells[0], cells[1], cells[2], convertWritable(cells[3])});
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

    public void LoadQueryFavorite(JPanel rootpanel, JTextField sqlQuerytextField) {
        String queryresult = "";

        String favName = SelFav.showDialog(rootpanel, "DB_query");
        logger.debug("returned selected favorite: " + favName);
        if (!"".equals(favName)) {
            String sql = "select command_query from userFavorites where favorite_type='DB_query' and favorite_name='" + favName + "' limit 1";
            queryresult = SQLiteJDBC.generalQuery(sql, "disk");
            logger.debug("returned command: " + queryresult);
 /*           // We do save to the database using single quotes, so if the command or the query contains single quotes we need to escape them
            // Upon retrieval we need to rool back as the user would see those escaped single quotes in his/her command
            String queryresult_unescaped = queryresult.replace("\'", "'"); */
            sqlQuerytextField.setText(queryresult);
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
        rootDBpanel.setMinimumSize(new Dimension(700, 500));
        rootDBpanel.setPreferredSize(new Dimension(850, 700));
        ExiftoolDBPanel = new JPanel();
        ExiftoolDBPanel.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), -1, -1));
        ExiftoolDBPanel.setPreferredSize(new Dimension(800, 550));
        rootDBpanel.add(ExiftoolDBPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        databaseScrollPanel = new JScrollPane();
        ExiftoolDBPanel.add(databaseScrollPanel, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DBResultsTable = new JTable();
        databaseScrollPanel.setViewportView(DBResultsTable);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ExiftoolDBPanel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        exiftoolDBText = new JLabel();
        this.$$$loadLabelText$$$(exiftoolDBText, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.toptext"));
        panel1.add(exiftoolDBText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
        exiftoolDBversion = new JLabel();
        Font exiftoolDBversionFont = this.$$$getFont$$$(null, Font.ITALIC, -1, exiftoolDBversion.getFont());
        if (exiftoolDBversionFont != null) exiftoolDBversion.setFont(exiftoolDBversionFont);
        this.$$$loadLabelText$$$(exiftoolDBversion, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.edbversion"));
        exiftoolDBversion.setToolTipText("The exiftool version to build the included database version is not necessarily the same as your installed exiftool version");
        ExiftoolDBPanel.add(exiftoolDBversion, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonDBdiagram = new JButton();
        this.$$$loadButtonText$$$(buttonDBdiagram, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.btndiagram"));
        buttonDBdiagram.setToolTipText("Opens a browser displaying the DB diagram");
        ExiftoolDBPanel.add(buttonDBdiagram, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 4, new Insets(5, 5, 5, 5), -1, -1));
        ExiftoolDBPanel.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        sqlQuerytextField = new JTextField();
        panel4.add(sqlQuerytextField, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, -1, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "edb.ownquery"));
        panel4.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sqlExecutebutton = new JButton();
        this.$$$loadButtonText$$$(sqlExecutebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btngo"));
        panel4.add(sqlExecutebutton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        SaveQuerybutton = new JButton();
        this.$$$loadButtonText$$$(SaveQuerybutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnaddfav"));
        panel4.add(SaveQuerybutton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        loadQuerybutton = new JButton();
        this.$$$loadButtonText$$$(loadQuerybutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "yc.btnloadfav"));
        panel4.add(loadQuerybutton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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

    /*
    / Below method and class display the database diagram in an "independent" window.
     */
    private class DiagramPanel extends JPanel {
    }

    public void DisplayDiagram() {
        BufferedImage img = null;

        JFrame frame = new JFrame("Database diagram");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new DiagramPanel();
        frame.setContentPane(contentPane);
        frame.setIconImage(Utils.getFrameIcon());
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        frame.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));

        try {
            img = ImageIO.read(DiagramPanel.class.getResource("/jexiftoolgui-diagram.png"));
        } catch (IOException ioe) {
            logger.info("erorr loading diagram png {}", ioe.toString());
        }

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.setBackground(Color.white);
        thePanel.setOpaque(true);
        thePanel.add(new JLabel(new ImageIcon(img)), BorderLayout.PAGE_START);

        JScrollPane theScroller = new JScrollPane(thePanel);
        theScroller.setPreferredSize(new Dimension(830, 600));
        frame.add(theScroller);

        frame.setLocationByPlatform(true);
        // Position to screen center.
        //Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        //setLocation((int) (screen_size.getWidth() - getWidth()) / 2,
        //        (int) (screen_size.getHeight() - getHeight()) / 2);
        frame.pack();
        frame.setVisible(true);
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
            frame.setContentPane(new ExiftoolDatabase(frame).rootDBpanel);
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
