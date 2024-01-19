package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();

        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), "Authentication Exception", e.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        mapper.writeValue(response.getWriter(), errorDTO);
    }
}
