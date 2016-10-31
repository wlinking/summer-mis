package cn.cerc.jmis.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cerc.jbean.core.Application;
import cn.cerc.jbean.core.AppConfig;

public class StartApp implements Filter {
	// private static final Logger log = Logger.getLogger(AppStart.class);
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = req.getRequestURI();

		// 处理默认首页问题
		if (uri.equals("/")) {
			AppConfig conf = Application.getConfig();
			resp.sendRedirect(String.format("/%s/%s", conf.getPathForms(), conf.getFormWelcome()));
			return;
		}

		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}
}