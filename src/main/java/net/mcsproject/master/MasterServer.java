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

package net.mcsproject.master;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.configuration.Config;
import net.mcsproject.master.configuration.Configuration;
import net.mcsproject.master.database.Database;
import net.mcsproject.master.database.DatabaseFactory;
import net.mcsproject.master.installation.Installation;
import net.mcsproject.master.libs.log4j.OutErrLogger;
import net.mcsproject.master.network.DaemonServer;
import net.mcsproject.master.utils.Logging;
import net.mcsproject.master.web.ApiServer;

@Log4j2
public final class MasterServer {

	private static MasterServer instance;

	private Database database;

	private MasterServer(String[] args) {
		instance = this;
		if (System.console() == null) {
			log.error("Not supported console, use -console parameter");
			return;
		}
		OutErrLogger.setOutAndErrToLog();

		Arguments arguments = new Arguments(args);
		if (arguments.isDebug()) {
			enableDebug();
		}

		log.info("Starting masterserver...");

		Configuration configuration = new Configuration();

		if (!configuration.exists()) {
			new Installation().run(configuration);
		}
		configuration.readConfiguration();

		log.info("Loaded configuration successfully");

		Config config = configuration.getConfig();

		database = new DatabaseFactory(config.getDatabaseConfig()).create();
		database.load();

		DaemonServer daemonServer = new DaemonServer(config.getInternalPort());
		ApiServer apiServer = new ApiServer(1337);

		System.console().readLine();
	}

	public static void main(String[] args) {
		new MasterServer(args);
	}

	private void enableDebug() {
		Logging.enableDebug();
		log.info("Debugging mode is enabled");
	}

	public void stopServer(int status) {
		log.info("Server will be stop with status code " + status);
		if (database != null) {
			database.close();
		}
		System.exit(status);
	}

	public static MasterServer getInstance() {
		return instance;
	}
}
