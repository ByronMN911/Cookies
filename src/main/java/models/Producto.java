package models;
/*
 * Autor: Byron Melo
 * Fecha: 06/11/2025
 * Versión: 1.0
 * Descripción: Esta clase define los atributos que tendrá un objeto Producto y además definimos
 * metodos getters y setters para acceder y modificar estos atributos.
 * */


    public class Producto {
        //Encapsulamos y declaramos las variables del objeto producto
        private Long idProducto;
        private String nombre;
        private String tipo;
        private double precio;

        /*
         * Un JavaBean es una clase de java que permite crear objetos, necesita tener un
         * constructor vacío y otro constructor en donde se inicialicen los valores a inicializar.
         * */
        //Constructor vacío para luego inicializar la clase con los parámetros que queramos.
        public Producto(){}

        //Constructor con todos los parámetros del objeto
        public Producto(Long idProducto, String nombre, String tipo, double precio){
            this.idProducto = idProducto;
            this.nombre = nombre;
            this.tipo = tipo;
            this.precio = precio;
        }

        /*
         * Metodos Getters y Setters para acceder a los atributos privados de la clase
         * */

        //Getter para idProducto
        public Long getIdProducto(){
            return this.idProducto;
        }
        public void setIdProducto(Long idProducto){
            this.idProducto = idProducto;
        }
        //Getter para nombre
        public String getNombre(){
            return this.nombre;
        }
        //Setter para nombre
        public void setNombre(String nombre){
            this.nombre = nombre;
        }
        //Getter para tipo
        public String getTipo(){
            return this.tipo;
        }
        //Setter para tipo
        public void setTipo(String tipo){
            this.tipo = tipo;
        }
        //Getter para precio
        public double getPrecio() {
            return precio;
        }

        //Setter para precio
        public void setPrecio(double precio) {
            this.precio = precio;
        }
    }

