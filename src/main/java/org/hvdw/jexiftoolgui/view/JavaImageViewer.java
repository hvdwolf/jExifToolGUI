package org.hvdw.jexiftoolgui.view;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JavaImageViewer {
    private final static Logger logger = LoggerFactory.getLogger(DatabasePanel.class);
    private ImagePanel imageViewPane;

    private void makeFrameFullSize(JFrame aFrame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        aFrame.setSize(screenSize.width, screenSize.height);
    }

    private class ImagePanel extends JPanel {
    }
    public void ViewImageInFullscreenFrame () {
        BufferedImage img = null;
        int panelWidth = 0;
        int panelHeight = 0;

        String ImgPath = MyVariables.getSelectedImagePath();
        File image = new File(ImgPath);
        String fileName = image.getName();

        JFrame frame = new JFrame(fileName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageViewPane = new ImagePanel();
        frame.setContentPane(imageViewPane);
        frame.setIconImage(Utils.getFrameIcon());

        try {
            img = ImageIO.read(image);
        } catch (IOException ioe) {
            logger.info("erorr loading image {}", ioe.toString());
        }
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrwidth = screenSize.width;
        int scrheight = screenSize.height;

        if (imgWidth > scrwidth) {
            panelWidth = scrwidth;
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            panelWidth = imgWidth;
        }
        if (imgHeight > scrheight) {
            panelHeight = scrheight;
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            panelHeight = imgHeight;
        }

        BufferedImage resizedImg = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Faster
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);   //more accurate
        g2.drawImage(img, 0, 0, panelWidth, panelHeight, null);
        g2.dispose();

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.setBackground(Color.white);
        thePanel.setOpaque(true);
        thePanel.add(new JLabel(new ImageIcon(resizedImg)), BorderLayout.NORTH);
        thePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        frame.add(thePanel);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);

    }
}
