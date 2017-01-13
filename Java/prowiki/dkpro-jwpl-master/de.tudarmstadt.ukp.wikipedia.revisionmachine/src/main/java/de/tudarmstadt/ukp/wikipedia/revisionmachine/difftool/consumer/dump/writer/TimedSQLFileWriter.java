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
package de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.consumer.dump.writer;

import java.io.IOException;
import java.util.logging.Level;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.ConfigurationException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.LoggingException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.SQLConsumerException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.logging.Logger;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.logging.LoggingFactory;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.consumer.dump.codec.TimedSQLEncoder;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.Task;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.TaskTypes;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.Diff;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.info.ArticleInformation;

/**
 * This class writes the output to a file while collecting statistical
 * information.
 *
 *
 *
 */
public class TimedSQLFileWriter
	extends SQLFileWriter
{

	/** Reference to the logger */
	private final Logger outputLogger;

	/**
	 * Temporary variable - used for storing the time needed to encode a task
	 */
	private long processingTimeSQL;

	/** Reference to the sql encoder */
	private TimedSQLEncoder sqlEncoder;

	/**
	 * (Constructor) Creates a new TimedSQLFileWriter object.
	 *
	 * @param outputName
	 *            Name of the sql consumer
	 * @param logger
	 *            Reference to a logger
	 *
	 * @throws ConfigurationException
	 *             if an error occurred while accessing the configuration
	 * @throws LoggingException
	 *             if an error occurred while accessing the logger
	 */
	public TimedSQLFileWriter(final String outputName, final Logger logger)
		throws IOException, ConfigurationException, LoggingException
	{

		super(outputName, logger);
		this.outputLogger = LoggingFactory
				.getLogger(LoggingFactory.NAME_ARTICLE_OUTPUT_LOGGER);
	}


	/*--------------------------------------------------------------------------*/

	/**
	 * Creates the sql encoder.
	 *
	 * @throws ConfigurationException
	 *             if an error occurred while accessing the configuration
	 * @throws LoggingException
	 *             if an error occurred while accessing the logger
	 */
	@Override
	protected void init()
		throws ConfigurationException, LoggingException
	{

		this.sqlEncoder = new TimedSQLEncoder(logger);
		super.sqlEncoder = this.sqlEncoder;
	}

	/**
	 * This method will process the given DiffTask and send him to the specified
	 * output.
	 *
	 * @param task
	 *            DiffTask
	 *
	 * @throws ConfigurationException
	 *             if problems occurred while initializing the components
	 *
	 * @throws IOException
	 *             if problems occurred while writing the output (to file or
	 *             archive)
	 *
	 * @throws SQLConsumerException
	 *             if problems occurred while writing the output (to the sql
	 *             producer database)
	 */
	@Override
	public void process(final Task<Diff> task)
		throws ConfigurationException, IOException, SQLConsumerException
	{

		long startTime = System.currentTimeMillis();

		TaskTypes type = task.getTaskType();

		if (type == TaskTypes.TASK_FULL || type == TaskTypes.TASK_PARTIAL_FIRST) {

			this.sqlEncoder.init();
			this.processingTimeSQL = 0;
		}

		super.process(task);

		this.processingTimeSQL += System.currentTimeMillis() - startTime;

		if (type == TaskTypes.TASK_FULL || type == TaskTypes.TASK_PARTIAL_LAST) {

			ArticleInformation info = task.getHeader();
			info.setEncodedSize(this.sqlEncoder.getEncodedSize());
			info.setEncodedSQLSize(this.sqlEncoder.getEncodedSQLSize());
			info.setExitingTime(System.currentTimeMillis());
			info.setProcessingTimeSQL(processingTimeSQL);

			String succesReport = info.toString();
			// System.out.println(succesReport);
			this.outputLogger.logMessage(Level.INFO, "\r\n" + succesReport);
		}
	}

	/**
	 * This method will close the connection to the output.
	 *
	 * @throws IOException
	 *             if problems occurred while closing the file or process.
	 *
	 */
	@Override
	public void close()
		throws IOException
	{
		try {
			super.close();
		}
		finally {
			this.outputLogger.close();
		}
	}
}
