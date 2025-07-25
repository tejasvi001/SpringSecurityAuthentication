package com.example.security.configs;

import com.example.security.entities.enums.Permission;
import com.example.security.entities.enums.Role;
import com.example.security.filters.JwtAuthFilter;
import com.example.security.handlers.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.example.security.entities.enums.Permission.POST_CREATE;
import static com.example.security.entities.enums.Permission.POST_VIEW;
import static com.example.security.entities.enums.Role.ADMIN;
import static com.example.security.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler auth2SuccessHandler;
    private final String[] publicRoutes={
            "/auth/**",
            "/error",
            "/home.html"
    };
    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter, OAuth2SuccessHandler auth2SuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.auth2SuccessHandler = auth2SuccessHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       httpSecurity
               .authorizeHttpRequests(
                       auth->
                               auth
                                       .requestMatchers(publicRoutes).permitAll()
                                       .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                                       .requestMatchers(HttpMethod.POST,"/posts/**")
                                            .hasAnyRole(ADMIN.name(),CREATOR.name())
//                                       .requestMatchers(HttpMethod.POST,"/posts/**")
//                                            .hasAnyAuthority(POST_CREATE.name())
//                                       .requestMatchers(HttpMethod.GET,"/posts/**")
//                                            .hasAnyAuthority(POST_VIEW.name())
                                       .anyRequest().authenticated()
               )
               .formLogin(Customizer.withDefaults())
               .csrf(csrfConfig-> csrfConfig.disable())
               .sessionManagement(sessionConifg->sessionConifg.sessionCreationPolicy(
                       SessionCreationPolicy.STATELESS
               ))
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .oauth2Login(oauth2Config->oauth2Config
                       .failureUrl("/login?error=true")
                       .successHandler(auth2SuccessHandler)
               );

        return httpSecurity.build();
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }


//    @Bean
//    UserDetailsService myInMemoryUserDetailsService(){
//        UserDetails user= User
//                .withUsername("anuj")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        UserDetails userAdmin= User
//                .withUsername("admin")
//                .password(passwordEncoder().encode("password"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user,userAdmin);
//    }


}
