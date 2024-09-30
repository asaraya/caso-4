package com.asaraya.restapiazure.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    @RequestMapping(value ="Get35", method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> allUsers() {
        List response = new ArrayList<Usuario>();
        Usuario usuario = new Usuario();
        String connectionURL = "jdbc:sqlserver://sqldb-caso4-server.database.windows.net:1433;database=Caso4DB;user=asaraya@sqldb-caso4-server;password=Guachin321!;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try {
            Connection connection = DriverManager.getConnection(connectionURL);
            Statement statement = connection.createStatement();
            //String SQL = "select * from Users";
            String SQL = "WITH UserSubset AS ( " +
                         "SELECT *, ROW_NUMBER() OVER (ORDER BY id) AS RowNum FROM Users ) " +
                         "SELECT * FROM UserSubset WHERE RowNum <= (SELECT CAST(COUNT(*) * 0.35 AS INT) FROM Users)";
            ResultSet result = statement.executeQuery(SQL);
           

            while(result.next()) {
                usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setNombre(result.getString("nombre"));
                usuario.setCorreo(result.getString("correo"));
                usuario.setTelefono(result.getString("telefono"));
                response.add(usuario);
            }
            connection.close();
            
        } catch (SQLException e) {
            usuario.setNombre(e.toString());
            response.add(usuario);
            return new ResponseEntity<List<Usuario>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<List<Usuario>>(response, HttpStatus.OK);
    }
}
 