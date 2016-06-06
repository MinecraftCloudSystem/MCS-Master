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

package net.mcsproject.master.installation;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.configuration.database.DatabaseConfig;
import net.mcsproject.master.configuration.database.MongoDBConfig;
import net.mcsproject.master.configuration.database.MySQLConfig;
import net.mcsproject.master.utils.Container;
import net.mcsproject.master.utils.ConvertUtils;
import net.mcsproject.master.utils.MongoDBConnectionTest;
import net.mcsproject.master.utils.MySQLConnectionTest;

import java.io.Console;

@Log4j2
class DatabaseInstallation {
    DatabaseConfig run(){
        DatabaseConfig databaseConfig = null;
        do {
            log.info("Please choose your database management system (MySQL/MongoDB)");
            switch (System.console().readLine().toLowerCase()) {
                case "mysql":
                    databaseConfig = mysqlInstall();
                    break;
                case "mongodb":
                    databaseConfig = mongodbInstall();
                    break;
            }
        } while (databaseConfig == null);
        return databaseConfig;
    }

    private MySQLConfig mysqlInstall(){
        MySQLConfig mysqlConfig;
        String ip;
        String port;
        String db;
        String user;
        String pw;

        do{
            Console console = System.console();
            log.info("Please enter the ip for MySQL");
            ip = console.readLine();

            Container<String, Integer> container;
            do {
                log.info("Please enter the Port for MySQL");
                container = new Container<>(console.readLine());
            } while (!ConvertUtils.tryParse(container));

            port = container.getResult().toString();

            log.info("Please enter the database");
            db = console.readLine();

            log.info("Please enter the user");
            user = console.readLine();

            log.info("Please enter the Password");
            pw = new String(console.readPassword());
            mysqlConfig = new MySQLConfig();
            mysqlConfig.setIp(ip);
            mysqlConfig.setPort(port);
            mysqlConfig.setDb(db);
            mysqlConfig.setUser(user);
            mysqlConfig.setPw(pw);
            log.info("Please wait");
        } while (!MySQLConnectionTest.connectionTest(mysqlConfig));
        return mysqlConfig;
    }

    private MongoDBConfig mongodbInstall(){
        MongoDBConfig mongoDBConfig;
        String ip;
        String port;

        do{
            Console console = System.console();
            log.info("Please enter the ip for MongoDB");
            ip = console.readLine();

            log.info("Please enter the port");
            port = console.readLine();

            mongoDBConfig = new MongoDBConfig();
            mongoDBConfig.setIp(ip);
            mongoDBConfig.setPort(port);
            log.info("Please wait");
        } while (!MongoDBConnectionTest.connectionTest(mongoDBConfig));
        return mongoDBConfig;
    }
}
