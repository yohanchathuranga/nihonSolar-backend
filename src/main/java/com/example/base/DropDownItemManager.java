/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.base;

import com.example.dataaccess.DAODataUtil;
import com.example.entity.DOCountRequest;
import com.example.entity.DODropDownItem;
import com.example.entity.DODropDownItem.DODropDownListItem;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.repository.DropDownItemRepository;
import com.example.util.InputValidatorUtil;
import com.yohan.exceptions.AlreadyExistException;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class DropDownItemManager {
    
    @Autowired
    private DropDownItemRepository dropDownItemRepository;
    private final DAODataUtil dataUtil;
    
    public DropDownItemManager(DAODataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }
    
    public List<DODropDownItem> listItems(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "drop_down_items");
            
            return this.dropDownItemRepository.listItems(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DOListCountResult countItems(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "drop_down_items"); 
            Object o =this.dropDownItemRepository.countItems(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }
    
    public DODropDownItem getItemById(String itemId) throws CustomException {
        try {
            
            itemId = InputValidatorUtil.validateStringProperty("Drop Down Item Id", itemId);
            if (!this.dropDownItemRepository.isExistsById(itemId)) {
                throw new DoesNotExistException("Drop Down Item does not exists.Drop Down Item Id : " + itemId);
            }
            return this.dropDownItemRepository.findById(itemId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public List<DODropDownListItem> getItemsByType(String type) throws CustomException {
        try {
            
            type = InputValidatorUtil.validateStringProperty("Drop Down Item Type", type);
            return this.dropDownItemRepository.getItemsByType(type);
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public DODropDownItem createItem(DODropDownItem dropDownItem) throws CustomException {
        try {
            String type = InputValidatorUtil.validateStringProperty("Drop Down Item Type", dropDownItem.getType());
            dropDownItem.setType(type);
            
            String data = InputValidatorUtil.validateStringProperty("Drop Down Item Data", dropDownItem.getData());
            dropDownItem.setData(data);
            
            if (this.dropDownItemRepository.isExistsDataAndType(data, type)) {
                throw new AlreadyExistException("Item already exists.");
            }
            
            String id = UUID.randomUUID().toString();
            
            dropDownItem.setId(id);
            dropDownItem.setDeleted(false);
            
            return this.dropDownItemRepository.save(dropDownItem);
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
    public boolean deleteItem(String itemId) throws CustomException {
        try {            
            itemId = InputValidatorUtil.validateStringProperty("Drop Down Item Id", itemId);
            if (!this.dropDownItemRepository.isExistsById(itemId)) {
                throw new DoesNotExistException("Drop Down Item does not exists.Drop Down Item Id : " + itemId);
            }
            this.dropDownItemRepository.setStatus(true, itemId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }
        
    }
    
}
