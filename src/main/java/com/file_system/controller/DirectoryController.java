package com.file_system.controller;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.file_system.entity.GenericFile;


@RestController
@RequestMapping("/directory")
public class DirectoryController {
	private static String rootPath = "test_files/";
	
	@GetMapping
	public ResponseEntity<String[]> listContent(@RequestBody GenericFile dir) {
		File directory = new File(rootPath + dir.path);
		if (directory.exists() && directory.isDirectory()) {
			String[] dirContent = directory.list();
	        return ResponseEntity.status(HttpStatus.OK).body(dirContent);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        
	}
	
	@PostMapping
	public ResponseEntity<Void> createDirectory(@RequestBody GenericFile dir) {
        File file = new File(rootPath + dir.path);
        if (file.mkdirs()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }
    	
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteDirectory(@RequestBody GenericFile dir) {
		File directory = new File(rootPath + dir.path);
		
		if (directory.exists() && directory.isDirectory()) {
	        File[] files = directory.listFiles();
	
	        for (File file: files) {
	            file.delete();
	        }
	
	        directory.delete();
	        
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
}
