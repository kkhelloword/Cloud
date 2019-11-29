package com.imooc.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Component
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
      final   UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      final   CorsConfiguration config = new CorsConfiguration();

      config.setAllowCredentials(true);
      config.setAllowedHeaders(Arrays.asList("*"));
      config.setAllowedMethods(Arrays.asList("*"));
      config.setAllowedOrigins(Arrays.asList("*"));
      source.registerCorsConfiguration("/**",config);
      return new CorsFilter(source);
    }
}
