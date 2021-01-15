package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.ProgramTexts;
import org.hvdw.jexiftoolgui.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/** This class will be used to search through metadata on both tag and value for loaded images
 * Maybe later I might implement a background process to store metadata also in the database (and then create a special metadata database?)
 */
public class SearchMetaData {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(SearchMetaData.class);

    HashMap<String, HashMap<String, String>> imagesData = MyVariables.getimagesData();

    public static String searchMetaData(JPanel rootPanel, String searchPhrase) {
        String result = "";
        HashMap <String, HashMap<String, String> > imagesData = MyVariables.getimagesData();


        for (Map.Entry<String, HashMap<String, String>> outerEntry: imagesData.entrySet()) {
            String imageName = outerEntry.getKey();
            HashMap<String, String> singleImageMetaData = outerEntry.getValue();

            //now loop through the single image hashmap with the tag & value
            Set<Map.Entry<String, String>> entrySet = singleImageMetaData.entrySet();
            for (Map.Entry<String, String> entry: entrySet) {
                if (entry.getKey().contains(searchPhrase)) {
                    logger.debug("imageName {}: found key {} with value {}", imageName, entry.getKey(), entry.getValue());
                }
                if (entry.getValue().contains(searchPhrase)) {
                    logger.debug("imageName {}: found value {} with corresponding key {}", imageName, entry.getValue(), entry.getKey());
                }
            }

        }


        return result;
    }
}
