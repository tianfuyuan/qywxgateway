package com.ihxlife.qyhgateway.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ihxlife.qyhgateway.support.exception.BusinessException;
import com.ihxlife.qyhgateway.support.exception.SysException;

/**
 * Http请求工具类
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
public class HttpUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	// HTTP请求和响应的默认编码
	private static final String DEFAULT_CHARSET = "UTF-8";

	private static RequestConfig getConfig(Integer timeout) {
		RequestConfig config = null;
		if (timeout != null && timeout != 0) {
			config = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(10000)
					.setConnectionRequestTimeout(10000).build();
			/* 代理配置 */
			// HttpHost proxy = new HttpHost("10.0.251.94", 8080, "http");
			// config = RequestConfig.custom()
			// .setSocketTimeout(10000)
			// .setConnectTimeout(10000)
			// .setConnectionRequestTimeout(10000)
			// .setStaleConnectionCheckEnabled(true).setProxy(proxy).build();

		} else {
			config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
					.setConnectionRequestTimeout(10000).build();
		}
		return config;
	}

	/**
	 * 
	 * @description 通过HTTP协议向服务器段发送请求,并得到返回结果，根据项目要求所有请求代码为UTF-8，请求地址必须用
	 *              URLEncoder.encode进行UTF-8转化。
	 * @param serviceUrl 服务器地址
	 * @param pMap 请求的键值对入参
	 * @return String 返回数据
	 * @throws Exception
	 */
	public static String post(String requestUrl, Map<String, String> paraMap, int timeout) throws Exception {
		String result = "";
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		long start_time = System.currentTimeMillis();
		try {
			HttpPost httpPost = new HttpPost(requestUrl);
			httpPost.setConfig(getConfig(timeout));
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			String para = "";
			if (paraMap != null && paraMap.size() != 0) {
				for (Map.Entry<String, String> entry : paraMap.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					para = para + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET) + "&";
				}
			}
			logger.info("执行http请求参数编码【{}】执行http请求参数【{}】", DEFAULT_CHARSET, para);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET));
			if (requestUrl.indexOf("https://") == 0) {
				logger.info("根据请求地址【{}】转换访问方式成https", requestUrl);
				httpclient = createSSLClientDefault();
			} else {
				httpclient = createSSLClientDefaultBycrert(null, null);
			}
			response = httpclient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("执行http请求返回状态码【{}】,耗时【{}】", statusCode, (System.currentTimeMillis() - start_time));
			HttpEntity entity = response.getEntity();
			StringBuffer sb = new StringBuffer();
			if (entity != null) {
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(entity.getContent(), DEFAULT_CHARSET));
				String text = null;
				while ((text = bufferedReader.readLine()) != null) {
					sb.append(text);
				}
			}
			result = sb.toString();
			EntityUtils.consume(entity);

			if (statusCode != 200) {
				logger.info("Http请求异常，返回码【{}】,返回内容【{}】", statusCode, result);
				return null;
			}
			return result;
		} catch (ConnectTimeoutException ex) {
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("post请求连接超时");
		}catch(SocketTimeoutException ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new SysException("post请求读取信息超时");
		}catch(Exception ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("系统异常，请联系保险公司！");
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("根据请求地址【{}】请求参数【{}】返回信息【{}】,请求耗时【{}】", requestUrl, paraMap.toString(), result,
					(System.currentTimeMillis() - start_time));
		}
	}

	public static String get(String requestUrl) throws Exception {
		/* 1 生成 HttpClinet 对象并设置参数 */
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		long start_time = System.currentTimeMillis();

		if (requestUrl.indexOf("https://") == 0) {
			logger.info("根据请求地址【{}】转换访问方式成https", requestUrl);
			httpClient = createSSLClientDefault();
		} else {
			httpClient = createSSLClientDefaultBycrert(null, null);
		}
		HttpGet httpRequst = new HttpGet(requestUrl);
		String result = "";
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			httpResponse = httpClient.execute(httpRequst);// 其中HttpGet是HttpUriRequst的子类
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = EntityUtils.toString(httpEntity);// 取出应答字符串
				// 一般来说都要删除多余的字符
				result.replaceAll("\r", "");// 去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格
			} else {
				httpRequst.abort();
			}
		}  catch (ConnectTimeoutException ex) {
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("get请求连接超时");
		}catch(SocketTimeoutException ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new SysException("get请求读取信息超时");
		}catch(Exception ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("系统异常，请联系保险公司！");
		}finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("读取http请求返回信息，请求地址【{}】,出参【{}】,请求耗时【{}】", requestUrl, result,
					(System.currentTimeMillis() - start_time));
		}
		return result;
	}

	public static InputStream getStream(String requestUrl) throws Exception {
		/* 1 生成 HttpClinet 对象并设置参数 */
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		long start_time = System.currentTimeMillis();

		if (requestUrl.indexOf("https://") == 0) {
			logger.info("根据请求地址【{}】转换访问方式成https", requestUrl);
			httpClient = createSSLClientDefault();
		} else {
			httpClient = createSSLClientDefaultBycrert(null, null);
		}
		HttpGet httpRequst = new HttpGet(requestUrl);
		InputStream result = null;
		try {
			// 使用DefaultHttpClient类的execute方法发送HTTP GET请求，并返回HttpResponse对象。
			httpResponse = httpClient.execute(httpRequst);// 其中HttpGet是HttpUriRequst的子类
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				result = httpEntity.getContent();

			} else {
				httpRequst.abort();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("读取http请求返回信息，请求地址【{}】,出参【{}】,请求耗时【{}】", requestUrl, result,
					(System.currentTimeMillis() - start_time));
		}
		return result;
	}

	/**
	 * 创建CloseableHttpClient
	 * @param keyID 证书密钥 
	 * @param keyPath 证书绝对路径
	 * @return CloseableHttpClient
	 */
	private static CloseableHttpClient createSSLClientDefaultBycrert(String certKey, String certPath) {
		// 判断是否传入证书和密钥
		if (certPath != null && certKey != null) {
			logger.info("创建CloseableHttpClient使用证书，证书路径【{}】，证书密码【{}】", certPath, certKey);
			try {
				FileInputStream instream = new FileInputStream(new File(certPath));
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				try {
					keyStore.load(instream, certKey.toCharArray());
				} finally {
					if (instream != null) {
						instream.close();
					}
				}

				SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certKey.toCharArray())
						.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" },
						null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				// 设置httpclient的SSLSocketFactory 
				return HttpClients.custom().setDefaultRequestConfig(getConfig(null)).setSSLSocketFactory(sslsf).build();
			} catch (Exception e) {
				logger.info("创建CloseableHttpClient使用证书，证书路径【{}】，证书密码【{}】，发生异常【{}】", certPath, certKey, e.getMessage());
				e.printStackTrace();
				return HttpClients.custom().setDefaultRequestConfig(getConfig(null)).build();
			}
		} else {
			logger.info("创建CloseableHttpClient未使用证书");
			return createSSLClientDefault();
		}
	}

	/**
	 * 信任所有https证书请求
	 * @return
	 */
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(getConfig(null)).build();
		} catch (KeyManagementException e) {
			logger.error("创建https访问异常【{}】", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("创建https访问异常【{}】", e);
		} catch (KeyStoreException e) {
			logger.error("创建https访问异常【{}】", e);
		}
		return HttpClients.custom().setDefaultRequestConfig(getConfig(null)).build();
	}

	/**
	 * 创建CloseableHttpClient
	 * @param keyID 证书密钥
	 * @param keyPath 证书绝对路径
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient createSSLClientDefault(String certKey, String certPath) {

		RequestConfig config = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000).setStaleConnectionCheckEnabled(true).build();

		// 判断是否传入证书和密钥
		if (StringUtils.isNotBlank(certPath) && StringUtils.isNotBlank(certKey)) {
			logger.info("创建CloseableHttpClient使用证书，证书路径【{}】，证书密码【{}】", certPath, certKey);
			try {
				FileInputStream instream = new FileInputStream(new File(certPath));
				KeyStore keyStore = KeyStore.getInstance("PKCS12");
				try {
					keyStore.load(instream, certKey.toCharArray());
				} finally {
					if (instream != null) {
						instream.close();
					}
				}

				SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certKey.toCharArray())
						.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();

				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" },
						null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
				// 设置httpclient的SSLSocketFactory 

				return HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sslsf).build();
			} catch (Exception e) {
				logger.info("创建CloseableHttpClient使用证书，证书路径【{}】，证书密码【{}】，发生异常【{}】",
						new Object[] { certPath, certKey, e.getMessage() });
				e.printStackTrace();
				return HttpClients.custom().setDefaultRequestConfig(config).build();
			}
		} else {
			logger.info("创建CloseableHttpClient未使用证书");
			return HttpClients.custom().setDefaultRequestConfig(config).build();
		}
	}

	/**
	 * 拼接调用中台接口所需参数
	 */
	public static String getParamsSMS(String phone, String code, String key, String uid, String effectiveTime,
			String busitype) {
		// 参数串
		StringBuffer url = new StringBuffer("");
		try {
			String nonce = getStringRandom(20).toLowerCase();// 随机数
			if (StringUtils.isBlank(nonce)) {
				logger.info("生成随机数失败！");
				return url.toString();
			}
			long timestamp = System.currentTimeMillis();// 时间戳
			String signature = key + timestamp + nonce; // 签名
			signature = EncoderHandler.encodeByMD5(signature).toLowerCase(); // 加密后签名

			if (StringUtils.isBlank(signature)) {
				logger.info("加密签名失败！");
				return url.toString();
			}

			// 拼接参数串
			url.append("busiType=" + busitype);
			url.append("&phone=" + phone);
			url.append("&effectiveTime=" + effectiveTime);
			url.append("&data={'chk_code':'" + code + "'}");
			url.append("&uid=" + uid);
			url.append("&timestamp=" + timestamp + "");
			url.append("&nonce=" + nonce);
			url.append("&signature=" + signature);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("拼接调用中台接口参数串时报错！");
		}
		return url.toString();

	}

	// 生成随机数字和字母,
	public static String getStringRandom(int length) {

		String val = "";
		try {
			Random random = new Random();
			// 参数length，表示生成几位随机数
			for (int i = 0; i < length; i++) {
				String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
				// 输出字母还是数字
				if ("char".equalsIgnoreCase(charOrNum)) {
					// 输出是大写字母还是小写字母
					int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
					val += (char) (random.nextInt(26) + temp);
				} else if ("num".equalsIgnoreCase(charOrNum)) {
					val += String.valueOf(random.nextInt(10));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("生成随机数时报错！");
		}
		return val;
	}

	/**
	 * @description 通过HTTP协议向服务器发送请求,并得到响应结果，默认字符集:UTF-8。
	 * @param serviceUrl 服务器地址
	 * @param object 请求的参数，调用微信需传入json格式的数据String类型，
	 *               其他post请求需传入Map<String,String>类型
	 * @return String 返回数据
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	public static String post(String serviceUrl, Object object) {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		HttpPost httpPost = new HttpPost(serviceUrl);
		long start_time = System.currentTimeMillis();
		String result = null;
		try {
			if(object instanceof Map){
				 Map<String,String> pMap = (Map<String,String>) object;
				 List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		            for (Map.Entry<String, String> entry : pMap.entrySet()) {
		            	  nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		            }
		            httpPost.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			}else if(object instanceof String){
				String json = (String) object;
				httpPost.addHeader("Content-type","application/json; charset=utf-8");  
		        httpPost.setHeader("Accept", "application/json");
		        httpPost.setEntity(new StringEntity(json,"UTF-8"));
			}
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (200 == response.getStatusLine().getStatusCode()) {
				result = EntityUtils.toString(entity, "UTF-8").trim();
			} else {
			}
			return result;
		} catch (ConnectTimeoutException ex) {
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("post请求连接超时");
		} catch(SocketTimeoutException ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new SysException("post请求读取信息超时");
		} catch(Exception ex){
			logger.error("执行http请求，发生异常。异常信息如下：", ex);
			throw new BusinessException("系统异常，请联系保险公司！");
		} finally {
			logger.info("读取http请求返回信息，请求地址【{}】,出参【{}】,请求耗时【{}】", serviceUrl, result,
					(System.currentTimeMillis() - start_time));
			httpclient.getConnectionManager().shutdown();
		}
	}

}