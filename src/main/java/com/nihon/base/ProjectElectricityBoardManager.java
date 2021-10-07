/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nihon.base;

import com.nihon.dataaccess.DAODataUtil;
import com.nihon.entity.DOCountRequest;
import com.nihon.entity.DOProjectElectricityBoard;
import com.nihon.entity.DOListCountResult;
import com.nihon.entity.DOListRequest;
import com.nihon.repository.ProjectElectricityBoardRepository;
import com.nihon.repository.ProjectRepository;
import com.nihon.util.DataUtil;
import com.nihon.util.InputValidatorUtil;
import yohan.exceptions.CustomException;
import yohan.exceptions.DoesNotExistException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yohan.exceptions.AlreadyExistException;

/**
 *
 * @author yohan
 */
@Service
public class ProjectElectricityBoardManager {

    @Autowired
    private ProjectElectricityBoardRepository projectElectricityBoardRepository;
    private final DAODataUtil dataUtil;
    private ProjectRepository projectRepository;

    public ProjectElectricityBoardManager(DAODataUtil dataUtil, ProjectRepository projectRepository) {
        this.dataUtil = dataUtil;
        this.projectRepository = projectRepository;
    }

    public List<DOProjectElectricityBoard> listProjectElectricityBoards(DOListRequest listRequest) throws CustomException {
        try {
            if (listRequest.getPage() <= 0) {
                listRequest.setPage(1);
            }
            if (listRequest.getLimit() <= 0) {
                listRequest.setLimit(20);
            }
            final String sql = dataUtil.listFilterData(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit(), "electricity_board_process");

            return this.projectElectricityBoardRepository.listProjectElectricityBoards(sql);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOListCountResult countProjectElectricityBoards(DOCountRequest countRequest) throws CustomException {
        try {
            final String sql = dataUtil.countFilterdata(countRequest.getFilterData(), "electricity_board_process");
            Object o = this.projectElectricityBoardRepository.countProjectElectricityBoards(sql);
            DOListCountResult countResult = new DOListCountResult();
            countResult.setCount(Integer.valueOf(o.toString()));
            return countResult;
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public DOProjectElectricityBoard getProjectElectricityBoardById(String projectElectricityBoardId) throws CustomException {
        try {

            projectElectricityBoardId = InputValidatorUtil.validateStringProperty("Project Electricity Board Id", projectElectricityBoardId);
            if (!this.projectElectricityBoardRepository.isExistsById(projectElectricityBoardId)) {
                throw new DoesNotExistException("Project Electricity Board does not exists.Project Electricity Board Id : " + projectElectricityBoardId);
            }
            return this.projectElectricityBoardRepository.findById(projectElectricityBoardId)
                    .orElse(null);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOProjectElectricityBoard createProjectElectricityBoard(DOProjectElectricityBoard projectElectricityBoard) throws CustomException {
        try {
            String projectId = InputValidatorUtil.validateStringProperty("Project Id", projectElectricityBoard.getProjectId());
            projectElectricityBoard.setProjectId(projectId);

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            if (projectElectricityBoardRepository.getItemsByProjectId(projectId) != null) {
                throw new AlreadyExistException("Already exists for Projrct id . Project Id :" + projectId);
            }

            String id = UUID.randomUUID().toString();

            projectElectricityBoard.setId(id);
            projectElectricityBoard.setStatus(DataUtil.STATE_NEW);
            projectElectricityBoard.setDeleted(false);

            return this.projectElectricityBoardRepository.save(projectElectricityBoard);
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public boolean deleteProjectElectricityBoard(String projectElectricityBoardId) throws CustomException {
        try {

            projectElectricityBoardId = InputValidatorUtil.validateStringProperty("Project Electricity Board Id", projectElectricityBoardId);
            if (!this.projectElectricityBoardRepository.isExistsById(projectElectricityBoardId)) {
                throw new DoesNotExistException("Project Electricity Board does not exists.Project Electricity Board Id : " + projectElectricityBoardId);
            }
            this.projectElectricityBoardRepository.delete(true, projectElectricityBoardId);
            return true;
        } catch (CustomException ex) {
            throw ex;
        }

    }

    public DOProjectElectricityBoard updateProject(DOProjectElectricityBoard projectElectricityBoard) throws CustomException {
        try {
            String projectElectricityBoardId = InputValidatorUtil.validateStringProperty("Project Electricity Board Id", projectElectricityBoard.getId());
            projectElectricityBoard.setId(projectElectricityBoardId);

            if (!this.projectElectricityBoardRepository.isExistsById(projectElectricityBoardId)) {
                throw new DoesNotExistException("Project Electricity board does not exists. Project Electricity board Id : " + projectElectricityBoardId);
            }

            DOProjectElectricityBoard projectElectricityBoardExists = projectElectricityBoardRepository.findById(projectElectricityBoardId).get();

            String projectId = projectElectricityBoardExists.getProjectId();

            if (!this.projectRepository.isExistsById(projectId)) {
                throw new DoesNotExistException("Project does not exists. Project Id : " + projectId);
            }

            if (!this.projectRepository.checkProjectAlive(projectId)) {
                throw new DoesNotExistException("Action not allowed in current state. Project Id : " + projectId);
            }

            projectElectricityBoard.setProjectId(projectId);
            projectElectricityBoard.setDeleted(false);

            DOProjectElectricityBoard projectCreated = this.projectElectricityBoardRepository.save(projectElectricityBoard);
            return projectCreated;
        } catch (CustomException ex) {
            throw ex;
        }

    }

}
