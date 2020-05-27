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
 * This interface holds the Constants of Offers Service Exception descriptions
 * and details
 *
 * @author jramio
 */
public interface OffersExceptionConstants {
	/** Client related String */
	public static final int API_GENERIC_ERROR = 510;

	/** services error constants */
	public static final int INTERNAL_SERVER_API_ERROR = 2510;
	public static final String INTERNAL_SERVER_API_ERROR_TYPE = "TargetedOffer API : API service errors";

	public static final int INVALID_REQUEST_CODE = 2410;
	public static final String INVALID_REQUEST_TYPE = "Errors related to request validation";
	public static final String INVALID_OFFER_ID = "Invalid offer id";
	public static final String INVALID_TOKEN_ID = "Tokens are either expired or not valid";

	public static final String OFFERS_REQUEST_VALIDATION_ERROR = "Request validation failed";
	public static final String ERROR_PARSING_OFFER_DATA = "Error parsing Offer data";
	public static final String MANDATORY_REQUEST_PARAMETER_ERROR = "Mandatory request parameters missing";

	/** security module error constants */
	public static final int HTTP_STATUS_CODE_BAD_REQUEST = 400;
	public static final int HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR = 500;

	public static final String INTERNAL_SERVER_API_SECURITY_ERROR_TYPE = "Offers Service API :  API specific security errors";

	/**
	 * Error Messages
	 */
	public static final String INTERNAL_SDK_EXCEPTION = "Internal SDK Exception";
	public static final String INTERNAL_API_EXCEPTION = "Internal API Exception";

}
