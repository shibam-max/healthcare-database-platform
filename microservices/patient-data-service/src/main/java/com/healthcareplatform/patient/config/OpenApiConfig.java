package com.healthcareplatform.patient.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration for Healthcare Platform
 * 
 * Demonstrates Oracle Health REST API documentation requirements:
 * - Comprehensive API documentation with OpenAPI 3
 * - Healthcare-specific security schemes
 * - Professional API presentation
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI healthcareOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Healthcare Semantic Database Platform API")
                        .description("Enterprise Healthcare Platform with Vector DB, Big Data & AWS Integration")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shibam Samaddar")
                                .email("shibam@healthcareplatform.com")
                                .url("https://github.com/shibam-max"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                .components(new Components()
                        .addSecuritySchemes("oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("OAuth2 JWT Token for Healthcare API Access")));
    }
}