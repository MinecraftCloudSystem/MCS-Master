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

package net.mcsproject.master.libs.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mcsproject.master.configuration.database.DatabaseConfig;

import java.lang.reflect.Type;

public class DataBaseConfigDeserialize implements JsonDeserializer<DatabaseConfig> {
	@Override
	public DatabaseConfig deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
		JsonObject jsonObj = jsonElement.getAsJsonObject();
		return jsonDeserializationContext.deserialize(jsonElement, DatabaseConfig.getClassForString(jsonObj.get("DBMS").getAsString()));
	}
}
