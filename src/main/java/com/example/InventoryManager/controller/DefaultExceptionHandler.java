package com.example.InventoryManager.controller;

import com.example.InventoryManager.model.ErrorMessage;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ErrorMessage> runTimeException(RuntimeException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Run time exception occurs");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<ErrorMessage> nullPointerException(NullPointerException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Null pointer exception occurs");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StackOverflowError.class)
    public final ResponseEntity<ErrorMessage> stackOverFlowException(StackOverflowError ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Stack over flow exception occurs");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArithmeticException.class)
    public final ResponseEntity<ErrorMessage> arithmeticException(ArithmeticException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Arithmetic exception occurs");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public final ResponseEntity<ErrorMessage> indexOutOfBoundException(IndexOutOfBoundsException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Index out of bound exception occurs");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "You have provided the wrong parameters");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JDBCConnectionException.class)
    public final ResponseEntity<ErrorMessage> jdbcConnectionException(JDBCConnectionException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Data base is down");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public final ResponseEntity<ErrorMessage> cannotCreateTransactionException(CannotCreateTransactionException ex){
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), "Data base is down");
        return new ResponseEntity<ErrorMessage>(errorMessage,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
