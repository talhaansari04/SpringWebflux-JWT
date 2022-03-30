package com.mps.insight.cabi.feed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import com.mps.insight.dao.TableQueryBuilder;
import com.mps.insight.logger.MyLogger;

public class FeedQueryBuilder {
	private static final String defaultValue = "-";
	private static int counter;
	private static int itemSize;
	private static Connection con = null;
	private static String TABLE_NAME;

	private static void initTask(List<String> column) {
		try {
			if (!TableQueryBuilder.tableExist(con, TABLE_NAME)) {
				TableQueryBuilder mdt = new TableQueryBuilder(TABLE_NAME);
				for (String string : column) {
					if (string.equalsIgnoreCase("title")) {
						mdt.setColumnName(string, "varchar", 1000);
					} else {
						mdt.setColumnName(string, "varchar", 500);
					}
				}
				mdt.createTable(con);
			}
		} catch (Exception e) {
		}

	}

	public static void buildQuery(List<Map<String, String>> ls, List<String> column, String name) {
		TABLE_NAME = name;
		getConnection();
		initTask(column);
		int iTracker = 0;
		counter = ls.size();
		TableQueryBuilder mdt = new TableQueryBuilder(TABLE_NAME);
		try {

			for (String sts : column) {
				mdt.addColoumnName(sts);
			}

			for (Map<String, String> map : ls) {
				iTracker = 0;
				updateRowSize();
				for (String col : column) {
					String val = map.getOrDefault(col, defaultValue).replace("'", "\\'").replace("\"", "").trim();
					iTracker++;
					mdt.setRowValue(val, defaultValue);
				}
				mdt.addRow();
				if (itemSize == 10000) {
					mdt.getFinalQuery();
					mdt.committ(con);
					itemSize = 0;
				}
				if (counter == 0) {
					mdt.getFinalQuery();
					mdt.committ(con);
					counter = 0;
				}
			}
		} catch (Exception e) {
			MyLogger.log(e.toString() + "::" + iTracker);
		}
	}

	private static void updateRowSize() {
		counter = counter - 1;
		itemSize = itemSize + 1;
	}

	private static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/rsc?useSSL=false";
		String user = "root";
		String pass = "Mps@1234";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pass);
			if (con == null) {
				MyLogger.error("Not Connected");
			} else {
			}
		} catch (Exception e) {
			MyLogger.error(e.toString());
		}
		return con;
	}
}
