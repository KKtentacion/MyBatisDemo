package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCTest {
    public static void main(String[] args) throws Exception
    {
        HikariConfig config=new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("zfx535976386");
        config.addDataSourceProperty("connectionTimeout", "1000");
        config.addDataSourceProperty("idleTimeout", "60000");
        config.addDataSourceProperty("maximumPoolSize", "20");
        DataSource dataSource=new HikariDataSource(config);

        try(Connection conn=dataSource.getConnection())
        {
            try(PreparedStatement ps=conn.prepareStatement(""))
            {
                ps.setString();
                
            }
        }

    }
}

class Student
{
    public String name,gender,grade,score;
}
