package org.kx.util.base;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * httpclient http
 *
 * @author sunkx
 */
public class HttpClient {

    private final static String APPLICATION_JSON = "application/json";
    private final static String CONTENT_TYPE_TEXT_JSON = "text/json";
    private static Logger logger = LoggerFactory.getLogger(HttpClient.class);


    /**
     * post(json|from)
     */
    public static String sendModel(String url, String to, Map<String, String> headers) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5 * 60000).setConnectionRequestTimeout(5 * 60000)
                    .setSocketTimeout(5 * 60000).build();
            HttpPost httppost = new HttpPost(url);
            httppost.setConfig(requestConfig);
            httppost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity se = new StringEntity(to, "utf-8");
            se.setContentType(CONTENT_TYPE_TEXT_JSON);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));

            if (headers != null && !headers.isEmpty()) {
                for (String key : headers.keySet()) {
                    httppost.setHeader(key, headers.get(key));
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
            logger.error("http连接失败", e);
            throw new BaseException(BaseErrorCode.SystemError, "http连接失败");
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void upload(String url, String localPath) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

        File file = new File(localPath);


        multipartEntityBuilder.addBinaryBody("file", file);
        multipartEntityBuilder.addTextBody("comment", "this is comment");
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);

        httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();

            String line; // 用来保存每行读取的内容
            line = reader.readLine(); // 读取第一行
            while (line != null) { // 如果 line 为空说明读完了
                buffer.append(line); // 将读到的内容添加到 buffer 中
                buffer.append("\n"); // 添加换行符
                line = reader.readLine(); // 读取下一行
            }

            System.out.println(buffer.toString());
        }

        httpClient.close();
        if (httpResponse != null) {
            httpResponse.close();
        }
    }
}