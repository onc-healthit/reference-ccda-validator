package org.sitenv.referenceccda.configuration;

import org.sitenv.contentvalidator.configuration.ContentValidatorApiConfiguration;
import org.sitenv.vocabularies.configuration.CodeValidatorApiConfiguration;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Matches the previous CommonsMultipartResolver defaults so upload
	 * behaviour does not change: no cap on file or request size, and anything
	 * over 10KB spools to a temp file rather than staying in memory.
	 */
	private static final long MAX_FILE_SIZE = -1L;
	private static final long MAX_REQUEST_SIZE = -1L;
	private static final int FILE_SIZE_THRESHOLD = 10240;

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { MvcConfiguration.class, CodeValidatorApiConfiguration.class,
				ContentValidatorApiConfiguration.class, OpenApiConfig.class,
				// Spring Boot would auto-register these; this is a plain Spring
				// MVC WAR, so springdoc needs them wired up explicitly.
				SpringDocConfiguration.class, SpringDocConfigProperties.class,
				SpringDocSpecPropertiesConfiguration.class, SpringDocWebMvcConfiguration.class,
				MultipleOpenApiSupportConfiguration.class, SwaggerUiConfigProperties.class,
				SwaggerUiOAuthProperties.class, org.springdoc.webmvc.ui.SwaggerConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);
    }

	/**
	 * StandardServletMultipartResolver reads its limits from the servlet
	 * registration rather than from a Spring bean, so the multipart config has
	 * to be attached here.
	 */
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setMultipartConfig(
				new MultipartConfigElement(null, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD));
	}

}
