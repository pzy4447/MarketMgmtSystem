package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tools.SqlHelper;
import entity.User;
import entity.UserProperty;
import entity.UserType;

/**
 * 实现对商品的增删改查等数据库操作
 * 
 * @author lenovo
 *
 */
public class UserDao {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Connection conn = SqlHelper.connect();// '%'||?||'%'
		String sql = "SELECT * FROM USER WHERE UNAME = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "admin");
			pstmt.executeQuery();
			System.out.printf("%s%n", pstmt.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * // 添加记录 UserDao gd = new UserDao(); boolean result = gd.add(new
		 * User("bob", "123456")); gd.add(new User("ant", "123456"));
		 * System.out.printf("%b%n", result);
		 */

		/*
		 * // 删除记录 UserDao gd = new UserDao(); boolean result = gd.delete(2);
		 * System.out.printf("%b%n", result);
		 */

		/*
		 * // 修改记录 UserDao gd = new UserDao(); boolean result =
		 * gd.modify(UserProperty.NAME, new User(1, "bobo", "123456")); result =
		 * gd.modify(UserProperty.PASSWORD, new User(2, "bobo", "123456789"));
		 * System.out.printf("%b%n", result);
		 */

		/*
		 * //查询 UserDao gd = new UserDao(); List<User> list =
		 * gd.queryByName("bob", false);
		 * System.out.printf("not fuzzy query :%n"); for (User user2 : list) {
		 * System.out.printf("%s%n", user2); }
		 * System.out.printf("fuzzy query :%n"); list = gd.queryByName("bob",
		 * true); for (User user2 : list) { System.out.printf("%s%n", user2); }
		 */
	}

	Connection conn;
	PreparedStatement pstmt = null;

	// UserType userType;// 指定管理的是售货员还是管理员

	/**
	 * 添加售货员到数据库
	 * 
	 * @param sm
	 *            售货员对象
	 * @return 操作结果
	 */
	public boolean addUser(User sm) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "INSERT INTO USER (UNAME, UPASSWORD, UTYPE) VALUES(?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sm.getName());
			pstmt.setString(2, sm.getPassword());
			pstmt.setString(3, sm.getUserType().toString());
			result = SqlHelper.executeUpdate(pstmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.printf("增加售货员时出错！%n");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return result;
	}

	/**
	 * 修改售货员信息
	 * 
	 * @param smp
	 *            待修改的信息
	 * @param sm
	 *            售货员对象
	 * @return 操作结果
	 */
	public boolean modifyUser(UserProperty smp, User sm) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "UPDATE USER SET UNAME = ? WHERE UID = ?";
		try {
			switch (smp) {
			case NAME:
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sm.getName());
				pstmt.setInt(2, sm.getId());
				result = SqlHelper.executeUpdate(pstmt);
				break;
			case PASSWORD:
				sql = "UPDATE USER SET UPASSWORD = ? WHERE UID = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, sm.getPassword());
				pstmt.setInt(2, sm.getId());
				result = SqlHelper.executeUpdate(pstmt);
				break;
			default:
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.printf("修改售货员信息时出错！%n");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return result;
	}

	/**
	 * 删除id指定的记录
	 * 
	 * @param id
	 *            待删除记录的id
	 * @return 操作结果
	 */
	public boolean deleteUser(int id) {
		boolean result = false;
		conn = SqlHelper.connect();
		String sql = "DELETE FROM USER WHERE UID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = SqlHelper.executeUpdate(pstmt);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.printf("删除售货员时出错！%n");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return result;
	}

	/**
	 * 根据姓名查询所有相关记录
	 * 
	 * @param name
	 *            售货员姓名
	 * @param isFuzzy
	 *            是否模糊查询
	 * @return 匹配的所有记录
	 */
	public List<User> queryUserByName(String name, boolean isFuzzy) {
		List<User> salesManList = new ArrayList<User>();
		conn = SqlHelper.connect();
		String sql = "";
		if (isFuzzy)// 模糊查询
			sql = "SELECT * FROM USER WHERE UNAME LIKE '%'||?||'%'";
		else {// 精确查询
			sql = "SELECT * FROM USER WHERE UNAME = ?";
		}
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			salesManList = convertResultSet2List(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}

		return salesManList;
	}

	/**
	 * 列出所有售货员，并按照指定属性的升序或降序排列
	 * 
	 * @param gp
	 *            售货员属性
	 * @param isAscending
	 *            是否升序排列
	 * @return 符合条件的所有售货员
	 */
	public List<User> queryAllUser(UserProperty gp, boolean isAscending,
			UserType userType) {
		List<User> userList = new ArrayList<User>();
		conn = SqlHelper.connect();
		String typeCondition = "";
		String orderCondition = "";

		if (!userType.equals(UserType.ALL)) {
			typeCondition = String.format(" WHERE UTYPE = '%s'",
					userType.toString());
		}
		switch (gp) {
		case ID:
			orderCondition = "ORDER BY UID ";
			break;
		case NAME:
			orderCondition = "ORDER BY UNAME ";
			break;
		default:
			System.out.printf("无法根据%s进行排序%n", gp);
			return userList;
		}
		if (isAscending)
			orderCondition += "ASC";
		else {
			orderCondition += "DESC";
		}
		String sql = "SELECT * FROM USER" + typeCondition + " "
				+ orderCondition;
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = SqlHelper.executeQuery(pstmt);
			while (rs.next()) {
				int id = rs.getInt("UID");
				String name = rs.getString("UNAME");
				String password = rs.getString("UPASSWORD");
				String ut = rs.getString("UTYPE");
				User user = new User(id, name, password, UserType.valueOf(ut));
				userList.add(user);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			SqlHelper.dispose();
		}
		return userList;
	}

	/**
	 * 将ResultSet中的值提取到list中
	 * 
	 * @param rs
	 *            查询结果ResultSet
	 * @return 商品列表list
	 * @throws SQLException
	 */
	private List<User> convertResultSet2List(ResultSet rs) throws SQLException {
		List<User> userList = new ArrayList<User>();
		while (rs.next()) {
			int id = rs.getInt("UID");
			String name = rs.getString("UNAME");
			String password = rs.getString("UPASSWORD");
			String ut = rs.getString("UTYPE");
			User user = new User(id, name, password, UserType.valueOf(ut));
			userList.add(user);
			// System.out.printf("%s%n", User);
		}
		return userList;
	}
}
