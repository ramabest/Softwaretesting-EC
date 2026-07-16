package de.hsbremen.atm.config;

import de.hsbremen.atm.common.BusinessClock;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TestClockFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String testNow = request.getHeader("X-Test-Now");
        try {
            if (testNow != null && !testNow.isBlank()) {
                BusinessClock.setTestNow(Instant.parse(testNow));
            }
            filterChain.doFilter(request, response);
        } finally {
            BusinessClock.clearTestNow();
        }
    }
}

