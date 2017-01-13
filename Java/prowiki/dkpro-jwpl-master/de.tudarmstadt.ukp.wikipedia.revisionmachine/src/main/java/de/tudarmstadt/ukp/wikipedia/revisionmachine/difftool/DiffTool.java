/*******************************************************************************
 * Copyright 2016
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.config.ConfigurationReader;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.config.gui.control.ConfigSettings;

/**
 * This class contains the start method for the DiffTool application.
 *
 *
 *
 */
public class DiffTool
{

	/**
	 * Starts the DiffTool application.
	 *
	 * @param args
	 *            program arguments args[0] has to be the path to the
	 *            configuration file
	 */
	public static void main(final String[] args)
	{

		if (args.length != 1) {
			throw new IllegalArgumentException(
					"Configuration File ist missing.");
		}

		try {

			// Reads the configuration
			ConfigSettings config = readConfiguration(args[0]);
			new DiffToolThread(config).run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads and parses the configuration file.
	 *
	 * @param path
	 *            path to the configuration file
	 * @return ConfigurationSettings
	 *
	 * @throws IOException
	 *             if an error occured while reading the configuration file
	 * @throws SAXException
	 *             if an error occured while using the xml parser
	 * @throws ParserConfigurationException
	 *             if the initialization of the xml parser failed
	 */
	private static ConfigSettings readConfiguration(final String path)
		throws IOException, SAXException, ParserConfigurationException
	{

		ConfigurationReader reader = new ConfigurationReader(path);
		return reader.read();
	}

	/** No object - Utility class */
	private DiffTool()
	{
	}
}
