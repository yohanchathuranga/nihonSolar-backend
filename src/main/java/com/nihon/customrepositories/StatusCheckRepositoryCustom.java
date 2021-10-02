/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOStatusCheck;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface StatusCheckRepositoryCustom {

    List<DOStatusCheck> listStatusChecks(String query);

    Object countStatusChecks(String query);
}
