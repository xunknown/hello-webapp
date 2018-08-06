package alpha.study.webapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class ServletExample
 */
public class ServletExample extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletExample.class);

	/**
	 * Default constructor.
	 */
	public ServletExample() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		try {
			response.getWriter().append("\n");
			response.getWriter().append(new GsonBuilder().setPrettyPrinting().create().toJson(request.getCookies()));
		} catch (Exception e) {
			// TODO: handle exception
			response.getWriter().append(ExceptionUtils.getStackTrace(e));
		}
		response.getWriter().append("\n");

    	LOGGER.trace(request.getMethod());
    	LOGGER.debug(request.getRequestURI());
    	LOGGER.info(request.getServletPath());
    	LOGGER.warn(request.getServerName());
    	LOGGER.error(request.getServletPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
