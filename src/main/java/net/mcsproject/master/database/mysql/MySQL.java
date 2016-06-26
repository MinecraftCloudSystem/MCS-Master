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
import net.mcsproject.master.configuration.database.MySQLConfig;
import net.mcsproject.master.database.Database;
import net.mcsproject.master.database.mysql.relations.UserRelation;
import net.mcsproject.master.database.mysql.relations.VersionRelation;
import net.mcsproject.master.database.objects.User;

import java.sql.SQLException;

@Log4j2
public class MySQL implements Database {
	private MySQLExecutor executor;

    private VersionRelation versionRelation;
    private UserRelation userRelation;

    public MySQL(MySQLConfig mysqlConfig) {
        executor = new MySQLExecutor(mysqlConfig);
    }

    @Override
    public void createUser(User user) throws SQLException {
        userRelation.addUser(user);
    }

    @Override
    public boolean checkUser(User user) throws SQLException {
        return userRelation.checkUser(user);
    }

    @Override
    public void install() {
        versionRelation = new VersionRelation(executor);
        versionRelation.createTable();
        userRelation = new UserRelation(executor);
        userRelation.createTable();
    }

    @Override
    public void load() {
        versionRelation = new VersionRelation(executor);
        userRelation = new UserRelation(executor);
        userRelation.checkUpdates();
    }

    @Override
    public void close() {
        executor.close();
    }
}
