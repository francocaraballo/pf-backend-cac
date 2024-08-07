package com.mycompany.finalprojectcac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpsServer;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sneakers")
public class Controller extends HttpServlet {

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

            if (rs.next()) {
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

            while (rs.next()) {
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

    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");

        Conexion conexion = new Conexion();
        Connection conn = conexion.getConnection();

        try {

            ObjectMapper mapper = new ObjectMapper();
            int idDelete = mapper.readValue(req.getInputStream(), Integer.class);

            String query = "DELETE FROM sneakers WHERE id_sneaker=?";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setInt(1, idDelete);
            statement.executeUpdate();

            res.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            conexion.close();
        }

    }

    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");

        Conexion conexion = new Conexion();
        Connection conn = conexion.getConnection();

        try {
            ObjectMapper mapper = new ObjectMapper();
            Sneaker sneaker = mapper.readValue(req.getInputStream(), Sneaker.class);

            String brand = sneaker.getBrand();
            String model = sneaker.getModel();
            String img = sneaker.getImg();
            int price = sneaker.getPrice();
            int id_sneaker = sneaker.getId_sneaker();

            String query = "UPDATE sneakers SET brand=?, model=?, img=?, price=? WHERE id_sneaker=?";
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, brand);
            statement.setString(2, model);
            statement.setString(3, img);
            statement.setInt(4, price);
            statement.setInt(5, id_sneaker);

            statement.executeUpdate();

//             ResultSet rs = statement.getGeneratedKeys();
            res.setContentType("application/json");
            String json = mapper.writeValueAsString(id_sneaker);
            res.getWriter().write(json);

            res.setStatus(HttpServletResponse.SC_ACCEPTED);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            conexion.close();
        }
    }

}
