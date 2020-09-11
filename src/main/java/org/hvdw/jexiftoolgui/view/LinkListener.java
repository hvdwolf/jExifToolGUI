package org.hvdw.jexiftoolgui.view;

import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class LinkListener implements HyperlinkListener {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(LinkListener.class);

    /*
     * Opens the default browser of the Operating System
     * and displays the specified URL
     * This one is a copy of Utils.openBrowser but that could not be used due to public/static
     */
    static void openBrowser(String webUrl) {

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(webUrl));
                return;
            }
            Application.OS_NAMES os = Utils.getCurrentOsName();

            Runtime runtime = Runtime.getRuntime();
            switch (os) {
                case APPLE:
                    runtime.exec("open " + webUrl);
                    return;
                case LINUX:
                    runtime.exec("xdg-open " + webUrl);
                    return;
                case MICROSOFT:
                    runtime.exec("explorer " + webUrl);
                    return;
            }
        }
        catch (IOException | IllegalArgumentException e) {
            logger.error("Could not open browser", e);
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                openBrowser(e.getURL().toString());
            }
            catch (Exception ioe) {
                logger.info("Hyperlink error: {}", e.toString());
            }
        }
    }

}
