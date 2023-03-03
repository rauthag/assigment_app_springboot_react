package com.rauthagproject.assigmentapp.filter;

import com.rauthagproject.assigmentapp.repository.UserRepository;
import com.rauthagproject.assigmentapp.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(header) || StringUtils.hasText(header) && !header.startsWith("Bearer ")) { //if this is not JWT, ignore, end state
            chain.doFilter(request, response);
            return;
        }

        //Get JWT token and user indentity and set it on the spring security context
        // get header out of a token
        final String token = header.split(" ")[1].trim();  //AuthToken is something like: [Bearer], [fa54616fasd/.],
                                                                  // split and trim filter Bearer from encoded value to Arr of 2 values
                                                                  // so we get encoded token (fa54616fasd/.)

        //with getUsernameFromToken we look for user from token, if its exists retturn that user else null
        UserDetails userDetails = userRepo
                .findByUsername(jwtUtil.getUsernameFromToken(token))
                .orElse(null);

        //Validate JWT token with UserDetails
        //function validateToke() validate if username name matches and if user is not expired, if not true it goes back to auth filter
        if(!jwtUtil.validateToken(token, userDetails)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken
                authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken); //this is a valid user so spring will inject in its context
                                                                //user successfully logged in - user is now valid
        chain.doFilter(request, response); //then its passed to next filter
    }
}
