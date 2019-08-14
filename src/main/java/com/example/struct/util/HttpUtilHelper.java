package com.example.struct.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HttpUtilHelper {
  private static final int CONNECTION_TIMEOUT = 500;
  private static final int CONNECTION_BIG_TIMEOUT = 60000;
  private static final int SO_TIMEOUT = 500;
  private static final int SO_BIG_TIMEOUT = 60000;
  private static final int MAX_REQUEST_NUM = 200;
  private static final int PER_SITE_MAX_REQUEST = 40;
  public static final String HEADER_COOKIE = "Cookie";
  public static final String HEADER_USER_AGNET = "User-Agent";
  public static final String HEADER_HOST = "Host";
  public static final String HEADER_REFER = "Referer";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String DEFAULT_CHARSET = "UTF8";
  private PoolingHttpClientConnectionManager pcmgr = null;
  private HttpClient httpclient;
  private RequestConfig bigTimeout;
  private RequestConfig smallTimeout;
  Logger logger = LoggerFactory.getLogger(HttpUtilHelper.class);

  public HttpUtilHelper() {
    this.init(200, 40, 60000, 60000, 500, 500, (SSLContext)null);
  }

  public HttpUtilHelper(String keyPassphrase, String certPath, int maxReqNum) {
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(new FileInputStream(certPath), keyPassphrase.toCharArray());
      SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, (char[])null).build();
      this.init(maxReqNum, maxReqNum / 5 + 1, 60000, 60000, 500, 500, sslContext);
    } catch (Exception var6) {
      var6.printStackTrace();
      this.logger.error("failed load key-pair:", var6);
      this.logger.error("key store path: {}", certPath);
    }

  }

  public HttpUtilHelper(int maxReqNum, int maxPerSite) {
    this.init(maxReqNum, maxPerSite, 60000, 60000, 500, 500, (SSLContext)null);
  }

  public HttpUtilHelper(int maxReqNum, int maxPerSite, int bigConnTimeout, int bigSocketConnTimeout, int smallConnTimeout, int smallSocketConnTimeout, SSLContext sslContext) {
    this.init(maxReqNum, maxPerSite, bigConnTimeout, bigSocketConnTimeout, smallConnTimeout, smallSocketConnTimeout, sslContext);
  }

  private void init(int maxReqNum, int maxPerSite, int bigConnTimeout, int bigSocketConnTimeout, int smallConnTimeout, int smallSocketConnTimeout, SSLContext sslContext) {
    this.pcmgr = new PoolingHttpClientConnectionManager();
    this.pcmgr.setMaxTotal(maxReqNum);
    this.pcmgr.setDefaultMaxPerRoute(maxPerSite);
    this.pcmgr.setValidateAfterInactivity(100);
    this.bigTimeout = RequestConfig.custom().setSocketTimeout(bigSocketConnTimeout).setConnectTimeout(bigConnTimeout).build();
    this.smallTimeout = RequestConfig.custom().setSocketTimeout(smallSocketConnTimeout).setConnectTimeout(smallConnTimeout).build();
    HttpClientBuilder cb = HttpClients.custom().setConnectionManager(this.pcmgr);
    if (sslContext != null) {
      cb.setSSLContext(sslContext);
    }

    this.httpclient = cb.build();
  }

  public ResultStatus httpGet(String url) {
    return this.httpGet(url, false);
  }

  public ResultStatus httpGet(String url, boolean bigtimeout) {
    return this.httpGet(url, bigtimeout, (List)null);
  }

  private final RequestConfig genRequestConfig(boolean bigtime) {
    return bigtime ? this.bigTimeout : this.smallTimeout;
  }

  public ResultStatus httpGet(String url, boolean bigtimeout, List<Entry<String, String>> header) {
    return this.httpGet(url, bigtimeout, header, false);
  }

  public ResultStatus httpGet(String url, boolean bigtimeout, List<Entry<String, String>> header, boolean byteResult) {
    long start = System.currentTimeMillis();
    ResultStatus retResult = new ResultStatus();
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader("Connection", "close");

    try {
      httpget.setConfig(this.genRequestConfig(bigtimeout));
      HttpUriRequest request = httpget;
      if (header != null && !header.isEmpty()) {
        Iterator var10 = header.iterator();

        while(var10.hasNext()) {
          Entry<String, String> e = (Entry)var10.next();
          request.addHeader((String)e.getKey(), (String)e.getValue());
        }
      }

      HttpResponse response = this.httpclient.execute(request);
      retResult.setCode(response.getStatusLine().getStatusCode());
      if (!byteResult) {
        retResult.setResponse(EntityUtils.toString(response.getEntity(), "UTF-8"));
      } else {
        retResult.setByteResponse(EntityUtils.toByteArray(response.getEntity()));
      }
    } catch (Exception var15) {
      this.logger.error("failed post request: ", var15);
      retResult.setException(var15.getMessage());
    } finally {
      httpget.releaseConnection();
    }

    start = System.currentTimeMillis() - start;
    this.logger.info("Finished :" + url + " cost:" + start);
    return retResult;
  }

  public ResultStatus httpPost(String url, Map<String, String> map) {
    return this.httpPost(url, map, false);
  }

  public ResultStatus httpPost(String url, Map<String, String> map, boolean bigtimeout) {
    return this.httpPost(url, map, bigtimeout, (List)null);
  }

  public ResultStatus httpPost(String url, Map<String, String> body, boolean bigtimeout, List<Entry<String, String>> header) {
    return this.httpPost(url, body, bigtimeout, header, false);
  }

  public final ResultStatus httpPost(String url, Map<String, String> body, boolean bigtimeout, List<Entry<String, String>> header, boolean byteResult) {
    String charset = "utf-8";
    HttpPost httpPost = null;
    long start = System.currentTimeMillis();
    ResultStatus retresult = new ResultStatus();
    httpPost = new HttpPost(url);
    httpPost.addHeader("Connection", "close");

    try {
      httpPost.setConfig(this.genRequestConfig(bigtimeout));
      List<NameValuePair> list = new ArrayList();
      Iterator iterator = body.entrySet().iterator();

      while(iterator.hasNext()) {
        Entry<String, String> elem = (Entry)iterator.next();
        list.add(new BasicNameValuePair((String)elem.getKey(), (String)elem.getValue()));
      }

      if (list.size() > 0) {
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
        httpPost.setEntity(entity);
      }

      if (header != null && !header.isEmpty()) {
        Iterator var21 = header.iterator();

        while(var21.hasNext()) {
          Entry<String, String> e = (Entry)var21.next();
          httpPost.addHeader((String)e.getKey(), (String)e.getValue());
        }
      }

      HttpResponse response = this.httpclient.execute(httpPost);
      HttpEntity resEntity = response.getEntity();
      retresult.setCode(response.getStatusLine().getStatusCode());
      if (!byteResult) {
        retresult.setResponse(resEntity != null ? EntityUtils.toString(resEntity, "UTF-8") : null);
      } else {
        retresult.setByteResponse(EntityUtils.toByteArray(resEntity));
      }
    } catch (Exception var18) {
      this.logger.error("failed to http post: {}", url);
      this.logger.error("error:", var18);
      retresult.setException(var18.getMessage());
    } finally {
      httpPost.releaseConnection();
    }

    start = System.currentTimeMillis() - start;
    this.logger.info("Finished :" + url + " cost:" + start);
    return retresult;
  }

  public ResultStatus httpJsonPost(String url, String data) {
    return this.httpJsonPost(url, data, false);
  }

  public ResultStatus httpJsonPost(String url, String data, boolean bigtimeout) {
    List<Entry<String, String>> header = new ArrayList();
    Entry<String, String> jsonh = new SimpleEntry("Content-Type", "application/json");
    header.add(jsonh);
    return this.httpPostWithData(url, data, bigtimeout, header, ContentType.APPLICATION_JSON, (String)null, false);
  }

  public ResultStatus httpJsonPost(String url, String data, boolean bigtimeout, boolean ifUseStream) {
    List<Entry<String, String>> header = new ArrayList<>();
    Entry<String, String> jsonh = new SimpleEntry<>("Content-Type", "application/json");
    header.add(jsonh);
    return this.httpPostWithData(url, data, bigtimeout, header, ContentType.APPLICATION_JSON, (String)null, ifUseStream);
  }

  public ResultStatus httpPostWithData(String url, String data, boolean bigtimeout, List<Entry<String, String>> header, ContentType type, String forceCharset, boolean byteResult) {
    HttpPost httpPost = null;
    ResultStatus retresult = new ResultStatus();
    long start = System.currentTimeMillis();
    httpPost = new HttpPost(url);
    httpPost.addHeader("Connection", "close");

    try {
      httpPost.setConfig(this.genRequestConfig(bigtimeout));
      StringEntity jsonenty = new StringEntity(data, type);
      httpPost.setEntity(jsonenty);
      if (header != null) {

        for (Entry<String, String> stringStringEntry : header) {
          Entry e = (Entry) stringStringEntry;
          httpPost.addHeader((String) e.getKey(), (String) e.getValue());
        }
      }

      HttpResponse response = this.httpclient.execute(httpPost);
      HttpEntity resEntity = response.getEntity();
      retresult.setCode(response.getStatusLine().getStatusCode());
      if (resEntity != null) {
        if (!byteResult) {
          ContentType ctype = ContentType.get(resEntity);
          if (forceCharset == null && ctype != null && ctype.getCharset() != null) {
            forceCharset = ctype.getCharset().displayName();
          }

          retresult.setResponse(forceCharset != null ? EntityUtils.toString(resEntity, forceCharset) : EntityUtils.toString(resEntity, "UTF8"));
        } else {
          retresult.setByteResponse(EntityUtils.toByteArray(resEntity));
        }
      } else {
        this.logger.error("failed found any response body: {} => {}, {}", new Object[]{url, response, retresult.getHttpCode()});
        retresult.setResponse((String)null);
      }
    } catch (Exception var19) {
      this.logger.error("failed request url: {}", url);
      this.logger.error("error while post :", var19);
      retresult.setException(var19.getMessage());
    } finally {
      httpPost.releaseConnection();
    }

    start = System.currentTimeMillis() - start;
    this.logger.info("Finished :" + url + " cost:" + start);
    return retresult;
  }

  public ResultStatus httpPostWithData(String url, String data, boolean bigtimeout, List<Entry<String, String>> header, ContentType type, String forceCharset) {
    HttpPost httpPost = null;
    long start = System.currentTimeMillis();
    ResultStatus retresult = new ResultStatus();
    httpPost = new HttpPost(url);
    httpPost.addHeader("Connection", "close");

    try {
      httpPost.setConfig(this.genRequestConfig(bigtimeout));
      StringEntity jsonenty = new StringEntity(data, type);
      httpPost.setEntity(jsonenty);
      if (header != null) {
        Iterator var12 = header.iterator();

        while(var12.hasNext()) {
          Entry<String, String> e = (Entry)var12.next();
          httpPost.addHeader((String)e.getKey(), (String)e.getValue());
        }
      }

      HttpResponse response = this.httpclient.execute(httpPost);
      HttpEntity resEntity = response.getEntity();
      retresult.setCode(response.getStatusLine().getStatusCode());
      if (resEntity == null) {
        this.logger.error("failed get response entiry: {} => {}", url, response);
        retresult.setException("failed found any response");
      } else {
        ContentType ctype = ContentType.get(resEntity);
        if (forceCharset == null && ctype != null && ctype.getCharset() != null) {
          forceCharset = ctype.getCharset().displayName();
        }

        retresult.setResponse(forceCharset != null ? new String(EntityUtils.toByteArray(resEntity), forceCharset) : new String(EntityUtils.toByteArray(resEntity)));
      }
    } catch (Exception var18) {
      this.logger.error("failed post exception: {}");
      retresult.setException(var18.getMessage());
    } finally {
      httpPost.releaseConnection();
    }

    start = System.currentTimeMillis() - start;
    this.logger.info("Finished :" + url + " cost:" + start);
    return retresult;
  }

  public ResultStatus httpTrans(String url, HttpEntity men) {
    return this.httpTrans(url, (List)null, men, false, false);
  }

  public ResultStatus httpTrans(String url, List<Entry<String, String>> header, HttpEntity men, boolean bigtimeout, boolean byteResult) {
    HttpPost httpPost = null;
    long start = System.currentTimeMillis();
    httpPost = new HttpPost(url);
    httpPost.addHeader("Connection", "close");
    ResultStatus retresult = new ResultStatus();

    try {
      if (header != null) {
        Iterator var10 = header.iterator();

        while(var10.hasNext()) {
          Entry<String, String> e = (Entry)var10.next();
          httpPost.addHeader((String)e.getKey(), (String)e.getValue());
        }
      }

      httpPost.setEntity(men);
      HttpResponse response = this.httpclient.execute(httpPost);
      retresult.setCode(response.getStatusLine().getStatusCode());
      if (!byteResult) {
        retresult.setResponse(EntityUtils.toString(response.getEntity(), "UTF-8"));
      } else {
        retresult.setByteResponse(EntityUtils.toByteArray(response.getEntity()));
      }
    } catch (Exception var15) {
      this.logger.error("error post tran:", var15);
      retresult.setException(var15.getMessage());
    } finally {
      httpPost.releaseConnection();
    }

    start = System.currentTimeMillis() - start;
    this.logger.info("Finished :" + url + " cost:" + start);
    return retresult;
  }

  public static String JoinKeyValue(List<Entry<String, String>> cookie, String connector) {
    String cookiestr = "";

    Entry e;
    for(Iterator var3 = cookie.iterator(); var3.hasNext(); cookiestr = cookiestr + (String)e.getKey() + "=" + (String)e.getValue() + connector) {
      e = (Entry)var3.next();
    }

    return cookiestr;
  }

  public String getStatus() {
    PoolStats pools = this.pcmgr.getTotalStats();
    return pools.toString();
  }

  public static void main(String[] args) {
    String url = "http://caiwu.fuyoukache.com/api/customer/checkorder/getAllCheckOrder.do";
    List<Entry<String, String>> header = new ArrayList(1);
    Entry<String, String> cookie = new SimpleEntry("Cookie", "fytoken=dc7e109c02b915583472a34e08e68c95; expires=Thu, 17 Aug 2017 13:14:34 GMT; path=/; domain=caiwu.fuyoukache.com");
    header.add(0, cookie);
    HttpUtilHelper helper = new HttpUtilHelper(1, 1);
    JSONObject j = new JSONObject();
    JSONArray ja = new JSONArray();
    JSONObject jj = new JSONObject();
    jj.put("url", "/v3/geocode/geo?key=7b57f70bb2de1f1a4ba5fd611bb03273&address=广东省深圳市龙岗区南湾街道白李路30号旗丰国际电子产业园&city=深圳市");
    ja.add(jj);
    j.put("ops", ja);
    System.out.println(j.toJSONString());
    url = "http://restapi.amap.com/v3/batch?key=7b57f70bb2de1f1a4ba5fd611bb03273";
    ResultStatus s = helper.httpJsonPost(url, j.toJSONString());
    System.out.println(s.getStrResponse());
  }
}
