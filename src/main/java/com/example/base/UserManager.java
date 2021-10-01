/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.base;

import com.example.dataaccess.DAODataUtil;
import com.example.entity.DOCountRequest;
import com.example.entity.DOForgotPassword;
import com.example.entity.DOForgotPasswordRequest;
import com.example.entity.DOListCountResult;
import com.example.entity.DOListRequest;
import com.example.entity.DOLoginRequest;
import com.example.entity.DOMailSender;
import com.example.entity.DOResetPasswordRequest;
import com.example.entity.DOUser;
import com.example.repository.UserRepository;
import com.example.util.DataUtil;
import com.example.util.EmailSender;
import com.example.util.InputValidatorUtil;
import java.util.ArrayList;
import yohan.exceptions.AlreadyExistException;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.exceptions.LoginFailedException;

/**
 *
 * @author yohan
 */
@Service
public class UserManager {

    @Autowired
    private UserRepository userRepository;
    private final DAODataUtil dataUtil;
    EmailSender emailSender;

    public UserManager(DAODataUtil dataUtil, EmailSender emailSender) {
        this.dataUtil = dataUtil;
        this.emailSender = emailSender;
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

            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE) && !type.toUpperCase().equals(DataUtil.USER_TYPE_ADMIN)) {
                throw new InvalidInputException("Invalid type. Type :" + type);
            }

            if (type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                String password = InputValidatorUtil.validateStringProperty("Password", user.getPassword());
                user.setPassword(password);
            }

            if (this.userRepository.isExistsByEmail(email)) {
                throw new AlreadyExistException("Email already exists. Email : " + email);
            }

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

    public DOUser updateUser(DOUser user) throws CustomException {
        try {
            String userId = InputValidatorUtil.validateStringProperty("User Id", user.getId());
            user.setId(userId);

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

            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                throw new InvalidInputException("Invalid type. Type :" + type);
            }

            DOUser userExists = this.userRepository.getById(userId);
            if (!email.equals(userExists.getEmail())) {
                if (!this.userRepository.isExistsByEmail(email)) {
                    throw new AlreadyExistException("Email already exists. Email : " + email);
                }
            }
            user.setPassword(userExists.getPassword());
            user.setDeleted(false);

            DOUser userUpdated = this.userRepository.save(user);
            return userUpdated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOUser userLogin(DOLoginRequest loginRequest) throws CustomException {
        try {

            String email = InputValidatorUtil.validateStringProperty("Email", loginRequest.getEmail());
            loginRequest.setEmail(email);

            String password = InputValidatorUtil.validateStringProperty("Password", loginRequest.getPassword());
            loginRequest.setPassword(password);

            if (!this.userRepository.isExistsByEmail(email)) {
                throw new DoesNotExistException("User does not exists. Email: " + email);
            }

            DOUser user = this.userRepository.getByEmail(email);

            if (!user.getPassword().equals(password)) {
                throw new LoginFailedException("Password does not match");
            }

            return user;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public void forgotPasswordRequest(DOForgotPasswordRequest forgotPasswordRequest) throws CustomException {       
        try {

            String email = InputValidatorUtil.validateStringProperty("Email", forgotPasswordRequest.getEmail());
            forgotPasswordRequest.setEmail(email);

            if (!this.userRepository.isExistsByEmail(email)) {
                throw new DoesNotExistException("User does not exists. Email: " + email);
            }

            DOUser user = this.userRepository.getByEmail(email);

            String verificationCode = String.valueOf((int) (Math.random() * (999999 - 100000)) + 100000);
            if (userRepository.isForgetPasswordExistsByUserId(user.getId())) {
                userRepository.updateForgotPassword(verificationCode, user.getId());
            } else {
                userRepository.createForgotPassword(user.getId(), email, verificationCode);
            }

            ArrayList<String> emails = new ArrayList();
            emails.add(email);
            DOMailSender mailSender = new DOMailSender();
            mailSender.setSubject("User Verification Code");
            mailSender.setMessage("Your Verification Code is " + verificationCode + ".");
            mailSender.setReceiver(emails);
            emailSender.sendEmail(mailSender);

        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOUser forgotPassword(DOForgotPassword forgotPassword) throws CustomException {
        try {

            String email = InputValidatorUtil.validateStringProperty("Email", forgotPassword.getEmail());
            forgotPassword.setEmail(email);

            String verificationCode = InputValidatorUtil.validateStringProperty("Verification Code", forgotPassword.getVerificationCode());
            forgotPassword.setVerificationCode(verificationCode);
            
            String password = InputValidatorUtil.validateStringProperty("Password", forgotPassword.getPassword());
            forgotPassword.setPassword(password);

            if (!this.userRepository.isExistsByEmail(email)) {
                throw new DoesNotExistException("User does not exists. Email: " + email);
            }

            DOUser user = this.userRepository.getByEmail(email);
            String vCode = userRepository.getForgotPassword(user.getId());
            if(!vCode.equals(verificationCode)){
                throw new LoginFailedException();
            }
            user.setPassword(password);
            userRepository.save(user);
            return user;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOUser resetPassword(DOResetPasswordRequest resetPasswordRequest) throws CustomException {
        try {

            String userId = InputValidatorUtil.validateStringProperty("User Id", resetPasswordRequest.getUserId());
            resetPasswordRequest.setUserId(userId);

            String oldPassword = InputValidatorUtil.validateStringProperty("Old Password", resetPasswordRequest.getOldPassword());
            resetPasswordRequest.setOldPassword(oldPassword);

            String newPassword = InputValidatorUtil.validateStringProperty("New Password", resetPasswordRequest.getNewPassword());
            resetPasswordRequest.setNewPassword(newPassword);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            DOUser user = this.userRepository.getById(userId);
            if(!user.getPassword().equals(oldPassword)){
                throw new LoginFailedException();
            }
            user.setPassword(newPassword);
            userRepository.save(user);
            return user;
        } catch (CustomException ex) {
            throw ex;
        }
    }

}
