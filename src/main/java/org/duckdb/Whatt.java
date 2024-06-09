package org.duckdb;

import org.bytedeco.javacpp.tools.Builder;
import org.bytedeco.javacpp.tools.ParserException;
import org.duckdb.DuckDBLibrary.DuckDB;

import java.io.IOException;

import static org.duckdb.DuckDBLibrary.Connection;
import static org.duckdb.DuckDBLibrary.MaterializedQueryResult;

public class Whatt {
    public static void main(String[] args) throws ParserException, IOException, InterruptedException, ClassNotFoundException {
        System.setProperty("org.bytedeco.javacpp.logger.debug", "true");
        new Builder()
                .classesOrPackages(DuckDBLibrary.class.getName())
                .header(true)
                .generate(true)
                .property("global", DuckDB.class.getName())
                .deleteJniFiles(true)
                .build();

        DuckDB duckdb = new DuckDB();
//        duckdb.instance();
        Connection conn = new Connection(duckdb);
        MaterializedQueryResult queryResult = conn.Query("select version()");
        System.out.println(queryResult);

        int row_idx = 0;
        for (int col_idx = 0; col_idx < queryResult.ColumnCount(); col_idx++) {
            System.out.println(queryResult.ColumnName(col_idx));
            System.out.println(queryResult.GetValue(row_idx, col_idx));
        }
    }
}
