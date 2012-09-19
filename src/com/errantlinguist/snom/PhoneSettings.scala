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
package com.errantlinguist.snom


/**
 * @author Todd Shore
 * @version 03.09.2012
 * @since 03.09.2012
 *
 */
class PhoneSettings(var phoneHostname : String, var encoding : String) {

	import java.io.FileOutputStream
	import java.util.Properties
	
	import PhoneSettings.HostnamePropertyName
	import PhoneSettings.EncodingPropertyName
	
	import PhoneSettings.ExternalPhoneSettingsResourceName
	
	private var _connectionURL = createConnectionURL(phoneHostname)
	
	def connectionURL = _connectionURL
	
	def connectionURL_= (value : String) : Unit = _connectionURL = value
	
	def toProperties() : Properties = {
		new Properties{
			setProperty(HostnamePropertyName, phoneHostname)
			setProperty(EncodingPropertyName, encoding)
		}
	}
	
	override def toString() : String = {
		val builder = new StringBuilder(PhoneSettings.EstimatedMaximumStringReprLength)
		builder ++= PhoneSettings.getClass().getSimpleName()
		builder ++= "[phoneHostname="
		builder ++= phoneHostname
		builder ++= ", connectionURL="
		builder ++= connectionURL
		builder ++= ", encoding="
		builder ++= encoding
		builder += ']'
		builder.toString()
	}
	
	def store(outfile : String = ExternalPhoneSettingsResourceName) {
		val props = toProperties()
		println(props)
		props.store(new FileOutputStream(outfile), "Phone connection properties")
	}
	
	private def createConnectionURL(phoneHostname : String) = "http://" + phoneHostname + "/command.htm"
	


}


object PhoneSettings {
	
	import java.io.FileInputStream
	import java.io.FileNotFoundException
	import java.util.Properties
	
	
	private val EstimatedMaximumStringReprLength = 96
	
	private val PhonePropertiesName = "phone"
		
	private val EncodingPropertyName = PhonePropertiesName + '.' + "encoding"
	
	private val HostnamePropertyName = PhonePropertiesName + '.' + "hostname"
	
	private val ExternalPhoneSettingsResourceName = "phone.properties"
	
	def canCreatePhoneSettingsFromSystemProperties() : Boolean = {
		sys.props.isDefinedAt(HostnamePropertyName) && sys.props.isDefinedAt(EncodingPropertyName)
	}
	
	def canCreatePhoneSettingsProperties() : Boolean = {
		getClass().getResourceAsStream(ExternalPhoneSettingsResourceName) != null
	}
	
	def createFromProperties() : PhoneSettings = {
		
		var result : PhoneSettings = null
		if(canCreatePhoneSettingsFromSystemProperties()) {
			result = createPhoneSettingsFromSystemProperties()
			
		} else {
			// Try to get the properties from an external resource
			try {
				val settingsResource = new FileInputStream(ExternalPhoneSettingsResourceName)
				val settingsProperties = new Properties{
					load(settingsResource)
				}
				 result = createPhoneSettingsFromExternalProperties(settingsProperties)
			} catch {
				case ioe: FileNotFoundException => {/* do nothing */}
			}
		   
		}
		
		return result
		
	}

	private def createPhoneSettingsFromExternalProperties(props : Properties) : PhoneSettings = {
		val hostname = props.getProperty(HostnamePropertyName)
		require(hostname != null, throw new IllegalArgumentException(HostnamePropertyName + " is not set."))
		val encoding = props.getProperty(EncodingPropertyName)
		require(encoding != null, throw new IllegalArgumentException(EncodingPropertyName + " is not set."))
		new PhoneSettings(hostname, encoding)
	}   
	
	private def createPhoneSettingsFromSystemProperties() : PhoneSettings = {
		val hostname = sys.props.getOrElse(HostnamePropertyName, throw new IllegalArgumentException(HostnamePropertyName + " is not set."))
		val encoding = sys.props.getOrElse(EncodingPropertyName, throw new IllegalArgumentException(EncodingPropertyName + " is not set."))
		new PhoneSettings(hostname, encoding)
	}
	
	def writeSettingsToFile(phoneSettings : PhoneSettings) {
		// TODO: Finish
		
	}
	
   
}
