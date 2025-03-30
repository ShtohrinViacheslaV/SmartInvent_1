//package com.smartinvent.config;
//
//import org.hibernate.dialect.Dialect;
//import org.hibernate.dialect.function.StandardSQLFunction;
//import org.hibernate.dialect.function.SQLFunctionTemplate;
//import org.hibernate.dialect.function.VarArgsSQLFunction;
//import org.hibernate.type.IntegerType;
//import org.hibernate.type.StringType;
//
//public class SQLiteDialect extends Dialect {
//
//    public SQLiteDialect() {
//        registerColumnType(java.sql.Types.INTEGER, "integer");
//        registerColumnType(java.sql.Types.VARCHAR, "text");
//        registerColumnType(java.sql.Types.BLOB, "blob");
//        registerColumnType(java.sql.Types.REAL, "real");
//        registerColumnType(java.sql.Types.DOUBLE, "double");
//        registerColumnType(java.sql.Types.BOOLEAN, "integer");
//
//        registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""));
//        registerFunction("mod", new SQLFunctionTemplate(IntegerType.INSTANCE, "?1 % ?2"));
//        registerFunction("substr", new StandardSQLFunction("substr", StringType.INSTANCE));
//        registerFunction("length", new StandardSQLFunction("length", IntegerType.INSTANCE));
//    }
//
//    @Override
//    public boolean dropConstraints() {
//        return false;
//    }
//
//    @Override
//    public boolean hasAlterTable() {
//        return false;
//    }
//
//    @Override
//    public boolean supportsIfExistsBeforeTableName() {
//        return true;
//    }
//
//    @Override
//    public boolean supportsCascadeDelete() {
//        return false;
//    }
//}
