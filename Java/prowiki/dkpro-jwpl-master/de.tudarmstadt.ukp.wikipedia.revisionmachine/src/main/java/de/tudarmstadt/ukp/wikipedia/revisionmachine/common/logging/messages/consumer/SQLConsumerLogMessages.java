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
package de.tudarmstadt.ukp.wikipedia.revisionmachine.common.logging.messages.consumer;

import java.util.logging.Level;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.SQLConsumerException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.logging.Logger;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.util.Time;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.Task;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.Diff;

/**
 * This class contains the english localized log messages for SQLConsumers.
 *
 * TODO: This file should be replaced with resource files.
 *
 *
 *
 */
public class SQLConsumerLogMessages
{

	/**
	 * Logs the processing of a diff task.
	 *
	 * @param logger
	 *            reference to the logger
	 * @param diff
	 *            reference to the task
	 * @param time
	 *            time
	 */
	public static void logDiffProcessed(final Logger logger,
			final Task<Diff> diff, final long time)
	{

		logger.logMessage(
				Level.INFO,
				"Generated Entry\t" + Time.toClock(time) + "\t"
						+ diff.toString());
	}

	/**
	 * Logs the creation of an output file.
	 *
	 * @param logger
	 *            reference to the logger
	 * @param path
	 *            path of the output file
	 */
	public static void logFileCreation(final Logger logger, final String path)
	{

		logger.logMessage(Level.INFO, "New File created:\t" + path);
	}

	/**
	 * Logs the occurance of an OutOfMemoryError while reading a task.
	 *
	 * @param logger
	 *            reference to the logger
	 * @param task
	 *            reference to the revision task
	 * @param e
	 *            reference to the error
	 */
	public static void logReadTaskOutOfMemoryError(final Logger logger,
			final Task<Diff> task, final OutOfMemoryError e)
	{

		if (task != null) {
			logger.logError(Level.WARNING, "Error while reading a task: "
					+ task.toString(), e);
		}
		else {
			logger.logError(Level.WARNING,
					"Error while reading an unknown task", e);
		}
	}

	/**
	 * Logs the occurance of an SqlConsumerException.
	 *
	 * @param logger
	 *            reference to the logger
	 * @param e
	 *            reference to the exception
	 */
	public static void logSQLConsumerException(final Logger logger,
			final SQLConsumerException e)
	{

		logger.logException(Level.SEVERE, "SQLConsumerException", e);
	}

	/** No object - utility class */
	private SQLConsumerLogMessages()
	{
	}
}
