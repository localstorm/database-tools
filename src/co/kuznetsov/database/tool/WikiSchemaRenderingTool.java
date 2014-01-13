package co.kuznetsov.database.tool;

import org.localstorm.dynamic.jsbean.JsBean;
import schemacrawler.schema.*;
import schemacrawler.schemacrawler.InclusionRule;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaInfoLevel;
import schemacrawler.utility.SchemaCrawlerUtility;

import javax.script.ScriptException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public class WikiSchemaRenderingTool {

    private final Set<String> varsizeTypes;
    private final JsBean filters;
    private final NamesRenderer namesRenderer;

    public static void main(String[] args, String driverClass, Properties extras, NamesRenderer namesRenderer, Set<String> varsizeTypes) throws Exception {

        if (args.length < 4) {
            WikiSchemaRenderingTool.usage(System.err);
            return;
        }

        String url = args[0];
        String username = args[1];
        String password = args[2];
        String filtersPath = args[3];

        WikiSchemaRenderingTool rdr = WikiSchemaRenderingTool.newRenderer(filtersPath, varsizeTypes, namesRenderer);

        if (WikiSchemaRenderingTool.loadDriver(driverClass)) return;

        Connection connection = null;

        try {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            if (extras != null) {
                props.putAll(extras);
            }
            connection = DriverManager.getConnection(url, props);
            rdr.render(connection);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static WikiSchemaRenderingTool newRenderer(String filtersPath, Set<String> varsizeTypes, NamesRenderer namesRenderer) throws Exception {
        JsBean filters = new JsBean();
        filters.addJsLibPaths(filtersPath);
        filters.init();
        return new WikiSchemaRenderingTool(varsizeTypes, filters, namesRenderer);
    }

    public WikiSchemaRenderingTool(Set<String> varsizeTypes, JsBean filters, NamesRenderer namesRenderer) {
        this.filters = filters;
        this.varsizeTypes = varsizeTypes;
        this.namesRenderer = namesRenderer;
    }

    public static boolean loadDriver(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your driver (" + className + ")?");
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public void render(Connection connection) throws SQLException, SchemaCrawlerException {
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        // Set what details are required in the schema - this affects the
        // time taken to crawl the schema

        SchemaInfoLevel sil = SchemaInfoLevel.standard();
        sil.setRetrieveAdditionalSchemaCrawlerInfo(true);
        options.setSchemaInfoLevel(sil);
        options.setSchemaInclusionRule(new InclusionRule() {
            @Override
            public boolean include(String schema) {
                try {
                    return filters.processJava("acceptSchema($value.get())", schema, Boolean.class);
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        options.setTableInclusionRule(new InclusionRule() {
            @Override
            public boolean include(String table) {
                try {
                    return filters.processJava("acceptTable($value.get())", table, Boolean.class);
                } catch (ScriptException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        final Database database = SchemaCrawlerUtility.getDatabase(connection, options);

        for (final Schema schema : database.getSchemas()) {
            renderSchema(schema);
            for (final Table table : database.getTables(schema)) {
                renderTableStart(table);
                for (final Column column : table.getColumns()) {
                    renderColumn(column);
                }
                renderTableEnd(table);
            }
        }
    }

    private void renderColumn(Column column) {
        ColumnDataType cdt = column.getColumnDataType();
        String typeStr = niceType(cdt, column.getSize());
        String remarks = column.getRemarks();
        String cname = namesRenderer.renderColumn(column.getName());
        System.out.println(String.format("|| %s | %s | %s | %s ||", cname, typeStr, remarks, constraints(column)));
    }

    private String constraints(Column column) {

        boolean nullable = column.isNullable();
        String nullableStr = (nullable) ? "NULL" : "NOT_NULL";

        String refStr = "";
        if (column.isPartOfPrimaryKey()) {
            refStr += "PK";
        }
        if (column.isPartOfForeignKey()) {
            if (refStr.length() > 0) {
                refStr += ", ";
            }
            refStr += "FK";
        }

        return nullableStr + ((refStr.length() > 0) ? (", " + refStr) : "");
    }

    private String niceType(ColumnDataType cdt, int size) {
        String typeName = cdt.getDatabaseSpecificTypeName();
        if (needsSize(typeName)) {
            typeName += "(" + size + ")";
        }
        return typeName;
    }

    private boolean needsSize(String typeName) {
        return varsizeTypes.contains(typeName);
    }

    private void renderTableStart(Table table) {
        System.out.println("=====" + namesRenderer.renderTable(table.getName()) + "=====");
        System.out.println("#|");
        System.out.println("||**Колонка**|**Тип**|**Комментарий**|**Ограничения**||");
    }

    private void renderTableEnd(Table table) {
        System.out.println("|#\n");
    }

    private void renderSchema(Schema schema) {
        //System.out.println("SCHEMA: " + namesRenderer.renderSchema(schema.getName()));
    }

    public static void usage(PrintStream err) {
        err.println("Usage: <jdbc-url> <username> <password> <filters.js>");
    }
}
