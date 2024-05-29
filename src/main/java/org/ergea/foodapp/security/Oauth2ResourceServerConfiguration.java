package org.ergea.foodapp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true) //secure definition
public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String[] WHITE_LIST_URL = {
            "/error**",
            "/v1/auth/**",
            "/v1/auth",
            "/oauth/authorize**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

    /**
     * Manage resource server.
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    /**
     * Manage endpoints.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URL)
                .permitAll()
                .antMatchers("/v1/role-test-global/list-barang").hasAnyAuthority("ROLE_READ")
                .antMatchers("/v1/role-test-global/post-barang").hasAnyAuthority("ROLE_WRITE")
                .antMatchers("/v1/role-test-global/post-barang-user").hasAnyAuthority("ROLE_USER")
                .antMatchers("/v1/role-test-global/post-barang-admin").hasAnyAuthority("ROLE_ADMIN")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }
}

