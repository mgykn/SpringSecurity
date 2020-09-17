package com.project.assessment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.project.assessment.exception.FilterChainExceptionHandler;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private FilterChainExceptionHandler filterChainExceptionHandler;
	
	@Autowired
    private HandlerExceptionResolver handlerExceptionResolver;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disable CSRF (cross site request forgery)
		http.cors().and().csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
		http.exceptionHandling().accessDeniedHandler((req,  res, e) -> { handlerExceptionResolver.resolveException(req, res, null, e);});
		
		// Entry points
		http.authorizeRequests()
				.antMatchers("/**/signin/**", "/**/v2/api-docs/**", "/**/swagger-ui.html#/**").permitAll()
				.antMatchers("/**/todo/**").hasAnyAuthority("M2m","Admin","EndUser")
				.antMatchers("/**/access/**").anonymous()
				.anyRequest().authenticated();		

		// Apply JWT
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));


	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow eureka client to be accessed without authentication
		web.ignoring().antMatchers("/*/")//
				.antMatchers("/eureka/**")//
				.antMatchers(HttpMethod.OPTIONS, "/**"); // Request type options
															// should be
															// allowed.
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

}
