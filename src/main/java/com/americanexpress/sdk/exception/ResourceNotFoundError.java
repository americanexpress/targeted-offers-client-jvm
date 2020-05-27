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
 * The ResourceNotFoundError class raises an error when the user is not qualified for a
 * PreQual or PreScreened Offers
 */
public class ResourceNotFoundError extends OffersException {

	private static final long serialVersionUID = -2302297868673501071L;

	private static final String USER_MESSAGE = "Resource not found";

	public ResourceNotFoundError(String developerMessage, Throwable cause) {
		super(USER_MESSAGE, developerMessage, cause);
	}

	public ResourceNotFoundError(String developerMessage) {
		super(developerMessage);
	}

	public ResourceNotFoundError() {
		super(USER_MESSAGE);
	}
}
