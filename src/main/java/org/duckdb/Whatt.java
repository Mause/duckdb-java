package org.duckdb;

import org.bytedeco.javacpp.tools.Builder;
import org.duckdb.DuckDBLibrary.DuckDB;
import org.duckdb.DuckDBLibrary.LogicalType;

import static java.util.Objects.requireNonNull;
import static org.duckdb.DuckDBLibrary.Connection;
import static org.duckdb.DuckDBLibrary.HugeInt;
import static org.duckdb.DuckDBLibrary.LogicalTypeId;
import static org.duckdb.DuckDBLibrary.MaterializedQueryResult;
import static org.duckdb.DuckDBLibrary.StructType;
import static org.duckdb.DuckDBLibrary.StructValue;
import static org.duckdb.DuckDBLibrary.Value;
import static org.duckdb.DuckDBLibrary.ValueVector;

public class Whatt {
    public static void main(String[] args) throws Exception {
        System.setProperty("org.bytedeco.javacpp.logger.debug", "true");
        new Builder()
                .classesOrPackages(DuckDBLibrary.class.getName(), Std.class.getName())
                .header(true)
                .generate(true)
                .property("global", DuckDB.class.getName())
                .deleteJniFiles(true)
                .build();

        DuckDB duckdb = new DuckDB();
//        duckdb.instance();
        Connection conn = new Connection(duckdb);
        MaterializedQueryResult queryResult = conn.Query("select thing from (select version(), 42) thing");
        System.out.println(queryResult);

        int row_idx = 0;
        for (int col_idx = 0; col_idx < queryResult.ColumnCount(); col_idx++) {
            String columnName = queryResult.ColumnName(col_idx);
            Value value = queryResult.GetValue(col_idx, row_idx);
            LogicalType type = value.type();
            var typeId = requireNonNull(type.id()).value;

            if (typeId == LogicalTypeId.HUGEINT.value) {
                HugeInt hugeInt = value.asHugeInt();
                System.out.printf("%s: %d %d%n", columnName, hugeInt.upper(), hugeInt.lower());
            } else if (typeId == LogicalTypeId.INTEGER.value) {
                int val = value.asInt32();
            } else if (typeId == LogicalTypeId.STRUCT.value) {
                ValueVector children = StructValue.GetChildren(value);
                int child_count = StructType.GetChildCount(type);

                var names = DuckDBLibrary.StructType.GetChildTypes(type);

                for (int i = 0; i < child_count; i++) {
                    var name = names.get(i);
                    var thing = children.get(i);
                    System.out.println(name);
                    System.out.println(thing);
                }
            } else {
                System.out.printf("%s: %s%n", columnName, value);
            }
        }
    }
}
