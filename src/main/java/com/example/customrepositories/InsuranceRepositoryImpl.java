/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.customrepositories;

import com.example.entity.DOInsurance;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yohan
 */
@Repository
@NoRepositoryBean
@Transactional(readOnly = true)
public class InsuranceRepositoryImpl implements InsuranceRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<DOInsurance> listInsurances(String query) {
        Query q = entityManager.createNativeQuery(query, DOInsurance.class);
        return q.getResultList();
    }

    @Override
    public Object countInsurances(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getSingleResult();
    }

}
