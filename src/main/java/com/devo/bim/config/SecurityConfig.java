package com.devo.bim.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private AuthenticationFailureHandler customFailureHandler;
//
//    @Autowired
//    private AuthenticationSuccessHandler customSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/","/account/resetPassword", "/account/login", "/layout", "/dist/**", "/plugins/**", "/sample/**")
            .permitAll()
            .anyRequest()
            .authenticated()
        .and()
            .formLogin()
            .loginPage("/account/login")
            .defaultSuccessUrl("/index")
        .and()
            .logout()
            .logoutUrl("/logout") /* ???????????? url*/
            .logoutSuccessUrl("/account/login") /* ???????????? ????????? ????????? url */
            .invalidateHttpSession(true) /*??????????????? ?????? ??????*/
            .deleteCookies("JSESSIONID") /*?????? ??????*/
            .clearAuthentication(true) /*???????????? ??????*/
            .permitAll()
        .and()
            .sessionManagement()
            .maximumSessions(1) /* session ?????? ?????? */
            .expiredUrl("/account/login") /* session ????????? ?????? ?????????*/
            .maxSessionsPreventsLogin(false);

        http.cors().and().csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select email as user_id, password, CASE WHEN enabled = 1 THEN true ELSE false END "
                        + "from account "
                        + "where email = ?")
                .authoritiesByUsernameQuery("select a.email as user_id, c.code "
                        + "from account a inner join account_role b on b.account_id=a.id  "
                        + "inner join role c on b.role_id = c.id "
                        + "where a.email = ? ")
                .groupAuthoritiesByUsername(
                        "select b.id, b.name, b.type "
                        + "from account a "
                        + "inner join company b on b.id = a.company_id "
                        + "where a.email = ?"
                );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}