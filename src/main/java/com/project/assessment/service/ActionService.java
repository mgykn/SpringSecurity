package com.project.assessment.service;

import java.util.Map;

import com.project.assessment.model.ToDoDto;
import com.project.assessment.model.ToDoResponse;
import com.project.assessment.response.BaseResponse;
import com.project.assessment.response.ToDosResponse;

public interface ActionService {

	Map<String, String> getCredentials();
	
	boolean findById(String clientId);

	ToDoResponse getToDo(String id);

	BaseResponse addTodo(ToDoDto toDo);

	ToDosResponse getAll();

	BaseResponse deleteToDo();

}
