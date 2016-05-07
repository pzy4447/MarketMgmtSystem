package tools;

import java.util.Scanner;

/**
 * @author lenovo
 *
 */
public class IOTools {

	/**
	 * 菜单项前面的制表符
	 */
	private static final String menuItemTabs = "\t";

	/**
	 * 在前面加上制表符后输出
	 */
	public static void outputWithTabs(String content) {
		System.out.printf("%s%s%n", menuItemTabs, content);
	}

	/**
	 * 输出banner中首行和末行的横线
	 */
	public static void outputBannerLine() {
		System.out.printf("========================================%n");// 20字符
	}

	/**
	 * @return 用户输入的字符串，如果输入了多个单词则只获取首单词
	 */
	public static String getInputString() {
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		return input;
	}

	/**
	 * @return 用户输入的整数
	 */
	public static Integer getInputInt() {
		Integer input;
		while (true) {
			Scanner sc = new Scanner(System.in);
			try {
				input = sc.nextInt();
				break;
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("输入不合法，请重新输入！");
			}
		}
		return input;
	}

	/**
	 * @return 用户输入的浮点数
	 */
	public static float getInputFloat() {
		Float input ;
		while (true) {
			Scanner sc = new Scanner(System.in);
			try {
				input = sc.nextFloat();
				break;
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("输入不合法，请重新输入！");
			}
		}
		return input;
	}
	
	/**
	 * @return 判断用户是否选择yes，用户输入错误则使其继续输入，直到输入合法
	 */
	public static boolean isChooseYes() {
		boolean isContinue = false;
		String choice = "";
		while (true) {
			choice = IOTools.getInputString();
			if (choice.matches("\\b[y,Y,n,N]\\b"))
				break;
			else {
				System.err.println("输入不合法，请重新输入！");
			}
		}
		// 如果用户选择n，则跳出while不再继续
		if (choice.matches("\\b[y,Y]\\b"))
			isContinue = true;
		return isContinue;
	}

}
