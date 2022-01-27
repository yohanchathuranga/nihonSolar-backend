/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOProjectUser;
import com.nihon.entity.DOUser;
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
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<DOUser> listUsers(String query) {
        Query q = entityManager.createNativeQuery(query, DOUser.class);
        return q.getResultList();
    }

    @Override
    public Object countUsers(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getSingleResult();
    }

    @Override
    public List<DOProjectUser> listCustomers(String query) {
        Query q = entityManager.createNativeQuery(query, DOProjectUser.class);
        return q.getResultList();
    }

    @Override
    public Object countCustomers(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getSingleResult();
    }
}
