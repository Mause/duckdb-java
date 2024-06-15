package org.duckdb;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.StdString;
import org.bytedeco.javacpp.annotation.UniquePtr;
import org.bytedeco.javacpp.annotation.MemberGetter;
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

        @ByVal
        public native Value GetValue(int row_idx, int col_idx);
    }

    public enum LogicalTypeId {
        INVALID(0),
        SQLNULL(1), /* NULL type, used for constant NULL */
        UNKNOWN(2), /* unknown type, used for parameter expressions */
        ANY(3),     /* ANY type, used for functions that accept any type as parameter */
        USER(4),    /* A User Defined Type (e.g., ENUMs before the binder) */
        BOOLEAN(10),
        TINYINT(11),
        SMALLINT(12),
        INTEGER(13),
        BIGINT(14),
        DATE(15),
        TIME(16),
        TIMESTAMP_SEC(17),
        TIMESTAMP_MS(18),
        TIMESTAMP(19), //! us
        TIMESTAMP_NS(20),
        DECIMAL(21),
        FLOAT(22),
        DOUBLE(23),
        CHAR(24),
        VARCHAR(25),
        BLOB(26),
        INTERVAL(27),
        UTINYINT(28),
        USMALLINT(29),
        UINTEGER(30),
        UBIGINT(31),
        TIMESTAMP_TZ(32),
        TIME_TZ(34),
        BIT(36),
        STRING_LITERAL(37), /* string literals, used for constant strings - only exists while binding */
        INTEGER_LITERAL(38),/* integer literals, used for constant integers - only exists while binding */

        UHUGEINT(49),
        HUGEINT(50),
        POINTER(51),
        VALIDITY(53),
        UUID(54),

        STRUCT(100),
        LIST(101),
        MAP(102),
        TABLE(103),
        ENUM(104),
        AGGREGATE_STATE(105),
        LAMBDA(106),
        UNION(107),
        ARRAY(108);

        public final int value;

        private LogicalTypeId(int v) {
            this.value = v;
        }

        private LogicalTypeId(LogicalTypeId e) {
            this.value = e.value;
        }

        public LogicalTypeId intern() {
            for (LogicalTypeId e : values()) if (e.value == value) return e;
            return this;
        }

        @Override
        public String toString() {
            return intern().name();
        }
    }

    public static class LogicalType extends Pointer {
        public native LogicalTypeId id();

        @Name("ToString")
        @StdString
        public native String toString();
    }

    @Name("hugeint_t")
    public static class HugeInt extends Pointer {
        @MemberGetter
        public native int upper();

        @MemberGetter
        public native int lower();
    }

    public static class Value extends Pointer {
        @StdString
        @Name("ToString")
        @Override
        public native String toString();

        @ByVal
        @Name("GetValue<duckdb::hugeint_t>")
        public native HugeInt asHugeInt();

        @Name("GetValue<int64_t>")
        public native int asInt64();

        @Name("GetValue<int32_t>")
        public native int asInt32();

        @Name("GetValue<int16_t>")
        public native int asInt16();

        @Name("GetValue<int8_t>")
        public native int asInt8();

        @Const
        @ByVal
        public native LogicalType type();
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
