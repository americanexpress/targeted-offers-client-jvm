package com.americanexpress.sdk.exception;

import com.americanexpress.sdk.models.targeted_offers.ThirdpartyRecommendation;
import lombok.Getter;

import java.util.List;

@Getter
public class OffersAPIException extends OffersException{
    /**
     * indicates if the applicant is eligible for third party offers
     */
    private final Boolean thirdPartyAcquisitionEligible;

    /**
     * the list of third party offer channels
     */
    private final List<String> thirdPartyAcquisitionChannels;

    /**
     * s
     * the list of third party offer recommendations
     */
    private final List<ThirdpartyRecommendation> thirdpartyRecommendation;


    public OffersAPIException(String userMessage,
                              String developerMessage,
                              Boolean thirdPartyAcquisitionEligible,
                              List<String> thirdPartyAcquisitionChannels,
                              List<ThirdpartyRecommendation> thirdpartyRecommendation) {
        super(userMessage, developerMessage);
        this.thirdPartyAcquisitionEligible = thirdPartyAcquisitionEligible;
        this.thirdPartyAcquisitionChannels = thirdPartyAcquisitionChannels;
        this.thirdpartyRecommendation = thirdpartyRecommendation;
    }
}
