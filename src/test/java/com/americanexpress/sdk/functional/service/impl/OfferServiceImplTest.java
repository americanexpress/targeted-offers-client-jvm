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

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.americanexpress.sdk.models.targeted_offers.*;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.client.http.ApiClientFactory;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.TokenService;
import com.americanexpress.sdk.service.impl.OffersServiceImpl;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Content.class, TargetedOffersRequest.class, OffersResponse.class, TokenService.class,
		Config.class, HttpClient.class, Offer.class, OfferUtil.class, TargetedOffersRequest.class,
		Url.class, ApiClientFactory.class })

public class OfferServiceImplTest {

	public TokenService tokenService;
	public Config config;
	public HttpClient httpClient;
	public Map<String, String> topics;
	public OffersService offersServiceImpl;
	public AcknowledgementRequest acknowledgementRequest;
	public TargetedOffersRequest targetedOffersRequest;
	public RSAPublicKey publicKey; 
	public Payload payload;
	public JWEHeader jweHeader;
	public JWEObject jweObject;
	public JWEEncrypter encrypter;
	private String applicantRequestTrackingId = "tracking id1";

	@Mock
	private List<Offer> targetedOffers;
	private String applicantRequestToken = "app request token1";
	private String customerFlag = "flag1";
	private String lineOfBusiness = "lob1";
	private String publicGuid = "guid1";
	private String customerReferenceId;
	private String chargeElgible = "true";
	private String lendingFeeElgible = "true";
	private String lendingNofeeElgible = "true";
	private String cobrandElgible = "true";


	Map<String, Boolean> testParams = new HashMap<>();
	final static String MAP_KEY_CONSTANTS_CONTAINS_MESSAGETYPEID = "chorusConstantsContainsTOAHeaderMessageTypeID";
	final static String MAP_KEY_TOAREQUESTPARTNER_IS_NULL = "targetedOfferRequestHeaderPartnerIsNull";

	@Before
	public void setUp() throws Exception {
		tokenService = EasyMock.createNiceMock(TokenService.class);
		httpClient = EasyMock.createNiceMock(HttpClient.class);
		acknowledgementRequest = EasyMock.createNiceMock(AcknowledgementRequest.class);
		config = EasyMock.createNiceMock(Config.class);
		targetedOffersRequest = EasyMock.createNiceMock(TargetedOffersRequest.class);
		publicKey = EasyMock.createNiceMock(RSAPublicKey.class);
		encrypter = EasyMock.createNiceMock(JWEEncrypter.class);
		offersServiceImpl = new OffersServiceImpl(config, httpClient);

		testParams.put(MAP_KEY_CONSTANTS_CONTAINS_MESSAGETYPEID, false);
		testParams.put(MAP_KEY_TOAREQUESTPARTNER_IS_NULL, false);
	}

	@Test
	public void testGetOffers() throws Exception {
		OffersResponse offersResponse = buildTargetedOffersResponse();
		assertNotNull(offersResponse.toString());
		assertEquals(applicantRequestTrackingId, offersResponse.getApplicantRequestTrackingId());
		assertEquals(customerFlag, offersResponse.getCustomerFlag());
	}


	private TargetedOffersRequest buildTargetedOfferRequest() {
		TargetedOffersRequest targetedOffersRequest = new TargetedOffersRequest();
		RequestHeader requestHeader = new RequestHeader();
		requestHeader.setClientId("ClientId");
		if (testParams.get(MAP_KEY_CONSTANTS_CONTAINS_MESSAGETYPEID)) {
			requestHeader.setMessageTypeId("2201");
		} else {
			requestHeader.setMessageTypeId("1001");
		}
		requestHeader.setCountryCode("US");
		requestHeader.setUserConsentStatus(true);
		requestHeader.setUserConsentTimestamp("2020-02-11 13:34:52.787 MST");
//		targetedOffersRequest.setRequestHeader(requestHeader);
//		targetedOffersRequest.getRequestHeader().setRequestId("testRequestID");
		Applicant applicant = buildApplicant();
		Personalization personalization = new Personalization();
		targetedOffersRequest.setPersonalization(personalization);
		List<Applicant> applicants = new ArrayList<Applicant>();
		applicants.add(applicant);
		targetedOffersRequest.setApplicants(applicants);
		Partner partner;
		if (testParams.get(MAP_KEY_TOAREQUESTPARTNER_IS_NULL)) {
			partner = null;
		} else {
			partner = new Partner();
			partner.setPartnerName("tlg");
		}
		targetedOffersRequest.setPartner(partner);
		targetedOffersRequest.setTransactionCost(new Amount());
		targetedOffersRequest.setLineOfBusiness("CONSUMER");
		targetedOffersRequest.getEncryptedRequest().setUserInfo("userInfo");
		return targetedOffersRequest;
	}

	private Applicant buildApplicant() {
		Applicant applicant = new Applicant();
		applicant.setBusinessInfo(new BusinessInfo());
		applicant.setPersonalInfo(new PersonalInfo());
		applicant.setType("basic");
		return applicant;
	}

	private OffersResponse buildTargetedOffersResponse() {
		OffersResponse offersResponse = new OffersResponse();
		offersResponse.setOffers(new ArrayList<Offer>());
		offersResponse.setApplicantRequestTrackingId(applicantRequestTrackingId);
		offersResponse.setApplicantRequestToken(applicantRequestToken);
		offersResponse.setCustomerFlag(customerFlag);
		return offersResponse;
	}

}
