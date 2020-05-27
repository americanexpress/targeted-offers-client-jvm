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

import com.americanexpress.sdk.models.targeted_offers.OffersResponse;
import com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.configuration.JWEConfig;
import com.americanexpress.sdk.configuration.ProxyConfig;
import com.americanexpress.sdk.service.AuthenticationService;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.TargetedOffersClient;
import com.americanexpress.sdk.service.TokenService;
import com.americanexpress.sdk.service.impl.TargetedOffersClientImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ TargetedOffersRequest.class, OffersResponse.class,
		Config.class, HttpClient.class, TargetedOffersClientImpl.class, TargetedOffersClient.class, ApiClientFactory.class, JWEConfig.class, ProxyConfig.class, CloseableHttpClient.class })

public class TargetedOffersClientImplTest {

	@Mock
	private AuthenticationService authenticationService;
	
	@Mock
	private TokenService tokenService;
	
	@Mock
	private OffersService offersService;

	@Mock
	private HttpClient httpClient;

	@Mock
	private Config config;
	
	@Mock
	public TargetedOffersClientImpl targetedOffersClientImpl;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetAuthenticationService() {
		authenticationService = AuthenticationService.Builder.create(config, httpClient);
		assertNotNull(authenticationService);
	}
	
	@Test
	public void testGetTokenService() {
		tokenService = TokenService.Builder.create(config, httpClient);
		assertNotNull(tokenService);
	}
	
	@Test
	public void testGetOffersService() {
		offersService = OffersService.Builder.create(config, httpClient);
		assertNotNull(offersService);

	}
	
}
