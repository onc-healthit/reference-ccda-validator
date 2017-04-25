package org.sitenv.referenceccda.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Brian on 8/18/2016.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${version:none}")
    private String applicationPomVersion;

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Reference C-CDA Validator API Documentation")
                .description("The goal of the Reference C-CDA Validator is to validate conformance of C-CDA documents to the standard in order to promote interoperability. The validator is also a resource that can evaluate submitted C-CDA documents conformance to ONC 2014 and 2015 Edition Standards and Certification Criteria (S&CC) objectives and regulations.")
                .version(applicationPomVersion)
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
}
