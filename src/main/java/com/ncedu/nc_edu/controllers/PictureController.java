package com.ncedu.nc_edu.controllers;

import com.ncedu.nc_edu.services.PictureStorageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
public class PictureController {
    private PictureStorageService pictureStorageService;

    public PictureController(@Autowired PictureStorageService pictureStorageService) {
        this.pictureStorageService = pictureStorageService;
    }

    @PostMapping("/pictures")
    public ResponseEntity<JSONObject> upload(@RequestParam("file") MultipartFile file) {
        return null;
    }

    @GetMapping("/pictures/{id}")
    public File get(@PathVariable UUID id) {
        return null;
    }
}
