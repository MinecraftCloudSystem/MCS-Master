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

package net.mcsproject.master.database.mysql.relations;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.MasterServer;
import net.mcsproject.master.database.mysql.MySQLExecutor;
import net.mcsproject.master.database.mysql.RelationVersions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j2
abstract class Relation {

    String resources;
    String name;
    MySQLExecutor executor;
    RelationVersions relationVersions;

    Relation(String name, MySQLExecutor executor){
        this(name,executor,null);
    }

    Relation(String name, MySQLExecutor executor, RelationVersions relationVersions){
        this.name = name;
        this.executor = executor;
        this.resources = "mysql/" + name;
        this.relationVersions = relationVersions;
    }

    private InputStream getResourceInputStream(String name){
        return ClassLoader.getSystemResourceAsStream(resources + "/" + name);
    }

    public void createTable(){
        try {
            executor.runScript(getResourceInputStream("create.sql"));
        } catch (IOException | SQLException e) {
            log.fatal(e);
            MasterServer.getInstance().stopServer(101);
        }
    }

    public void checkUpdates(){
        if(relationVersions == null){
            log.warn("Relation " + name + " can not update");
            return;
        }
        try {
            PreparedStatement stmt = executor.createPreparedStatement("SELECT version FROM version WHERE relation = ?;");
            stmt.setString(1, name);
            executor.asyncRequest(stmt, cachedRowSet -> {
                try {
                    cachedRowSet.next();
                    String version = cachedRowSet.getString("version");
                    if(!relationVersions.getLatestVersion().equalsIgnoreCase(version)){
                        String[] newVersions = relationVersions.getAllUpdates(version);
                        for (String versions : newVersions){
                            try {
                                executor.runScript(getResourceInputStream(versions + ".sql"));
                            } catch (IOException e) {
                                log.fatal(e);
                                MasterServer.getInstance().stopServer(105);
                            }
                        }
                    }
                } catch (SQLException e) {
                    log.fatal(e);
                    MasterServer.getInstance().stopServer(104);
                }
            });
        } catch (SQLException e) {
            log.fatal(e);
            MasterServer.getInstance().stopServer(103);
        }
    }
}
