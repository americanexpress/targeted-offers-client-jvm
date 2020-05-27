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
package com.americanexpress.sdk.client.core.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.americanexpress.sdk.service.constants.OffersApiConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * The OfferUtil class handles the Offers Service API call specific
 * utility methods
 * 
 * @author jramio
 */
public class OfferUtil {

	private OfferUtil() {
		throw new IllegalStateException("Offer Utility Class");
	}

	/**
	 * This method is responsible to build request entity for HttpClient
	 * 
	 * @param request
	 * @return HttpEntity
	 * @throws UnsupportedEncodingException
	 * @throws JsonProcessingException
	 */
	public static HttpEntity buildRequestEntity(Object request)
			throws UnsupportedEncodingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return new StringEntity(mapper.writeValueAsString(request));
	}

	/**
	 * This method retrieves the Json to deserialized string
	 * 
	 * @param deserialized
	 * @return <T> String
	 * @throws IOException
	 */
	public static <T> String getJson(T deserialized) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper.writeValueAsString(deserialized);
	}

	/**
	 * This method is the builder for Targeted offers request Headers
	 * 
	 * @param requestHeader
	 * @param config
	 * @return MultivaluedMap<String, Object>
	 */
	public static MultivaluedMap<String, Object> buildHeaders(RequestHeader requestHeader, Config config) {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add(OffersApiConstants.REQUEST_HEADER_USER_CONSENT_STATUS, requestHeader.isUserConsentStatus());
		headers.add(OffersApiConstants.REQUEST_HEADER_USER_CONSENT_TIMESTAMP, requestHeader.getUserConsentTimestamp());
		headers.add(OffersApiConstants.REQUEST_HEADER_MESSAGE_TYPE_ID, requestHeader.getMessageTypeId());
		headers.add(OffersApiConstants.REQUEST_HEADER_REQUEST_ID, requestHeader.getRequestId());
		headers.add(OffersApiConstants.REQUEST_HEADER_CLIENT_ID, requestHeader.getClientId());
		headers.add(OffersApiConstants.REQUEST_HEADER_CONTENT_TYPE, "application/json");
		headers.add(OffersApiConstants.MAC_ID, config.getApiKey());
		headers.add(OffersApiConstants.AUTHORIZATION, OffersApiConstants.BEARER + " " + config.getAccessToken());
		return headers;
	}

	/**
	 * This method gets the response data string
	 * 
	 * @param entity
	 * @return String
	 */
	public static String getResponseString(HttpEntity entity) {
		if (entity != null) {
			try {
				return EntityUtils.toString(entity);
			} catch (Exception ex) {
				return null;
			}
		}
		return null;
	}
}
