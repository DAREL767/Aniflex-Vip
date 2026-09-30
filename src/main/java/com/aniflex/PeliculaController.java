package com.aniflex;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class PeliculaController {

    private final Map<String, Pelicula> peliculasMap = new ConcurrentHashMap<>();

    @QueryMapping
    public List<Pelicula> listarPeliculas() {
        return new ArrayList<>(peliculasMap.values());
    }

    @QueryMapping
    public Pelicula buscarPeliculaPorId(@Argument String id) {
        return peliculasMap.get(id);
    }

    @MutationMapping
    public Pelicula crearPelicula(@Argument PeliculaInput peliculaInput) {
        String id = peliculaInput.getId();

        // Validación de ID duplicado
        if (peliculasMap.containsKey(id)) {
            throw new RuntimeException("El ID '" + id + "' ya se encuentra registrado.");
        }

        Pelicula nuevaPelicula = Pelicula.builder()
                .id(id)
                .titulo(peliculaInput.getTitulo())
                .duracionMinutos(peliculaInput.getDuracionMinutos())
                .recaudacionTaquilla(peliculaInput.getRecaudacionTaquilla())
                .esSaga(peliculaInput.isEsSaga())
                .build();

        peliculasMap.put(id, nuevaPelicula);
        return nuevaPelicula;
    }

    @MutationMapping
    public Pelicula actualizarPelicula(@Argument String id, @Argument PeliculaInput peliculaInput) {
        if (!peliculasMap.containsKey(id)) {
            return null;
        }

        Pelicula peliculaActualizada = Pelicula.builder()
                .id(id)
                .titulo(peliculaInput.getTitulo())
                .duracionMinutos(peliculaInput.getDuracionMinutos())
                .recaudacionTaquilla(peliculaInput.getRecaudacionTaquilla())
                .esSaga(peliculaInput.isEsSaga())
                .build();

        peliculasMap.put(id, peliculaActualizada);
        return peliculaActualizada;
    }

    @MutationMapping
    public Boolean eliminarPelicula(@Argument String id) {
        return peliculasMap.remove(id) != null;
    }
}
