package org.duckdb;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.StdString;
import org.bytedeco.javacpp.annotation.UniquePtr;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Platform(
        include = "duckdb.hpp",
        includepath = "/home/me/duckdb/src/include",
        link = "duckdb",
        compiler = "cpp11",
        define = {"SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"}
)
@Namespace("duckdb")
public class DuckDBLibrary implements InfoMapper {
    @Override
    public void map(InfoMap infoMap) {
        infoMap.put(
                new Info("duckdb::MaterializedQueryResult::GetValue<int>").define(true)
        );
    }

    public static class DuckDB extends Pointer {
        static {
            Loader.load();
        }

        public DuckDB() {
            allocate();
        }

        private native void allocate();
    }

    public static class MaterializedQueryResult extends Pointer {
        static {
            Loader.load();
        }

        public native int ColumnCount();

        public native @StdString String ColumnName(int index);
    }

    public static class DatabaseInstance extends Pointer {
        static {
            Loader.load();
        }

        public DatabaseInstance() {
            allocate();
        }

        private native void allocate();
    }

    public static class Connection extends Pointer {
        static {
            Loader.load();
        }

        public Connection(DuckDB duckDB) {
            allocate(duckDB);
        }

        private native void allocate(@ByRef DuckDB duckDB);

        @UniquePtr
        public native MaterializedQueryResult Query(String query);
    }
}
