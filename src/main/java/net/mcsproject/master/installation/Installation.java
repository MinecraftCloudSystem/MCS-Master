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

package net.mcsproject.master.installation;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.configuration.Config;
import net.mcsproject.master.configuration.Configuration;
import net.mcsproject.master.configuration.database.DatabaseConfig;
import net.mcsproject.master.utils.Container;
import net.mcsproject.master.utils.ConvertUtils;

import java.io.Console;

@Log4j2
public class Installation {

	private Console console;

	public Installation() {
		console = System.console();
	}

	public void run(Configuration configuration) {
		Config config = new Config();

		Container<String, Integer> internalPort;
		DatabaseConfig databaseConfig;

		log.info("Welcome to installation");

		do {
			log.info("Please enter the internal Port. The port must be between 1024 and 49151 (27755)");
			internalPort = new Container<>(console.readLine());
		}
		while (!ConvertUtils.tryParse(internalPort) || internalPort.getResult() < 1024 || internalPort.getResult() > 49151);

		databaseConfig = new DatabaseInstallation().run();

		config.setInternalPort(internalPort.getResult());
		config.setDatabaseConfig(databaseConfig);

		configuration.writeConfiguration(config);
	}
}
