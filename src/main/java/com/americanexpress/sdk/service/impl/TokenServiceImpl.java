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
package com.americanexpress.sdk.service.impl;

import static com.americanexpress.sdk.service.constants.OffersApiConstants.TOKEN_SERVICE_RESOURCE_PATH;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.INTERNAL_SDK_EXCEPTION;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.models.entities.TokenRequest;
import com.americanexpress.sdk.models.entities.TokenResponse;
import com.americanexpress.sdk.service.TokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * The Token Service Implementation class is used to handle and retrieve the session token
 *
 */
public class TokenServiceImpl implements TokenService {

	private final HttpClient authClient;

	private final Config config;

	public TokenServiceImpl(final Config config, final HttpClient authClient) {
		this.config = config;
		this.authClient = authClient;
	}

	/**
	 * Get SessionToken and the list of API Products approved for the token
	 * 
	 * @return {@link TokenResponse}
	 * @throws OffersException
	 */
	public TokenResponse getSessionToken(TokenRequest tokenRequest) throws OffersException {
		MultivaluedMap<String, Object> headers = OfferUtil.buildHeaders(tokenRequest.getRequestHeader(), config);
		TokenResponse tokenResponse = null;
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			HttpEntity requestEntity = new StringEntity(mapper.writeValueAsString(tokenRequest));
			tokenResponse = authClient.postClientResource(requestEntity,
					config.getUrl().concat(TOKEN_SERVICE_RESOURCE_PATH), headers, new TypeReference<TokenResponse>() {
					}, null);
			tokenResponse.setRequestHeader(tokenRequest.getRequestHeader());
		} catch (OffersException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new OffersException(INTERNAL_SDK_EXCEPTION, ex);
		}
		return tokenResponse;
	}
}
