package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import tools.SqlHelper;
import entity.GoodsOrder;
import entity.GoodsOrderProperty;

public class GoodsOrderDao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoodsOrderDao god = new GoodsOrderDao();
		/*
		 * int id = god.addGoodsOrder(new GoodsOrder(3688, 2, "1111"));
		 * System.out.printf("%d%n", id);
		 */
		String sql = "INSERT INTO GOODS_ORDER(SID,PRICE,DATE) VALUES(3600, 2, 1111)";
		List<String> sqlList = new ArrayList<String>();
		sqlList.add(sql);
		sqlList.add(sql);
		sqlList.add(sql);
		// System.out.printf("%d%n", god.executeBatch(sqlList));
		// god.commit();
		/*
		 * List<GoodsOrder> list = god.queryAllGoodsOrder(true); for (GoodsOrder
		 * goodsOrder2 : list) { System.out.printf("%s%n", goodsOrder2); }
		 */
		/*
		 * 
		 * GoodsOrder go = god.queryGoodsOrder(2); System.out.printf("%s%n",
		 * go);
		 */
		/*
		 * boolean result = god.deleteGoodsOrder(2); System.out.printf("%b%n",
		 * result);
		 */
	}

	Connection conn;
	PreparedStatement pstmt = null;

	/**
	 * 插入新订单，并返回新纪录的id
	 * 
	 * @param go
	 * @return 新纪录的id，如果插入失败则返回-1，插入成功而查询失败则返回0
	 */
	public int addGoodsOrder(GoodsOrder go) {
		int id = -1;
		conn = SqlHelper.connect();
		String sql = "INSERT INTO GOODS_ORDER(SID,PRICE,DATE) VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, go.getSaleManId());
			pstmt.setFloat(2, go.getPrice());
			pstmt.setString(3, go.getDateString());
			if (!SqlHelper.executeUpdate(pstmt))
				return -1;
			id = SqlHelper.queryLastRowId("GOODS_ORDER");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return id;
	}

	public boolean deleteGoodsOrder(int id) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "DELETE FROM GOODS_ORDER WHERE GOID=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = SqlHelper.executeUpdate(pstmt);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return result;
	}

	public GoodsOrder queryGoodsOrder(int id) {
		conn = SqlHelper.connect();
		String sql = "SELECT * FROM GOODS_ORDER WHERE GOID = ?";
		GoodsOrder goodsOrder = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			int goid = rs.getInt("GOID");
			int sid = rs.getInt("SID");
			float price = rs.getFloat("PRICE");
			String date = rs.getString("DATE");
			goodsOrder = new GoodsOrder(goid, price, sid, date);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return goodsOrder;
	}

	public List<GoodsOrder> queryAllGoodsOrder(GoodsOrderProperty gop,
			boolean isAscending) {
		List<GoodsOrder> goodsOrderList = new ArrayList<GoodsOrder>();
		conn = SqlHelper.connect();
		String orderCondition = "";
		switch (gop) {
		case GOID:
			orderCondition = "ORDER BY GOID ";
			break;
		case SID:
			orderCondition = "ORDER BY SID ";
			break;
		case PRICE:
			orderCondition = "ORDER BY PRICE ";
			break;
		case DATE:
			orderCondition = "ORDER BY DATE ";
			break;
		default:
			System.out.printf("无法根据%s进行排序%n", gop);
		}
		if (isAscending)
			orderCondition += "ASC";
		else {
			orderCondition += "DESC";
		}
		String sql = "SELECT * FROM GOODS_ORDER " + orderCondition;
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			while (rs.next()) {
				int goid = rs.getInt("GOID");
				int sid = rs.getInt("SID");
				float price = rs.getFloat("PRICE");
				String date = rs.getString("DATE");
				GoodsOrder goodsOrder = new GoodsOrder(goid, price, sid, date);
				goodsOrderList.add(goodsOrder);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return goodsOrderList;
	}

}
