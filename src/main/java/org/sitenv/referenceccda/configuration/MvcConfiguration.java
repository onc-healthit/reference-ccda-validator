package org.sitenv.referenceccda.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@EnableCaching
@Configuration
@ComponentScan("org.sitenv")
@PropertySource(value = "/META-INF/maven/org.sitenv/referenceccdavalidator/pom.properties", ignoreResourceNotFound=true)
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    /**
     * Spring 6 removed CommonsMultipartResolver along with its
     * commons-fileupload dependency. StandardServletMultipartResolver delegates
     * to the container's Servlet 3.0+ multipart parsing instead; the size
     * limits live on the servlet registration, in ApplicationInitializer.
     */
    @Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("messagetypeValidationObjectivesAndReferenceFilesMap");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ui").setViewName("redirect:static/validationui.html");
    }
}
