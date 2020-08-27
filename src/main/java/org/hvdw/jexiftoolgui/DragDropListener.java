package org.hvdw.jexiftoolgui;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.commons.io.FileUtils;

import java.io.FileWriter;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;

/*
/ DragDropListener: This was the starting point but it is no longer used anymore as there
/ is a much simpler method in the Gui.
 */

class DragDropListener implements DropTargetListener {
    private final static Logger logger = LoggerFactory.getLogger(DragDropListener.class);

    @Override
    public void drop(DropTargetDropEvent event) {

        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {

            try {

                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
                    List <File> files = (List) transferable.getTransferData(flavor);

                    // Loop them through
                    for (File file : files) {

                        // Print out the file path
                        logger.info("File path is: {}", file.getPath());
                    }
                }

            } catch (Exception e) {
                // Print out the error stack
                e.printStackTrace();
                logger.error("drop error: {}", e);
            }
        }
        // Drop complete, write file
        File drop = new File( System.getProperty("user.home") + File.separator + "jexiftoolgui_data" + File.separator +  "drop_complete");
        try {
            //FileUtils.touch(drop);
            FileWriter fwdrop = new FileWriter(drop, false);
            fwdrop.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("FileUtils touch failed {}", e);
        }
        // Inform that the drop is complete
        Utils.LoadfilesAfterDrop();
        event.dropComplete(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }

}
