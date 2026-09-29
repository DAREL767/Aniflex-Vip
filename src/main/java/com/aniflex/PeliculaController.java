package com.aniflex;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Pelicula crearPelicula(@Argument String id,
                                  @Argument String titulo,
                                  @Argument int duracionMinutos,
                                  @Argument double recaudacionTaquilla,
                                  @Argument boolean esSaga) {

        Pelicula nuevaPelicula = Pelicula.builder()
                .id(id)
                .titulo(titulo)
                .duracionMinutos(duracionMinutos)
                .recaudacionTaquilla(recaudacionTaquilla)
                .esSaga(esSaga)
                .build();

        peliculasMap.put(id, nuevaPelicula);
        return nuevaPelicula;
    }

    @MutationMapping
    public Pelicula actualizarPelicula(@Argument String id,
                                       @Argument String titulo,
                                       @Argument int duracionMinutos,
                                       @Argument double recaudacionTaquilla,
                                       @Argument boolean esSaga) {

        if (!peliculasMap.containsKey(id)) {
            return null;
        }

        Pelicula peliculaActualizada = Pelicula.builder()
                .id(id)
                .titulo(titulo)
                .duracionMinutos(duracionMinutos)
                .recaudacionTaquilla(recaudacionTaquilla)
                .esSaga(esSaga)
                .build();

        peliculasMap.put(id, peliculaActualizada);
        return peliculaActualizada;
    }

    @MutationMapping
    public Boolean eliminarPelicula(@Argument String id) {
        return peliculasMap.remove(id) != null;
    }
}
