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
package com.americanexpress.sdk.service;

import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.service.impl.TargetedOffersClientImpl;

/**
 * The Targeted Offers Client Interface handles all the API operations required
 * for creating the Targeted Offers API Client
 * 
 * @author jramio
 */
public interface TargetedOffersClient {

	/**
	 * Creates a new Instance of Authentication Service to help get the Access Token
	 * required to make API calls
	 * 
	 * @return Instance of {@link AuthenticationService}
	 */
	public AuthenticationService getAuthenticationService();

	/**
	 * Creates a new Instance of Offers Service to help get the Offers required to
	 * make API calls
	 * 
	 * @return Instance of {@link OffersService}
	 */
	public OffersService getOffersService();

	/**
	 * Creates a new Instance of Token Service to help get the session Token
	 * required to make API calls
	 * 
	 * @return Instance of {@link TokenService}
	 */
	public TokenService getTokenService();

	/**
	 * updates AccessToken
	 * 
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken);

	class Builder {
		public static TargetedOffersClient build(final Config config) {
			return new TargetedOffersClientImpl(config);
		}
	}

}
