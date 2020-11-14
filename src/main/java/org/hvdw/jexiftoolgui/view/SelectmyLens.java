package org.hvdw.jexiftoolgui.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.hvdw.jexiftoolgui.model.Lenses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class SelectmyLens extends JDialog {
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JTable lensnametable;
    private JButton OKbutton;
    private JButton Cancelbutton;
    private JButton Deletebutton;
    private JLabel selectLensTopText;

    private String lensname = "";
    private String queryresult = "";
    private JPanel rp;

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SelectmyLens.class);
    private final String loadLensTxt = "<html>" + ResourceBundle.getBundle("translations/program_strings").getString("sellens.loadlenstxt") + "<br><br></html>";
    private final String deleteLensTxt = "<html>" + ResourceBundle.getBundle("translations/program_strings").getString("sellens.deletelenstxt") + "<br><br></html>";


    public SelectmyLens() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(OKbutton);

        // Only active on load lens
        OKbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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
        // Only active on delete lens
        Deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.info("lens selected for deletion -{}-", lensname);
                String sql = "delete from myLenses where lens_name = '" + lensname.trim() + "'";
                queryresult = SQLiteJDBC.insertUpdateQuery(sql, "disk");
                if (!"".equals(queryresult)) { //means we have an error
                    JOptionPane.showMessageDialog(rp, ResourceBundle.getBundle("translations/program_strings").getString("sellens.delerror") + lensname, ResourceBundle.getBundle("translations/program_strings").getString("fav.delerrorshort"), JOptionPane.ERROR_MESSAGE);
                } else { //success
                    JOptionPane.showMessageDialog(rp, ResourceBundle.getBundle("translations/program_strings").getString("sellens.deleted") + " " + lensname, ResourceBundle.getBundle("translations/program_strings").getString("sellens.deletedshort"), JOptionPane.INFORMATION_MESSAGE);
                }
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

        // mouse/table listener
        lensnametable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) lensnametable.getModel();
                int selectedRowIndex = lensnametable.getSelectedRow();
                MyVariables.setselectedLensConfig(model.getValueAt(selectedRowIndex, 0).toString());
                lensname = model.getValueAt(selectedRowIndex, 0).toString();
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        setVisible(false);
        dispose();
    }

    public String showDialog(JPanel rootpanel, String action) {
        pack();
        //setLocationRelativeTo(null);
        rp = rootpanel;
        setLocationByPlatform(true);
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("selectlens.title"));
        if ("load lens".equals(action)) {
            selectLensTopText.setText(loadLensTxt);
            OKbutton.setVisible(true);
            OKbutton.setEnabled(true);
            Deletebutton.setVisible(false);
            Deletebutton.setEnabled(false);
        } else {
            selectLensTopText.setText(deleteLensTxt);
            OKbutton.setVisible(false);
            OKbutton.setEnabled(false);
            Deletebutton.setVisible(true);
            Deletebutton.setEnabled(true);
        }
        // Make table readonly
        lensnametable.setDefaultEditor(Object.class, null);
        // Get current defined lenses
        String lensnames = Lenses.loadlensnames();
        logger.info("retrieved lensnames: " + lensnames);
        Lenses.displaylensnames(lensnames, lensnametable);

        setVisible(true);
        return lensname;
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
        contentPane.setPreferredSize(new Dimension(700, 300));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), 5, 5));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "sellens.currentlenses"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        panel2.add(scrollPane, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lensnametable = new JTable();
        scrollPane.setViewportView(lensnametable);
        selectLensTopText = new JLabel();
        selectLensTopText.setText("<html>Select a row from the table to select and load an existing lens configuration.<br><br></html>");
        panel1.add(selectLensTopText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(450, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        Deletebutton = new JButton();
        this.$$$loadButtonText$$$(Deletebutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.delete"));
        panel3.add(Deletebutton);
        OKbutton = new JButton();
        this.$$$loadButtonText$$$(OKbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.ok"));
        panel3.add(OKbutton);
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
