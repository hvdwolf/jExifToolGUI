package org.hvdw.jexiftoolgui.metadata;

import org.hvdw.jexiftoolgui.MyVariables;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/** This class will be used to search through metadata on both tag and value for loaded images
 * Maybe later I might implement a background process to store metadata also in the database (and then create a special metadata database?)
 */
public class SearchMetaData {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(SearchMetaData.class);

    HashMap<String, HashMap<String, String>> imagesData = MyVariables.getimagesData();

    public static List<String> searchMetaData(JPanel rootPanel, String searchPhrase) {
        List<String> result = new ArrayList<>();
        HashMap <String, HashMap<String, String> > imagesData = MyVariables.getimagesData();
        //logger.info("in searchmetada \n\n{}", imagesData.toString());

        for (Map.Entry<String, HashMap<String, String>> outerEntry: imagesData.entrySet()) {
            String imageName = outerEntry.getKey();
            HashMap<String, String> singleImageMetaData = outerEntry.getValue();

            //now loop through the single image hashmap with the tag & value
            Set<Map.Entry<String, String>> entrySet = singleImageMetaData.entrySet();
            searchPhrase = searchPhrase.toLowerCase();
            for (Map.Entry<String, String> entry: entrySet) {
                //logger.info("imageName {}", imageName);
                if (entry.getKey().toLowerCase().contains(searchPhrase)) {
                    result.add(imageName + "\tkey-value\t" + entry.getKey() + "\t" + entry.getValue());
                    logger.debug("imageName {}: found key {} with value {}", imageName, entry.getKey(), entry.getValue());
                }
                if (entry.getValue().toLowerCase().contains(searchPhrase)) {
                    result.add(imageName + "\tvalue-key\t" + entry.getValue() + "\t" + entry.getKey());
                    logger.debug("imageName {}: found value {} with corresponding key {}", imageName, entry.getValue(), entry.getKey());
                }
            }
            // Now sort the list of results, if not empty
            if ((result != null) && !(result.isEmpty())) {
                result = result.stream().sorted().collect(Collectors.toList());
            }
        }

        return result;
    }
}
