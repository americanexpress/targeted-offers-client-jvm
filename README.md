# Targeted Offers Java SDK
Amex Targeted Offers API is a capability to request instant pre-qualified , prescreened  Card offer(s) for the applicants, if eligible. 
Targeted Offers API provides an interface to interact with systems of American Express. 
​
</br></br>This Java implementation allows Amex partners to integrate seamlessly to Targeted Offers and reduces complexity out of coding service layer integration to Targeted Offers API. The Targeted Offers Java SDK is a simple wrapper to the API. It assumes you have already set up your credentials with American Express and have your certs prepared. See the [authentication and authorization guides](https://developer.americanexpress.com/products/targeted-offers/guide#authentication) for more information. 
</br></br>

## Table of Contents

- [Documentation](#documentation)    
- [Installation](#installation)
- [Compatibility](#compatibility)
- [Configuration](#configuring-sdk)
- [Authentication](#authentication)
- [Offers](#offers)
    - [Getting offers](#getting-offers)
    - [Acknowledging Offers](#acknowledging-offers)
- [Token](#token)
- [Error Handling](#error-handling)
- [Samples](#running-samples)
- [Open Source Contribution](#contributing)
- [License](#license)
- [Code of Conduct](#code-of-conduct)


<br/>

## Documentation

Please see : [documentation for Amex Targeted Offers API](https://developer.americanexpress.com/products/targeted-offers/overview)

<br/>

## Installation

- Install maven 
    ```
    brew install maven
    ```
- Clone repo
- Go inside px-toa-java-sdk  and type
   ```
    $ mvn clean install
   ```
<br/>

## Compatibility

targeted-offers-client sdk will support Java Version 8 or higher.



<br/>

## Configuring SDK

SDK needs to be configured with OAuth, Mutual Auth and Payload encryption configurations, below is the sample configuration snippet.

```java
private TargetedOffersClient createTargetedOffersClient() throws IOException, CertificateException,
        NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
    Map<String, String> sampleConfig = buildClientConfig();
    TargetedOffersClient targetedOffersClient = null;

    InputStream inputStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(sampleConfig.get(DEVELOPER_PORTAL_SDK));
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(inputStream, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray());
    KeyStore trustStore = KeyStore.getInstance(sampleConfig.get(KEYSTORE_JKS));
    InputStream trustStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(sampleConfig.get(OAUTH_KEYSTORE_TRUST_STREAM));
    trustStore.load(trustStream, sampleConfig.get(OAUTH_KEYSTORE_LOAD_TRUST_STREAM).toCharArray());

    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
            new SSLContextBuilder().loadTrustMaterial(trustStore, (chain, authType) -> false)
                    .loadKeyMaterial(ks, sampleConfig.get(OAUTH_KEYSTORE_PASSPHRASE_PROPERTY).toCharArray(),
                            (aliases, socket) -> sampleConfig.get(OAUTH_KEYSTORE_ALIAS_PROPERTY))
                    .build());

    targetedOffersClient = TargetedOffersClient.Builder
            .build(Config.builder().url(sampleConfig.get(OAUTH_OFFERS_API_ENDPOINT))
            .apiKey(sampleConfig.get(OAUTH_API_KEY))
            .apiSecret(sampleConfig.get(OAUTH_API_SECRET))
            .accessToken(null)
            .socketFactory(socketFactory)
            .jweConfig(new JWEConfig(false))
            .proxyConfig(new ProxyConfig(Boolean.parseBoolean(sampleConfig.get(PROXY_ENABLED)),
                    sampleConfig.get(PROXY_PROTOCOL), sampleConfig.get(PROXY_HOST),
                    Integer.valueOf(sampleConfig.get(PROXY_PORT)))).build());

    return targetedOffersClient;
}
```

<br/>

## Authentication

Amex Targeted Offers API uses token based authentication. The following examples demonstrates how to generate bearer tokens using the SDK :

```java
AccessTokenResponse accessTokenResponse = getAuthenticationToken(targetedOffersClient); //success response

targetedOffersClient.setAccessToken(accessTokenResponse.getAccessToken()); //set the Access Token for further API calls 
```
Sample Response : 

```java
{
  scope: 'default',
  status: 'approved',
  expires_in: '3599', // token expiry in seconds, you can cache the token for the amount of time specified.
  token_type: 'BearerToken',
  access_token: 'wJeW9CPT0DbrqBjrTN1xbMQZkae2'
}
```
Note : you can skip this call if you have an active Token in your cache. if you have an active token, you can just set the bearerToken in config under authentication or call `setAccessToken('access_token')` method to update the config.



<br/>

## Offers :

## Getting Offers

Request body and API mandatory fields can be found at [API Specifications](https://developer.americanexpress.com/products/targeted-offers/resources#readme).

```java

TargetedOffersClient targetedOffersClient = createTargetedOffersClient();

TargetedOffersRequest targetedOffersRequest = objectMapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream("targetedOffersRequest.json"), TargetedOffersRequest.class);

offersResponse = offersService.getOffers(targetedOffersRequest);

//see src/main/resources for a sample header and body

return offersResponse; //successful response
```

This will return an array of offers, you can find more information on response at [reference guide](https://developer.americanexpress.com/products/targeted-offers/resources#readme).

<br/>

## Acknowledging Offers
The `acknowledgeOffers()` method is used to let American Express know that offers were presented to the user. Response will be true if the API call is success.

```java
//applicant_request_tracking_id will be provided as part of the targeted offers response.
acknowledgementRequest.setApplicantRequestTrackingId(offersResponse.getApplicantRequestTrackingId());
				
return offersService.acknowledgeOffers(acknowledgementRequest); //successful response
```


<br/>

## Token
The `getSessionToken()` method is used to get a session token. Session Token is required to maintain the session of the user and is only of one time use.

```java
//applicant_request_tracking_id will be provided as part of the targeted offers response. please pass the value to get session token.
 
TokenRequest tokenRequest = objectMapper.readValue(
	Thread.currentThread().getContextClassLoader().getResourceAsStream("sessionTokenRequest.json"), TokenRequest.class);

TokenResponse tokenResponse = tokenService.getSessionToken(tokenRequest);
				
return objectMapper.writeValueAsString(tokenResponse)); //successful response
```
Sample Success response: 

```java
{
  SessionToken: eyJhbGciOiJSUzI1NiJ9.eyJjbGFpbXNfcmVxIjoidHJ1ZSIsImFjY2Vzc190eXBlIjoiUyIsInRva2VuX2lkIjoiYXBwbGljYW50X3JlcXVlc3RfdHJhY2tpbmdfaWQiLCJyZXNvdXJjZV9pZCI6IjUyMjRhNjMzLTczNDQtNGZjZi1hOGVkLWU2MzE0NmM2ZmVhOCIsInRva2VuX3R5cGUiOiJTIiwiZXhwIjoxNTgyODY3MTQzLCJqdGkiOiI1YzUyYmNlYi1mNmExLTQ0YTEtOTk4ZS0yYTBjNmFmMDhiYmQiLCJjbGllbnRfaWQiOiJDbGllbnRfSUQifQ.NnmzkiiB08YWZQKWKFvAwAHdcNoqjmkSBc4vNwr0TXu1SBWgEhC4J_3mbunQ_PV8FUq6-n4YI4ogPSTuy570AZJ4sq9e86gE-dIuuOMs2AzsLukeIz5PHuHUP9friqpcKHbOqJFMohBNwsA0IGwA96aB9BJ0yPDqcGgiOtju_QekGJRk6LByHUQevuPsHdZjGCgXpXiqHjOFA3SFycE0NUi2pLnC5VISgG-OFFr0HaoqskcM843UMyGb_MGDKcgMJUhDuHaqqsI4oJ-SmGYs6r88vIlxLvPX-JWuSjI8uLITOYvCq8CQPoKEasLhhu93I_YlZO4B423oJ_OQoIvePw
}
```

<br/>

## Error Handling

In case of exceptions encountered while calling American Express APIs, the SDK will throw Errors. 


Possible exceptions : 
```java
- ResourceNotFoundError // ResourceNotFoundError will be raised when the user is not qualified for PreQualified or PreScreened Offers
 
- OffersAuthenticationError // Authentication errors with the API -- example : invalid API Key or Secret is sent to the API

- OffersRequestValidationError // Request Validation Error -- request or configs provided to the SDK are invalid, you can see more info in err.fields for the fields that failed validations.

- NoOffersAvailable // NoOffersAvailable will be raised when the user is not qualified for a PreQual or PreScreened Offers. 

- OffersAPIError // Is a generic type of error, It will be raised when there is an Internal server error or any other error which is not covered by any of the named errors.

- PayloadEncryptionError // PayloadEncryptionError is a generic type of error for Encryption errors,It will be raised when there is an Exception raised at the Payload encryption. More information will be present in the error message.
```

<br/>

## Running Samples 
See TargetedOffersSample.java for sample usages of Targeted Offers SDK.
There are four different samples included to demonstrate the different 
scenarios in which Targeted Offers API can be used:
- Logged In (only PII available)
- Logged In (PII and session data available)
- Logged Out Warm (Partner account number and session data available)
- Logged Out Full (only session data available)

Each sample includes an example request JSON. Please update the example values
with actual values before running the sample.

The "sample.properties" resource file contains the variables needed to run the sample.
Please replace the example values with actual values before running the sample.

```java
developer.portal.sdk=jks_file_example.jks           // SDK keystore
keystore.jks=jks                                    // Java keystore format type
oauth.keystore.trust.stream=trust_file_example.jks  // Path to trust store file
oauth.keystore.load.trust.stream=trust_user_example // Keystore username
oauth.keystore.passphrase.property=password_example // Keystore password
oauth.keystore.alias.property=alias_example         // Alias (or name) under which the key is stored in the keystore
oauth.offers.api.endpoint=https://example.aexp.com  // TargetedOffersAPI SDK endpoint
oauth.api.key=auth_key_example                      // OAuth Client ID/Key
oauth.api.secret=auth_key_secret_example            // OAuth Secret 
proxy.protocol=http                                 // Protocol Client uses to connect to proxy/load balancer
proxy.enabled=true                                  // Enabled or disabled proxy
proxy.host=proxy.example.com                        // Proxy host
proxy.port=8080                                     // Proxy port
```

<br/>

## Contributing

We welcome your interest in the American Express Open Source Community on Github. Any Contributor to
any Open Source Project managed by the American Express Open Source Community must accept and sign
an Agreement indicating agreement to the terms below. Except for the rights granted in this 
Agreement to American Express and to recipients of software distributed by American Express, You
reserve all right, title, and interest, if any, in and to Your Contributions. Please
[fill out the Agreement](https://cla-assistant.io/americanexpress/targeted-offers-client-java).

<br/>

## License

Any contributions made under this project will be governed by the
[Apache License 2.0](./LICENSE.txt).


<br/>

## Code of Conduct

This project adheres to the [American Express Community Guidelines](./CODE_OF_CONDUCT.md). By
participating, you are expected to honor these guidelines.