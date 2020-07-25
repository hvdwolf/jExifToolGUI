package org.hvdw.jexiftoolgui;

import java.awt.Font;

public class MyConstants {
    public static final String MY_DATA_FOLDER = "jexiftoolgui_data";
    // exiftool image info parameters
    public static final String[] ALL_PARAMS = {"-a","-G", "-tab"};
    public static final String[] EXIF_PARAMS = {"-a", "-exif:all","-G", "-tab"};
    public static final String[] XMP_PARAMS = {"-a", "-xmp:all","-G", "-tab"};
    public static final String[] IPTC_PARAMS = {"-a", "-iptc:all","-G", "-tab"};
//    public static final String[] GPS_LOC_PARAMS = {"-a","-G", "-tab","-gps:all","-xmp:GPSLatitude","-xmp:GPSLongitude","-xmp:Location","-xmp:Country","-xmp:State","-xmp:City"};
    public static final String[] GPS_PARAMS = {"-a","-G", "-tab","-gps:all"};
    public static final String[] LOCATION_PARAMS = {"-a","-G", "-tab","-location:all"};
    public static final String[] GPANO_PARAMS = {"-a", "-G", "-tab", "-xmp:StitchingSoftware","-xmp:CroppedAreaImageHeightPixels","-xmp:CroppedAreaImageWidthPixels","-xmp:CroppedAreaLeftPixels","-xmp:CroppedAreaTopPixels","-xmp:FullPanoHeightPixels","-xmp:FullPanoWidthPixels","-xmp:ProjectionType","-xmp:UsePanoramaViewer","-xmp:PoseHeadingDegrees","-xmp:InitialViewHeadingDegrees","-xmp:InitialViewPitchDegrees","-xmp:InitialViewRollDegrees","-xmp:InitialHorizontalFOVDegrees"};
    public static final String[] ICC_PARAMS = {"-a", "-icc_profile:all","-G", "-tab"};
    public static final String[] MAKERNOTES_PARAMS = {"-a", "-makernotes:all","-G", "-tab"};
    public static final String[] COMPOSITE_PARAMS = {"-a", "-composite:all","-G", "-tab"};
//    public static final String[] LENS_PARAMS = {"-exif:lensmake","-exif:lensmodel","-exif:lensserialnumber","-exif:focallength","-exif:focallengthIn35mmformat","-exif:fnumber","-exif:maxaperturevalue","-composite:aperture","-exif:meteringmode","-composite:lensid","-makernotes:conversionlens","-makernotes:lenstype","-makernotes:lensserialnumber","-makernotes:lensfirmwareversion","-G", "-tab"};
    public static final String[] LENS_PARAMS = {"-exif:lensmake","-exif:lensmodel","-exif:lensserialnumber","-makernotes:lensserialnumber","-exif:focallength","-exif:focallengthIn35mmformat","-exif:fnumber","-exif:maxaperturevalue","-exif:meteringmode","-composite:lensid","-composite:lens","-makernotes:focusdistance","-makernotes:conversionlens","-makernotes:lenstype","-makernotes:lensfirmwareversion","-G", "-tab"};
    public static final String[] REF_IMAGE_DATETIME = {"-exif:ModifyDate","-exif:DateTimeOriginal","-exif:CreateDate"};
    public static final String[] GPANO_PROJECTIONS = {"equirectangular", "cylindrical", "rectilinear"};

    // exiftool image modification parameters
    public static final String[] SET_FILEDATETIME_TO_DATETIMEORIGINAL = {"-FileModifyDate<DateTimeOriginal"};
    public static final String[] ALTER_DATETIME = {"-e","-n","-exif:Make","-exif:Model","-exif:ModifyDate","-exif:DateTimeOriginal","-exif:CreateDate","-exif:Artist","-exif:Copyright","-exif:UserComment","-exif:ImageDescription"};
    public static final String[] REPAIR_JPG_METADATA = {"-all=","-tagsfromfile","@","-all:all","-unsafe","-F"};
    public static final String[] CREATE_ARGS_FILE_STRINGS = {"-args","--filename","--directory","-w","args"};

