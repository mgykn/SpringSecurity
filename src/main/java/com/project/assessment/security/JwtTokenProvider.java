package com.project.assessment.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.project.assessment.model.TokenParams;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	private static final String AUTH = "auth";
	private static final String CLIENTID = "cid";
	private static final String CLIENTSECRET = "cs";
	private static final String SOCIALID = "sid";

	@Autowired
	private Environment env;

	private String secretKey;

	@Value("${jwt.validity-in-seconds: 3600}")
	private int validityInSeconds;

	@PostConstruct
	protected void init() {
		this.secretKey = Base64.getEncoder().encodeToString(this.env.getProperty("jwt.secret-key").getBytes());
	}

	public String createToken(TokenParams tokenParams) {
		String role = tokenParams.getAuthType();
		String clientId = tokenParams.getClientId();
		String socialId = tokenParams.getSocialId();
		String clientSecret = tokenParams.getClientSecret();

		Claims claims = Jwts.claims().setSubject(clientId);
		claims.put(AUTH, role);
		claims.put(CLIENTID, clientId);
		claims.put(SOCIALID, socialId);
		claims.put(CLIENTSECRET, clientSecret);

		Date now = new Date();

		Date validity = new Date(now.getTime() + validityInSeconds * 1000);

		String token = Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, this.secretKey)//
				.compact();

		return token;
	}

	public TokenParams validateToken(String token) throws JwtException, IllegalArgumentException {
		Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
		@SuppressWarnings("unchecked")
		String roleList = (String) parseClaimsJws.getBody().get(AUTH);
		String socialId = (String) parseClaimsJws.getBody().get(SOCIALID);
		String clientSecret = (String) parseClaimsJws.getBody().get(CLIENTSECRET);
		Object clientId = parseClaimsJws.getBody().get(CLIENTID);

		return TokenParams.builder().clientId((String) clientId).clientSecret((String) clientSecret).socialId((String) socialId).authType(roleList).build();
	}

	// user details with out database hit
	public UserDetails getUserDetails(String token) {
		Jws<Claims> parseClaimsJws = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token);
		String userEmail = parseClaimsJws.getBody().getSubject();
		@SuppressWarnings("unchecked")
		List<String> roleList = (List<String>) parseClaimsJws.getBody().get(AUTH);
		UserDetails userDetails = new DeserializeUserDetails(userEmail, roleList.toArray(new String[roleList.size()]));
		return userDetails;
	}

	public Authentication getAuthentication(String token) {
		// using data base: uncomment when you want to fetch data from data base
		// UserDetails userDetails =
		// userDetailsService.loadUserByUsername(getUsername(token));
		// from token take user value. comment below line for changing it taking
		// from data base
		UserDetails userDetails = getUserDetails(token);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

}
