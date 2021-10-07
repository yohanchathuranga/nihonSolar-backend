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
            long dayEndTime = DateTimeUtil.getDayStartEpochTime(currentTime);

            ArrayList<DOCustomerFeedback> feedbackList = customerFeedbackRepository.getNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOClearance> clearanceList = clearanceRepository.getNotificationList(dayStartTime, dayEndTime);
            ArrayList<DOStatusCheck> statusCheckList = statusCheckRepository.getNotificationList(dayStartTime, dayEndTime);

            feedbackNotification(feedbackList);
            clearanceNotification(clearanceList);
            statusCheckNotification(statusCheckList);

        } catch (CustomException ex) {
            throw ex;
        }
    }

    private void feedbackNotification(ArrayList<DOCustomerFeedback> customerFeedbacks) {
        try {
            ArrayList<DOUser> employees = userRepository.getEmployeeByRole("maketing_officer");
            ArrayList<String> emails = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
            }

            for (DOCustomerFeedback customerFeedback : customerFeedbacks) {
                DOMailSender mailSender = new DOMailSender();
                mailSender.setSubject("Customer Feedback Reminder");
                mailSender.setMessage("You should get customer feedback from project " + customerFeedback.getProjectId() + " today.");
                mailSender.setReceiver(emails);
                emailSender.sendEmail(mailSender);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    private void clearanceNotification(ArrayList<DOClearance> clearances) {
        try {
            ArrayList<DOUser> employees = userRepository.getEmployeeByRole("finance_officer");
            ArrayList<String> emails = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
            }

            for (DOClearance clearance : clearances) {
                DOMailSender mailSender = new DOMailSender();
                mailSender.setSubject("Clearance Reminder");
                mailSender.setMessage("You should check clearance of project " + clearance.getProjectId() + " today.");
                mailSender.setReceiver(emails);
                emailSender.sendEmail(mailSender);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

    private void statusCheckNotification(ArrayList<DOStatusCheck> statusChecks) {
        try {
            ArrayList<DOUser> employees = userRepository.getEmployeeByRole("finance_officer");
            ArrayList<String> emails = new ArrayList();

            for (DOUser employee : employees) {
                emails.add(employee.getEmail());
            }

            for (DOStatusCheck statusCheck : statusChecks) {
                DOMailSender mailSender = new DOMailSender();
                mailSender.setSubject("Ststus Check  Reminder");
                mailSender.setMessage("You should check status  of bank loan project " + statusCheck.getProjectId() + " today.");
                mailSender.setReceiver(emails);
                emailSender.sendEmail(mailSender);
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

}
