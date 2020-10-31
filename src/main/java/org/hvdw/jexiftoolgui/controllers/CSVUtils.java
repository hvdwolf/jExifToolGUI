package org.hvdw.jexiftoolgui.controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is using the opencsv library
 *
 * website & manual: http://opencsv.sourceforge.net/index.html
 * Download: https://sourceforge.net/projects/opencsv/
 */
public class CSVUtils {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(CSVUtils.class);


    public static String ReadCSV(String csvFile) throws IOException {

        String result = "";

        CSVReader reader = null;
        try {
            //reader = new CSVReader(new FileReaderHeaderAware(csvFile));
            reader = new CSVReader(new FileReader(csvFile));
            // Either use a String array
            String[] line;
            try {
                while ((line = reader.readNext()) != null) {
                    //System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
                }
            } catch (CsvValidationException e) {
                logger.error("CsvValidationException {}", e);
                e.printStackTrace();
            }
            // Or use a List<String[]>
            try {
                List<String[]> myEntries = reader.readAll();
            } catch (CsvException e) {
                logger.error("CsvValidationException {}", e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    return result;
    }

    public static void WriteCSV(String csvFile, String[] csvData) throws IOException {

        //CSVWriter writer = new CSVWriter(new FileWriter(csvFile), '\t');
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        writer.writeNext(csvData);
        writer.close();
    }

}