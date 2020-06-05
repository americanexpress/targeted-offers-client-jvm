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
package com.americanexpress.sdk.service.constants;

/**
 * This class holds the constants required for Offers API calls
 *
 * @author jramio
 */
public class OffersApiConstants {
	/**
	 * APIGEE related String
	 */
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	public static final String MAC_ID = "X-AMEX-API-KEY";

	/**
	 * Retrieve Auth Token API
	 */
	public static final String AUTH_TOKEN_GRANT_TYPE = "grant_type";
	public static final String AUTH_TOKEN_GRANT_TYPE_VALUE = "client_credentials";
	public static final String AUTH_TOKEN_API_PATH = "/apiplatform/v1/oauth/token_provisioning/bearer_tokens";
	public static final String CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded";

	/**
	 * API constants
	 */
	public static final String API_CLIENT_TYPE_HTTP = "http";

	/**
	 * Content Service related Constants
	 */
	public static final String ACTIVE_STATUS = "Active";
	public static final String ARCHIVED_STATUS = "Archived";
	public static final String OFFER_EXPIRY_DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_ZONE = "America/Phoenix";
	public static final String PATH_PARAM_SESSION_TOKEN = "session_token";


	/**
	 * Request,Response headers, and Header fields
	 */
	public static final String REQUEST_HEADER_CLIENT_ID = "client_id";
	public static final String REQUEST_HEADER_REQUEST_ID = "request_id";
	public static final String REQUEST_HEADER_USER_CONSENT_STATUS = "user_consent_status";
	public static final String REQUEST_HEADER_USER_CONSENT_TIMESTAMP = "user_consent_timestamp";
	public static final String REQUEST_HEADER_SESSION_TOKEN = "session_token";
	public static final String REQUEST_HEADER_COUNTRY_CODE = "country_code";
	public static final String REQUEST_HEADER_MESSAGE_TYPE_ID = "message_type_id";
	public static final String REQUEST_HEADER_CONTENT_TYPE = "content-type";
	public static final String OFFERS_PULL_RESOURCE_PATH = "/acquisition/digital/v1/offers/cards/targeted_offers";
	public static final String OFFERS_ACKNOWLEDGE_RESOURCE_PATH = "/acquisition/digital/v1/offers/cards/targeted_offers_acknowledgment";
	public static final String TOKEN_SERVICE_RESOURCE_PATH = "/acquisition/digital/v1/token_mgmt/tokens";

	/**
	 * Targeted Offers Sample Configuration keys
	 */
	public static final String DEVELOPER_PORTAL_SDK = "developer.portal.sdk";
	public static final String KEYSTORE_JKS = "keystore.jks";
	public static final String OAUTH_KEYSTORE_TRUST_STREAM = "oauth.keystore.trust.stream";
	public static final String OAUTH_KEYSTORE_LOAD_TRUST_STREAM = "oauth.keystore.load.trust.stream";
	public static final String OAUTH_KEYSTORE_PASSPHRASE_PROPERTY = "oauth.keystore.passphrase.property";
	public static final String OAUTH_KEYSTORE_ALIAS_PROPERTY = "oauth.keystore.alias.property";
	public static final String OAUTH_OFFERS_API_ENDPOINT = "oauth.offers.api.endpoint";
	public static final String OAUTH_API_KEY = "oauth.api.key";
	public static final String OAUTH_API_SECRET = "oauth.api.secret";
	public static final String PROXY_PROTOCOL = "proxy.protocol";
	public static final String PROXY_HOST = "proxy.host";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_ENABLED = "proxy.enabled";
}