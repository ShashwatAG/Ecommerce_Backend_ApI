package com.playground.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.ecommerce.exception.ApiProblem;
import com.playground.ecommerce.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityProblemSupport {

    private final ObjectMapper objectMapper;

    public void writeUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeProblem(response, HttpStatus.UNAUTHORIZED, "Unauthorized.", "Authentication is required to access this resource.", request.getRequestURI());
    }

    public void writeForbidden(HttpServletRequest request, HttpServletResponse response) throws IOException {
        writeProblem(response, HttpStatus.FORBIDDEN, "Forbidden.", "You do not have permission to access this resource.", request.getRequestURI());
    }

    private void writeProblem(HttpServletResponse response,
                              HttpStatus status,
                              String title,
                              String detail,
                              String instance) throws IOException {
        ApiProblem problem = GlobalExceptionHandler.createProblem(status, title, detail, instance, null);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), problem);
    }
}
