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

package net.mcsproject.master.database;

import net.mcsproject.master.configuration.Config;
import net.mcsproject.master.configuration.database.DatabaseConfig;
import net.mcsproject.master.configuration.database.MongoDBConfig;
import net.mcsproject.master.configuration.database.MySQLConfig;
import net.mcsproject.master.database.mysql.MySQL;

public class DatabaseFactory {

    private DatabaseConfig databaseConfig;

    public DatabaseFactory(Config config){
        databaseConfig = config.getDatabaseConfig();
    }

    public Database create(){
        if(databaseConfig instanceof MongoDBConfig){
            return new MongoDB((MongoDBConfig) databaseConfig);
        } else if(databaseConfig instanceof MySQLConfig){
            return new MySQL((MySQLConfig) databaseConfig);
        } else {
            throw new IllegalArgumentException("Not supported dbms");
        }
    }
}
