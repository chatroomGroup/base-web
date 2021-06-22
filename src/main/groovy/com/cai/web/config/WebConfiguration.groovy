package com.cai.web.config

import com.cai.web.interceptor.AuthInterceptor
import com.cai.web.interceptor.SessionInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfiguration implements WebMvcConfigurer{

    @Autowired
    SessionInterceptor sessionInterceptor

    @Autowired
    AuthInterceptor authInterceptor
    @Override
    void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
        registry.addInterceptor(sessionInterceptor)
    }

    @Override
    void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin")
                .allowCredentials(true);
    }
}
