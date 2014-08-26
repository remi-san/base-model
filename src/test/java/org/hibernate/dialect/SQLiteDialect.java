/*
 * The author disclaims copyright to this source code.  In place of
 * a legal notice, here is a blessing:
 * 
 *    May you do good and not evil.
 *    May you find forgiveness for yourself and forgive others.
 *    May you share freely, never taking more than you give.
 *
 */
package org.hibernate.dialect;

import java.sql.Types;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        this.registerColumnType(Types.BIT, "integer");
        this.registerColumnType(Types.TINYINT, "tinyint");
        this.registerColumnType(Types.SMALLINT, "smallint");
        this.registerColumnType(Types.INTEGER, "integer");
        this.registerColumnType(Types.BIGINT, "bigint");
        this.registerColumnType(Types.FLOAT, "float");
        this.registerColumnType(Types.REAL, "real");
        this.registerColumnType(Types.DOUBLE, "double");
        this.registerColumnType(Types.NUMERIC, "numeric");
        this.registerColumnType(Types.DECIMAL, "decimal");
        this.registerColumnType(Types.CHAR, "char");
        this.registerColumnType(Types.VARCHAR, "varchar");
        this.registerColumnType(Types.LONGVARCHAR, "longvarchar");
        this.registerColumnType(Types.DATE, "date");
        this.registerColumnType(Types.TIME, "time");
        this.registerColumnType(Types.TIMESTAMP, "timestamp");
        this.registerColumnType(Types.BINARY, "blob");
        this.registerColumnType(Types.VARBINARY, "blob");
        this.registerColumnType(Types.LONGVARBINARY, "blob");
        // registerColumnType(Types.NULL, "null");
        this.registerColumnType(Types.BLOB, "blob");
        this.registerColumnType(Types.CLOB, "clob");
        this.registerColumnType(Types.BOOLEAN, "integer");

        this.registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""));
        this.registerFunction("mod", new SQLFunctionTemplate(IntegerType.INSTANCE, "?1 % ?2"));
        this.registerFunction("substr", new StandardSQLFunction("substr", StringType.INSTANCE));
        this.registerFunction("substring", new StandardSQLFunction("substr", StringType.INSTANCE));
    }

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    /*
     * public boolean supportsInsertSelectIdentity() { return true; // As
     * specify in NHibernate dialect }
     */

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        // As specify in NHibernate dialect
        return false; 
    }

    /*
     * public String appendIdentitySelectToInsert(String insertString) { return
     * new StringBuffer(insertString.length()+30). // As specify in NHibernate
     * dialect append(insertString).
     * append("; ").append(getIdentitySelectString()). toString(); }
     */

    @Override
    public String getIdentityColumnString() {
        // return "integer primary key autoincrement";
        return "integer";
    }

    @Override
    public String getIdentitySelectString() {
        return "select last_insert_rowid()";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    protected String getLimitString(String query, boolean hasOffset) {
        String limitOffset = " limit ?";
        if (hasOffset) {
            limitOffset += " offset ?";
        }
        return new StringBuffer(query.length() + 20).append(query)
                .append(limitOffset)
                .toString();
    }

    @Override
    public boolean supportsTemporaryTables() {
        return true;
    }

    @Override
    public String getCreateTemporaryTableString() {
        return "create temporary table if not exists";
    }

    @Override
    public boolean dropTemporaryTableAfterUse() {
        return false;
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return "select current_timestamp";
    }

    @Override
    public boolean supportsUnionAll() {
        return true;
    }

    @Override
    public boolean hasAlterTable() {
        // As specify in NHibernate dialect
        return false; 
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public String getForUpdateString() {
        return "";
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public String getDropForeignKeyString() {
        throw new UnsupportedOperationException(
                "No drop foreign key syntax supported by SQLiteDialect");
    }

    @Override
    public String getAddForeignKeyConstraintString(String constraintName,
            String[] foreignKey, String referencedTable, String[] primaryKey,
            boolean referencesPrimaryKey) {
        throw new UnsupportedOperationException(
                "No add foreign key syntax supported by SQLiteDialect");
    }

    @Override
    public String getAddPrimaryKeyConstraintString(String constraintName) {
        throw new UnsupportedOperationException(
                "No add primary key syntax supported by SQLiteDialect");
    }

    @Override
    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    @Override
    public boolean supportsCascadeDelete() {
        return false;
    }
}