package services;
/*
 * Autor: Byron Melo
 * Fecha: 06/11/2025
 * Versión: 1.0
 * Descripción: En el package modelos tenemos la clase Producto que define todos los atributos de un producto.
 * Esta clase tiene un metodo que crea objetos de tipo Producto que se mandan al modelo de nuestra aplicación
 * en donde está el constructor de Producto y los getters y setters.
 * */

//Clase que nos permite importar el objeto de tipo lista
import java.util.List;
//Importamos las clase del modelo de nuestra aplicación web
import models.Producto;
//Im
import java.util.Arrays;

public class ProductoServicesImplement implements ProductoServices {
    //Sobreescribimos el metodo heredado de ProductoServices
    @Override
    public List<Producto> listar() {
        /*
         * El metodo retorna una lista en forma de arreglo de objetos de tipo Producto
         * Estos objetos que creamos se van al modelo de nuestra aplicación web
         * */
        return Arrays.asList(new Producto(1L, "Laptop", "Computación", 256.23),
                new Producto(2L, "Mouse", "Computacion", 25.50),
                new Producto(3L, "Cocina", "Cocina", 25.35));

    }
}
