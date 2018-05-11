package cc.darhao.farm.app;

import java.io.IOException;

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import cc.darhao.dautils.api.ResourcesUtil;
import cc.darhao.farm.controller.MainController;
import cc.darhao.farm.thread.OnlineThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	private MainController mainController;
	
	public static String MODE = OnlineThread.UPDATE_MODE == 0 ? "Jafka模式" : "传统模式";
	
	public static String TITLE = "农产品称重端 - " + MODE + " - ";
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(ResourcesUtil.getResourceURL("fxml/app.fxml"));
		Parent root = loader.load();
		//把Stage存入MainController
		mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);
        //显示
        primaryStage.setTitle(TITLE + "请先登录");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}
	
	
	/**
	 * 程序入口
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(ResourcesUtil.getResourceAsStream("log4j/log4j.xml"));
			Configurator.initialize(null, source);   
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	
	@Override
	public void stop() throws Exception {
		System.exit(0);
	}
	
}
