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

package net.mcsproject.master.test.log4j;

import net.mcsproject.master.libs.log4j.LoggerStream;
import net.mcsproject.master.libs.log4j.OutErrLogger;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Test;

import java.io.PrintStream;

public class OutErrLoggerTests {

    @Test
    public void SetOutAndErrToLog_NewPrintWriterSystemOutErr(){
        OutErrLogger.setOutAndErrToLog();
        try {
            Assert.assertTrue(PrintStream.class.getDeclaredField("out").get(System.out).equals(new LoggerStream(Level.INFO)));
            Assert.assertTrue(PrintStream.class.getDeclaredField("out").get(System.err).equals(new LoggerStream(Level.ERROR)));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }
}
