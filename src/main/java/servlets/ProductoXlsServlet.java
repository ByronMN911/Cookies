package servlets;
/*
 * Autor: Byron Melo
 * Fecha: 06/11/2025
 * Versión: 1.0
 * Descripción: Esta clase es un servlet que nos permitirá manejar peticiones HTTP tipo GET que el usuario
 * realice desde el navegador al dar click en un hipervínculo para mostrar un archivo html dinámico con
 * estructuras condicionales que muestran distintos datos de una tabla de productos solo si el usuario
 * se ha logeado, es decir solo si existe la cookie username.
 * */
import jakarta.servlet.ServletException;
import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//Importamos las clases de nuestro package services
import services.ProductoServices;
import services.ProductoServicesImplement;

//Importamos el modelo de nuestra aplicación web
import models.Producto;

//Importamos la clase para crear listas
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/*Definimos una llave para acceder a este servlet
 */
@WebServlet("/productos.html")
public class ProductoXlsServlet extends HttpServlet {
    //Sobreescribimos el metodo GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Obtener las cookies de la petición GET
        Cookie[] cookies = req.getCookies()!=null ? req.getCookies() : new Cookie[0];

        /*Este bloque de código devuelve un Optional<String> (contenedor que puede tener un valor o no tenerlo),
          contiene el valor de la cookie si existe y
          está vacío si no existe, en el String del Optional estaría nuestro valor para la clave
          Busco dentro de la cookie si existe algo de información
          Se convierte el arreglo de cookies en un Stream, es decir, una secuencia que se pude filtrar y procesar.
        */
        Optional<String> cookieOptional = Arrays.stream(cookies)
                //Filter recorre todas las cookies y solo deja pasar las cookies cuyo nombre sea "username".
                .filter(c->"username".equals(c.getName()))
                //Si encontró una cookie con ese nombre, ahora la convierte a su valor String.
                .map(Cookie::getValue)
                //findAny() busca cualquier coincidencia, podrían existir varios username pero en este caso solo hay uno.
                .findAny();
        //Si el contenedor cookieOptional tiene un valor la variable booleana es verdadera y si no es false
        boolean usuarioLogeado = cookieOptional.isPresent();

        //Guardamos el valor de la cookie si existe y si no el valor es un string vacío("")
        String username = cookieOptional.orElse("");

        /*Creamos un objeto de tipo ProductosServices que hace referencia a la clase ProductosServicesImplement
         * el cual implementa el metodo, esto es polimorfismo ya que el objeto de tipo ProductoServices en
         * realidad es una instancia de ProductosServicesImplement.
         * Esto se hace así porque en Java es buena práctica programar contra interfaces, no contra clase, por lo
         * tanto, esto se hace siempre que queremos usar una clase que implementa un metodo de una interfaz.
         */
        ProductoServices service = new ProductoServicesImplement();

        //Definimos una lista que usará el metodo listar de nuestro objeto services que ya implementó el metodo
        List<Producto> productos = service.listar();

        //Definimos el tipo de contenido que se enviará como respuesta a la petición
        resp.setContentType("text/html;charset=UTF-8");

        /*
         * Usamos un try-with-resources para que el objeto PrintWriter se cierre automáticamente
         * una vez se termine el bloque try
         * */
        try(PrintWriter out = resp.getWriter())
        {
            //Imprimimos un archivo HTML con PrintWriter
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            //Usamos UTF-8 para permitir caracteres especiales como tildes
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Lista de Productos</title>");
            //Usamos la etiqueta style para aplicar estilos a las estructuras html
            out.println("<style>");
            out.println("h2 { color: #40739e; align: center; }");
            out.println("a { display: inline-block; margin-top: 20px; color: #0097e6; text-decoration: none; font-weight: bold; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Lista de Productos</h2>");
            //Mostramos un mensaje si el usuario se logeo correctamente
            if (usuarioLogeado) {
                out.println("<h3> Bienvenido " + username + "</h3>");
            }
            //Creamos una tabla y definimos atributos para darle una presentación más agradable
            out.println("<table border= '1' cellpadding= '5' cellspacing= '0' width= '55%' bgcolor='#f5f5f5'> ");
            /*
             * Podríamos utilizar la etiqueta caption para poner un título que vaya unido a la tabla
             * out.println("<caption style= 'text-align: center';>LISTA DE PRODUCTOS</caption>");
             * */

            //Creamos la cabecera de la tabla
            out.println("<thead>");
            //Creamos la fila de la cabecera de la tabla que tendrá un color de fondo azul marino
            out.println("<tr bgcolor='#4a69bd'>");
            //Creamos los campos de la cabecera de la tabla que representan los datos de cada producto
            out.println("<th>ID PRODUCTO</th>");
            out.println("<th>NOMBRE</th>");
            out.println("<th>TIPO</th>");
            //Se muestra el campo de precio solo si el usuario está logeado
            if(usuarioLogeado) {
                out.println("<th>PRECIO</th>");
            }

            out.println("</tr>");
            out.println("</thead>");
            //Cuerpo de la tabla
            out.println("<tbody>");
            /*
             * Creamos un bucle for-each para construir en cada iteración una fila con
             * todos los datos de un producto con base a los atributos definidos en nuestro modelo
             * */
            productos.forEach(p ->{
                //El atributo align='center' nos permite centrar todos los datos de los campos de las filas
                out.println("<tr align= 'center'>");
                //Utilizamos los métodos públicos getters para acceder al valor de cada atributo de un producto
                out.println("<td>" + p.getIdProducto() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getTipo() + "</td>");
                if(usuarioLogeado) {
                    out.println("<td>" + p.getPrecio()+ "</td>");
                }
                out.println("</tr>");
            });
            out.println("</tbody>");
            out.println("</table>");
            out.println("<br>");
            //Hipervínculo para regresar al menú de productos
            out.println("<a href='/manejocookies/index.html'>Regresar</a>");
            out.println("</body>");
            out.println("</html>");

        }
    }
}