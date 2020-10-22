package org.hvdw.jexiftoolgui.model;

import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;

import javax.swing.*;

import java.awt.*;

import static org.hvdw.jexiftoolgui.facades.IPreferencesFacade.PreferenceKey.*;
import static org.slf4j.LoggerFactory.getLogger;

public class GuiConfig {

    public static int guiWidth;
    public static int guiHeight;

    private final static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) getLogger(GuiConfig.class);

    /**
     * Gets the current width and height from the rootPanel (not the frame)
     * @param rootPanel
     */
    public static void GuiSize(JPanel rootPanel){
        guiWidth = rootPanel.getWidth();
        guiHeight = rootPanel.getHeight();
        logger.debug("rootPanel size {} x {}", guiWidth, guiHeight);
    }
    public static void GuiSize(JFrame frame){
        guiWidth = frame.getWidth();
        guiHeight = frame.getHeight();
        logger.debug("frame size {} x {}", guiWidth, guiHeight);
    }

    /**
     * SplitPaneDividerPercentage returns the position as a value like 0.33 (450/1350) or so
     * @param splitpane
     */
    private static double SplitPaneDividerPercentage( JSplitPane splitpane) {
        double SplitPositionPercentage;
        // getDividerLocation is int, setDividerLocation is double
        double splitposition = splitpane.getDividerLocation() /
                (double) (splitpane.getWidth() - splitpane.getDividerSize());
        return SplitPositionPercentage = ((int) (100 * (splitposition + 0.005))) / (double) 100;
    }

    /**
     * SaveGuiConfig saves Width, Height and JSplitpane position to Preferences
     * This method is called when the user nicely uses the File -> Exit method
     * @param rootPanel
     * @param splitpanel
     */
    public static void SaveGuiConfig(JFrame frame, JPanel rootPanel, JSplitPane splitpanel) {
        //GuiSize(rootPanel);
        GuiSize(frame);
        double SPP = SplitPaneDividerPercentage(splitpanel);
        prefs.storeByKey(GUI_WIDTH, String.valueOf(guiWidth));
        prefs.storeByKey(GUI_HEIGHT, String.valueOf(guiHeight));
        prefs.storeByKey(SPLITPANEL_POSITION, String.valueOf(SPP));
        logger.debug("Save Gui Settings: Width x Height {}x{}; splitpanel postion {}", guiWidth, guiHeight, String.valueOf(SPP));
    }

    /**
     * This SaveGuiConfig method saves only the frame size and is invoked when the user presses the exit button in the menubar
     * @param frame
     */
    public static void SaveGuiConfig(JFrame frame) {
        //GuiSize(rootPanel);
        GuiSize(frame);
        prefs.storeByKey(GUI_WIDTH, String.valueOf(guiWidth));
        prefs.storeByKey(GUI_HEIGHT, String.valueOf(guiHeight));
    }


    /**
     * LoadGuiConfig loads Width, Height and JSplitpane postion from Preferences and restores the Gui
     * to the state when the user exited the application
     * @param rootPanel
     * @param splitpanel
     */
    public static void LoadGuiConfig(JPanel rootPanel, JSplitPane splitpanel) {
        String strWidth;
        String strHeight;
        int Width;
        int Height;

        strWidth = prefs.getByKey(GUI_WIDTH, "1"); // 1450 Apple, 1360 others
        strHeight = prefs.getByKey(GUI_HEIGHT, "1"); //850
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if ("1".equals(strWidth)) {
            if (os == Application.OS_NAMES.APPLE) {
                Width = 1450;
            } else {
                Width = 850;
            }
        } else {
            Width = Integer.parseInt(strWidth);
        }
        if ("1".equals(strHeight)) {
            Height = 850;
        } else {
            Height = Integer.parseInt(strHeight);
        }
        logger.debug("rootPanel.setSize {} x {}", Width, Height);
        rootPanel.setPreferredSize(new Dimension(Width, Height));
    }

    /**
     *
     * @param splitpanel
     */
    public static void SetSplitPaneDivider(JSplitPane splitpanel) {
        String strSplitPanePosition = prefs.getByKey(SPLITPANEL_POSITION, "1");
        if ("1".equals(strSplitPanePosition)) {
            splitpanel.setDividerLocation(0.33);
        } else {
            splitpanel.setDividerLocation(Double.parseDouble(strSplitPanePosition));
        }
    }

    /**
     * This version of the LoadGuiConfig sets the total frame size
     * @param frame
     */
    public static void LoadGuiConfig(JFrame frame) {
        String strWidth;
        String strHeight;
        int Width;
        int Height;

        strWidth = prefs.getByKey(GUI_WIDTH, "1"); // 1450 Apple, 1360 others
        strHeight = prefs.getByKey(GUI_HEIGHT, "1"); //850
        Application.OS_NAMES os = Utils.getCurrentOsName();
        if ("1".equals(strWidth)) {
            if (os == Application.OS_NAMES.APPLE) {
                Width = 1450;
            } else {
                Width = 850;
            }
        } else {
            Width = Integer.parseInt(strWidth);
        }
        if ("1".equals(strHeight)) {
            Height = 850;
        } else {
            Height = Integer.parseInt(strHeight);
        }
        logger.debug("frame.setSize {} x {}", Width, Height);
        frame.setSize(Width, Height);
    }


}
