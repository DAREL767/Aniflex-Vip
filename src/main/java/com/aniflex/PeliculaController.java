package com.aniflex;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class PeliculaController {

    private final Map<String, Pelicula> peliculasMap = new ConcurrentHashMap<>();

    @QueryMapping
    public List<Pelicula> listarPeliculas(@Argument Boolean esSaga, @Argument String titulo) {
        return peliculasMap.values().stream()
                .filter(p -> esSaga == null || p.isEsSaga() == esSaga)
                .filter(p -> titulo == null || titulo.isBlank() ||
                        p.getTitulo().toLowerCase().contains(titulo.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public Pelicula buscarPeliculaPorId(@Argument String id) {
        return peliculasMap.get(id);
    }

    @MutationMapping
    public Pelicula crearPelicula(@Argument PeliculaInput peliculaInput) {
        String id = peliculaInput.getId();

        if (peliculasMap.containsKey(id)) {
            throw new RuntimeException("El ID '" + id + "' ya se encuentra registrado.");
        }

        LocalDateTime fecha = parseFecha(peliculaInput.getFechaEstreno());

        Pelicula nuevaPelicula = Pelicula.builder()
                .id(id)
                .titulo(peliculaInput.getTitulo())
                .duracionMinutos(peliculaInput.getDuracionMinutos())
                .recaudacionTaquilla(peliculaInput.getRecaudacionTaquilla())
                .esSaga(peliculaInput.isEsSaga())
                .fechaEstreno(fecha)
                .build();

        peliculasMap.put(id, nuevaPelicula);
        return nuevaPelicula;
    }

    @MutationMapping
    public Pelicula actualizarPelicula(@Argument String id, @Argument PeliculaInput peliculaInput) {
        if (!peliculasMap.containsKey(id)) {
            return null;
        }

        LocalDateTime fecha = parseFecha(peliculaInput.getFechaEstreno());

        Pelicula peliculaEditada = Pelicula.builder()
                .id(id)
                .titulo(peliculaInput.getTitulo())
                .duracionMinutos(peliculaInput.getDuracionMinutos())
                .recaudacionTaquilla(peliculaInput.getRecaudacionTaquilla())
                .esSaga(peliculaInput.isEsSaga())
                .fechaEstreno(fecha)
                .build();

        peliculasMap.put(id, peliculaEditada);
        return peliculaEditada;
    }

    @MutationMapping
    public Boolean eliminarPelicula(@Argument String id) {
        return peliculasMap.remove(id) != null;
    }

    // Auxiliar para convertir String a LocalDateTime de forma segura
    private LocalDateTime parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isBlank()) {
            return LocalDateTime.now();
        }
        try {
            // Si viene solo fecha YYYY-MM-DD le agregamos hora por defecto
            if (!fechaStr.contains("T")) {
                fechaStr += "T00:00:00";
            }
            return LocalDateTime.parse(fechaStr);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
