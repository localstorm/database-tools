function acceptSchema(schemaName){
    return (schemaName.toLowerCase().indexOf('abo') == 0);
}

function acceptTable(tableName){
    return (tableName.toLowerCase().indexOf('abo.a_') == 0);
}