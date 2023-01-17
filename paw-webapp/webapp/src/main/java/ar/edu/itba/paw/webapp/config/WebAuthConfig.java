package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtFilter;
import ar.edu.itba.paw.webapp.auth.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.models"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    //TODO: ACTUALIZAR LOS URLS
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl().disable()
                .and().authorizeRequests().antMatchers("/**").permitAll()
                /*    .invalidSessionUrl("/login")
                .and().authorizeRequests()
                    .antMatchers("/login").anonymous()
                    .antMatchers("/createUser").anonymous()
                    .antMatchers("/createEnterprise").anonymous()
                    .antMatchers("/").hasRole("ENTERPRISE")
                    .antMatchers("/createJobOffer/**").hasRole("ENTERPRISE")
                    .antMatchers("/contact/**").hasRole("ENTERPRISE")
                    .antMatchers("/editEnterprise/**").hasRole("ENTERPRISE")
                    .antMatchers("//uploadEnterpriseProfileImage/**").hasRole("ENTERPRISE")
                    .antMatchers("/home").hasRole("USER")
                    .antMatchers("/editUser/**").hasRole("USER")
                    .antMatchers("/createExperience/**").hasRole("USER")
                    .antMatchers("/createEducation/**").hasRole("USER")
                    .antMatchers("/createSkill/**").hasRole("USER")
                    .antMatchers("//uploadProfileImage/**").hasRole("USER")
                    .antMatchers("/**").authenticated() // .permitAll()
                .and().formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(myAuthenticationSuccessHandler())
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("rememberMe")
                    .userDetailsService(userDetailsService)
                    .key(loadRememberMeKey())
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")*/
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }

    private String loadRememberMeKey() {
        try (Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("rememberme.key"))) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/assets/css/**", "/assets/js/**", "/assets/images/**",
                "/views/403", "/views/404","/views/500", "/users/**");
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