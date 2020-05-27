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
package com.americanexpress.sdk.configuration;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * This configuration class holds the required builder configuration to startup
 * the Targeted offers SDK application
 * 
 * @author jramio
 */
@Getter
@Setter
public class Config {

	/**
	 * URL for establishing connection
	 */
	private String url;

	/**
	 * OAuth Api Key
	 */
	private String apiKey;

	/**
	 * OAuth Api Secret
	 */
	private String apiSecret;

	/**
	 * AccessToken
	 */
	private String accessToken;

	/**
	 * (Optional if Server is taking care of TLS SSL Connection Socket Factory for
	 * enabling 2 way SSL Configuration
	 */
	private SSLConnectionSocketFactory socketFactory;

	/**
	 * Proxy Configuration (Optional, if needs to be tested through a corporate
	 * proxy
	 * 
	 */
	private ProxyConfig proxyConfig;

	/**
	 * PayLoad Encryption config (Optional, if Payload Encryption is required)
	 */
	private JWEConfig jweConfig;

	public String getUrl() {
		return url;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public SSLConnectionSocketFactory getSocketFactory() {
		return socketFactory;
	}

	public ProxyConfig getProxyConfig() {
		return proxyConfig;
	}

	public JWEConfig getJweConfig() {
		return jweConfig;
	}

	private Config() {

	}

	/**
	 * Builder class for creating new instance.
	 * 
	 */
	public static class Builder {

		private String url;

		private String apiKey;

		private String apiSecret;

		private String accessToken;

		private SSLConnectionSocketFactory socketFactory;

		private ProxyConfig proxyConfig;

		private JWEConfig jweConfig;

		public Builder(final String url, final String apiKey, final String apiSecret, final String accessToken,
				final SSLConnectionSocketFactory socketFactory) {
			this.url = url;
			this.apiKey = apiKey;
			this.apiSecret = apiSecret;
			this.accessToken = accessToken;
			this.proxyConfig = new ProxyConfig(false);
			this.jweConfig = new JWEConfig(false);
			this.socketFactory = socketFactory;
		}

		public Builder setProxyConfig(ProxyConfig proxyConfig) {
			this.proxyConfig = proxyConfig;
			return this;
		}

		public Builder setJweConfig(JWEConfig jweConfig) {
			this.jweConfig = jweConfig;
			return this;
		}

		public Config build() {
			Config config = new Config();
			config.apiKey = this.apiKey;
			config.apiSecret = this.apiSecret;
			config.accessToken = this.accessToken;
			config.jweConfig = this.jweConfig;
			config.proxyConfig = this.proxyConfig;
			config.socketFactory = this.socketFactory;
			config.url = this.url;
			return config;
		}

	}
}
