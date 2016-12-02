
/**
 *************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 *
 * @author WebGoat
 * @since  December 12, 2015
 * @version $Id: $Id
 */
package org.owasp.webgoat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Security configuration for WebGoat.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry security = http
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/js/**", "fonts/**", "/plugins/**").permitAll()
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
        security.and().csrf().disable();

        http.headers().cacheControl().disable();
        http.exceptionHandling().authenticationEntryPoint(new AjaxAuthenticationEntryPoint("/login"));
    }

    //// TODO: 11/18/2016 make this a little bit more configurabe last part at least
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/plugin_lessons/**", "/XXE/**");
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