package org.hvdw.jexiftoolgui.editpane;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.CommandRunner;
import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.PRESERVE_MODIFY_DATE;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class EditUserDefinedCombis {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(EditUserDefinedCombis.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    JTable usercombiTable;
    MyTableModel model;
    TableModelListener modelListener;
    List<String> tablerowdata = new ArrayList<String>();
    List<String> defaultValues = new ArrayList<String>();
    String strcustomconfigfile = "";

    /*
    / This option makes only the 3rd (tag value) and 4th (Save checkbox) columns (2, 3) editable
    / column 1 (no. 0) & 2 (no. 1) are ready-only.
    / Also override getColumnClass to allow for the Save checkbox in 4th column.
     */
    public class MyTableModel extends DefaultTableModel {
        public boolean isCellEditable(int row, int column){
            return column == 2 || column == 3;
        }

        public Class getColumnClass(int column) {
            if (column == 3)
                return Boolean.class;
            else
                return String.class;
        }
    }

    /*
    / This method updates the table in case we have selected another combi from the JCombobox
     */
    public void UpdateTable(JPanel rootpanel, JComboBox combicombobox, JScrollPane userCombiPane) {
        if (model != null && modelListener != null)
            model.removeTableModelListener(modelListener);

        tablerowdata.clear();
        defaultValues.clear();

        if (model == null) {
            model = new MyTableModel();
            model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("mduc.columnlabel"),
                    ResourceBundle.getBundle("translations/program_strings").getString("mduc.columntag"),
                    ResourceBundle.getBundle("translations/program_strings").getString("mduc.columndefault"),
                    ResourceBundle.getBundle("translations/program_strings").getString("label.save")});
        }
        else
            model.setRowCount(0);

        if (usercombiTable == null)
            usercombiTable = new JTable(model);

        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select screen_label, tag, default_value from custommetadatasetLines where customset_name='" + setName.trim() + "' order by rowcount";
        String queryResult = SQLiteJDBC.generalQuery(sql, "disk");
        if (queryResult.length() > 0) {
            String[] lines = queryResult.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));

            for (String line : lines) {
                String[] cells = line.split("\\t", 4);
                String defVal = cells[2].trim();
                model.addRow(new Object[]{cells[0], cells[1], defVal, defVal.length() == 0 ? Boolean.FALSE : Boolean.TRUE});
                tablerowdata.add("");
                defaultValues.add(defVal); // We'll use the default values again during a copy operation
            }
            MyVariables.setuserCombiTableValues(tablerowdata);
        }
        userCombiPane.setViewportView(usercombiTable);
        // Make the Save column narrower.
        usercombiTable.getColumnModel().getColumn(3).setMinWidth(50);
        usercombiTable.getColumnModel().getColumn(3).setMaxWidth(110);
        usercombiTable.getColumnModel().getColumn(3).setPreferredWidth(70);

       if (modelListener == null) {
            modelListener = new TableModelListener() {
                // React to edits of tag values.  If value is different to original value then tick the Save checkbox
                public void tableChanged(TableModelEvent e) {
                    int col = e.getColumn();
                    if (col == 3) // No need to react to checkboxes
                        return;
                    int row = e.getFirstRow();
                    Object originalValue = tablerowdata.get(row);
                    Object modifiedValue = model.getValueAt(row, col);
                    logger.debug("source {}; firstRow {}; lastRow {}, column{}", e.getSource(), row, e.getLastRow(), col);
                    logger.debug("tag {}; original value {}; modified tablecell value {}", model.getValueAt(row, 1), originalValue, modifiedValue);
                    if (originalValue.equals(modifiedValue))
                        model.setValueAt(Boolean.FALSE, row, 3);
                    else
                        model.setValueAt(Boolean.TRUE, row, 3);
                }
            };
        }

        model.addTableModelListener(modelListener);
    }

    public void UpdateCustomConfigLabel(JComboBox combicombobox, JLabel customconfiglabel) {
        String setName = combicombobox.getSelectedItem().toString();
        String sql = "select custom_config from custommetadataset where customset_name='" + setName.trim() + "'";
        String queryResult = SQLiteJDBC.singleFieldQuery(sql, "custom_config").trim();
        if ("".equals(queryResult) || queryResult.isEmpty() || "null".equals(queryResult)) {
            customconfiglabel.setText("");
            strcustomconfigfile = "";
            customconfiglabel.setVisible(false);
        } else {
            strcustomconfigfile = queryResult.trim();
            customconfiglabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("udc.thisset") + " " + queryResult);
            customconfiglabel.setVisible(true);
        }
    }

    public void ResetFields(JScrollPane userCombiPane) {
        // Not used here: if user wants to reset, he/she simply selects the dropdown again.
    }

    public void SaveTableValues(JCheckBox udcOverwriteOriginalscheckBox, JProgressBar progressBar) {
        // if Save checkedbox ticked  => save
        File[] files = MyVariables.getLoadedFiles();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        tablerowdata = MyVariables.getuserCombiTableValues();
        int rowcounter = 0;
        List<String> cmdparams = new ArrayList<String>();

        cmdparams.add(Utils.platformExiftool());
        // -config parameter for custom config file has to be first parameter on command line
        if (!strcustomconfigfile.equals("")) {
            String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
            String strjexiftoolguifolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER;
            cmdparams.add("-config");
            cmdparams.add(strjexiftoolguifolder + File.separator + strcustomconfigfile);
        }
        boolean preserveModifyDate = prefs.getByKey(PRESERVE_MODIFY_DATE, true);
        if (preserveModifyDate) {
            cmdparams.add("-preserve");
        }
        if (!udcOverwriteOriginalscheckBox.isSelected()) {
            // default not select ->overwrite originals
            cmdparams.add("-overwrite_original");
        }
        cmdparams.addAll(Utils.AlwaysAdd());
        cmdparams.add("-n"); // Need this for some tags, eg Orientation
        boolean hasChanged = false;
        model.removeTableModelListener(modelListener); // Temporarily remove listener to avoid triggering unnecessary events
        for (String value : tablerowdata) {
            if ((Boolean) model.getValueAt(rowcounter, 3)) {
                hasChanged = true;
                String cellValue = model.getValueAt(rowcounter,2).toString().trim();
                logger.info("tag {}; original value {}; modified tablecell value {}", model.getValueAt(rowcounter,1), tablerowdata.get(rowcounter), cellValue);
                if (model.getValueAt(rowcounter,1).toString().startsWith("-")) {
                    cmdparams.add(model.getValueAt(rowcounter,1).toString() + "=" + cellValue);
                } else { //tag without - (minus sign/hyphen) as prefix
                    cmdparams.add("-" + model.getValueAt(rowcounter,1).toString() + "=" + cellValue);
                }
                tablerowdata.set(rowcounter, cellValue);
                model.setValueAt(cellValue, rowcounter, 2);
            }
            rowcounter++;
        }
        model.addTableModelListener(modelListener); // Add back listener

        for (int index: selectedIndices) {
            //logger.info("index: {}  image path: {}", index, files[index].getPath());
            if (Utils.isOsFromMicrosoft()) {
                cmdparams.add(files[index].getPath().replace("\\", "/"));
            } else {
                cmdparams.add(files[index].getPath());
            }
        }

        if (hasChanged) {
            logger.info("Save user combi parameter command {}", cmdparams);
            CommandRunner.runCommandWithProgressBar(cmdparams, progressBar);
        } else {
            logger.info("Nothing to save -- no edits made");
        }
    }

    public void CopyFromSelectedImage() {
        File[] files = MyVariables.getLoadedFiles();
        int SelectedRow = MyVariables.getSelectedRowOrIndex();
        tablerowdata = MyVariables.getuserCombiTableValues();
        int rowcounter = 0;
        String fpath ="";
        String res = "";
        List<String> cmdparams = new ArrayList<String>();
        List<String> tagnames = new ArrayList<String>();


        if (Utils.isOsFromMicrosoft()) {
            fpath = files[SelectedRow].getPath().replace("\\", "/");
        } else {
            fpath = files[SelectedRow].getPath();
        }
        cmdparams.add(Utils.platformExiftool());
        cmdparams.add("-e");
        cmdparams.add("-n");
        // Now get the tags from table
        for (String value : tablerowdata) {
            logger.debug("tag derived from table {}", model.getValueAt(rowcounter,1));
            String tname = model.getValueAt(rowcounter,1).toString();
            if (tname.startsWith("-")) {
                tname = tname.substring(1);
            }
            // Format the output using exiftool -p option rather than relying on the default output.
            // The format used is: TAGNAME::VALUE.  CANNOT USE "=" for the delimiter due to handling of args in runCommand under Windows
            cmdparams.add("-p");
            cmdparams.add(tname + "::${" + tname + "}");
            tagnames.add(tname);
            rowcounter++;
        }
        cmdparams.add(fpath);
        try {
            res = CommandRunner.runCommand(cmdparams);
            logger.debug("res from copyfrom is\n{}", res);
        } catch(IOException | InterruptedException ex) {
            logger.debug("Error executing command");
        }
        if (res.length() > 0) {
            String[] strTagnames = tagnames.stream().toArray(String[]::new);
            model.removeTableModelListener(modelListener); // Temporarily remove listener to avoid triggering unnecessary events
            displayCopiedInfo( res, strTagnames);
            model.addTableModelListener(modelListener); // Add back listener
        }

    }

    private void displayCopiedInfo(String exiftoolInfo, String[] tagNames) {
        int rowcounter = 0;
        String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
        tablerowdata = MyVariables.getuserCombiTableValues();

        // Clear previous values and set defaults
        for (int row = 0; row < model.getRowCount(); row++) {
            tablerowdata.set(row, "");
            model.setValueAt(defaultValues.get(row), row, 2);
        }

        for (String line : lines) {
            String[] returnedValuesRow = line.split("::", 2); // Only split on first :: as some tags also contain (multiple) ::
            if (returnedValuesRow.length < 2) // line does not contain "TAGNAME::VALUE" so skip it, eg warning messages
                continue;
            String SpaceStrippedTag = returnedValuesRow[0];
            logger.debug("returnedValuesRow tag {}; returnedValuesRow value {}; SpaceStrippedTag {}", returnedValuesRow[0], returnedValuesRow[1], SpaceStrippedTag);

            rowcounter =0;
            for (String tagname: tablerowdata) {
                if (model.getValueAt(rowcounter,1).toString().equals(SpaceStrippedTag)) {
                    // The model data and tablerowdata values are the same when first retrieved.
                    // Later, during save, each row is checked if it was changed.
                    tablerowdata.set(rowcounter, returnedValuesRow[1].trim());
                    model.setValueAt(returnedValuesRow[1].trim(),rowcounter,2);
                }
                rowcounter++;
            }
        }
        // Tick the Save checkbox for any tags which have defaults still remaining
        for (int row = 0; row < model.getRowCount(); row++) {
            if (model.getValueAt(row, 2).equals(tablerowdata.get(row)))
                model.setValueAt(Boolean.FALSE, row, 3);
            else
                model.setValueAt(Boolean.TRUE, row, 3);
        }
    }
}
