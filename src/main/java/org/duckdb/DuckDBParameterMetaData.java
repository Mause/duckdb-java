package org.duckdb;

import static org.duckdb.DuckDBResultSetMetaData.typeToClass;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

public class DuckDBParameterMetaData implements ParameterMetaData {
    private DuckDBResultSetMetaData meta;

    public DuckDBParameterMetaData(DuckDBResultSetMetaData meta) {
        this.meta = meta;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return JdbcUtils.unwrap(this, iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }

    @Override
    public int getParameterCount() throws SQLException {
        return meta.param_count;
    }

    @Override
    public int isNullable(int param) throws SQLException {
        return ParameterMetaData.parameterNullableUnknown;
    }

    @Override
    public boolean isSigned(int param) throws SQLException {
        return true;
    }

    @Override
    public int getPrecision(int param) throws SQLException {
        return meta.param_types_meta[param - 1].type_size;
    }

    @Override
    public int getScale(int param) throws SQLException {
        return meta.param_types_meta[param - 1].scale;
    }

    @Override
    public int getParameterType(int param) throws SQLException {
        throw new SQLFeatureNotSupportedException("getParameterType");
    }

    @Override
    public String getParameterTypeName(int param) throws SQLException {
        return meta.param_types[param - 1].name();
    }

    @Override
    public String getParameterClassName(int param) throws SQLException {
        return typeToClass(meta.param_types[param - 1]);
    }

    @Override
    public int getParameterMode(int param) throws SQLException {
        return ParameterMetaData.parameterModeIn;
    }
}
