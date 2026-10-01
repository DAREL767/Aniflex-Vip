package com.aniflex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaInput {
    private String id; // <-- Permitir ID manual
    private String titulo;
    private int duracionMinutos;
    private double recaudacionTaquilla;
    private boolean esSaga;
    private String fechaEstreno;
}
