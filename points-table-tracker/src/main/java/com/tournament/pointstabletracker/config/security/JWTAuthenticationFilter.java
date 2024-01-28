package com.tournament.pointstabletracker.config.security;

import com.tournament.pointstabletracker.exceptions.InvalidRequestException;
import com.tournament.pointstabletracker.service.security.JWTServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTServiceImpl jwtService;

    private final UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.authorizationHeaderString}")
    private String authorizationHeaderString;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private final RequestMatcher requestMatcher = new OrRequestMatcher(
            // TODO: Read it from config
            new AntPathRequestMatcher("/api/v1/auth/**"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/api/v1/pointsTable/tournament/**"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/actuator/info")
    );

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain) {

        try {
            if (requestMatcher.matches(request)) {
                logger.info("Request is skipped for authentication as it is whitelisted");
                filterChain.doFilter(request, response);
                return;
            }
            final String authorizationHeader = request.getHeader(authorizationHeaderString);

            if ((authorizationHeader == null) || !authorizationHeader.startsWith(tokenPrefix)) {
                logger.error("No JWT token found in request headers");
                throw new InvalidRequestException("No JWT token found in request headers");
            }

            final String jwtToken = authorizationHeader.substring(7);
            final String username = jwtService.extractUsername(jwtToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (InvalidRequestException | UsernameNotFoundException | ServletException | IOException e) {
            resolver.resolveException(request, response, null, e);
        }

    }
}
