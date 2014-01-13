package co.kuznetsov.database.tool;

/**
 * @author localstorm
 *         Date: 13.01.14
 */
public interface NamesRenderer {

    String renderSchema(String schema);
    String renderTable(String table);
    String renderColumn(String column);

}
