package ru.eltex.magnus.streamer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


public class PropertiesManager {

    private static final String SERVER_ADDRESS_PROPERTY = "server.address";
    private static final String SERVER_PORT_PROPERTY = "server.port";
    private static final String LOGIN_PROPERTY = "user.login";
    private static final String PASSWORD_PROPERTY = "user.password";

    private final String fileName;
    private String serverAddress;
    private int serverPort;
    private String login;
    private String password;

    public PropertiesManager(String fileName) {
        this.fileName = fileName;
    }

    public boolean load() {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader(fileName)) {
            prop.load(reader);
        } catch (IOException e) {
            System.out.println("Failed to read config file: " + fileName);
            return false;
        }
        setServerAddress(prop.getProperty(SERVER_ADDRESS_PROPERTY, ""));
        setServerPort(Integer.parseInt(prop.getProperty(SERVER_PORT_PROPERTY, "0")));
        setLogin(prop.getProperty(LOGIN_PROPERTY, ""));
        setPassword(prop.getProperty(PASSWORD_PROPERTY, ""));
        return true;
    }

    public boolean save() {
        Properties prop = new Properties();
        prop.setProperty(SERVER_ADDRESS_PROPERTY, getServerAddress());
        prop.setProperty(SERVER_PORT_PROPERTY, String.valueOf(getServerPort()));
        prop.setProperty(LOGIN_PROPERTY, getLogin());
        prop.setProperty(PASSWORD_PROPERTY, getPassword());

        try (FileWriter writer = new FileWriter(fileName)) {
            prop.store(writer, "");
        } catch (IOException e) {
            System.out.println("Failed to write config file: " + fileName);
            return false;
        }
        return true;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
