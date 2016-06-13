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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.database.mysql.MySQLExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@Log4j2
abstract class Relation {

    @Getter(AccessLevel.PUBLIC)
    protected String resources;

    private MySQLExecutor executor;

    Relation(MySQLExecutor executor, String resources, boolean exist){
        this.executor = executor;
        this.resources = resources;
        if(!exist){
            createTable();
        }
    }

    private void createTable(){
        try {
            executor.runScript(getResourceInputStream("create.sql"));
        } catch (IOException | SQLException e) {
            log.fatal(e);
            //ToDo stop masterserver
        }
    }

    private InputStream getResourceInputStream(String name){
        return ClassLoader.getSystemResourceAsStream(resources + "/" + name);
    }
}
