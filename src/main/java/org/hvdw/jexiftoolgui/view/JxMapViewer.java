package org.hvdw.jexiftoolgui.view;

import ch.qos.logback.classic.Logger;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.model.Nominatim;
import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;

/**
 * This class draws the initial map, activates the mouse/key listeners. And handles the search/fin options
 * It uses the standard jxmapviewer2 functionality,
 * but in parallel the JXMapKit functionality is also built in, but that is not used currently
 */
public class JxMapViewer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField SearchtextField;
    private JButton searchLocationbutton;
    private JTable searchResultstable;
    private JLabel lblDisplay_Name;
    private JLabel lblLatitude;
    private JLabel lblLongitude;
    private JPanel MapViewerPane;
    private ListSelectionModel listSelectionModel;
    private String[] returnPlace;
    private String mapUsageHints;
    private JXMapViewer mapViewer = new JXMapViewer();
    private JXMapKit jXMapKit = new JXMapKit();
    private List<String[]> placesList = null;
    private boolean useXMapKit = false;

    private final static Logger logger = (Logger) LoggerFactory.getLogger(JxMapViewer.class);
    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;

    public JxMapViewer() {
        setContentPane(contentPane);
        setModal(true);
        this.setIconImage(Utils.getFrameIcon());
        getRootPane().setDefaultButton(searchLocationbutton);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Save last used position to preferences
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        searchLocationbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String getResult;
                //List<String[]> placesList = null;
                String searchphrase = SearchtextField.getText();
                if (!("".equals(searchphrase))) {
                    try {
                        getResult = Nominatim.SearchLocation(searchphrase);
                        placesList = Nominatim.parseLocationJson(getResult);
                        if (placesList.size() > 0) {
                            fillsearchResultstable(placesList);
                        } else {
                            JOptionPane.showMessageDialog(contentPane, ResourceBundle.getBundle("translations/program_strings").getString("mpv.noresults"), ResourceBundle.getBundle("translations/program_strings").getString("mpv.noresults"), JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (IOException e) {
                        logger.error("Nominatim search error {}", e.toString());
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, ResourceBundle.getBundle("translations/program_strings").getString("mpv.nosrchphrs"), ResourceBundle.getBundle("translations/program_strings").getString("mpv.nosrchphrs"), JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        searchResultstable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                super.mousePressed(mouseEvent);
                DefaultTableModel model = (DefaultTableModel) searchResultstable.getModel();
                int selectedRowIndex = searchResultstable.getSelectedRow();
                updateFieldsVariablesMap(model, selectedRowIndex);
                logger.info("row with search result pressed");
            }
        });
        searchResultstable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel model = (DefaultTableModel) searchResultstable.getModel();
                int selectedRowIndex = searchResultstable.getSelectedRow();
                updateFieldsVariablesMap(model, selectedRowIndex);
                logger.info("row with search result clicked");
            }
        });
    }


    private void onOK() {
        // add your code here
        returnPlace = new String[]{lblDisplay_Name.getText().trim(), lblLatitude.getText().trim(), lblLongitude.getText().trim()};
        if (!"".equals(lblLatitude.getText())) {
            try {
                Double doub = Double.parseDouble(lblLatitude.getText().trim());
                prefs.storeByKey(LATITUDE, lblLatitude.getText().trim());
            } catch (NumberFormatException ex) {
                prefs.storeByKey(LATITUDE, "52.515");
            }
        }
        if (!"".equals(lblLongitude.getText())) {
            try {
                Double doub = Double.parseDouble(lblLongitude.getText().trim());
                prefs.storeByKey(LONGITUDE, lblLongitude.getText().trim());
            } catch (NumberFormatException ex) {
                prefs.storeByKey(LONGITUDE, "6.098");
            }
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        returnPlace = new String[]{"", "", ""};
        dispose();
    }

    /**
     * This method fills the results table after a search for a location
     * @param placesList
     */
    void fillsearchResultstable(List<String[]> placesList) {
        DefaultTableModel model = (DefaultTableModel) searchResultstable.getModel();
        // make table uneditable
        searchResultstable.setDefaultEditor(Object.class, null);
        model.setColumnIdentifiers(new String[]{ResourceBundle.getBundle("translations/program_strings").getString("mpv.name"),
                ResourceBundle.getBundle("translations/program_strings").getString("mpv.tbllat"),
                ResourceBundle.getBundle("translations/program_strings").getString("mpv.tbllon")});
        searchResultstable.getColumnModel().getColumn(0).setPreferredWidth(300);
        searchResultstable.getColumnModel().getColumn(1).setPreferredWidth(100);
        searchResultstable.getColumnModel().getColumn(2).setPreferredWidth(100);
        listSelectionModel = searchResultstable.getSelectionModel();
        searchResultstable.setRowSelectionAllowed(true);
        searchResultstable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.setRowCount(0);
        Object[] row = new Object[1];

        // Now start filling the table
        for (String[] place : placesList) {
            model.addRow(new Object[]{place[0], place[1], place[2]});
        }
    }

    /**
     * This method is called when a user select a result place from the table
     * @param model
     * @param row
     */
    void updateFieldsVariablesMap(DefaultTableModel model, int row) {
        List<GeoPosition> boundingbox;
        int selectedRowIndex = searchResultstable.getSelectedRow();
        String display_name = model.getValueAt(selectedRowIndex, 0).toString();
        String strlat = model.getValueAt(selectedRowIndex, 1).toString();
        String strlon = model.getValueAt(selectedRowIndex, 2).toString();
        lblDisplay_Name.setText(model.getValueAt(selectedRowIndex, 0).toString());
        lblLatitude.setText(strlat);
        lblLongitude.setText(strlon);
        MyVariables.setLatitude(strlat);
        MyVariables.setLongitude(strlon);

        //mapViewer.setZoom(16);
        GeoPosition newPos = new GeoPosition(Double.parseDouble(strlat), Double.parseDouble(strlon));
        addWaypoint(newPos);
        mapViewer.setAddressLocation(newPos);
        mapViewer.setCenterPosition(newPos);
        jXMapKit.setAddressLocation(newPos);
        jXMapKit.setCenterPosition(newPos);
        for (String[] place : placesList) {
            if (display_name.equals(place[0])) {
                String[] tmpbb = place[3].substring(2, place[3].length() - 2).split("\",\"");
                GeoPosition topleft = new GeoPosition(Double.parseDouble(tmpbb[0]), Double.parseDouble(tmpbb[2]));
                GeoPosition topright = new GeoPosition(Double.parseDouble(tmpbb[1]), Double.parseDouble(tmpbb[2]));
                GeoPosition btmleft = new GeoPosition(Double.parseDouble(tmpbb[0]), Double.parseDouble(tmpbb[3]));
                GeoPosition btmright = new GeoPosition(Double.parseDouble(tmpbb[1]), Double.parseDouble(tmpbb[3]));
                boundingbox = Arrays.asList(topleft, topright, btmleft, btmright);
                mapViewer.zoomToBestFit(new HashSet<GeoPosition>(boundingbox), 0.7);
            }

        }
    }

    /**
     * This method is called when a use right-clicks on the map to get the clicked position
     *
     * @param latitude
     * @param longitude
     * @param xLatitude
     * @param xLongitude
     */
    void updateFieldsVariablesMap(double latitude, double longitude, double xLatitude, double xLongitude) {
        List<GeoPosition> boundingbox;

        if (useXMapKit) {
            lblLatitude.setText(String.valueOf(xLatitude));
            lblLongitude.setText(String.valueOf(xLongitude));
            MyVariables.setLatitude(String.valueOf(xLatitude));
            MyVariables.setLongitude(String.valueOf(xLongitude));
        } else {
            lblLatitude.setText(String.valueOf(latitude));
            lblLongitude.setText(String.valueOf(longitude));
            MyVariables.setLatitude(String.valueOf(latitude));
            MyVariables.setLongitude(String.valueOf(longitude));
        }

        //mapViewer.setZoom(16);
        GeoPosition newPos = new GeoPosition(latitude, longitude);
        GeoPosition newXPos = new GeoPosition(xLatitude, xLongitude);
        addWaypoint(newPos);
        mapViewer.setAddressLocation(newPos);
        mapViewer.setCenterPosition(newPos);
        jXMapKit.setAddressLocation(newXPos);
        jXMapKit.setCenterPosition(newXPos);

        try {
            String getResult = Nominatim.ReverseSearch(latitude, longitude);
            JsonValue place = Json.parse(getResult);
            String display_Name = place.asObject().getString("display_name", "");
            JsonArray bb = place.asObject().get("boundingbox").asArray();
            logger.debug("display_Name {} boundingbox {}", display_Name, bb.toString());
            GeoPosition topleft = new GeoPosition(Double.parseDouble(bb.get(0).asString()), Double.parseDouble(bb.get(2).asString()));
            GeoPosition topright = new GeoPosition(Double.parseDouble(bb.get(1).asString()), Double.parseDouble(bb.get(2).asString()));
            GeoPosition btmleft = new GeoPosition(Double.parseDouble(bb.get(0).asString()), Double.parseDouble(bb.get(3).asString()));
            GeoPosition btmright = new GeoPosition(Double.parseDouble(bb.get(1).asString()), Double.parseDouble(bb.get(3).asString()));
            boundingbox = Arrays.asList(topleft, topright, btmleft, btmright);
            mapViewer.zoomToBestFit(new HashSet<GeoPosition>(boundingbox), 0.7);

        } catch (IOException e) {
            logger.error("Nominatim.ReverseSearch error {}", e);
            e.printStackTrace();
        }
    }

    /** This method puts a waypoint marker on the map for the clicked location from the results table
     * or when a user has right-clicked the map
     * @param markerposition
     */
    void addWaypoint(GeoPosition markerposition) {
        Set<Waypoint> waypoints = new HashSet<Waypoint>(Arrays.asList(new DefaultWaypoint(markerposition)));

        //crate a WaypointPainter to draw the points
        WaypointPainter painter = new WaypointPainter();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }


    /**
     * This method builds the standard jxmapviewer2 map and activates the mouse listeners
     */
    void buildMapviewer() {
        useXMapKit = false;
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        mapViewer.setTileFactory(tileFactory);
        //final JLabel labelAttr = new JLabel();
        //mapViewer.setLayout(new BorderLayout());
        //mapViewer.add(labelAttr, BorderLayout.SOUTH);

        // Set the focus
        mapViewer.setZoom(7);
        GeoPosition zwolle = new GeoPosition(52.515, 6.098);
        String strlat = prefs.getByKey(LATITUDE, "52.515");
        String strlon = prefs.getByKey(LONGITUDE, "6.098");
        GeoPosition startPos = new GeoPosition(Double.parseDouble(strlat), Double.parseDouble(strlon));
        mapViewer.setAddressLocation(startPos);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);

        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        LocationSelector ls = new LocationSelector();
        mapViewer.addMouseListener(ls);
        //Add stuff to mapviewer pane
        MapViewerPane.add(new JLabel(mapUsageHints), BorderLayout.PAGE_START);
        String attributes = tileFactory.getInfo().getAttribution() + " - " + tileFactory.getInfo().getLicense();
        MapViewerPane.add(new JLabel(attributes), BorderLayout.PAGE_END);
        MapViewerPane.add(mapViewer);
    }

    /**
     * This method builds the JXMapKit mapviewer. On one hand it has some features like a slider, plus/min buttons and a minimap
     * But it lacks the zoomtobestfit option
     */
    void buildXMapkitviewer() {
        useXMapKit = true;
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        jXMapKit.setTileFactory(tileFactory);

        // Set the focus
        jXMapKit.setZoom(7);
        GeoPosition zwolle = new GeoPosition(52.515, 6.098);
        String strlat = prefs.getByKey(LATITUDE, "52.515");
        String strlon = prefs.getByKey(LONGITUDE, "6.098");
        GeoPosition startPos = new GeoPosition(Double.parseDouble(strlat), Double.parseDouble(strlon));
        jXMapKit.setAddressLocation(startPos);

        // Add interactions
        JXMapViewer map = jXMapKit.getMainMap();
        MouseInputListener mia = new PanMouseInputListener(map);
        jXMapKit.addMouseListener(mia);
        LocationSelector ls = new LocationSelector();
        map.addMouseListener(ls);

        jXMapKit.addMouseMotionListener(mia);

        jXMapKit.addMouseListener(new CenterMapListener(map));

        jXMapKit.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));

        jXMapKit.addKeyListener(new PanKeyListener(map));

        jXMapKit.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // ignore
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                JXMapViewer map = jXMapKit.getMainMap();

                // convert to world bitmap
                Point2D worldPos = map.getTileFactory().geoToPixel(startPos, map.getZoom());

                // convert to screen
                Rectangle rect = map.getViewportBounds();
                int sx = (int) worldPos.getX() - rect.x;
                int sy = (int) worldPos.getY() - rect.y;
                Point screenPos = new Point(sx, sy);

            }
        });
        //Add stuff to mapviewer pane
        //MapViewerPane.setPreferredSize(1150, 600);
        MapViewerPane.add(new JLabel(mapUsageHints), BorderLayout.PAGE_START);
        String attributes = tileFactory.getInfo().getAttribution() + " - " + tileFactory.getInfo().getLicense();
        MapViewerPane.add(new JLabel(attributes), BorderLayout.PAGE_END);
        MapViewerPane.add(jXMapKit);
    }

    /**
     * This class provides the right-click mouselistener to get the geoposition from the map
     */
    private class LocationSelector implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            JXMapViewer map = jXMapKit.getMainMap();
            if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
                Point p = e.getPoint();
                GeoPosition geox = map.convertPointToGeoPosition(p);
                GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
                logger.debug("X: {} Y: {}", geo.getLatitude(), geo.getLongitude());
                //logger.debug("X: {} Y: {}", geox.getLatitude(), geox.getLongitude());
                GeoPosition newPos = new GeoPosition(geo.getLatitude(), geo.getLongitude());
                GeoPosition newXPos = new GeoPosition(geox.getLatitude(), geox.getLongitude());
                jXMapKit.setAddressLocation(newXPos);
                jXMapKit.setCenterPosition(newXPos);
                mapViewer.setAddressLocation(newPos);
                mapViewer.setCenterPosition(newPos);
                updateFieldsVariablesMap(geo.getLatitude(), geo.getLongitude(), geox.getLatitude(), geox.getLongitude());
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    // The  main" function of this class
    public String[] showDialog() {
        //JxMapViewer2 dialog = new JxMapViewer2();
        setTitle(ResourceBundle.getBundle("translations/program_strings").getString("mpv.title"));
        pack();
        double x = getParent().getBounds().getCenterX();
        double y = getParent().getBounds().getCenterY();
        //setLocation((int) x - getWidth() / 2, (int) y - getHeight() / 2);
        setLocationRelativeTo(null);

        // Create the mapviewer panel
        mapUsageHints = ResourceBundle.getBundle("translations/program_strings").getString("mpv.hints");
        buildMapviewer();
        //buildXMapkitviewer();
        setVisible(true);

        return returnPlace;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setPreferredSize(new Dimension(1200, 700));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.okbtn"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/program_strings", "dlg.close"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        panel3.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setPreferredSize(new Dimension(350, -1));
        splitPane1.setRightComponent(panel4);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchResultstable = new JTable();
        searchResultstable.setAutoResizeMode(0);
        searchResultstable.setPreferredScrollableViewportSize(new Dimension(400, 400));
        scrollPane1.setViewportView(searchResultstable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.searchlbl"));
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        SearchtextField = new JTextField();
        SearchtextField.setToolTipText("<HTML>Eiffel tower OR<br> Downingstreet 10 OR<br> Pennsylvania Avenue Northwest 1600 OR<br>Iguazu Falls");
        panel5.add(SearchtextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 1, false));
        searchLocationbutton = new JButton();
        this.$$$loadButtonText$$$(searchLocationbutton, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.searchbtn"));
        panel5.add(searchLocationbutton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel4.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.location"));
        panel6.add(label2);
        lblDisplay_Name = new JLabel();
        lblDisplay_Name.setText("");
        panel6.add(lblDisplay_Name);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel4.add(panel7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.lon"));
        panel7.add(label3);
        lblLongitude = new JLabel();
        lblLongitude.setText("");
        panel7.add(lblLongitude);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel4.add(panel8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("translations/program_strings", "mpv.lat"));
        panel8.add(label4);
        lblLatitude = new JLabel();
        lblLatitude.setText("");
        panel8.add(lblLatitude);
        MapViewerPane = new JPanel();
        MapViewerPane.setLayout(new BorderLayout(0, 0));
        MapViewerPane.setPreferredSize(new Dimension(1150, 600));
        splitPane1.setLeftComponent(MapViewerPane);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
