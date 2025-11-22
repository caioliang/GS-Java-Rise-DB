package br.com.fiap.rise.config;

import br.com.fiap.rise.service.AuthService;
import br.com.fiap.rise.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private final JwtService jwtService;
    private final AuthService authService;

    public AuthFilter(JwtService jwtService, @Lazy AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var header = request.getHeader("Authorization");

        log.debug("AuthFilter: incoming request {} {} | Authorization header present: {}", request.getMethod(), request.getRequestURI(), header != null);

        if (header != null && header.startsWith("Bearer ")) {
            try {
                var jwt = header.replace("Bearer ", "");

                var userId = jwtService.validateAndExtractUserId(jwt);
                log.debug("AuthFilter: token valid, extracted userId={}", userId);

                var userDetails = authService.loadUserById(UUID.fromString(userId));
                log.debug("AuthFilter: loaded userDetails for id={}, username={}", userId, userDetails.getUsername());

                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("AuthFilter: authentication set in SecurityContext");

            } catch (Exception e) {
                log.warn("AuthFilter: authentication failed: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{ \"message\": \"Token inválido ou expirado. Requer autenticação.\" }");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
