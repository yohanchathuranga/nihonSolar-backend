/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOClearance;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface ClearanceRepositoryCustom {

    List<DOClearance> listClearances(String query);

    Object countClearances(String query);
}
