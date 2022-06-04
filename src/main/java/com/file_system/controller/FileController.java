package com.file_system.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.file_system.entity.FileToCopy;
import com.file_system.entity.FileToWrite;
import com.file_system.entity.GenericFile;


@RestController
@RequestMapping("/file")
public class FileController {
	private static String rootPath = "test_files/";
	
	@GetMapping
	public ResponseEntity<String> openFile(@RequestBody GenericFile f) {
		String fileContent = "";

        try {
            fileContent = Files.readString(Paths.get(rootPath + f.path));
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(fileContent);
	}
	
	@PostMapping
	public ResponseEntity<Void> createFile(@RequestBody GenericFile f) {
        try {
            File file = new File(rootPath + f.path);
            if (file.createNewFile()) {
            	return ResponseEntity.status(HttpStatus.CREATED).body(null);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteFile(@RequestBody GenericFile f) {
		File file = new File(rootPath+ f.path);

        if (file.delete()) {
        	return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@PutMapping("/write")
	public ResponseEntity<Void> writeFile(@RequestBody FileToWrite f) {
		try {
			Files.write(Paths.get(rootPath + f.path), f.content.getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	@PutMapping("/copy")
	public ResponseEntity<Void> copyFile(@RequestBody FileToCopy f) {
		try {
			Path srcPath = Paths.get(rootPath + f.path);
	        Path destPath = Paths.get(rootPath + f.destination);
	        Files.copy(srcPath, destPath);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
	
