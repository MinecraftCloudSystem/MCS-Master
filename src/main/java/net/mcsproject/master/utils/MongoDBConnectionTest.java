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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.configuration.database.MongoDBConfig;

@Log4j2
public class MongoDBConnectionTest {
	public static boolean connectionTest(MongoDBConfig mongoDBConfig) {
		Logging.disableMongoDBLogging();
		boolean success = true;
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient(new MongoClientURI("mongodb://" + mongoDBConfig.getIp() + ":" + mongoDBConfig.getPort()));
			mongoClient.getDatabaseNames();
		} catch (MongoException e) {
			success = false;
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
			Logging.enableMongoDBLogging();
		}
		return success;
	}
}
