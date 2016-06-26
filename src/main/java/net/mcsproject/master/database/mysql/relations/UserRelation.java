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

import net.mcsproject.master.database.mysql.MySQLExecutor;
import net.mcsproject.master.database.mysql.RelationVersions;
import net.mcsproject.master.database.objects.User;

import javax.sql.rowset.CachedRowSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UserRelation extends Relation {

    public static String NAME = "user";

    public UserRelation(MySQLExecutor executor) {
        super(NAME, executor, RelationVersions.USER);
    }

    public void addUser(User user) throws SQLException {
        PreparedStatement stmt = executor.createPreparedStatement("INSERT INTO user VALUES (?, ?);");
        stmt.setString(1, user.getUsername());
        stmt.setBytes(2, user.getPasswordHash());
        executor.asyncStatement(stmt);
    }

    public boolean checkUser(User user) throws SQLException {
        PreparedStatement stmt = executor.createPreparedStatement("SELECT COUNT(*) FROM user WHERE username = ? AND pw = ?;");
        stmt.setString(1, user.getUsername());
        stmt.setBytes(2, user.getPasswordHash());
        CachedRowSet cachedRowSet = executor.syncRequest(stmt);
        return cachedRowSet.getBoolean(1);
    }
}
