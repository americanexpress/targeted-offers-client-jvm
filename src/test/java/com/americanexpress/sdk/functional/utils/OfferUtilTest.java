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

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpEntity.class, ObjectMapper.class })
public class OfferUtilTest {

	public HttpEntity httpEntity;
	public ObjectMapper mapper;
	public HttpClient httpClient;
	public CloseableHttpClient client;
	public Object request;

	@Mock
	private PropertyNamingStrategy propertyNamingStrategy;

	@Mock
	private RequestHeader requestHeader;

	@Mock
	private Config config;

	@Mock
	private OfferUtil offerUtil;

	@Before
	public void setUp() throws Exception {
		client = EasyMock.createNiceMock(CloseableHttpClient.class);
		httpClient = new HttpClient(client);

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testbuildRequestEntity_success() throws Exception {
		StatusLine statusline = EasyMock.createNiceMock(StatusLine.class);

		CloseableHttpResponse response = EasyMock.createNiceMock(CloseableHttpResponse.class);
		EasyMock.expect(client.execute(EasyMock.isA(HttpPost.class))).andReturn(response);
		EasyMock.expect(response.getStatusLine()).andReturn(statusline);
		PowerMock.replay(statusline);

		mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		HttpEntity entity = new StringEntity(mapper.writeValueAsString(request));

		assertNotNull(entity);
		OfferUtil.buildRequestEntity(request);
	}

	@Test(expected = Exception.class)
	public void testbuildRequestEntity_failure() throws Exception {
		HttpEntity entity = new StringEntity("request");
		String apiUrl = "url";
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();

		CloseableHttpResponse response = EasyMock.createNiceMock(CloseableHttpResponse.class);
		EasyMock.expect(client.execute(EasyMock.isA(HttpPost.class))).andReturn(response);

		httpClient.postClientResource(entity, apiUrl, headers, new TypeReference<StatusLine>() {
		}, null);
	}

	@Test
	public void testGetJson() throws IOException {
		assertNotNull(OfferUtil.getJson(buildDeserialzed()));
	}

	@Test
	public void testBuildHeaders() {
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
		requestHeader.setUserConsentStatus(true);
		requestHeader.setUserConsentTimestamp("2020-02-10 09:17:04.101 MST");
		requestHeader.setMessageTypeId("1201");
		requestHeader.setRequestId("test");
		requestHeader.setClientId("E0222A9A432B24D9");
		return requestHeader;
	}
}
