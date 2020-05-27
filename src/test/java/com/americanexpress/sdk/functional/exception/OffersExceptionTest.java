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
package com.americanexpress.sdk.functional.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.americanexpress.sdk.models.targeted_offers.ErrorMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.americanexpress.sdk.exception.OffersException;

public class OffersExceptionTest {

	private OffersException offersException;

	@Mock
	Throwable cause;

	@Mock
	ErrorMessage error;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testChorusException() {
		offersException = buildOffersException();
		assertEquals(OffersException.class, offersException.getClass());
	}

	@Test
	public void testStatusCodes() {
		offersException = buildOffersException();
		offersException.setUserMessage("message");
		assertEquals("message", offersException.getUserMessage());
	}

	@Test
	public void testExceptionMessages() {
		offersException = buildOffersException();
		offersException.setUserMessage("userMessage");
		offersException.setCause();
		offersException.setError();
		assertEquals("userMessage", offersException.getUserMessage());
		assertNotNull(offersException.getCause());
		assertNotNull(offersException.getError());
	}

	private OffersException buildOffersException() {
		offersException = new OffersException("message");
		offersException = new OffersException("message", cause);
		offersException = new OffersException("message", cause, error);

		return offersException;
	}

}
