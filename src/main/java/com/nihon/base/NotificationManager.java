/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOClearance;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOCustomerFeedback;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.entity.DOMailSender;
import com.nihon.entity.DONotification;
import com.nihon.entity.DOStatusCheck;
import com.nihon.entity.DOUser;
import com.nihon.repository.ClearanceRepository;
import com.nihon.repository.CustomerFeedbackRepository;
import com.nihon.repository.ProjectRepository;
import com.nihon.repository.NotificationRepository;
import com.nihon.repository.StatusCheckRepository;
import com.nihon.repository.UserRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.DateTimeUtil;
import com.nihon.util.EmailSender;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author yohan
 */
@Service
public class NotificationManager {

    @Autowired
    private NotificationRepository notificationRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CustomerFeedbackRepository customerFeedbackRepository;
    private final ClearanceRepository clearanceRepository;
    private final StatusCheckRepository statusCheckRepository;
    private final EmailSender emailSender;
    private final DAODataUtil dataUtil;

    public NotificationManager(ProjectRepository projectRepository, UserRepository userRepository, CustomerFeedbackRepository customerFeedbackRepository, ClearanceRepository clearanceRepository, StatusCheckRepository statusCheckRepository, EmailSender emailSender, DAODataUtil dataUtil) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.customerFeedbackRepository = customerFeedbackRepository;
        this.clearanceRepository = clearanceRepository;
        this.statusCheckRepository = statusCheckRepository;
        this.emailSender = emailSender;
        this.dataUtil = dataUtil;
    }

