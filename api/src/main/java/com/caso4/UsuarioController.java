package com.caso4;

import java.util.Random;

import javax.persistence.EntityManager;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@Controller("/usuarios")
public class UsuarioController {

    @Inject
    private EntityManager entityManager;

    @Post("/crearUsuarios/")
    @Transactional
    public HttpResponse<String> crearUsuarios() {
        Random random = new Random();

        for (int i = 1; i <= 60000; i++) {
            String nombre = "Usuario" + i;
            String correo = "usuario" + i + "@hotmail.com";
            String telefono = generarTelefonoAleatorio(random);

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setCorreo(correo);
            usuario.setTelefono(telefono);

            entityManager.persist(usuario);
        }

        return HttpResponse.ok("usuarios creados con extio");
    }

    private String generarTelefonoAleatorio(Random random) {
        return String.format("%010d", random.nextInt(1_000_000));
    }
}