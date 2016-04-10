package org.owasp.webgoat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry security = http
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/js/**", "fonts/**", "/plugins/**", "plugin_lessons/**").permitAll()
                .antMatchers("/servlet/AdminServlet/**").hasAnyRole("WEBGOAT_ADMIN", "SERVER_ADMIN") //
                .antMatchers("/JavaSource/**").hasRole("SERVER_ADMIN") //
                .anyRequest().hasAnyRole("WEBGOAT_USER", "WEBGOAT_ADMIN", "SERVER_ADMIN");
        security.and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome.mvc", true)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll();
        security.and()
                .logout()
                .permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("guest").password("guest").roles("WEBGOAT_USER").and() //
                .withUser("webgoat").password("webgoat").roles("WEBGOAT_ADMIN").and() //
                .withUser("server").password("server").roles("SERVER_ADMIN");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }
}