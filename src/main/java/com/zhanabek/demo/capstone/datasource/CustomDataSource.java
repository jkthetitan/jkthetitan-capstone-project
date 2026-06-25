package com.zhanabek.demo.capstone.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class CustomDataSource implements DataSource, AutoCloseable {

    private List<Connection> connectionPool;
    private BlockingQueue<Connection> availableConnections;

    private String url;
    private String username;
    private String password;
    private int size;
    private int timeout;

    private PrintWriter printWriter;

    public CustomDataSource(String url, String username, String password, int size, int timeout) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.size = size;

        this.availableConnections = new ArrayBlockingQueue<>(size);
        this.connectionPool = new ArrayList<>(size);

        DriverManager.setLoginTimeout(timeout);
        for (int i = 0; i < size; ++i) {
            Connection realConnection = DriverManager.getConnection(url, username, password);
            Connection proxyConnection = createProxyConnection(realConnection);
            this.availableConnections.add(proxyConnection);
            this.connectionPool.add(realConnection);
        }
    }

    CustomDataSource(Supplier<Connection> factory, int size, int timeout) {
        this.size = size;
        this.timeout = timeout;
        this.availableConnections = new ArrayBlockingQueue<>(size);
        this.connectionPool = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            Connection realConnection = factory.get();
            Connection proxyConnection = createProxyConnection(realConnection);
            this.connectionPool.add(realConnection);
            this.availableConnections.add(proxyConnection);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
             Connection connection = availableConnections.poll(timeout, TimeUnit.SECONDS);
             if (connection == null) {
                 throw new SQLException("Connection timeout occured");
             } else {
                return connection;
             }
        } catch (InterruptedException e) {
            throw new SQLException("Thread interrupted while waiting for a connection");
        }
    }

    @Override
    public Connection getConnection(String s, String s1) throws SQLException {
        throw new SQLFeatureNotSupportedException("getConnection(user, password) not supported by this pool.");
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return printWriter;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        this.printWriter = printWriter;
    }

    @Override
    public void setLoginTimeout(int i) throws SQLException {
        timeout = i;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return timeout;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Logging not supported.");
    }

    @Override
    public <T> T unwrap(Class<T> aClass) throws SQLException {
        if (isWrapperFor(aClass)) {
            return aClass.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + aClass.getName());
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return aClass.isInstance(this);
    }

    @Override
    public void close() throws Exception {
        for (Connection realConnection : connectionPool) {
            realConnection.close();
        }
    }

    private Connection createProxyConnection(Connection realConnection) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("close".equals(method.getName())) {
                        availableConnections.add((Connection) proxy);
                        return null;
                    }
                    return method.invoke(realConnection, args);
                });
    }

}
