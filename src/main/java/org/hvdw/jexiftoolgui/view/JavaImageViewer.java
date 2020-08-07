package org.hvdw.jexiftoolgui.view;

import com.twelvemonkeys.imageio.metadata.Directory;
import com.twelvemonkeys.imageio.metadata.Entry;
import com.twelvemonkeys.imageio.metadata.tiff.TIFFReader;
import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.controllers.ImageFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hvdw.jexiftoolgui.Utils.getCurrentOsName;

public class JavaImageViewer {
    private final static Logger logger = LoggerFactory.getLogger(DatabasePanel.class);
    private ImagePanel imageViewPane;

    private void makeFrameFullSize(JFrame aFrame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        aFrame.setSize(screenSize.width, screenSize.height);
    }

    private static BufferedImage ImageForViewing (BufferedImage img, int scrWidth, int scrHeight) {
        if (img == null) return null;

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        float imgratio = img.getWidth() / img.getHeight();
        float scrratio = scrWidth / scrHeight;

        if (imgratio >= scrratio) {
            scrHeight = (int) (scrWidth / imgratio);
        } else {
            scrWidth = (int) (scrHeight * imgratio);
        }
        // Make sure we do not get divided by zero errors
        if (scrWidth <= 0 || scrHeight <= 0) {
            scrWidth = 1;
            scrHeight = 1;
        }

        BufferedImage resizedImg = new BufferedImage(scrWidth, scrHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Faster
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);   //more accurate
        g2.drawImage(img, 0, 0, scrWidth, scrHeight, null);
        g2.dispose();

        return resizedImg;
    }

    private class ImagePanel extends JPanel {
    }
    public void ViewImageInFullscreenFrame () {
        BufferedImage img = null;
        BufferedImage resizedImg = null;
        int panelWidth = 0;
        int panelHeight = 0;
        String fileName = "";
        File image = null;

        String ImgPath = MyVariables.getSelectedImagePath();
        //File Imgfile = new File (ImgPath);
        //fileName = Imgfile.getName();
        image = new File(ImgPath);
        fileName = image.getName();

        Application.OS_NAMES currentOsName = getCurrentOsName();
        String filenameExt = Utils.getFileExtension(MyVariables.getSelectedImagePath());
        if (filenameExt.toLowerCase().equals("heic") || filenameExt.toLowerCase().equals("heif")) {
            //We should have a converted jpg in the tempwork folder
            String tmpfilename = fileName.substring(0, fileName.lastIndexOf('.')) + ".jpg";
            image = new File(MyVariables.gettmpWorkFolder() + File.separator + tmpfilename);
            logger.info("In ViewImageInFullscreenFrame trying a heic");
        }


        try {
            img = ImageIO.read(image);
        } catch (IOException ioe) {
            logger.info("erorr loading image {}", ioe.toString());
        }
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        //int imgoritnetation = img.getProperty("orientation")
        float imgratio = img.getWidth() / img.getHeight();

        /// Test
        /*try {
            Directory metadata = new TIFFReader().read(new FileImageInputStream(image));
            for (Entry entry: metadata) {
                logger.info(String.valueOf(entry));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /// End test
        JFrame frame = new JFrame(fileName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageViewPane = new ImagePanel();
        frame.setContentPane(imageViewPane);
        frame.setIconImage(Utils.getFrameIcon());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrwidth = screenSize.width;
        int scrheight = screenSize.height;
        float scrratio = scrwidth / scrheight;
        int[] basicdata = {0, 0, 0};
        boolean bde = false;

        /*if (imgratio >= scrratio) {
            scrheight = (int) (scrwidth / imgratio);
        } else {
            scrwidth = (int) (scrheight * imgratio);
        } */

        if ( (imgWidth <= scrwidth)  && (imgHeight <= scrheight)) {
            // no rescaling necessary
            resizedImg = img;
            panelWidth = imgWidth;
            panelHeight = imgHeight;
        } else {
            /*if (imgWidth > imgHeight) { //landscape mode
                if (imgWidth > scrwidth) {
                    panelWidth = scrwidth;
                    panelHeight = Math.round( (float) scrwidth / imgratio);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    //if (imgHeight > scrheight) { // so imgwidth < screenwidth, but imgheight > screenheight
                    //}
                    panelWidth = imgWidth;
                    panelHeight = imgHeight;
                }

            } else { //portrait mode
                if (imgHeight > scrheight) {
                    panelHeight = scrheight;
                    panelWidth = Math.round((float) scrheight / (float) imgHeight * imgWidth);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    panelWidth = imgWidth;
                    panelHeight = imgHeight;
                }

            } */

/*            if (imgHeight > scrheight) {
                panelHeight = scrheight;
                panelWidth = Math.round(scrheight * imgratio);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                panelHeight = imgHeight;
                //panelWidth = imgWidth;
            } */

            /*resizedImg = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Faster
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);   //more accurate
            g2.drawImage(img, 0, 0, panelWidth, panelHeight, null);
            g2.dispose(); */

            try {
                basicdata = ImageFunctions.getbasicImageData(image);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                bde = true;

            }
            //logger.info("after getbasicdata");
            if (bde) {
                // We had some error. Mostly this is the orientation
                basicdata[2]= 1;
            }
            try {
                img = ImageIO.read(new File(image.getPath().replace("\\", "/")));
                if (basicdata[2] > 1) {
                    resizedImg = ImageFunctions.rotate(img, basicdata[2]);
                    resizedImg = ImageFunctions.scaleImageToContainer(resizedImg, scrwidth, scrheight);
                } else { // No rotation necessary
                    resizedImg = ImageFunctions.scaleImageToContainer(img, scrwidth, scrheight);
                }
                //logger.info("after scaleImageToContainer");


                panelWidth = resizedImg.getWidth();
                panelHeight = resizedImg.getHeight();
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } catch (IOException iex) {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                panelWidth = imgWidth;
                panelHeight = imgHeight;
            }
            

        }
        logger.info("Image: {} ; Width {} ; Height {}", fileName, panelWidth, panelHeight);

        JPanel thePanel = new JPanel(new BorderLayout());
        thePanel.setBackground(Color.white);
        thePanel.setOpaque(true);
        thePanel.add(new JLabel(new ImageIcon(resizedImg)), BorderLayout.NORTH);
        //thePanel.add(new JLabel(new ImageIcon(ImageForViewing(img, scrwidth, scrheight))), BorderLayout.NORTH);
        thePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        frame.add(thePanel);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);

    }
}
