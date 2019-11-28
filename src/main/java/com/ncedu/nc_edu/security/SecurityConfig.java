package com.ncedu.nc_edu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Configuration
    public static class ApiSecurityAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/users").access("@securityUtils.isAdminOrModerator()")
                    .antMatchers("/users/{userId}/info").access("@securityUtils.isSelfOrGranted(#userId)")
                    .antMatchers("/users/{userId}/reviews").access("@securityUtils.isSelfOrGranted(#userId)")
                    .antMatchers(HttpMethod.PUT, "/users/{userId}").access("@securityUtils.isSelfOrGranted(#userId)")
                    .antMatchers(HttpMethod.PUT, "/recipes/{recipeId}/**").access("@securityUtils.isRecipesOwnerOrGranted(#recipeId)")
                    .antMatchers(HttpMethod.DELETE, "/recipes/{recipeId}/**").access("@securityUtils.isRecipesOwnerOrGranted(#recipeId)")
                    .antMatchers(HttpMethod.POST, "/ingredients/**").access("@securityUtils.isAdminOrModerator()")
                    .antMatchers(HttpMethod.PUT, "/ingredients/**").access("@securityUtils.isAdminOrModerator()")
                    .antMatchers(HttpMethod.DELETE, "/ingredients/**").access("@securityUtils.isAdminOrModerator()")

                    .antMatchers("/").permitAll()
                    .antMatchers("/register").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().cors();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
