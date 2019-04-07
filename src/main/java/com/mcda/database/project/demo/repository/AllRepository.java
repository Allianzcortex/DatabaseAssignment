package com.mcda.database.project.demo.repository;

import com.zaxxer.hikari.util.DriverDataSource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class AllRepository {

    private JdbcTemplate jdbcTemplate;

    public AllRepository() {

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        System.out.println("=-=--------------------------");
        ds.setUrl("jdbc:mysql://localhost:3306/database_project");
        ds.setUsername("root");
        ds.setPassword("root");
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Transactional
    public List<String> findAllAvailableTable(){
        // Just hard-coded the database name
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema ='database_project';";
        return this.jdbcTemplate.query(query, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }

    @Transactional
    public List<Map<String, Object>> findAllTable(String tableName) {
        String query = "select * from " + tableName;
        return this.jdbcTemplate.queryForList(query);
    }



    @Transactional
    public List<String> findAllTableFields(String tableName) {
        String query = "select COLUMN_NAME " +
                "  from information_schema.columns " +
                " where table_schema ='" + "database_project" +
                "'   and table_name = '" + tableName + "'";

        return this.jdbcTemplate.query(query, new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }
}
