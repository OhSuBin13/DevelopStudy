package com.example.S3crud.global.controller;

import com.example.S3crud.global.dto.FileDeleteDto;
import com.example.S3crud.global.dto.FileNameDto;
import com.example.S3crud.global.repository.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/aws")
public class AwsController {

    private final FileStore fileStore;

    @PostMapping("/create")
    public ResponseEntity<?> uploadFile(MultipartFile multipartFile) throws IOException {
        return ResponseEntity.ok().body(fileStore.create(multipartFile));
    }

    @GetMapping("/read/{fileName}")
    public ResponseEntity<?> findFile(@PathVariable String fileName) throws IOException {
        return fileStore.read(fileName);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFile(FileDeleteDto fileDeleteDto) throws IOException {
        return ResponseEntity.ok().body(fileStore.update(fileDeleteDto));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestBody FileNameDto fileName) {
        fileStore.delete(fileName.getFileName());
        return ResponseEntity.ok().body(null);
    }
}
