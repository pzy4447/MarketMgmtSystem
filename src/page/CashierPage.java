package page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.DateHelper;
import tools.IOTools;
import tools.SqlHelper;
import dao.GoodsDao;
import entity.Goods;
import entity.GoodsProperty;
import entity.User;
import entity.UserType;

public class CashierPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CashierPage.mainPage();

		/*
		 * float sum = 17.0001f; double sum1 = 1.02; // String sql =
		 * String.format("Hi %s", "111");// String str = String.format("Hi, %f",
		 * sum1); // DateHelper.currentDateTime()
		 * 
		 * System.out.printf("%s%n", str);
		 */
	}

	private static User currentSalesMan;
	private static GoodsDao gd = new GoodsDao();

	public static void mainPage() {
		outputBanner();
		System.out.printf("请使用售货员账号登陆！%n");
		Boolean result = LoginPage.mainPage(3);// 最多是错3次
		if (result == null) {
			return;
		} else if (!result) {
			System.out.printf("登录失败，退出收银台%n");
		} else {
			currentSalesMan = LoginPage.getLoginUser();
			if (currentSalesMan.getUserType() != UserType.SALESMAN) {
				System.out.printf("你的权限不够，请使用售货员账号登陆！%n");
				return;
			}
			System.out.printf("欢迎你，%s%n", currentSalesMan.getName());
			cashierPage();
		}
	}

	/**
	 * 输出主界面
	 */
	private static void outputBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("正在进行购物收银!");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}

	private static boolean isNeedPay;// 用于指示是否已经将商品结算完毕，需要进行支付

	/**
	 * 收银过程
	 */
	private static void doCashier() {
		float sum = 0;
		List<Goods> boughtGoodsList = new ArrayList<Goods>();
		Map<Integer, Integer> goodsBuyNumberMap = new HashMap<Integer, Integer>();
		while (true) {
			if (isNeedPay)
				break;
			Goods goods = getChoosedGoods();
			// 用户选中了商品
			if (goods != null) {
				System.out.printf("商品信息： %s%n", goods);
				System.out.printf("请输入购买数量%n");
				int buyNumber = IOTools.getInputInt();
				int goodsNumber = goods.getNumber();
				if (buyNumber > goodsNumber) {
					System.out.printf("余货不足，购买失败-_-!%n");
					continue;
				}
				// 记录每个商品的购买数量
				goodsBuyNumberMap.put(goods.getId(), buyNumber);
				goods.setNumber(goodsNumber - buyNumber);
				boughtGoodsList.add(goods);
				sum += (float) Math.round((goods.getPrice() * buyNumber * 10)) / 10;
				System.out.printf("已添加，当前总额为%f%n", sum);
			}
		}
		// 进入支付环节
		if (boughtGoodsList.size() == 0) {
			System.out.printf("您没有购买，无需支付%n");
			return;
		}
		System.out.printf("您的消费总额为%f%n", sum);

		System.out.printf("请输入支付金额%n");
		float payNum = IOTools.getInputFloat();
		if (payNum < sum) {
			System.out.printf("支付金额不足，购买失败-_-!%n");
		} else {
			Integer maxId = SqlHelper.queryMaxValue("GOID", "GOODS_ORDER");
			if (maxId == null) {
				System.err.printf("无法确定订单号，无法插入详单！%n");
				System.out.printf("支付失败@_@！%n");
				return;
			}
			// 录入数据库
			ArrayList<String> sqlList = new ArrayList<String>();
			// 更新商品表的商品数量
			for (Goods goods : boughtGoodsList) {
				String sql = String.format(
						"UPDATE GOODS SET GNUMBER = %d WHERE GID = %d",
						goods.getNumber(), goods.getId(), goods.getNumber());
				sqlList.add(sql);
			}
			// 插入新订单
			String sql = String.format(
					"INSERT INTO GOODS_ORDER(SID,PRICE,DATE) VALUES(%d,%f,%s)",
					currentSalesMan.getId(), sum,
					"\"" + DateHelper.currentDateTime() + "\"");
			sqlList.add(sql);
			// 插入新详单
			// 更新商品表的商品数量
			for (Goods goods : boughtGoodsList) {
				String sqlGoodsOrderDetail = String
						.format("INSERT INTO GOODS_ORDER_DETAIL(GOID,GID,NUMBER,PRICE) VALUES(%d, %d,%d,%f)",
								maxId, goods.getId(),
								goodsBuyNumberMap.get(goods.getId()),
								goods.getPrice());
				sqlList.add(sqlGoodsOrderDetail);
			}
			try {
				SqlHelper.executeBatch(sqlList);
				SqlHelper.commit();
				float change = payNum - sum;
				System.out.printf("购买完毕，您的找零为%f，欢迎再次光临！%n", change);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.printf("结算时出错，购买失败！%n");
				e.printStackTrace();
				try {
					SqlHelper.rollback();
					System.out.printf("数据库已回滚%n");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.err.printf("数据库回滚失败%n");
					e1.printStackTrace();
				}
			}

		}

	}

	/**
	 * 通过交互，获取用户选定的待操作的商品
	 * 
	 * @return 用户选定的商品
	 */
	private static Goods getChoosedGoods() {
		// 初始化该值，用户选择进入支付时置为true
		isNeedPay = false;
		Goods goods = null;
		System.out.printf("请输入商品名称(输入zf进入支付)%n");
		String name = IOTools.getInputString();
		if (name.toLowerCase().equals("zf")) {
			isNeedPay = true;
			return null;
		}

		// 首先在数据库中精确匹配姓名
		List<Goods> goodsList = gd.queryGoodsByProperty(GoodsProperty.NAME,
				name);
		// 匹配成功
		if (goodsList != null && goodsList.size() == 1) {
			goods = goodsList.get(0);
			// 商品存在
			System.out.printf("该商品存在，信息如下：%n");
			System.out.printf("%s%n", goods);
		} else {
			// 精确匹配不到则模糊匹配
			goodsList = gd.fuzzyQueryByName(name);
			int size = goodsList.size();

			if (goodsList != null && size == 0) {
				System.out.printf("该商品不存在！%n");
			} else {// 匹配到的内容由用户选择
				for (int i = 0; i < goodsList.size(); i++) {
					System.out.printf("%d : %s%n", i + 1, goodsList.get(i));
				}
				while (true) {
					System.out.printf("请选择商品，输入0取消%n");
					int index = IOTools.getInputInt();
					if (index < 0 || index >= size + 1) {
						System.out.printf("该序号不存在!%n");
						continue;
					} else if (index == 0) {
						break;
					} else {
						goods = goodsList.get(index - 1);// 显示序号时，加了个1，这里进行补偿
						break;
					}
				}
			}
		}
		return goods;
	}

	/**
	 * 收银界面
	 */
	public static void cashierPage() {
		outputCashierBanner();
		System.out.printf("开始收银？(y/n)%n");
		if (IOTools.isChooseYes()) {
			// System.out.printf("收银结束%n");
			doCashier();
		} else {
			System.out.printf("慢走不送！%n");
		}
	}

	/**
	 * 输出主界面
	 */
	private static void outputCashierBanner() {
		IOTools.outputBannerLine();// 输出banner中首行的横线
		IOTools.outputWithTabs("这里是收银台，售货员[" + currentSalesMan.getName()
				+ "]为您服务");
		IOTools.outputBannerLine();// 输出banner中末行的横线
	}
}