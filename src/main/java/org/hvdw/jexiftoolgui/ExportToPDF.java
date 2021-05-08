package org.hvdw.jexiftoolgui;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.hvdw.jexiftoolgui.Application.OS_NAMES.APPLE;
import static org.hvdw.jexiftoolgui.Utils.getCurrentOsName;
import static org.hvdw.jexiftoolgui.Utils.getFileExtension;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class ExportToPDF {

    private final static Logger logger = (Logger) LoggerFactory.getLogger(ExportToPDF.class);
    // radiobuttons {A4radioButton, LetterradioButton, ImgSizeLargeradioButton, ImgSizeSmallradioButton,
    // pdfPerImgradioButton, pdfCombinedradioButton, pdfradioButtonExpAll, pdfradioButtonExpCommonTags, pdfradioButtonExpByTagName}
    // comboboxes {pdfcomboBoxExpCommonTags, pdfcomboBoxExpByTagName}

    //new page document.add(new AreaBreak());


    private static String[] GetDesiredParams(JRadioButton[] PDFradiobuttons, JComboBox[] PDFcomboboxes) {
        String[] params = MyConstants.ALL_PARAMS;
        if (PDFradiobuttons[6].isSelected()) {
            return MyConstants.ALL_PARAMS;
        } else if (PDFradiobuttons[7].isSelected()) {
            params = Utils.getWhichCommonTagSelected(PDFcomboboxes[0]);
        } else if (PDFradiobuttons[8].isSelected()) {
            params = Utils.getWhichCommonTagSelected(PDFcomboboxes[1]);
        }
        return params;
    }

    private static String GetConvertImage(File tmpfile) {
        boolean basicextension = false;
        boolean RAWextension = false;
        String imageFile = "";
        String tmpfilename = "";
        String[] raw_extensions = MyConstants.RAW_IMAGES;
        String[] basic_extensions = MyConstants.BASIC_EXTENSIONS;

        String filenameExt = getFileExtension(tmpfile);
        String filename = tmpfile.getName();

        for (String ext : basic_extensions) {
            if (filenameExt.toLowerCase().equals(ext)) { // it is either bmp, gif, jp(e)g, png or tif(f)
                basicextension = true;
                break;
            }
        }
        if (basicextension) {
            imageFile = tmpfile.getPath();
        } else {
            // loop through RAW extensions
            for (String ext : raw_extensions) {
                if (filenameExt.toLowerCase().equals(ext)) { // it is either bmp, gif, jp(e)g, png or tif(f)
                    RAWextension = true;
                    break;
                }
            }
            logger.debug("getconvertimage RAW RAWextion {}", RAWextension);
            //Use the ImageFunctions.ExtractPreviews(File file) for Raws and check on jpgfromraw and previewimage
            if (RAWextension) {
                String exportResult = ImageFunctions.ExtractPreviews(tmpfile);
                if ("Success".equals(exportResult)) {
                    //hoping we have a JPGfromRAW
                    tmpfilename = filename.substring(0, filename.lastIndexOf('.')) + "_JpgFromRaw.jpg";
                    tmpfile = new File (MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
                    logger.debug("getconvertimage RAW jpgfromraw {}", tmpfile.toString());
                    if (tmpfile.exists()) {
                        imageFile = tmpfile.getPath();
                    } else {
                        tmpfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
                        tmpfile = new File (MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
                        logger.debug("getconvertimage RAW jPreviewImage {}", tmpfile.toString());
                        if (tmpfile.exists()) {
                            imageFile = tmpfile.getPath();
                        } else {
                            logger.debug("getconvertimage RAW cantconvert");
                            imageFile = "/cantconvert.png";
                        }
                    }
                } else {
                    imageFile = "/cantconvert.png";
                }

            } else if ( (filenameExt.toLowerCase().equals("heic")) || (filenameExt.toLowerCase().equals("heif")) ) {
                Application.OS_NAMES currentOsName = getCurrentOsName();
                if ( currentOsName == APPLE) {
                    String exportResult = ImageFunctions.sipsConvertToJPG(tmpfile, "pdfexport");
                    if ("Success".equals(exportResult)) {
                        tmpfilename = filename.substring(0, filename.lastIndexOf('.')) + ".jpg";
                        tmpfile = new File(MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
                        imageFile = tmpfile.getPath();
                        logger.debug("getconvertimage HEIC convert {}", tmpfile.toString());
                    } else { // we have some error
                        logger.debug("getconvertimage HEIC cantconvert");
                        imageFile = "/cantconvert.png";
                    }
                } else { //we are not on Apple
                    imageFile = "/cantconvert.png";
                }

            } else if ( (filenameExt.toLowerCase().equals("mp4")) || (filenameExt.toLowerCase().equals("m4v")) ) {
                String exportResult = ImageFunctions.ExportPreviewsThumbnailsForIconDisplay(tmpfile);
                if ("Success".equals(exportResult)) {
                    logger.debug("getconvertimage MP4 export thumbnails successful");
                    tmpfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
                    tmpfile = new File (MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
                    if (tmpfile.exists()) {
                        imageFile = tmpfile.getPath();
                        logger.debug("getconvertimage MP4 convert {}", tmpfile.toString());
                    } else {
                        tmpfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
                        tmpfile = new File (MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
                        if (tmpfile.exists()) {
                            imageFile = tmpfile.getPath();
                            logger.debug("getconvertimage MP4 convert {}", tmpfile.toString());
                        } else {
                            logger.debug("getconvertimage MP4 cant convert");
                            imageFile = "/cantconvert.png";
                        }
                    }
                } else {
                    imageFile = "/cantconvert.png";
                }

            } else { // if all fails .....
                logger.debug("getconvertimage ..if all fails .. cant convert");
                imageFile = "/cantconvert.png";
            }
        }

        return imageFile;
    }


    /**
     * Method toptable writes the table with the filename, path and the image itself
     * @param tmpfile
     * @return
     */
    private static Table topTable(File tmpfile) {

        float[] pointColumnWidths = {150f, 150f};
        Table table = new Table(pointColumnWidths);
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.name"))));
        table.addCell(new Cell().add(new Paragraph(tmpfile.getName())));
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.path"))));
        table.addCell(new Cell().add(new Paragraph(tmpfile.getParent())));
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.image"))));

        String imageFile = GetConvertImage(tmpfile);
        if ("/cantconvert.png".equals(imageFile)) {
            ImageIcon icon = null;
            try {
                java.awt.Image img = ImageIO.read(mainScreen.class.getResource("/cantdisplay.png"));
                ImageData data = ImageDataFactory.create(img, null);
                Image newImg = new Image(data);
                //icon = new ImageIcon(img);
                table.addCell(new Cell().add(newImg.setAutoScale(true)));
            } catch (IOException e){
                logger.error("Error loading image", e);
                icon = null;
            }
        } else {
            try {
                ImageData data = ImageDataFactory.create(imageFile);
                Image img = new Image(data);
                table.addCell(new Cell().add(img.setAutoScale(true)));
            } catch (MalformedURLException e) {
                logger.error("Can't create image object for PDF {}", e);
                e.printStackTrace();
            }
        }

        return table;
    }

    /**
     * This fillMetadataTable creates the table with the requested metadata based on the info returned from exiftool and returns the table to the document
     * @param exiftoolInfo
     * @return
     */
    private static Table fillMetadataTable(String exiftoolInfo) {
        float[] mdpointColumnWidths = {100f, 260f, 450f};
        Table table = new Table(mdpointColumnWidths);

        table.setFontSize(10);
        if (exiftoolInfo.length() > 0) {
            if (exiftoolInfo.trim().startsWith("Warning")) {
                table.addCell(new Cell().add(new Paragraph("ExifTool")));
                table.addCell(new Cell().add(new Paragraph("Warning")));
                table.addCell(new Cell().add(new Paragraph("Invalid Metadata data")));
            } else if (exiftoolInfo.trim().startsWith("Error")) {
                table.addCell(new Cell().add(new Paragraph("ExifTool")));
                table.addCell(new Cell().add(new Paragraph("Error")));
                table.addCell(new Cell().add(new Paragraph("Invalid Metadata data")));
            } else {
                String[] lines = exiftoolInfo.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                for (String line : lines) {
                    //String[] cells = lines[i].split(":", 2); // Only split on first : as some tags also contain (multiple) :
                    String[] cells = line.split("\\t", 3);
                    table.addCell(new Cell().add(new Paragraph(cells[0])));
                    table.addCell(new Cell().add(new Paragraph(cells[1])));
                    table.addCell(new Cell().add(new Paragraph(cells[2])));
                }
            }
        }

        return table;
    }

    /**
     * This fillMetadataTable creates the table with the requested metadata based on the allMetadata ListArray coming
     * from the CompareImagesWindow and returns the table to the document
     * @param imageMetadata
     * @return
     */
    private static Table fillMetadataTable(List<String[]> imageMetadata) {
        float[] mdpointColumnWidths = {100f, 260f, 450f};
        Table table = new Table(mdpointColumnWidths);

        table.setFontSize(10);
        if (imageMetadata.size() > 0) {
            for (String[] row : imageMetadata) {
                table.addCell(new Cell().add(new Paragraph(row[2])));
                table.addCell(new Cell().add(new Paragraph(row[3])));
                table.addCell(new Cell().add(new Paragraph(row[4])));
            }
        }

        return table;
    }

    /**
     * This method is called from the mainScreen and starts a background task "WriteToPDF" while an infinite progressbar is keeping the user informed
     * @param rootPanel
     * @param PDFradiobuttons
     * @param PDFcomboboxes
     * @param progressBar
     * @return
     */
    public static void CreatePDFs(JPanel rootPanel, JRadioButton[] PDFradiobuttons, JComboBox[] PDFcomboboxes, JProgressBar progressBar, JLabel outputLabel, String ExpImgFoldertextField, boolean includeSubFolders) {
        String pdfdocs = "";
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        WriteToPDF(PDFradiobuttons, PDFcomboboxes, progressBar, ExpImgFoldertextField, includeSubFolders);
                        progressBar.setVisible(false);
                        outputLabel.setText("");
                        JOptionPane.showMessageDialog(rootPanel, String.format(ProgramTexts.HTML, 400, (ResourceBundle.getBundle("translations/program_strings").getString("exppdf.pdfscreated") + ":<br><br>" + MyVariables.getpdfDocs()), ResourceBundle.getBundle("translations/program_strings").getString("exppdf.pdfscreated"), JOptionPane.INFORMATION_MESSAGE));
                        // Somehow something keeps the focus off of the main window
                        rootPanel.setFocusable(true);
                        rootPanel.requestFocus();
                        //rootPanel.requestFocusInWindow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception ex) {
                    logger.debug("Error executing command");
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressBar.setVisible(true);
                outputLabel.setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.exppdf"));
            }
        });
    }

    /**
     * This method really writes the PDF. either a pdf per image, or one big pdf for all images.
     * It is called from the Export/Import tab
     * @param PDFradiobuttons
     * @param PDFcomboboxes
     * @param progressBar
     * @return
     */
    public static void WriteToPDF(JRadioButton[] PDFradiobuttons, JComboBox[] PDFcomboboxes, JProgressBar progressBar, String ExpImgFoldertextField, boolean includeSubFolders) {
        List<String> cmdparams = new ArrayList<String>();
        File[] files = null;

        int[] selectedIndices = null;
        if (!("".equals(ExpImgFoldertextField))) { // folder takes precedence over preview files
            //the pdf export is not an exiftool function. We can't simply specify a folder
            // We need to read the files in the folder
            File directoryPath = new File(ExpImgFoldertextField);
            //List of all files and directories
            if (includeSubFolders) {
                // recurse into subfolders
                List<String> tmpfiles = null;
                try {
                    /*Files.walk(Paths.get(ExpImgFoldertextField))
                            .filter(Files::isRegularFile)
                            .forEach(System.out::println);*/
                    Stream<Path> pathStream = Files.walk(Paths.get(ExpImgFoldertextField)).filter(Files::isRegularFile);
                    Path[] pathArray= pathStream.toArray(Path[]::new);
                    int Length = pathArray.length;
                    files = new File[Length];
                    selectedIndices = new int[Length];
                    for (int i = 0; i < Length; ++i) {
                        files[i] = pathArray[i].toFile();
                        selectedIndices[i] = i;
                    }
                    MyVariables.setLoadedFiles(files);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                FileFilter filterOnFiles = new FileFilter() {
                    public boolean accept(File file) {
                        boolean isFile = file.isFile();
                        if (isFile) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
                files = directoryPath.listFiles(filterOnFiles);
                MyVariables.setLoadedFiles(files);
                int Length = files.length;
                selectedIndices = new int[Length];
                for (int i = 0; i < Length; i++) {
                    selectedIndices[i] = i;
                }
            }
        } else { // Normal selection via previews
            files = MyVariables.getLoadedFiles();
            selectedIndices = MyVariables.getSelectedFilenamesIndices();
        }
        File tmpfile;
        String filename;
        String pdfnamepath = "";
        Document doc = null;
        String producedDocs = "";

        boolean isWindows = Utils.isOsFromMicrosoft();
        String[] params = GetDesiredParams(PDFradiobuttons, PDFcomboboxes);
        cmdparams.add(Utils.platformExiftool());

        if (PDFradiobuttons[5].isSelected()) { // one combined document
            try {
                tmpfile = files[0];
                pdfnamepath = tmpfile.getParent() + File.separator + "Combined.pdf";
                PdfWriter bigWriter = new PdfWriter(pdfnamepath);
                PdfDocument pdfCombiDoc = new PdfDocument(bigWriter);
                doc = new Document(pdfCombiDoc);
            } catch (FileNotFoundException e) {
                logger.error("pdf file not found error {}", e);
                e.printStackTrace();
                doc.close();
            }
        }


        for (int index : selectedIndices) {
            String res = Utils.getImageInfoFromSelectedFile(params, index);
            filename = files[index].getName();

            tmpfile = files[index];

            if (!(PDFradiobuttons[5].isSelected())) { //User wants a document per image
                pdfnamepath = tmpfile.getParent() + File.separator + Utils.getFileNameWithoutExtension(filename) + ".pdf";
                logger.debug("pdfnamepath {}", pdfnamepath);
                try {
                    PdfWriter writer = new PdfWriter(pdfnamepath);
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    doc = new Document(pdfDoc);
                    // Creating the top table
                    doc.add(topTable(tmpfile));
                    Paragraph paragraph1 = new Paragraph("\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("exppdf.metadata") + " " + filename);
                    doc.add(paragraph1);
                    // Now writing the metadata table
                    doc.add(fillMetadataTable(res));
                    doc.close();
                    producedDocs += pdfnamepath + "<br>";
                } catch (FileNotFoundException e) {
                    logger.error("pdf file not found error {}", e);
                    e.printStackTrace();
                    doc.close();
                }
            } else { // So the user wants one big document
                doc.add(topTable(tmpfile));
                Paragraph paragraph1 = new Paragraph("\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("exppdf.metadata") + " " + filename);
                doc.add(paragraph1);
                // Now writing the metadata table
                doc.add(fillMetadataTable(res));
                doc.add(new AreaBreak());
            }
        }
        if (PDFradiobuttons[5].isSelected()) { // one combined document
            producedDocs = pdfnamepath;
            doc.close();
        }

        MyVariables.setpdfDocs(producedDocs);
        logger.debug("producedDocs {}", producedDocs);

    }

    /**
     * This method writes the pdf and is called from the CompareImagesWindow for the there displayed info
     * @param allMetadata
     */
    public static void WriteToPDF(List<String[]> allMetadata) {
        List<String[]> imageMetadata = new ArrayList<String[]>();
        File[] files = MyVariables.getLoadedFiles();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        File tmpfile;
        String filename;
        String pdfnamepath = "";
        Document doc = null;
        String producedDocs = "";

        boolean isWindows = Utils.isOsFromMicrosoft();
        for (int index : selectedIndices) {
            // First get the data belonging to this file (index)
            for (String[] row : allMetadata) {
                if (Integer.valueOf(row[1]) == index) {
                    imageMetadata.add(row);
                    logger.trace("index {} rowdata {}", index, Arrays.toString(row));
                }
            }
            filename = files[index].getName();

            tmpfile = files[index];
            pdfnamepath = tmpfile.getParent() + File.separator + Utils.getFileNameWithoutExtension(filename) + ".pdf";
            logger.debug("pdfnamepath {}", pdfnamepath);
            try {
                PdfWriter writer = new PdfWriter(pdfnamepath);
                PdfDocument pdfDoc = new PdfDocument(writer);
                doc = new Document(pdfDoc);
                // Creating the top table
                doc.add(topTable(tmpfile));
                Paragraph paragraph1 = new Paragraph("\n\n" + ResourceBundle.getBundle("translations/program_strings").getString("exppdf.metadata") + " " + filename);
                doc.add(paragraph1);
                // Now writing the metadata table
                doc.add(fillMetadataTable(imageMetadata));
                doc.close();
                producedDocs += pdfnamepath + "<br>";
            } catch (FileNotFoundException e) {
                logger.error("pdf file not found error {}", e);
                e.printStackTrace();
                doc.close();
            }
            MyVariables.setpdfDocs(producedDocs);
            logger.debug("producedDocs {}", producedDocs);
        }
    }
}
