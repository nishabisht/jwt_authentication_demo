package com.demo.jwt_auth.util;

import com.demo.jwt_auth.filter.JwtFilter;
import com.demo.jwt_auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    //private MyUserDetailsService userDetailsService;
    private UserDetailsService userDetailsService;

    //Step 12
    @Autowired
    private JwtFilter jwtFilter;

    //custom securityFilerChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF
        http.csrf(customizer -> customizer.disable())
                // Allow h2-console access
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/h2-console/**","/swagger-ui/**","/v3/api-docs/","/swagger-ui.html").permitAll()  // Ensure h2-console and swagger is permitted
                        .requestMatchers("/register","/login").permitAll()//permit login and registration to access without auth
                        .anyRequest().authenticated())  // All other requests require authentication
                // to Use HTTP basic authentication
                .httpBasic(Customizer.withDefaults())
                // Stateless session (no cookies/session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Bearer token authentication request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin() // ✅ Modern replacement for deprecated method (allow h2 UI)
                        )
                );

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(); //database authentication provider class
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));//using bcryptPassword(12) converting user-password and then validating from a database
        provider.setUserDetailsService(userDetailsService); //instance of MyUserDetailsService

        return provider;
    }

    //Step 1
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    //userDetailService //default authentication provider
    //   @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("rohit")
//                .password("rohit123")
//                .roles("Admin")
//                .build();
//
//        UserDetails user2=User
//                .withDefaultPasswordEncoder()
//                .username("nisha")
//                .password("nisha@123")
//                .roles("User")
//                .build();
//        return new InMemoryUserDetailsManager(user2,user1);
//    }


}
