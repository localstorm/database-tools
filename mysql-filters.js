function acceptSchema(schemaName){
    return (schemaName.toLowerCase().indexOf('checkout') == 0);
}

function acceptTable(tableName){
    return (tableName.toLowerCase().indexOf('checkout.mn_') == 0);
}