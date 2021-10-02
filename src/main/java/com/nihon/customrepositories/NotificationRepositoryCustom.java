/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.customrepositories;

import com.nihon.entity.DONotification;
import java.util.List;

/**
 *
 * @author yohan
 */
public interface NotificationRepositoryCustom {

    List<DONotification> listNotifications(String query);

    Object countNotifications(String query);
}
