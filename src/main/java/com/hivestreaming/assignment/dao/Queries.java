package com.hivestreaming.assignment.dao;

public class Queries {

	final public static String ALL_TABLE_NAMES = "SELECT name FROM sqlite_master WHERE type='table'";

	final public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s (timestamp INTEGER, percent INTEGER)";

	final public static String INSERT_USAGE = "INSERT INTO %s VALUES (%d, %d)";

	final public static String SELECT_USAGE = "SELECT * FROM %s WHERE timestamp >= %d AND timestamp <= %d AND percent >= %d";

}
