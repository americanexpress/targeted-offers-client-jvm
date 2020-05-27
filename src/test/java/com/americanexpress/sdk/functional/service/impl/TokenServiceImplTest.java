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
package com.americanexpress.sdk.functional.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.models.entities.Token;
import com.americanexpress.sdk.models.entities.TokenRequest;
import com.americanexpress.sdk.models.entities.TokenResponse;
import com.americanexpress.sdk.service.TokenService;
import com.americanexpress.sdk.service.impl.TokenServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TokenService.class, TokenServiceImpl.class, Token.class, Config.class, CloseableHttpClient.class,
		HttpClient.class, ApiClientFactory.class })
public class TokenServiceImplTest {

	public Token token;
	public CloseableHttpClient client;
	public Config config;
	private HttpClient authClient;
	private TokenServiceImpl tokenServiceImpl;
	private TokenRequest tokenRequest;
	private TokenResponse tokenResponse;
	public AccessTokenResponse accessTokenResponse;

	Properties tokenServiceConfiguration = null;

	@Before
	public void setUp() throws Exception {
		authClient = EasyMock.createNiceMock(HttpClient.class);
		config = EasyMock.createNiceMock(Config.class);
		client = EasyMock.createNiceMock(CloseableHttpClient.class);
		tokenServiceImpl = EasyMock.createNiceMock(TokenServiceImpl.class);
		tokenRequest = EasyMock.createNiceMock(TokenRequest.class);
		tokenResponse = EasyMock.createNiceMock(TokenResponse.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetSessionToken() throws Exception {
		PowerMock.mockStatic(ApiClientFactory.class);
		EasyMock.expect(ApiClientFactory.createHttpClient(EasyMock.anyObject())).andReturn(authClient);
		PowerMock.replay(ApiClientFactory.class);

		EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class), EasyMock.isA(String.class),
				EasyMock.anyObject(), (TypeReference<Object>) EasyMock.isA(Object.class), EasyMock.anyObject()))
				.andReturn(tokenResponse);
		EasyMock.replay(authClient);

		EasyMock.expect(tokenServiceImpl.getSessionToken(tokenRequest)).andReturn(tokenResponse);
		EasyMock.replay(tokenResponse);

		tokenResponse.setRequestHeader(tokenRequest.getRequestHeader());
		assertNotNull(tokenResponse);
	}
}
