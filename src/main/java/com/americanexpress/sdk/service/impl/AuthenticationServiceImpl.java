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

import static com.americanexpress.sdk.service.constants.OffersApiConstants.*;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.INTERNAL_SDK_EXCEPTION;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.OFFERS_REQUEST_VALIDATION_ERROR;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import org.apache.commons.lang3.StringUtils;

import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.exception.OffersRequestValidationError;
import com.americanexpress.sdk.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.models.entities.Token;
import com.americanexpress.sdk.service.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * 
 * The Authentication Service Implementation class is used to handle OAuth Token
 * Service actions; authenticate and retrieve the access token
 *
 */
public class AuthenticationServiceImpl implements AuthenticationService {

	private final HttpClient authClient;

	private final Config config;

	public AuthenticationServiceImpl(final Config config, final HttpClient authClient) {
		this.config = config;
		this.authClient = authClient;

	}

	/**
	 * Get AccessToken and the list of API Products approved for the token
	 * 
	 * @return {@link Token}
	 * @throws OffersException
	 */
	public AccessTokenResponse getAccessToken() throws OffersException {

		if (StringUtils.isEmpty(config.getApiKey()) && StringUtils.isEmpty(config.getApiSecret())) {
			throw new OffersRequestValidationError(OFFERS_REQUEST_VALIDATION_ERROR);
		}
		String encodedBaseString = "Basic ".concat(Base64.getEncoder().encodeToString(
				(config.getApiKey().concat(":").concat(config.getApiSecret())).getBytes(StandardCharsets.UTF_8)));

		AccessTokenResponse accessTokenResponse = null;
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add(AUTHORIZATION, encodedBaseString);
		headers.add(REQUEST_HEADER_CONTENT_TYPE, CONTENT_TYPE_VALUE);
		headers.add(MAC_ID, config.getApiKey());
		try {
			List<NameValuePair> form = new ArrayList<>();
			form.add(new BasicNameValuePair(AUTH_TOKEN_GRANT_TYPE, AUTH_TOKEN_GRANT_TYPE_VALUE));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);
			Map<String, String> responseHeaders = new HashMap<>();
			accessTokenResponse = authClient.postClientResource(entity, config.getUrl().concat(AUTH_TOKEN_API_PATH),
					headers, new TypeReference<AccessTokenResponse>() {
					}, responseHeaders);
		} catch (OffersException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new OffersException(INTERNAL_SDK_EXCEPTION, ex);
		}
		return accessTokenResponse;
	}
}
