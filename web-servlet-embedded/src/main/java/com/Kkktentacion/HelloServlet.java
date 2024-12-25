package com.Kkktentacion;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns="/")
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        String name=request.getParameter("name");
        if(name==null)
        {
            name="world";
        }
        PrintWriter pw=response.getWriter();
        pw.write("<h1>Hello, "+name+"!</h1>");
        pw.flush();
    }
}