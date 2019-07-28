package org.kx.util;

import com.lianlianpay.lli.common.BaseErrorCode;
import com.lianlianpay.lli.common.BaseException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * httpclient http
 * @author sunkx
 * 
 */
public class HttpClient{
	
	private  final static String APPLICATION_JSON = "application/json";
	private  final static  String CONTENT_TYPE_TEXT_JSON = "text/json";
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);

	
	/**
	 * post(json|from)
	 */
	public  static String sendModel(String url, String to,Map<String,String> headers) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			RequestConfig requestConfig = RequestConfig.custom()  
			        .setConnectTimeout(5*60000).setConnectionRequestTimeout(5*60000)  
			        .setSocketTimeout(5*60000).build();  
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			httppost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			StringEntity se = new  StringEntity(to, "utf-8");
			se.setContentType(CONTENT_TYPE_TEXT_JSON);
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,APPLICATION_JSON));

			if(headers !=null && !headers.isEmpty()){
				for (String key : headers.keySet()) {
					httppost.setHeader(key,headers.get(key));
				}
			}
			httppost.setEntity(se);
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String responses = EntityUtils.toString(entity, "UTF-8");
					return responses;
				}
				throw new Exception("response entity is null.");
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("http连接失败",e);
			throw new BaseException(BaseErrorCode.SystemError,"http连接失败");
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}