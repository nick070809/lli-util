package org.kx.util.base;


import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * @author sunkx
 */
public class HttpsClient  {
	private final static String APPLICATION_JSON = "application/json";
	private final static  String CONTENT_TYPE_TEXT_JSON = "text/json";
	private static Logger logger = LoggerFactory
			.getLogger(HttpsClient.class);
	private static DefaultHttpClient client;

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	};

	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry()
					.register(https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static String sendModel(String url, String body,Map<String,String> headers) {
		if (client == null) {
			client = new DefaultHttpClient();
			enableSSL(client);
		}
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(5 * 60000)
					.setConnectionRequestTimeout(5 * 60000)
					.setSocketTimeout(5 * 60000).build();
			HttpPost httppost = new HttpPost(url);
			httppost.setConfig(requestConfig);
			if(headers !=null && !headers.isEmpty()){
				for (String key : headers.keySet()) {
					httppost.setHeader(key,headers.get(key));
				}
			}

				httppost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
				StringEntity se = new StringEntity(body, "utf-8");
				se.setContentType(CONTENT_TYPE_TEXT_JSON);
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						APPLICATION_JSON));
				httppost.setEntity(se);

			CloseableHttpResponse response = client.execute(httppost);
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
			logger.error("http连接失败", e);
			throw new BaseException(BaseErrorCode.SystemError, "http连接失败");
		} finally {
			// 关闭连接,释放资源
			// client.close();
		}

	}
}