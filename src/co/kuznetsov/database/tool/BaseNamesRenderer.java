package co.kuznetsov.database.tool;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public class BaseNamesRenderer implements NamesRenderer {

    @Override
    public String renderSchema(String schema) {
        return unquote(schema);
    }

    private String unquote(String name) {
        return name.replaceAll("[\\\'`\\\"]", "");
    }

    @Override
    public String renderTable(String table) {
        return unquote(table);
    }

    @Override
    public String renderColumn(String column) {
        return unquote(column);
    }
}
