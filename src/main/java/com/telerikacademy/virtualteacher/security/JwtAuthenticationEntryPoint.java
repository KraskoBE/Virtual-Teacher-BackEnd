package com.telerikacademy.virtualteacher.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
        Set<String> attributes = new HashSet<>();
        attributes.add((String) httpServletRequest.getAttribute("invalidSignature"));
        attributes.add((String) httpServletRequest.getAttribute("invalidToken"));
        attributes.add((String) httpServletRequest.getAttribute("expired"));
        attributes.add((String) httpServletRequest.getAttribute("unsupported"));
        attributes.add((String) httpServletRequest.getAttribute("empty"));

        Optional<String> errorString = attributes.stream()
                .filter(Objects::nonNull)
                .findFirst();

        if (errorString.isPresent())
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorString.get());
        else
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authorized");
    }
}

