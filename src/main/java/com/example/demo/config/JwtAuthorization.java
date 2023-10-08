package com.example.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthorization extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", SecurityConstants.CLIENT_DOMAIN_URL);

        response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
                + "Content-Type, Access-Control-Request-Method, " + "Access-Control-Request-Headers, Authorization");

        response.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, " + "Access-Control-Allow-Creentials, " + "Authorization");

        response.addHeader("Access-Control-Allow-Methods", "GET," + "POST, " + "DELETE");

        if ((request.getMethod().equalsIgnoreCase("OPTIONS"))) {
            try {
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String jwtToken = request.getHeader(SecurityConstants.HEADER_TYPE);
            if (jwtToken == null || !jwtToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            JWT.require(Algorithm.HMAC256(SecurityConstants.SECRET));
            DecodedJWT jwt = JWT.decode(jwtToken.substring(SecurityConstants.TOKEN_PREFIX.length()));
            String username = jwt.getSubject();
            List<String> roles = jwt.getClaims().get("roles").asList(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(username,
                    null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            filterChain.doFilter(request, response);
        }
    }
}





















