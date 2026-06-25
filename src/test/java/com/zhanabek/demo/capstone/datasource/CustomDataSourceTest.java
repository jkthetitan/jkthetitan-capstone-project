package com.zhanabek.demo.capstone.datasource;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomDataSourceTest {

    private CustomDataSource dataSource;
    private Connection mockConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        dataSource = new CustomDataSource(() -> mockConnection, 1, 1);
    }

    @Test
    @DisplayName("Should return connection when pool is not empty")
    void testGetConnectionSuccess() throws SQLException {
        Connection conn = dataSource.getConnection();
        assertNotNull(conn);
    }

    @Test
    @DisplayName("Should return connection to pool on close instead of physical closing")
    void testCloseReturnsToPool() throws Exception {
        Connection conn = dataSource.getConnection();
        conn.close();

        verify(mockConnection, never()).close();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    @DisplayName("Should throw SQLException on timeout when pool is exhausted")
    void testTimeout() throws SQLException {
        dataSource.getConnection();

        assertThrows(SQLException.class, () -> {
            dataSource.getConnection();
        });
    }

    @Test
    @DisplayName("Should close all physical connections on DataSource close")
    void testDataSourceClose() throws Exception {
        dataSource.close();
        verify(mockConnection, times(1)).close();
    }
}
