package page;

import java.util.List;

import tools.IOTools;
import dao.GoodsDao;
import entity.Goods;
import entity.GoodsProperty;

/**
 * @author lenovo 商品管理界面及功能
 */
public class GoodsMgmtPage {

	/**
	 * 用于单元测试
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GoodsMgmtPage().mainPage();

	}

	static GoodsDao gd = new GoodsDao();

	/**
	 * 输出主界面
	 */
	private static void outputBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("正在进行商品管理!");
		IOTools.outputWithTabs("1.添加");
		IOTools.outputWithTabs("2.修改");
		IOTools.outputWithTabs("3.删除");
		IOTools.outputWithTabs("4.查询");
		IOTools.outputWithTabs("5.列出所有商品");
		IOTools.outputWithTabs("0.返回");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}

	/**
	 * 商品管理主菜单
	 */
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
				addGoods();// 添加商品
				break;
			case 2:
				modifyGoods();// 修改商品
				break;
			case 3:
				deleteGoods();// 删除商品
				break;
			case 4:
				queryGoods();// 查询商品
				break;
			case 5:
				displayAllGoods();// 列出所有商品
				break;
			default:
				System.err.println("该选项不存在！");
				break;
			}
		}
	}

	/**
	 * 添加商品
	 */
	public static void addGoods() {
		// 不断循环，直到用户选择退出
		while (true) {
			System.out.printf("请输入商品名称%n");
			String name = IOTools.getInputString();
			// 到数据库中查询，该名称是否已经存在
			List<Goods> goodsList = gd.queryGoodsByProperty(GoodsProperty.NAME,
					name);
			if (goodsList != null && goodsList.size() > 0) {
				// 商品存在
				System.out.printf("该商品已经存在，无法添加！%n");
				for (Goods goods : goodsList) {
					System.out.printf("%s%n", goods);
				}
			} else {

				System.out.printf("请输入商品价格%n");
				float price = IOTools.getInputFloat();
				System.out.printf("请输入商品数量%n");
				int number = IOTools.getInputInt();
				// 添加到数据库前确认
				System.out.printf("确定添加？(y/n)%n");
				if (IOTools.isChooseYes()) {
					// 在数据库中添加商品
					boolean result = gd
							.addGoods(new Goods(name, price, number));
					if (result)
						System.out.printf("添加商品成功！%n");
					else {
						System.out.printf("添加商品失败！%n");
					}
				}
			}

			// 询问用户是否继续添加商品，不继续则跳出while循环，程序返回
			System.out.printf("继续添加商品？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
		// 返回主界面
	}

	/**
	 * 修改商品
	 */
	public static void modifyGoods() {
		while (true) {
			System.out.printf("请输入商品名称%n");
			String name = IOTools.getInputString();
			// 在数据库中查询该名称是否存在
			List<Goods> goodsList = gd.queryGoodsByProperty(GoodsProperty.NAME,
					name);
			if (goodsList != null && goodsList.size() > 0) {
				// 商品存在
				System.out.printf("请选择修改内容!%n");
				System.out.printf("1.名称%n");
				System.out.printf("2.价格%n");
				System.out.printf("3.数量%n");
				System.out.printf("0.返回%n");
				Boolean result = null;
				Goods goods = goodsList.get(0);
				int i = IOTools.getInputInt();
				switch (i) {
				case 0:
					break;
				case 1:
					// 名称
					System.out.printf("请输入新名称");
					String newName = IOTools.getInputString();
					goods.setName(newName);
					result = gd.modifyGoods(GoodsProperty.NAME, goods);
					// 更新名称
					break;
				case 2:
					// 价格
					System.out.printf("请输入新价格");
					float newPrice = IOTools.getInputFloat();
					goods.setPrice(newPrice);
					result = gd.modifyGoods(GoodsProperty.PRICE, goods);
					break;
				case 3:
					// 数量
					System.out.printf("请输入新数量");
					int newNumber = IOTools.getInputInt();
					goods.setNumber(newNumber);
					result = gd.modifyGoods(GoodsProperty.NUMBER, goods);
					// 更新数量
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
			} else {
				System.out.printf("该商品不存在！%n");
			}

			// 询问用户是否继续修改商品，不继续则跳出while循环，程序返回
			System.out.printf("继续修改商品？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 删除商品
	 */
	public static void deleteGoods() {
		while (true) {
			System.out.printf("请输入商品名称%n");
			String name = IOTools.getInputString();
			// 在数据库中查询该名称是否存在
			List<Goods> goodsList = gd.queryGoodsByProperty(GoodsProperty.NAME,
					name);
			if (goodsList != null && goodsList.size() > 0) {
				// 商品存在
				System.out.printf("商品已经存在，信息如下：%n");
				// 列出商品信息
				Goods goods = goodsList.get(0);
				System.out.printf("%s%n", goods);
				System.out.printf("确认删除？(y/n)%n");
				if (IOTools.isChooseYes()) {
					// 删除商品
					boolean result = gd.deleteGoods(goods.getId());
					if (result)
						System.out.printf("删除商品成功！%n");
					else if (!result) {
						System.out.printf("删除商品失败！%n");
					}
				}
			} else {
				System.out.printf("该商品不存在！%n");

			}

			// 询问用户是否继续删除商品，不继续则跳出while循环，程序返回
			System.out.printf("继续删除商品？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 查询商品
	 */
	public static void queryGoods() {
		while (true) {
			System.out.printf("请选择查询方式!%n");
			System.out.printf("1.名称(模糊查询)%n");
			System.out.printf("2.价格%n");
			System.out.printf("3.数量%n");
			System.out.printf("0.返回%n");
			List<Goods> goodsList = null;
			int i = IOTools.getInputInt();
			switch (i) {
			case 0:
				break;
			case 1:
				// 名称
				System.out.printf("请输入名称");
				String name = IOTools.getInputString();
				// 在数据库中查询该商品是否存在
				goodsList = gd.queryGoodsByProperty(GoodsProperty.NAME, name);
				if (goodsList != null && goodsList.size() > 0) {
					// 商品存在
					for (Goods goods : goodsList) {
						System.out.printf("%s%n", goods);
					}
				} else {
					System.out.printf("该商品不存在%n");
				}
				// 根据名称查询
				break;
			case 2:
				// 价格
				System.out.printf("请输入价格");
				float price = IOTools.getInputFloat();
				// 在数据库中查询该商品是否存在
				goodsList = gd.queryGoodsByProperty(GoodsProperty.PRICE, price);
				if (goodsList != null && goodsList.size() > 0) {
					// 商品存在
					for (Goods goods : goodsList) {
						System.out.printf("%s%n", goods);
					}
				} else {
					System.out.printf("该商品不存在%n");
				}
				// 根据价格查询
				break;
			case 3:
				// 数量
				System.out.printf("请输入数量");
				int number = IOTools.getInputInt();
				// 在数据库中查询该商品是否存在
				goodsList = gd.queryGoodsByProperty(GoodsProperty.NUMBER,
						number);
				if (goodsList != null && goodsList.size() > 0) {
					// 商品存在
					for (Goods goods : goodsList) {
						System.out.printf("%s%n", goods);
					}
				} else {
					System.out.printf("该商品不存在%n");
				}
				// 根据数量查询
				break;
			default:
				System.err.println("该选项不存在！！");
				break;
			}
			// 询问用户是否继续查询商品，不继续则跳出while循环，程序返回
			System.out.printf("继续查询商品？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

	/**
	 * 列出所有商品
	 */
	public static void displayAllGoods() {
		while (true) {
			System.out.printf("请选择排列方式!%n");
			System.out.printf("0.ID-升序(默认)%n");
			System.out.printf("1.ID-降序%n");
			System.out.printf("2.名称-升序%n");
			System.out.printf("3.名称-降序%n");
			System.out.printf("4.价格-升序%n");
			System.out.printf("5.价格-降序%n");
			System.out.printf("6.数量-升序%n");
			System.out.printf("7.数量-降序%n");
			List<Goods> goodsList = null;
			int i = IOTools.getInputInt();
			switch (i) {
			case 0:
				goodsList = gd.queryAllGoods(GoodsProperty.ID, true);
				break;
			case 1:
				goodsList = gd.queryAllGoods(GoodsProperty.ID, false);
				break;
			case 2:
				// 名称-升序
				goodsList = gd.queryAllGoods(GoodsProperty.NAME, true);
				break;
			case 3:
				// 名称-降序
				goodsList = gd.queryAllGoods(GoodsProperty.NAME, false);
				break;
			case 4:
				// 价格-升序
				goodsList = gd.queryAllGoods(GoodsProperty.PRICE, true);
				break;
			case 5:
				// 价格-降序
				goodsList = gd.queryAllGoods(GoodsProperty.PRICE, false);
				break;
			case 6:
				// 数量-升序
				goodsList = gd.queryAllGoods(GoodsProperty.NUMBER, true);
				break;
			case 7:
				// 数量-降序
				goodsList = gd.queryAllGoods(GoodsProperty.NUMBER, false);
				break;
			default:
				System.err.println("该选项不存在！！");
				break;
			}
			if (goodsList.size() > 0) {
				for (Goods goods : goodsList) {
					System.out.printf("%s%n", goods);
				}
			}
			// 询问用户是否继续修改商品，不继续则跳出while循环，程序返回
			System.out.printf("继续展示所有商品？(y/n)%n");
			if (!IOTools.isChooseYes())
				break;
		}
	}

}
