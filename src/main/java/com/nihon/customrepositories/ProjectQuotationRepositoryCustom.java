/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOProjectQuotation;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface ProjectQuotationRepositoryCustom {

    List<DOProjectQuotation> listProjectQuotations(String query);

    Object countProjectQuotations(String query);
}
