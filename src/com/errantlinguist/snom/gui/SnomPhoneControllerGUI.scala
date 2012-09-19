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
package com.errantlinguist.snom.gui

import scala.swing.SimpleSwingApplication
import com.errantlinguist.snom.PhoneControllerManager


/**
 * @author Todd Shore
 * @version 15.08.2012
 * @since 15.08.2012
 *
 */
object SnomPhoneControllerGUI extends SimpleSwingApplication with PhoneControllerManager {

	import scala.swing.Component
	import scala.swing.Dialog
	import scala.swing.MainFrame
	
	import java.util.logging.Level
	
	import com.errantlinguist.SystemConstants.FileEncoding	
	import com.errantlinguist.snom.PhoneController
	import com.errantlinguist.snom.PhoneSettings
	
	private val Logger = java.util.logging.Logger.getLogger(SnomPhoneControllerGUI.getClass().getName())
	
	private lazy val SettingsFrame = new SettingsFrame(this, "Settings")
	
	private var settings : PhoneSettings = PhoneSettings.createFromProperties()
	if(settings == null) {
		promptPhoneSettings()
		// TODO: Make settings frame appear "on top of" main frame and focused
	}
	
	private var _phoneController = new PhoneController(settings.connectionURL, settings.encoding)	
	
	updateNewPhoneSettings()
	
	override def notifyNewPhoneSettings(newSettings : PhoneSettings) {
		settings = newSettings
		updateNewPhoneSettings()
		
	}

   override def phoneController = _phoneController	
	
   override def top = new PhoneControllerMainFrame(this, "SnomPhoneController", SettingsFrame)

   private def createPhoneSettings() : PhoneSettings = {
		var result = PhoneSettings.createFromProperties()
		if (result == null){
			result = new PhoneSettings("", FileEncoding)
		}
		return result
		
	}
	
	private def updateNewPhoneSettings() {
		if(Logger.isLoggable(Level.CONFIG)) {
			Logger.config("New phone controller settings [" + settings.toString() + "].")		   
		}

		_phoneController = new PhoneController(settings.connectionURL, settings.encoding)
		SettingsFrame.notifyNewPhoneSettings(settings)
	}
	
	private def promptPhoneSettings() {
		// Add some minimal settings to allow the settings frame to be correctly displayed
		settings = new PhoneSettings("", FileEncoding)
		Dialog.showMessage(top.contents.head, "No phone controllers settings were defined; please enter them manually before using.", "Properties missing", Dialog.Message.Error)
		SettingsFrame.visible = true
		SettingsFrame.contents.head.requestFocus()
	}
}
