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

import com.sun.rowset.CachedRowSetImpl;
import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.configuration.database.MySQLConfig;
import net.mcsproject.master.database.utils.ConnectionPool;

import javax.sql.rowset.CachedRowSet;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

@Log4j2
public class MySQLExecutor implements Closeable{
    private ConnectionPool pool;
    private UpdateQueue updateQueue;

    private ScriptRunner scriptRunner;

    MySQLExecutor(MySQLConfig mysqlConfig){
        pool = new ConnectionPool(mysqlConfig.getMinOpenConnections(), mysqlConfig.getMaxOpenConnections(), mysqlConfig.getValidationInterval()) {
            @Override
            protected Connection createConnection() {
                try {
                    return DriverManager.getConnection("jdbc:mysql://" + mysqlConfig.getIp() + ":" + mysqlConfig.getPort() + "/" + mysqlConfig.getDb(), mysqlConfig.getUser(), mysqlConfig.getPw());
                } catch (SQLException e) {
                    log.fatal(e);
                    //ToDo stop masterserver
                }
                return null;
            }
        };
        scriptRunner = new ScriptRunner(this);
        updateQueue = new UpdateQueue(pool);
    }

    public CachedRowSet syncRequest(PreparedStatement stmt) throws SQLException{
        Connection con = this.pool.borrowConnection();
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        cachedRowSet.populate(stmt.executeQuery());
        stmt.close();
        pool.returnConnection(con);
        return cachedRowSet;
    }

    public Thread asyncRequest(PreparedStatement stmt, Consumer<CachedRowSet> consumer) {
        Thread thread = new Thread(() -> {
            try {
                CachedRowSet cachedRowSet = syncRequest(stmt);
                consumer.accept(cachedRowSet);
            } catch (SQLException e) {
                log.warn(e);
            }
        });
        thread.setName("DatabaseRequest Thread");
        thread.run();
        return thread;
    }

    public PreparedStatement createPreparedStatement(String qry) throws SQLException{
        Connection connection = pool.borrowConnection();
        return connection.prepareStatement(qry);
    }

    public void asyncStatement(PreparedStatement stmt) throws SQLException{
        updateQueue.addUpdate(stmt);
    }

    public void runScript(InputStream inputStream) throws IOException, SQLException {
        scriptRunner.runScript(inputStream);
    }

    @Override
    public void close() {
        updateQueue.close();
        pool.close();
    }
}
