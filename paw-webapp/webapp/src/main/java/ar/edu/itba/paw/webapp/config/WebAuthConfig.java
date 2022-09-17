package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
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
                    .antMatchers("/login").anonymous() // tambien /register?
                    .antMatchers("/admin/**").hasRole("ADMIN") // FIXME: Cambiar por rol de empresa o usuario
                    .antMatchers("/**").authenticated() // .permitAll()
                .and().formLogin()
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
                    .defaultSuccessUrl("/", false)
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("j_rememberme")
                    .userDetailsService(userDetailsService)
                    .key(loadRememberMeKey()) // no hacer esto, crear una aleatoria segura suficientemente grande y colocarla bajo src/main/resources
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
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}