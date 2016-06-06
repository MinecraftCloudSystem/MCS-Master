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

package net.mcsproject.master.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.Arguments;
import net.mcsproject.master.configuration.database.DatabaseConfig;
import net.mcsproject.master.libs.gson.DataBaseConfigDeserialize;
import net.mcsproject.master.libs.gson.DataBaseConfigSerialize;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Log4j2
public class Configuration {

    private File configFile;
    @Getter(AccessLevel.PUBLIC)
    private Config config;

    private Gson gson;

    public Configuration() {
        this.configFile = Arguments.getInstance().getConfigFile();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DatabaseConfig.class, new DataBaseConfigSerialize())
                .registerTypeAdapter(DatabaseConfig.class, new DataBaseConfigDeserialize())
                .create();
    }

    public boolean exists(){
        return configFile.exists();
    }

    public void writeConfiguration() {
        writeConfiguration(config);
    }

    public void writeConfiguration(Config config) {
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            gson.toJson(config, fileWriter);
        } catch (IOException e) {
            log.fatal(e);
        }
    }

    public void readConfiguration() {
        try (FileReader fileReader = new FileReader(configFile)) {
            config = gson.fromJson(fileReader, Config.class);
        } catch (IOException e) {
            log.fatal(e);
        }
    }
}
