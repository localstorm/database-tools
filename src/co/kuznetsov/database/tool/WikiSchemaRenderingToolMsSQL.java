package co.kuznetsov.database.tool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public class WikiSchemaRenderingToolMsSQL {

    private static final Set<String> VARSIZE_TYPES = new HashSet<String>() {{
        add("CHAR");
        add("VARCHAR");
    }};

    public static void main(String[] args) throws Exception {
        WikiSchemaRenderingTool.main(args, "net.sourceforge.jtds.jdbc.Driver", null, VARSIZE_TYPES);
    }

}
