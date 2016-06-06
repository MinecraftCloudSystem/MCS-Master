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

package net.mcsproject.master.test.configuration.database;

import net.mcsproject.master.configuration.database.DatabaseConfig;
import net.mcsproject.master.configuration.database.MongoDBConfig;
import net.mcsproject.master.configuration.database.MySQLConfig;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseConfigTests {

    @Test(expected = IllegalArgumentException.class)
    public void getClassForString_throwIllegalArgumentException(){
        DatabaseConfig.getClassForString(".");
    }

    @Test()
    public void getClassForString_getMongoDBConfigClass(){
        Assert.assertEquals(DatabaseConfig.getClassForString("MongoDB"), MongoDBConfig.class);
    }

    @Test()
    public void getClassForString_getMySQLConfigClass(){
        Assert.assertEquals(DatabaseConfig.getClassForString("MySQL"), MySQLConfig.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getStringForDatabaseConfig_throwIllegalArgumentException(){
        DatabaseConfig.getStringForDatabaseConfig(new DatabaseConfig() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
    }

    @Test()
    public void getStringForDatabaseConfig_getMongoDB(){
        Assert.assertEquals(DatabaseConfig.getStringForDatabaseConfig(new MongoDBConfig()), "MongoDB");
    }

    @Test()
    public void getStringForDatabaseConfig_getMySQL(){
        Assert.assertEquals(DatabaseConfig.getStringForDatabaseConfig(new MySQLConfig()), "MySQL");
    }
}
