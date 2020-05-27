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
package com.americanexpress.sdk.functional.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.configuration.JWEConfig;
import com.americanexpress.sdk.configuration.ProxyConfig;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.TargetedOffersClient;
import com.fasterxml.jackson.core.type.TypeReference;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TargetedOffersClient.class, OffersService.class, HttpClient.class, ApiClientFactory.class,
		Config.class, JWEConfig.class, ProxyConfig.class, CloseableHttpClient.class, OfferUtil.class,
		HttpRequest.class })
public class HttpClientTest {

	public Config config;
	public HttpClient httpClient;
	public CloseableHttpClient client;

	@Before
	public void setUp() throws Exception {
		config = EasyMock.createNiceMock(Config.class);
		client = EasyMock.createNiceMock(CloseableHttpClient.class);
		httpClient = EasyMock.createNiceMock(HttpClient.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPostClientResource_success() throws Exception {
		Object response = PowerMock.createMock(Object.class);
		PowerMock.mockStatic(ApiClientFactory.class);
		EasyMock.expect(ApiClientFactory.createHttpClient(EasyMock.anyObject())).andReturn(httpClient);
		PowerMock.replay(ApiClientFactory.class);

		EasyMock.expect(httpClient.postClientResource(EasyMock.isA(HttpEntity.class), EasyMock.isA(String.class),
				EasyMock.anyObject(), (TypeReference<Object>) EasyMock.isA(Object.class), EasyMock.anyObject()))
				.andReturn(response);
		EasyMock.replay(httpClient);
		assertNotNull(response);
	}
	
	@Test
	public void testpostClientResource_failure() throws Exception {
		HttpEntity entity = new StringEntity("request");
		String apiUrl = "url";
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		CloseableHttpResponse httpResponse = EasyMock.createNiceMock(CloseableHttpResponse.class);
		EasyMock.expect(client.execute(EasyMock.isA(HttpPost.class))).andReturn(httpResponse);

		Object response = httpClient.postClientResource(entity, apiUrl, headers, new TypeReference<StatusLine>() {
		}, null);
		assertNull(response);
	}

}
