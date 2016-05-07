package page;

import java.util.List;

import tools.IOTools;
import dao.GoodsOrderDao;
import dao.GoodsOrderDetailDao;
import dao.UserDao;
import entity.GoodsOrder;
import entity.GoodsOrderProperty;
import entity.User;
import entity.UserType;

public class MarketMgmtPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarketMgmtPage.mainPage();
	}

	private static UserDao ud = new UserDao();
	private static GoodsOrderDao god = new GoodsOrderDao();
	private static User currentManager;

	public static void LoginPage() {
		Boolean result = LoginPage.mainPage(3);// 最多是错3次
		if (result == null) {
			return;
		} else if (!result) {
			System.out.printf("登录失败！%n");
		} else {
			currentManager = LoginPage.getLoginUser();
			System.out.printf("欢迎你，%s%n", currentManager.getName());
			mainPage();
		}
	}

	/**
	 * 超市管理主页面
	 */
	public static void mainPage() {
		System.out.printf("请使用管理员账号登陆！%n");
		Boolean result = LoginPage.mainPage(3);// 最多是错3次
		if (result == null) {
			return;
		} else if (!result) {
			System.out.printf("登录失败，退出收银台%n");
		} else {
			currentManager = LoginPage.getLoginUser();
			if (currentManager.getUserType() != UserType.MANAGER) {
				System.out.printf("你的权限不够，请使用管理员账号登陆！%n");
				return;
			}
			System.out.printf("欢迎你，%s%n", currentManager.getName());
			while (true) {
				outputBanner();
				System.out.printf("请选择:%n");
				int i = IOTools.getInputInt();
				switch (i) {
				case 0:
					System.out.printf("再见!%n");
					System.exit(0);
					break;
				case 1:
					userManage();// 人员管理
					break;
				case 2:
					goodsOrderManage();// 订单管理
					break;
				case 3:
					goodsOrderDetailManage();// 订单管理
					break;
				default:
					System.err.println("输入不合法，请重新输入！");
					break;
				}
			}
		}
	}

	private static void userManage() {
		UserMgmtPage.mainPage();
	}

	private static void goodsOrderManage() {
		displayAllOrder();
	}

	private static void goodsOrderDetailManage() {
		System.out.printf("请输入订单编号%n");
		int i = IOTools.getInputInt();
		new GoodsOrderDetailDao().queryGoodsOrderDetail(i);
	}

	private static void displayAllOrder() {
		while (true) {
			System.out.printf("请选择排列方式!%n");
			System.out.printf("0.ID-升序%n");
			System.out.printf("1.ID-降序%n");
			System.out.printf("2.售货员ID-升序%n");
			System.out.printf("3.售货员ID-降序%n");
			System.out.printf("4.价格-升序%n");
			System.out.printf("5.价格-降序%n");
			System.out.printf("6.日期-升序%n");
			System.out.printf("7.日期-降序%n");
			List<GoodsOrder> goodsOrderList = null;
			int i = IOTools.getInputInt();
			switch (i) {
			case 0:
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.GOID, true);
				break;
			case 1:
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.GOID, false);
				break;
			case 2:
				// 姓名-升序
				goodsOrderList = god.queryAllGoodsOrder(GoodsOrderProperty.SID,
						true);
				break;
			case 3:
				// 姓名-降序
				goodsOrderList = god.queryAllGoodsOrder(GoodsOrderProperty.SID,
						false);
				break;
			case 4:
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.PRICE, true);
				break;
			case 5:
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.PRICE, false);
				break;
			case 6:
				// 姓名-升序
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.DATE, true);
				break;
			case 7:
				// 姓名-降序
				goodsOrderList = god.queryAllGoodsOrder(
						GoodsOrderProperty.DATE, false);
				break;
			default:
				System.err.println("该选项不存在！！");
				break;
			}
			if (goodsOrderList.size() > 0) {
				for (GoodsOrder goodsOrder : goodsOrderList) {
					System.out.printf("%s%n", goodsOrder);
				}
			}
			// 询问用户是否，不继续则跳出while循环，程序返回
			System.out.printf("重新排序？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 输出主界面
	 */
	private static void outputBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("超市管理");
		IOTools.outputWithTabs("1.人员管理");
		IOTools.outputWithTabs("2.订单管理");
		IOTools.outputWithTabs("3.详单管理");
		IOTools.outputWithTabs("0.退出");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}

}
