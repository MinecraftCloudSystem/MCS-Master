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

package net.mcsproject.master.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

public class Logging {
    static void disableMongoDBLogging(){
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration conf = ctx.getConfiguration();
        conf.getLoggerConfig("org.mongodb.driver.connection").setLevel(Level.OFF);
        conf.getLoggerConfig("org.mongodb.driver.management").setLevel(Level.OFF);
        conf.getLoggerConfig("org.mongodb.driver.cluster").setLevel(Level.OFF);
        conf.getLoggerConfig("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
        conf.getLoggerConfig("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
        conf.getLoggerConfig("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
        ctx.updateLoggers(conf);
    }

    static void enableMongoDBLogging() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration conf = ctx.getConfiguration();
        conf.getLoggerConfig("org.mongodb.driver.connection").setLevel(Level.INFO);
        conf.getLoggerConfig("org.mongodb.driver.management").setLevel(Level.INFO);
        conf.getLoggerConfig("org.mongodb.driver.cluster").setLevel(Level.INFO);
        conf.getLoggerConfig("org.mongodb.driver.protocol.insert").setLevel(Level.INFO);
        conf.getLoggerConfig("org.mongodb.driver.protocol.query").setLevel(Level.INFO);
        conf.getLoggerConfig("org.mongodb.driver.protocol.update").setLevel(Level.INFO);
        ctx.updateLoggers(conf);
    }


    public static void enableDebug() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration conf = ctx.getConfiguration();
        conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(Level.DEBUG);
        ctx.updateLoggers(conf);
    }
}
