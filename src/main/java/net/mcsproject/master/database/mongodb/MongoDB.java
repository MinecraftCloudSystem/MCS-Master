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

package net.mcsproject.master.database.mongodb;

import net.mcsproject.master.configuration.database.MongoDBConfig;
import net.mcsproject.master.database.Database;
import net.mcsproject.master.database.objects.User;

public class MongoDB implements Database {
    public MongoDB(MongoDBConfig mongoDBConfig) {
        super();
    }

    @Override
    public void createUser(User user) throws Exception {

    }

    @Override
    public boolean checkUser(User user) throws Exception {
        return false;
    }

    @Override
    public void install() {

    }

    @Override
    public void load() {

    }

    @Override
    public void close() {

    }
}
