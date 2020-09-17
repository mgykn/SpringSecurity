package com.project.assessment.config.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private final Predicate<String> loginApiRegex = PathSelectors.regex("/cms/v1/login.*");
	
	@Bean
	public Docket customerApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Assesment-Service").select()
				.apis(RequestHandlerSelectors.basePackage("com.project.assessment"))
				.paths(PathSelectors.any()).paths(Predicates.not(loginApiRegex)).build().apiInfo(metaData());
	}

	private static final Contact CONTACT = new Contact("Muge Yakin","mugeyakin@gmail.com",null);

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Assesment Service").description("Assesment Service").version("1.0")
				.license("Apache License Version 2.0").licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
				.contact(CONTACT).build();
	}

	@Bean
	public UiConfiguration uiConfiguration() {
		return UiConfigurationBuilder.builder().deepLinking(true).validatorUrl(null).build();
	}

}
