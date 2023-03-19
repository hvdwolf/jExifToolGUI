package org.hvdw.jexiftoolgui.controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hvdw.jexiftoolgui.controllers.StandardFileIO.getResourceAsStream;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is using the opencsv library
 *
 * website & manual: http://opencsv.sourceforge.net/index.html
 * Download: https://sourceforge.net/projects/opencsv/
 */
public class CSVUtils {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(CSVUtils.class);


    public static List<String[]> ReadCSV(Path csvFile) throws IOException {

        String[] line;

        List<String[]> csvList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(csvFile)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                //String[] line;
                while ((line = csvReader.readNext()) != null) {
                    csvList.add(line);
                }
            } catch (CsvValidationException e) {
                logger.error("CsvValidationException {}", e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    return csvList;
    }

    public static List<String[]> ReadCSVfromResources(String csvFile) throws IOException {

        String[] line;

        List<String[]> csvList = new ArrayList<>();
        InputStream is = getResourceAsStream(csvFile);
        //try (Reader reader = Files.newBufferedReader(Paths.get(csvFile))) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                //String[] line;
                while ((line = csvReader.readNext()) != null) {
                    csvList.add(line);
                }
            } catch (CsvValidationException e) {
                logger.error("CsvValidationException {}", e);
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvList;
    }

    public static void WriteCSV(String csvFile, String[] csvData) throws IOException {

        //CSVWriter writer = new CSVWriter(new FileWriter(csvFile), '\t');
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        writer.writeNext(csvData);
        writer.close();
    }

}