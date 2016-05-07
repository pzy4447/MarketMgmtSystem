package tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String currentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String dateTime = df.format(new Date());// 获取当前系统时间
		return dateTime;
	}

}
