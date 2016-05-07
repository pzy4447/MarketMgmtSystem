package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tools.SqlHelper;

public class GoodsOrderDetailDao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	Connection conn;
	PreparedStatement pstmt = null;

	public void queryGoodsOrderDetail(int goid) {
		conn = SqlHelper.connect();
		String sql1 = "SELECT * FROM GOODS_ORDER WHERE GOID = ?";
		String sql2 = "SELECT * FROM GOODS_ORDER_DETAIL WHERE GOID = ?";
		try {
			pstmt = conn.prepareStatement(sql1);
			pstmt.setInt(1, goid);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			System.out.printf("该订单内容如下%n");
			while (rs.next()) {
				int sid = rs.getInt("SID");
				float price = rs.getFloat("PRICE");
				String date = rs.getString("DATE");
				System.out.printf(
						"goid=%d,salesmanId=%d,goodPrice=%f,goodNumber=%s%n",
						goid, sid, price, date);
			}
			pstmt = conn.prepareStatement(sql2);
			pstmt.setInt(1, goid);
			rs = SqlHelper.executeQuery(pstmt);
			System.out.printf("该详单内容如下%n");
			while (rs.next()) {
				int godid = rs.getInt("GODID");
				int gid = rs.getInt("GID");
				int sid = rs.getInt("SID");
				float price = rs.getFloat("PRICE");
				int number = rs.getInt("NUMBER");
				System.out
						.printf("godid=%d,goodid=%d,salesmanId=%d,goodPrice=%f,goodNumber=%d%n",
								godid, gid, sid, price, number);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
	}

}
