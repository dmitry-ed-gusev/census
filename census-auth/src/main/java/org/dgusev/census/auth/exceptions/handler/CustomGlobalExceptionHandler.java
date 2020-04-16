package org.dgusev.census.auth.exceptions.handler;

import org.dgusev.census.auth.exceptions.RoleNotFoundException;
import org.dgusev.census.auth.exceptions.RoleUnSupportedFieldPatchException;
import org.dgusev.census.auth.exceptions.UserNotFoundException;
import org.dgusev.census.auth.exceptions.UserUnSupportedFieldPatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
    @ExceptionHandler({RoleNotFoundException.class, UserNotFoundException.class})
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({RoleUnSupportedFieldPatchException.class, UserUnSupportedFieldPatchException.class})
    public void springUnSupportedFieldPatch(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void springEmptyResultData(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    /** Handle data integrity violations for DBMS - unique constraints etc. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void springDataIntegrityViolation(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Data Integrity Error!");

    }

}