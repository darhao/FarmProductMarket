package cc.darhao.farm.util;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求工具类
 * <br>
 * <b>2018年4月18日</b>
 * @author 沫熊工作室 <a href="http://www.darhao.cc">www.darhao.cc</a>
 */
public class HttpUtils {

	public static Logger logger = LogManager.getRootLogger();
	
	
	public interface MyCallback{
		void onFailed();
		void onSucceed(String data);
	}
	
	
	/**
	 * 发送异步Http请求
	 * @param path 绝对地址
	 * @param postBody post体
	 * @param myCallback 回调接口
	 */
	public static void asynRequest(String path, Map<String, String> postBody, MyCallback myCallback) {
		//解析postBody
		FormBody.Builder formBody = new FormBody.Builder();
		if(postBody != null) {
			for (Entry<String, String> entry : postBody.entrySet()) {
				formBody.add(entry.getKey(), entry.getValue());
			}
		}
		//异步请求
		OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder()
	            .url(path)
	            .post(formBody.build())
	            .build();
	    client.newCall(request).enqueue(new Callback() {
	        @Override
	        public void onFailure(Call call, IOException e) {
	        	logger.error(e.getMessage());
				e.printStackTrace();
				Platform.runLater(() -> {
					myCallback.onFailed();
				});
	        }
	        @Override
	        public void onResponse(Call call, Response response) throws IOException {
	        	if(response.isSuccessful()) {
	                String data = response.body().string();
	                Platform.runLater(() -> {
						myCallback.onSucceed(data);
					});
	            }
	        }
	    });
	}
	
	
	
}
