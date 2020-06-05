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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.service.AuthenticationService;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.TokenService;
import com.americanexpress.sdk.service.impl.TargetedOffersClientImpl;


public class TargetedOffersClientImplTest {

	private Config config = Config.builder().build();
	private TargetedOffersClientImpl targetedOffersClient = new TargetedOffersClientImpl(config);

	@Test
	public void testSetAccessToken() {
		targetedOffersClient.setAccessToken("accessToken");
		assertEquals("accessToken", config.getAccessToken());
	}

	@Test
	public void testGetAuthenticationService() {
		AuthenticationService authenticationService = targetedOffersClient.getAuthenticationService();
		assertNotNull(authenticationService);
	}

	@Test
	public void testGetTokenService() {
		TokenService tokenService = targetedOffersClient.getTokenService();
		assertNotNull(tokenService);
	}

	@Test
	public void testGetOffersService() {
		OffersService offersService = targetedOffersClient.getOffersService();
		assertNotNull(offersService);

	}

}
