package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlHelper {
	private static Connection conn = null;
	private static PreparedStatement pstmt = null;
	private static String dbPath = "E:/java/workspace/MarketMgmtSystem/Files/MarketMgmtSystem.db3";
	private static final String jdbcName = "org.sqlite.JDBC";
	private static final String dbur1 = "jdbc:sqlite:";

	public static Connection connect() {
		try {
			if (conn != null && conn.isValid(10))
				return conn;
			String url = dbur1 + dbPath;
			conn = DriverManager.getConnection(url, "", "");
		} catch (SQLException e) {
			conn = null;
			// TODO Auto-generated catch block
			System.err.printf("Error occured when connect to the db%n");
			System.err.println("dbpath :" + dbPath);
			System.err.println("jdbcName :" + jdbcName);
			System.err.println("dbur1 :" + dbur1);
			e.printStackTrace();
		}
		return conn;
	}

	public static void dispose() {
		try {
			if (!pstmt.isClosed())
				pstmt.close();
			if (!conn.isClosed())
				conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn = null;
			pstmt = null;
		}
	}

	/**
	 * 根据指定的PreparedStatement执行更新操作
	 * 
	 * @param pstmt
	 *            包含sql语句及属性值
	 * @return 更新结果
	 */
	public static boolean executeUpdate(PreparedStatement preparedStatement) {
		boolean result = false;
		pstmt = preparedStatement;
		try {
			int rs = pstmt.executeUpdate();
			if (rs > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据指定的PreparedStatement执行查询操作
	 * 
	 * @param pstmt
	 *            包含sql语句及属性值
	 * @return 查询的数据
	 */
	public static ResultSet executeQuery(PreparedStatement preparedStatement) {
		pstmt = preparedStatement;
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static boolean executeBatch(List<String> sqlList,
			PreparedStatement preparedStatement) {
		boolean result = false;
		pstmt = preparedStatement;
		try {
			for (String sql : sqlList) {
				pstmt.addBatch(sql);
			}
			// 关闭自动提交，后面无论成功与否都要恢复为自动提交
			conn.setAutoCommit(false);
			pstmt.executeBatch();
			conn.setAutoCommit(true);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				// 产生的任何SQL异常都需要进行回滚,并设置为系统默认的提交方式,即为TRUE
				if (conn != null) {
					conn.rollback();
					conn.setAutoCommit(true);
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		} finally {
			dispose();
		}
		return result;
	}

	/**
	 * 查询指定表最后一行的自增ID,必须在未中断连接时进行，否则结果为0
	 * 
	 * @param tableName
	 * @return id 最后一行的自增id，查询失败时为0
	 */
	public static int queryLastRowId(String tableName) {
		int id = -1;
		String sql = String.format("select last_insert_rowid() from %s",
				tableName);
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			id = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 查询指定列的最大值，只限整数列
	 * 
	 * @param colName
	 *            列名
	 * @param tableName
	 *            表明
	 * @return 最大值，查询失败则结果为null
	 */
	public static Integer queryMaxValue(String colName, String tableName) {
		Integer value = null;
		String sql = String
				.format("select max(%s) from %s", colName, tableName);
		ResultSet rs = null;
		try {
			if (conn == null || conn.isClosed())
				connect();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			value = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dispose();
		}

		return value;
	}

	/**
	 * 执行事务
	 * 
	 * @param sqlList
	 *            sql语句
	 * @return 执行完毕后最后一行的id
	 * @throws SQLException
	 */
	public static int executeBatch(List<String> sqlList) throws SQLException {
		int id = -1;
		conn = connect();
		Statement stmt = conn.createStatement();
		for (String sql : sqlList) {
			stmt.addBatch(sql);
		}
		// 关闭自动提交，后面无论成功与否都要恢复为自动提交
		conn.setAutoCommit(false);
		stmt.executeBatch();
		id = queryLastRowId("GOODS_ORDER");
		return id;
	}

	public static void rollback() throws SQLException {
		// 回滚时需要将系统默认的提交方式重置为TRUE
		conn.rollback();
		conn.setAutoCommit(true);
		conn.close();
	}

	public static void commit() throws SQLException {
		conn.commit();
		conn.setAutoCommit(true);
		conn.close();
	}

	public static void main(String[] args) {
		try {
			SqlHelper sqlHelper = new SqlHelper();
			String sqlString = "select * from plan;";

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
