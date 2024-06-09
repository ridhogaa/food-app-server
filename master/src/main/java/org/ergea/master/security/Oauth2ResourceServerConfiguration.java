package org.ergea.master.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ergea.master.dto.base.BaseResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    final ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(response.getOutputStream(), BaseResponse.failure(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
                }).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(WHITE_LIST_URL).permitAll()
                .antMatchers(HttpMethod.POST, "/v1/merchants").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/v1/merchants").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/v1/merchants").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/v1/products").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/v1/products").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/v1/products").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/v1/users").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/v1/users").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/v1/users").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/v1/users").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
    }
}

