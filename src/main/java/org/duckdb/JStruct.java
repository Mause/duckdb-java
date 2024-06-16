package org.duckdb;

import org.bytedeco.javacpp.annotation.ByRef;

import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;

public class JStruct implements Struct {
    final DuckDBLibrary.Vector datum;

    public JStruct(DuckDBLibrary.Vector datum) {
        this.datum = datum;
    }

    @Override
    public String getSQLTypeName() throws SQLException {
        return datum.GetType().toString();
    }

    @Override
    public Object[] getAttributes() throws SQLException {
        DuckDBLibrary.StructVector.GetEntries(this.datum);
        return new Object[0];
    }

    @Override
    public Object[] getAttributes(Map<String, Class<?>> map) throws SQLException {
        return new Object[0];
    }
}
