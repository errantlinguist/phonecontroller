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
package com.errantlinguist.snom.cli

import edu.berkeley.cs.SysExits
import util.control.Breaks.breakable
import util.control.Breaks.break
import com.errantlinguist.snom.PhoneController

/**
 * @author <a href="mailto:todd.shore@excelsisnet.com">Todd Shore</a>
 * @version 29.08.2012
 * @since 29.08.2012
 *
 * TODO: Finish
 */
object SnomPhoneControllerCLI extends App {
	
	private val Encoding = "UTF-8"
        
    private val ConnectionURL = "http://10.49.7.84/command.htm"
	
    private val PhoneController = new PhoneController(ConnectionURL, Encoding)
	
	private val CommandPrompt = "snom> "
		
    private var isRunning = false
		
	private val exitCode = run()
    sys.exit(exitCode)
	
   
    private def promptUserInput() : String = {
        Console.print(CommandPrompt)
        return Console.readLine()
	}
	
	private val ExitCommands = Set("exit", "quit")
	
	
	private def parse(word: CharSequence) = word match {
		case "exit" => sys.exit(SysExits.EX_OK)
		case "quit" => sys.exit(SysExits.EX_OK)
		
		case "connect_test" => PhoneController.notifyNumber("0017635718729")
		
	}

	
	private def run(): Int = {
		
		var shouldExit = false

        breakable { 
			Iterator.continually(promptUserInput()).foreach { input =>
			val words = input.toLowerCase().split("\\s+")
			words foreach { word =>
			parse(word)	
			}
			
//			if(isExitCommand(input)) {
//			    shouldExit = true
//				break
//			}
    			
    			
    		}
		}

		
		
		return SysExits.EX_OK
	}

}