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

package net.mcsproject.master.database.utils;

import lombok.extern.log4j.Log4j2;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public abstract class ConnectionPool implements Closeable {

	private ConcurrentLinkedQueue<Connection> pool;
	private ScheduledExecutorService executorService;

	public ConnectionPool(final int min, final int max, final long validationInterval) {
		initialize(min);
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleWithFixedDelay(() -> {
			int size = pool.size();
			if (size < min) {
				int sizeToBeAdded = min - size;
				for (int i = 0; i < sizeToBeAdded; i++) {
					pool.add(createConnection());
				}
			} else if (size > max) {
				int sizeToBeRemoved = size - max;
				for (int i = 0; i < sizeToBeRemoved; i++) {
					try {
						pool.poll().close();
					} catch (SQLException e) {
						log.warn(e);
					}
				}
			}
		}, validationInterval, validationInterval, TimeUnit.MILLISECONDS);
	}

	public Connection borrowConnection() {
		Connection con;
		if ((con = pool.poll()) == null) {
			con = createConnection();
		}
		return con;
	}

	public void returnConnection(Connection con) {
		try {
			if (con == null || con.isClosed()) {
				return;
			}
		} catch (SQLException e) {
			log.warn(e);
		}
		this.pool.offer(con);
	}

	@Override
	public void close() {
		executorService.shutdown();
		while (pool.size() > 0) {
			try {
				Connection con = pool.poll();
				con.close();
			} catch (SQLException e) {
				log.warn(e);
			}
		}
	}

	private void initialize(final int minIdle) {
		pool = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < minIdle; i++) {
			pool.add(createConnection());
		}
	}

	protected abstract Connection createConnection();
}