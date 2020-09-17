package com.project.assessment.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.project.assessment.exception.UnauthorizedException;
import com.project.assessment.model.TokenParams;
import com.project.assessment.service.ActionService;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilterBean {

	/*
	 * This generic filter base class has no dependency on the Spring
	 * org.springframework.context.ApplicationContext concept. Filters usually don't
	 * load their own context but rather access service beans from the Spring root
	 * application context, accessible via the filter's ServletContext (see
	 * org.springframework.web.context.support.WebApplicationContextUtils).
	 * 
	 * // we cannot AUTOWIRED a component in GenericFilterBean //@Autowired
	 * //private MessageProperties messages;
	 */

	@Autowired
	private ActionService service;

	private JwtTokenProvider jwtTokenProvider;
	
	@Value("${header.key: x-auth-id}")
	private String headerAuthKey;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		// HttpServletResponse response = (HttpServletResponse) res;

		String requestURI = request.getRequestURI();
		String token = getBearerToken((HttpServletRequest) req);

		if (token != null) {
			TokenParams params = null;
			try {
				params = this.jwtTokenProvider.validateToken(token);
			} catch (JwtException | IllegalArgumentException e) {
				log.warn("Invalid Token: {}, Error: {}", params, e.getMessage());
				// response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "INVALID JWT token");
				// return;
				throw new UnauthorizedException();
			}
			String authType = request.getHeader("authType");
			HeaderMapRequestWrapper wrappedRequest = new HeaderMapRequestWrapper(request);

			if (authType.equalsIgnoreCase("M2M")) {
				boolean isM2m = service.findById(params.getClientId());
				if (!isM2m) {
					log.info("User could not found m2m user with this clientId: {}", params.getClientId());
					// ne dönmeliyim
					throw new UnauthorizedException();

				}
				wrappedRequest.addHeader(headerAuthKey, "M2M" + "/" + params.getClientId());

				if (requestURI.contains("clientId") && requestURI.contains("clientSecret")) {

					wrappedRequest.addHeader("clientId", params.getClientId());
					wrappedRequest.addHeader("clientSecret", params.getClientSecret());

				}
			} else if (authType.equalsIgnoreCase("EndUser")) {
				wrappedRequest.addHeader(headerAuthKey, "Social" + "/" + params.getSocialId());

			} else if (authType.equalsIgnoreCase("Admin")) {
				wrappedRequest.addHeader(headerAuthKey, "Admin" + "/" + params.getAuth0Id());

			} else {
				wrappedRequest.addHeader(headerAuthKey, "free");
			}

			Authentication auth = this.jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);

			filterChain.doFilter(wrappedRequest, res);

		} else {
			filterChain.doFilter(req, res);
		}
	}

	private static final String AUTHORIZATION = "Authorization";

	private String getBearerToken(HttpServletRequest req) {
		String bearerToken = req.getHeader(AUTHORIZATION);
		/*
		 * if (bearerToken != null && bearerToken.startsWith("Bearer ")) { return
		 * bearerToken.substring(7, bearerToken.length()); }
		 */
		if (bearerToken != null) {
			return bearerToken;
		}
		return null;
	}

}