//    public NotificationManager() {
//    }
    public List<DONotification> listNotifications(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "notification");

            return this.notificationRepository.listNotifications(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countNotifications(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "notification");
            Object o = this.notificationRepository.countNotifications(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DONotification getNotificationById(String notificationId) throws CustomException {
        try {

            notificationId = InputValidatorUtil.validateStringProperty("Notification Id", notificationId);
            if (!this.notificationRepository.isExistsById(notificationId)) {
                throw new DoesNotExistException("Notification does not exists. Notification Id : " + notificationId);
            }
            return this.notificationRepository.findById(notificationId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DONotification createNotification(DONotification notification) throws CustomException {
        try {

            String userId = InputValidatorUtil.validateStringProperty("User Id", notification.getUserId());
            notification.setUserId(userId);

            long currentTime = DateTimeUtil.getCurrentTime();
            notification.setCreatedDate(currentTime);

            String method = InputValidatorUtil.validateStringProperty("Send Method", notification.getMethod());
            notification.setMethod(method);

            String value = InputValidatorUtil.validateStringProperty("Value", notification.getValue());
            notification.setValue(value);

            String message = InputValidatorUtil.validateStringProperty("Message", notification.getMessage());
            notification.setMessage(message);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

            String id = UUID.randomUUID().toString();

            notification.setId(id);
            notification.setStatus(DataUtil.NOTIFICATION_STATE_NEW);
            notification.setDeleted(false);

            DONotification notificationCreated = this.notificationRepository.save(notification);
            return notificationCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteNotification(String notificationId) throws CustomException {
        try {

            notificationId = InputValidatorUtil.validateStringProperty("Notification Id", notificationId);
            if (!this.notificationRepository.isExistsById(notificationId)) {
                throw new DoesNotExistException("Notification does not exists.Notification Id : " + notificationId);
            }
            this.notificationRepository.deleteNotification(true, notificationId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DONotification updateNotification(DONotification notification) throws CustomException {
        try {
            String notificationId = InputValidatorUtil.validateStringProperty("Notification Id", notification.getId());
            notification.setId(notificationId);

            if (!this.notificationRepository.isExistsById(notificationId)) {
                throw new DoesNotExistException("Notification does not exists.Notification Id : " + notificationId);
            }

            String userId = InputValidatorUtil.validateStringProperty("User Id", notification.getUserId());
            notification.setUserId(userId);

            long currentTime = DateTimeUtil.getCurrentTime();
            notification.setCreatedDate(currentTime);

            String method = InputValidatorUtil.validateStringProperty("Send Method", notification.getMethod());
            notification.setMethod(method);

            String value = InputValidatorUtil.validateStringProperty("Value", notification.getValue());
            notification.setValue(value);

            String message = InputValidatorUtil.validateStringProperty("Message", notification.getMessage());
            notification.setMessage(message);

            if (!this.userRepository.isExistsById(userId)) {
                throw new DoesNotExistException("User does not exists. User Id : " + userId);
            }

//            notification.setStatus(DataUtil.QUOTATION_STATE_NEW);
            notification.setStatus(notification.getStatus());
            notification.setDeleted(false);

            DONotification notificationCreated = this.notificationRepository.save(notification);
            return notificationCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public void processNotification() throws CustomException {
        try {
            long currentTime = DateTimeUtil.getCurrentTime();
            long dayStartTime = DateTimeUtil.getDayStartEpochTime(currentTime);
            long dayEndTime = DateTimeUtil.getDayEndEpochTime(currentTime);

            ArrayList<DOCustomerFeedback> feedbackList = customerFeedbackRepository.getNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOStatusCheck> clearanceList = statusCheckRepository.getClearancenNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOStatusCheck> statusCheckList = statusCheckRepository.getBankLoanNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOStatusCheck> insuranceCheckList = statusCheckRepository.getInsuranceNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOStatusCheck> serviceCheckList = statusCheckRepository.getNotificationListByType(dayStartTime, dayEndTime, DataUtil.STATUS_CHECK_TYPE_SERVICE);
            ArrayList<DOStatusCheck> siteVisitCheckList = statusCheckRepository.getNotificationListByType(dayStartTime, dayEndTime, DataUtil.STATUS_CHECK_TYPE_SITE_VISIT);
            ArrayList<DOStatusCheck> complainCheckList = statusCheckRepository.getNotificationListByType(dayStartTime, dayEndTime, DataUtil.STATUS_CHECK_TYPE_COMPLAIN);
            ArrayList<DOStatusCheck> quotationCheckList = statusCheckRepository.getNotificationListByType(dayStartTime, dayEndTime, DataUtil.STATUS_CHECK_TYPE_QUOTATION);

            feedbackNotification(feedbackList);
            clearanceNotification(clearanceList);
            statusCheckNotification(statusCheckList);
            insuranceCheckNotification(insuranceCheckList);
            serviceCheckNotification(serviceCheckList);
            siteVisitCheckNotification(siteVisitCheckList);
            complainCheckNotification(complainCheckList);
            quotationCheckNotification(quotationCheckList);

        } catch (CustomException ex) {
            throw ex;
        }
    }

    private void feedbackNotification(ArrayList<DOCustomerFeedback> customerFeedbacks) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOCustomerFeedback customerFeedback : customerFeedbacks) {
                    String message = "You should get customer feedback from project " + customerFeedback.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(customerFeedback.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_CUSTOMER_FEEDBACK);
                        notification.setMethod("email");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    DOMailSender mailSender = new DOMailSender();
                    mailSender.setSubject("Customer Feedback Reminder");
                    mailSender.setMessage(message);
                    mailSender.setReceiver(emails);
                    emailSender.sendEmail(mailSender);

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }
                    customerFeedbackRepository.setStatus(DataUtil.NOTIFICATION_SENT, customerFeedback.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }
    }

    private void clearanceNotification(ArrayList<DOStatusCheck> clearances) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_OFFICE_ASSISTANT);
            roles.add(DataUtil.EMPLOYEE_ROLE_FINANCIAL_OFFICER);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck clearance : clearances) {
                    String message = "You should check clearance of project " + clearance.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(clearance.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_CLEARANCE);
                        notification.setMethod("email");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    DOMailSender mailSender = new DOMailSender();
                    mailSender.setSubject("Clearance Reminder");
                    mailSender.setMessage(message);
                    mailSender.setReceiver(emails);
                    emailSender.sendEmail(mailSender);

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, clearance.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }
    }

    private void statusCheckNotification(ArrayList<DOStatusCheck> statusChecks) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_FINANCIAL_OFFICER);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck statusCheck : statusChecks) {
                    String message = "You should check status  of bank loan project " + statusCheck.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(statusCheck.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_BANK_LOAN);
                        notification.setMethod("email");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    DOMailSender mailSender = new DOMailSender();
                    mailSender.setSubject("Ststus Check  Reminder");
                    mailSender.setMessage(message);
                    mailSender.setReceiver(emails);
                    emailSender.sendEmail(mailSender);

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }
                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, statusCheck.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }
    }

    private void insuranceCheckNotification(ArrayList<DOStatusCheck> insurances) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_OFFICE_ASSISTANT);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck insurance : insurances) {
                    String message = "You should renew insurance of project " + insurance.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(insurance.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_INSURANCE);
                        notification.setMethod("email");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    DOMailSender mailSender = new DOMailSender();
                    mailSender.setSubject("Insurance Renew Reminder");
                    mailSender.setMessage(message);
                    mailSender.setReceiver(emails);
                    emailSender.sendEmail(mailSender);

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, insurance.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }

    }

    private void serviceCheckNotification(ArrayList<DOStatusCheck> services) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_OFFICE_ASSISTANT);
            roles.add(DataUtil.EMPLOYEE_ROLE_ENGINEER);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck service : services) {
                    String message = "You should service the project " + service.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(service.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_SERVICE);
                        notification.setMethod("list");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, service.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }

    }
    
    private void siteVisitCheckNotification(ArrayList<DOStatusCheck> siteVisits) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_TECHNICAL_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_ENGINEER);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck siteVisit : siteVisits) {
                    String message = "You should site visit of the project " + siteVisit.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(siteVisit.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_SITE_VISIT);
                        notification.setMethod("list");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, siteVisit.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }

    }
    
    private void complainCheckNotification(ArrayList<DOStatusCheck> complains) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_OFFICE_ASSISTANT);
            roles.add(DataUtil.EMPLOYEE_ROLE_ENGINEER);
            roles.add(DataUtil.EMPLOYEE_ROLE_TECHNICAL_OFFICER);
            
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck complain : complains) {
                    String message = "You should be resolve complain of the project " + complain.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(complain.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_COMPLAIN);
                        notification.setMethod("list");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, complain.getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }

    }
    
    private void quotationCheckNotification(ArrayList<DOStatusCheck> quotations) throws CustomException {
        try {
            ArrayList<String> roles = new ArrayList();
            roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
            roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
            roles.add(DataUtil.EMPLOYEE_ROLE_OFFICE_ASSISTANT);
            String sql = employeeListQuery(roles);
            List<DOUser> employees = userRepository.listUsers(sql);

            ArrayList<String> emails = new ArrayList();
            ArrayList<String> users = new ArrayList();
            ArrayList<DONotification> notifications = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
                users.add(employee.getId());
            }

            if (!emails.isEmpty()) {
                for (DOStatusCheck quotation : quotations) {
                    String message = "You should send a quotation for the project " + quotation.getProjectId() + " today.";
                    int i = 0;
                    for (String user : users) {
                        DONotification notification = new DONotification();
                        notification.setUserId(user);
                        notification.setProjectId(quotation.getProjectId());
                        notification.setType(DataUtil.NOTIFICATION_TYPE_QUOTATION);
                        notification.setMethod("list");
                        notification.setMessage(message);
                        notification.setValue(emails.get(i));

                        notification = createNotification(notification);
                        notifications.add(notification);
                        i++;
                    }

                    for (DONotification notification : notifications) {
                        notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_SENT, notification.getId());
                    }

                    statusCheckRepository.setStatus(DataUtil.NOTIFICATION_SENT, quotation   .getId());
                }
            }

        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean readNotification(String notificationId) throws CustomException {
        try {

            notificationId = InputValidatorUtil.validateStringProperty("Notification Id", notificationId);
            if (!this.notificationRepository.isExistsById(notificationId)) {
                throw new DoesNotExistException("Notification does not exists. Notification Id : " + notificationId);
            }
            this.notificationRepository.setStatus(DataUtil.NOTIFICATION_STATE_READ, notificationId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public String employeeListQuery(ArrayList<String> roles) throws CustomException {
        try {
            String sql = "";

            sql = "select * from users where deleted = 0 and type ='EMPLOYEE' ";

            String sql1 = "";
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if (sql1.isEmpty()) {
                        sql1 = sql1 + " occupation = '" + role + "'";
                    } else {
                        sql1 = sql1 + " or occupation = '" + role + "'";
                    }
                }

                sql = sql + "and (" + sql1 + ")";

            }
//            System.out.println("Sql : " + sql);
            return sql;
        } catch (Exception ex) {
            throw ex;
        }

    }

//    public static void main(String[] args) throws CustomException {
//        NotificationManager nm = new NotificationManager();
//        ArrayList<String> roles = new ArrayList();
//        roles.add(DataUtil.EMPLOYEE_ROLE_ADMINISTRATOR);
//        roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_MANAGER);
//        roles.add(DataUtil.EMPLOYEE_ROLE_MARKETING_OFFICER);
//        roles.add(DataUtil.EMPLOYEE_ROLE_ENGINEER);
//        nm.employeeListQuery(roles);
//    }
}
