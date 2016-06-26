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

package net.mcsproject.master;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import lombok.AccessLevel;
import lombok.Getter;
import net.mcsproject.master.libs.jcommander.FileConverter;

import java.io.File;

public class Arguments {

	private static Arguments arguments;

	@Getter(AccessLevel.PUBLIC)
	private JCommander jCommander;
	@Getter(AccessLevel.PUBLIC)
	@Parameter(names = "-debug", description = "Debug mode")
	private boolean debug = false;
	@Getter(AccessLevel.PUBLIC)
	@Parameter(names = "-config", converter = FileConverter.class, description = "Path to config.json")
	private File configFile = new File("config.json");

	public Arguments(String[] args) {
		arguments = this;
		jCommander = new JCommander(this, args);
	}

	public static Arguments getInstance() {
		return arguments;
	}
}
