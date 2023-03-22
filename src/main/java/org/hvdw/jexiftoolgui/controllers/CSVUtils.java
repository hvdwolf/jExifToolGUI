package org.hvdw.jexiftoolgui.controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBaseBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
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


    public static List<String[]> ReadCSV(Path csvFile, String separator) throws IOException {

        String[] line;

        // CSVReader reader = new CSVReaderBuilder(inputStreamReader)
        //                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
        //                // Skip the header
        //                .withSkipLines(1)
        //                .build();

        //         try (Reader reader = Files.newBufferedReader(csvFile)) {
        //            try (CSVReader csvReader = new CSVReader(reader)) {
        List<String[]> csvList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(csvFile)) {
            //try (CSVReader csvReader = new CSVReader(reader, "\t")){
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).withSkipLines(1).build()) {
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

        //logger.info("the list {}", csvList);
        return csvList;
    }

    public static List<String[]> ReadCSVfromResources(String csvFile) throws IOException {

        String[] line;

        List<String[]> csvList = new ArrayList<>();
        InputStream is = getResourceAsStream(csvFile);
        //try (Reader reader = Files.newBufferedReader(Paths.get(csvFile))) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS).withSkipLines(1).build()) {
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

    public static void WriteCSVList(String csvFile, List<String[]> csvData) throws IOException {
        //using custom delimiter and quote character
        //CSVWriter csvWriter = new CSVWriter(writer, '#', '\'');
        FileWriter fw = new FileWriter(new File(csvFile));
        try (CSVWriter writer = new CSVWriter(fw)) {
            for (String[] line : csvData) {
                writer.writeNext(line);
            }
        }
    }

}