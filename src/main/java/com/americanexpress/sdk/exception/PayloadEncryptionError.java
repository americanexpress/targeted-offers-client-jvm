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
 * The PayloadEncryptionError class throws a generic type of error for
 * Encryption errors, It will be raised when there is an Exception raised at the
 * Payload encryption
 */
public class PayloadEncryptionError extends OffersException {

	private static final long serialVersionUID = 4800474897004209641L;

	public PayloadEncryptionError(String developerMessage) {
		super(developerMessage);
	}

	public PayloadEncryptionError(String developerMessage, Throwable cause) {
		super(developerMessage, cause);
	}
}
