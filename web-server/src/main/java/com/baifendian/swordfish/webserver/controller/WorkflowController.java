/*
 * Copyright (C) 2017 Baifendian Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baifendian.swordfish.webserver.controller;

/**
 * 工作流管理的服务入口
 */

import com.baifendian.swordfish.dao.model.ProjectFlow;
import com.baifendian.swordfish.dao.model.User;
import com.baifendian.swordfish.webserver.dto.WorkflowDto;
import com.baifendian.swordfish.webserver.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects/{projectName}")
public class WorkflowController {

  private static Logger logger = LoggerFactory.getLogger(WorkflowController.class.getName());

  @Autowired
  private WorkflowService workflowService;

  /**
   * 创建工作流
   *
   * @param operator
   * @param projectName
   * @param name
   * @param desc
   * @param proxyUser
   * @param queue
   * @param data
   * @param file
   * @param response
   */
  @PostMapping(value = "/workflows/{name}")
  public WorkflowDto createWorkflow(@RequestAttribute(value = "session.user") User operator,
                                    @PathVariable String projectName,
                                    @PathVariable String name,
                                    @RequestParam(value = "desc", required = false) String desc,
                                    @RequestParam(value = "proxyUser") String proxyUser,
                                    @RequestParam(value = "queue") String queue,
                                    @RequestParam(value = "data", required = false) String data,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam(value = "extras", required = false) String extras,
                                    HttpServletResponse response) {
    logger.info("Operator user {}, create workflow, project name: {}, workflow name: {}, desc: {}, proxyUser: {}, queue: {}, data: {}, file: [{},{}]",
        operator.getName(), projectName, name, desc, proxyUser, queue, data, (file == null) ? null : file.getName(), (file == null) ? null : file.getOriginalFilename());

    return new WorkflowDto(workflowService.createWorkflow(operator, projectName, name, desc, proxyUser, queue, data, file, extras, null));
  }

  /**
   * 修改或创建一个工作流, 不存在则创建
   *
   * @param operator
   * @param projectName
   * @param name
   * @param desc
   * @param proxyUser
   * @param queue
   * @param data
   * @param file
   * @param response
   */
  @PutMapping(value = "/workflows/{name}")
  public WorkflowDto putWorkflow(@RequestAttribute(value = "session.user") User operator,
                                 @PathVariable String projectName,
                                 @PathVariable String name,
                                 @RequestParam(value = "desc", required = false) String desc,
                                 @RequestParam(value = "proxyUser") String proxyUser,
                                 @RequestParam(value = "queue") String queue,
                                 @RequestParam(value = "data", required = false) String data,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 @RequestParam(value = "extras", required = false) String extras,
                                 HttpServletResponse response) {
    logger.info("Operator user {}, put workflow, project name: {}, workflow name: {}, desc: {}, proxyUser: {}, queue: {}, data: {}, file: [{},{}]",
        operator.getName(), projectName, name, desc, proxyUser, queue, data, (file == null) ? null : file.getName(), (file == null) ? null : file.getOriginalFilename());

    return new WorkflowDto(workflowService.putWorkflow(operator, projectName, name, desc, proxyUser, queue, data, file, extras));
  }

  /**
   * 修改一个工作流
   *
   * @param operator
   * @param projectName
   * @param name
   * @param desc
   * @param proxyUser
   * @param queue
   * @param data
   * @param file
   * @param response
   */
  @PatchMapping(value = "/workflows/{name}")
  public WorkflowDto patchWorkflow(@RequestAttribute(value = "session.user") User operator,
                                   @PathVariable String projectName,
                                   @PathVariable String name,
                                   @RequestParam(value = "desc", required = false) String desc,
                                   @RequestParam(value = "proxyUser", required = false) String proxyUser,
                                   @RequestParam(value = "queue", required = false) String queue,
                                   @RequestParam(value = "data", required = false) String data,
                                   @RequestParam(value = "file", required = false) MultipartFile file,
                                   @RequestParam(value = "extras", required = false) String extras,
                                   HttpServletResponse response) {
    logger.info("Operator user {}, modify workflow, project name: {}, workflow name: {}, desc: {}, proxyUser: {}, queue: {}, data: {}, file: [{},{}]",
        operator.getName(), projectName, name, desc, proxyUser, queue, data, (file == null) ? null : file.getName(), (file == null) ? null : file.getOriginalFilename());

    return new WorkflowDto(workflowService.patchWorkflow(operator, projectName, name, desc, proxyUser, queue, data, file, extras));
  }


