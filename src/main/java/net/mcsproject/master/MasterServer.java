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
import net.mcsproject.master.log4j.OutErrLogger;
import net.mcsproject.master.network.DaemonServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

import java.util.Scanner;

@Log4j2
public class MasterServer {

    private MasterServer(String[] args) {
        OutErrLogger.setOutAndErrToLog();

        Arguments startParameter = new Arguments(args);

        if (startParameter.isDebug())
            enableDebug();

        log.info("Starting masterserver...");

        DaemonServer daemonServer = new DaemonServer(1337);

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }

    public static void main(String[] args) {
        new MasterServer(args);
    }

    private void enableDebug() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration conf = ctx.getConfiguration();
        conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);
        ctx.updateLoggers(conf);
        log.info("Debugging mode is Enabled");
    }
}
