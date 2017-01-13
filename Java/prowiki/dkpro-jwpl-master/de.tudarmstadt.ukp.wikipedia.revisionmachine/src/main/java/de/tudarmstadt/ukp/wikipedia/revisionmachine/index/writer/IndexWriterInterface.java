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
package de.tudarmstadt.ukp.wikipedia.revisionmachine.index.writer;

import java.io.IOException;
import java.sql.SQLException;

import de.tudarmstadt.ukp.wikipedia.revisionmachine.index.indices.AbstractIndex;

/**
 * Interface for the IndexWriter
 *
 *
 *
 */
public interface IndexWriterInterface
{

	/**
	 * Writes the buffered finalzed queries to the output.
	 *
	 * @param index
	 *            Reference to an index
	 * @throws IOException
	 *             if an error occured while writing the output
	 * @throws SQLException
	 *             if an error occured while transmitting the output
	 */
	void write(final AbstractIndex index)
		throws IOException, SQLException;

	/**
	 * Closes the file or the database connection.
	 *
	 * @throws IOException
	 *             if an error occured while closing the file
	 * @throws SQLException
	 *             if an error occured while closing the database connection
	 */
	void close()
		throws IOException, SQLException;

	/**
	 * Wraps up the index generation process and writes all remaining statements
	 * e.g. concerning UNCOMPRESSED-Indexes on the created tables.
	 *
	 * @throws SQLException
	 *             if an error occured while accessing the database
	 * @throws IOException
	 *             if an error occured while accessing the sql file
	 */
	public void finish() throws IOException, SQLException;
}
