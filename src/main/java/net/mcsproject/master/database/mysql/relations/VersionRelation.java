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
import net.mcsproject.master.database.mysql.MySQLExecutor;
import net.mcsproject.master.database.utils.ConnectionPool;

@Log4j2
public class VersionRelation extends Relation {

	public static String NAME = "version";

	private ConnectionPool pool;

	private String version;

    public VersionRelation(MySQLExecutor executor){
        super(NAME, executor);
    }
}
