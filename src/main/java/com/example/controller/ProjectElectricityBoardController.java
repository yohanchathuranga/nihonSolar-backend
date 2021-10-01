package com.example.controller;

import com.example.base.ProjectElectricityBoardManager;
import com.example.entity.DOCountRequest;
import com.example.entity.DOProjectElectricityBoard;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import yohan.exceptions.CustomException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project_electricity_boards")
public class ProjectElectricityBoardController {

    private final ProjectElectricityBoardManager ProjectElectricityBoardManager;

    @Autowired
    public ProjectElectricityBoardController(ProjectElectricityBoardManager ProjectElectricityBoardManager) {
        this.ProjectElectricityBoardManager = ProjectElectricityBoardManager;
    }

    // get all Project Electricity Board
    @PostMapping("/list")
    public ResponseEntity<List<DOProjectElectricityBoard>> getProjectElectricityBoardList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.listProjectElectricityBoards(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getProjectElectricityBoardCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.countProjectElectricityBoards(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get Project Electricity Board by id
    @GetMapping("/{id}")
    public ResponseEntity<DOProjectElectricityBoard> getProjectElectricityBoardById(@PathVariable(value = "id") String projectElectricityBoardId) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.getProjectElectricityBoardById(projectElectricityBoardId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create Project Electricity Board
    @PostMapping
    public ResponseEntity<DOProjectElectricityBoard> createProjectElectricityBoard(@RequestBody DOProjectElectricityBoard projectElectricityBoard) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.createProjectElectricityBoard(projectElectricityBoard), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete Project Electricity Board by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOProjectElectricityBoard> deleteProjectElectricityBoard(@PathVariable("id") String projectElectricityBoardId) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.deleteProjectElectricityBoard(projectElectricityBoardId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
         // update Project Electricity Board
    @PutMapping
    public ResponseEntity<DOProjectElectricityBoard> updateProjectElectricityBoard(@RequestBody DOProjectElectricityBoard projectElectricityBoard) {
        try {
            return new ResponseEntity(this.ProjectElectricityBoardManager.updateProject(projectElectricityBoard), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
