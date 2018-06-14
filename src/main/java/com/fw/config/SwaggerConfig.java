package com.fw.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Narendra Gurjar
 *
 */

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "com.fw.controller.impl" })

public class SwaggerConfig {
    @Bean
    public Docket mLogisticsBidAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Bids APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("bid"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    private ApiKey apiKey() {
        return new ApiKey("authkey", "Authorization", "header");
      }
    @Bean
    public Docket mLogisticsRequestAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("LoadRequest APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("load"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    @Bean
    public Docket mLogisticsUsersAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Users APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("users"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    @Bean
    public Docket mLogisticsPaymentAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Payment APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("payment"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    @Bean
    public Docket mLogisticsDashboardAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Dashboard APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("dashboard"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    @Bean
    public Docket mLogisticsMarketingAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Marketing APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .paths(includePath("marketing"))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
    @Bean
    public Docket mLogisticsAllAPIs() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("All APIs")
                .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
                .build()
                .apiInfo(apiInfo())
                 .securitySchemes(Arrays.asList(apiKey()));
    }
    
    private  Predicate<String> excludePath(final String path) {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return !input.contains(path);
            }
        };
    }

    private Predicate<String> includePath(final String path) {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains(path);
            }
        };
    }

    private Predicate<RequestHandler> excludeClass(final Class<?> clazz) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return !input.getHandlerMethod().getBeanType().equals(clazz);
            }
        };
    }
    
    private ApiInfo apiInfo() {
         return new ApiInfo(
           "mLogitcis APIs", 
           "API documentation", 
           "API TOS", 
           "Terms of service", 
           new Contact("Faberwork", "www.mlogistics.com", "team@faberwork.com"), 
           "License of API", "www.mlogistics.biz", Collections.emptyList());
    }
   /*     .select().apis(RequestHandlerSelectors.basePackage("com.fw.controller.impl"))
        .paths(regex("/product.*"))
        .build();*/
             
    
}
