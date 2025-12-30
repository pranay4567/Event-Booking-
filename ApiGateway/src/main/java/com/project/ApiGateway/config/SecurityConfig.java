package com.project.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.print.attribute.standard.NumberUpSupported;

@Configuration
public class SecurityConfig {

    @Value("${keycloak.auth.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${security.excluded.urls}")
    private String[] excludedUrls;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.authorizeHttpRequests(authorizationRequests->
                authorizationRequests
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/booking/v3/api-docs",
                                "/inventory/v3/api-docs"
                        ).permitAll()

                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth->oauth.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
