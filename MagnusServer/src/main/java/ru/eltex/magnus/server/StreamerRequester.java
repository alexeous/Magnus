package ru.eltex.magnus.server;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class StreamerRequester {

    private static final int MAX_BUFFER_SIZE = 2 << 18;

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private String login;

    StreamerRequester(Socket socket, DataInputStream inputStream, DataOutputStream outputStream, String login) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public synchronized byte[] takeScreenshot() {
        try {
            String command = "screenshot";
            sendToStreamer(command.getBytes());
            byte[] bytes = readFromStreamer();
            return bytes != null ? bytes : new byte[0];
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public boolean checkStreamerConnection() {
        try {
            if (socket.isClosed()) return false;
            String command = "checkup";
            sendToStreamer(command.getBytes());
            byte[] bytes = readFromStreamer();
            return bytes != null && "connected".equals(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("catched");
            return false;
        }
    }

    private void sendToStreamer(byte[] data) throws IOException {
        outputStream.writeInt(data.length);
        outputStream.flush();
        outputStream.write(data);
        outputStream.flush();
    }

    private synchronized byte[] readFromStreamer() throws IOException {
        int size = inputStream.readInt();
        System.out.println(size);
        if (size < 0 || size > MAX_BUFFER_SIZE) {
            System.err.println("Unacceptable message size: " + size);
            return null;
        }

        byte[] buffer = new byte[size];
        int actualSize = inputStream.read(buffer, 0, size);
        if (size != actualSize) {
            System.err.println("Expected " + size + "bytes but got " + actualSize);
            return null;
        }
        return buffer;
    }
}