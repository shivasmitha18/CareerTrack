package CareerTrack.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {


        String authorizationHeader =
                request.getHeader("Authorization");


        System.out.println(
                "JWT FILTER - Request: "
                        + request.getRequestURI()
        );

        System.out.println(
                "JWT FILTER - Authorization: "
                        + (authorizationHeader != null
                        ? "Bearer token received"
                        : "NO TOKEN")
        );


        // No token
        if (
                authorizationHeader == null ||
                        !authorizationHeader.startsWith("Bearer ")
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        String token =
                authorizationHeader.substring(7);


        try {

            Jws<Claims> claims =
                    Jwts.parser()
                            .verifyWith(getSigningKey())
                            .build()
                            .parseSignedClaims(token);


            String email =
                    claims.getPayload().getSubject();


            System.out.println(
                    "JWT FILTER - Token valid for: "
                            + email
            );


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.emptyList()
                    );


            SecurityContextHolder
                    .getContext()
                    .setAuthentication(
                            authentication
                    );


        } catch (Exception exception) {

            System.out.println(
                    "JWT FILTER - INVALID TOKEN: "
                            + exception.getMessage()
            );

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        filterChain.doFilter(
                request,
                response
        );
    }
}