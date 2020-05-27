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

	/**
	 * Sample for testing Targeted offers
	 *
	 * @return OffersResponse
	 * @throws OffersException
	 */
	@Test
	public void getTargetedOffersSample() {
		OffersResponse offersResponse = null;
		try {
			TargetedOffersClient targetedOffersClient = createTargetedOffersClient();
			/**
			 * if a valid access Token is already available in the cache, please build the
			 * configuration with the available token. getting Authentication Token call is
			 * not needed.
			 */
			AccessTokenResponse accessTokenResponse = getAuthenticationToken(targetedOffersClient);

			if (null != accessTokenResponse && StringUtils.isNotEmpty(accessTokenResponse.getAccessToken())) {
				targetedOffersClient.setAccessToken(accessTokenResponse.getAccessToken());

				System.out.println("AccessToken: " + accessTokenResponse.getAccessToken());

				OffersService offersService = targetedOffersClient.getOffersService();

				ObjectMapper objectMapper = new ObjectMapper()
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

				RequestHeader requestHeader = objectMapper.readValue(Thread.currentThread()
								.getContextClassLoader().getResourceAsStream("targetedOffersRequestHeader.json"),
						RequestHeader.class);

				com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest targetedOffersRequest = objectMapper.readValue(Thread.currentThread()
						.getContextClassLoader().getResourceAsStream("targetedOffersRequest.json"),
						TargetedOffersRequest.class);
				offersResponse = offersService.getOffers(targetedOffersRequest, requestHeader);

				System.out.println("Offer: " + offersResponse.toString());

				/**
				 * Acknowledging offers
				 */
				requestHeader = objectMapper.readValue(Thread.currentThread()
								.getContextClassLoader().getResourceAsStream("acknowledgementRequestHeader.json"),
						RequestHeader.class);
				AcknowledgementRequest acknowledgementRequest = new AcknowledgementRequest();
				acknowledgementRequest
						.setApplicantRequestTrackingId(offersResponse.getApplicantRequestTrackingId());
				Boolean isOfferAcknowledged = offersService.acknowledgeOffers(acknowledgementRequest, requestHeader);
				assertEquals(true, isOfferAcknowledged);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sample for testing Session Tokens
	 *
	 * @throws OffersException
	 */
	@Test
	public void getSessionTokenSample() {
		TokenResponse tokenResponse = null;
		try {
			TargetedOffersClient targetedOffersClient = createTargetedOffersClient();

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

				ObjectMapper objectMapper = new ObjectMapper()
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
	 * Initializes the HTTP Client instance to get AAM details bdaas API
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
				.build(new Config.Builder(sampleConfig.get(OAUTH_OFFERS_API_ENDPOINT), sampleConfig.get(OAUTH_API_KEY),
						sampleConfig.get(OAUTH_API_SECRET), null, socketFactory)
								.setJweConfig(new JWEConfig(false))
								.setProxyConfig(new ProxyConfig(Boolean.parseBoolean(sampleConfig.get(PROXY_ENABLED)),
										sampleConfig.get(PROXY_PROTOCOL), sampleConfig.get(PROXY_HOST),
										Integer.valueOf(sampleConfig.get(PROXY_PORT))))
								.build());

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
