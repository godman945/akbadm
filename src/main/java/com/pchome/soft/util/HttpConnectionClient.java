package com.pchome.soft.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpConnectionClient {
    private static final Log log = LogFactory.getLog(HttpConnectionClient.class);
    private static final int timeout = 5000;

    private static HttpConnectionClient httpConnectionClient = new HttpConnectionClient();

    private HttpConnectionClient() {}

    public static HttpConnectionClient getInstance() {
        return httpConnectionClient;
    }

    // Create a trust manager that does not validate certificate chains
    private TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
    }};

    private String[] userAgents = new String[]{
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

    private String pickUserAgent() {
       double v = Math.random() * userAgents.length;
       return userAgents[(int) v];
    }

    private HttpURLConnection getHttpURLConnection(String sUrl, String cookie, String referer, String userAgent, Proxy proxy, int timeout) throws IOException {
        HttpURLConnection urlConn = null;

        URL url = new URL(sUrl);
        if (proxy != null) {
            urlConn = (HttpURLConnection) url.openConnection(proxy);
        }
        else {
            urlConn = (HttpURLConnection) url.openConnection();
        }
        urlConn.setConnectTimeout(timeout);
        urlConn.setReadTimeout(timeout);

        urlConn.setRequestProperty("User-agent", StringUtils.isNotBlank(userAgent) ? userAgent : pickUserAgent());
//        urlConn.setRequestProperty("Accept", "text/html, application/xhtml+xml, application/xml;q = 0.9, */*;q = 0.8");
        urlConn.setRequestProperty("Accept", "text/html, application/xhtml+xml, application/xml, application/json");
        urlConn.setRequestProperty("Accept-Language", "zh-tw, en-us;q = 0.7, en;q = 0.3");
        urlConn.setRequestProperty("Accept-Charset", "Big5, utf-8;q = 0.7, *;q = 0.7");
        if (cookie != null) {
            urlConn.setRequestProperty("Cookie", cookie);
        }
        if (referer != null) {
            urlConn.setRequestProperty("Referer", referer);
        }

        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);

        return urlConn;
    }

    private HttpsURLConnection getHttpsURLConnection(String sUrl, String cookie, String referer, String userAgent, Proxy proxy, int timeout) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        HttpsURLConnection urlConn = null;

        URL url = new URL(sUrl);
        if (proxy != null) {
            urlConn = (HttpsURLConnection) url.openConnection(proxy);
        }
        else {
            urlConn = (HttpsURLConnection) url.openConnection();
        }
        urlConn.setConnectTimeout(timeout);
        urlConn.setReadTimeout(timeout);

//        System.setProperty("javax.net.debug", "all");
//        System.setProperty("https.protocols", "TLSv1.2");

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLSv1.2");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        urlConn.setSSLSocketFactory(sc.getSocketFactory());

        urlConn.setRequestProperty("User-agent", StringUtils.isNotBlank(userAgent) ? userAgent : pickUserAgent());
