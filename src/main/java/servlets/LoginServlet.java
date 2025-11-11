package servlets;
/*
 * Autor: Byron Melo
 * Fecha: 10/11/2025
 * Versión: 1.0
 * Descripción: Esta clase es un servlet que nos permitirá manejar peticiones HTTP tipo GET que el usuario
 * realice desde el navegador al dar click en disintos hipervínculos que utilizan las 3 llaves de este servlet
 * para solicitar la descarga de archivos xls, json o mostrar un archivo html dinámico.
 * */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {
    /*Inicializamos las variables estáticas para el login,
    *El valor de estas variables nunca cambiará porque son constantes y se declaran afuera de todos los métodos
    * para usarlo en cualquier parte de nuestro programa.
    * */
    final static String USERNAME = "admin";
    final static String PASSWORD = "123";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*Creamos un arreglo de tipo cookie
        * El metodo getCookies() entrega un arreglo de cookies que vienen del navegador (petición)
        * Utilizamos un operador ternario: si existen cookies se obtienen y guardan en el arreglo y si no existen, es decir,
        * si es nulo el retorno del valor al utilizar el metodo getCookies(), entonces se crea un nuevo objeto cookie.
        * Gracias a esta estructura luego con el arrays.stream() evitamos un NullPointerException
        * */
        Cookie[] cookies = req.getCookies()!=null ? req.getCookies() : new Cookie[0];
        /*Este bloque de código devuelve un Optional<String> que contiene el valor de la cookie si existe y
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

        //Establecemos el tipo de contenido de respuesta
        resp.setContentType("text/html;charset=UTF-8");
        /*
        * Utilizamos un try-with-resources para cerrar automáticamente el objeto PrintWriter automáticamente
        * */
        if(cookieOptional.isPresent()) {
            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login " +  cookieOptional.get() + "</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Login</h1>");
                out.println("<p> Bienvenido a mi sistema " + cookieOptional.get() + " has iniciado sesión (GET)");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            /*
            * En caso de que el optionalCookies no haya retornado un valor, es decir no se haya encontrado un valor para
            * la clave username, el servlet redirige al usuario al formulario para que pueda volver a iniciar sus credenciales
            * */
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Capturamos los parámetros del formulario
        String username = req.getParameter("user");
        String password = req.getParameter("password");
        //Validamos que las credenciales ingresadas por el usuario desde el formulario sean iguales a nuestras variables estáticas
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            //Establecemos el tipo de contenido en respuesta a la petición
            resp.setContentType("text/html;charset=UTF-8");
            //Creamos una nueva cookie con el username del formulario (clave - valor)
            Cookie cookie = new Cookie("username", username);
            //Agregamos la cookie a la respuesta de la petición tipo POST
            resp.addCookie(cookie);
            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login Exitoso</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Login</h1>");
                out.println("<p> Bienvenido a mi sistema " + username + ", has iniciado sesión :)");
                /*
                Cuando el usuario ingresa correctamente se muestran las distintas opciones
                //Hipervínculo que utiliza la llave productos.html del servlet -->
                out.println("<a href='/manejocookies/productos.html'>Ver catálogo de productos</a>");
                out.println("<br><br>");
                //Hipervínculo que utiliza la llave productos.xls del servlet -->
                out.println("<a href='/manejocookies/productos.xls'>Descargar el archivo xls (excel) de la tabla productos</a>");
                out.println("<br><br>");
                //Hipervínculo que utiliza la llave productos.json del servlet -->
                out.println("<a href='/manejocookies/productos.json'>Descargar el archivo json de la tabla productos</a>");
                out.println("<br><br>");
                //Hipervínculo que nos redirige a la página principal index.html-->
                out.println("<a href='/manejocookies/index.html'>Volver a la página principal</a>");
                out.println("<br><br>");
                //out.println("<a href= '"+req.getContextPath() + "/index.html'>Volver a login</a>");
                */
                /*
                * También podríamos usar esta línea de código para redirigir al usuario a la página principal
                *
                * */
                out.println("<a href= '/manejocookies/index.html'>Volver a la Página Principal</a>");
                out.println("</body>");
                out.println("</html>");
            }
            //resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no tiene acceso o credenciales incorrectas");
        }

        /*
        * El deber consiste en modificar el servlet de productos para incorporar un sistema de autorización al contenido con
        * base al usuario que ingresó a la aplicación web
        * Si no se da de alta el usuario (si no ha generado la sesion)
        * debe mostrar todos los campos de la tabla menos el campo de precio
        * debe mostrar el nombre del usuario en el html donde está la tabla de productos
        *
        * El segundo deber consiste en implementar el código de un respositorio
        * */
    }
}
