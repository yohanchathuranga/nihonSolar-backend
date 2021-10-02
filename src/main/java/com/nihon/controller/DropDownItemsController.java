package com.nihon.controller;

import com.nihon.base.DropDownItemManager;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DODropDownItem;
import com.nihon.entity.DODropDownItem.DODropDownListItem;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import yohan.exceptions.CustomException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/drop_down_items")
public class DropDownItemsController {

    private final DropDownItemManager dropDownManager;

    @Autowired
    public DropDownItemsController(DropDownItemManager dropDownManager) {
        this.dropDownManager = dropDownManager;
    }

    // get all items
    @PostMapping("/list")
    public ResponseEntity<List<DODropDownItem>> getItemList(@RequestBody DOListRequest listRequest) {
        try {
            return new ResponseEntity(this.dropDownManager.listItems(listRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/count")
    public ResponseEntity<DOListCountResult> getItemCount(@RequestBody DOCountRequest countRequest) {
        try {
            return new ResponseEntity(this.dropDownManager.countItems(countRequest), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get item by id
    @GetMapping("/{id}")
    public ResponseEntity<DODropDownItem> getItemById(@PathVariable(value = "id") String itemId) {
        try {
            return new ResponseEntity(this.dropDownManager.getItemById(itemId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // get item list by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<DODropDownListItem>> getItemsByType(@PathVariable(value = "type") String type) {
        try {
            return new ResponseEntity(this.dropDownManager.getItemsByType(type), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // create item
    @PostMapping
    public ResponseEntity<DODropDownItem> createItem(@RequestBody DODropDownItem dropDownItem) {
        try {
            return new ResponseEntity(this.dropDownManager.createItem(dropDownItem), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }

    // delete item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<DODropDownItem> deleteItem(@PathVariable("id") String itemId) {
        try {
            return new ResponseEntity(this.dropDownManager.deleteItem(itemId), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/type/type_list")
    public ResponseEntity<List<String>> getTypeList() {
        try {
            return new ResponseEntity(this.dropDownManager.getTypeList(), HttpStatus.OK);
        } catch (CustomException ex) {
            return new ResponseEntity(ex, HttpStatus.BAD_REQUEST);
        }
    }
}
