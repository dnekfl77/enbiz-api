package com.enbiz.api.common.base.advice;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enbiz.common.base.rest.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GlobalErrorController implements ErrorController {

	@RequestMapping("/error")
    public ResponseEntity<ErrorResponse> error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        log.error("status_code: {}", request.getAttribute("javax.servlet.error.status_code"));
        log.error("exception_type: {}", request.getAttribute("javax.servlet.error.exception_type"));
        log.error("message: {}", request.getAttribute("javax.servlet.error.message"));
        log.error("request_uri: {}", request.getAttribute("javax.servlet.error.request_uri"));
        log.error("exception: {}", request.getAttribute("javax.servlet.error.exception"));
        
        if (status.equals(HttpStatus.BAD_REQUEST.value())) {
    		return new ResponseEntity<ErrorResponse>(
    				new ErrorResponse("0400", "BAD_REQUEST"), 
    				new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        else if (status.equals(HttpStatus.FORBIDDEN.value())) {
    		return new ResponseEntity<ErrorResponse>(
    				new ErrorResponse("0403", "FORBIDDEN"), 
    				new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
        else if (status.equals(HttpStatus.NOT_FOUND.value())) {
    		return new ResponseEntity<ErrorResponse>(
    				new ErrorResponse("0404", "NOT_FOUND"), 
    				new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        else {
    		return new ResponseEntity<ErrorResponse>(
    				new ErrorResponse("9000", "시스템오류가 발생했습니다."), 
    				new HttpHeaders(), HttpStatus.valueOf(Integer.valueOf(String.valueOf(status))));
        }
    }

}
