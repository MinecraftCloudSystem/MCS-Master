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

package net.mcsproject.master.test;

import net.mcsproject.master.Arguments;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ArgumentsTests {

    @Test
    public void Arguments_FieldsInitializeCorrectWithArguments() {
        String[] args = {
                "-debug",
                "-config",
                "settings.json"
        };
        Arguments arguments = new Arguments(args);
        Assert.assertEquals(arguments.isDebug(), true);
        Assert.assertEquals(arguments.getConfigFile(), new File("settings.json"));
    }

    @Test
    public void Arguments_FieldsInitializeCorrectWithoutArguments() {
        String[] args = {};
        Arguments arguments = new Arguments(args);
        Assert.assertEquals(arguments.isDebug(), false);
        Assert.assertEquals(arguments.getConfigFile(), new File("config.json"));
    }


}
