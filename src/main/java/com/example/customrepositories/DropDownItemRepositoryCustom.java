/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.customrepositories;

import com.example.entity.DODropDownItem;
import com.example.entity.DOListCountResult;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author yohan
 */
public interface DropDownItemRepositoryCustom {

    List<DODropDownItem> listItems(String query);

    Object countItems(String query);
}
