package org.kx.util.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2021/3/3
 */

public class PoolingClientConnectionManagerFactory {

    private static SchemeRegistry schemeRegistry;

    private static ClientConnectionManager cm;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 200;

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 20;

    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 5000;

    /**
     * 读取超时时间
     */
    public final static int                READ_TIMEOUT          = 10000;


    public static HttpClient getHttpClient() {
        return getHttpClient(CONNECT_TIMEOUT, READ_TIMEOUT);
    }
    public static HttpClient getHttpClient(int connectionTimeoutMillis, int readTimeoutMillis) {
        HttpClient httpClient = new DefaultHttpClient();
        if(connectionTimeoutMillis <= 0){
            connectionTimeoutMillis = CONNECT_TIMEOUT;
        }
        if(readTimeoutMillis <= 0){
            readTimeoutMillis = READ_TIMEOUT;
        }
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                connectionTimeoutMillis);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeoutMillis);
        return httpClient;
    }


}
