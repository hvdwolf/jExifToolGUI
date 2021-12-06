package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.*;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
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
import static org.hvdw.jexiftoolgui.Utils.*;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class ImageFunctions {
    // A big deal was copied from Dennis Damico's FastPhotoTagger
    // And he copied it almost 100% from Wyat Olsons original ImageTagger Imagefunctions (2005)
    // Extended with additional functionality

    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ImageFunctions.class);

    /*
    / This gets all image data using exiftool, but only returns the basic image data
    / The total tag data is put into a hashmap via a getter/setter
     */
    public static int[] getImageMetaData (File file) {
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
            logger.debug("res is {}", who);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command", ex.toString());
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
    / This only gets width, height and rotation for an image. Although exiftool is slower than java for jpeg, it is faster for other file formats
    * and gives better results for orientation. See also below the getImageDimension method: same performance
    */
    public static int[] getWidthHeightOrientation (File file) {
        int[] basicdata = {0, 0, 999, 0, 0, 0, 0, 0};

        /*
        // Standard java imageIO
        //BufferedImage bimg = ImageIO.read(new File(filename));
        //int width          = bimg.getWidth();
        //int height         = bimg.getHeight();

        //Or
        public static int getOrientation(File imageFile){
            int result = 0;
            ImageIcon image = new ImageIcon(imageFile.getPath());
            if (image.getIconWidth() > image.getIconHeight()) {
                result = 0;
            } else {
                result = 1;
            }
            image = null;
            return result;
       }
       */

        String exiftool = Utils.platformExiftool();
        List<String> cmdparams = new ArrayList<String>();
        cmdparams.add(exiftool.trim());
        cmdparams.add("-n");
        cmdparams.add("-S");
        cmdparams.add("-imagewidth");
        cmdparams.add("-imageheight");
        cmdparams.add("-orientation");

        cmdparams.add(file.getPath());
        String w_h_o ="";

        try {
            w_h_o = CommandRunner.runCommand(cmdparams);
            logger.debug("res is {}", w_h_o);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command", ex.toString());
        }

        if (w_h_o.length() > 0) {
            String[] lines = w_h_o.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
            for (String line : lines) {
                String[] parts = line.split(":", 2);
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
            }
        }
        return basicdata;
    }

    /**
     * Gets image dimensions for given file
     * @param imgFile image file
     * @return dimensions of image
     * @throws IOException if the file is not a known image
     *
     *  This is one of the fastest, if not the fastest, java method to get this data
     *  for png files is is between 50~100ms
     *  but average operation is around 700 ms for jpegs
     *  while exiftool does it in ~235ms
     */
    public static Dimension getImageDimension(File imgFile) throws IOException {
        int pos = imgFile.getName().lastIndexOf(".");
        if (pos == -1)
            throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
        String suffix = imgFile.getName().substring(pos + 1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        while(iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = new FileImageInputStream(imgFile);
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                return new Dimension(width, height);
            } catch (IOException e) {
                logger.error("Error reading: " + imgFile.getAbsolutePath(), e);
            } finally {
                reader.dispose();
            }
        }

        throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
    }

    /*
/ This one is used to get all metadata in the background for further use
 */
    public static void getImageData (JLabel[] mainScreenLabels, JProgressBar progressBar, JButton buttonSearchMetadata) {

        HashMap<String, String> imgBasicData = new HashMap<String, String>();

        String exiftool = Utils.platformExiftool();
        List<String> basiccmdparams = new ArrayList<String>();
        basiccmdparams.add(exiftool.trim());
        basiccmdparams.add("-n");
        basiccmdparams.add("-S");
        basiccmdparams.add("-a");

        boolean files_null = false;
        File[] files = MyVariables.getLoadedFiles();

        SwingWorker sw = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                List<String> cmdparams = new ArrayList<String>();
                cmdparams.addAll(basiccmdparams);
                for (File file : files) {
                    cmdparams.add(file.getPath());
                }
                logger.debug("Show getBulkImageData command string: {}", String.join(" ", cmdparams));
                String imgTags = "";
                try {
                    imgTags = CommandRunner.runCommand(cmdparams);
                    logger.debug("complete images metadata is {}", imgTags);
                } catch (IOException | InterruptedException ex) {
                    logger.error("Error executing command", ex.toString());
                }
                int counter = 0;
                if (imgTags.length() > 0) {
                    String filename = "";
                    String prev_filename = "";
                    String[] lines = imgTags.split(SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                    boolean initialized = true;

                    HashMap<String, String> imgBasicData = new HashMap<String, String>();
                    MyVariables.setimgBasicData(imgBasicData);
                    HashMap<String, HashMap<String, String>> imagesData = new HashMap<String, HashMap<String, String>>();
                    MyVariables.setimagesData(imagesData);
                    for (String line : lines) {
                        if (line.startsWith("======== ")) {
                            String[] arrfilename = line.split(" ",2);
                            //logger.info("filename : {}", filename);
                            filename = arrfilename[1].trim();
                            //logger.info("\n\n\nfile no. : {} filename: {}\n\n", String.valueOf(counter), filename);
                            if (counter == 0) {
                                prev_filename = filename;
                                //imgBasicData.clear();
                                imgBasicData = new HashMap<String, String>();
                                //imagesData.clear();
                            } else {
                                MyVariables.setimgBasicData(imgBasicData);
                                logger.debug("\n\n prev_filename {} imgBasicData {}\n\n", prev_filename, imgBasicData.toString());
                                //logger.debug("\n\n prev_filename {}\n\n", prev_filename);
                                imagesData = MyVariables.getimagesData();
                                imagesData.put(prev_filename, imgBasicData);
                                MyVariables.setimagesData(imagesData);
                                prev_filename = filename;
                                //imgBasicData.clear();
                                imgBasicData = new HashMap<String, String>();
                            }
                            counter++;
                        } else {
                            if (line.contains(":")) {
                                String[] parts = line.split(":", 2);
                                imgBasicData.put(parts[0].trim(), parts[1].trim());
                            } else {
                                imgBasicData.put("Error in tag", "Error in value");
                                logger.info("Error in tag or value for file {}", filename);
                            }
                        }
                    }
                    // And the last image data
                    logger.debug("\n\n prev_filename {}\n\n", prev_filename);
                    imagesData = MyVariables.getimagesData();
                    imagesData.put(prev_filename, imgBasicData);
                    MyVariables.setimagesData(imagesData);
                    //logger.info("imagesData\n {}", imagesData.toString());
                    imgBasicData.clear();
                    MyVariables.setimgBasicData(imgBasicData);
                }
                //logger.info("MyVariables.getimagesData().toString() \n{}",MyVariables.getimagesData().toString());
                //logger.debug("\n\n\n\ncounter : {}", String.valueOf(counter));

                return null;
            }
            @Override
            public void done() {
                logger.debug("Finished reading all the metadata in the background");
                progressBar.setVisible(false);
                mainScreenLabels[0].setText(ResourceBundle.getBundle("translations/program_strings").getString("pt.finishedreadingmetadabackground"));
                buttonSearchMetadata.setEnabled(true);
            }
        };
        sw.execute();

    }


    /*
    / This method is used to mass extract thumbnails from images, either by load folder, load images or "dropped" images.
     */
    public static void extractThumbnails() {
        String exiftool = Utils.platformExiftool();
        List<String> cmdparams = new ArrayList<String>();
        cmdparams.add(exiftool.trim());
        String filename = "";

        boolean isWindows = Utils.isOsFromMicrosoft();
        File[] files = MyVariables.getLoadedFiles();
        List<File> createthumbs = new ArrayList<File>();

        // Get the temporary directory
        String tempWorkDir = MyVariables.gettmpWorkFolder();
        String userHome = SystemPropertyFacade.getPropertyByKey(USER_HOME);
        String strjexiftoolguicachefolder = userHome + File.separator + MyConstants.MY_DATA_FOLDER + File.separator + "cache";

        cmdparams.add("-a");
        cmdparams.add("-m");
        cmdparams.add("-b");
        cmdparams.add("-W");
        cmdparams.add(tempWorkDir + File.separator + "%f_%t%-c.%s");
        cmdparams.add("-preview:all");

        for (File file : files) {
            // First check for existing thumbnails
            filename = file.getName().replace("\\", "/");
            String thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
            String photoshopThumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
            File thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
            File psthumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + photoshopThumbfilename);
            if (!thumbfile.exists()) {  // If the thumbfile doesn't exist
                if (!psthumbfile.exists()) {  // and the photoshop thumbfile doesn't exist
                    // then we need to try to create one of them
                    if (isWindows) {
                        cmdparams.add(file.getPath().replace("\\", "/"));
                    } else {
                        cmdparams.add(file.getPath());
                    }
                }
            }

        }
        try {
            String cmdResult = CommandRunner.runCommand(cmdparams);
            logger.debug("cmd result after export previews " + cmdResult);
        } catch (IOException | InterruptedException ex) {
            logger.error("Error executing command to export thumbnails and previews for selected images");
            //exportResult = (" " + ResourceBundle.getBundle("translations/program_strings").getString("ept.exporterror"));
        }

        StandardFileIO.copyThumbsToCache();
    }

    /*
     * This method is used to try to get a preview image for those (RAW) images that can't be converted directly to be displayed in the left images column
     * We will try to extract a jpg from the RAW to the tempdir and resize/display that one
     */
    public static String ExportPreviewsThumbnailsForIconDisplay(File file, boolean bSimpleExtension, String filenameExt) {
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
        cmdparams.add("-preview:all");

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

    public static ImageIcon useCachedOrCreateIcon (File file) {
        ImageIcon icon = null;

        String filename = file.getName().replace("\\", "/");
        String thumbfilename = MyVariables.getjexiftoolguiCacheFolder() + File.separator + filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
        String photoshopThumbfilename = MyVariables.getjexiftoolguiCacheFolder() + File.separator + filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
        File thumbfile = new File(thumbfilename);
        File psthumbfile = new File(photoshopThumbfilename);
        if (thumbfile.exists()) {
            icon = new ImageIcon(thumbfilename);
        } else if (psthumbfile.exists()) {
            icon = new ImageIcon(photoshopThumbfilename);
        } else {
            icon = ImageFunctions.analyzeImageAndCreateIcon(file);
        }
        return icon;
    }

    /**
     * This method AnalyzeImageAndCreateIcon will check if a thumbnail already exists, created by the extract thumbnails method
     * If not, like in the case for images without any preview, it will resize the image to thumbnail
     * This function is also called by useCachedOrCreateThumbNail
     * @param file
     * @return
     */
    public static ImageIcon analyzeImageAndCreateIcon (File file) {
        boolean heicextension = false;
        String[] SimpleExtensions = MyConstants.JAVA_SUP_EXTENSIONS;

        boolean bSimpleExtension = false;
        ImageIcon icon = null;
        ImageIcon finalIcon = null;

        Application.OS_NAMES currentOsName = getCurrentOsName();
        String filename = file.getName().replace("\\", "/");
        String filenameExt = getFileExtension(filename);

        // Define our possible previews
        String thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_ThumbnailImage.jpg";
        String photoshopThumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PhotoshopThumbnail.jpg";
        String previewThumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
        File thumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
        File psthumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + photoshopThumbfilename);
        File prevthumbfile = new File (MyVariables.gettmpWorkFolder() + File.separator + previewThumbfilename);
        File cachedthumbfile = new File (MyVariables.getjexiftoolguiCacheFolder() + File.separator + thumbfilename);
        File cachedpsthumbfile = new File (MyVariables.getjexiftoolguiCacheFolder() + File.separator + photoshopThumbfilename);

        if ( (filenameExt.toLowerCase().equals("heic")) || ((filenameExt.toLowerCase().equals("heif"))) ) {
            heicextension = true;
        }
        for (String ext : SimpleExtensions) {
            if (filenameExt.toLowerCase().equals(ext)) { // it is either bmp, gif, jp(e)g, png or tif(f)
                bSimpleExtension = true;
                break;
            }
        }

        if ( (heicextension) && currentOsName == APPLE) { // For Apple we deviate
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
        } else if ( (filenameExt.toLowerCase().equals("jpg")) || (filenameExt.toLowerCase().equals("jpeg") || filenameExt.toLowerCase().equals("tif")) || (filenameExt.toLowerCase().equals("tiff")) ) {
            if (cachedthumbfile.exists()) {
                icon = ImageFunctions.createIcon(file);
                return icon;
            } else if (cachedpsthumbfile.exists()) {
                icon = ImageFunctions.createIcon(cachedpsthumbfile);
                return icon;
            } else {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(file);
                } catch (IOException e) {
                    logger.error("error reading buffered image to scale it to icon {}", e.toString());
                    e.printStackTrace();
                }
                BufferedImage resizedImg = ImageFunctions.scaleImageToContainer(img, 160, 160);
                icon = new ImageIcon(resizedImg);
                // Save our created icon
                if ( (filenameExt.toLowerCase().equals("jpg")) || (filenameExt.toLowerCase().equals("jpeg")) ) {
                    //BufferedImage thumbImg = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
                    BufferedImage thumbImg = (BufferedImage) icon.getImage();
                    StandardFileIO.saveIconToCache(filename, thumbImg);
                } else { //tiff
                    //BufferedImage thumbImg = new BufferedImage(icon);
                    BufferedImage thumbImg = new BufferedImage(resizedImg.getWidth(), resizedImg.getHeight(), BufferedImage.OPAQUE);
                    thumbImg.createGraphics().drawImage(resizedImg, 0, 0, Color.WHITE, null);
                    StandardFileIO.saveIconToCache(filename, thumbImg);
                }
                return icon;
            }
        } else if ( (filenameExt.toLowerCase().equals("bmp")) || (filenameExt.toLowerCase().equals("png")) ) {
            if (cachedthumbfile.exists()) {
                icon = ImageFunctions.createIcon(file);
            } else {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(file);
                } catch (IOException e) {
                    logger.error("error reading buffered image to scale it to icon {}", e.toString());
                    e.printStackTrace();
                }
                BufferedImage resizedImg = ImageFunctions.scaleImageToContainer(img, 160, 160);
                icon = new ImageIcon(resizedImg);
                BufferedImage thumbImg = (BufferedImage) icon.getImage();
                // Save our created icon
                StandardFileIO.saveIconToCache(filename, thumbImg);
                //logger.info("Saving file {} to cache", filename);
            }
            return icon;
        } else { //We have a RAW image extension or something else like audio/video
            String exportResult = "";
            exportResult = "Success";
            if ("Success".equals(exportResult)) {
                //Hoping we have a thumbnail
                logger.debug("thumb nr1:"  + MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                if (cachedthumbfile.exists()) {
                    // Create icon of this thumbnail (thumbnail is 90% 160x120 already, but resize it anyway
                    logger.trace("create thumb nr1");
                    icon = ImageFunctions.createIcon(file);
                    if (icon != null) {
                        // display our created icon from the thumbnail
                        return icon;
                    }
                } else { //thumbnail image probably doesn't exist, move to 2nd option
                    // We do not cache PreviewImage as they are way too big, so check the tmp folder instead of the cache folder
                    thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_PreviewImage.jpg";
                    thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    logger.debug("PreviewImage option {}", MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                    if (thumbfile.exists()) {
                        // Create icon of this Preview
                        logger.trace("create thumb nr2");
                        //icon = ImageFunctions.createIcon(file);
                        icon = ImageFunctions.createIcon(thumbfile);
                        if (icon != null) {
                            BufferedImage thumbImg = (BufferedImage) icon.getImage();
                            // Save our created icon
                            StandardFileIO.saveIconToCache(filename, thumbImg);
                            // display our created icon from the preview
                            return icon;
                        }
                    } else { // So thumbnail and previewImage don't exist. Try 3rd option
                        // We do not cache JpgFromRaw as they are way too big, so check the tmp folder instead of the cache folder
                        thumbfilename = filename.substring(0, filename.lastIndexOf('.')) + "_JpgFromRaw.jpg";
                        thumbfile = new File(MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                        logger.debug("JpgFromRaw option {}", MyVariables.gettmpWorkFolder() + File.separator + thumbfilename);
                        if (thumbfile.exists()) {
                            // Create icon of this Preview
                            //icon = ImageFunctions.createIcon(file);
                            icon = ImageFunctions.createIcon(thumbfile);
                            if (icon != null) {
                                BufferedImage thumbImg = (BufferedImage) icon.getImage();
                                // Save our created icon
                                StandardFileIO.saveIconToCache(filename, thumbImg);
                                // display our created icon from the preview
                                return icon;
                            }
                        } else { // So thumbnail and previewImage don't exist. Try 4th option for photoshop thumbnail
                            if (cachedpsthumbfile.exists()) {
                                // Create icon of this Preview
                                icon = ImageFunctions.createIcon(file);
                                //icon = ImageFunctions.createIcon(thumbfile);
                                if (icon != null) {
                                    BufferedImage thumbImg = (BufferedImage) icon.getImage();
                                    // Save our created icon
                                    StandardFileIO.saveIconToCache(filename, thumbImg);
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
                                //ImageFunctions.getbasicImageData(file);
                                if (icon != null) {
                                    // display our created icon from the preview
                                    return icon;
                                }
                            } // end of 3nd option creation ("else if") and 4rd option creation (else)
                        } // end of 4th option creation ("else if") and cantdisplaypng option (else)
                    } // end of 2nd option creation ("else if") and 3rd option creation (else)
                } // end of 1st option creation ("else if") and 2nd option creation (else)

            } else {
                // Our "String exportResult = ExportPreviewsThumbnailsForIconDisplay(file);"  either failed due to some weird RAW format
                try {
                    BufferedImage img = ImageIO.read(mainScreen.class.getResource("/cantdisplay.png"));
                    icon = new ImageIcon(img);
                } catch (IOException e) {
                    logger.error("Error loading image", e);
                    icon = null;
                }
                //ImageFunctions.getbasicImageData(file);
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
                basicdata = getWidthHeightOrientation(file);
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
        cmdparams.add(file.getPath().replaceAll(" ", "\\ "));
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
        BufferedImage scaledImg = new BufferedImage(newScaledImg.getWidth(null), newScaledImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = scaledImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw the scaled image.
        g2.drawImage(img, 0, 0, scaledWidth, scaledHeight, Color.WHITE, null);
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

