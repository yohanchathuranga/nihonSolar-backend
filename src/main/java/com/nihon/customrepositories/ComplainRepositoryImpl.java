/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOComplain;
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
public class ComplainRepositoryImpl implements ComplainRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<DOComplain> listComplains(String query) {
        Query q = entityManager.createNativeQuery(query, DOComplain.class);
        return q.getResultList();
    }

    @Override
    public Object countComplains(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getSingleResult();
    }

}
