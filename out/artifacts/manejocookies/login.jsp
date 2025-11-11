<%--
  Created by IntelliJ IDEA.
  User: Arman
  Date: 10/11/2025
  Time: 8:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio de Sesión</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f5f6fa;
            color: #2f3640;
            text-align: center;
            padding: 40px;
        }

        h1 {
            color: #273c75;
        }

        form {
            background-color: #ffffff;
            border-radius: 10px;
            padding: 25px;
            width: 350px;
            margin: 30px auto;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: left;
        }

        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #dcdde1;
            border-radius: 6px;
        }

    </style>
</head>
<body>
<h1>Inicio de Sesión</h1>
<form action="/manejocookies/login" method="post">
    <div>
        <label for="user">Ingrese el usuario</label>
        <input type="text" id="user" name="user">
    </div>
    <div>
        <label for="password">Ingrese el password</label>
        <input type="password" id="password" name="password">
    </div>
    <div>
        <input type="submit" value="Entrar">
    </div>
</form>
</body>
</html>
