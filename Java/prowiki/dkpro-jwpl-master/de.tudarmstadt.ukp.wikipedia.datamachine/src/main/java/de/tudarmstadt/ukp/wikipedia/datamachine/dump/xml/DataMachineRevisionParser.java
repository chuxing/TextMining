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
package de.tudarmstadt.ukp.wikipedia.datamachine.dump.xml;

import java.io.EOFException;
import java.io.IOException;

import de.tudarmstadt.ukp.wikipedia.wikimachine.dump.xml.RevisionParser;

public class DataMachineRevisionParser extends RevisionParser {

	public boolean next() throws IOException {
		boolean hasNext = true;
		try {
			revPage = stream.readInt();
			revTextId = stream.readInt();
		} catch (EOFException e) {
			hasNext = false;
		}
		return hasNext;
	}
}
