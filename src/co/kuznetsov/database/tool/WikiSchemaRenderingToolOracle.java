package co.kuznetsov.database.tool;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public class WikiSchemaRenderingToolOracle {

    private static final Set<String> VARSIZE_TYPES = new HashSet<String>() {{
        add("VARCHAR2");
        add("VARCHAR");
    }};

    public static void main(String[] args) throws Exception {
        Properties oracleProps = new Properties();
        oracleProps.setProperty("remarksReporting", "true");
        WikiSchemaRenderingTool.main(args, "oracle.jdbc.driver.OracleDriver", oracleProps, new BaseNamesRenderer(), VARSIZE_TYPES);
    }

}
