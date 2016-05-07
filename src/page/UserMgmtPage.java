package page;

import java.util.List;

import tools.IOTools;
import dao.UserDao;
import entity.User;
import entity.UserProperty;
import entity.UserType;

public class UserMgmtPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserMgmtPage.mainPage();
	}

	static UserDao ud = new UserDao();

	public static void mainPage() {

		boolean isContinue = true;
		// 不断循环，直到用户选择退出
		while (isContinue) {
			outputBanner();
			// 获取用户输入
			System.out.printf("请选择:%n");
			int i = IOTools.getInputInt();
			switch (i) {
			case 0:
				isContinue = false;// 退出while循环，将返回上一级菜单
				break;
			case 1:
				addUser();// 添加人员
				break;
			case 2:
				modifyUser();// 修改人员
				break;
			case 3:
				deleteUser();// 删除人员
				break;
			case 4:
				queryUser();// 查询人员
				break;
			case 5:
				displayUser(UserType.SALESMAN);// 列出所有售货员
				break;
			case 6:
				displayUser(UserType.MANAGER);// 列出所有管理员
				break;
			case 7:
				displayUser(UserType.ALL);// 列出所有人员
				break;
			default:
				System.err.println("该选项不存在！");
				break;
			}
		}
	}

	/**
	 * 输出主界面
	 */
	private static void outputBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("正在进行人员管理!");
		IOTools.outputWithTabs("1.添加");
		IOTools.outputWithTabs("2.修改");
		IOTools.outputWithTabs("3.删除");
		IOTools.outputWithTabs("4.查询");
		IOTools.outputWithTabs("5.列出所有售货员");
		IOTools.outputWithTabs("6.列出所有管理员");
		IOTools.outputWithTabs("7.列出所有人员");
		IOTools.outputWithTabs("0.返回");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}

	/**
	 * 验证用户名设置是否符合要求： 字母数字下划线横线或汉字
	 * 
	 * @param name
	 *            待验证用户名
	 * @return 验证结果
	 */
	private static boolean isUserNameValid(String name) {
		boolean result = false;
		if (name.matches("[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+"))
			return true;
		return result;
	}

	/**
	 * 验证口令设置是否符合要求： 1.长度为6-16个字符 2.不能是9位以下纯数字3.不能包含空格
	 * 
	 * @param name
	 *            待验证口令
	 * @return 验证结果
	 */
	private static boolean isPasswordValid(String name) {
		boolean result = false;
		if (name.contains(" "))
			return false;
		if (name.matches("\\d{0,9}"))
			return false;
		if (name.matches("\\w{6,16}"))
			return true;
		return result;
	}

	/**
	 * 添加用户
	 * 
	 * @param userType
	 *            指定用户类型，售货员or管理员
	 */
	public static void addUser() {
		// 不断循环，直到用户选择退出
		while (true) {
			String name = "";
			while (true) {
				System.out.printf("请输入售货员姓名%n");
				name = IOTools.getInputString();
				if (isUserNameValid(name)) {
					// 校验通过，跳出循环
					break;
				} else {
					// 校验不通过，强制继续输入，直到通过
					System.out.printf("用户名不合法！%n");
				}
			}
			// 到数据库中查询，该姓名是否已经存在
			List<User> SalesManList = ud.queryUserByName(name, false);
			if (SalesManList != null && SalesManList.size() > 0) {
				// 售货员存在
				System.out.printf("该售货员已经存在，无法添加！%n");
				for (User SalesMan : SalesManList) {
					System.out.printf("%s%n", SalesMan);
				}
			} else {
				String password1 = "";
				String password2 = "";
				// 输入口令
				while (true) {
					System.out.printf("请输入售货员密码%n");
					password1 = IOTools.getInputString();
					if (!isPasswordValid(password1)) {
						// 校验不通过，则强制重新输入
						System.out.printf("口令不合法！%n");
						continue;
					}
					System.out.printf("再次输入密码%n");
					password2 = IOTools.getInputString();
					if (password1.equals(password2)) {
						// 口令正确，跳出循环
						break;
					} else {
						System.out.printf("两次密码不一致！%n");
					}
				}
				// 输入人员类型
				int uTypeIndex;
				while (true) {
					System.out.printf("请选择人员身份%n");
					System.out.printf("1.售货员%n");
					System.out.printf("2.管理员%n");
					uTypeIndex = IOTools.getInputInt();
					if (uTypeIndex > 1 || uTypeIndex < 0) {
						// 校验不通过，则强制重新输入
						System.out.printf("输入不合法！%n");
						continue;
					} else {
						break;
					}
				}
				// 添加到数据库前确认
				System.out.printf("确定添加？(y/n)%n");
				if (IOTools.isChooseYes()) {
					// 在数据库中添加
					boolean result = ud.addUser(new User(name, password2,
							UserType.valueOf(uTypeIndex)));
					if (result)
						System.out.printf("添加成功！%n");
					else {
						System.out.printf("添加失败！%n");
					}
				}
			}

			// 询问用户是否继续添加，不继续则跳出while循环，程序返回
			System.out.printf("继续添加？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
		// 返回主界面
	}

	/**
	 * 通过交互，获取用户选定的待操作的人员
	 * 
	 * @return 用户选定的人员
	 */
	private static User getChoosedUser() {
		User salesMan = null;
		System.out.printf("请输入人员姓名%n");
		String name = IOTools.getInputString();

		// 首先在数据库中精确匹配姓名
		List<User> salesManList = ud.queryUserByName(name, false);
		// 匹配成功
		if (salesManList != null && salesManList.size() == 1) {
			salesMan = salesManList.get(0);
			// 人员存在
			System.out.printf("该人员存在，信息如下：%n");
			System.out.printf("%s%n", salesMan);
		} else {
			// 精确匹配不到则模糊匹配
			salesManList = ud.queryUserByName(name, true);
			int size = salesManList.size();

			if (salesManList != null && size == 0) {
				System.out.printf("该人员不存在！%n");
			} else {// 匹配到的内容由用户选择
				for (int i = 0; i < salesManList.size(); i++) {
					System.out.printf("%d : %s%n", i + 1, salesManList.get(i)
							.getName());
				}
				while (true) {
					System.out.printf("请选择人员，输入0取消%n");
					int index = IOTools.getInputInt();
					if (index < 0 || index >= size + 1) {
						System.out.printf("该序号不存在!%n");
						continue;
					} else if (index == 0) {
						break;
					} else {
						salesMan = salesManList.get(index - 1);// 显示序号时，加了个1，这里进行补偿
						break;
					}
				}
			}
		}
		return salesMan;
	}

	/**
	 * 修改人员
	 */
	public static void modifyUser() {
		while (true) {
			User salesMan = getChoosedUser();
			// 用户选中了要修改的人员
			if (salesMan != null) {
				System.out.printf("请选择修改内容!%n");
				System.out.printf("1.姓名%n");
				System.out.printf("2.密码%n");
				System.out.printf("0.返回%n");
				Boolean result = null;
				int i = IOTools.getInputInt();
				switch (i) {
				case 0:
					break;
				case 1:
					// 姓名
					System.out.printf("请输入新姓名");
					String newName = IOTools.getInputString();
					salesMan.setName(newName);
					result = ud.modifyUser(UserProperty.NAME, salesMan);
					// 更新姓名
					break;
				case 2:
					// 密码
					System.out.printf("请输入新密码");
					String password = IOTools.getInputString();
					salesMan.setPassword(password);
					result = ud.modifyUser(UserProperty.PASSWORD, salesMan);
					break;
				default:
					System.err.println("该选项不存在！！");
					break;
				}
				if (result != null && result)
					System.out.printf("修改成功！%n");
				else if (result != null && !result) {
					System.out.printf("修改失败！%n");
				}

			}

			// 询问用户是否继续修改人员，不继续则跳出while循环，程序返回
			System.out.printf("继续修改？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 删除人员
	 */
	public static void deleteUser() {
		while (true) {
			// 获取用户选择的人员
			User salesMan = getChoosedUser();
			if (salesMan != null) {
				System.out.printf("确认删除？(y/n)%n");
				if (IOTools.isChooseYes()) {
					// 删除人员
					boolean result = ud.deleteUser(salesMan.getId());
					if (result)
						System.out.printf("删除成功！%n");
					else if (!result) {
						System.out.printf("删除失败！%n");
					}
				}
			} else {
				System.out.printf("该人员不存在！%n");
			}

			// 询问用户是否继续删除人员，不继续则跳出while循环，程序返回
			System.out.printf("继续删除人员？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 查询人员
	 */
	public static void queryUser() {
		while (true) {
			System.out.printf("请输入姓名");
			String name = IOTools.getInputString();
			// 在数据库中查询该人员是否存在
			List<User> salesManList = ud.queryUserByName(name, true);
			if (salesManList != null && salesManList.size() > 0) {
				// 人员存在
				for (User salesMan : salesManList) {
					System.out.printf("%s%n", salesMan);
				}
			} else {
				System.out.printf("该人员不存在%n");
			}
			// 询问用户是否继续查询人员，不继续则跳出while循环，程序返回
			System.out.printf("继续查询？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 列出所有人员
	 */
	public static void displayUser(UserType userType) {
		while (true) {
			System.out.printf("请选择排列方式!%n");
			System.out.printf("0.ID-升序(默认)%n");
			System.out.printf("1.ID-降序%n");
			System.out.printf("2.姓名-升序%n");
			System.out.printf("3.姓名-降序%n");
			List<User> salesManList = null;
			int i = IOTools.getInputInt();
			switch (i) {
			case 0:
				salesManList = ud.queryAllUser(UserProperty.ID, true, userType);
				break;
			case 1:
				salesManList = ud
						.queryAllUser(UserProperty.ID, false, userType);
				break;
			case 2:
				// 姓名-升序
				salesManList = ud.queryAllUser(UserProperty.NAME, true,
						userType);
				break;
			case 3:
				// 姓名-降序
				salesManList = ud.queryAllUser(UserProperty.NAME, false,
						userType);
				break;
			default:
				System.err.println("该选项不存在！！");
				break;
			}
			if (salesManList.size() > 0) {
				for (User salesMan : salesManList) {
					System.out.printf("%s%n", salesMan);
				}
			}
			// 询问用户是否继续修改人员，不继续则跳出while循环，程序返回
			System.out.printf("重新排序？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}
}
