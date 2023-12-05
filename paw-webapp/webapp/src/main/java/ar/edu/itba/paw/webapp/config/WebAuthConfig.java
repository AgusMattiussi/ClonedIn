package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.JwtAuthenticationFilter;
import ar.edu.itba.paw.webapp.auth.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.security"})
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthUserDetailsService userDetailsService;
    /*@Autowired
    private JwtAuthenticationProvider authenticationProvider;*/

    //TODO: ACTUALIZAR LOS URLS
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                // Create and Authorize
                .antMatchers("/auth/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/users").anonymous()
                .antMatchers(HttpMethod.POST, "/enterprises").anonymous()
                // Users
                .antMatchers(HttpMethod.GET, "/users").authenticated()//.hasAuthority(Role.ENTERPRISE.name())
                .antMatchers("/test").authenticated()
                .antMatchers(HttpMethod.GET,
                        "/test/{id}",
                        "/users/{id}",
                        "/users/{id}/experiences/**",
                        "/users/{id}/educations/**",
                        "/users/{id}/skills/**",
                        "/users/{id}/image").authenticated() // TODO: Cambiar por Authenticated Y Visible
                .antMatchers(HttpMethod.GET,
                        "/users/{id}/applications",
                        "/users/{id}/notifications").authenticated() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.POST,
                        "/users/{id}/applications",
                        "/users/{id}/experiences",
                        "/users/{id}/educations",
                        "/users/{id}/skills").authenticated() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.PUT,
                        "/users/{id}/applications/{jobOfferId}",
                        "/users/{id}/notifications/{jobOfferId}",
                        "/users/{id}/profile",
                        "/{id}/visibility",
                        "/users/{id}/image").authenticated() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.DELETE,
                        "/users/{id}/experiences/{expId}",
                        "/users/{id}/educations/{educationId}",
                        "/users/{id}/skills/{skillId}").authenticated() // TODO: Cambiar para verificar mismo usuario
                // Enterprises
                .antMatchers(HttpMethod.GET,
                        "/enterprises/{id}",
                        "/enterprises/{id}/jobOffers",
                        "/enterprises/{id}/jobOffers/{joid}").hasAuthority(Role.ENTERPRISE.name())
                .antMatchers(HttpMethod.GET,
                        "/enterprises/{id}/contacts",
                        "/enterprises/{id}/contacts/{joid}",
                        "/enterprises/{id}/contacts/{joid}/{uid}").authenticated() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.POST,
                        "/enterprises/{id}/jobOffers",
                        "/enterprises/{id}/contacts").authenticated() // TODO: Cambiar para verificar mismo usuario
                .antMatchers(HttpMethod.PUT, "/enterprises/{id}/jobOffers/{joid}").authenticated() // TODO: Cambiar para verificar mismo usuario
                // Categories
                .antMatchers(HttpMethod.GET, "/categories").authenticated()
                // JobOffers
                .antMatchers(HttpMethod.GET, "/jobOffers/**").hasAuthority(Role.USER.name())
                .and().exceptionHandling()
                    .accessDeniedHandler((request, response, ex) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN))
                    .authenticationEntryPoint((request, response, ex) -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                //.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
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

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //auth.authenticationProvider(authenticationProvider);
    }










}