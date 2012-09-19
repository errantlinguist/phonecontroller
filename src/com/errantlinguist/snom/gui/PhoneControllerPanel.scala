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

import scala.swing.BorderPanel

import com.errantlinguist.snom.PhoneControllerManager

/**
 *
 * @author Todd Shore
 * @version 04.09.2012
 * @since 04.09.2012
 *
 */
class PhoneControllerPanel(var phoneControllerManager : PhoneControllerManager) extends BorderPanel {

  import scala.collection.mutable.Buffer
  import scala.swing.Action
  import scala.swing.BoxPanel
  import scala.swing.Button
  import scala.swing.Component
  import scala.swing.ComboBox
  import scala.swing.event.ButtonClicked
  import scala.swing.event.Key
  import scala.swing.event.KeyReleased
  import scala.swing.FlowPanel
  import scala.swing.GridPanel
  import scala.swing.GridBagPanel
  import scala.swing.Orientation
  import com.errantlinguist.snom.PhoneKeyEvent
  import com.errantlinguist.snom.PhoneSettingsListener
  import PhoneControllerPanel._
	
	private var phoneKeyEventButtons =  Map[PhoneKeyEvent.Value, Button]()
		
	private def createDirectionalNavigationPanel() : GridBagPanel = {
		new GridBagPanel() {
			val constraints = new Constraints()
			constraints.gridx = 1
			constraints.gridy = 0
			add(createPhoneKeyEventButton(PhoneKeyEvent.Up), constraints)
			
			constraints.gridx = 0
			constraints.gridy = 1
			add(createPhoneKeyEventButton(PhoneKeyEvent.Left), constraints)
			constraints.gridx = 2
			add(createPhoneKeyEventButton(PhoneKeyEvent.Right), constraints)
			constraints.gridx = 1
			constraints.gridy = 2
			add(createPhoneKeyEventButton(PhoneKeyEvent.Down), constraints)
 
			
		}
	}
	
	private def createDisplay(dialButton : Button, clearButton : Button, displayHistory : Buffer[String]) : ComboBox[String] = {
		new ComboBox(displayHistory) {

			// An input verifier which matches only digits
			def inputVerifier(v : Component) : Boolean = selection.item.matches(DialNumberPattern)		  
			
			makeEditable()
			
			// Listen to dial button
			listenTo(dialButton)
			reactions += {
				case b: ButtonClicked if b == dialButton =>   {
					val input = selection.item
					if(input != null && input.matches(DialNumberPattern)){
						phoneControllerManager.phoneController.notifyNumber(input)
						// Add the input to the last-dialled number history
						displayHistory += input
 
					}
						
				}
			}
			// Listen to clear button   
			listenTo(clearButton)
			reactions += {
				case b: ButtonClicked if b == clearButton =>   {
					// Clear the display
					selection.item = ""
				   
				}			   
				
			}
						
			// Listen to keypad keys
			// TODO: ensure that keys are always listened to when the text field or a menu window is not in use,
			// i.e. ensure that this panel is always selected and listening to the keys
			// Get the set of all keypad key events
			val keypadEvents = PhoneKeyEvent.EventType.Instances(PhoneKeyEvent.EventType.Keypad)
			keypadEvents.foreach({ event =>
				// Listen to each button representing a keypad key event
				val keypadEventButton = phoneKeyEventButtons(event)
				listenTo(keypadEventButton)
				reactions += {
					case b: ButtonClicked if b == keypadEventButton => {
					  val input = selection.item
					  if(input != null && !input.matches("\\s*")) {
					  // Add the key event name (e.g. "2" or "#" to the display)
							selection.item = input + event.toString()						   
					  } else {
							selection.item = event.toString()
					  }

				  }
				  
			  }
				
			})
			
			

			
		}
		
		
	}
	
	private def createDisplayNavigationPanel() : BoxPanel = {
		val enterButton = createPhoneKeyEventButton(PhoneKeyEvent.Enter)
		val cancelButton = createPhoneKeyEventButton(PhoneKeyEvent.Cancel)
		
		new BoxPanel(Orientation.Vertical) {
			val displayPanel = createDisplayPanel(enterButton, cancelButton)   
			contents += displayPanel

			val navigatonPanel = createNavigationPanel(enterButton, cancelButton)
			contents += navigatonPanel
		}
		
	}
	
	
	private def createDisplayPanel(dialButton : Button, clearButton : Button) : FlowPanel = {
		new FlowPanel {
			val display = createDisplay(dialButton, clearButton, DisplayHistory)
			contents += display
			
		}
	}
	
	/**
	 * Creates a new {@link Button} for a given {@link PhoneKeyEvent} and adds
	 * the button to the global map of such buttons to their respective events;
	 * that means multiple buttons cannot be created for the same key event.
	 */
	private def createPhoneKeyEventButton(phoneKeyEvent: PhoneKeyEvent.Value) : Button = {
		// Only one button can be created per key event
		require(!phoneKeyEventButtons.contains(phoneKeyEvent),
			{System.err.println("A Button for the " + PhoneKeyEvent.getClass().getSimpleName() + ' ' + phoneKeyEvent + " has already been created.")})
		
		val keyEventButtonName = PhoneKeyEventButtonNames(phoneKeyEvent)
		
		val result = new Button(Action(keyEventButtonName) {
			phoneControllerManager.phoneController.notifyKey(phoneKeyEvent)
			
		})
		
		// Add the button to the map of all created key event buttons
		phoneKeyEventButtons += phoneKeyEvent -> result  
		return result
				
	}
	
	
	private def createPhoneKeyEventTypeControlPanel(keyEvents : Iterable[PhoneKeyEvent.Value], rowCount : Int, columnCount : Int) : GridPanel = {
		new GridPanel(rowCount, columnCount) {
			// Get the intersection of all key events of the given type and the events with button names
			keyEvents.foreach(phoneKeyEvent => contents += createPhoneKeyEventButton(phoneKeyEvent))
		}
		
	}
	
