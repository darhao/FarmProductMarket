package cc.darhao.farm.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONArray;

import cc.darhao.farm.app.Main;
import cc.darhao.farm.entity.ProductionProperties;
import cc.darhao.farm.entity.Type;
import cc.darhao.farm.thread.OnlineThread;
import cc.darhao.farm.thread.WeighThread;
import cc.darhao.farm.util.AlertUtils;
import cc.darhao.farm.util.HttpUtils;
import cc.darhao.farm.util.HttpUtils.MyCallback;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * 主页控制器
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class MainController implements Initializable {

	public static Logger logger = LogManager.getRootLogger();
	
	private Stage primaryStage;
	
	@FXML
	private Button loginBt;
	@FXML
	private Button onlineBt;
	@FXML
	private Button weighBt;
	@FXML
	private ChoiceBox typeCb;
	@FXML
	private Label onlineLb;
	@FXML
	private Label weighLb;
	@FXML
	private Label modeLb;
	@FXML
	private PasswordField passwordPf;
	@FXML
	private RadioButton autoRb;
	@FXML
	private RadioButton intervalRb;
	@FXML
	private RadioButton manualRb;
	@FXML
	private Slider speedSd;
	@FXML
	private TableColumn createTimeCol;
	@FXML
	private TableColumn nameCol;
	@FXML
	private TableColumn typeCol;
	@FXML
	private TableColumn weightCol;
	@FXML
	private TableColumn isOnlineCol;
	@FXML
	private TableView productionTb;
	@FXML
	private TextField userTf;
	@FXML
	private TextField nameTf;
	@FXML
	private TextField intervalTf;
	
	//IP地址
	public static final String IP = "39.108.231.15";
	//端口
	public static int PORT = 80;
	//基地址
	public static final String BASE = "http://"+ IP +":"+ PORT + "/FarmProductMarket";
	//表格数据
	private ObservableList<ProductionProperties> productionProperties = FXCollections.observableArrayList();
	
	//共享状态
	public static boolean isLogined = false;
	public static boolean isWeighing = false;
	public static double speed = 0;
	public static int intervalMin = 1;
	public static int onlineMode = 2; // 0自动 ； 1定时； 2手动
	
	//线程句柄
	private OnlineThread onlineThread;
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		initTable();
		initTypeSelector();
		initSlider();
		initIntervalTf();
		initRbs();
	}


	private void initRbs() {
		ChangeListener<Boolean> rbListener = new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(autoRb.isSelected()) {
					onlineMode = 0;
					onlineThread.interrupt();
					modeLb.setText("当期为即时模式");
				}else if(intervalRb.isSelected()){
					onlineMode = 1;
					onlineThread.interrupt();
					modeLb.setText("当期为间隔模式");
				}else if(manualRb.isSelected()) {
					onlineMode = 2;
					onlineThread.interrupt();
					modeLb.setText("当期为手动模式");
				}
			}
		};
		autoRb.selectedProperty().addListener(rbListener);
		intervalRb.selectedProperty().addListener(rbListener);
		manualRb.selectedProperty().addListener(rbListener);
	}


	private void initIntervalTf() {
		intervalTf.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				new Thread(()->{
					try {
						Thread.sleep(2000);
						intervalMin = Integer.parseInt(intervalTf.getText());
					} catch (Exception e1) {
						Platform.runLater(()->{
							intervalTf.setText("1");
						});
					}
					if(intervalRb.isSelected()) {
						onlineThread.interrupt();
					}
				}).start();
			}
		});
	}


	private void initSlider() {
		speedSd.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				speed = speedSd.getValue();
			}
		});
	}


	private void initTypeSelector() {
		HttpUtils.asynRequest(BASE + "/type/list", null, new MyCallback() {
			
			@Override
			public void onSucceed(String data) {
				ObservableList<String> list = FXCollections.observableArrayList();
				List<Type> types = JSONArray.parseArray(data, Type.class);
				for (Type type : types) {
					list.add(type.getName());
				}
				typeCb.setItems(list);
				typeCb.getSelectionModel().select(0);
			}
			
			@Override
			public void onFailed() {
				AlertUtils.error("获取农产品类型列表失败");
			}
		});
	}


	private void initTable() {
		//初始化表格列表
		createTimeCol.setCellValueFactory(new PropertyValueFactory<>("createTime"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		isOnlineCol.setCellValueFactory(new PropertyValueFactory<>("isOnline"));
		productionTb.setItems(productionProperties);
		productionProperties.addListener(new ListChangeListener<ProductionProperties>() {

			private void updateUI(Change<? extends ProductionProperties> c) {
				int onlineSum = 0;
				synchronized (productionProperties) {
					for (ProductionProperties productionProperties2 : productionProperties) {
						if(productionProperties2.getIsOnline().equals("是")) {
							onlineSum++;
						}
					}
				}
				onlineLb.setText(onlineSum + "");
				weighLb.setText(productionProperties.size() + "");
			}
			
			@Override
			public void onChanged(Change<? extends ProductionProperties> c) {
				if(Thread.currentThread().getName().contains("FX")) {
					updateUI(c);
				}else {
					Platform.runLater(() -> {
						updateUI(c);
					});
				}
			}
		});
	}


	public void onClickLogin() {
		if(isLogined == false) {
			Map<String, String> postBody = new HashMap<String, String>();
			postBody.put("name", userTf.getText());
			postBody.put("password", passwordPf.getText());
			HttpUtils.asynRequest(BASE + "/user/login", postBody, new MyCallback() {
				
				@Override
				public void onSucceed(String data) {
					if(data.contains("succeed")) {
						userTf.setDisable(true);
						passwordPf.setDisable(true);
						autoRb.setDisable(false);
						intervalRb.setDisable(false);
						manualRb.setDisable(false);
						intervalTf.setDisable(false);
						onlineBt.setDisable(false);
						loginBt.setText("登出");
						primaryStage.setTitle(Main.TITLE + userTf.getText());
						isLogined = true;
						//开启线程
						onlineThread = new OnlineThread(userTf.getText(), productionProperties);
						onlineThread.start();
					}else {
						AlertUtils.error("用户名或密码错误");
					}
				}
				
				@Override
				public void onFailed() {
					AlertUtils.error("操作失败");
				}
			});
		}else {
			userTf.setDisable(false);
			passwordPf.setDisable(false);
			autoRb.setDisable(true);
			intervalRb.setDisable(true);
			manualRb.setDisable(true);
			intervalTf.setDisable(true);
			onlineBt.setDisable(true);
			loginBt.setText("登录");
			primaryStage.setTitle(Main.TITLE + "请先登录");
			isLogined = false;
			//清空列表
			synchronized (productionProperties) {
				productionProperties.clear();
			}
			onlineLb.setText("0");
			weighLb.setText("0");
		}
	}
	
	
	public void onClickWeigh() {
		if(isWeighing == false) {
			//检验
			if(isLogined == false) {
				AlertUtils.error("必须先登录");
				return;
			}
			if(nameTf.getText().equals("") || nameTf.getText() == null) {
				AlertUtils.error("品名不能为空");
				return;
			}
			//锁定UI
			loginBt.setDisable(true);
			nameTf.setDisable(true);
			typeCb.setDisable(true);
			weighBt.setText("结束称重");
			isWeighing = true;
			//开启线程
			new WeighThread(nameTf.getText(), typeCb.getSelectionModel().getSelectedItem().toString(), productionProperties).start();
		}else {
			//解锁UI
			loginBt.setDisable(false);
			nameTf.setDisable(false);
			typeCb.setDisable(false);
			weighBt.setText("开始称重");
			isWeighing = false;
		}
		
	}


	public void onClickOnline() {
		if(manualRb.isSelected()) {
			onlineThread.interrupt();
		}else {
			AlertUtils.error("当前非手动模式");
		}
	}
	
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	
}
