package com.demo.jwt_auth.filter;

import com.demo.jwt_auth.service.JwtService;
import com.demo.jwt_auth.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//step 13
@Component
public class JwtFilter extends OncePerRequestFilter {
    //for line 29
    @Autowired
    private JwtService jwtService;

    //step 15
    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2hpdCIsImlhdCI6MTc0NzExNDY1NywiZXhwIjoxNzQ3MTE2NDU3fQ.fudrbOR8zRTSVyuwQ7sduO9tjXyThhS2BrzLZghAsr0
        String authHeader= request.getHeader("Authorization");
        String token=null;
        String username=null;

        if(authHeader != null && authHeader.startsWith("Bearer")){
            token=authHeader.substring(7);
            //step 14 creating extractUsername(token) in jwtService
            username=jwtService.extractUsername(token);
        }

        //username should not be null and SecurityContextHolder.getContext().getAuthentication() should be null
        //otherwise it's already authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //step 16 instead of getting UserDetails bean get MyUserDetailsService bean and from that we will call loadUserByUsername(username)
           // UserDetails userDetails= context.getBean(UserDetails.class);
            UserDetails userDetails= context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            //step 17 create validToken in jwtService
            if(jwtService.validToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        filterChain.doFilter(request,response);
    }
}
