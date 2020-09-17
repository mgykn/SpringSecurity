package com.project.assessment.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.assessment.model.ToDoDto;
import com.project.assessment.model.ToDoResponse;
import com.project.assessment.response.BaseResponse;
import com.project.assessment.response.ToDosResponse;
import com.project.assessment.service.ActionService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequestMapping(value = "/v1", produces = "application/json", consumes = "application/json")
@RestController
public class ActionController {

	public static final String MSG_INVALID_ID = "id.@NotBlank";
	
	@Autowired
	private ActionService actionService;

	@ApiOperation(value = "get", notes = "get.")
	@RequestMapping(value = "/todo/get", method = RequestMethod.GET)
	public ResponseEntity<ToDoResponse> getToDo(
			@Valid @NotBlank(message = MSG_INVALID_ID) @RequestParam(value = "id", required = true) @ApiIgnore String id) {
		log.info("Starting getToDo with {}",id);
		ToDoResponse response = this.actionService.getToDo(id);
		return ResponseEntity.accepted().body(response);
	}

	@ApiOperation(value = "list", notes = "List All")
	@RequestMapping(value = "/todo/list", method = RequestMethod.GET)
	public ResponseEntity<ToDosResponse> listAll() {
		log.info("Starting list All");
		ToDosResponse response = this.actionService.getAll();
		return ResponseEntity.accepted().body(response);
	}

	@ApiOperation(value = "add", notes = "add")
	@RequestMapping(value = "/todo/add", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> addToDo(@RequestBody ToDoDto toDo) {
		log.info("Starting addToDo with {}", toDo);
		BaseResponse response = this.actionService.addTodo(toDo);
		return ResponseEntity.accepted().body(response);
	}
	
	@ApiOperation(value = "delete ", notes = "delete")
	@RequestMapping(value = "/todo/delete", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> deleteTodo() {
		log.info("Starting deleteToDo ");
		BaseResponse response = this.actionService.deleteToDo();
		return ResponseEntity.accepted().body(response);
	}

}
