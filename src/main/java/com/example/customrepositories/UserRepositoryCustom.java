/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.customrepositories;

import com.example.entity.DOUser;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface UserRepositoryCustom {

    List<DOUser> listUsers(String query);

    Object countUsers(String query);
}
