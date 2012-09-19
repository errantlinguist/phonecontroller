/*
 *	Licensed to the Apache Software Foundation (ASF) under one
 *	or more contributor license agreements.  See the NOTICE file
 *	distributed with this work for additional information
 *	regarding copyright ownership.  The ASF licenses this file
 *	to you under the Apache License, Version 2.0 (the
 *	"License"); you may not use this file except in compliance
 *	with the License.  You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing,
 *	software distributed under the License is distributed on an
 *	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *	KIND, either express or implied.  See the License for the
 *	specific language governing permissions and limitations
 *	under the License.
 */
package com.errantlinguist.net

/**
 * @author Todd Shore
 * @see <a href="http://stackoverflow.com/a/5571782">http://stackoverflow.com/a/5571782</a>
 * @version 21.08.2012
 * @since 21.08.2012
 *
 */
class HttpPoster(private val userAgent : String, encoding : String, private val httpRequestTimeout : Int = 15000) {
	
	import scala.io.Source
	
	import collection.JavaConversions._
	
	import java.io.OutputStreamWriter
	import java.net.URL
	import java.net.URLConnection
	import java.net.URLEncoder.encode
	import java.util.logging.Level

	import com.errantlinguist.SystemConstants.LineSeparator

	private def encodePostData(data : Map[String, String]) =
		(for ((name, value) <- data) yield encodePostProperty(name, encoding)).mkString("&")

	private def encodePostProperty(name : String, value : String) : String = encode(name, encoding) + "=" + encode(value, encoding)

	def post(url : String, data : Map[String, String]) : String = {
		if(HttpPoster.Logger.isLoggable(Level.FINE)) {
			HttpPoster.Logger.fine("Posting to \"" + url + "\" with properties " + data + ".")
		}
		
		val u = new URL(url)
		val conn = u.openConnection

		conn.setRequestProperty("User-Agent", userAgent)
		conn.setConnectTimeout(httpRequestTimeout)

		conn.setDoOutput(true)
		conn.connect

		val wr = new OutputStreamWriter(conn.getOutputStream())
		wr.write(encodePostData(data))
		wr.flush
		wr.close

		return Source.fromInputStream(conn.getInputStream()).getLines().mkString(LineSeparator)

	}
	
	def post(url : String, propertyName : String, propertyValue : String) : String = {
		if(HttpPoster.Logger.isLoggable(Level.FINE)) {
			HttpPoster.Logger.fine("Posting to \"" + url + "\" with property \"" + propertyName + "\" -> \"" + propertyValue + "\".")
		}
//		require(propertyName != null && !propertyName.matches("\\s*"),
//			{System.err.println("Property name must be a non-empty string.")})
//			
//		require(propertyValue != null && !propertyValue.matches("\\s*"),
//			{System.err.println("Property value must be a non-empty string.")})
			
		val u = new URL(url)
		val conn = u.openConnection

		conn.setRequestProperty("User-Agent", userAgent)
		conn.setConnectTimeout(httpRequestTimeout)

		conn.setDoOutput(true)
		conn.connect

		val wr = new OutputStreamWriter(conn.getOutputStream())
		wr.write(encodePostProperty(propertyName, propertyValue))
		wr.flush
		wr.close

		return Source.fromInputStream(conn.getInputStream()).getLines().mkString(LineSeparator)
	}
}

/**
 * @author Todd Shore
 * @see <a href="http://stackoverflow.com/a/5571782">http://stackoverflow.com/a/5571782</a>
 * @version 21.08.2012
 * @since 21.08.2012
 *
 */
private object HttpPoster {

	private val Logger = java.util.logging.Logger.getLogger(HttpPoster.getClass().getName())	
	
}
