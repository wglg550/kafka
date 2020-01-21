package distributed.transaction.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * url 处理工具
 *
 * @author WANGWEI
 */
public class UrlUtil {
	/**
	 * 获取url参数
	 *
	 * @author WANG WEI
	 *
	 * @param str
	 * @return
	 */
	public static Map<String, String> getUrlParams(String url) {
		Map<String, String> params = new HashMap<String, String>();

		try {
			int indexOf = url.lastIndexOf('?');

			if (0 < indexOf) {
				url = url.substring(indexOf);
			}
			String[] pairs = url.trim().split("&");
			for (String pair : pairs) {
				String[] kv = pair.split("=");
				if (2 != kv.length) {
					continue;
				}
				params.put(kv[0], URLDecoder.decode(kv[1], "UTF-8"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return params;
	}

	/**
	 * 编码
	 *
	 * @author WANGWEI
	 * @param s
	 * @return
	 */
	public static String encode(String s) {
		return encode(s, "UTF-8");
	}

	/**
	 * 编码
	 *
	 * @author WANGWEI
	 * @param s
	 * @param enc
	 * @return
	 */
	public static String encode(String s, String enc) {
		try {
			return URLEncoder.encode(s, enc).replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 解码
	 *
	 * @author WANGWEI
	 * @param s
	 * @return
	 */
	public static String decode(String s) {
		return decode(s, "UTF-8");
	}

	/**
	 * 解码
	 *
	 * @author WANGWEI
	 * @param s
	 * @param enc
	 * @return
	 */
	public static String decode(String s, String enc) {
		try {
			return URLDecoder.decode(s, enc);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 方法注释
	 *
	 * @author WANGWEI
	 * @param fragments
	 * @return
	 */
	public static String joinUrl(String... fragments) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fragments.length; i++) {
			String cur = fragments[i];
			if (cur.endsWith("/")) {
				cur = cur.substring(0, cur.length() - 1);
			}
			if (0 == i) {
				sb.append(cur);
			} else if (cur.startsWith("/")) {
				sb.append(cur);
			} else {
				sb.append("/").append(cur);
			}
		}

		return sb.toString();
	}

}
