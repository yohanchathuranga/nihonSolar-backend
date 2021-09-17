/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.base;

import com.example.dataaccess.DAODataUtil;
import com.example.entity.DOCountRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOUser;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.InputValidatorUtil;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.DoesNotExistException;
import com.yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class UserManager {

    @Autowired
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;

    public UserManager(DAODataUtil dataUtil) {
        this.dataUtil = dataUtil;
    }

    public List<DOUser> listUsers(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "users");

            return this.userRepository.listUsers(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countUsers(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "users");
            Object o = this.userRepository.countUsers(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOUser getUserById(String userId) throws CustomException {
        try {

            userId = InputValidatorUtil.validateStringProperty("User Id", userId);
            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }
            return this.userRepository.findById(userId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOUser createUser(DOUser user) throws CustomException {
        try {
            String firstName = InputValidatorUtil.validateStringProperty("First Name", user.getFirstName());
            user.setFirstName(firstName);

            String lastName = InputValidatorUtil.validateStringProperty("Last Name", user.getLastName());
            user.setLastName(lastName);

            String contactNo = InputValidatorUtil.validateStringProperty("Contact No", user.getContactNo());
            user.setContact_no(contactNo);

            String email = InputValidatorUtil.validateStringProperty("Email", user.getEmail());
            user.setEmail(email);

            String nic = InputValidatorUtil.validateStringProperty("NIC", user.getNic());
            user.setNic(nic);

            String address = InputValidatorUtil.validateStringProperty("Address", user.getAddress());
            user.setStatus(address);

            String type = InputValidatorUtil.validateStringProperty("User Type", user.getType());
            user.setType(type);

            String password = InputValidatorUtil.validateStringProperty("Password", user.getPassword());
            user.setPassword(password);

            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                throw new InvalidInputException("Invalid type. Type :" + type);
            }
            String occupation = InputValidatorUtil.validateStringProperty("Occupation", user.getOccupation());
            user.setOccupation(occupation);

            String id = UUID.randomUUID().toString();

            user.setId(id);
            user.setStatus(DataUtil.USER_STATE_REGISTERD);
            user.setDeleted(false);

            DOUser userCreated = this.userRepository.save(user);
            return userCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteUser(String userId) throws CustomException {
        try {

            userId = InputValidatorUtil.validateStringProperty("User Id", userId);
            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists.User Id : " + userId);
            }
            this.userRepository.deleteUser(true, userId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
