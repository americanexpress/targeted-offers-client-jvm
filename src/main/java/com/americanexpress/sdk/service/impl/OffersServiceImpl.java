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
package com.americanexpress.sdk.service.impl;

import static com.americanexpress.sdk.service.constants.OffersApiConstants.*;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.ERROR_PARSING_OFFER_DATA;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.INTERNAL_SDK_EXCEPTION;
import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.MANDATORY_REQUEST_PARAMETER_ERROR;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.core.MultivaluedMap;

import com.americanexpress.sdk.models.entities.RequestHeader;
import com.americanexpress.sdk.models.targeted_offers.AcknowledgementRequest;
import com.americanexpress.sdk.models.targeted_offers.OffersResponse;
import com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.exception.NoOfferFoundError;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.exception.OffersRequestValidationError;
import com.americanexpress.sdk.exception.PayloadEncryptionError;
import com.americanexpress.sdk.exception.ResourceNotFoundError;
import com.americanexpress.sdk.service.OffersService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSAEncrypter;

/**
 *
 * The Offers Service Implementation class handles all the API operations
 * required for Targeted Offers feature
 *
 */
public class OffersServiceImpl implements OffersService {

	private final HttpClient authClient;

	private final Config config;

	public OffersServiceImpl(final Config config, final HttpClient authClient) {
		this.config = config;
		this.authClient = authClient;
	}

	/**
	 * Get AccessToken and the list of API Products approved for the token
	 *
	 * @return {@link OffersResponse}
	 * @throws OffersException
	 */
	public OffersResponse getOffers(TargetedOffersRequest targetedOffersRequest, RequestHeader requestHeader) throws OffersException {

		MultivaluedMap<String, Object> headers = OfferUtil.buildHeaders(requestHeader,
				config);
		if (null == headers.get(AUTHORIZATION)) {
			throw new OffersRequestValidationError(MANDATORY_REQUEST_PARAMETER_ERROR);
		}
		OffersResponse offersResponse = null;
		try {
			TargetedOffersRequest request = new TargetedOffersRequest();
			if (null != config.getJweConfig() && null != config.getJweConfig().getPublicKey()) {
				String encryptedPayload = encrypt(targetedOffersRequest, config.getJweConfig().getPublicKey());
				request.getEncryptedRequest().setUserInfo(encryptedPayload);
			} else {
				request = targetedOffersRequest;
			}
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			HttpEntity requestEntity = new StringEntity(mapper.writeValueAsString(request));
			Map<String, String> responseHeaders = new HashMap<>();
			offersResponse = authClient.postClientResource(requestEntity,
					config.getUrl().concat(OFFERS_PULL_RESOURCE_PATH), headers,
					new TypeReference<OffersResponse>() {
					}, responseHeaders);
		} catch (ResourceNotFoundError ex) {
			throw new NoOfferFoundError();
		} catch (OffersException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new OffersException(INTERNAL_SDK_EXCEPTION, ex);
		}
		return offersResponse;
	}

	/**
	 * Acknowledging offers were received.
	 *
	 * @param acknowledgementRequest
	 * @return Boolean
	 */
	public Boolean acknowledgeOffers(AcknowledgementRequest acknowledgementRequest, RequestHeader requestHeader) {
		MultivaluedMap<String, Object> headers = OfferUtil.buildHeaders(requestHeader, config);
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false).setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			HttpEntity requestEntity = new StringEntity(mapper.writeValueAsString(acknowledgementRequest));
			authClient.postClientResource(requestEntity, config.getUrl().concat(OFFERS_ACKNOWLEDGE_RESOURCE_PATH),
					headers, null, null);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * JWE encryption
	 *
	 * @param targetedOffersRequest
	 * @param publicKey
	 * @return String
	 * @throws OffersException
	 */

	public static String encrypt(TargetedOffersRequest targetedOffersRequest, RSAPublicKey publicKey)
			throws OffersException {
		String response = null;
		JWEHeader jweHeader = null;
		try {
			jweHeader = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM);
			JWEObject jweObject = new JWEObject(jweHeader, new Payload(OfferUtil.getJson(targetedOffersRequest)));

			/** Encrypt with public key */
			JWEEncrypter encrypter = new RSAEncrypter(publicKey);
			jweObject.encrypt(encrypter);

			/** Output JWE string */
			response = jweObject.serialize();
		} catch (JOSEException | IOException joseEx) {
			throw new PayloadEncryptionError(joseEx.getMessage(), joseEx);
		}
		return response;
	}

	/**
	 * validate offer expire and offer status
	 *
	 * @param offerExpireDate
	 * @param offerStatus
	 * @return boolean
	 * @throws OffersRequestValidationError
	 */
	public static boolean validateOffer(final Date offerExpireDate, final String offerStatus)
			throws OffersRequestValidationError {
		boolean isOfferValid = true;
		java.util.Date offerExpiryDate = null;
		Calendar calendar = null;
		if (!ACTIVE_STATUS.equalsIgnoreCase(offerStatus) && !ARCHIVED_STATUS.equalsIgnoreCase(offerStatus)) {
			return false;
		}
		DateFormat dateFormat = new SimpleDateFormat(OFFER_EXPIRY_DATE_FORMAT);
		try {
			if (null != offerExpireDate) {
				offerExpiryDate = dateFormat.parse(offerExpireDate.toString());
				calendar = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE));
				Integer calendarMonth = calendar.get(Calendar.MONTH) + 1;
				String currentDate = calendar.get(Calendar.YEAR) + "-" + calendarMonth + "-"
						+ calendar.get(Calendar.DATE);
				java.util.Date currentFormatDate = dateFormat.parse(currentDate);
				if (currentFormatDate.compareTo(offerExpiryDate) > 0) {
					isOfferValid = false;
				}
			}
		} catch (ParseException exception) {
			throw new OffersRequestValidationError(ERROR_PARSING_OFFER_DATA);
		}
		return isOfferValid;
	}

}
