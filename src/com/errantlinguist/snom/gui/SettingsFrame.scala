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
package com.errantlinguist.snom.gui

import scala.swing.Frame
import com.errantlinguist.snom.PhoneSettingsListener
import com.errantlinguist.snom.PhoneSettings

/**
 * @author Todd Shore
 * @version 03.09.2012
 * @since 03.09.2012
 *
 */
class SettingsFrame(private val settingsListener : PhoneSettingsListener, title0 : String, private var currentSettings : PhoneSettings = null) extends Frame with PhoneSettingsListener {
	 
	import collection.JavaConversions._
	import scala.swing.Action
	import scala.swing.BoxPanel
	import scala.swing.Button
	import scala.swing.ComboBox
	import scala.swing.Dialog
	import scala.swing.FlowPanel
	import scala.swing.Label
	import scala.swing.Orientation
	import scala.swing.TextField
	
	import java.nio.charset.Charset
	
	title = title0
	
	private lazy val encodingBox = {
		val encodingNameIter = for (charset <- Charset.availableCharsets().values())  yield charset.name()
		val encodingNames = encodingNameIter.toSeq
		new ComboBox(encodingNames) {
			makeEditable()
		}
	}
	
	private lazy val phoneHostnameField = new TextField(SettingsFrame.PhoneHostnameFieldColumns)
	
	contents = new BoxPanel(Orientation.Vertical){
		contents += createPhoneHostnamePanel()
		contents += createEncodingPanel()
		contents += createCloseButtonPanel()
		
		focusable = true
	}
	
	override def notifyNewPhoneSettings(newSettings : PhoneSettings) {
		currentSettings = newSettings
		updateDisplayedSettings()
		
	}
	
	private def createPhoneHostnamePanel() : FlowPanel = {
		new FlowPanel {
			contents += new Label("Phone hostname")
			contents += phoneHostnameField
		}
	}
	
	private def createEncodingPanel() : FlowPanel = {
		new FlowPanel {
			contents += new Label("Encoding")
			contents += encodingBox
		}
	}
	
		

	
	private def createCloseButtonPanel() : FlowPanel = {
		new FlowPanel(){
			contents += new Button(Action("OK"){
				val newSettings = createNewPhoneSettings()
				if(!isValidHostname()) {
					Dialog.showMessage(this, "Please enter a valid (i.e. non-empty) hostname.", "Invalid hostname", Dialog.Message.Error)
				} else if (!isValidEncoding()) {
					Dialog.showMessage(this, "Please enter a valid (i.e. non-empty) encoding.", "Invalid encoding", Dialog.Message.Error)
					
				} else {
					settingsListener.notifyNewPhoneSettings(newSettings)
					newSettings.store()
					dispose()
				}
				
				
				
			})
			contents += new Button(Action("Cancel"){
				dispose()
			})
		}
	}
	
	
	private def createNewPhoneSettings() : PhoneSettings = {
		new PhoneSettings(phoneHostnameField.text, encodingBox.selection.item)
	}
	
	private def updateDisplayedSettings() {
		phoneHostnameField.text = currentSettings.phoneHostname
		encodingBox.selection.item = currentSettings.encoding
	}
	
	private def isValidHostname() : Boolean = {
		(phoneHostnameField.text != null
			&& !phoneHostnameField.text.matches("\\s*")
			)
	}
	
   private def isValidEncoding() : Boolean = {
		   (encodingBox.selection.item != null
			&& !encodingBox.selection.item.matches("\\s*"))
	}

}

/**
 * @author Todd Shore
 * @version 03.09.2012
 * @since 03.09.2012
 *
 */
private object SettingsFrame {
	
	private val PhoneHostnameFieldColumns = 15
}
