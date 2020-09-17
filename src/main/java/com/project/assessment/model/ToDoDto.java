package com.project.assessment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDto {
	
	
	@JsonProperty("id")
	@ApiModelProperty(required = false)
	    private String id;
	
	@JsonProperty("name")
	@ApiModelProperty(required = false)
	    private String name;
	
	

}
