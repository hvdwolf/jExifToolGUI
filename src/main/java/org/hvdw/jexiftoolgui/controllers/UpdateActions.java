package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyConstants;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hvdw.jexiftoolgui.controllers.StandardFileIO.extract_resource_to_jexiftoolguiFolder;
import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.USER_HOME;

public class UpdateActions {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UpdateActions.class);

    static void do_Update( String sql, String Comments) {
        String qr = SQLiteJDBC.insertUpdateQuery(sql, "disk");
        if (!"".equals(qr)) { //means we have an error
            JOptionPane.showMessageDialog(null, "Encountered an error " + Comments);
            logger.error("Encountered an error: {}", Comments);
        } else { // we were successful
            logger.debug("Successfully did: " + Comments);
        }
    }

    static void fill_UserMetadataCustomSet_Tables( String sql_file) {
        String sqlFile = StandardFileIO.readTextFileAsStringFromResource(sql_file);
        String[] sqlCommands = sqlFile.split("\\r?\\n"); // split on new lines
        for (String sqlCommand : sqlCommands) {
            if (!sqlCommand.startsWith("--") && !sqlCommand.equals("")) {
                do_Update(sqlCommand, sqlCommand);
            }
        }
    }


    static void update_1_4() {
        String queryresult = "";
        // Version 1.4
        // Add the userFavorites table and functionality for "Own commands" and "SQL queries"
        String sql = "Create table if not exists userFavorites (\n"
                +"id integer primary key autoincrement,\n"
                +"favorite_type text NOT NULL,\n"
                +"favorite_name text NOT NULL,\n"
                +"command_query text NOT NULL,\n"
                +"UNIQUE (favorite_type, favorite_name));";
        do_Update(sql, "creating the table userFavorites (1.4)");

        // => Add table ApplicationVersion to also check on database options.
        //sql = "create table if not exists ApplicationVersion ( version text );";
        //do_Update(sql, "creating table ApplicationVersion (1.4");
        //do_Update("delete from table ApplicationVersion;", "deleting (all) versions(s)");
        //do_Update("insert into ApplicationVersion(version) values('1.4.0');", "inserting version into table ApplicationVersion");
    }

    static void update_1_6() {
        String queryresult = "";
        // version 1.6
        // First drop some unused tables
        do_Update("drop table if exists CustomView","drop table CustomView");
        do_Update("drop table if exists CustomViewLines","drop table CustomViewLines");
        do_Update("drop table if exists CustomEdit","drop table CustomEdit");
        do_Update("drop table if exists CustomEditLines","drop table CustomEditLines");

        // Add the User defined custom metadata set combinations tables
        String sql = "Create table if not exists CustomMetadataset (\n" +
                "    id integer primary key autoincrement,\n" +
                "    customset_name text NOT NULL UNIQUE,\n" +
                "    custom_config text,\n" +
                "    unique (id, customset_name))";
        do_Update(sql, "creating the table CustomMetadataset (1.6)");

        sql = "Create table if not exists CustomMetadatasetLines (\n" +
                "    id integer primary key autoincrement,\n" +
                "    customset_name text NOT NULL,\n" +
                "    rowcount integer,\n" +
                "    screen_label text NOT NULL,\n" +
                "    tag text NOT NULL,\n" +
                "    default_value text,\n" +
                "    UNIQUE (customset_name, tag),\n" +
                "    foreign key(customset_name) references CustomMetadataset(customset_name))";
        do_Update(sql, "creating the table CustomMetadatasetLines (1.6)");

        // pre-fill both tables if necessary with isadg data
        queryresult = SQLiteJDBC.generalQuery("select count(customset_name) from custommetadatasetLines where customset_name='isadg'", "disk");
        logger.debug("isadg test {}", queryresult.trim());
        if (!"26".equals(queryresult.trim())) {
            fill_UserMetadataCustomSet_Tables("sql/fill_isadg.sql");
        }
        // our data folder
        String strjexiftoolguifolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER;
        // Check if isadg-struct.cfg exists
        File isadg = new File(strjexiftoolguifolder + File.separator + "isadg-struct.cfg");
        if (isadg.exists()) {
            isadg.delete();
        }
        String method_result = extract_resource_to_jexiftoolguiFolder("isadg-struct.cfg", strjexiftoolguifolder, "");


        // pre-fill both tables if necessary with gps_location data
        queryresult = SQLiteJDBC.generalQuery("select count(customset_name) from custommetadatasetLines where customset_name='gps_location'", "disk");
        logger.debug("gps_location test {}", queryresult.trim());
        if (!"19".equals(queryresult.trim())) {
            fill_UserMetadataCustomSet_Tables("sql/fill_location.sql");
        }

        // pre-fill both tables if necessary with gps_location data
        queryresult = SQLiteJDBC.generalQuery("select count(customset_name) from custommetadatasetLines where customset_name='Google Photos'", "disk");
        logger.debug("Google Photos test {}", queryresult.trim());
        if (!"38".equals(queryresult.trim())) {
            fill_UserMetadataCustomSet_Tables("sql/fill_gphotos.sql");
        }
    }

    static void update_1_7() {
        // our data folder
        String strjexiftoolguifolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER;
        String args_files[] = {"exif2iptc.args","gps2xmp.args","iptc2xmp.args","pdf2xmp.args","xmp2gps.args","xmp2pdf.args","exif2xmp.args","iptc2exif.args","iptcCore.args","xmp2exif.args","xmp2iptc.args"};

        String str_args_folder = strjexiftoolguifolder + File.separator + "args";
        File args_folder = new File(str_args_folder);
        if (!args_folder.exists()) {
            try {
                Files.createDirectories(Paths.get(str_args_folder));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                logger.error("Error creating directory " + str_args_folder);
            }
        }
        for (String args_file : args_files) {
            String method_result = extract_resource_to_jexiftoolguiFolder("args" + File.separator + args_file, strjexiftoolguifolder, "args");
        }
    }

    static void update_1_9() {
        String queryresult = "";

        // our data folder
        String strjexiftoolguifolder = SystemPropertyFacade.getPropertyByKey(USER_HOME) + File.separator + MyConstants.MY_DATA_FOLDER;
        // Check if vrae.config exists
        File vrae = new File(strjexiftoolguifolder + File.separator + "vrae.config");
        if (!vrae.exists()) {
            //vrae.delete();
            String method_result = extract_resource_to_jexiftoolguiFolder("vrae.config", strjexiftoolguifolder, "");

            // add vrae data to both tables if necessary
            queryresult = SQLiteJDBC.generalQuery("select count(customset_name) from custommetadatasetLines where customset_name='vrae-display'", "disk");
            logger.debug("VRAE data test {}", queryresult.trim());
            if (queryresult.isEmpty()) {
                fill_UserMetadataCustomSet_Tables("sql/fill_vrae-display.sql");
            }

        }

    }

    // ################## Start of the update stuff ####################
    // This is where we add extra tables or table data after an update that added extra functionality
    // This can also be to alter table commands
    //
    // This can also mean that we update/alter settings
    // This is run as background task using a swingworker

    public static void Updates() {

        SwingWorker sw = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                update_1_4();
                update_1_6();
                update_1_7();
                update_1_9();
                //logger.debug("Checked and when necessary did the updates");
                return null;
            }
            @Override
            public void done() {
                // maybe some time this needs to do something as well
            }
        };
        sw.execute();
    }
}
