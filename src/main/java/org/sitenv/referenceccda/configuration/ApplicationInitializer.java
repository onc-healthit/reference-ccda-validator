package org.sitenv.referenceccda.configuration;

import org.sitenv.vocabularies.configuration.CodeValidatorApiConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { MvcConfiguration.class, CodeValidatorApiConfiguration.class };
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

}