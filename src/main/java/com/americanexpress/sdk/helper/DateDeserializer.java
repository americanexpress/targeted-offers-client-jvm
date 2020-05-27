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
package com.americanexpress.sdk.helper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

/**
 * The DateDeserializer class handles deserializing JSON data containing custom date formats
 * 
 * @author jramio
 */
@Slf4j
public class DateDeserializer extends JsonDeserializer<String> {

	private static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd", "yyyyMMdd" };

	@Override
	public String deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext)
			throws IOException, JsonProcessingException {
		String date = jsonparser.getText();
		for (String DATE_FORMAT : DATE_FORMATS) {
			try {
				DateFormat df = new SimpleDateFormat(DATE_FORMAT);
				df.setLenient(false);
				return new SimpleDateFormat("yyyyMMdd").format(df.parse(date));
			} catch (ParseException e) {
				continue;
			}
		}
		log.debug("Invalid Date format::" + date);
		return null;
	}
}