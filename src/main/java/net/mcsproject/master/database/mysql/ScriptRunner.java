/*
 *     MCS - Minecraft Cloud System
 *     Copyright (C) 2016
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.mcsproject.master.database.mysql;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.SQLException;

@Log4j2
class ScriptRunner {

	private MySQLExecutor mysqlExecutor;

	ScriptRunner(MySQLExecutor mysqlExecutor) {
		this.mysqlExecutor = mysqlExecutor;
	}

	void runScript(InputStream inputStream) throws IOException, SQLException {
		runScript(new LineNumberReader(new InputStreamReader(inputStream)));
	}

	private void runScript(LineNumberReader lineReader) throws IOException, SQLException {
		StringBuilder command = null;
		String line;
		while ((line = lineReader.readLine()) != null) {
			if (command == null) {
				command = new StringBuilder();
			}
			String trimmed = line.trim();

			if (trimmed.startsWith("--") || trimmed.startsWith("#") || trimmed.length() < 1) {
				continue;
			}

			command.append(line);

			if (trimmed.endsWith(";")) {
				execCommand(command);
				command = null;
			}
		}
	}

	private void execCommand(StringBuilder command) throws SQLException {
		mysqlExecutor.asyncStatement(mysqlExecutor.createPreparedStatement(command.toString()));
	}
}
