package com.leyou.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * Classe utilitaire de cookies
 *
 */
public final class CookieUtils {

	static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

	/**
	 * Obtenir la valeur du cookie sans encodage
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		return getCookieValue(request, cookieName, false);
	}

	/**
	 * Obtenir la valeur du cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null){
			return null;			
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					if (isDecoder) {
						retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
					} else {
						retValue = cookieList[i].getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Cookie Decode Error.", e);
		}
		return retValue;
	}

	/**
	 * Obtenir la valeur du cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null){
			return null;			
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Erreur de décodage de cookie.", e);
		}
		return retValue;
	}

	/**
	 * Créer un cookie et spécifier l'encodage
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param encodeString
	 */
	public static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, String encodeString) {
		setCookie(request,response,cookieName,cookieValue,null,encodeString, null);
	}

	/**
	 * Créer un cookie et spécifier la durée de vie
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxAge
	 */
	public static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge) {
		setCookie(request,response,cookieName,cookieValue,cookieMaxAge,null, null);
	}

	/**
	 * Définir un cookie sans spécifier l'attribut httpOnly
	 */
	public static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge, String encodeString) {
		setCookie(request,response,cookieName,cookieValue,cookieMaxAge,encodeString, null);
	}

	/**
	 * Définir la valeur du cookie et la faire valider pendant une période spécifiée
	 * 
	 * @param cookieMaxAge
	 *
	 */
	public static final void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, Integer cookieMaxAge, String encodeString, Boolean httpOnly) {
		try {
			if(StringUtils.isBlank(encodeString)) {
				encodeString = "utf-8";
			}

			if (cookieValue == null) {
				cookieValue = "";
			} else {
				cookieValue = URLEncoder.encode(cookieValue, encodeString);
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxAge != null && cookieMaxAge > 0)
				cookie.setMaxAge(cookieMaxAge);
			if (null != request)// 设置域名的cookie
				cookie.setDomain(getDomainName(request));
			cookie.setPath("/");

			if(httpOnly != null) {
				cookie.setHttpOnly(httpOnly);
			}
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error("Erreur d'encodage de cookie.", e);
		}
	}

	/**
	 * Récupérer le domaine du cookie
	 */
	private static final String getDomainName(HttpServletRequest request) {
		String domainName = null;

		String serverName = request.getRequestURL().toString();
		if (serverName == null || serverName.equals("")) {
			domainName = "";
		} else {
			serverName = serverName.toLowerCase();
			serverName = serverName.substring(7);
			final int end = serverName.indexOf("/");
			serverName = serverName.substring(0, end);
			final String[] domains = serverName.split("\\.");
			int len = domains.length;
			if (len > 3) {
				// www.xxx.com.cn
				domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
			} else if (len <= 3 && len > 1) {
				// xxx.com or xxx.cn
				domainName = domains[len - 2] + "." + domains[len - 1];
			} else {
				domainName = serverName;
			}
		}

		if (domainName != null && domainName.indexOf(":") > 0) {
			String[] ary = domainName.split("\\:");
			domainName = ary[0];
		}
		return domainName;
	}

}