//        urlConn.setRequestProperty("Accept", "text/html, application/xhtml+xml, application/xml;q = 0.9, */*;q = 0.8");
        urlConn.setRequestProperty("Accept", "text/html, application/xhtml+xml, application/xml, application/json");
        urlConn.setRequestProperty("Accept-Language", "zh-tw, en-us;q = 0.7, en;q = 0.3");
        urlConn.setRequestProperty("Accept-Charset", "Big5, utf-8;q = 0.7, *;q = 0.7");
        if (cookie != null) {
            urlConn.setRequestProperty("Cookie", cookie);
        }
        if (referer != null) {
            urlConn.setRequestProperty("Referer", referer);
        }

        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);

        return urlConn;
    }

    public String doPost(String sUrl, String data) {
        return doPost(sUrl, data, null, null, "UTF-8");
    }

    public String doPost(String sUrl, String data, String charset) {
        return doPost(sUrl, data, null, null, charset);
    }

    public String doPost(String sUrl, String data, String charset, int timeout) {
        return doPost(sUrl, data, null, null, charset, timeout);
    }

    public String doPost(String sUrl, String data, String cookie, String referer, String charset) {
       return doPost(sUrl, data, cookie, referer, charset, timeout);
    }

    public String doPost(String sUrl, String data, String cookie, String referer, String charset, int timeout) {
        return doPost(sUrl, data, cookie, referer, charset, null, null, timeout);
    }

    public String doPost(String sUrl, String data, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        String result = "";

        if (StringUtils.isBlank(sUrl)) {
            return result;
        }

        if (sUrl.indexOf("http://") == 0) {
            result = this.doHttpPost(sUrl, data, cookie, referer, charset, userAgent, proxy, timeout);
        }
        else if (sUrl.indexOf("https://") == 0) {
            result = this.doHttpsPost(sUrl, data, cookie, referer, charset, userAgent, proxy, timeout);
        }

        return result;
    }

    public String doHttpPost(String sUrl, String data, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        HttpURLConnection urlConn = null;
        DataOutputStream dos = null;
        BufferedReader br = null;
        StringBuffer lineBuffer = new StringBuffer();

        try {
            urlConn = this.getHttpURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConn.setInstanceFollowRedirects(true);

            dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(data);

            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StringUtils.isNotBlank(charset) ? charset : "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                lineBuffer.append(line).append("\n");
            }
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                br = null;
            }

            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                dos = null;
            }

            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return lineBuffer.toString().trim();
    }

    public String doHttpsPost(String sUrl, String data, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        HttpsURLConnection urlConn = null;
        DataOutputStream dos = null;
        BufferedReader br = null;
        StringBuffer lineBuffer = new StringBuffer();

        try {
            urlConn = this.getHttpsURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConn.setInstanceFollowRedirects(true);

            dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(data);

            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StringUtils.isNotBlank(charset) ? charset : "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                lineBuffer.append(line).append("\n");
            }
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                br = null;
            }

            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                dos = null;
            }

            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return lineBuffer.toString().trim();
    }

    public int doPostStatus(String sUrl, String data) {
        return doPostStatus(sUrl, data, null, null, null, null, timeout);
    }

    public int doPostStatus(String sUrl, String data, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        int result = -1;

        if (StringUtils.isBlank(sUrl)) {
            return result;
        }

        if (sUrl.indexOf("http://") == 0) {
            result = this.doHttpPostStatus(sUrl, data, cookie, referer, userAgent, proxy, timeout);
        }
        else if (sUrl.indexOf("https://") == 0) {
            result = this.doHttpsPostStatus(sUrl, data, cookie, referer, userAgent, proxy, timeout);
        }

        return result;
    }

    public int doHttpPostStatus(String sUrl, String data, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        HttpURLConnection urlConn = null;
        int result = -1;

        try {
            urlConn = this.getHttpURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConn.setInstanceFollowRedirects(true);

            result = urlConn.getResponseCode();
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return result;
    }

    public int doHttpsPostStatus(String sUrl, String data, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        HttpsURLConnection urlConn = null;
        int result = -1;

        try {
            urlConn = this.getHttpsURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConn.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            urlConn.setRequestMethod("POST");
            urlConn.setUseCaches(false);
            urlConn.setAllowUserInteraction(true);
            HttpURLConnection.setFollowRedirects(true);
            urlConn.setInstanceFollowRedirects(true);

            result = urlConn.getResponseCode();
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return result;
    }

    public String doGet(String sUrl) {
        return doGet(sUrl, null, null, "UTF-8");
    }

    public String doGet(String sUrl,  String charset) {
        return doGet(sUrl, null, null, charset);
    }

    public String doGet(String sUrl,  String charset, int timeout) {
        return doGet(sUrl, null, null, charset, timeout);
    }

    public String doGet(String sUrl, String cookie, String referer, String charset) {
        return doGet(sUrl, cookie, referer, charset, timeout);
    }

    public String doGet(String sUrl, String cookie, String referer, String charset, int timeout) {
        return doGet(sUrl, cookie, referer, charset, null, null, timeout);
    }

    public String doGet(String sUrl, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        String result = "";

        if (StringUtils.isBlank(sUrl)) {
            return result;
        }

        if (sUrl.indexOf("http://") == 0) {
            result = this.doHttpGet(sUrl, cookie, referer, charset, userAgent, proxy, timeout);
        }
        else if (sUrl.indexOf("https://") == 0) {
            result = this.doHttpsGet(sUrl, cookie, referer, charset, userAgent, proxy, timeout);
        }

        return result;
    }

    public String doHttpGet(String sUrl, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        HttpURLConnection urlConn = null;
        BufferedReader br = null;
        StringBuffer lineBuffer = new StringBuffer();

        try {
            urlConn = this.getHttpURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.connect();
//            urlConn.getOutputStream().flush();

            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StringUtils.isNotBlank(charset) ? charset : "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                lineBuffer.append(line).append("\n");
            }
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                br = null;
            }

            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return lineBuffer.toString().trim();
    }

    public String doHttpsGet(String sUrl, String cookie, String referer, String charset, String userAgent, Proxy proxy, int timeout) {
        HttpsURLConnection urlConn = null;
        BufferedReader br = null;
        StringBuffer lineBuffer = new StringBuffer();

        try {
            urlConn = this.getHttpsURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.connect();
//            urlConn.getOutputStream().flush();

            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), StringUtils.isNotBlank(charset) ? charset : "UTF-8"));

            String line = null;
            while ((line = br.readLine()) != null) {
                lineBuffer.append(line).append("\n");
            }
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                br = null;
            }

            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return lineBuffer.toString().trim();
    }

    public int doGetStatus(String sUrl) {
        return doGetStatus(sUrl, null, null,  null, null, timeout);
    }

    public int doGetStatus(String sUrl, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        int result = -1;

        if (StringUtils.isBlank(sUrl)) {
            return result;
        }

        if (sUrl.indexOf("http://") == 0) {
            result = this.doHttpGetStatus(sUrl, cookie, referer, userAgent, proxy, timeout);
        }
        else if (sUrl.indexOf("https://") == 0) {
            result = this.doHttpsGetStatus(sUrl, cookie, referer, userAgent, proxy, timeout);
        }

        return result;
    }

    public int doHttpGetStatus(String sUrl, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        HttpURLConnection urlConn = null;
        int result = -1;

        try {
            urlConn = this.getHttpURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.connect();
//            urlConn.getOutputStream().flush();

            result = urlConn.getResponseCode();
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return result;
    }

    public int doHttpsGetStatus(String sUrl, String cookie, String referer, String userAgent, Proxy proxy, int timeout) {
        HttpsURLConnection urlConn = null;
        int result = -1;

        try {
            urlConn = this.getHttpsURLConnection(sUrl, cookie, referer, userAgent, proxy, timeout);

            urlConn.connect();
//            urlConn.getOutputStream().flush();

            result = urlConn.getResponseCode();
        }
        catch (Exception e) {
            log.error(sUrl, e);
        }
        finally {
            if (urlConn != null) {
                urlConn.disconnect();
                urlConn = null;
            }
        }

        return result;
    }

    public static void main(String str[]){
        String sUrl = "https://www.pchome.com.tw";
        String cookie = null;
        String referer = null;
        String charset = "UTF-8";
        int timeout = 3000;

//        String result = HttpConnectionClient.getInstance().doGet(sUrl, cookie, referer, charset, null, null, timeout);
//        String result = HttpConnectionClient.getInstance().doPost(sUrl, "", cookie, referer, charset, null, null, timeout);
//        int result = HttpConnectionClient.getInstance().doGetStatus(sUrl);
        int result = HttpConnectionClient.getInstance().doPostStatus(sUrl, "");

        System.out.println(result);
    }
}