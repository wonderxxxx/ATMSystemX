package com.zhu;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class NetLink {

    Socket socket = null;

    public Socket getSocket() {
        return socket;
    }

    public NetLink() throws IOException {

    }
}
