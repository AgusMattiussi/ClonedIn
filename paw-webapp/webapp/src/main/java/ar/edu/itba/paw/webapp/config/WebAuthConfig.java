package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtFilter;
import ar.edu.itba.paw.webapp.auth.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.models"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    /*
    @Autowired
    private JwtFilter jwtFilter;
    */

    //TODO: ACTUALIZAR LOS URLS
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                    // Users
                .antMatchers(HttpMethod.GET, "/users").permitAll() // TODO: Cambiar por Authenticated y Empresa
                .antMatchers(HttpMethod.GET,
                        "/users/{id}",
                        "/users/{id}/experiences/**",
                        "/users/{id}/educations/**",
                        "/users/{id}/skills/**",
                        "/users/{id}/image").permitAll() // TODO: Cambiar por Authenticated Y Visible
                .antMatchers(HttpMethod.GET,
                        "/users/{id}/applications",
                        "/users/{id}/notifications").permitAll() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.POST, "/users").anonymous()
                .antMatchers(HttpMethod.POST,
                        "/users/{id}/applications",
                        "/users/{id}/experiences",
                        "/users/{id}/educations",
                        "/users/{id}/skills").permitAll() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.PUT,
                        "/users/{id}/applications/{jobOfferId}",
                        "/users/{id}/notifications/{jobOfferId}",
                        "/users/{id}/profile",
                        "/{id}/visibility",
                        "/users/{id}/image").permitAll() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.DELETE,
                        "/users/{id}/experiences/{expId}",
                        "/users/{id}/educations/{educationId}",
                        "/users/{id}/skills/{skillId}").permitAll() // TODO: Cambiar para verificar mismo usuario
                // Enterprises
                .antMatchers(HttpMethod.GET,
                        "/enterprises/{id}",
                        "/enterprises/{id}/jobOffers",
                        "/enterprises/{id}/jobOffers/{joid}").permitAll() // TODO: Cambiar por Authenticated y Usuario
                .antMatchers(HttpMethod.GET,
                        "/enterprises/{id}/contacts",
                        "/enterprises/{id}/contacts/{joid}",
                        "/enterprises/{id}/contacts/{joid}/{uid}").permitAll() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.POST, "/enterprises").anonymous()
                .antMatchers(HttpMethod.POST,
                        "/enterprises/{id}/jobOffers",
                        "/enterprises/{id}/contacts").permitAll() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.PUT, "/enterprises/{id}/jobOffers/{joid}").permitAll() // TODO: Cambiar para verificar mismo usuario
                // Categories
                .antMatchers(HttpMethod.GET, "/categories").permitAll()
                // JobOffers
                .antMatchers(HttpMethod.GET, "/jobOffers/**").permitAll() // TODO: Cambiar por Authenticated y Usuario
                .and().exceptionHandling()
                    .accessDeniedHandler((request, response, ex) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                    .authenticationEntryPoint((request, response, ex) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                /*.and().addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()*/;
    }

    private String loadRememberMeKey() {
        try (Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("rememberme.key"))) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //FIXME: Revisar esto
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers( "/assets/css/**", "/assets/js/**", "/assets/images/**",
                "/views/403", "/views/404","/views/500");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // React Frontend
        cors.setAllowedMethods(Collections.singletonList("*"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setExposedHeaders(Arrays.asList("Authorization", "X-Refresh", "Location", "Link", "X-Total-Pages"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new SimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}