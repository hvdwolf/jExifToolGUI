package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.editpane.EditLensdata;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class UpdateActions {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(UpdateActions.class);

    static void do_Update( String sql, String Comments) {
        String qr = SQLiteJDBC.insertUpdateQuery(sql);
        if (!"".equals(qr)) { //means we have an error
            JOptionPane.showMessageDialog(null, "Encountered an error " + Comments);
            logger.trace("Encountered an error: {}", Comments);
        } else { // we were successful
            logger.trace("Successfully did: " + Comments);
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
    }


    // ################## Start of the update stuff ####################
    // This is where we add extra tables or table data after an update that added extra functionality
    // This can also be alter table commands
    //
    // This can also mean that that we update/alter settings

    public static void Updates() {

        update_1_4();
        update_1_6();
    }
}
