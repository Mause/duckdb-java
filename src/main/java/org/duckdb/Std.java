package org.duckdb;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(
        include = {
                "duckdb.hpp",
                "duckdb/main/database.hpp",
                "duckdb/main/config.hpp"
        },
        includepath = "/home/me/duckdb/src/include",
        compiler = "cpp11",
        link = "duckdb",
        define = {"SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"}
)
@Namespace("std")
public class Std {
    static {
        Loader.load();
    }

    @Name("pair<std::string, duckdb::LogicalType>")
    public static class Pair extends Pointer {
        static {
            Loader.load();
        }

        @MemberGetter
        @ByVal
        @StdString
        public native String first();

        @MemberGetter
        @ByVal
        public native DuckDBLibrary.LogicalType second();

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first() +
                    ", second=" + second() +
                    '}';
        }
    }
}
