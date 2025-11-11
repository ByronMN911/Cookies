package servlets;
/*
 * Autor: Byron Melo
 * Fecha: 10/11/2025
 * Versión: 1.0
 * Descripción: Esta es una clase servlet que nos permite manejar peticiones GET cuando el usuario accede a login.jsp
 * que es cuando no existe una cookie, en ese caso se muestra un formulario que debe llenar con sus credenciales, este
 * formulario es manejado por el metodo doPost y permite crear una cookie con base al username ingresado.
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
     *El valor de estas variables nunca cambiará porque son constantes, por ello,
     * se declaran afuera y antes de todos los métodos
     * para usarlo en cualquier parte de nuestro programa.
     * */
    final static String USERNAME = "admin";
    final static String PASSWORD = "123";
    //Metodo doGet heredado de HttpServlet y sobreescrito
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*Creamos un arreglo de tipo cookie
         * El metodo getCookies() entrega un arreglo de cookies que vienen del navegador (petición)
         * Utilizamos un operador ternario: si existen cookies se obtienen y guardan en el arreglo y si no existen, es decir,
         * si es nulo el retorno del valor al utilizar el metodo getCookies(), entonces se crea un nuevo objeto cookie.
         * Gracias a esta estructura luego con el arrays.stream() evitamos un NullPointerException
         * */
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
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; background-color: #f0f3f5; text-align: center; padding: 40px; }");
                out.println("h1 { color: #2f3640; }");
                out.println("a { color: #0097e6; text-decoration: none; font-weight: bold; }");
                out.println("a:hover { text-decoration: underline; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Login</h1>");
                out.println("<p> Bienvenido a mi sistema " + cookieOptional.get() + " has iniciado sesión");
                out.println("<a href='" +req.getContextPath()+"/index.html'>Volver a la página principal</a>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            /*
             * En caso de que el optionalCookies no haya retornado un valor, es decir no se haya encontrado un valor para
             * la clave username, el servlet redirige al usuario al formulario para que pueda volver a iniciar sus credenciales
             * pero redirige sin volver a hacer una nueva petición, es un forward interno
             * El getServletContext retorna un objeto que representa a la aplicación web completa
             * El getRequestDispatcher permite enviar una petición a un recurso dentro del servidor
             * con forward(req, resp) transferimos el control al JSP (muestra el formulario)
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
                out.println("<style>");
                out.println("body { font-family: Arial, sans-serif; background-color: #f0f3f5; text-align: center; padding: 40px; }");
                out.println("h1 { color: #2f3640; }");
                out.println("a { color: #0097e6; text-decoration: none; font-weight: bold; }");
                out.println("a:hover { text-decoration: underline; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Login</h1>");
                out.println("<p> Bienvenido a mi sistema " + "<strong>"+ username +"</strong>" + ", has iniciado sesión :)");

                //getContextPath() retorna el nombre de nuestro proyecto = /manejocookies y creamos un enlace para enviar a otra URL
                out.println("<br><br>");
                out.println("<a href= '"+req.getContextPath() + "/index.html'>Volver a la página principal</a>");
                /*
                 * También podríamos usar esta línea de código para redirigir al usuario a la página principal pero esto es menos flexible
                 * out.println("<a href= '/manejocookies/index.html'>Volver a la Página Principal</a>");
                 * */

                out.println("</body>");
                out.println("</html>");
            }
            //El metodo sendRedirect redirecciona automáticamente y crea una nueva petición Get que al index.html
            //resp.sendRedirect(req.getContextPath() + "/index.html");
        } else {
            //Envíamos a la respuesta de la petición http post un código de estado Http, el 401 (unauthorized)
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no tiene acceso o credenciales incorrectas");
        }
    }
}
