package services;
import models.Producto;
import java.util.List;
/*
 * Autor: Byron Melo
 * Fecha: 06/11/2025
 * Versión: 1.0
 * Descripción: Clase especial de tipo interfaz donde definimos un metodo que será
 * implementado por otras clases y debe retornar una lista.
 * */

public interface ProductoServices {

    //declaramos un metodo que retorna una lista de elementos tipo Producto (sin implementar)
    List<Producto> listar();
}
