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

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.easymock.classextension.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.models.entities.AccessTokenResponse;
import com.americanexpress.sdk.service.AuthenticationService;
import com.americanexpress.sdk.service.TargetedOffersClient;
import com.fasterxml.jackson.core.type.TypeReference;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AccessTokenResponse.class, AuthenticationService.class, TargetedOffersClient.class,
		ApiClientFactory.class })

public class AuthenticationServiceImplTest {

	public AuthenticationService authenticationService;
	public TargetedOffersClient targetedOffersClient;
	public HttpClient authClient;
	public AccessTokenResponse accessTokenResponse;

	@Before
	public void setUp() throws Exception {
		authenticationService = EasyMock.createNiceMock(AuthenticationService.class);
		targetedOffersClient = EasyMock.createNiceMock(TargetedOffersClient.class);
		authClient = EasyMock.createNiceMock(HttpClient.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetAccessToken() throws OffersException, IOException {
		accessTokenResponse = PowerMock.createMock(AccessTokenResponse.class);
		PowerMock.mockStatic(ApiClientFactory.class);
		EasyMock.expect(ApiClientFactory.createHttpClient(EasyMock.anyObject())).andReturn(authClient);
		PowerMock.replay(ApiClientFactory.class);

		EasyMock.expect(targetedOffersClient.getAuthenticationService()).andReturn(authenticationService);
		EasyMock.replay(targetedOffersClient);

		EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class), EasyMock.isA(String.class),
				EasyMock.anyObject(), (TypeReference<Object>) EasyMock.isA(Object.class), EasyMock.anyObject()))
				.andReturn(accessTokenResponse);
		EasyMock.replay(authClient);

		assertNotNull(accessTokenResponse);
	}
}
