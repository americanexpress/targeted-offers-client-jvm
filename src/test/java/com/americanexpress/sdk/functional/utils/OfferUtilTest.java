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
package com.americanexpress.sdk.functional.utils;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JWEHeader;
import org.apache.http.HttpEntity;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpEntity.class, ObjectMapper.class, ApiClientFactory.class, JWEHeader.class})
public class OfferUtilTest {


	@Test
	public void testBuildRequestEntity() throws UnsupportedEncodingException, JsonProcessingException {
		TargetedOffersRequest targetedOffersRequest = new TargetedOffersRequest();
		HttpEntity requestEntity = OfferUtil.buildRequestEntity(targetedOffersRequest);
		assertNotNull(requestEntity);
	}

	@Test
	public void testGetJson() throws IOException {
		assertNotNull(OfferUtil.getJson(buildDeserialzed()));
	}

	@Test
	public void testBuildHeaders() {
		Config config = org.easymock.EasyMock.createMock(Config.class);
		org.easymock.EasyMock.expect(config.getApiKey()).andReturn("api_key");
		org.easymock.EasyMock.expect(config.getAccessToken()).andReturn("access_token");
		EasyMock.replay(config);
		assertNotNull(OfferUtil.buildHeaders(buildRequestHeader(), config));
	}

	private <T> String buildDeserialzed() throws IOException {
		T deserialized = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper.writeValueAsString(deserialized);
	}

	private RequestHeader buildRequestHeader() {
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setRequestId("request id");
		requestHeader.setClientId("client id");
		requestHeader.setUserConsentStatus(true);
		requestHeader.setUserConsentTimestamp("user consent timestamp");
		requestHeader.setSessionToken("session token");
		requestHeader.setCountryCode("country code");
		requestHeader.setMessageTypeId("message type id");
		return requestHeader;
	}
}
