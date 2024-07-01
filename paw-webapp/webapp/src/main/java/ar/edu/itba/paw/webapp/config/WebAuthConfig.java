package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.security.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.ClonedInAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.filter.ExceptionHandlerFilter;
import ar.edu.itba.paw.webapp.filter.RefreshTokenFilter;
import ar.edu.itba.paw.webapp.filter.JwtAuthenticationFilter;
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
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.security", "ar.edu.itba.paw.webapp.filter"})
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthUserDetailsService userDetailsService;
    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;
    @Autowired
    private RefreshTokenFilter refreshTokenFilter;


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests()
                // Create and Authorize
                .antMatchers(HttpMethod.POST, "/api/users").anonymous()
                .antMatchers(HttpMethod.POST, "/api/enterprises").anonymous()
                // Users and Enterprises
                .antMatchers("/api/users/*/image").permitAll() // TODO: authenticated()
                .antMatchers("/api/users", "/api/users/**").authenticated()
                .antMatchers("/api/enterprises/*/image").permitAll() // TODO: authenticated()
                .antMatchers("/api/enterprises", "/api/enterprises/**").authenticated()
                // Categories
                .antMatchers("/api/categories").permitAll() // TODO: authenticated()
                // Skills
                .antMatchers("/api/skills").authenticated()
                // JobOffers
                .antMatchers("/api/jobOffers", "/api/jobOffers/**").authenticated()
                // Contacts
                .antMatchers("/api/contacts", "/api/contacts/**").authenticated()
                .and().exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint())
                .and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, CorsFilter.class)
                .addFilterBefore(refreshTokenFilter, CorsFilter.class)
                .csrf().disable();
    }

    //FIXME: Revisar esto
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers( "/assets/css/**", "/assets/js/**", "/assets/images/**",
                "/resources/**", "/static/**");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList(CorsConfiguration.ALL)); //Collections.singletonList("http://localhost:3000")); // React Frontend
        cors.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));
        cors.setExposedHeaders(Arrays.asList("Authorization", "Location", "Link", "X-Total-Pages", "X-Access-Token",
                "ETag", "Set-Cookie"));
        cors.setAllowCredentials(true);
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
        return new ClonedInAuthenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }










}