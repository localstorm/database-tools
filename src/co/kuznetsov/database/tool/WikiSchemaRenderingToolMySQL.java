package co.kuznetsov.database.tool;

import java.util.HashSet;
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
        WikiSchemaRenderingTool.main(args, "com.mysql.jdbc.Driver", null, new BaseNamesRenderer(), VARSIZE_TYPES);
    }

}
