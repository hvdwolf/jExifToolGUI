package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.editpane.EditLensdata;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class UpdateActions {

    private IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(UpdateActions.class);

    static void update_1_4() {
        // Version 1.4 => Added the userFavorites table and functionality for "Own commands" and "SQL queries"
        String sql = "Create table if not exists userFavorites ("
                +"id integer primary key autoincrement,"
                +"favorite_type text NOT NULL,"
                +"favorite_name text NOT NULL,"
                +"command_query text NOT NULL,"
                +"UNIQUE (favorite_type, favorite_name));";
        String qr = SQLiteJDBC.insertUpdateQuery(sql);
        if (!"".equals(qr)) { //means we have an error
            JOptionPane.showMessageDialog(null, "Encountered an error creating the table userFavorites introduced in 1.4", "insert error", JOptionPane.ERROR_MESSAGE);
        } else { // we were successful
            logger.info("Did the database update for version 1.4");
        }
    }


    // ################## Start of the update stuff ####################
    // This is where we add extra tables or table data after an update that added extra functionality
    // This can also be alter table commands
    //
    // This can also mean that that we update/alter settings

    public static void Updates() {
        update_1_4();
    }
}
