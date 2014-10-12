package com.github.errantlinguist.snom.gui

import java.net.URLClassLoader

class ApplicationInfoMessageFactory(private val resourceBundleName: String) {
	
	import java.util.Properties
	import scala.io.Source
	import scala.io.BufferedSource
	
	private object Property extends Enumeration {
		val Name = Value("name")
		val Description = Value("description")
		val Version = Value("version")
		val Author = Value("author")
		val Website = Value("website")
	}
	
	def create() : String = {
		var result : String = null
		
		var resourceSource : BufferedSource = null
		try {
			val resourceSource = Source.fromURL(Thread.currentThread().getContextClassLoader().getResource(resourceBundleName))
			val resourceStream = resourceSource.bufferedReader
			val properties = new Properties() {
				load(resourceStream)
			}
			val name = properties.getProperty(Property.Name.toString())
			val description = properties.getProperty(Property.Description.toString())
			val version = properties.getProperty(Property.Version.toString())
			val author = properties.getProperty(Property.Author.toString())
			val website = properties.getProperty(Property.Website.toString())
			
			result = "%s%n%n%s%n%nVersion: %s%nAuthor: %s%nWebsite: %s".format(name, description, version, author, website)						
			
		} finally {
			if (resourceSource != null) {
				resourceSource.close()
			}
		}
		
		return result
	}

}