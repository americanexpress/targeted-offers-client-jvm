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

import java.util.Map;

import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.models.targeted_offers.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpEntity;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.impl.OffersServiceImpl;

import javax.ws.rs.core.MultivaluedMap;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class OfferServiceImplTest {

	private HttpClient authClient;
	private OffersService offersService;
	private TargetedOffersRequest targetedOffersRequest;
	private AcknowledgementRequest acknowledgementRequest;
	private RequestHeader requestHeader;
	private OffersResponse offersResponse;

	@Before
	public void setUp() {
		Config config = Config.builder().accessToken("accessToken")
				.url("https://example.americanexpress.com").build();
		authClient = EasyMock.createNiceMock(HttpClient.class);
		offersService = new OffersServiceImpl(config, authClient);
		targetedOffersRequest = new TargetedOffersRequest();
		acknowledgementRequest = new AcknowledgementRequest();
		requestHeader = RequestHeader.builder().build();
		offersResponse = new OffersResponse();
	}

	@Test
	public void testGetOffers() throws OffersException {
		EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class),
				EasyMock.isA(String.class),
				EasyMock.isA(MultivaluedMap.class),
				(TypeReference<Object>) EasyMock.isA(Object.class),
				EasyMock.isA(Map.class)))
				.andReturn(offersResponse);
		EasyMock.replay(authClient);

		OffersResponse result = offersService.getOffers(targetedOffersRequest, requestHeader);
		assertNotNull(result);
	}

	@Test
	public void testAcknowledgeOffers() throws OffersException {
		EasyMock.expect(authClient.postClientResource(EasyMock.isA(HttpEntity.class),
				EasyMock.isA(String.class),
				EasyMock.isA(MultivaluedMap.class),
				(TypeReference<Object>) EasyMock.isA(Object.class),
				EasyMock.isA(Map.class)))
				.andReturn(null);
		EasyMock.replay(authClient);

		Boolean result = offersService.acknowledgeOffers(acknowledgementRequest, requestHeader);
		assertTrue(result);
	}

}
