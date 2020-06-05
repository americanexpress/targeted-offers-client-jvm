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

import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.models.entities.*;
import org.apache.http.HttpEntity;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.service.TokenService;
import com.americanexpress.sdk.service.impl.TokenServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.ws.rs.core.MultivaluedMap;

public class TokenServiceImplTest {

	private HttpClient authClient;
	private TokenService tokenService;
	private TokenRequest tokenRequest;
	private TokenResponse tokenResponse;

	@Before
	public void setUp() {
		Config config = Config.builder().accessToken("accessToken")
				.url("https://example.americanexpress.com").build();
		authClient = EasyMock.createNiceMock(HttpClient.class);
		tokenService = new TokenServiceImpl(config, authClient);
		tokenRequest = new TokenRequest();
		tokenRequest.setRequestHeader(new RequestHeader());
		tokenResponse = new TokenResponse();
	}

	@Test
	public void testGetSessionToken() throws OffersException {
		EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class),
				EasyMock.isA(String.class),
				EasyMock.isA(MultivaluedMap.class),
				(TypeReference<Object>) EasyMock.isA(Object.class),
				EasyMock.isNull()))
				.andReturn(tokenResponse);
		EasyMock.replay(authClient);

		TokenResponse result = tokenService.getSessionToken(tokenRequest);
		assertNotNull(result);
	}


}
