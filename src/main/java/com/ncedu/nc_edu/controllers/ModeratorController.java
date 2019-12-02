package com.ncedu.nc_edu.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModeratorController {
    @GetMapping("/moderator")
    public ResponseEntity<?> moderatorRoot() {
        return ResponseEntity.ok("md rt");
    }
}
