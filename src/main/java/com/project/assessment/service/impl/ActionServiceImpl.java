package com.project.assessment.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.project.assessment.Utility;
import com.project.assessment.exception.InvalidParameterException;
import com.project.assessment.exception.SystemErrorException;
import com.project.assessment.model.ToDoDto;
import com.project.assessment.model.ToDoResponse;
import com.project.assessment.response.BaseResponse;
import com.project.assessment.response.ToDosResponse;
import com.project.assessment.service.ActionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActionServiceImpl implements ActionService {

	private Map<String, String> mapData = new HashMap<String, String>(){
		{
			put("101", "Yapılacak 1");
			put("102", "Yapılacak 2");
		}
	};
	
	
	Map<String, String> mapCredentials = new HashMap<String, String>() {
		{
			put("Client1", "ClientSecret1");
			put("Client2", "ClientSecret2");
		}
	};

	@Override
	public Map<String, String> getCredentials() {
		return mapCredentials;
	}
	
	private Map<String, String> getMapData(){
		return mapData;
	}
	
	
	private boolean findToDoById(String id) {
		return mapData.containsKey(id);
	}

	@Override
	public boolean findById(String clientId) {
		return mapCredentials.containsKey(clientId);
	}

	@Override
	public ToDoResponse getToDo(String id) {
		log.info("Received addTodo request.");
		long execStartTime = System.currentTimeMillis();
		ToDoResponse response = new ToDoResponse();

		if (!findToDoById(id)) {
			
			log.error("could not found any toDo with this id:{}",id);
			throw new InvalidParameterException("could not found any toDo");

		}
		ToDoDto obj = new ToDoDto(id,mapData.get(id));
		response.setObj(obj);
		
		log.info("Finished execution (getToDo) in :{} ms, response :{}.", System.currentTimeMillis() - execStartTime,
				response);

		return response;
	}

	@Override
	public BaseResponse addTodo(ToDoDto toDo) {
		long execStartTime = System.currentTimeMillis();
		BaseResponse response = new BaseResponse();
		log.info("Received addTodo request : {}.", toDo);

		addData(toDo.getId(), toDo.getName());

		log.info("Finished execution (addTodo) in :{} ms, response :{}.", System.currentTimeMillis() - execStartTime,
				response);

		return response;
	}

	public void addData(String name, String value) {
		this.mapData.put(name, value);
	}
	@Override
	public ToDosResponse getAll() {
		log.info("Received getAll request.");

		long execStartTime = System.currentTimeMillis();
		ToDosResponse response = new ToDosResponse();
		Map<String, String> map = getData();
		 List<ToDoDto> objs = null;
		 ToDoDto toDoDto =new ToDoDto();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			log.info(key + " : " + value);
			toDoDto.setId(key);
			toDoDto.setName(value);
			objs.add(toDoDto);	
		}
		response.setObjs(objs);
		

		log.info("Finished execution (getAll) in :{} ms, response :{}.", System.currentTimeMillis() - execStartTime,
				response);

		return response;
	}

	@Override
	public BaseResponse deleteToDo() {
		log.info("Received deleteToDo request.");

		long execStartTime = System.currentTimeMillis();
		BaseResponse response = new BaseResponse();

		Map<String, String> map = getData();

		log.info("Finished execution (deleteToDo) in :{} ms, response :{}.", System.currentTimeMillis() - execStartTime,
				response);

		return response;
	}

	private Map<String, String> getData() {
		log.info("Getting Data from file");
		Map<String, String> map = null;
		try {
			map = Utility.getMapData();
		} catch (IOException e) {
			log.error("Something gone wrong");
			throw new SystemErrorException("Something gone wrong");
		}
		return map;
	}

	private void writeDataToFile(List<ToDoDto> newData) {

		try {
			Utility.generateExcelData(newData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	Map<String, String> map = getData();
//	
//	Map<String, String> mapCredentials = getCredentials();
//
//	for (Map.Entry<String, String> entry : map.entrySet()) {
//		String key = entry.getKey();
//		String value = entry.getValue();
//		log.info(key + " : " + value);
//	}
}
