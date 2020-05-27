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
package com.americanexpress.sdk.exception;

/**
 * The OffersAuthentiationError class raises an error when invalid API Key or
 * Secret is sent to the API
 */
public class OffersAuthenticationError extends OffersException {

	private static final long serialVersionUID = 5980189255685342019L;

	private static final String USER_MESSAGE = "Offers Authentication Error";

	public OffersAuthenticationError(String developerMessage, Throwable cause) {
		super(USER_MESSAGE, developerMessage, cause);
	}

	public OffersAuthenticationError(String developerMessage) {
		super(USER_MESSAGE, developerMessage);
	}
}
