package org.hvdw.jexiftoolgui.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NominatimResult {
    private String[] geoPosition;  //display_Name, latitude,longitude;
    private String[] boundingbox; // contains X1, X2, Y1, Y2
    private Map address = new HashMap();

    public NominatimResult(String[] geoPosition, String[] boundingbox, Map address) {
        this.geoPosition = geoPosition;
        this.boundingbox = boundingbox;
        this.address = address;
    }

    public String[] getGeoPosition() {
        return geoPosition;
    }
    public String[] setGeoPosition(String[] geoPosition) {
        this.geoPosition = geoPosition;
        return geoPosition;
    }

    public String[] getBoundingbox() {
        return boundingbox;
    }
    public String[] setBoundingbox(String[] boundingbox) {
        this.boundingbox = boundingbox;
        return boundingbox;
    }

    public Map getAddress() {
        return address;
    }
    public Map setAddress(Map address) {
        this.address = address;
        return address;
    }
}
