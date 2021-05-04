/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ricston.flights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.ApiInfo;

/**
 * Swagger configuration.
 * <p>Set the following properties in the {@code application.properties}:</p>
 * <ul>
 * <li>swagger.title_ws: Web-Service title</li>
 * <li>swagger.description_ws: Web-Service description</li>
 * <li>swagger.version_ws: Web-Service version</li>
 * <li>swagger.package_controllers: package where are the controllers located.</li>
 * </ul>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    @Value("${swagger.title_ws: Web-Service title}")
    private String title;

    @Value("${swagger.description_ws: Web-Service description missing.}")
    private String description;

    @Value("${swagger.version_ws: 1.0.0}")
    private String version;

    @Value("${swagger.package_controllers: com.sample.controllers}")
    private String packageControllers;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().
                apis(RequestHandlerSelectors.basePackage(this.
                packageControllers)).paths(PathSelectors.any()).
                build().apiInfo(this.apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(this.title).description(
                this.description).version(this.version).build();
    }
}