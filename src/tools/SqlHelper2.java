package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlHelper2 {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private String dbPath = "E:/java/workspace/MarketMgmtSystem/Files/111.db3";// "E:/java/workspace/MarketMgmtSystem/Files/MarketMgmtSystem.accdb";
	private final String jdbcName = "org.sqlite.JDBC";// "sun.jdbc.odbc.JdbcOdbcDriver";//
														// "";
	private final String dbur1 = "jdbc:sqlite:";// "jdbc:ucanaccess://";//
												// "jdbc:odbc:driver={Microsoft Access Driver (*.accdb)};DBQ=";//

	public Connection connect() {
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

	public void dispose() {
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
	public boolean executeUpdate(PreparedStatement preparedStatement) {
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
	public ResultSet executeQuery(PreparedStatement preparedStatement) {
		pstmt = preparedStatement;
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 查询指定表最后一行的自增ID,必须在未中断连接时进行，否则结果为0
	 * 
	 * @param tableName
	 * @return id 最后一行的自增id，查询失败时为0
	 */
	public int queryLastRowId(String tableName) {
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
	public Integer queryMaxValue(String colName, String tableName) {
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
	public int executeBatch(List<String> sqlList) throws SQLException {
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

	public void rollback() throws SQLException {
		// 回滚时需要将系统默认的提交方式重置为TRUE
		conn.rollback();
		conn.setAutoCommit(true);
		conn.close();
	}

	public void commit() throws SQLException {
		conn.commit();
		conn.setAutoCommit(true);
		conn.close();
	}

	public static void main(String[] args) {
		SqlHelper2 sh2Goods = new SqlHelper2();
		Integer i = sh2Goods.queryMaxValue("GOID", "GOODS_ORDER");
		if (i == null) {
			System.out.printf("无法确定订单号，程序返回%n");
			return;
		}
		try {
			// 商品
			String sqlGoods = "INSERT INTO GOODS(GNAME,GPRICE,GNUMBER) VALUES('apple',1.11,22)";
			List<String> sqlGoodsList = new ArrayList<String>();
			sqlGoodsList.add(sqlGoods);
			sqlGoodsList.add(sqlGoods);
			sqlGoodsList.add(sqlGoods);

			// 订单
			String sqlGoodsOrder = "INSERT INTO GOODS_ORDER(SID,PRICE,DATE) VALUES(3600, 2, 1111)";
			sqlGoodsList.add(sqlGoodsOrder);
			sqlGoodsList.add(sqlGoodsOrder);
			sqlGoodsList.add(sqlGoodsOrder);

			// 详单
			String sqlGoodsOrderDetail = String
					.format("INSERT INTO GOODS_ORDER_DETAIL(GOID,GID,NUMBER,PRICE) VALUES(%d, 11,1111,2.2)",
							i + 1);
			sqlGoodsList.add(sqlGoodsOrderDetail);
			sqlGoodsList.add(sqlGoodsOrderDetail);
			sqlGoodsList.add(sqlGoodsOrderDetail);
			int id3 = sh2Goods.executeBatch(sqlGoodsList);
			System.out.printf("%d%n", id3);
			sh2Goods.commit();

			System.out.printf("数据库插入订单信息成功%n");

		} catch (Exception e) {
			// 更新订单失败
			System.out.printf("数据库插入订单信息出错%n");
			e.printStackTrace();
			try {
				sh2Goods.rollback();

				System.out.printf("数据库已回滚%n");

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.err.printf("数据库回滚失败%n");
			}

		}
	}
}
