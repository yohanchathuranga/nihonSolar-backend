/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DOProjectElectricityBoard;
import com.nihon.entity.DOListCountResult;
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
public class ProjectElectricityBoardRepositoryImpl implements ProjectElectricityBoardRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<DOProjectElectricityBoard> listProjectElectricityBoards(String query) {
        Query q = entityManager.createNativeQuery(query, DOProjectElectricityBoard.class);
        return q.getResultList();
    }

    @Override
    public Object countProjectElectricityBoards(String query) {
        Query q = entityManager.createNativeQuery(query);
        return q.getSingleResult();
    }

}
