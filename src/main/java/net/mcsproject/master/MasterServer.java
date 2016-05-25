package net.mcsproject.master;

import net.mcsproject.master.logging.Log;
import net.mcsproject.master.network.DaemonServer;

import java.util.Scanner;

public class MasterServer {

    public MasterServer() {
        Log.info("Starting masterserver...");
        DaemonServer daemonServer = new DaemonServer(1337);

        Scanner scanner = new Scanner(System.in);
        scanner.next();
    }

    public static void main(String[] args) {
        new MasterServer();
    }

}