	private def createNavigationPanel(enterButton : Button, cancelButton : Button) : BoxPanel = {
		new BoxPanel(Orientation.Horizontal){
			contents += cancelButton
			
			contents += createDirectionalNavigationPanel()
			
			contents += enterButton
		}
	}
	
			val keypadControlPanel = createPhoneKeyEventTypeControlPanel(KeypadKeyEvents, KeypadDimensions._1, KeypadDimensions._2)
			add(keypadControlPanel, BorderPanel.Position.Center)
			
			val displayNavigationPanel = createDisplayNavigationPanel()
			add(displayNavigationPanel, BorderPanel.Position.North)
			
			val callControlPanel = createPhoneKeyEventTypeControlPanel(CallControlPanelKeyEvents, CallControlPanelDimensions._1, CallControlPanelDimensions._2)
			add(callControlPanel, BorderPanel.Position.East)
			
		   listenTo(keys)
		   // Add key bindings
		   KeyPhoneKeyEvents.foreach(entry => {
			 
			var button = phoneKeyEventButtons(entry._2)
			reactions += {
//			case KeyPressed(_, entry._1, _, _) =>
					
				case KeyReleased(_, entry._1, _, _) => button.doClick()
			 }
			 
		   })
			focusable = true
}

/**
 *
 * @author Todd Shore
 * @version 04.09.2012
 * @since 04.09.2012
 *
 */
private object PhoneControllerPanel {

   import scala.collection.mutable.Buffer
   import scala.swing.event.Key
   
   import com.errantlinguist.snom.PhoneKeyEvent
	
   private val CallControlPanelDimensions = (4, 1)
	
	private val CallControlPanelKeyEvents = List(
		PhoneKeyEvent.Redial,
		PhoneKeyEvent.Transfer,
		PhoneKeyEvent.FHold, PhoneKeyEvent.DoNotDisturb		
		)
		
	private val DialNumberPattern = "[#\\d\\*]+"
		
	private val DisplayHistory = Buffer[String]()

	private val KeypadDimensions = (4, 3)
	
	private val KeypadKeyEvents = List(
		PhoneKeyEvent.Key1, PhoneKeyEvent.Key2, PhoneKeyEvent.Key3,
		PhoneKeyEvent.Key4, PhoneKeyEvent.Key5, PhoneKeyEvent.Key6,
		PhoneKeyEvent.Key7, PhoneKeyEvent.Key8, PhoneKeyEvent.Key9,
		PhoneKeyEvent.Asterisk, PhoneKeyEvent.Key0, PhoneKeyEvent.NumberSign
		)
		
	private val KeyPhoneKeyEvents = Map(
		Key.Key0 -> PhoneKeyEvent.Key0,
		Key.Key1 -> PhoneKeyEvent.Key1,
		Key.Key2 -> PhoneKeyEvent.Key2,
		Key.Key3 -> PhoneKeyEvent.Key3,
		Key.Key4 -> PhoneKeyEvent.Key4,
		Key.Key5 -> PhoneKeyEvent.Key5,
		Key.Key6 -> PhoneKeyEvent.Key6,
		Key.Key7 -> PhoneKeyEvent.Key7,
		Key.Key8 -> PhoneKeyEvent.Key8,
		Key.Key9 -> PhoneKeyEvent.Key9,
		Key.Numpad0 -> PhoneKeyEvent.Key0,
		Key.Numpad1 -> PhoneKeyEvent.Key1,
		Key.Numpad2 -> PhoneKeyEvent.Key2,
		Key.Numpad3 -> PhoneKeyEvent.Key3,
		Key.Numpad4 -> PhoneKeyEvent.Key4,
		Key.Numpad5 -> PhoneKeyEvent.Key5,
		Key.Numpad6 -> PhoneKeyEvent.Key6,
		Key.Numpad7 -> PhoneKeyEvent.Key7,
		Key.Numpad8 -> PhoneKeyEvent.Key8,
		Key.Numpad9 -> PhoneKeyEvent.Key9,
		Key.Enter -> PhoneKeyEvent.Enter,
		Key.Escape -> PhoneKeyEvent.Cancel,
		Key.H -> PhoneKeyEvent.FHold
	)
	
	private val PhoneKeyEventButtonNames = Map(
		PhoneKeyEvent.Asterisk -> "*",
		PhoneKeyEvent.Cancel -> "\u2717",
		PhoneKeyEvent.DoNotDisturb -> "DND",
		PhoneKeyEvent.Enter -> "\u2713",
		PhoneKeyEvent.FHold -> "Hold",
		PhoneKeyEvent.Key0 -> "0",
		PhoneKeyEvent.Key1 -> "1",
		PhoneKeyEvent.Key2 -> "2",
		PhoneKeyEvent.Key3 -> "3",
		PhoneKeyEvent.Key4 -> "4",
		PhoneKeyEvent.Key5 -> "5",
		PhoneKeyEvent.Key6 -> "6",
		PhoneKeyEvent.Key7 -> "7",
		PhoneKeyEvent.Key8 -> "8",
		PhoneKeyEvent.Key9 -> "9",
		PhoneKeyEvent.NumberSign -> "#",
		PhoneKeyEvent.Record -> "Record",
		PhoneKeyEvent.Redial -> "Redial",
		PhoneKeyEvent.Transfer -> "Transfer",
		PhoneKeyEvent.Up -> "\u25B4",
		PhoneKeyEvent.Down -> "\u25BE",
		PhoneKeyEvent.Left -> "\u25C0",
		PhoneKeyEvent.Right -> "\u25B6"
		)
}
