/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.errantlinguist.snom

import java.net.URL

/**
  * @author Todd Shore
  * @version 21.08.2012
  * @since 21.08.2012
  *
  */
class PhoneController(private val connectionURL: URL, private val encoding: String) {

	import java.net.URLConnection
	import java.net.URLEncoder.encode
	import java.util.logging.Level

	import com.github.errantlinguist.net.HttpClient

	private val http = new HttpClient(this.getClass.getSimpleName, encoding)

	def notifyKey(key: PhoneKeyEvent.Value) {
		//		require(key != null,
		//			{System.err.println("Notified key event cannot be null.")})

		if (PhoneController.Logger.isLoggable(Level.FINE)) {
			PhoneController.Logger.fine("Triggering phone key event [" + key + "].")
		}

		val params = Map("key" -> key.toString())
		http.post(connectionURL, params)
	}

	def notifyNumber(number: String) {
		//		require(number != null && !number.matches("\\s*"),
		//			{System.err.println("Notified number to dial must be a non-empty string.")})

		if (PhoneController.Logger.isLoggable(Level.INFO)) {
			PhoneController.Logger.info("Dialing phone number [\"" + number + "\"].")
		}

		val params = Map("number" -> number)
		http.post(connectionURL, params)
	}

}

/**
  * @author Todd Shore
  * @version 21.08.2012
  * @since 21.08.2012
  *
  */
private object PhoneController {
	private val Logger = java.util.logging.Logger.getLogger(PhoneController.getClass().getName())

}
