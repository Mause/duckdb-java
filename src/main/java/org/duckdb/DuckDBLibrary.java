package org.duckdb;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.ByRef;
import org.bytedeco.javacpp.annotation.ByVal;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Const;
import org.bytedeco.javacpp.annotation.MemberGetter;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Opaque;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.StdMove;
import org.bytedeco.javacpp.annotation.StdString;
import org.bytedeco.javacpp.annotation.UniquePtr;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

import java.nio.ByteBuffer;

@Platform(
        include = {
                "duckdb.hpp",
                "duckdb/main/database.hpp",
                "duckdb/main/config.hpp"
        },
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

    public static class MaterializedQueryResult extends QueryResult {
        static {
            Loader.load();
        }

        @ByVal
        public native Value GetValue(int row_idx, int col_idx);
    }

    public enum LogicalTypeId {
        INVALID(0);

        public int value;

        LogicalTypeId(int value) {
            this.value = value;
        }

        public LogicalTypeIdActual valueOf() {
            for (var variant : LogicalTypeIdActual.values()) {
                if (variant.value == this.value) {
                    return variant;
                }
            }
            throw new IllegalStateException();
        }
    }

    public enum LogicalTypeIdActual {
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

        LogicalTypeIdActual(int v) {
            this.value = v;
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
        public native long asInt64();

        @Name("GetValue<int32_t>")
        public native int asInt32();

        @Name("GetValue<int16_t>")
        public native short asInt16();

        @Name("GetValue<int8_t>")
        public native byte asInt8();

        @Name("GetValue<bool>")
        public native boolean asBoolean();

        @Const
        @ByVal
        public native LogicalType type();

        @Name("GetValue<duckdb::uhugeint_t>")
        @ByVal
        public native HugeInt asUHugeInt();
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

        @UniquePtr
        public native PreparedStatement Prepare(String sql);
    }

    public static class PreparedStatement extends Pointer {
        @UniquePtr
        public native QueryResult Execute();
    }

    public static class Vector extends Pointer {
        @StdString
        @Name("ToString")
        @Override
        public native String toString();
        @ByVal
        public native LogicalType GetType();

        public native VectorType GetVectorType();

        @Cast("signed char*")
        public native ByteBuffer /*data_ptr_t*/ GetData();
    }

    public enum VectorTypeWrapper {
        FLAT(0),
        ROUND(0);
        private final int value;
        VectorTypeWrapper(int value) {
            this.value = value;
        }
    }

    public enum VectorType {
        INVALID;
        public int value;
        public VectorTypeWrapper resolve() {
            for (var variant : VectorTypeWrapper.values()) {
                if (variant.value == value) {
                    return variant;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    @Name("vector<duckdb::Value>")
    public static class ValueVector extends Pointer {
        @ByVal
        public native Value get(int idx);
    }

    @Name("vector<std::pair<std::string, duckdb::LogicalType>>")
    public static class PairVector extends Pointer {
        @ByVal
        public native Std.Pair get(int idx);
    }

    public static class StructValue extends Pointer {
        @ByVal
        public static native ValueVector GetChildren(@ByVal Value value);
    }

    public static class StructType extends Pointer {
        public static native int GetChildCount(@ByRef LogicalType type);

        @ByVal
        public static native PairVector GetChildTypes(@ByRef LogicalType type);
    }

    @Opaque
    public static class DataChunk extends Pointer {
        public native int ColumnCount();

        @ByVal
        public native Value GetValue(int columnIndex, int rowIdx);

        @MemberGetter
        @ByVal
        @StdMove
        public native VectorVector data();
    }

    @Name("vector<duckdb::Vector>")
    public static class VectorVector extends Pointer {
        @ByVal
        public native Vector get(int idx);
    }

    @Name("vector<duckdb::LogicalType>")
    public static class LogicalTypeVector extends Pointer {
        @ByVal
        public native LogicalType get(int i);
    }

    public static class BaseQueryResult extends Pointer {
        public native int ColumnCount();

        @MemberGetter
        @ByVal
        public native LogicalTypeVector types();

//        //! The type of the result (MATERIALIZED or STREAMING)
//        QueryResultType type;
//        //! The type of the statement that created this result
//        StatementType statement_type;
//        //! Properties of the statement
//        StatementProperties properties;
//        //! The SQL types of the result
//        vector<LogicalType> types;
//        //! The names of the result
//        vector<string> names;
//
//        public:
//                [[noreturn]] DUCKDB_API void ThrowError(const string &prepended_message = "") const;
//        DUCKDB_API void SetError(ErrorData error);
//        DUCKDB_API bool HasError() const;
//        DUCKDB_API const ExceptionType &GetErrorType() const;
//        DUCKDB_API const std::string &GetError();
//        DUCKDB_API ErrorData &GetErrorObject();
//        DUCKDB_API idx_t ColumnCount();
//
//        protected:
//        //! Whether or not execution was successful
//        bool success;
//        //! The error (in case execution was not successful)
//        ErrorData error;

    }

    public static class QueryResult extends BaseQueryResult {
//        @Name("Cast<duckdb::MaterializedQueryResult>")
//        @ByVal
//        public native MaterializedQueryResult asMaterialized();

        @ByVal
        @UniquePtr
        public native DataChunk Fetch();

        public native @StdString String ColumnName(int index);
    }

//    @Name("vector<duckdb::unique_ptr<duckdb::Vector>>")
//    public static class VectorUniquePtrVector extends Pointer {
////        @UniquePtr
////        @StdMove
////        public native Vector get(int index);
//    }

    public static class StructVector extends Pointer {
//        @ByVal
//        public static native VectorUniquePtrVector GetEntries(@ByRef Vector vector);
    }
}
