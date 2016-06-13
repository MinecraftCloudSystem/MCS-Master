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
import net.mcsproject.master.database.mysql.relations.VersionRelation;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class MySQL implements Database {
    private MySQLExecutor executor;

    private VersionRelation versionRelation;

    public MySQL(MySQLConfig mysqlConfig) {
        MySQLExecutor executor = new MySQLExecutor(mysqlConfig);
        ArrayList<String> existTables = new ArrayList<>();
        try {
            CachedRowSet cachedRowSet = executor.syncRequest(executor.createPreparedStatement("SHOW TABLES;"));

            while (cachedRowSet.next()){
                existTables.add(cachedRowSet.getString(1));
            }
        } catch (SQLException e) {
            log.fatal(e);
            //ToDo stop masterserver
        }

        versionRelation = new VersionRelation(executor, existTables.contains(VersionRelation.NAME));
    }

    public void close() {
        executor.close();
    }
}
