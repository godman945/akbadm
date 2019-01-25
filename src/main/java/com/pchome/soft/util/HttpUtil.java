package com.pchome.soft.util;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

/**
 * httpclient all settings
 * @author supertolove
 */
public class HttpUtil {
    private static final Log log = LogFactory.getLog(HttpUtil.class);
    private static String CHARSET = "UTF-8";
    private static HttpUtil http = new HttpUtil();
    private DefaultHttpClient client;
    private String[] userAgents = new String[] {
        "Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X; en-us) AppleWebKit/531.21.10 (KHTML, like Gecko) Version/4.0.4 Mobile/7B334b Safari/531.21.10",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/5.0; SLCC1; .NET CLR 3.0.%X%; Media Center PC 5.0; .NET CLR",
        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; es-ES; rv:1.9.2.12) Gecko/20101026 Firefox/3.7.12",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; fr; rv:1.9.2.12) Gecko/20101026 Firefox/3.6.12 (.NET CLR 3.6.%X%)",
        "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/544.12 (KHTML, like Gecko) Chrome/9.0.%X%.0 Safari/634.12",
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.%X%)",
        "Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/574.12 (KHTML, like Gecko) Chrome/9.0.587.0 Safari/534.12",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/5.0; .NET CLR 2.0.%X%; .NET CLR 3.0.%X%.2152; .NET CLR 3.5",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Trident/9.0; SLCC1; .NET CLR 2.0.%X%; Media Center PC 5.0; .NET CLR",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/11.0; GTB6.6; InfoPath.1; .NET CLR 2.0.%X%; .NET CLR 3.0.450",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.5) Gecko/%X% Netscape/8.1",
        "Mozilla/4.0 (compatible; MSIE 6.0; MSIE 5.5; Windows NT 5.1) Opera 6.02 [en]",
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.2322; .NET CLR 2.0.50727; .NET CLR 3.0.%X%.30)",
        "Mozilla/5.0 (X11; U; Linux i686 (x86_64); en-US) AppleWebKit/531.0 (KHTML, like Gecko) Chrome/3.0.198.0 Safari/532.0",
        "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-US) AppleWebKit/431.0 (KHTML, like Gecko) Chrome/3.0.183 Safari/531.0"
    };

    private HttpUtil() {
        try {
            configureClient();
        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * get singleton instance
     * @return instance
     */
    public static HttpUtil getInstance() {
        return http;
    }

    /**
     * get default http client, this is thread safe
     * @return client
     */
    public synchronized DefaultHttpClient getClient() {
        return client;
    }

    /**
     * get result of url, this is thread safe
     * @param url url
     * @return url content
     */
    public synchronized String getResult(String url) {
        return getResult(url, null, 0, null, null, 0);
    }

    /**
     * get result of url, this is thread safe
     * @param url url
     * @param charSet encoding
     * @return url content
     */
    public synchronized String getResult(String url, String charSet) {
        return getResult(url, charSet, 0, null, null, 0);
    }

    /**
     * get result of url, this is thread safe
     * @param url url
     * @param charSet encoding
     * @param timeout microsecond
     * @return url content
     */
    public synchronized String getResult(String url, String charSet, int timeout) {
        return getResult(url, charSet, timeout, null, null, 0);
    }

    /**
     * get result of url, this is thread safe
     * @param url url
     * @param charSet encoding
     * @param timeout microsecond
     * @param referer referer url
     * @return url content
     */
    public synchronized String getResult(String url, String charSet, int timeout, String referer) {
        return getResult(url, charSet, timeout, referer, null, 0);
    }

    /**
     * set proxy, then get result of url, this is thread safe
     * @param url url
     * @param charSet encoding, maybe null
     * @param timeout microsecond, maybe null
     * @param referer referer url, maybe null
     * @param hostname proxy host, maybe null
     * @param port proxy port, maybe null
     * @return url content
     */
    public synchronized String getResult(String url, String charSet, int timeout, String referer, String hostname, int port) {
        String result = null;
        HttpGet httpget = null;

        log.debug(url);
        try {
            HttpParams params = client.getParams();

            // timeout
            if (timeout > 0) {
                HttpConnectionParams.setConnectionTimeout(params, timeout);
                HttpConnectionParams.setSoTimeout(params, timeout);
            }

            // proxy
            if (StringUtils.isNotBlank(hostname)) {
                HttpHost httpHost = new HttpHost(hostname, port);
                ConnRouteParams.setDefaultProxy(params, httpHost);
            }

            httpget = new HttpGet(url);

            // referer
            if (StringUtils.isNotBlank(referer)) {
                httpget.setHeader("Referer", referer);
            }

            HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
            response = client.execute(httpget);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
//                if (StringUtils.isBlank(charSet)) {
//                    charSet = getCharset(entity);
//                }
                if (StringUtils.isBlank(charSet)) {
                    result = EntityUtils.toString(entity, CHARSET);
                } else {
                    result = EntityUtils.toString(entity, charSet);
                }
            } else {
                log.info("statusCode=" + statusCode);
            }
        } catch (Exception e) {
            log.error(url, e);
        } finally {
            if (httpget != null) {
                httpget.abort();
            }
            closeExpiredConns();
            closeIdleConns();
        }

        result = StringUtils.defaultIfEmpty(result, "");
        return result;
    }

//    /**
//     * get result of url to file, this is thread safe
//     * @param url url
//     * @param charSet encoding
//     * @param referer referer url
//     * @return path
//     */
//    public synchronized String getPath(String url, String referer) {
//        int statusCode = 0;
//        String path = null;
//
//        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
//        HttpGet httpget = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//        byte[] buffer = null;
//        int len = 0;
//
//        log.debug(url);
//        try {
//            httpget = new HttpGet(url);
//            if (StringUtils.isNotBlank(referer)) {
//                httpget.setHeader("Referer", referer);
//            }
//            response = client.execute(httpget);
//            statusCode = response.getStatusLine().getStatusCode();
//
//            if (statusCode == HttpStatus.SC_OK) {
//                File dir = new File("/tmp/", CHARSET);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                is = response.getEntity().getContent();
//                path = dir.getPath() + "/" + DigestUtils.md5Hex(url);
//                fos = new FileOutputStream(path);
//                buffer = new byte[1024];
//                len = 0;
//                while ((len = is.read(buffer)) > 0) {
//                    fos.write(buffer, 0, len);
//                }
//            } else {
//                log.info("statusCode=" + statusCode);
//            }
//        } catch (Exception e) {
//            log.error(e.toString() + " " + url, e);
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (IOException e) {
//                log.info(url, e);
//            }
//
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException e) {
//                log.info(url, e);
//            }
//
//            if (httpget != null) {
//                httpget.abort();
//            }
//            closeExpiredConns();
//            closeIdleConns();
//        }
//
//        return path;
//    }

//    /**
//     * get byte array from url, this is thread safe
//     * @param url
//     * @return binary
//     */
//    public synchronized byte[] getBytes(String url) {
//        int statusCode = 0;
//        byte[] result = new byte[0];
//
//        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
//        HttpGet httpget = null;
//
//        log.debug(url);
//        try {
//            httpget = new HttpGet(url);
//            response = client.execute(httpget);
//            statusCode = response.getStatusLine().getStatusCode();
//
//            if (statusCode == HttpStatus.SC_OK) {
//                result = EntityUtils.toByteArray(response.getEntity());
//            } else {
//                log.info("statusCode=" + statusCode);
//            }
//        } catch (Exception e) {
//            log.error(e.toString() + " " + url, e);
//        } finally {
//            if (httpget != null) {
//                httpget.abort();
//            }
//            closeExpiredConns();
//            closeIdleConns();
//        }
//
//        return result;
//    }

    /**
     * get random user agent
     * @return user agent
     */
    private String pickUserAgent() {
        double v = Math.random() * userAgents.length;
        return userAgents[(int) v];
    }

//    /**
//     * get charset
//     * @param entity
//     * @return charset
//     */
//    private String getCharset(HttpEntity entity) {
//        Header contentType = entity.getContentType();
////        log.info(contentType);
//        if (contentType == null) {
////            log.info("contentType == null");
//            return null;
//        }
//
//        String[] contents = contentType.getValue().toLowerCase().split(";");
//        if (contents.length < 2) {
////            log.info("contents.length < 2");
//            return null;
//        }
//
//        String[] charsets = contents[1].split("=");
//        if (charsets.length < 2) {
////            log.info("charsets.length < 2");
//            return null;
//        }
//
//        return charsets[1].trim();
//    }

    /**
     * get status code of url, this is thread safe
     * @param url url
     * @return status code
     */
    public synchronized int getStatusCode(String url) {
        int statusCode = HttpStatus.SC_NOT_FOUND;

        if (StringUtils.isNotEmpty(url)) {
            HttpGet httpget = null;
            try {
                httpget = new HttpGet(url);
                statusCode = client.execute(httpget).getStatusLine().getStatusCode();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (httpget != null) {
                    httpget.abort();
                }
                closeExpiredConns();
                closeIdleConns();
            }
        }

        return statusCode;
    }

    /**
     * filter html tag
     * @param htmlstring html string
     * @return string without html tag
     */
    public String NoHTML(String htmlstring) {
        //删除脚本
        //String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        //String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        //String regEx_html = "<[^>]+>"; //定義HTML標籤的正規表示式
        htmlstring = htmlstring.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        htmlstring = htmlstring.replaceAll("<[\\s]*?iframe[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?iframe[\\s]*?>", "");
        htmlstring = htmlstring.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        htmlstring = htmlstring.replaceAll("<[^>]+>", "");
        htmlstring = htmlstring.replaceAll("&nbsp;", "");
        //htmlstring = htmlstring.replaceAll("[ ]*", "");

        //htmlstring = htmlstring.replaceAll("<script[^<>]*>(.*?)</script>", "");

        //删除HTML
        htmlstring = htmlstring.replaceAll("<a.*?</a>", "");
        htmlstring = htmlstring.replaceAll("<(.[^>]*)>", "");
        htmlstring = htmlstring.replaceAll("([\r\n])+", "");
        htmlstring = htmlstring.replaceAll("-->", "");
        htmlstring = htmlstring.replaceAll("<!--.*", "");
        htmlstring = htmlstring.replaceAll("&(quot|#34);", "\"");
        htmlstring = htmlstring.replaceAll("&(amp|#38);", "&");
        htmlstring = htmlstring.replaceAll("&(lt|#60);", "<");
        htmlstring = htmlstring.replaceAll("&(gt|#62);", ">");
        htmlstring = htmlstring.replaceAll("&(nbsp|#160);", " ");
        htmlstring = htmlstring.replaceAll("&(iexcl|#161);", "");
        htmlstring = htmlstring.replaceAll("&(cent|#162);", "");
        htmlstring = htmlstring.replaceAll("&(pound|#163);", "");
        htmlstring = htmlstring.replaceAll("&(copy|#169);", "");

        htmlstring = htmlstring.replaceAll("<", "");
        htmlstring = htmlstring.replaceAll(">", "");
        htmlstring = htmlstring.replaceAll("\r\n", "");
        htmlstring = htmlstring.replaceAll("[ ]*", "");

        return htmlstring;
    }

    /**
     * DefaultHttpClient config
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private void configureClient() throws NoSuchAlgorithmException, KeyManagementException {
        HttpParams params = new BasicHttpParams();

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, CHARSET);
        HttpProtocolParams.setUserAgent(params, pickUserAgent());
        HttpProtocolParams.setUseExpectContinue(params, false);

        // set connection timeout
        HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
        HttpConnectionParams.setSoTimeout(params, 5 * 1000);
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType){}
            public void checkServerTrusted(X509Certificate[] chain, String authType){}
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        } }, new SecureRandom());
        SSLSocketFactory sf = new SSLSocketFactory(sslContext);

        // Create and initialize scheme registry
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
//        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        schemeRegistry.register(new Scheme("https", 443, sf));

        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will be using the HttpClient.
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        // Increase max total connection
        cm.setMaxTotal(1000);
        // Increase default max connection per route
        cm.setDefaultMaxPerRoute(100);

        client = new DefaultHttpClient(cm, params);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3,true));
    }

    public void shutdownHttp() {
        client.getConnectionManager().shutdown();
    }

    public void closeExpiredConns() {
        client.getConnectionManager().closeExpiredConnections();
    }

    public void closeIdleConns() {
        client.getConnectionManager().closeIdleConnections(10L, TimeUnit.SECONDS);
    }

//    public static void main(String age[]){
//        String url = "http://lsstg.mypchome.com.tw";
//        String result = HttpUtil.getInstance().getResult(url);
//        System.out.println("url: " + url);
//        System.out.println("result: " + result);
//    }
}