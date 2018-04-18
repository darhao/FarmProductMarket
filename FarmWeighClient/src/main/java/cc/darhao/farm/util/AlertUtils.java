package cc.darhao.farm.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * 弹出框工具
 * <br>
 * <b>2018年4月18日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class AlertUtils {

	public static void show(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(string);
		alert.show();
	}

	
	public static void error(String string) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText(string);
		alert.show();
	}
	
}
