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
package com.americanexpress.sdk.client.http;

import static com.americanexpress.sdk.service.constants.OffersExceptionConstants.INTERNAL_API_EXCEPTION;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import com.americanexpress.sdk.client.core.utils.OfferUtil;
import com.americanexpress.sdk.exception.OffersApiError;
import com.americanexpress.sdk.exception.OffersAuthenticationError;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.exception.OffersRequestValidationError;
import com.americanexpress.sdk.exception.ResourceNotFoundError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * The HttpClient class implementation handles all the HTTP operations for PE
 * API clients
 * 
 * @author jramio
 */
public class HttpClient {

	/**
	 * Client interface to connect to external service
	 */
	CloseableHttpClient client;
	private ObjectMapper mapperForGetResponse = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	/**
	 * @param client
	 */
	public HttpClient(CloseableHttpClient client) {
		this.client = client;
	}

	/**
	 * This method is used to connect to external service API to post resource to
	 * the client
	 *
	 * @param apiRequest
	 * @param apiUrl
	 * @param headers
	 * @param responseObject
	 * @param responseHeaders
	 * @return <R, T> R
	 * @throws Exception
	 */
	public <R, T> R postClientResource(T apiRequest, String apiUrl, MultivaluedMap<String, Object> headers,
			TypeReference<R> responseObject, Map<String, String> responseHeaders) throws OffersException {
		R response = null;
		HttpPost request = new HttpPost(apiUrl);
		HttpEntity entity = (HttpEntity) apiRequest;
		request.setEntity(entity);
		addHeaders(request, headers);
		try (CloseableHttpResponse httpResponse = client.execute(request)) {
			if (null != httpResponse.getEntity() && (httpResponse.getStatusLine().getStatusCode() == Response.Status.OK
					.getStatusCode()
					|| httpResponse.getStatusLine().getStatusCode() == Response.Status.CREATED.getStatusCode())) {
				if (null != responseObject) {
					response = generateResponse(responseObject, httpResponse);
				}
				if (null != responseHeaders) {
					extractResponseHeaders(httpResponse, responseHeaders);
				}
				return response;
			} else {
				throw handleHttpStatusCodes(httpResponse);
			}
		} catch (OffersException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new OffersApiError(INTERNAL_API_EXCEPTION, ex);
		}
	}

	/**
	 * This method is to convert response based on content-type
	 *
	 * @param responseObject
	 * @param httpResponse
	 * @return <R> R
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 */
	private <R> R generateResponse(TypeReference<R> responseObject, CloseableHttpResponse httpResponse)
			throws IOException {
		R response = null;
		final InputStream content = httpResponse.getEntity().getContent();
		mapperForGetResponse.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		response = mapperForGetResponse.readValue(content, responseObject);
		return response;
	}

	/**
	 * This method is to extract response based from the HTTP response
	 *
	 * @param httpResponse
	 * @param responseHeaders
	 */
	private void extractResponseHeaders(CloseableHttpResponse httpResponse, Map<String, String> responseHeaders) {
		Header[] headers = httpResponse.getAllHeaders();
		for (Header header : headers) {
			responseHeaders.put(header.getName(), header.getValue());
		}
	}

	/**
	 * This method adds required headers to the request
	 *
	 * @param request
	 * @param headers
	 */
	private void addHeaders(HttpRequest request, MultivaluedMap<String, Object> headers) {
		for (String str : headers.keySet()) {
			request.addHeader(str, String.valueOf(headers.getFirst(str)));
		}
	}

	/**
	 * This method provides proper exception handling
	 *
	 * @param httpResponse
	 */
	private OffersException handleHttpStatusCodes(CloseableHttpResponse httpResponse) {
		String developerMessage = OfferUtil.getResponseString(httpResponse.getEntity());
		Response.Status status = Response.Status.fromStatusCode(httpResponse.getStatusLine().getStatusCode());
		switch (status) {
		case NOT_FOUND:
			return new ResourceNotFoundError();
		case BAD_REQUEST:
			return new OffersRequestValidationError(developerMessage);
		case UNAUTHORIZED:
			return new OffersAuthenticationError(developerMessage);
		default:
			return new OffersApiError(developerMessage);
		}
	}
}
