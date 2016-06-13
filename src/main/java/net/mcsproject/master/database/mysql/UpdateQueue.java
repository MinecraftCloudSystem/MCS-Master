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

/*
 *     This file is part of RealSQL.
 *
 *     RealSQL is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either mysql.version 3 of the License, or
 *     (at your option) any later mysql.version.
 *
 *     RealSQL is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 */

package net.mcsproject.master.database.mysql;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.database.utils.ConnectionPool;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Log4j2
class UpdateQueue implements Closeable, Runnable{

    private final BlockingQueue<PreparedStatement> updates;
    private ConnectionPool pool;
    private Thread thread;

    private boolean started;

    UpdateQueue(ConnectionPool pool) {
        this.updates = new ArrayBlockingQueue<>(12);
        this.pool = pool;
        thread = new Thread(this);
        thread.setName("MySQLUpdateQueue Thread");
        thread.start();
        started = true;
    }

    void addUpdate(PreparedStatement stmt) {
        this.updates.add(stmt);
    }

    public void run() {
        while (this.started) {
            try {
                PreparedStatement stmt;
                try {
                    stmt = this.getQueueObject();
                } catch (InterruptedException e) {
                    continue;
                }
                stmt.executeUpdate();
                Connection con = stmt.getConnection();
                stmt.close();
                pool.returnConnection(con);
            } catch (SQLException e) {
                log.warn(e);
            }
        }
    }

    private PreparedStatement getQueueObject() throws InterruptedException {
        return this.updates.take();
    }

    @Override
    public void close(){
        if(thread != null){
            this.thread.interrupt();
        }
        this.started = false;
    }
}
