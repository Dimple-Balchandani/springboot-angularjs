package com.vs.BaseProject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
@Configuration
@EnableWebMvc
public class AppConfig extends WebMvcConfigurerAdapter{

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// 	registry.addResourceHandler("/resources/**").addResourceLocations("WEB-INF/resources/");
	// }
	
	@Bean
	public ViewResolver getViewResolver(){
		 InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		 resolver.setPrefix("/WEB-INF/");
		 resolver.setSuffix(".html");
		 return resolver; 
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

}
