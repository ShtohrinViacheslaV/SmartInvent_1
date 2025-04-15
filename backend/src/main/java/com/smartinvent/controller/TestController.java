//package com.smartinvent.controller;
//
//import com.smartinvent.config.ResourceNotFoundException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/test")
//public class TestController {
//
//    @GetMapping("/runtime")
//    public ResponseEntity<String> throwRuntimeException() {
//        throw new RuntimeException("Runtime test error for Sentry");
//    }
//
//    @GetMapping("/null")
//    public ResponseEntity<String> throwNullPointer() {
//        String str = null;
//        str.length(); // NullPointerException
//        return ResponseEntity.ok("This won't be reached");
//    }
//
//    @GetMapping("/custom")
//    public ResponseEntity<String> throwCustomException() {
//        throw new ResourceNotFoundException("Custom test exception for Sentry");
//    }
//
//    @GetMapping("/illegal")
//    public ResponseEntity<String> throwIllegalState() {
//        throw new IllegalStateException("IllegalStateException for alert testing");
//    }
//
//    @GetMapping("/nullpointer")
//    public ResponseEntity<String> generateNullPointerError() {
//        String str = null;
//        str.length(); // спричинить NullPointerException
//        return ResponseEntity.ok("Done");
//    }
//
//}
//
