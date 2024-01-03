package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.CustomAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.JwtAuthenticationFilter;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
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


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                // Create and Authorize
                .antMatchers("/auth/access-token").authenticated()
                .antMatchers("/auth/refresh-token").anonymous()
                .antMatchers("/test/**").authenticated()
                .antMatchers(HttpMethod.POST, "/users").anonymous()
                .antMatchers(HttpMethod.POST, "/enterprises").anonymous()
                // Users and Enterprises
                .antMatchers("/users", "/users/**").authenticated()
                .antMatchers("/enterprises", "/enterprises/**").authenticated()
                // Categories
                .antMatchers("/categories").permitAll()
                // JobOffers
                .antMatchers("/jobOffers", "/jobOffers/**").authenticated()
                .and().exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                .and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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
        cors.setExposedHeaders(Arrays.asList("Authorization", "X-Refresh", "Location", "Link", "X-Total-Pages", "X-Access-Token"));
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

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //auth.authenticationProvider(authenticationProvider);
    }










}