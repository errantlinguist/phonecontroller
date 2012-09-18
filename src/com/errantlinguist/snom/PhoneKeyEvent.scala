/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */
package com.errantlinguist.snom

object PhoneKeyEvent extends Enumeration {

	object EventType extends Enumeration {
		
	    import scala.collection.mutable.HashMap
	    import scala.collection.mutable.MultiMap
	    import scala.collection.mutable.Set

		val CallControl, Function, Keypad, Navigation, Programmable, ProgrammableExpansion, Volume = Value

		val Instances = new HashMap[EventType.Value, Set[PhoneKeyEvent.Value]] with MultiMap[EventType.Value, PhoneKeyEvent.Value]

	}

	protected final def Value(httpCommandParameterValue : String, eventType : EventType.Value) : Value = {
		val result = Value(httpCommandParameterValue)

		EventType.Instances.addBinding(eventType, result)

		return result
	}

	val Key0 = Value("0", EventType.Keypad)
	val Key1 = Value("1", EventType.Keypad)
	val Key2 = Value("2", EventType.Keypad)
	val Key3 = Value("3", EventType.Keypad)
	val Key4 = Value("4", EventType.Keypad)
	val Key5 = Value("5", EventType.Keypad)
	val Key6 = Value("6", EventType.Keypad)
	val Key7 = Value("7", EventType.Keypad)
	val Key8 = Value("8", EventType.Keypad)
	val Key9 = Value("9", EventType.Keypad)
    val Asterisk = Value("*", EventType.Keypad)
	val NumberSign = Value("#", EventType.Keypad)
	
    val DoNotDisturb = Value("DND", EventType.CallControl)
    val FHold = Value("F_HOLD", EventType.CallControl)	
	val Offhook = Value("OFFHOOK", EventType.CallControl)
	val Onhook = Value("ONHOOK", EventType.CallControl)
	val Record = Value("REC", EventType.CallControl)
    val Redial = Value("REDIAL", EventType.CallControl)
	val Transfer = Value("TRANSFER", EventType.CallControl)

    val Enter = Value("ENTER", EventType.Navigation)
    val Cancel = Value("CANCEL", EventType.Navigation)	
	val Up = Value("UP", EventType.Navigation)
	val Down = Value("DOWN", EventType.Navigation)
    val Left = Value("LEFT", EventType.Navigation)
	val Right = Value("RIGHT", EventType.Navigation)
}