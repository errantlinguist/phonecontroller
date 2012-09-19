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

import com.errantlinguist.snom.PhoneControllerManager
import scala.swing.Frame
import scala.swing.MainFrame

/**
 * @author Todd Shore
 * @version 04.09.2012
 * @since 04.09.2012
 *
 */
class PhoneControllerMainFrame(private val phoneControllerManager : PhoneControllerManager, title0 : String, private val settingsFrame : Frame) extends MainFrame{
	import scala.collection.immutable.ListMap
	import scala.collection.mutable.Map
	import scala.collection.mutable.Buffer
	import scala.swing.Action
	import scala.swing.BorderPanel
	import scala.swing.BoxPanel
	import scala.swing.Button
	import scala.swing.ComboBox
	import scala.swing.Component
	import scala.swing.Dialog
	import scala.swing.event.ButtonClicked
	import scala.swing.event.EditDone
	import scala.swing.event.Key
	import scala.swing.event.KeyReleased
	import scala.swing.FlowPanel
	import scala.swing.GridPanel
	import scala.swing.GridBagPanel
	import scala.swing.Label
	import scala.swing.MainFrame
	import scala.swing.Menu
	import scala.swing.MenuBar
	import scala.swing.MenuItem
	import scala.swing.Orientation
	import scala.swing.TextField
	
	import com.errantlinguist.snom.PhoneController
	import com.errantlinguist.snom.PhoneKeyEvent
	import com.errantlinguist.snom.PhoneSettings
	
	import edu.berkeley.cs.SysExits

	import java.util.logging.Level
	import javax.swing.KeyStroke
	

	private val createEditMenu:(String) => Menu = title => {
		new Menu(title) {
			mnemonic = Key.E
			
			contents += new MenuItem(Action("Settings...") {
				settingsFrame.visible = true
			})		  
		}
	}
	
	/**
	 * Creates the file menu.
	 */
	private val createFileMenu:(String) => Menu = title => {
		new Menu(title) {
			mnemonic = Key.F
			
			val quitItemName = "Quit"
			contents += new MenuItem(quitItemName) {
				action = new Action(quitItemName) {
					accelerator = Some(KeyStroke.getKeyStroke("ctrl Q"))
					
					def apply(){
						sys.exit(SysExits.EX_OK)
					}
				}   
				
				
				
			}
			
		}
	}
	
	private val createHelpMenu:(String) => Menu = title => {
		new Menu(title) {
			mnemonic = Key.H
			
			contents += new MenuItem(Action("About...") {
				// TODO: Re-do this with Ant-specified properties for automatic naming+versioning+about message
				val dialogTitle = "About"
				Dialog.showMessage(this, "about the program", dialogTitle, Dialog.Message.Info)
			})	
		}
	}
	
	private val MenuCreationFunctions = ListMap(
		"File" -> createFileMenu,
		"Edit" -> createEditMenu,
		"Help" -> createHelpMenu)
		
	private var phoneKeyEventButtons =  Map[PhoneKeyEvent.Value, Button]()
	

	
 
	
	title = title0
	
	menuBar = createMenuBar()
		
	contents = new PhoneControllerPanel(phoneControllerManager)
	
	
	/**
	* Creates a menu bar with a couple of menus and menu items and 
	* set the result as this frame's menu bar.
	*/  
	private def createMenuBar() : MenuBar = {
		new MenuBar {
			MenuCreationFunctions.foreach(entry => {
				contents += entry._2(entry._1)
			})
		}
		  
		  
	}
	

	

}
