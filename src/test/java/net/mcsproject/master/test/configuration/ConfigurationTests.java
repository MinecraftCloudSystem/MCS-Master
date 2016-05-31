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

package net.mcsproject.master.test.configuration;

import lombok.extern.log4j.Log4j2;
import net.mcsproject.master.Arguments;
import net.mcsproject.master.configuration.Config;
import net.mcsproject.master.configuration.Configuration;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

@Log4j2
@RunWith(PowerMockRunner.class)
@PrepareForTest(Arguments.class)
@PowerMockIgnore("javax.management.*") //ToDo Fix it, that it's possible without this
public class ConfigurationTests {

    private Configuration configuration;

    private File configFile = new File("target" + File.pathSeparator + "test" + File.pathSeparator + "testconfig.json");

    private Config config;
    @Before
    public void setUp(){
        Arguments mockArguments = PowerMockito.mock(Arguments.class);
        PowerMockito.when(mockArguments.getConfigFile()).thenReturn(configFile);

        PowerMockito.mockStatic(Arguments.class);
        PowerMockito.when(Arguments.getInstance()).thenReturn(mockArguments);

        configuration = new Configuration();
        config = new Config();
        config.setInternalPort(80);
    }

    @Test
    public void writeAndReadConfiguration_CorrectlyWriteAndRead() {
        configuration.writeConfiguration(config);
        configuration.readConfiguration();
        Assert.assertEquals(config, configuration.getConfig());
    }

    @After
    public void completion(){
        if(!configFile.delete())
            log.warn(configFile + " cannot delete");
    }
}
