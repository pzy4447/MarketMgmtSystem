package page;

import java.util.List;

import tools.IOTools;
import dao.UserDao;
import entity.User;

public class LoginPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 用户登录
	 * 
	 * @param retry
	 *            重试次数
	 * @return true=成功;false=失败;null=用户退出;
	 * 
	 */
	public static Boolean mainPage(int retry) {
		int i = 0;
		loginUser = null;// 每次登陆前重置该值
		while (i++ < retry) {
			System.out.printf("请输入用户名(输入exit退出)%n");
			String name = IOTools.getInputString();
			if (name != null && name.toLowerCase().equals("exit"))
				return null;// 用户退出时返回null
			System.out.printf("请输入口令%n");
			String password = IOTools.getInputString();
			Boolean result = validateUser(name, password);
			if (result == null) {
				System.out.printf("用户名不存在！%n");
			} else if (result) {
				return true;
			} else {
				System.out.printf("用户名或密码错误！%n");
			}
		}
		return false;
	}

	/**
	 * @param name
	 * @param password
	 * @return true=成功;false=失败;null=用户不存在;
	 */
	private static Boolean validateUser(String name, String password) {
		boolean result = false;
		UserDao smd = new UserDao();
		List<User> salesManList = smd.queryUserByName(name, false);
		if (salesManList == null || salesManList.isEmpty()) {
			return null;
		}
		User salesMan = salesManList.get(0);
		if (password.equals(salesMan.getPassword())) {
			loginUser = salesMan;
			result = true;
		} else {
			return false;
		}
		return result;
	}

	private static User loginUser = null;

	public static User getLoginUser() {
		return loginUser;
	}

}
