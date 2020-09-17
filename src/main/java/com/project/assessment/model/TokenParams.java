package com.project.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenParams {

	// m2m
	private String clientId;
	private String clientSecret;
	// enduser
	private String socialId;
	// admin
	private String auth0Id;
    //general
	private String authType;

}
