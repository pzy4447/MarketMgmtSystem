package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tools.SqlHelper;
import entity.Goods;
import entity.GoodsProperty;

/**
 * 实现对商品的增删改查等数据库操作
 * 
 * @author lenovo
 *
 */
public class GoodsDao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GoodsDao gd = new GoodsDao();
		/*
		 * List<Goods> list = gd.queryAllGoods(GoodsProperty.PRICE, true); for
		 * (Goods goods2 : list) { System.out.printf("%s%n", goods2); }
		 */

		// boolean result = gd.addGoods(new Goods("iphone4s", 2588, 30));

		/*
		 * List<Goods> list = gd.queryByNameFuzzy("iphone6Plus"); for (Goods
		 * goods2 : list) { System.out.printf("%s%n", goods2); }
		 */
		/*
		 * Goods goods = new Goods(2, "iphone4ss", 2588.8f, 50); boolean result
		 * = gd.deleteGoods(2); boolean result =
		 * gd.modifyGoods(GoodsProperty.PRICE, goods); boolean result =
		 * gd.modifyGoods(GoodsProperty.NAME, goods); boolean result =
		 * gd.modifyGoods(GoodsProperty.NUMBER, goods);
		 * System.out.printf("%b%n", result);
		 */

	}

	Connection conn;
	PreparedStatement pstmt = null;

	/**
	 * 添加指定商品到数据库
	 * 
	 * @param goods
	 *            待添加商品
	 * @return 添加结果
	 */
	public boolean addGoods(Goods goods) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "INSERT INTO GOODS(GNAME,GPRICE,GNUMBER) VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, goods.getName());
			pstmt.setFloat(2, goods.getPrice());
			pstmt.setInt(3, goods.getNumber());
			result = SqlHelper.executeUpdate(pstmt);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return result;
	}

	/**
	 * 修改商品信息
	 * 
	 * @param gp
	 *            商品属性
	 * @param goods
	 *            待修改商品
	 * @return 修改结果
	 */
	public boolean modifyGoods(GoodsProperty gp, Goods goods) {
		boolean result = false;
		conn = SqlHelper.connect();
		switch (gp) {
		case ID: //
			System.out.printf("商品ID无法修改！%n");
			break;
		case NAME: // 更改商品名称
			String sqlName = "UPDATE GOODS SET GNAME =? WHERE GID =?";
			try {
				pstmt = conn.prepareStatement(sqlName);
				pstmt.setString(1, goods.getName());
				pstmt.setInt(2, goods.getId());
				result = SqlHelper.executeUpdate(pstmt);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			} finally {
				SqlHelper.dispose();
			}
			break;
		case PRICE: // 更改商品名称
			String sqlPrice = "UPDATE GOODS SET GPRICE=? WHERE GID=?";
			try {
				pstmt = conn.prepareStatement(sqlPrice);
				pstmt.setDouble(1, goods.getPrice());
				pstmt.setInt(2, goods.getId());
				result = SqlHelper.executeUpdate(pstmt);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			} finally {
				SqlHelper.dispose();
			}
			break;
		case NUMBER: // 更改商品名称
			String sqlNum = "UPDATE GOODS SET GNUMBER=? WHERE GID=?";

			try {
				pstmt = conn.prepareStatement(sqlNum);
				pstmt.setInt(1, goods.getNumber());
				pstmt.setInt(2, goods.getId());
				result = SqlHelper.executeUpdate(pstmt);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			} finally {
				SqlHelper.dispose();
			}
			break;
		}
		return result;
	}

	/**
	 * 删除指定商品
	 * 
	 * @param id
	 *            待删除商品ID
	 * @return 删除结果
	 */
	public boolean deleteGoods(int id) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "DELETE FROM GOODS WHERE GID=?";

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

	/**
	 * 列出所有商品，并按照指定属性的升序或降序排列
	 * 
	 * @param gp
	 *            商品属性
	 * @param isAscending
	 *            是否升序排列
	 * @return 符合条件的所有商品
	 */
	public List<Goods> queryAllGoods(GoodsProperty gp, boolean isAscending) {
		List<Goods> goodsList = new ArrayList<Goods>();
		conn = SqlHelper.connect();
		String sql = "";
		switch (gp) {
		case ID:
			sql = "SELECT * FROM GOODS ORDER BY GID ";
			break;
		case NAME:
			sql = "SELECT * FROM GOODS ORDER BY GNAME ";
			break;
		case PRICE:
			sql = "SELECT * FROM GOODS ORDER BY GPRICE ";
			break;
		case NUMBER:
			sql = "SELECT * FROM GOODS ORDER BY GNUMBER ";
			break;
		default:
			System.out.printf("无法根据%s进行排序%n", gp);
			return goodsList;
		}
		if (isAscending)
			sql += "ASC";
		else {
			sql += "DESC";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			while (rs.next()) {
				int id = rs.getInt("GID");
				String name = rs.getString(2);
				float price = rs.getFloat(3);
				int number = rs.getInt(4);
				Goods goods = new Goods(id, name, price, number);
				goodsList.add(goods);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return goodsList;
	}

	/**
	 * 根据名称查询商品，模糊匹配
	 * 
	 * @param name
	 *            商品名称
	 * @return 符合条件的所有商品
	 */
	public List<Goods> fuzzyQueryByName(String name) {
		List<Goods> goodsList = new ArrayList<Goods>();
		conn = SqlHelper.connect();
		String sql = "SELECT * FROM GOODS WHERE GNAME LIKE '%'||?||'%'";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			goodsList = convertResultSet2List(rs);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return goodsList;
	}

	/**
	 * 根据指定的属性和值查询商品，精确匹配
	 * 
	 * @param gp
	 *            指定的属性
	 * @param value
	 *            指定的属性值
	 * @return 符合条件的所有商品
	 */
	public List<Goods> queryGoodsByProperty(GoodsProperty gp, Object value) {
		List<Goods> goodsList = new ArrayList<Goods>();
		if (gp.equals(GoodsProperty.NAME))
			return fuzzyQueryByName(value.toString());
		conn = SqlHelper.connect();
		String sql = "";
		switch (gp) {
		case ID:
			sql = "SELECT * FROM GOODS WHERE GID = ?";
			break;
		case NAME:
			break;
		case PRICE:
			sql = "SELECT * FROM GOODS WHERE GPRICE = ?";
			break;
		case NUMBER:
			sql = "SELECT * FROM GOODS WHERE GNUMBER = ?";
			break;
		default:
			System.out.printf("无法根据%s进行排序%n", gp);
			return goodsList;
		}
		try {
			pstmt = conn.prepareStatement(sql);
			// 设置sql的值
			switch (gp) {
			case ID:
				int id = (int) value;
				pstmt.setInt(1, id);
				break;
			case NAME:
				break;
			case PRICE:
				float price = (float) value;
				pstmt.setFloat(1, price);
				break;
			case NUMBER:
				int number = (int) value;
				pstmt.setInt(1, number);
				break;
			default:
				break;
			}
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			goodsList = convertResultSet2List(rs);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}

		return goodsList;
	}

	/**
	 * 将ResultSet中的值提取到list中
	 * 
	 * @param rs
	 *            查询结果ResultSet
	 * @return 商品列表list
	 * @throws SQLException
	 */
	private List<Goods> convertResultSet2List(ResultSet rs) throws SQLException {
		List<Goods> goodsList = new ArrayList<Goods>();
		while (rs.next()) {
			int id = rs.getInt("GID");
			String name = rs.getString(2);
			float price = rs.getFloat(3);
			int number = rs.getInt(4);
			Goods goods = new Goods(id, name, price, number);
			goodsList.add(goods);
			// System.out.printf("%s%n", goods);
		}
		return goodsList;
	}
}
