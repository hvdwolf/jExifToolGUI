package org.hvdw.jexiftoolgui.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class DeleteFavorite extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTable favoritetable;
    private JButton Deletebutton;
    private JButton Cancelbutton;

    private String favorite_name = "";
    private String queryresult = "";
    private JPanel jp = null;

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(DeleteFavorite.class);


    public DeleteFavorite() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(Deletebutton);

        Deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.info("favorite for deletion {}", favorite_name);
                String sql = "delete from userFavorites where favorite_name = '" + favorite_name.trim() + "'";
                queryresult = SQLiteJDBC.insertUpdateQuery(sql, "disk");
                if (!"".equals(queryresult)) { //means we have an error
                    JOptionPane.showMessageDialog(jp, ResourceBundle.getBundle("translations/program_strings").getString("fav.delerror") + favorite_name, ResourceBundle.getBundle("translations/program_strings").getString("fav.delerrorshort"), JOptionPane.ERROR_MESSAGE);
                } else { //success
                    JOptionPane.showMessageDialog(jp, ResourceBundle.getBundle("translations/program_strings").getString("fav.deleted") + " " + favorite_name, ResourceBundle.getBundle("translations/program_strings").getString("fav.deletedshort"), JOptionPane.INFORMATION_MESSAGE);
                }
                setVisible(false);
                dispose();
            }
        });
        Cancelbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // mouse table listener
        favoritetable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) favoritetable.getModel();
                int selectedRowIndex = favoritetable.getSelectedRow();
                MyVariables.setselectedLensConfig(model.getValueAt(selectedRowIndex, 1).toString());
                favorite_name = model.getValueAt(selectedRowIndex, 1).toString();
            }
        });
    }

    private String loadfavorites() {
        String sql = "select favorite_type, favorite_name,command_query from userFavorites order by favorite_name";
        String favorite_names = SQLiteJDBC.generalQuery(sql, "disk");
        return favorite_names;

    }

    private void displayfavorite_names(String favorite_names) {
        DefaultTableModel model = (DefaultTableModel) favoritetable.getModel();
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("fav.type"), ResourceBundle.getBundle("translations/program_strings").getString("fav.name"), ResourceBundle.getBundle("translations/program_strings").getString("fav.descr")});
        favoritetable.getColumnModel().getColumn(0).setPreferredWidth(100);
        favoritetable.getColumnModel().getColumn(1).setPreferredWidth(150);
        favoritetable.getColumnModel().getColumn(2).setPreferredWidth(300);
        model.setRowCount(0);

        Object[] row = new Object[1];

        if (favorite_names.length() > 0) {
            String[] lines = favorite_names.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t");
                model.addRow(new Object[]{cells[0], cells[1], cells[2]});
            }
        }

    }


    private void onCancel() {
        // add your code here if necessary
        setVisible(false);
        dispose();
    }

    public String showDialog(JPanel rootpanel) {
        pack();
        //setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("favoriteselect.title"));
        // Make table readonly
        favoritetable.setDefaultEditor(Object.class, null);

        // Get current defined lenses
        String favorite_names = loadfavorites();
        logger.debug("retrieved favorites: " + favorite_names);
        displayfavorite_names(favorite_names);

        setVisible(true);
        return favorite_name;
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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(15, 15, 15, 15), -1, -1));
        contentPane.setPreferredSize(new Dimension(800, 400));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), 5, 5));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "fav.cursavedfavs"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        panel2.add(scrollPane, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        favoritetable = new JTable();
        scrollPane.setViewportView(favoritetable);
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "fav.selfordel"));
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(550, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Deletebutton = new JButton();
        this.$$$loadButtonText$$$(Deletebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel3.add(Deletebutton);
        Cancelbutton = new JButton();
        this.$$$loadButtonText$$$(Cancelbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.cancel"));
        panel3.add(Cancelbutton);
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
