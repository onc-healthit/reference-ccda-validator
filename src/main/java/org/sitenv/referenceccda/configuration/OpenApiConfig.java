package org.sitenv.referenceccda.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Brian on 8/18/2016.
 *
 * Migrated from springfox to springdoc-openapi. springfox was last released in
 * 2020, ships the CVE-2019-17495 Swagger UI XSS, and is incompatible with
 * Spring 6 (it targets javax.servlet and Spring 5 internals).
 *
 * springfox's Docket with RequestHandlerSelectors.any() / PathSelectors.any()
 * documented every endpoint, which is springdoc's default behaviour, so only
 * the API info needs declaring here. The springdoc configuration classes that
 * Spring Boot would normally auto-register are listed in ApplicationInitializer
 * instead, because this application does not use Boot.
 *
 * The UI moves from /swagger-ui.html to /swagger-ui/index.html (springdoc keeps
 * /swagger-ui.html working as a redirect), and the OpenAPI document is served
 * from /v3/api-docs rather than springfox's /v2/api-docs.
 *
 * Named OpenApiConfig rather than SwaggerConfig deliberately: springdoc ships
 * its own org.springdoc.webmvc.ui.SwaggerConfig, which ApplicationInitializer
 * has to register by hand. Spring derives bean names from the simple class
 * name, so two classes both called SwaggerConfig collide on "swaggerConfig" and
 * one silently overwrites the other.
 */
@Configuration
public class OpenApiConfig {
    @Value("${version:none}")
    private String applicationPomVersion;

    /**
     * springdoc's SwaggerWebMvcConfigurer injects these two Spring Boot
     * @ConfigurationProperties beans to work out static resource locations and
     * the servlet path. Boot would contribute them from its own
     * auto-configuration; there is no Boot here, so the defaults are supplied
     * directly. These two are the only Boot beans springdoc actually requires.
     */
    @Bean
    public WebProperties webProperties() {
        return new WebProperties();
    }

    @Bean
    public WebMvcProperties webMvcProperties() {
        return new WebMvcProperties();
    }

    @Bean
    public OpenAPI referenceCcdaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Reference C-CDA Validator API Documentation")
                .description("The goal of the Reference C-CDA Validator is to validate conformance of C-CDA documents to the standard in order to promote interoperability. The validator is also a resource that can evaluate submitted C-CDA documents conformance to ONC 2014 and 2015 Edition Standards and Certification Criteria (S&CC) objectives and regulations.")
                .version(applicationPomVersion));
    }
}
