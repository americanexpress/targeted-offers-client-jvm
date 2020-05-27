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

import com.americanexpress.sdk.client.http.HttpClient;
import com.americanexpress.sdk.configuration.Config;
import com.americanexpress.sdk.exception.OffersException;
import com.americanexpress.sdk.models.entities.RequestHeader;
import com.americanexpress.sdk.models.targeted_offers.AcknowledgementRequest;
import com.americanexpress.sdk.models.targeted_offers.OffersResponse;
import com.americanexpress.sdk.models.targeted_offers.TargetedOffersRequest;
import com.americanexpress.sdk.service.impl.OffersServiceImpl;

/**
 * 
 * The Offer Service Interface handles all the API operations required for
 * Targeted Offers service
 * 
 * @author jramio
 */
public interface OffersService {
	/**
	 * Get Offers from Targeted offers API
	 * 
	 * @return {@link OffersResponse}
	 * @throws OffersException
	 */
	public OffersResponse getOffers(TargetedOffersRequest targetedOffersRequest, RequestHeader requestHeader) throws OffersException;

	/**
	 * Acknowledging offers
	 * 
	 * @param acknowledgementRequest
	 * @return Boolean
	 */
	public Boolean acknowledgeOffers(AcknowledgementRequest acknowledgementRequest, RequestHeader requestHeader);

	class Builder {
		public static OffersService create(final Config config, final HttpClient authClient) {
			return new OffersServiceImpl(config, authClient);
		}

		private Builder() {

		}
	}
}