    // image file filters
    public static final String[] SUPPORTED_IMAGES = {"3fr","acr","ai","ait","arw","bmp","dib","btf","cos","cr2","crw","ciff","cs1","dcm","dc3","dic","dicm","dcp","dcr","djvu","djv","dng","eip","erf","exif","exr","fff","fpx","gif","hdp","wdp","hdr","icc","icm","idml","iiq","ind","indd","indt","inx","itc","j2c","jpc","jp2","jpf","j2k","jpm","jpx","jpeg","jpg","k25","kdc","mef","mie","miff","mif","mos","mpo","mrw","mxf","nef","nrw","orf","pcd","pdf","pef","pgf","pict","pct","pmp","png","jng","mng","ppm","pbm","pgm","psp","pspimage","qtif","qti","qif","raf","raw","rw2","rwl","rwz","sr2","srf","srw","svg","thm","tiff","tif","webp","x3f","xcf","xmp"};
    public static final String[] RAW_IMAGES = {"3fr","acr","ai","ait","arw","dib","btf","cos","cr2","crw","ciff","cs1","dcm","dc3","dic","dicm","dcp","dcr","djvu","djv","dng","eip","erf","exif","exr","fff","fpx","hdp","wdp","hdr","icc","icm","idml","iiq","ind","indd","indt","inx","itc","j2c","jpc","jp2","jpf","j2k","jpm","jpx","k25","kdc","mef","mie","miff","mif","mos","mpo","mrw","mxf","nef","nrw","orf","pcd","pdf","pef","pgf","pict","pct","pmp","jng","mng","ppm","pbm","pgm","psp","pspimage","qtif","qti","qif","raf","raw","rw2","rwl","rwz","sr2","srf","srw","svg","thm","tiff","tif","webp","x3f","xcf","xmp"};
    public static final String[] SUPPORTED_VIDEOS = {"3gp","avi","flv","mkv","mov","mp2","m4v","mp4","mpeg","mpg","ogv","swf","wmv","wtv"};
    public static final String[] SUPPORTED_AUDIOS = {"3gpp","aif","ape","au","flac","m3u","m4a","mid","midi","mp3","oga","ogg","ra","ram","wav","wma"};
    public static final String[] SUPPORTED_FORMATS = {"3fr","3g2","3gp2","3gp","3gpp","acr","afm","acfm","amfm","ai","ait","aiff","aif","aifc","ape","arw","asf","avi","bmp","dib","btf","chm","cos","cr2","crw","ciff","cs1","dcm","dc3","dic","dicm","dcp","dcr","dfont","divx","djvu","djv","dng","doc","dot","docx","docm","dotx","dotm","dylib","dv","dvb","eip","eps","epsf","ps","erf","exe","dll","exif","exr","f4a","f4b","f4p","f4v","fff","fla","flac","flv","fpx","gif","gz","gzip","hdp","wdp","hdr","html","htm","xhtml","icc","icm","idml","iiq","ind","indd","indt","inx","itc","j2c","jpc","jp2","jpf","j2k","jpm","jpx","jpeg","jpg","k25","kdc","key","kth","la","lnk","m2ts","mts","m2t","ts","m4a","m4b","m4p","m4v","mef","mie","miff","mif","mka","mkv","mks","mos","mov","qt","mp3","mp4","mpc","mpeg","mpg","m2v","mpo","mqv","mrw","mxf","nef","nmbtemplate","nrw","numbers","odb","odc","odf","odg","odi","odp","ods","odt","ofr","ogg","ogv","orf","otf","pac","pages","pcd","pdf","pef","pfa","pfb","pfm","pgf","pict","pct","pmp","png","jng","mng","ppm","pbm","pgm","ppt","pps","pot","potx","potm","ppsx","ppsm","pptx","pptm","psd","psb","psp","pspimage","qtif","qti","qif","ra","raf","ram","rpm","rar","raw","raw","riff","rif","rm","rv","rmvb","rsrc","rtf","rw2","rwl","rwz","so","sr2","srf","srw","svg","swf","thm","thmx","tiff","tif","ttf","ttc","vob","vrd","vsd","wav","webm","webp","wma","wmv","wv","x3f","xcf","xls","xlt","xlsx","xlsm","xlsb","xltx","xltm","xmp"};
    // Subsection file extensions
    public static final String[] JAVA8_IMG_EXTENSIONS = {"bmp","gif,","jpg", "jpeg", "png"};
    public static final String[] JAVA11_IMG_EXTENSIONS = {"bmp","gif,","jpg", "jpeg", "png", "tif", "tiff"};

    // Date_time and Date strings
    public static final String[] DATES_TIMES_STRINGS = {"YYYYMMDDHHMMSS", "YYYYMMDD_HHMMSS", "YYYYMMDD-HHMMSS", "YYYY_MM_DD_HH_MM_SS", "YYYY-MM-DD-HH-MM-SS"};
    public static final String[] DATES_STRINGS = {"YYYYMMDD", "YYYY_MM_DD", "YYYY-MM-DD"};

    // Default font
    public static final Font appdefFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
}
