package org.hvdw.jexiftoolgui.model;

import org.hvdw.jexiftoolgui.controllers.SQLiteJDBC;
import org.slf4j.LoggerFactory;

import static org.hvdw.jexiftoolgui.controllers.SQLiteJDBC.*;

public class SQLiteModel {
    private final static ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.hvdw.jexiftoolgui.controllers.SQLiteJDBC.class);

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

        sqlresult = generalQuery(sql, "disk");
        return sqlresult;
    }

    static public String ownQuery(String sql) {
        String sqlresult = "";

        sqlresult = generalQuery(sql, "disk");
        return sqlresult;
    }

}
