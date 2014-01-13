package co.kuznetsov.database.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public class WikiSchemaRenderingToolMySQL {

    private static final Set<String> VARSIZE_TYPES = new HashSet<String>() {{
        add("VARCHAR");
    }};

    public static void main(String[] args) throws Exception {
        WikiSchemaRenderingTool.main(args, "com.mysql.jdbc.Driver", null, VARSIZE_TYPES);
    }

}
