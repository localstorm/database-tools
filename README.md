database-tools
==============

Some occasionally helpful database tools.

System requirements: Java 7+.

-------------------------------------------------------------------------------------
WikiSchemaRenderingTool -- Wiki-docs generator for Oracle/Mysql/MS SQL/...
-------------------------------------------------------------------------------------

*** WikiSchemaRenderingToolOracle ***

Generates Wiki-markup documentation for Oracle database tables. What tables/schemas to look at is determined by
JavaScript filtering functions (see oracle-filters.js):

function acceptSchema(schemaName){
    return (schemaName.toLowerCase().indexOf('abo') == 0);
}

function acceptTable(tableName){
    return (tableName.toLowerCase().indexOf('abo.a_') == 0);
}

Usage:

Windows: java -cp "dist/\*;lib/\*" co.kuznetsov.database.tool.WikiSchemaRenderingToolOracle <Oracle JDBC url> <user password> oracle-filters.js

Unix:    java -cp "dist/\*:lib/\*" co.kuznetsov.database.tool.WikiSchemaRenderingToolOracle <Oracle JDBC url> <user password> oracle-filters.js


*** WikiSchemaRenderingToolMySQL ***

Generates Wiki-markup documentation for MySQL database tables. What tables/schemas to look at is determined by
JavaScript filtering functions (see mysql-filters.js):

function acceptSchema(schemaName){
    return (schemaName.toLowerCase().indexOf('checkout') == 0);
}

function acceptTable(tableName){
    return (tableName.toLowerCase().indexOf('checkout.mn_') == 0);
}

Usage:

Windows: java -cp "dist/\*;lib/\*" co.kuznetsov.database.tool.WikiSchemaRenderingToolMySQL <MySQL JDBC url> <user password> mysql-filters.js

Unix:    java -cp "dist/\*:lib/\*" co.kuznetsov.database.tool.WikiSchemaRenderingToolMySQL <MySQL JDBC url> <user password> mysql-filters.js
