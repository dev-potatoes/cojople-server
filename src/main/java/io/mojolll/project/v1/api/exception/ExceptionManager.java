//package io.mojolll.project.v1.api.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class ExceptionManager {
//    @ExceptionHandler(AppCustomException.class) //특정 exception 받아서 처리가 가능하다.
//    public ResponseEntity<?> AppExceptionHandler(AppCustomException e){
//        return ResponseEntity.status(e.getErrorCode().getHttpStatus()) //409
//                .body(e.getErrorCode().name() + " " + e.getMessage()); //나중에 response object로 매핑해서 " " 여기 처리가능
//    }
//
//    @ExceptionHandler(RuntimeException.class) //특정 exception 받아서 처리가 가능하다.
//    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
//        return ResponseEntity.status(HttpStatus.CONFLICT) //409
//                .body(e.getMessage());
//    }
//}
