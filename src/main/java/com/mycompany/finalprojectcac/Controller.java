package com.mycompany.finalprojectcac;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sneakers")
public class Controller {
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
        // CORS
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        Conexion conexion = new Conexion();
        Connection conn = conexion.getConnection();
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Sneaker sneaker = mapper.readValue(req.getInputStream(), Sneaker.class);
            
            String query = "INSERT INTO sneakers (brand, model, img, price) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            statement.setString(1, sneaker.getBrand());
            statement.setString(2, sneaker.getModel());
            statement.setString(3, sneaker.getImg());
            statement.setInt(4, sneaker.getPrice());
            
            statement.executeUpdate();
            
            ResultSet rs = statement.getGeneratedKeys();
            
            if(rs.next()){
                Long id_sneaker = rs.getLong(1);
                res.setContentType("application/json");
                String json = mapper.writeValueAsString(id_sneaker);
                res.getWriter().write(json);
            }
            res.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            conexion.close();
        }
    }
    
     protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        Conexion conexion = new Conexion();
        Connection conn = conexion.getConnection();
        
         try {
            String query = "SELECT * FROM sneakers";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            List<Sneaker> sneakers = new ArrayList<>();
            
            while(rs.next()){
                Sneaker sneaker;
                sneaker = new Sneaker(
                        rs.getInt("id_sneaker"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("img"),
                        rs.getInt("price")
                );
                sneakers.add(sneaker);
            }
            
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(sneakers);
            
            res.setContentType("application/json");
            res.getWriter().write(json);
            
         } catch (SQLException | IOException e) {
             e.printStackTrace();
             res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
         } finally {
             conexion.close();
         }
    }
}

   