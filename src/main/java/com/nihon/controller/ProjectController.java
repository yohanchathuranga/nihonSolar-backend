package com.nihon.controller;

import com.nihon.base.ProjectManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOProject;
import com.nihon.entity.DOProjectDetails;
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
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectManager projectManager;

    @Autowired
    public ProjectController(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    // get all projects
    @PostMapping("/list")
    public ResponseEntity<List<DOProject>> getProjectList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.projectManager.listProjects(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getProjectCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.projectManager.countProjects(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get project by id
    @GetMapping("/{id}")
    public ResponseEntity<DOProject> getProjectById(@PathVariable(value = "id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.getProjectById(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create project
    @PostMapping
    public ResponseEntity<DOProject> createProject(@RequestBody DOProject project) {
        try {
            return new ResponseEntity(this.projectManager.createProject(project), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete project by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DOProject> deleteProject(@PathVariable("id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.deleteProject(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // update project
    @PutMapping
    public ResponseEntity<DOProject> updateProject(@RequestBody DOProject project) {
        try {
            return new ResponseEntity(this.projectManager.updateProject(project), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get project by id
    @GetMapping("/details/{id}")
    public ResponseEntity<DOProjectDetails> getProjectDetailsById(@PathVariable(value = "id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.getProjectDetailsById(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/cancel/{id}")
    public ResponseEntity<DOProject> cancelProject(@PathVariable(value = "id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.cancel(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/complete/{id}")
    public ResponseEntity<DOProject> completeProject(@PathVariable(value = "id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.complete(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/approve/{id}")
    public ResponseEntity<DOProject> approveProject(@PathVariable(value = "id") String projectId) {
        try {
            return new ResponseEntity(this.projectManager.approve(projectId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
