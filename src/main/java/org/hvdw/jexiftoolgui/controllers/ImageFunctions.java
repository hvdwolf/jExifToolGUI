package org.hvdw.jexiftoolgui.controllers;

//import com.twelvemonkeys.image.AffineTransformOp;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


import static org.hvdw.jexiftoolgui.Application.OS_NAMES.APPLE;
import static org.hvdw.jexiftoolgui.Utils.getCurrentOsName;
import static org.hvdw.jexiftoolgui.Utils.getFileExtension;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class ImageFunctions {
    // Almost 100% copied from Dennis Damico's FastPhotoTagger
    // And he copied it almost 100% from Wyat Olsons original ImageTagger Imagefunctions (2005)
    // And I then extended it with the TwelveMonkeys imageIO libraries

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ImageFunctions.class);

    public static int[] getbasicImageData (File file) {
        // BASIC_IMG_DATA = {"-n", "-S", "-imagewidth", "-imageheight", "-orientation", "-iso", "-fnumber", "-exposuretime", "-focallength", "-focallengthin35mmformat"}
        int[] basicdata = {0, 0, 999, 0, 0, 0, 0, 0};
        long tmpvalue;
        String tmpValue;
        HashMap<String, String> imgBasicData = new HashMap<String, String>();
        //Directory metadata = null;
        String filename = file.getName().replace("\\", "/");

        String exiftool = Utils.platformExiftool();
        List<String> cmdparams = new ArrayList<String>();
        cmdparams.add(exiftool.trim());
        //cmdparams.addAll(Arrays.asList(MyConstants.BASIC_IMG_DATA));
        cmdparams.add("-n");
        cmdparams.add("-S");
        cmdparams.add("-a");
        cmdparams.add(file.getPath());
        int counter = 0;
        String who ="";

        try {
            who = CommandRunner.runCommand(cmdparams);
            logger.trace("res is {}", who);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command", ex);
        }

        if (who.length() > 0) {
            String[] lines = who.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                imgBasicData.put(parts[0].trim(), parts[1].trim());
                    try {
                        if (parts[0].contains("ImageWidth")) {
                            basicdata[0] = Integer.parseInt(parts[1].trim());
                        } else if (parts[0].contains("ImageHeight")) {
                            basicdata[1] = Integer.parseInt(parts[1].trim());
                        } else if (parts[0].contains("Orientation")) {
                            basicdata[2] = Integer.parseInt(parts[1].trim());
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        logger.info("error Integer.parseInt {}", e);
                    }
                //}
                counter++;
            }
            MyVariables.setimgBasicData(imgBasicData);
            logger.trace("imgBasicData {}", imgBasicData);
            HashMap<String, HashMap<String, String> > imagesData = MyVariables.getimagesData();
            imagesData.put(filename, imgBasicData);
            MyVariables.setimagesData(imagesData);
            // Note: 100 images will create 300~600 Kb in the total imagesData hashmap.
        }

        return basicdata;
    }
    

    /*
    / This method is used to mass extract thumbnails from JPG images, either by load folder, load images or "dropped" images.
     */
    public static void extractThumbnails() {
        String exiftool = Utils.platformExiftool();
        List<String> cmdparams = new ArrayList<String>();
        cmdparams.add(exiftool.trim());

        boolean isWindows = Utils.isOsFromMicrosoft();
        File[] files = MyVariables.getLoadedFiles();

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();

        cmdparams.add("-a");
        cmdparams.add("-m");
        cmdparams.add("-b");
        cmdparams.add("-W");
        cmdparams.add(tempWorkDir + File.separator + "%f_%t%-c.%s");
        cmdparams.add("-preview:ThumbnailImage");
        //cmdparams.add("-preview:PreviewImage");

        for (File file : files) {
            if (isWindows) {
                cmdparams.add(file.getPath().replace("\\", "/"));
            } else {
                cmdparams.add(file.getPath());
            }
        }
        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            //logger.info("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command to export thumbnails and previews for selected images");
            //exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }

    }

    /*
     * This method is used to try to get a preview image for those (RAW) images that can't be converted directly to be displayed in the left images column
     * We will try to extract a jpg from the RAW to the tempdir and resize/display that one
     */
    public static String ExportPreviewsThumbnailsForIconDisplay(File file) {
        List<String> cmdparams = new ArrayList<String>();
        String exportResult = "Success";

        cmdparams.add(Utils.platformExiftool());
        boolean isWindows = Utils.isOsFromMicrosoft();

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();

        cmdparams.add("-a");
        cmdparams.add("-m");
        cmdparams.add("-b");
        cmdparams.add("-W");
        cmdparams.add(tempWorkDir + File.separator + "%f_%t%-c.%s");
        cmdparams.add("-preview:ThumbnailImage");
        cmdparams.add("-preview:PhotoshopThumbnail");
        cmdparams.add("-preview:PreviewImage");

        if (isWindows) {
            cmdparams.add(file.getPath().replace("\\", "/"));
        } else {
            cmdparams.add(file.getPath());
        }

        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            //logger.info("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command to export previews for one RAW");
            exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }
        return exportResult;
    }

    /**
     * This method extract previews for PDF export and likewise exports
     * @param file
     * @return
     */
    public static String ExtractPreviews(File file) {
        List<String> cmdparams = new ArrayList<String>();
        String exportResult = "Success";

        cmdparams.add(Utils.platformExiftool());
        boolean isWindows = Utils.isOsFromMicrosoft();

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();

        cmdparams.add("-a");
        cmdparams.add("-m");
        cmdparams.add("-b");
        cmdparams.add("-W");
        cmdparams.add(tempWorkDir + File.separator + "%f_%t%-c.%s");
        cmdparams.add("-preview:JpgFromRaw");
        cmdparams.add("-preview:PreviewImage");

        if (isWindows) {
            cmdparams.add(file.getPath().replace("\\", "/"));
        } else {
            cmdparams.add(file.getPath());
        }

        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            //logger.info("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing command to export previews for one RAW");
            exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }
        return exportResult;
    }



    public static ImageIcon analyzeImageAndCreateIcon (File file) {
        boolean heicextension = false;
        String[] SimpleExtensions = MyConstants.JAVA_SUP_EXTENSIONS;

        boolean bSimpleExtension = false;
        String thumbfilename = "";
        String photoshopThumbfilename = "";
        File thumbfile = null;
        File psthumbfile = null;
        ImageIcon icon = null;
        ImageIcon finalIcon = null;

        Application.OS_NAMES currentOsName = getCurrentOsName();
        String filename = file.getName().replace("\\", "/");
        String filenameExt = getFileExtension(filename);
        if (filenameExt.toLowerCase().equals("heic")) {
            heicextension = true;
        }
        for (String ext : SimpleExtensions) {
            if (filenameExt.toLowerCase().equals(ext)) { // it is either bmp, gif, jp(e)g, png or tif(f)
                bSimpleExtension = true;
                break;
            }
        }

        if ( (heicextension) && currentOsName == APPLE) { // For Apple we deviate
//            if ( (tifextension || heicextension) && currentOsName == APPLE) { // For Apple we deviate
            logger.info("do sipsConvertToJPG for {}", filename);
            String exportResult = ImageFunctions.sipsConvertToJPG(file, "thumb");
            if ("Success".equals(exportResult)) {
                logger.info("back from sipsconvert: result {}", exportResult);
                //Hoping we have a thumbnail
                thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + ".jpg";
                thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                if (thumbfile.exists()) {
                    // Create icon of this thumbnail (thumbnail is 90% 160x120 already, but resize it anyway
                    logger.debug("create thumb nr1");
                    //icon = ImageFunctions.createIcon(thumbfile);
                    icon = ImageFunctions.createIcon(thumbfile);
                    if (icon != null) {
                        // display our created icon from the thumbnail
                        return icon;
                    }
                }
            }
            //reset our heic flag
            heicextension = false;
        } else if (bSimpleExtension) {
            thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
            photoshopThumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
            thumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
            psthumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + photoshopThumbfilename);
            if ((!thumbfile.exists() || !psthumbfile.exists()) && (filenameExt.toLowerCase().equals("jpg")) || (filenameExt.toLowerCase().equals("jpeg") || filenameExt.toLowerCase().equals("tif")) || (filenameExt.toLowerCase().equals("tiff")) ) {
                String exportResult = ImageFunctions.ExportPreviewsThumbnailsForIconDisplay(file);
                /*if (thumbfile.exists()) {
                    icon = ImageFunctions.createIcon(thumbfile);
                } else {
                    icon = ImageFunctions.createIcon(file);
                }*/
            }

                /*if ( (filenameExt.toLowerCase().equals("jpg")) || (filenameExt.toLowerCase().equals("jpeg")) ) {
                    String exportResult = ImageFunctions.ExportPreviewsThumbnailsForIconDisplay(file, "RAW");
                    thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
                    thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    icon = ImageFunctions.createIcon(file);
                } else {
                    icon = ImageFunctions.createIcon(file);
                } */
            icon = ImageFunctions.createIcon(file);
            return icon;
        } else { //We have a RAW image extension or tiff or something else like audio/video
            // Export previews for current (RAW) image to tempWorkfolder
            String exportResult = ImageFunctions.ExportPreviewsThumbnailsForIconDisplay(file);
            if ("Success".equals(exportResult)) {
                //Hoping we have a thumbnail
                thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
                thumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                logger.trace("thumb nr1:"  + MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                if (thumbfile.exists()) {
                    // Create icon of this thumbnail (thumbnail is 90% 160x120 already, but resize it anyway
                    logger.trace("create thumb nr1");
                    icon = ImageFunctions.createIcon(file);
                    //icon = ImageFunctions.createIcon(thumbfile);
                    if (icon != null) {
                        // display our created icon from the thumbnail
                        return icon;
                    }
                } else { //thumbnail image probably doesn't exist, move to 2nd option
                    thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
                    thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    if (thumbfile.exists()) {
                        // Create icon of this Preview
                        logger.trace("create thumb nr2");
                        icon = ImageFunctions.createIcon(file);
                        //icon = ImageFunctions.createIcon(thumbfile);
                        if (icon != null) {
                            // display our created icon from the preview
                            return icon;
                        }
                    } else { // So thumbnail and previewImage don't exist. Try 3rd option
                        thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_JpgFromRaw.jpg";
                        thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                        if (thumbfile.exists()) {
                            // Create icon of this Preview
                            icon = ImageFunctions.createIcon(file);
                            //icon = ImageFunctions.createIcon(thumbfile);
                            if (icon != null) {
                                // display our created icon from the preview
                                return icon;
                            }
                        } else { // So thumbnail and previewImage don't exist. Try 4th option for photoshop thumbnail
                            thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
                            thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                            if (thumbfile.exists()) {
                                // Create icon of this Preview
                                        icon = ImageFunctions.createIcon(file);
                                //icon = ImageFunctions.createIcon(thumbfile);
                                if (icon != null) {
                                    // display our created icon from the preview
                                    return icon;
                                }
                        } else {
                                // Load he cantdisplay.png from our resources
                                try {
                                    BufferedImage img = ImageIO.read(mainScreen.class.getResource("/cantdisplay.png"));
                                    icon = new ImageIcon(img);
                                } catch (IOException e) {
                                    logger.error("Error loading image", e);
                                    icon = null;
                                }
                                ImageFunctions.getbasicImageData(file);
                                if (icon != null) {
                                    // display our created icon from the preview
                                    return icon;
                                }
                            } // end of 3nd option creation ("else if") and 4rd option creation (else)
                        } // end of 4th option creation ("else if") and cantdisplaypng option (else)
                    } // end of 2nd option creation ("else if") and 3rd option creation (else)
                } // end of 1st option creation ("else if") and 2nd option creation (else)

            } else { // Our "String exportResult = ExportPreviewsThumbnailsForIconDisplay(file);"  completely failed due to some weird RAW format
                // Load he cantdisplay.png from our resources
                try {
                    BufferedImage img = ImageIO.read(mainScreen.class.getResource("/cantdisplay.png"));
                    icon = new ImageIcon(img);
                } catch (IOException e){
                    logger.error("Error loading image", e);
                    icon = null;
                }
                ImageFunctions.getbasicImageData(file);
                if (icon != null) {
                    // display our created icon from the preview
                    return icon;
                }

            }

        }

        return icon;
    }

    /*
     * Create the icon after having determined what kind of image we have
     * This is only necessary if we do not have a thumbnail, previewimage, etc from our "big" image
     */
    public static ImageIcon createIcon(File file) {
        ImageIcon icon = null;
        int[] basicdata = {0, 0, 999, 0, 0, 0, 0, 0};;
        boolean bde = false;
        String thumbfilename = "";
        String photoshopThumbfilename = "";
        File thumbfile = null;
        File psthumbfile = null;
        String filename = "";
        BufferedImage img = null;
        BufferedImage resizedImg = null;

        filename = file.getName().replace("\\", "/");
        logger.debug("Now working on image: " +filename);
        String filenameExt = getFileExtension(filename);
        try {
            try {
                // We use exiftool to get width, height and orientation from the ORIGINAL image
                // (as it is not always available in the thumbnail or preview)
                basicdata = getbasicImageData(file);
                logger.debug("Width {} Height {} Orientation {}", String.valueOf(basicdata[0]), String.valueOf(basicdata[1]), String.valueOf(basicdata[2]));
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                bde = true;

            }
            logger.trace("after getbasicdata");
            if ((bde) || (basicdata[2] == 999)) {
                // We had some error. Mostly this is the orientation
                basicdata[2]= 1;
            }
            // Check whether we have a thumbnail
            thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
            photoshopThumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
            thumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
            psthumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + photoshopThumbfilename);
            if (thumbfile.exists()) {
                logger.debug("precreated thumbnail found: {}", thumbfile.toString());
                img = ImageIO.read(new File(thumbfile.getPath().replace("\\", "/")));
            } else if (psthumbfile.exists()) {
                logger.debug("precreated photoshop thumbnail found: {}", psthumbfile.toString());
                img = ImageIO.read(new File(psthumbfile.getPath().replace("\\", "/")));
            } else {
                logger.debug("precreated thumbnail NOT found: {}", thumbfile.toString());
                img = ImageIO.read(new File(file.getPath().replace("\\", "/")));
            }
            if (basicdata[0] > 160) {
                resizedImg = ImageFunctions.scaleImageToContainer(img, 160, 160);
                logger.trace("after scaleImageToContainer");
            } else {
                // In some circumstances we even have images < 160 width
                resizedImg = img;
            }
            if ( basicdata[2] > 1 ) { //We use 999 if we can' t find an orientation
                resizedImg = ImageFunctions.rotate(resizedImg, basicdata[2]);
            }


            logger.trace("after rotate");

            icon = new ImageIcon(resizedImg);
            return icon;
        } catch (IIOException iex) {
            icon = null;
        } catch (IOException ex) {
            logger.error("Error loading image", ex);
            icon = null;
        }
        return icon;
    }

    /*
    / On Mac we let sips do the conversion of tif and heic images to previews
    / like "sips -s format JPEG -Z 160 test.heic --out test.jpg"
     */
    public static String sipsConvertToJPG(File file, String size) {
        //ImageIcon icon = null;
        //Runtime runtime = Runtime.getRuntime();
        List<String> cmdparams = new ArrayList<String>();
        String exportResult = "Success";

        cmdparams.add("/usr/bin/sips");
        cmdparams.add("-s");
        cmdparams.add("format");
        cmdparams.add("jpeg");
        if (size.toLowerCase().equals("thumb")) {
            cmdparams.add("-Z");
            cmdparams.add("160");
        } else if (size.toLowerCase().equals("pdfexport")) {
            cmdparams.add("-Z");
            cmdparams.add("1600");
        }
        // Get the file
        cmdparams.add(file.getPath().replace(" ", "\\ "));
        //cmdparams.add("\"" + file.getPath() + "\"");
        cmdparams.add("--out");

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();
        // Get the file name without extension
        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        cmdparams.add(tempWorkDir + File.separator + fileName + ".jpg");
        logger.info("final sips command: " + cmdparams.toString());

        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            logger.trace("cmd result from export previews for single RAW" + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.debug("Error executing sipd command to convert to jpg");
        }
        return exportResult;
    }


    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param src - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    public static BufferedImage scaleImage(BufferedImage src, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Scale an image.
     * @param img the image to be scaled.
     * @param conWidth the maximum width after scaling.
     * @param conHeight the maximum height after scaling.
     * @return the scaled image.
     */
    public static BufferedImage scaleImageToContainer(BufferedImage img, int conWidth, int conHeight) {
        if (img == null) return null;

        // Original image size:
        int width = img.getWidth();
        int height = img.getHeight();

        // If the image is already the right size then there is nothing to do.
        if ((width == conWidth && height <= conHeight) ||
                (height == conHeight && width <= conWidth)) {
            return img;
        }

        // Scaled image size:
        int scaledWidth = conWidth;
        int scaledHeight = conHeight;

        float cAspect = ((float) conWidth) / conHeight;
        float fileAspect = ((float) width) / height;

        if (fileAspect >= cAspect) {
            scaledHeight = (int) (scaledWidth / fileAspect);
        }
        else {
            scaledWidth = (int) (scaledHeight * fileAspect);
        }

        // Prevent scaling to 0 size.
        if (scaledWidth <= 0 || scaledHeight <= 0) {
            scaledWidth = 1;
            scaledHeight = 1;
        }

        Image newScaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
        // Buffered image for drawing scaled image:
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        // OLD BufferedImage scaledImg = new BufferedImage(scaledWidth, scaledHeight, type);
        BufferedImage scaledImg = new BufferedImage(newScaledImg.getWidth(null), newScaledImg.getHeight(null), type);
        Graphics2D g2 = scaledImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw the scaled image.
        g2.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);
        g2.dispose();

        return scaledImg;
    }

    /**
     * Rotate an image.
     * @param image The image to rotate.
     * @param rotation The rotation constant.
     * @return The rotated image.
     */
    public static BufferedImage rotate(BufferedImage image, int rotation) {
        // http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/EXIF.html tag 0x0112	Orientation
        // "exiftool -exif:orientation" gives for example Rotate 90 CW
        // "exiftool -n -exif:orientation" gives for example 6
        // rotation values:
        // 1 = Horizontal (normal)
        // 2 = Mirror horizontal
        // 3 = Rotate 180
        // 4 = Mirror vertical
        // 5 = Mirror horizontal and rotate 270 CW
        // 6 = Rotate 90 CW
        // 7 = Mirror horizontal and rotate 90 CW
        // 8 = Rotate 270 CW

        AffineTransform tx = null;
        AffineTransformOp op = null;
        if (image == null) return null;

        switch (rotation) {
            default:
            case 1:
                // No rotation
                break;

            case 2:
                // Mirror horizontal
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-image.getWidth(null), 0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;

            case 3:
                // Rotate 180
                // Relocate the center of the image to the origin.
                // Rotate about the origin.  Then move image back.
                // (This avoids black bars on rotated images.)
                tx = new AffineTransform();
                tx.translate(image.getWidth() / 2.0, image.getHeight() / 2.0);
                tx.rotate(Math.toRadians(180));
                tx.translate( - image.getWidth() / 2.0, - image.getHeight() / 2.0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;

            case 4:
                // Mirror vertical
                tx = AffineTransform.getScaleInstance(1, -1);
                tx.translate(0, -image.getHeight(null));
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;

            case 5:
                // Mirror horizontal and rotate 270 CW
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-image.getWidth(null), 0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                // Fall thru to case 8.

            case 8:
                // Rotate 270 CW
                tx = new AffineTransform();
                tx.translate(image.getHeight() / 2.0, image.getWidth() / 2.0);
                tx.rotate(Math.toRadians(270));
                tx.translate( - image.getWidth() / 2.0, - image.getHeight() / 2.0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;

            case 7:
                // Mirror horizontal and rotate 90 CW
                tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-image.getWidth(null), 0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                // Fall through to case 6.

            case 6:
                // Rotate 90 CW
                tx = new AffineTransform();
                tx.translate(image.getHeight() / 2.0, image.getWidth() / 2.0);
                tx.rotate(Math.toRadians(90));
                tx.translate( - image.getWidth() / 2.0, - image.getHeight() / 2.0);
                op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;
        }
        return image;
    }

    /**
     * Adjust an image aspect ratio depending on the image rotation.
     * @param oldAspectRatio The original aspect ratio.
     * @param rotation The rotation constant.
     * @return The adjusted aspect ratio.
     */
    public static float fixAspectRatio(float oldAspectRatio, int rotation) {
        switch (rotation) {
            default:
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return oldAspectRatio;

            case 5:
            case 6:
            case 7:
            case 8:
                return 1 / oldAspectRatio;
        }
    }

}
