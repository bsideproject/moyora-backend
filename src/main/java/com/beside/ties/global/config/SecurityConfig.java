package com.beside.ties.global.config;

import com.beside.ties.global.auth.security.AuthFilterContainer;
import com.beside.ties.domain.account.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.lang.reflect.Method;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthFilterContainer authFilterContainer;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/v1/login").permitAll()
                .antMatchers("/api/v1/user/local/login").permitAll()
                .antMatchers("/api/v1/user/secondarySignUp").hasRole(Role.USER.getName())
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/region/state").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/region/city").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/region/state").hasRole(Role.USER.getName())
                .antMatchers(HttpMethod.POST, "/api/v1/region/city").hasRole(Role.USER.getName())
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/v1/test").hasRole(Role.USER.getName())
                .anyRequest().authenticated()
                .and()
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .addFilterBefore(authFilterContainer.getFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs",  "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**","/swagger/**", "/swagger-ui/**");
    }




}
