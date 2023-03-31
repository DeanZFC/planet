package com.dean.planet.wechat.utils;

import com.alibaba.fastjson2.JSON;
import com.dean.planet.wechat.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dean
 * @since 2023/3/31 16:05
 */
@Slf4j
public class HttpClientUtils {
    private HttpClientUtils() {}
    public static final Integer HTTP_SUCCESS_STATUS_CODE = 200;

    public static final Integer DEFAULT_CONNECT_TIMEOUT = 2000;

    public static final Integer DEFAULT_SOCKET_TIMEOUT = 10000;

    private static final String APPLICATION_JSON_VALUE = "application/json";

    private static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

    private static final String ENCODING = "UTF-8";

    /**
     * 发送x-www-form-urlencoded请求
     *
     * @param url            请求url
     * @param paramMap       请求参数
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @param clazz          要返回的类
     * @return T
     */
    public static <T> T formPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout, Type clazz) throws IOException {
        HttpResponse response = formPostSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getResult(response, clazz, url, paramMap == null ? null : JSON.toJSONString(paramMap)));
    }

    public static <T> T formPost(String url , Map<String , String> paramMap , Map<String, String> headerMap , int connectTimeout , int socketTimeout ,Type clazz , boolean debug) throws IOException {
        HttpResponse response = formPostSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getResult(response, clazz, url, paramMap == null ? null : JSON.toJSONString(paramMap) , debug));
    }

    /**
     * 发送x-www-form-urlencoded请求
     *
     * @param url       请求url
     * @param paramMap  请求参数
     * @param headerMap 请求头
     * @param clazz     要返回的类
     * @return T
     */
    public static <T> T formPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, Type clazz) throws IOException {
        return (formPost(url, paramMap, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz));
    }

    public static <T> T formPost(String url, Map<String, String> paramMap, Map<String, String> headerMap, Type clazz , boolean debug) throws IOException {
        return (formPost(url, paramMap, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz , debug));
    }

    /**
     * 发送x-www-form-urlencoded请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @param clazz    要返回的类
     * @return T
     */
    public static <T> T formPost(String url, Map<String, String> paramMap, Type clazz) throws IOException {
        return (formPost(url, paramMap, null, clazz));
    }

    public static <T> T formPost(String url, Map<String, String> paramMap, Type clazz , boolean debug) throws IOException {
        return (formPost(url, paramMap, null, clazz , debug));
    }

    /**
     * 发送x-www-form-urlencoded请求
     *
     * @param url   请求url
     * @param clazz 要返回的类
     * @return T
     */
    public static <T> T formPost(String url, Type clazz) throws IOException {
        return (formPost(url, null, null, clazz));
    }

    public static <T> T formPost(String url, Type clazz , boolean debug) throws IOException {
        return (formPost(url, null, null, clazz , debug));
    }

    /**
     * 发送x-www-form-urlencoded请求返回string结果
     *
     * @param url            请求url
     * @param paramMap       请求参数
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @return 调用api返回的值
     * @throws IOException io异常
     */
    public static String formPostStringResult(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout) throws IOException {
        HttpResponse response = formPostSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getStringResult(response, url, JSON.toJSONString(paramMap)));
    }

    public static String formPostStringResult(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout , boolean debug) throws IOException {
        HttpResponse response = formPostSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getStringResult(response, url, JSON.toJSONString(paramMap) , debug));
    }

    /**
     * 发送json请求
     *
     * @param url            请求url
     * @param paramString    请求参数
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @param clazz          要返回的类
     * @return T
     */
    public static <T> T jsonPost(String url, String paramString, Map<String, String> headerMap, int connectTimeout, int socketTimeout, Type clazz) throws IOException {
        //打印入参日志
        log.info("api入参url={}，params={}", url, paramString);

        /*
         * 1、创建httpclient
         */
        HttpClient httpClient = creatHttpClient(connectTimeout, socketTimeout);

        /*
         * 2、创建httpPost并设置参数
         */
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.forEach(httpPost::setHeader);
        }
        httpPost.setHeader("Content-type", APPLICATION_JSON_VALUE);

        /*
         * 3、创建httpEntity并设置参数
         */
        StringEntity stringEntity = null;

        //设置请求参数
        if (StringUtils.isNotBlank(paramString)) {
            stringEntity = new StringEntity(paramString, ENCODING);
            stringEntity.setContentType(APPLICATION_JSON_VALUE);
        }

        if (stringEntity != null) {
            httpPost.setEntity(stringEntity);
        }

        HttpResponse response = httpClient.execute(httpPost);

        return (getResult(response, clazz, url, paramString));
    }

    public static <T> T jsonPost(String url, String paramString, Map<String, String> headerMap, int connectTimeout, int socketTimeout, Type clazz , boolean debug) throws IOException {
        //打印入参日志
        log.info(String.format("api入参url=%s，params=%s%n", url, paramString));

        /*
         * 1、创建httpclient
         */
        HttpClient httpClient = creatHttpClient(connectTimeout, socketTimeout);

        /*
         * 2、创建httpPost并设置参数
         */
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.forEach(httpPost::setHeader);
        }
        httpPost.setHeader("Content-type", APPLICATION_JSON_VALUE);

        /*
         * 3、创建httpEntity并设置参数
         */
        StringEntity stringEntity = null;

        //设置请求参数
        if (StringUtils.isNotBlank(paramString)) {
            stringEntity = new StringEntity(paramString, ENCODING);
            stringEntity.setContentType(APPLICATION_JSON_VALUE);
        }

        if (stringEntity != null) {
            httpPost.setEntity(stringEntity);
        }

        HttpResponse response = httpClient.execute(httpPost);

        return (getResult(response, clazz, url, paramString , debug));
    }

    /**
     * 发送json请求
     *
     * @param url         请求url
     * @param paramString 请求参数
     * @param headerMap   请求头
     * @param clazz       要返回的类
     * @return T
     */
    public static <T> T jsonPost(String url, String paramString, Map<String, String> headerMap, Type clazz) throws IOException {
        return (jsonPost(url, paramString, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz));
    }

    public static <T> T jsonPost(String url, String paramString, Map<String, String> headerMap, Type clazz , boolean debug) throws IOException {
        return (jsonPost(url, paramString, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz , debug));
    }

    /**
     * 发送json请求
     *
     * @param url         请求url
     * @param paramString 请求参数
     * @param clazz       要返回的类
     * @return T
     */
    public static <T> T jsonPost(String url, String paramString, Type clazz) throws IOException {
        return (jsonPost(url, paramString, null, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz));
    }

    public static <T> T jsonPost(String url, String paramString, Type clazz , boolean debug) throws IOException {
        return (jsonPost(url, paramString, null, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz , debug));
    }

    /**
     * 发送json请求
     *
     * @param url   请求url
     * @param clazz 要返回的类
     * @return T
     */
    public static <T> T jsonPost(String url, Type clazz) throws IOException {
        return (jsonPost(url, null, null, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz));
    }

    public static <T> T jsonPost(String url, Type clazz , boolean debug) throws IOException {
        return (jsonPost(url, null, null, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz , debug));
    }

    /**
     * get发送器
     *
     * @param url            请求url
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @return {@link HttpResponse}
     * @throws IOException io异常
     */
    public static HttpResponse getSender(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout) throws Exception {
        //打印入参日志
        log.info("api入参url={}，param={}，header={}", url, paramMap, headerMap);

        /*
         * 1、创建httpclient
         */
        HttpClient httpClient = creatHttpClient(connectTimeout, socketTimeout);

        /*
         * 2、创建httpGet并设置参数
         */
        URIBuilder uriBuilder = new URIBuilder(url);
        if (paramMap != null) {
            for (String key : paramMap.keySet()) {
                uriBuilder.setParameter(key, paramMap.get(key));
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.forEach(httpGet::setHeader);
        }

        /*
         * 4、发送请求
         */
        return (httpClient.execute(httpGet));
    }

    /**
     * 发送get请求
     *
     * @param url            请求url
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @param clazz          要返回的类
     * @return T
     */
    public static <T> T get(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout, Type clazz) throws Exception {
        HttpResponse response = getSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getResult(response, clazz, url, paramMap == null ? null : JSON.toJSONString(paramMap)));
    }

    public static <T> T get(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout, Type clazz , boolean debug) throws Exception {
        HttpResponse response = getSender(url, paramMap, headerMap, connectTimeout, socketTimeout);
        return (getResult(response, clazz, url, paramMap == null ? null : JSON.toJSONString(paramMap) ,debug));
    }

    /**
     * 发送get请求
     *
     * @param url       请求url
     * @param paramMap  请求参数
     * @param headerMap 请求头
     * @param clazz     要返回的类
     * @return T
     */
    public static <T> T get(String url, Map<String, String> paramMap, Map<String, String> headerMap, Type clazz) throws Exception {
        return (get(url, paramMap, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz));
    }

    public static <T> T get(String url, Map<String, String> paramMap, Map<String, String> headerMap, Type clazz , boolean debug) throws Exception {
        return (get(url, paramMap, headerMap, DEFAULT_CONNECT_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, clazz ,debug));
    }

    /**
     * 发送get请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @param clazz    要返回的类
     * @return T
     */
    public static <T> T get(String url, Map<String, String> paramMap, Type clazz) throws Exception {
        return (get(url, paramMap, null, clazz));
    }

    public static <T> T get(String url, Map<String, String> paramMap, Type clazz , boolean debug) throws Exception {
        return (get(url, paramMap, null, clazz , debug));
    }

    /**
     * 发送get请求
     *
     * @param url   请求url
     * @param clazz 要返回的类
     * @return T
     */
    public static <T> T get(String url, Type clazz) throws Exception {
        return (get(url, null, null, clazz));
    }

    public static <T> T get(String url, Type clazz , boolean debug) throws Exception {
        return (get(url, null, null, clazz , debug));
    }


    /**
     * form post发送器
     *
     * @param url            请求url
     * @param paramMap       请求参数
     * @param headerMap      请求头
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @return {@link HttpResponse}
     * @throws IOException io异常
     */
    private static HttpResponse formPostSender(String url, Map<String, String> paramMap, Map<String, String> headerMap, int connectTimeout, int socketTimeout) throws IOException {
        //打印入参日志
        log.info("api入参url={}，params={}，header={}", url, JSON.toJSONString(paramMap), JSON.toJSONString(headerMap));

        /*
         * 1、创建httpclient
         */
        HttpClient httpClient = creatHttpClient(connectTimeout, socketTimeout);

        /*
         * 2、创建httpPost并设置参数
         */
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        if (headerMap != null && !headerMap.isEmpty()) {
            headerMap.forEach(httpPost::setHeader);
        }
        httpPost.setHeader("Content-type", APPLICATION_FORM_URLENCODED_VALUE);

        /*
         * 3、创建httpEntity并设置参数
         */
        HttpEntity httpEntity = null;

        //设置请求参数
        if (paramMap != null && !paramMap.isEmpty()) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            paramMap.forEach((name, value) -> nameValuePairList.add(new BasicNameValuePair(name, value)));
            httpEntity = new UrlEncodedFormEntity(nameValuePairList, ENCODING);
        }

        if (httpEntity != null) {
            httpPost.setEntity(httpEntity);
        }

        /*
         * 4、发送请求
         */
        return (httpClient.execute(httpPost));
    }


    /**
     * 创建httpClient
     *
     * @param connectTimeout 连接超时时间
     * @param socketTimeout  socket超时时间
     * @return {@link CloseableHttpClient}
     */
    private static HttpClient creatHttpClient(int connectTimeout, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        return (HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build());
    }

    /**
     * 获取响应类
     *
     * @param response    httpclient响应类
     * @param clazz       需要转换的响应类
     * @param url         请求url
     * @param paramString 请求参数
     * @return 转换后的响应类
     */
    private static <T> T getResult(HttpResponse response, Type clazz, String url, String paramString) throws IOException {
        if (HTTP_SUCCESS_STATUS_CODE.equals(response.getStatusLine().getStatusCode())) {
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            T result = JSON.parseObject(bytes, clazz);
            //打印出参日志
            log.info("api出参url={}，params={}，result={}", url, paramString, JSON.toJSONString(result));
            return (result);
        } else {
            String errorMessage = String.format("api请求报错，url=%s，params=%s，httpStatusCode=%d%n", url, paramString, response.getStatusLine().getStatusCode());
            //打印错误日志
            log.error(errorMessage);
            throw new ApiException(errorMessage);
        }
    }

    /**
     * 获取响应类
     *
     * @param response    httpclient响应类
     * @param clazz       需要转换的响应类
     * @param url         请求url
     * @param paramString 请求参数
     * @param debug 是否打印日志
     * @return 转换后的响应类
     */
    private static <T> T getResult(HttpResponse response, Type clazz, String url, String paramString , boolean debug) throws IOException {
        if (HTTP_SUCCESS_STATUS_CODE.equals(response.getStatusLine().getStatusCode())) {
            byte[] bytes = EntityUtils.toByteArray(response.getEntity());
            T result = JSON.parseObject(bytes, clazz);
            if (debug){
                //打印出参日志
                log.info("api出参url={}，params={}，result={}", url, paramString, JSON.toJSONString(result));
            }
            return (result);
        } else {
            String errorMessage = String.format("api请求报错，url=%s，params=%s，httpStatusCode=%d%n", url, paramString, response.getStatusLine().getStatusCode());
            //打印错误日志
            log.error(errorMessage);
            throw new ApiException(errorMessage);
        }
    }

    /**
     * 获取响应值
     *
     * @param response    httpclient响应类
     * @param url         请求url
     * @param paramString 请求参数
     * @return 转换后的响应类
     * @throws IOException io异常
     */
    private static String getStringResult(HttpResponse response, String url, String paramString) throws IOException {
        if (HTTP_SUCCESS_STATUS_CODE.equals(response.getStatusLine().getStatusCode())) {
            String result = EntityUtils.toString(response.getEntity());
            //打印出参日志
            log.info("api出参url={}，params={}，result={}", url, paramString, result);
            return (result);
        } else {
            String errorMessage = String.format("api请求报错，url=%s，params=%s，httpStatusCode=%d%n", url, paramString, response.getStatusLine().getStatusCode());
            //打印错误日志
            log.error(errorMessage);
            throw new ApiException(errorMessage);
        }
    }

    /**
     * 获取响应值
     *
     * @param response    httpclient响应类
     * @param url         请求url
     * @param paramString 请求参数
     * @param debug 是否打印日志
     * @return 转换后的响应类
     * @throws IOException io异常
     */
    private static String getStringResult(HttpResponse response, String url, String paramString , boolean debug) throws IOException {
        if (HTTP_SUCCESS_STATUS_CODE.equals(response.getStatusLine().getStatusCode())) {
            String result = EntityUtils.toString(response.getEntity());
            if (debug){
                //打印出参日志
                log.info("api出参url={}，params={}，result={}", url, paramString, result);
            }
            return (result);
        } else {
            String errorMessage = String.format("api请求报错，url=%s，params=%s，httpStatusCode=%d%n", url, paramString, response.getStatusLine().getStatusCode());
            //打印错误日志
            log.error(errorMessage);
            throw new ApiException(errorMessage);
        }
    }

}