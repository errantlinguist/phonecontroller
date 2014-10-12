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
package com.github.errantlinguist.net

/**
  * @author Todd Shore
  * @see <a href="http://stackoverflow.com/a/5571782">http://stackoverflow.com/a/5571782</a>
  * @version 21.08.2012
  * @since 21.08.2012
  *
  */
class HttpClient(private val userAgent: String, encoding: String, private val httpRequestTimeout: Int = 15000) {

	import scala.io.Source

	import collection.JavaConversions._

	import java.io.BufferedWriter
	import java.io.OutputStreamWriter
	import java.net.URL
	import java.net.URLConnection
	import java.net.URLEncoder.encode
	import java.util.logging.Level

	import com.github.errantlinguist.SystemConstants.LineSeparator
	
	private def createConnection(url: URL): URLConnection = {
		val result = url.openConnection
		result.setRequestProperty("User-Agent", userAgent)
		result.setConnectTimeout(httpRequestTimeout)
		return result
	}

	private def encodePostData(data: Map[String, String]) =
		(for ((name, value) ← data) yield encodePostProperty(name, encoding)).mkString("&")

	private def encodePostProperty(name: String, value: String): String = encode(name, encoding) + "=" + encode(value, encoding)
	
	def post(url: URL, data: Map[String, String]): String = {
		if (HttpClient.Logger.isLoggable(Level.FINE)) {
			HttpClient.Logger.fine("Posting to \"" + url + "\" with properties " + data + ".")
		}

		val connection = createConnection(url)
		connection.setDoOutput(true)
		connection.connect

		val writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))
		try {
			writer.write(encodePostData(data))
			writer.flush
		} finally {
			writer.close
		}
		
		// NOTE: "Source.fromInputStream(...)" creates a buffered input source
		return Source.fromInputStream(connection.getInputStream()).getLines().mkString(LineSeparator)
	}
}

/**
  * @author Todd Shore
  * @see <a href="http://stackoverflow.com/a/5571782">http://stackoverflow.com/a/5571782</a>
  * @version 21.08.2012
  * @since 21.08.2012
  *
  */
private object HttpClient {

	private val Logger = java.util.logging.Logger.getLogger(HttpClient.getClass().getName())

}
