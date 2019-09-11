package com.example.struct.interceptor;

import com.example.struct.util.CommonUtil;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口请求验证拦截器
 */
@Configuration
public class ValidateConfigurerAdapter implements WebMvcConfigurer {

	private static Logger logger = LoggerFactory.getLogger(ValidateConfigurerAdapter.class);
	/* 令牌桶限流器 100permits/s */
	private static RateLimiter rateLimiter = RateLimiter.create(80);
	private static final String VALID_TOKEN= CommonUtil.getProperty("valid-token");

	/**
	 * 拦截器 token校验 限流
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
				rateLimiter.acquire(1);
				httpServletResponse.setCharacterEncoding("utf-8");
				String validToken = httpServletRequest.getHeader("valid-token");
				return !StringUtils.isEmpty(validToken) && validToken.equals(VALID_TOKEN);
			}

			@Override
			public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
			}

			@Override
			public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
			}
		});
	}

}
