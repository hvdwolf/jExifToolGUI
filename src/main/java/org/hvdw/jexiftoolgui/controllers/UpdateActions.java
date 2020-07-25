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
        } else { // we were successful
            logger.info("Successfully did: " + Comments);
        }

    }


    static void update_1_4() {
        String queryresult = "";
        // Version 1.4
        // Add the userFavorites table and functionality for "Own commands" and "SQL queries"
        String sql = "Create table if not exists userFavorites ("
                +"id integer primary key autoincrement,"
                +"favorite_type text NOT NULL,"
                +"favorite_name text NOT NULL,"
                +"command_query text NOT NULL,"
                +"UNIQUE (favorite_type, favorite_name));";
        do_Update(sql, "creating the table userFavorites (1.4)");

        // => Add table ApplicationVersion to also check on database options.
        //sql = "create table if not exists ApplicationVersion ( version text );";
        //do_Update(sql, "creating table ApplicationVersion (1.4");
        //do_Update("delete from table ApplicationVersion;", "deleting (all) versions(s)");
        //do_Update("insert into ApplicationVersion(version) values('1.4.0');", "inserting version into table ApplicationVersion");
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
