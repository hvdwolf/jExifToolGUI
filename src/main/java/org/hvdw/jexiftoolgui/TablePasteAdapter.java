package org.hvdw.jexiftoolgui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.datatransfer.StringSelection;
import java.util.Vector;

/**
 * TablePasteAdapter enables Cut-Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by spreadsheets. This provides for clipboard
 * interoperability between enabled JTables and spreadsheets.
 */
public class TablePasteAdapter implements ActionListener {
    private final JTable myTable;
    private final Clipboard myClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private static final ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(TablePasteAdapter.class);


    /**
     * The TablePasteAdapter is constructed with a JTable on which it enables
     * Cut-Copy-Paste and acts as a Clipboard listener.
     * @param aTable
     */
    public TablePasteAdapter(JTable aTable) {
        myTable = aTable;
        initKeyboardActions();
    }

    /** Register the cut, copy and paste keyboard sequences. */
    private void initKeyboardActions() {
        // Identify the cut, copy and paste KeyStrokes. The user can modify this to
        // cut, copy and paste with other Key combinations.
        KeyStroke cut = KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK,false);
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
        myTable.registerKeyboardAction(this, "Cut", cut, JComponent.WHEN_FOCUSED);
        myTable.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        myTable.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
    }

    /**
     * This method is activated on the Keystrokes we are listening to
     * in this implementation. Here it listens for Cut, Copy and Paste ActionCommands.
     * Selections comprising non-adjacent cells result in invalid selection and
     * then the cut/copy actions cannot be performed.
     * Paste is done by aligning the upper left corner of the clipboard data with
     * the 1st element in the current selection of the JTable. Rows are added
     * as necessary.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Cut/Copy
        if (e.getActionCommand().compareTo("Copy") == 0 ||
                e.getActionCommand().compareTo("Cut" ) == 0) {
            try {
                // Check to ensure we have selected only a contiguous block of cells
                int numcols = myTable.getSelectedColumnCount();
                int numrows = myTable.getSelectedRowCount();
                if (numcols == 0 || numrows == 0) return; // nothing selected.

                int[] rowsselected = myTable.getSelectedRows();
                int[] colsselected = myTable.getSelectedColumns();
                if ( ! ((numrows-1 == rowsselected[rowsselected.length-1]-rowsselected[0] &&
                        numrows == rowsselected.length) &&
                        (numcols-1 == colsselected[colsselected.length-1]-colsselected[0] &&
                                numcols == colsselected.length))) {
                    // In FPT discontiguous selections may be forbidden by table properties.
                    logger.info("Invalid Copy selection: not contiguous");
                    return;
                }

                // Collect the selected cells. (Tabs separate cells in same row,
                // newlines separate rows. Last items in row do not have tabs.)
                StringBuilder sbf = new StringBuilder();
                for (int r = 0; r < numrows; r++) {
                    for (int c = 0; c < numcols; c++) {
                        String cellValue = (String) myTable.getValueAt(rowsselected[r], colsselected[c]);
                        sbf.append(cellValue);
                        if (c < numcols - 1) {sbf.append("\t");} // No tab after last item.
                    }
                    sbf.append("\n"); // After each row.
                }

                // Put the selected cells on the clipboard.
                StringSelection stsel = new StringSelection(sbf.toString());
                myClipboard.setContents(stsel, stsel);

                // If a Cut them clear the cells just copied.
                if (e.getActionCommand().compareTo("Cut" ) == 0) {
                    for (int r = 0; r < numrows; r++) {
                        for (int c = 0; c < numcols; c++) {
                            myTable.setValueAt("", rowsselected[r], colsselected[c]);
                        }
                    }
                }
            }
            catch (Exception ex) {
                logger.info("Copy failed. {}", ex);
            }
            return;
        }

        // Paste
        if (e.getActionCommand().compareTo("Paste") == 0) {
            try {
                // Copy from clipboard to table starting at the top-left selected cell.
                // If nothing is selected then put at end of table.
                int startRow = 0;
                int startCol = 0;
                if (myTable.getSelectedRows().length == 0) {
                    // Nothing selected, put at end of table, first column.
                    startRow = myTable.getRowCount();
                    startCol = 0;
                }
                else {
                    startRow = (myTable.getSelectedRows())[0];
                    startCol = (myTable.getSelectedColumns())[0];
                }
                String transferData = (String)(myClipboard.getContents(this).
                        getTransferData(DataFlavor.stringFlavor));

                // Make sure at least one row is found.
                transferData = transferData.replaceAll("\n", " \n");

                // Split transfer data into rows.
                String rowArray[] = transferData.split("\n");

                // Paste into a copy of the table, then if successful write the
                // copy to the real table.  This is much faster than updating
                // the actual table.
                // Get the table headings.
                Vector theHeadings = new Vector();
                for (int c = 0; c < myTable.getColumnCount(); c++) {
                    theHeadings.add(myTable.getColumnName(c));
                }
                // Get the table data.
                Vector theData = ((DefaultTableModel) myTable.getModel()).getDataVector();

                // If the table must be expanded to accommodate the pasted data
                // do it before starting to copy.
                int pasteRowCount = rowArray.length;
                int rowsToAdd = startRow + pasteRowCount - myTable.getRowCount();
                if (rowsToAdd > 0) {
                    // Create a blank row of proper size.
                    Vector blankRow = new Vector(myTable.getColumnCount());
                    for (int c = 0; c < myTable.getColumnCount(); c++) {
                        blankRow.add("");
                    }

                    // Add blank rows to the end of the data.
                    for (int r = 0; r < rowsToAdd; r++) {
                        theData.add(blankRow.clone());
                    }
                }

                // Copy the clipboard data into the table.
                for (int r = 0; r < pasteRowCount; r++) {
                    // Make sure something is found after every tab. Trim later.
                    rowArray[r] = rowArray[r].replaceAll("\t", "\t ");

                    // Get a row of transfer data
                    String colArray[] = rowArray[r].split("\t");
                    int cCount = rowArray[0].split("\t").length;

                    // Copy a row into the table.
                    for (int c = 0; c < cCount; c++) {
                        // Test overflow on table right edge.
                        if (startCol + c < myTable.getColumnCount()) {
                            String aCellValue = colArray[c].trim();
                            Vector rowR = (Vector) theData.get(startRow + r);
                            rowR.setElementAt(aCellValue, startCol + c);
                            theData.setElementAt(rowR, startRow + r);
                        }
                    }
                }

                // Transfer the table copy into the real table.
                ((DefaultTableModel) myTable.getModel()).setDataVector(theData, theHeadings);
            }
            catch(Exception ex) {
                logger.info("Paste failed. {}", ex);
            }
        }
    }
}
