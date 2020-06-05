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
package com.americanexpress.sdk.functional.sample;

import com.americanexpress.sdk.models.entities.*;
import com.americanexpress.sdk.models.targeted_offers.AcknowledgementRequest;
import com.americanexpress.sdk.models.targeted_offers.OffersResponse;
import com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.lang3.StringUtils;

import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.configuration.JWEConfig;
import com.americanexpress.sdk.configuration.ProxyConfig;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.service.AuthenticationService;
import com.americanexpress.sdk.service.OffersService;
import com.americanexpress.sdk.service.TargetedOffersClient;
import com.americanexpress.sdk.service.TokenService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.americanexpress.sdk.service.constants.OffersApiConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TargetedOffersSample {

	TargetedOffersClient targetedOffersClient;
	OffersService offersService;
	ObjectMapper objectMapper;

	@Before
	public void setUp() throws Exception {
		targetedOffersClient = createTargetedOffersClient();
		offersService = targetedOffersClient.getOffersService();
		objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	/**
	 * Sample for getting Targeted offers in the Logged In state, with only PII
	 *
	 */
	@Test
	public void getTargetedOffersSample_loggedInPiiOnly () {
		OffersResponse offersResponse = getTargetedOffers(
				"targetedOffersRequestHeader_LoggedInPiiOnly.json",
				"targetedOffersRequest_LoggedInPiiOnly.json");
		assertNotNull(offersResponse);

		boolean isOfferAcknowledged = acknowledgeOffers(offersResponse);
		assertEquals(true, isOfferAcknowledged);
	}

	/**
	 * Sample for getting Targeted offers in the Logged In state, with both PII and Session Data
	 *
	 */
	@Test
	public void getTargetedOffersSample_loggedInPiiAndSessionData () {
		OffersResponse offersResponse = getTargetedOffers(
				"targetedOffersRequestHeader_LoggedInPiiAndSessionData.json",
				"targetedOffersRequest_LoggedInPiiAndSessionData.json");
		assertNotNull(offersResponse);

		boolean isOfferAcknowledged = acknowledgeOffers(offersResponse);
		assertEquals(true, isOfferAcknowledged);
	}

	/**
	 * Sample for getting Targeted offers in the Warm Logged Out state
	 *
	 */
	@Test
	public void getTargetedOffersSample_loggedOutWarm () {
		OffersResponse offersResponse = getTargetedOffers(
				"targetedOffersRequestHeader_LoggedOutWarm.json",
				"targetedOffersRequest_LoggedOutWarm.json");
		assertNotNull(offersResponse);

		boolean isOfferAcknowledged = acknowledgeOffers(offersResponse);
		assertEquals(true, isOfferAcknowledged);
	}

	/**
	 * Sample for getting Targeted offers in the Full Logged Out state
	 */
	@Test
	public void getTargetedOffersSample_loggedOutFull () {
		OffersResponse offersResponse = getTargetedOffers(
				"targetedOffersRequestHeader_LoggedOutFull.json",
				"targetedOffersRequest_LoggedOutFull.json");
		assertNotNull(offersResponse);

		boolean isOfferAcknowledged = acknowledgeOffers(offersResponse);
		assertEquals(true, isOfferAcknowledged);
	}

	/**
	 * Sample for getting Session Tokens
	 *
	 */
	@Test
	public void getSessionTokenSample() {
		TokenResponse tokenResponse = null;
		try {
			/**
			 * if a valid access Token is already available in the cache, please build the
			 * configuration with the available token. getting Authentication Token call is
			 * not needed.
			 */
			AccessTokenResponse accessTokenResponse = getAuthenticationToken(targetedOffersClient);

			if (null != accessTokenResponse && StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
				targetedOffersClient.setAccessToken(accessTokenResponse.getAccessToken());

				accessTokenResponse.getAccessToken();

				TokenService tokenService = targetedOffersClient.getTokenService();

				/**
				 * Session Token call
				 */
				TokenRequest tokenRequest = objectMapper.readValue(
						Thread.currentThread().getContextClassLoader().getResourceAsStream("sessionTokenRequest.json"),
						TokenRequest.class);
				tokenResponse = tokenService.getSessionToken(tokenRequest);
				assertNotNull(objectMapper.writeValueAsString(tokenResponse));

				System.out.println("SessionToken: " + tokenResponse.getAcquisitionWebToken());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper method for getting Targeted offers
	 *
	 * @param headerJson
	 * @param requestJson
	 * @return OffersResponse
	 */
	private OffersResponse getTargetedOffers(String headerJson, String requestJson) {
		OffersResponse offersResponse = null;
		try {
			/**
			 * if a valid access Token is already available in the cache, please build the
			 * configuration with the available token. getting Authentication Token call is
			 * not needed.
			 */
			AccessTokenResponse accessTokenResponse = getAuthenticationToken(targetedOffersClient);

			if (null != accessTokenResponse && StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
				targetedOffersClient.setAccessToken(accessTokenResponse.getAccessToken());

				System.out.println("AccessToken: " + accessTokenResponse.getAccessToken());

				RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
								.getContextClassLoader().getResourceAsStream(headerJson),
						RequestHeader.class);

				TargetedOffersRequest targetedOffersRequest = objectMapper.readValue(Thread.currentThread()
								.getContextClassLoader().getResourceAsStream(requestJson),
						TargetedOffersRequest.class);
				offersResponse = offersService.getOffers(targetedOffersRequest, requestHeader);

				System.out.println("Offer: " + offersResponse.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offersResponse;
	}

	/**
	 * Helper method for acknowledging offers
	 *
	 * @param offersResponse
	 * @return  boolean
	 */
	private boolean acknowledgeOffers(OffersResponse offersResponse) {
		boolean isOfferAcknowledged = false;
		try {
			RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
							.getContextClassLoader().getResourceAsStream("acknowledgementRequestHeader.json"),
					RequestHeader.class);
			AcknowledgementRequest acknowledgementRequest = new AcknowledgementRequest();
			acknowledgementRequest
					.setApplicantRequestTrackingId(offersResponse.getApplicantRequestTrackingId());
			isOfferAcknowledged = offersService.acknowledgeOffers(acknowledgementRequest, requestHeader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOfferAcknowledged;
	}

	/**
	 * Initializes the HTTP Client instance
	 *
	 * @throws IOException
	 */
	private Map<String, String> buildClientConfig() throws IOException {
		Properties targetedOffersSampleConfiguration = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sample.properties");
		targetedOffersSampleConfiguration.load(inputStream);
		return new HashMap(targetedOffersSampleConfiguration);
	}

	/**
	 * Sample Client Builder
	 *
	 * @return TargetedOffersClient
	 * @throws OffersException
	 * @throws IOException
	 */
	private TargetedOffersClient createTargetedOffersClient() throws IOException, CertificateException,
			NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		Map<String, String> sampleConfig = buildClientConfig();
		TargetedOffersClient targetedOffersClient = null;

		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(sampleConfig.get(DEVELOPER_PORTAL_SDK));
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(inputStream, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray());
		KeyStore trustStore = KeyStore.getInstance(sampleConfig.get(KEYSTORE_JKS));
		InputStream trustStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(sampleConfig.get(OAUTH_KEYSTORE_TRUST_STREAM));
		trustStore.load(trustStream, sampleConfig.get(OAUTH_KEYSTORE_LOAD_TRUST_STREAM).toCharArray());

		SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
				new SSLContextBuilder().loadTrustMaterial(trustStore, (chain, authType) -> false)
						.loadKeyMaterial(ks, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray(),
								(aliases, socket) -> sampleConfig.get(OAUTH_KEYSTORE_ALIAS_PROPERTY))
						.build());

		targetedOffersClient = TargetedOffersClient.Builder
				.build(Config.builder().url(sampleConfig.get(OAUTH_OFFERS_API_ENDPOINT))
						.apiKey(sampleConfig.get(OAUTH_API_KEY))
						.apiSecret(sampleConfig.get(OAUTH_API_SECRET))
						.accessToken(null)
						.socketFactory(socketFactory)
						.jweConfig(new JWEConfig(false))
						.proxyConfig(new ProxyConfig(Boolean.parseBoolean(sampleConfig.get(PROXY_ENABLED)),
								sampleConfig.get(PROXY_PROTOCOL), sampleConfig.get(PROXY_HOST),
								Integer.valueOf(sampleConfig.get(PROXY_PORT)))).build());

		return targetedOffersClient;
	}

	/**
	 * This method will get the access Token from the SDK.
	 *
	 * @param targetedOffersClient
	 * @return AccessTokenResponse
	 * @throws OffersException
	 */
	private AccessTokenResponse getAuthenticationToken(TargetedOffersClient targetedOffersClient)
			throws OffersException {
		AccessTokenResponse accessTokenResponse = null;
		AuthenticationService authenticationService = targetedOffersClient.getAuthenticationService();
		accessTokenResponse = authenticationService.getAccessToken();
		return accessTokenResponse;
	}
}
