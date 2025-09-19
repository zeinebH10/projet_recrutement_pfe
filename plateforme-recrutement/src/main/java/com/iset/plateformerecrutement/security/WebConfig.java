package com.iset.plateformerecrutement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ComponentScan
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
     ImageCorsInterceptor imageCorsInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/Content/**").addResourceLocations("file:/var/www/uploads/");


    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(imageCorsInterceptor).addPathPatterns("/Content/**");
    }



}
