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
package de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.consumer.dump;

import java.io.IOException;
import java.sql.SQLException;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.ConfigurationException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.common.exceptions.SQLConsumerException;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.Task;
import de.tudarmstadt.ukp.wikipedia.revisionmachine.difftool.data.tasks.content.Diff;

/**
 * The WriterInterface symbolizes the link to the output writer.
 *
 *
 */
public interface WriterInterface
{

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
	void process(final Task<Diff> task)
		throws ConfigurationException, IOException, SQLConsumerException;

	/**
	 * This method will close the connection to the output.
	 *
	 * @throws IOException
	 *             if problems occurred while closing the file or process.
	 *
	 * @throws SQLException
	 *             if problems occurred while closing the connection to the
	 *             database.
	 */
	void close()
		throws IOException, SQLException;
}
