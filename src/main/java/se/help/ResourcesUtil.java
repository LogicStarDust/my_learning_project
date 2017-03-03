package se.help;

import java.util.ResourceBundle;

/**
 * 
 * @author 刘宪领
 * @date 2016-8-15
 * @Time 下午6:36:36
 */
public class ResourcesUtil {
	private static ResourceBundle config;
	static {
		config = ResourceBundle.getBundle("config");
	}

	public static String getVal(String key) {
		String value = config.getString(key);
		// 去除多余的空格
		if (value != null)
			return value.trim();
		return value;
	}
}
