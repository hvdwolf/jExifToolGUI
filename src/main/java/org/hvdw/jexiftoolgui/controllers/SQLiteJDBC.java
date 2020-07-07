package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJDBC {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    private final static Logger logger = LoggerFactory.getLogger(SQLiteJDBC.class);

    static public Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + MyVariables.getjexiftoolguiDBPath();
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            logger.debug("Connection to SQLite DB has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    static public String getDBversion() {
        String sql = "select version from ExiftoolVersion limit 1";
        String DBversion = "";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // Set initial value
            //sbgroups.append("all\n"); Remove again. We don' t want to retrieve and display 15.000+ tags
            // loop through the result set
            while (rs.next()) {
                DBversion = (rs.getString("version") + "\n");
                logger.info(rs.getString("taggroup"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        //logger.info("getGroups: " + groups);
        return DBversion;
    }

    static public String getGroups() {
        StringBuilder sbgroups = new StringBuilder();
        String sql = "SELECT taggroup FROM Groups order by taggroup";

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // Set initial value
            //sbgroups.append("all\n"); Remove again. We don' t want to retrieve and display 15.000+ tags
            // loop through the result set
            while (rs.next()) {
                sbgroups.append(rs.getString("taggroup") + "\n");
                logger.info(rs.getString("taggroup"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        //logger.info("getGroups: " + groups);
        return sbgroups.toString();
    }

    static public String queryByTagname(String searchString, boolean likequery) {
        StringBuilder sbresult = new StringBuilder();
        String sql = "";
        logger.debug("search string is: " + searchString);
        if (likequery) {
            sql = "select taggroup,tagname,tagtype,writable from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and tagname like '%" + searchString + "%' order by taggroup";
            // use our view
            //sql = "select taggroup,tagname,tagtype,writable from v_tags_groups where tagname like '%" + searchString + "%'";
        } else {
            sql = "select taggroup,tagname,tagtype,writable from Groups,Tags,tagsingroups where tagsingroups.groupid=Groups.id and tagsingroups.tagid=tags.id and taggroup='" + searchString + "'";
            // use our view
            //sql = "select taggroup,tagname,tagtype,writable from v_tags_groups where taggroup='" + searchString + "'";
        }

        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                sbresult.append(rs.getString("taggroup") + "\t");
                sbresult.append(rs.getString("tagname") + "\t");
                sbresult.append(rs.getString("tagtype") + "\t");
                sbresult.append(rs.getString("writable") + "\n");
                //logger.info(rs.getString("taggroup"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        logger.info(sbresult.toString());
        return sbresult.toString();
    }

}
