package org.hvdw.jexiftoolgui;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    private static Table topTable(Table table, File tmpfile) {
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.name"))));
        table.addCell(new Cell().add(new Paragraph(tmpfile.getName())));
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.path"))));
        table.addCell(new Cell().add(new Paragraph(tmpfile.getParent())));
        table.addCell(new Cell().add(new Paragraph(ResourceBundle.getBundle("translations/program_strings").getString("exppdf.image"))));
        String imageFile = tmpfile.getPath();
        try {
            ImageData data = ImageDataFactory.create(imageFile);
            Image img = new Image(data);
            table.addCell(new Cell().add(img.setAutoScale(true)));
        } catch (MalformedURLException e) {
            logger.error("Can't create image object for PDF {}", e);
            e.printStackTrace();
        }

        return table;
    }

    /**
     * This fillMetadataTable creates the table with the requested metadata and returns it to the document
     * @param exiftoolInfo
     * @param table
     * @return
     */
    private static Table fillMetadataTable(String exiftoolInfo, Table table) {
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

    public static void WriteToPDF(JRadioButton[] PDFradiobuttons, JComboBox[] PDFcomboboxes, JProgressBar progressBar) {
        List<String> cmdparams = new ArrayList<String>();
        File[] files = MyVariables.getLoadedFiles();
        int[] selectedIndices = MyVariables.getSelectedFilenamesIndices();
        String imgPath;
        String filename;
        String filepath;
        String pdfnamepath;

        boolean isWindows = Utils.isOsFromMicrosoft();

        String[] params = GetDesiredParams(PDFradiobuttons, PDFcomboboxes);
        cmdparams.add(Utils.platformExiftool());
        for (int index : selectedIndices) {
            String res = Utils.getImageInfoFromSelectedFile(params, index);
            //image
            if (isWindows) {
                imgPath = files[index].getPath().replace("\\", "/");
            } else {
                imgPath = files[index].getPath();
            }
            filename = files[index].getName();

            File tmpfile = files[index];
            filepath = files[index].getParent();
            pdfnamepath = filepath + File.separator + Utils.getFileNameWithoutExtension(filename) + ".pdf";
            logger.debug("filename {}; filepath {}", filename, filepath);
            logger.debug("pdfnamepath {}", pdfnamepath);

            // Creating a PdfWriter object
            try {
                PdfWriter writer = new PdfWriter(pdfnamepath);
                // Creating a PdfDocument object
                PdfDocument pdfDoc = new PdfDocument(writer);
                // Creating a Document object
                Document doc = new Document(pdfDoc);
                //Document doc = new Document(pdfDoc, pdfDoc.setDefaultPageSize(PageSize.A4));
                // Creating a table
                float[] pointColumnWidths = {150f, 150f};
                Table table = new Table(pointColumnWidths);
                doc.add(topTable(table, tmpfile));
                Paragraph paragraph1 = new Paragraph("\n\n"+ ResourceBundle.getBundle("translations/program_strings").getString("exppdf.metadata") + " " + filename);
                doc.add(paragraph1);

                // Now writing the metadata table
                float[] mdpointColumnWidths = {100f, 260f, 450f};
                Table metadatatable = new Table(mdpointColumnWidths);
                metadatatable.setFontSize(10);
                doc.add(fillMetadataTable(res, metadatatable));

                doc.close();

            } catch (FileNotFoundException e) {
                logger.error("pdf file not found error {}", e);
                e.printStackTrace();
            }

        }
    }
}
