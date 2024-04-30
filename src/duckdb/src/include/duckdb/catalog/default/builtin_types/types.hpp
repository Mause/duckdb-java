//===----------------------------------------------------------------------===//
//                         DuckDB
//
// duckdb/catalog/default/builtin_types/types.hpp
//
//
//===----------------------------------------------------------------------===//
// This file is generated by scripts/generate_builtin_types.py

#pragma once

#include "duckdb/common/types.hpp"
#include "duckdb/common/array.hpp"

namespace duckdb {

struct DefaultType {
	const char *name;
	LogicalTypeId type;
};

using builtin_type_array = std::array<DefaultType, 72>;

static constexpr const builtin_type_array BUILTIN_TYPES{{
	{"decimal", LogicalTypeId::DECIMAL},
	{"dec", LogicalTypeId::DECIMAL},
	{"numeric", LogicalTypeId::DECIMAL},
	{"time", LogicalTypeId::TIME},
	{"date", LogicalTypeId::DATE},
	{"timestamp", LogicalTypeId::TIMESTAMP},
	{"datetime", LogicalTypeId::TIMESTAMP},
	{"timestamp_us", LogicalTypeId::TIMESTAMP},
	{"timestamp_ms", LogicalTypeId::TIMESTAMP_MS},
	{"timestamp_ns", LogicalTypeId::TIMESTAMP_NS},
	{"timestamp_s", LogicalTypeId::TIMESTAMP_SEC},
	{"timestamptz", LogicalTypeId::TIMESTAMP_TZ},
	{"timetz", LogicalTypeId::TIME_TZ},
	{"interval", LogicalTypeId::INTERVAL},
	{"varchar", LogicalTypeId::VARCHAR},
	{"bpchar", LogicalTypeId::VARCHAR},
	{"string", LogicalTypeId::VARCHAR},
	{"char", LogicalTypeId::VARCHAR},
	{"nvarchar", LogicalTypeId::VARCHAR},
	{"text", LogicalTypeId::VARCHAR},
	{"blob", LogicalTypeId::BLOB},
	{"bytea", LogicalTypeId::BLOB},
	{"varbinary", LogicalTypeId::BLOB},
	{"binary", LogicalTypeId::BLOB},
	{"hugeint", LogicalTypeId::HUGEINT},
	{"int128", LogicalTypeId::HUGEINT},
	{"uhugeint", LogicalTypeId::UHUGEINT},
	{"uint128", LogicalTypeId::UHUGEINT},
	{"bigint", LogicalTypeId::BIGINT},
	{"oid", LogicalTypeId::BIGINT},
	{"long", LogicalTypeId::BIGINT},
	{"int8", LogicalTypeId::BIGINT},
	{"int64", LogicalTypeId::BIGINT},
	{"ubigint", LogicalTypeId::UBIGINT},
	{"uint64", LogicalTypeId::UBIGINT},
	{"integer", LogicalTypeId::INTEGER},
	{"int", LogicalTypeId::INTEGER},
	{"int4", LogicalTypeId::INTEGER},
	{"signed", LogicalTypeId::INTEGER},
	{"integral", LogicalTypeId::INTEGER},
	{"int32", LogicalTypeId::INTEGER},
	{"uinteger", LogicalTypeId::UINTEGER},
	{"uint32", LogicalTypeId::UINTEGER},
	{"smallint", LogicalTypeId::SMALLINT},
	{"int2", LogicalTypeId::SMALLINT},
	{"short", LogicalTypeId::SMALLINT},
	{"int16", LogicalTypeId::SMALLINT},
	{"usmallint", LogicalTypeId::USMALLINT},
	{"uint16", LogicalTypeId::USMALLINT},
	{"tinyint", LogicalTypeId::TINYINT},
	{"int1", LogicalTypeId::TINYINT},
	{"utinyint", LogicalTypeId::UTINYINT},
	{"uint8", LogicalTypeId::UTINYINT},
	{"struct", LogicalTypeId::STRUCT},
	{"row", LogicalTypeId::STRUCT},
	{"list", LogicalTypeId::LIST},
	{"map", LogicalTypeId::MAP},
	{"union", LogicalTypeId::UNION},
	{"bit", LogicalTypeId::BIT},
	{"bitstring", LogicalTypeId::BIT},
	{"boolean", LogicalTypeId::BOOLEAN},
	{"bool", LogicalTypeId::BOOLEAN},
	{"logical", LogicalTypeId::BOOLEAN},
	{"uuid", LogicalTypeId::UUID},
	{"guid", LogicalTypeId::UUID},
	{"enum", LogicalTypeId::ENUM},
	{"null", LogicalTypeId::SQLNULL},
	{"float", LogicalTypeId::FLOAT},
	{"real", LogicalTypeId::FLOAT},
	{"float4", LogicalTypeId::FLOAT},
	{"double", LogicalTypeId::DOUBLE},
	{"float8", LogicalTypeId::DOUBLE}
}};

} // namespace duckdb