package org.kx.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/3/3
 */

public class HttpAgent {

    private static final transient Logger logger = LoggerFactory
            .getLogger(HttpAgent.class);

    private static final int GET_MAX_LENGTH = 2048;

    public String send(String serviceURL, Map<String, String> param,
                       boolean isNeedUrlEncode, boolean post) throws HttpAgentException {
        HttpAgentResult result;
        if (post) {
            result = executePost(serviceURL, param, isNeedUrlEncode);
        } else {
            result = executeGet(serviceURL, param, isNeedUrlEncode);
        }
        if (null != result) {
            return result.getResponseBodyString();
        }
        throw new HttpAgentException("result 为空");
    }

    public int getResponseCode(String serviceURL, Map<String, String> param,
                       boolean isNeedUrlEncode, boolean post) throws HttpAgentException {
        HttpAgentResult result;
        if (post) {
            result = executePost(serviceURL, param, isNeedUrlEncode);
        } else {
            result = executeGet(serviceURL, param, isNeedUrlEncode);
        }
        if (null != result) {
            return result.getStatusCode();
        }
        throw new HttpAgentException("result 为空");
    }


    /**
     * 执行get方式的HTTP请求
     */
    public HttpAgentResult executeGet(String url, Map<String, String> params,
                                      boolean isNeedEncode) throws HttpAgentException {
        if (url == null) {
            throw new HttpAgentException("URL cann't be null.");
        }
        // 下文log info url用
        String getUrl = null;
        HttpClient httpClient = PoolingClientConnectionManagerFactory
                .getHttpClient();
        getUrl = assembleUrlTarget(url, params, isNeedEncode);
        if (getUrl.length() >= GET_MAX_LENGTH) {
            throw new HttpAgentException("HTTP get URL too long.");
        }
        HttpGet httpGet = null;
        HttpAgentResult tbr = new HttpAgentResult();
        long start = System.currentTimeMillis();
        try {
            httpGet = new HttpGet(getUrl);
            // 设置为不需要cookie
            httpGet.getParams().setParameter("http.protocol.cookie-policy",
                    CookiePolicy.IGNORE_COOKIES);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            tbr.setStatusCode(response.getStatusLine().getStatusCode());
            if (null != entity) {
                String str = EntityUtils.toString(entity);
                tbr.setResponseBodyString(str);
            }
            return tbr;
        } catch (Exception e) {
            tbr.setResponseBodyString("Error");
            httpGet.abort();
            throw new HttpAgentException(url, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logAlipayResult(getUrl, params, tbr.getResponseBodyString(), cost);
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * 执行POST方式的HTTP请求
     *
     * @return HttpAgentResult
     * @throws HttpAgentException
     */
    public HttpAgentResult executePost(String url, Map<String, String> params,
                                       boolean isNeedEncode) throws HttpAgentException {
        if (url == null) {
            throw new HttpAgentException("URL cann't be null.");
        }
        // 下文log info url用
        String postUrl = url;
        HttpClient httpClient = PoolingClientConnectionManagerFactory
                .getHttpClient();
        postUrl = assembleUrlTarget(postUrl, params, isNeedEncode);

        HttpPost httpPost = null;
        HttpAgentResult tbr = new HttpAgentResult();
        long start = System.currentTimeMillis();
        try {
            httpPost = new HttpPost(postUrl);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            tbr.setStatusCode(response.getStatusLine().getStatusCode());
            if (null != entity) {
                String str = EntityUtils.toString(entity);
                tbr.setResponseBodyString(str);
            }
            return tbr;
        } catch (Exception e) {
            tbr.setResponseBodyString("Error");
            httpPost.abort();
            throw new HttpAgentException(url, e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logAlipayResult(postUrl, params, tbr.getResponseBodyString(), cost);
            httpClient.getConnectionManager().shutdown();
        }
    }

    public String assembleUrlTarget(String url, Map<String, String> params,
                                    boolean isNeedEncode) throws HttpAgentException {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url);
        if (null != params && params.size() > 0) {
            buffer.append("?");
            String str = getParamString(params, isNeedEncode);
            buffer.append(str);
        }
        return buffer.toString();
    }

    private String getParamString(Map<String, String> params,
                                  boolean isNeedEncode) throws HttpAgentException {
        if (params.isEmpty()) {
            return "";
        }
        TreeMap<String, String> treemap = new TreeMap<String, String>(params);
        String charset = treemap.get("_input_charset");
        if (null == charset) {
            charset = "UTF-8";
        }
        StringBuffer content = new StringBuffer();
        for (String key : treemap.keySet()) {
            String value = treemap.get(key);
            if (!StringUtils.isEmpty(value)) {
                // value = StringUtil.defaultIfNull(value);
                if (isNeedEncode) {
                    value = encode(value, charset);
                }
                content.append(key + "=" + value);
                if (!key.equals(treemap.lastKey())) {
                    content.append("&");
                }
            }
        }

        return content.toString();
    }

    protected String encode(String s, String charset) throws HttpAgentException {
        String ret = s;
        try {
            ret = URLEncoder.encode(s, charset);
        } catch (Exception e) {
            throw new HttpAgentException("URL encoder 出错", e);
        }
        return ret;
    }

    private void logAlipayResult(String requestUrl, Map<String, String> params,
                                 String responseBody, long time) {
        StringBuilder sb = new StringBuilder();
        String alipayService = params.get("service");
        sb.append("\n");
        sb.append("********** Request and Response *********\n");
        sb.append("Service Name:\n");
        sb.append("    " + alipayService + "\n");
        sb.append("Request URL:\n");
        sb.append("    " + requestUrl + "\n");
        sb.append("Response XML:\n");
        sb.append("    " + responseBody + "\n");
        sb.append("*******************************************");
        sb.append("   cost " + time + " seconds");
        logger.info(sb.toString());
    }
    /**
     *     public static void main(String[] args) throws IOException, HttpAgentException {
     *         String ip ="11.163.109.204" ;
     *         String url ="xxxx";
     *         int port =5168;
     *         Map<String, String> param = new HashMap<>();
     *
     *
     *         new HttpAgent().send(url,param,false,false);
     *
     *     }
     */
}
