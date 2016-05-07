package page;

import tools.IOTools;

/**
 * @author lenovo 程序主界面，根据用户选择将其导向相应功能模块
 */
public class MainPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainPage.mainPage();

	}

	/**
	 * 输出主界面
	 */
	private static void outputBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("欢迎进入超市管理系统!");
		IOTools.outputWithTabs("1.商品管理");
		IOTools.outputWithTabs("2.购物收银");
		IOTools.outputWithTabs("3.超市管理");
		IOTools.outputWithTabs("0.退出");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}

	public static void mainPage() {
		// 不断循环，直到用户选择退出
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
				GoodsMgmtPage.mainPage();// 商品管理模块
				break;
			case 2:
				CashierPage.mainPage();// 购物收银
				break;
			case 3:
				MarketMgmtPage.mainPage();// 超市管理
				break;
			default:
				System.err.println("输入不合法，请重新输入！");
				break;
			}
		}

	}

}