  /**
   * copy一个工作流
   * @param operator
   * @param projectName
   * @param srcWorkflowName
   * @param destWorkflowName
   * @param response
   * @return
   */
  @PostMapping(value = "workflow-copy")
  public WorkflowDto postWorkflowCopy(@RequestAttribute(value = "session.user") User operator,
                                      @PathVariable String projectName,
                                      @RequestParam(value = "srcWorkflowName") String srcWorkflowName,
                                      @RequestParam(value = "destWorkflowName") String destWorkflowName,
                                      HttpServletResponse response){
    logger.info("Operator user {},project name: {} , source workflow: {}, dest workflow: {}",operator.getName(),projectName,srcWorkflowName,destWorkflowName);

    return new WorkflowDto(workflowService.postWorkflowCopy(operator,projectName,srcWorkflowName,destWorkflowName));
  }

  /**
   * 删除一个工作流
   *
   * @param operator
   * @param projectName
   * @param name
   * @param response
   */
  @DeleteMapping(value = "/workflows/{name}")
  public void deleteWorkflow(@RequestAttribute(value = "session.user") User operator,
                             @PathVariable String projectName,
                             @PathVariable String name,
                             HttpServletResponse response) {
    logger.info("Operator user {}, delete workflow, project name: {}, workflow name: {}",
        operator.getName(), projectName, name);

    workflowService.deleteProjectFlow(operator, projectName, name);
  }

  /**
   * 修改一个工作流的全局配置
   *
   * @param operator
   * @param projectName
   * @param queue
   * @param proxyUser
   * @param response
   */
  @PutMapping(value = "/workflows-conf")
  public void modifyWorkflowConf(@RequestAttribute(value = "session.user") User operator,
                                 @PathVariable String projectName,
                                 @RequestParam(value = "queue", required = false) String queue,
                                 @RequestParam(value = "proxyUser", required = false) String proxyUser,
                                 HttpServletResponse response) {
    logger.info("Operator user {}, modify workflow conf, project name: {}, proxyUser: {}, queue: {}",
        operator.getName(), projectName, proxyUser, queue);

    workflowService.modifyWorkflowConf(operator, projectName, queue, proxyUser);
  }

  /**
   * 查询一个项目下所有的工作流
   *
   * @param operator
   * @param projectName
   * @param response
   * @return
   */
  @GetMapping(value = "")
  public List<WorkflowDto> queryWorkflow(@RequestAttribute(value = "session.user") User operator,
                                         @PathVariable String projectName,
                                         HttpServletResponse response) {
    logger.info("Operator user {}, query workflow list of project, project name: {}",
        operator.getName(), projectName);

    List<ProjectFlow> projectFlowList = workflowService.queryAllProjectFlow(operator, projectName);
    List<WorkflowDto> workflowDtoList = new ArrayList<>();
    for (ProjectFlow projectFlow:projectFlowList){
      workflowDtoList.add(new WorkflowDto(projectFlow));
    }
    return workflowDtoList;
  }

  /**
   * 查询一个具体的工作流
   *
   * @param operator
   * @param projectName
   * @param name
   * @param response
   * @return
   */
  @GetMapping(value = "/workflows/{name}")
  public WorkflowDto queryWorkflowDetail(@RequestAttribute(value = "session.user") User operator,
                                         @PathVariable String projectName,
                                         @PathVariable String name,
                                         HttpServletResponse response) {
    logger.info("Operator user {}, query workflow detail, project name: {}, workflow name: {}",
        operator.getName(), projectName, name);

    return new WorkflowDto(workflowService.queryProjectFlow(operator, projectName, name));
  }

  /**
   * 下载指定文件
   *
   * @param operator
   * @param projectName
   * @param name
   * @param response
   * @return
   */
  @GetMapping(value = "/workflows/{name}/file")
  public ResponseEntity<Resource> downloadWorkflowDetail(@RequestAttribute(value = "session.user") User operator,
                                                         @PathVariable String projectName,
                                                         @PathVariable String name,
                                                         HttpServletResponse response) {
    logger.info("Operator user {}, download workflow, project name: {}, workflow name: {}",
        operator.getName(), projectName, name);

    org.springframework.core.io.Resource file = workflowService.downloadProjectFlowFile(operator, projectName, name);

    if (file == null) {
      return ResponseEntity
              .noContent().build();
    }

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);

  }
}
