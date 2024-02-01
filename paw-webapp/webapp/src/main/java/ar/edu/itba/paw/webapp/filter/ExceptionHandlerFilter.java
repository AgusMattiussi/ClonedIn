package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.models.exceptions.ClonedInException;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ClonedInException e) {
            writeErrorResponse(e, response, e.getHttpStatus(), e.getDetails());
        }
        catch (RuntimeException e) {
            writeErrorResponse(e, response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    private void writeErrorResponse(Exception e, HttpServletResponse response, int status, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ErrorDTO errorDTO = new ErrorDTO(e.getClass(), message, message);
        response.setStatus(status);
        mapper.writeValue(response.getWriter(), errorDTO);
    }

}
