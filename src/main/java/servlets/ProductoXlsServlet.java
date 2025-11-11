package servlets;
/*
 * Autor: Byron Melo
 * Fecha: 06/11/2025
 * Versión: 1.0
 * Descripción: Esta clase es un servlet que nos permitirá manejar peticiones HTTP tipo GET que el usuario
 * realice desde el navegador al dar click en disintos hipervínculos que utilizan las 3 llaves de este servlet
 * para solicitar la descarga de archivos xls, json o mostrar un archivo html dinámico.
 * */
import jakarta.servlet.ServletException;
import java.io.IOException;
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
import java.util.List;



/*Definimos 3 llaves (paths) agregando antes el símbolo de llaves y separándolos por comas
Un servlet puede tener más de 1 llave, porque al final son rutas a las cuales el usuario
puede acceder para interactuar con el mismo servlet
 */
@WebServlet({"/productos.xls", "/productos.html", "/productos.json"})
public class ProductoXlsServlet extends HttpServlet {
    //Sobreescribimos el metodo GET
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*Creamos un objeto de tipo ProductosServices que hace referencia a la clase ProductosServicesImplement
         * el cual implementa el metodo, esto es polimorfismo ya que el objeto de tipo ProductoServices en
         * realidad es una instancia de ProductosServicesImplement.
         * Esto se hace así porque en Java es buena práctica programar contra interfaces, no contra clase, por lo
         * tanto, esto se hace siempre que queremos usar una clase que implementa un metodo de una interfaz.
         */
        ProductoServices service =  new ProductoServicesImplement();

        //Definimos una lista que usará el metodo listar de nuestro objeto services que ya implementó el metodo
        List<Producto> productos = service.listar();

        //Definimos el tipo de contenido que se enviará como respuesta a la petición
        resp.setContentType("text/html;charset=UTF-8");
        /*
         * Esta variable es fundamental para entender cómo el servlet sabe qué llave utilizar, el metodo
         * getServletPath nos devuelve exactamente la llave que se usó, si el usuario dio click en el
         * hipervínculo que usa la llave "producto.xls", este metodo devuelve esa llave y luego se decide
         * qué hacer con base a ello.
         * */
        String servletPath = req.getServletPath();
        //Creamos una variable booleana que nos permite saber si el usuario accedió a la ruta de la llave productos.xls
        boolean esXls=servletPath.endsWith("xls");
        //Creamos una variable booleana que nos permite saber si el usuario accedió a la ruta de la llave productos.json
        boolean esJson = servletPath.endsWith("json");
        /*
         * Este bloque permite verificar si el usuario accedió a la ruta de llave productos.xls, productos.json o a productos.html
         * y dependiendo del caso se descarga un archivo xls (excel), un archivo json o muestra simplemente el HTML generado con
         * el print writer
         * */
        if(esXls){
            /*
             * Este es un nuevo tipo de contenido que le indica al navegador que recibirá un
             * archivo Excel, a pesar de que realmente es un html, pero excel es capaz de interpretarlo
             * */
            resp.setContentType("application/vnd.ms-excel");
            /*
             * Se agrega un header a la respuesta de la petición, indicando especificaciones para la descarga del archivo:
             * attachment obliga al navegador a descargarlo
             * filename=productos.xls es el nombre que tendrá el archivo
             * */
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        } else if(esJson){
            //Cambiamos el tipo de contenido de respuesta a json
            resp.setContentType("application/json;charset=UTF-8");
            //Establecemos un header para la respuesta indicándole al navegador la descarga obligatoria del archivo json
            resp.setHeader("Content-Disposition", "attachment; filename=productos.json");
            //Creamos un archivo JSON utilizando el PrintWriter dentro de un try-with-resources para cerrarlo automáticamente
            try(PrintWriter out = resp.getWriter()){
                //Imprimimos corchetes porque vamos a crear una colección de documentos (productos)
                out.println("[");
                //Utilizamos un for porque nos permite controlar más específicamente lo que podemos hacer en cada iteración
                for(int i=0;i<productos.size();i++){
                    //Esta variable almacenará el objeto de la iteración actual con base a la variable auxiliar i
                    Producto p = productos.get(i);
                    //Imprimimos un corchete que representa el inicio de un documento de la colección
                    out.println("{");
                    //Es importante utilizar \ para poder imprimir comillas y seguir el formato clave valor de un archivo JSON
                    out.println(" \"id\":" + p.getIdProducto()+",");
                    //Utilizamos los métodos públicos getters para acceder al valor de los atributos de un producto
                    out.println(" \"nombre\":\""+ p.getNombre()+ "\",");
                    out.println(" \"tipo\":\"" + p.getTipo()+ "\",");
                    out.println(" \"precio\":" + p.getPrecio());
                    //Esta validación nos permite colocar comas al final de cada documento para seguir la estructura de un archivo JSON
                    out.println(" }" +(i<productos.size()-1 ? "," : ""));
                }
                out.println("]");
            }
            //Para que cuando se termine el bucle no se procese la creación del archivo HTML
            return;
        }
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
            out.println("<th>PRECIO</th>");
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
                out.println("<td>" + p.getPrecio()+ "</td>");
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