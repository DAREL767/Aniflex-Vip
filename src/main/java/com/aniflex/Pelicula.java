package com.aniflex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {
    private String id;
    private String titulo;
    private int duracionMinutos;
    private double recaudacionTaquilla;
    private boolean esSaga;
    private LocalDateTime fechaEstreno;
}
