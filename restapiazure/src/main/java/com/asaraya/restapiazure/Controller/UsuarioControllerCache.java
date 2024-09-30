package com.asaraya.restapiazure.Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import redis.clients.jedis.Jedis;

@RestController
public class UsuarioControllerCache {

    private static HikariDataSource dataSource;
    private static Jedis jedis;
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlserver://sqldb-caso4-server.database.windows.net:1433;database=Caso4DB;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        config.setUsername("asaraya@sqldb-caso4-server");
        config.setPassword("Guachin321!");
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

        jedis = new Jedis("localhost", 6379);
    }

    @RequestMapping(value = "Get35WithCache", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> allUsersWithCache(@RequestParam String cacheKey) {
        List<Usuario> response = new ArrayList<>();
        Usuario usuario = new Usuario();

        if (jedis.exists(cacheKey)) {
            String cachedResponse = jedis.get(cacheKey);
            try {
                response = objectMapper.readValue(cachedResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Usuario.class));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String SQL = "WITH UserSubset AS ( " +
                         "SELECT *, ROW_NUMBER() OVER (ORDER BY id) AS RowNum FROM Users ) " +
                         "SELECT * FROM UserSubset WHERE RowNum <= (SELECT CAST(COUNT(*) * 0.35 AS INT) FROM Users)";
            ResultSet result = statement.executeQuery(SQL);

            while (result.next()) {
                usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setNombre(result.getString("nombre"));
                usuario.setCorreo(result.getString("correo"));
                usuario.setTelefono(result.getString("telefono"));
                response.add(usuario);
            }
            connection.close();

            String jsonResponse = objectMapper.writeValueAsString(response);
            jedis.set(cacheKey, jsonResponse);

        } catch (SQLException | JsonProcessingException e) {
            usuario.setNombre(e.toString());
            response.add(usuario);
            return new ResponseEntity<List<Usuario>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<Usuario>>(response, HttpStatus.OK);
    }
}
