package org.duckdb;

import org.bytedeco.javacpp.tools.Builder;
import org.duckdb.DuckDBLibrary.DuckDB;
import org.duckdb.DuckDBLibrary.LogicalType;

import static org.duckdb.DuckDBLibrary.Connection;
import static org.duckdb.DuckDBLibrary.HugeInt;
import static org.duckdb.DuckDBLibrary.MaterializedQueryResult;
import static org.duckdb.DuckDBLibrary.StructType;
import static org.duckdb.DuckDBLibrary.StructValue;
import static org.duckdb.DuckDBLibrary.Value;
import static org.duckdb.DuckDBLibrary.ValueVector;


public class Whatt {
    public static void main(String[] args) throws Exception {
        new Builder()
//                .outputDirectory("target/generated-sources/annotations")
                .classesOrPackages(DuckDBLibrary.class.getName(), Std.class.getName())
                .header(true)
                .deleteJniFiles(false)
//                .generate(true)
                .build();

        try (var c = new JConnection()) {
            try (var rs = c.prepareStatement("select test_all_types from test_all_types()").executeQuery()) {
                rs.next();
                var metaData = rs.getMetaData();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    var name = metaData.getColumnName(i + 1);
                    System.out.println(name);
                    var value = rs.getObject(i + 1);
                    System.out.println(value);

                    JStruct value1 = (JStruct) value;
                    System.out.println(value1.getSQLTypeName());
                    DuckDBLibrary.Vector datum = value1.datum;
                    System.out.println(datum.GetVectorType());
                    System.out.println(datum.GetValue(0));
                    DuckDBLibrary.StructVector.GetEntries(datum);
//                    System.out.println(DuckDBLibrary.FlatVector datum.GetData().getPointer());
                    System.out.println(DuckDBLibrary.FlatVector.GetData(datum));
                }
            }
        }
    }

    public void test() throws Exception {
        DuckDB duckdb = new DuckDB();
        Connection conn = new Connection(duckdb);
        MaterializedQueryResult queryResult = conn.Query("select thing from (select version(), 42) thing");

        int row_idx = 0;
        for (int col_idx = 0; col_idx < queryResult.ColumnCount(); col_idx++) {
            String columnName = queryResult.ColumnName(col_idx);
            Value value = queryResult.GetValue(col_idx, row_idx);
            LogicalType type = value.type();
            var typeId = type.id().resolve();

            switch (typeId) {
                case HUGEINT:
                    HugeInt hugeInt = value.asHugeInt();
                    System.out.printf("%s: %d %d%n", columnName, hugeInt.upper(), hugeInt.lower());
                    break;
                case INTEGER:
                    int val = value.asInt32();
                    break;
                case STRUCT:
                    ValueVector children = StructValue.GetChildren(value);
                    int child_count = StructType.GetChildCount(type);

                    var names = StructType.GetChildTypes(type);

                    for (int i = 0; i < child_count; i++) {
                        var name = names.get(i);
                        var thing = children.get(i);
                        System.out.println(name);
                        System.out.println(thing);
                    }
                    break;
                default:
                    System.out.printf("%s: %s%n", columnName, value);
                    break;
            }
        }
    }
}
