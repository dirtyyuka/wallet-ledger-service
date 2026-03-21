package com.distributed.transaction_system.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PaymentController {

    @PostMapping("/payment")
    public ResponseEntity<?> postMethodName(@RequestBody String entity) {
        return ResponseEntity.ok("Payment successful");
    }

}
