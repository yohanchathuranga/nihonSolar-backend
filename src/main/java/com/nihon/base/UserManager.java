/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOForgotPassword;
import com.nihon.entity.DOForgotPasswordRequest;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOLoginRequest;
import com.nihon.entity.DOMailSender;
import com.nihon.entity.DOProjectUser;
import com.nihon.entity.DOResetPasswordRequest;
import com.nihon.entity.DOUser;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.EmailSender;
import com.nihon.util.InputValidatorUtil;
import java.security.MessageDigest;
import java.util.ArrayList;
import yohan.exceptions.AlreadyExistException;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import yohan.exceptions.InvalidInputException;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.commons.filters.DOPropertyFilter;
import yohan.commons.filters.FilterUtil;
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

            String address = InputValidatorUtil.validateStringProperty("Address", user.getAddress());
            user.setAddress(address);

            String type = InputValidatorUtil.validateStringProperty("User Type", user.getType());
            user.setType(type);

            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE) && !type.toUpperCase().equals(DataUtil.USER_TYPE_ADMIN)) {
                throw new InvalidInputException("Invalid type. Type :" + type);
            }

            if (type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                String email = InputValidatorUtil.validateStringProperty("Email", user.getEmail());
                user.setEmail(email);

                String nic = InputValidatorUtil.validateStringProperty("NIC", user.getNic());
                user.setNic(nic);

                String password = InputValidatorUtil.validateStringProperty("Password", user.getPassword());
                password = getSHAHash(password);
                user.setPassword(password);

                if (this.userRepository.isExistsByEmail(email)) {
                    throw new AlreadyExistException("Email already exists. Email : " + email);
                }

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

            String address = InputValidatorUtil.validateStringProperty("Address", user.getAddress());
            user.setAddress(address);

            String type = InputValidatorUtil.validateStringProperty("User Type", user.getType());
            user.setType(type);

            if (!type.toUpperCase().equals(DataUtil.USER_TYPE_CUSTOMER) && !type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                throw new InvalidInputException("Invalid type. Type :" + type);
            }

            DOUser userExists = this.userRepository.getById(userId);
            if (type.toUpperCase().equals(DataUtil.USER_TYPE_EMPLOYEE)) {
                String email = InputValidatorUtil.validateStringProperty("Email", user.getEmail());
                user.setEmail(email);

                String nic = InputValidatorUtil.validateStringProperty("NIC", user.getNic());
                user.setNic(nic);

                if (!email.equals(userExists.getEmail())) {
                    if (this.userRepository.isExistsByEmail(email)) {
                        throw new AlreadyExistException("Email already exists. Email : " + email);
                    }
                }
            }

            user.setStatus(userExists.getStatus());
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
            password = getSHAHash(password);
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
            password = getSHAHash(password);
            forgotPassword.setPassword(password);

            if (!this.userRepository.isExistsByEmail(email)) {
                throw new DoesNotExistException("User does not exists. Email: " + email);
            }

            DOUser user = this.userRepository.getByEmail(email);
            String vCode = userRepository.getForgotPassword(user.getId());
            if (!vCode.equals(verificationCode)) {
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
            oldPassword = getSHAHash(oldPassword);
            resetPasswordRequest.setOldPassword(oldPassword);

            String newPassword = InputValidatorUtil.validateStringProperty("New Password", resetPasswordRequest.getNewPassword());
            newPassword = getSHAHash(newPassword);
            resetPasswordRequest.setNewPassword(newPassword);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            DOUser user = this.userRepository.getById(userId);
            if (!user.getPassword().equals(oldPassword)) {
                throw new LoginFailedException();
            }
            user.setPassword(newPassword);
            userRepository.save(user);
            return user;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    private String getSHAHash(String input) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

    public List<DOProjectUser> listCustomers(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
//            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "view_users");
            final String like = where(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit());
            return this.userRepository.getCustomers(like, listRequest.getLimit(), ((listRequest.getPage() - 1) * listRequest.getLimit()));
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countCustomers(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "view_users");
            Object o = this.userRepository.countCustomers(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public String where(List<DOPropertyFilter> filterData, ArrayList<String> orderFields, boolean isDescending, int page, int limit) throws CustomException {
//        String sql = "SELECT u.id,u.first_name as firstName,u.last_name as lastName,u.project_id as projectId ,u.email,u.contact_no as contactNo,u.address,u.occupation FROM view_users where deleted =0 ";
//        String whereClause = "";
//        if (filterData != null && filterData.size() > 0) {
//            whereClause = FilterUtil.generateWhereClause((ArrayList<DOPropertyFilter>) filterData);
//        }
//
//        if (!whereClause.isEmpty()) {
//            sql = sql + " and (" + whereClause + ") ";
//        }
//
//        if (orderFields != null && orderFields.size() > 0) {
//            String orderStr = String.join(",", orderFields);
//            sql = sql + "order by " + orderStr;
//            if (isDescending) {
//                sql += " desc";
//            }
//        } else {
//            sql = sql + "order by id";
//            if (isDescending) {
//                sql += " desc";
//            }
//        }
//
//        sql = sql + " limit " + limit + " offset " + ((page - 1) * limit);

        String like = (String) filterData.get(0).getValue();

        String sql = "%" + like + "%";
        return sql;
    }

}
