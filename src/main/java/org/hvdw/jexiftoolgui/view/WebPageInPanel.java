package org.hvdw.jexiftoolgui.view;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;

import java.awt.*;
import java.net.URL;
import java.util.Locale;
import javax.swing.*;
import javax.swing.event.HyperlinkListener;

public class WebPageInPanel extends JFrame {
    private final HyperlinkListener pageListener = new LinkListener();

    public void WebPageInPanel(JPanel rootPanel, String url, int panelWidth, int panelHeight) {
        JFrame webFrame = new JFrame();
        Locale currentLocale = new Locale.Builder().setLocale(MyVariables.getCurrentLocale()).build();
        webFrame.applyComponentOrientation(ComponentOrientation.getOrientation(currentLocale));
        this.setIconImage(Utils.getFrameIcon());

        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JScrollPane sp=new JScrollPane();
        //sp.setPreferredSize(new Dimension(panelWidth, panelHeight));
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(pageListener);
        editorPane.setPreferredSize(new Dimension(panelWidth, panelHeight));
        sp.setViewportView(editorPane);
        add(sp);
        try {
            editorPane.setPage(new URL(url));
        } catch (Exception ex) {ex.printStackTrace();}

        webFrame.add(editorPane);
        // Position to screen center.
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        webFrame.setLocation((int) (screen_size.getWidth() - getWidth()) / 2,
                (int) (screen_size.getHeight() - getHeight()) / 2);
        webFrame.pack();
        webFrame.setVisible(true);
    }
}
