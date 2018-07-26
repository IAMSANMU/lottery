package com.lottery.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class HttpUtil {

	private static Logger logger = Logger.getLogger(HttpUtil.class);

	public static  String getUrl(String urlString) {
		return getUrl(urlString, "GBK");
	}
	public static  String getUrlUtf8(String urlString) {
		return getUrl(urlString, "UTF-8");
	}
	public static  String getUrl(String urlString, String charsetCode) {

		URL url = null;
		URLConnection connection = null;
		InputStream in = null;

		if (urlString != null && !urlString.trim().startsWith("http:")) {
			logger.error("http tools: 非法请求(" + urlString + ")");
			return "";
		}

		try {
			url = new URL(urlString);
			// 匹配url为500的 就用代理
			Pattern pattern500 = Pattern.compile("\\.(500|500wan)\\.");
			Matcher matcher500 = pattern500.matcher(urlString);

			// 匹配url为ydniu的 就用代理
			Pattern patternYdniu = Pattern.compile("\\.(ydniu)\\.");
			Matcher matcherYdniu = patternYdniu.matcher(urlString);

			// if(matcher500.find()){
			// SocketAddress addr = new
			// InetSocketAddress("117.177.250.152",80);//代理地址
			// Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
			// connection = url.openConnection(typeProxy);
			// } else if(matcherYdniu.find()){
			// SocketAddress addr = new
			// InetSocketAddress("202.100.167.159",80);//代理地址
			// Proxy typeProxy = new Proxy(Proxy.Type.HTTP, addr);
			// connection = url.openConnection(typeProxy);
			// }else{
			connection = url.openConnection();
			// }

			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			connection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			connection.setRequestProperty("Referer", urlString);
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");

			String charset = charsetCode; // default IE charset
			String encoding = "";
			if (connection instanceof HttpsURLConnectionImpl) {

				HttpsURLConnection https = (HttpsURLConnection) connection;
				https.setHostnameVerifier(new MyVerified());

				X509TrustManager xtm = new MyTrustManager();
				TrustManager mytm[] = { xtm };
				SSLContext ctx = SSLContext.getInstance("SSL");
				ctx.init(null, mytm, null);
				SSLSocketFactory sf = ctx.getSocketFactory();
				https.setSSLSocketFactory(sf);

			}
			if (connection instanceof HttpURLConnection) {

				HttpURLConnection http = (HttpURLConnection) connection;
				encoding = http.getContentEncoding();

			}
			Map headers = connection.getHeaderFields();
			if (headers.size() > 0) {
				String response = headers.get(null).toString();
				if (response.indexOf("200 OK") < 0) {
					throw new Exception("读取地址:" + url + " 错误:" + response);
				}
				try {
					String contentType = headers.get("Content-Type").toString().replaceAll("\\[|\\]|\\\"", "");
					String ct[] = contentType.split(";");
					if (ct.length > 1) {
						String[] cs = ct[1].split("=");
						if (cs.length > 1) {
							charset = cs[1];
						}
					}
				} catch (Exception e) {
					logger.error(e.getMessage() + "");
				}
			}

			if (("gzip").equals(encoding)) {
				in = new GZIPInputStream(connection.getInputStream());
			} else {
				in = connection.getInputStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
			StringBuffer sb = new StringBuffer();
			String temp = "";
			while ((temp = reader.readLine()) != null) {
				sb.append(temp + "\r\n");
			}
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {

			}
		}
		return null;
	}

}

class MyVerified implements HostnameVerifier {
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}

class MyTrustManager implements X509TrustManager {
	MyTrustManager() { // constructor
		// create/load keystore
	}

	public void checkClientTrusted(X509Certificate chain[], String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate chain[], String authType) throws CertificateException {
		// special handling such as poping dialog boxes
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
