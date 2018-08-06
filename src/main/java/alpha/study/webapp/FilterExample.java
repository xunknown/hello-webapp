package alpha.study.webapp;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Servlet Filter implementation class FilterExample
 */
public class FilterExample implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterExample.class);

	/**
	 * Default constructor.
	 */
	public FilterExample() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		response.getWriter().append("FilterExample");
		try {
			if (request instanceof HttpServletRequest) {
				response.getWriter().append("\n");
				response.getWriter().append("HttpServletRequest");
				response.getWriter().append("\n");
				response.getWriter().append(request.getClass().getName());
				response.getWriter().append("\n");
				response.getWriter().append(((HttpServletRequest) request).getRequestURL());
				response.getWriter().append("\n");
				response.getWriter().append(((HttpServletRequest) request).getRequestURI());
				String content = ((HttpServletRequest) request).getRequestURI().toString();
				String pattern = "(.*Servlet.*)(.*Example.*)";
				boolean isMatch = Pattern.matches(pattern, content);
				response.getWriter().append("\n");
				response.getWriter().append("regex Pattern :" + isMatch);
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(content);
				if (m.find()) {
					response.getWriter().append("\n");
					response.getWriter().append("regex Pattern Found value: " + m.group(0));
					response.getWriter().append("\n");
					response.getWriter().append("regex Pattern Found value: " + m.group());
					for (int i = 0; i <= m.groupCount(); i++) {
						response.getWriter().append("\n");
						response.getWriter().append("regex Pattern Found value(" + i + "): " + m.group(i));
					}
				}
				
//				final long mask = 0xAFBB1234D678ECFFL;
//				Cookie cookie = null;
//				Cookie[] cookies = null;
//				cookies = ((HttpServletRequest) request).getCookies();
//				if (cookies != null) {
//					for (int i = 0; i < cookies.length; i++) {
//						cookie = cookies[i];
//						if ((cookie.getName()).compareTo("time") == 0) {
//							LOGGER.info("cookie get time: {}", Long.parseLong(cookie.getValue())^mask);
//							break;
//						}
//					}
//				}
//
//				long now = System.currentTimeMillis();
//				cookie = new Cookie("time", "" + (now ^ mask));
//				((HttpServletResponse) response).addCookie(cookie);
//				response.getWriter().append("cookie set time:" + now);
//				LOGGER.info("cookie set time: {}", now);

				Cookie cookie = null;
				Cookie[] cookies = null;
				cookies = ((HttpServletRequest) request).getCookies();
				if (cookies != null) {
					for (int i = 0; i < cookies.length; i++) {
						cookie = cookies[i];
						if ((cookie.getName()).compareTo("time") == 0) {
							LOGGER.info("cookie get time: {}", Mask.decodeTime(Long.parseLong(cookie.getValue())));
							break;
						}
					}
				}

				long now = Mask.encodeTime();
				cookie = new Cookie("time", "" + now);
				((HttpServletResponse) response).addCookie(cookie);
				response.getWriter().append("cookie set time:" + Mask.decodeTime(now));
				LOGGER.info("cookie set time: {}", Mask.decodeTime(now));
			}
		} catch (Exception e) {
			// TODO: handle exception
			response.getWriter().append(ExceptionUtils.getStackTrace(e));
		}
		response.getWriter().append("\n");

		LOGGER.trace(request.getLocalAddr());
		LOGGER.debug(request.getLocalName());
		LOGGER.info(request.getServerName());
		LOGGER.warn(request.getScheme());
		LOGGER.error(request.getProtocol());

//		long key = Mask.buildKey();
//		long id = Mask.buildlId();
//		long index = 0;
//		LOGGER.info("key: {}, id: {}, index: {}", key, id >> 20, index);
//		long data = Mask.build(key, id, index);
//		LOGGER.info("key: {}, id: {}, index: {}", Mask.decodeKey(data), Mask.decodeId(data), Mask.decodeIndex(data));
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
