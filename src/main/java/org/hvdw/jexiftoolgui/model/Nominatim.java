package org.hvdw.jexiftoolgui.model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.hvdw.jexiftoolgui.controllers.ButtonsActionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static org.slf4j.LoggerFactory.getLogger;

public class Nominatim {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(ButtonsActionListener.class);

    /**
     * This is the standard search routine for Nominatim
     * @param searchphrase
     * @return
     * @throws IOException
     */
    public static String SearchLocation(String searchphrase) throws IOException {
        String getResponse = "";
        final String baseurl = "https://nominatim.openstreetmap.org/search?q=";
        String geturl = baseurl + searchphrase.replace(" ", "+") + "&format=json";
        logger.debug("geturl {}", geturl);
        URL urlObj = new URL(geturl);
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            logger.debug(response.toString());
            getResponse = response.toString();
        } else {
            logger.error("The GET request to Nominatim {} did not work", geturl);
        }

        return getResponse;
    }


    /**
     * This method does a reverse search whe na user has right-clicked the map
     * @param lat
     * @param lon
     * @return
     * @throws IOException
     */
    public static String ReverseSearch(double lat, double lon) throws IOException {
        String getResponse = "";
        final String baseurl = "https://nominatim.openstreetmap.org/reverse?";
        String geturl = baseurl + "lat=" + lat + "&lon=" + lon + "&format=json";
        logger.debug("geturl {}", geturl);
        URL urlObj = new URL(geturl);
        HttpsURLConnection connection = (HttpsURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();
            logger.debug(response.toString());
            getResponse = response.toString();
        } else {
            logger.error("The GET request to Nominatim {} did not work", geturl);
        }

        return getResponse;
    }


    /**
     * Get list of strings containing (display_)name, latitude, longitude using simple-json
     * @param input
     * @return
     */
    public static List<String[]> parseLocationJson(String input) {
        List<String[]> placesList = new ArrayList<String[]>();

        JsonValue placeval = Json.parse(input);
        JsonArray places = Json.parse(input).asArray();
        logger.debug("places {}", places.toString());
        for (JsonValue place : places) {
            String display_Name = place.asObject().getString("display_name", "Unknown display_name");
            String lat = place.asObject().getString("lat", "Unknown latitude");
            String lon = place.asObject().getString("lon", "Unknown longitude");
            JsonArray boundingbox = (JsonArray) place.asObject().get("boundingbox");
            logger.debug("display_Name {} lat {} lon {} boundingbox {}", display_Name, lat, lon, boundingbox.toString());
            placesList.add(new String[]{display_Name, lat, lon, boundingbox.toString()});
        }
        return placesList;
    }

}
