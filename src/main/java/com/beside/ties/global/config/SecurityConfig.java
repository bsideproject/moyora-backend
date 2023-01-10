package com.beside.ties.global.config;

import com.beside.ties.global.auth.security.AuthFilterContainer;
import com.beside.ties.domain.account.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .antMatchers("/").permitAll()
                                .antMatchers("/api/v1/user/local/signup").permitAll()
                                .antMatchers("/api/v1/user/classmate/**").permitAll()
                                .antMatchers(HttpMethod.POST,"/api/v1/job/category").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.POST,"/api/v1/user").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.GET,"/api/v1/job/category").permitAll()
                                .antMatchers(HttpMethod.DELETE,"/api/v1/user/article").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.POST,"/api/v1/user/article").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.GET,"/api/v1/user/article").permitAll()
                                .antMatchers("/api/v1/user/signin").permitAll()
                                .antMatchers("/api/v1/user/signup").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.PUT, "/api/v1/user/**").hasRole(Role.USER.getName())
                                .antMatchers("/api/v1/user/myinfo").hasRole(Role.USER.getName())
                                .antMatchers("/api/v1/user/school").hasRole(Role.USER.getName())
                                .antMatchers("/api/v1/user/name").hasRole(Role.USER.getName())
                                .antMatchers("/swagger-ui/**").permitAll()
                                .antMatchers(HttpMethod.GET,"/api/v1/region/state").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/v1/region/city").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/v1/region/state").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.POST, "/api/v1/region/city").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.GET, "/api/v1/job/category/parent").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/v1/job/category/child").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/v1/job/category/parent").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/v1/job/category/child").permitAll()
                                .antMatchers(HttpMethod.PUT, "/api/v1/user").hasRole(Role.USER.getName())
                                .antMatchers(HttpMethod.GET, "/api/v1/school/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/v1/schoolGuestBook/**").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/v1/schoolGuestBook/**").permitAll()
                                .antMatchers(HttpMethod.PUT, "/api/v1/schoolGuestBook/**").permitAll()
                                .antMatchers(HttpMethod.DELETE, "/api/v1/schoolGuestBook/**").permitAll()
                                .antMatchers("/swagger-resources/**").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()

                )
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .addFilterBefore(authFilterContainer.getFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs",  "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui.html", "/webjars/**","/swagger/**", "/swagger-ui/**");
    }




}
