package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.MySimpleUrlAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                    .invalidSessionUrl("/login")
                .and().authorizeRequests()
                    .antMatchers("/login").anonymous()
                    .antMatchers("/createUser").anonymous()
                    .antMatchers("/createEnterprise").anonymous()
                .antMatchers("/").hasRole("ENTERPRISE")
                .antMatchers("/**").authenticated() // .permitAll()
                .and().formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(myAuthenticationSuccessHandler())
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("remember_me")
                    .userDetailsService(userDetailsService)
                    .key(loadRememberMeKey())
                                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                                .and().logout()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .and().exceptionHandling()
                                .accessDeniedPage("/403")
                                .and().csrf().disable();
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
                "/views/403", "/views/404");
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
        return new MySimpleUrlAuthenticationSuccessHandler();
    }
}