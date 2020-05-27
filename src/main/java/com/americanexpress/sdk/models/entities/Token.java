/*
 * Copyright 2020 American Express Travel Related Services Company, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.americanexpress.sdk.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * This model represents the Token information model
 */
@ApiModel(description = "Token model")
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Token {
	
	/**
	 * scope
	 **/
	@ApiModelProperty(value = "")
	private String scope;
	
	/**
	 * status of the token
	 **/
	private String status;
	
	/**
	 * apiProduct List, List of products approved for the Token
	 **/
	private String apiProductList;
	
	/**
	 * expiresIn
	 **/
	private String expiresIn;
	
	/**
	 * Token Type
	 **/
	private String tokenType;
	
	/**
	 * Access Token
	 **/
	private String accessToken;
	
}
