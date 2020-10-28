package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.Application;
import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.hvdw.jexiftoolgui.facades.IPreferencesFacade;
import org.hvdw.jexiftoolgui.facades.SystemPropertyFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.hvdw.jexiftoolgui.facades.SystemPropertyFacade.SystemPropertyKey.LINE_SEPARATOR;

public class SQLiteJDBC {

    private static IPreferencesFacade prefs = IPreferencesFacade.defaultInstance;
    //private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SQLiteJDBC.class);
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SQLiteJDBC.class);

    static public Connection connect() {

        // ######################### The basic necessary stuff ###################3
        Connection conn = null;
        String url;
        try {
            // db parameters
            boolean isWindows = Utils.isOsFromMicrosoft();
            if (isWindows) {
                url = "jdbc:sqlite:" + MyVariables.getjexiftoolguiDBPath();
            } else {
                url = "jdbc:sqlite:" + MyVariables.getjexiftoolguiDBPath().replace(" ", "\\ ");
            }
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            logger.debug("Connection to SQLite DB has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    static public Connection connect(String dbType) {

        // ######################### The basic necessary stuff ###################3
        Connection conn = null;
        String url;
        try {
            // db parameters
            boolean isWindows = Utils.isOsFromMicrosoft();
            if ("inmemory".equals(dbType)) {
                url = "jdbc:sqlite::memory:";
            } else {
                if (isWindows) {
                    url = "jdbc:sqlite:" + MyVariables.getjexiftoolguiDBPath();
                } else {
                    url = "jdbc:sqlite:" + MyVariables.getjexiftoolguiDBPath().replace(" ", "\\ ");
                }
            }
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            if ("inmemory".equals(dbType)) {
                logger.debug("Connection to in Memeory SQLite DB has been established.");
            } else {
                logger.debug("Connection to SQLite DB has been established.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }



    static public Connection imConnect() {

        // ################ The basic necessary stuff for in memory DB###################3
        Connection imconn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite::memory:";
            // create a connection to the database
            imconn = DriverManager.getConnection(url);
            logger.debug("Connection to SQLite in memory DB has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return imconn;
    }

    static public String generalQuery(String sql, String dbType) {
        String DBresult = "";
        StringBuilder sbresult = new StringBuilder();

        // get the fields that are being queried on and immediately remove spaces
        String queryFields = Utils.stringBetween(sql.toLowerCase(), "select", "from").replaceAll("\\s+","");  // regex "\s" is space, extra \ to escape the first \;
        logger.debug("the general query queryfields returned string: " + queryFields);
        String[] dbFields = queryFields.split(",");

        try (Connection conn = connect(dbType);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            int noOfFields = dbFields.length;
            //logger.debug("nooffields: " + String.valueOf(noOfFields));
            int counter = 1;
            while (rs.next()) {
                for (String dbfield : dbFields) {
                    if ( (noOfFields) > counter) {
                        sbresult.append(rs.getString(dbfield) + "\t");
                        //logger.debug("counter in tab: " + counter);
                        counter ++;
                    } else {
                        //sbresult.append(rs.getString(dbfield) + "\n");
                        sbresult.append(rs.getString(dbfield) + SystemPropertyFacade.getPropertyByKey(LINE_SEPARATOR));
                        //logger.debug("counter in linefeed: " + counter);
                        counter = 1;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("sql error: " + e.getMessage());
            sbresult.append(e.getMessage());
        }
        //logger.info(sbresult.toString());
        return sbresult.toString();
    }

    static public String singleFieldQuery(String sql, String field) {
        String DBresult = "";
        StringBuilder sbresult = new StringBuilder();
        String typeDB = "inmemory";

        try (
             Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                sbresult.append(rs.getString(field) + "\n");
                //logger.info(rs.getString(field));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        logger.trace("singlefieldQuery result {}", sbresult.toString());
        return sbresult.toString();
    }

    static public String countQuery(String sql) {
        String DBresult = "";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            rs.next(); // You always have a row, with the count
            int count = rs.getInt(1);
            DBresult = String.valueOf(count).trim();
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

        return DBresult;
    }

    static public String insertUpdateQuery(String sql, String dbType) {
        String queryresult = "";

        try {
            Connection conn = connect(dbType);
             Statement stmt  = conn.createStatement();
             stmt.executeUpdate(sql);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            queryresult = e.getMessage();
        }
        logger.trace("insertUpdateQueryResult {}", queryresult);
        return queryresult;
    }
    // ################### End of the basic necessary stuff ###################

    // ######################### Our program queries ###################

    static public String getDBversion() {
        String sql = "select version from ExiftoolVersion limit 1";
        return singleFieldQuery(sql, "version");
    }

    static public String getGroups() {
        String sql = "SELECT taggroup FROM Groups order by taggroup";
        return singleFieldQuery(sql, "taggroup");
    }

    static public String getdefinedlensnames() {
        String sql = "select lens_name from myLenses order by lens_Name";
        return singleFieldQuery(sql, "lens_name");
    }

    static public String getdefinedCustomSets() {
        String sql = "select customset_name from CustomMetadataset order by customset_name";
        return singleFieldQuery(sql, "customset_name");
    }

    static public String deleteCustomSetRows( String setName) {
        String sql = "delete from CustomMetadatasetLines where customset_name='" + setName + "'";
        return insertUpdateQuery(sql, "disk");
    }

    static public String queryByTagname(String searchString, boolean likequery) {
        String sqlresult = "";
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

        sqlresult = SQLiteJDBC.generalQuery(sql, "disk");
        return sqlresult;
    }

    static public String ownQuery(String sql) {
        String sqlresult = "";

        sqlresult = SQLiteJDBC.generalQuery(sql, "disk");
        return sqlresult;
    }

}
