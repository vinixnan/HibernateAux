/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author vinicius
 */
public class GerarRelatorio {

    private static Connection con;

    public static Connection getConexao() throws SQLException {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://127.0.0.1:5432/agrupasys";
        String login = "postgres";
        String senha = "12345";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, login, senha);
        } catch (ClassNotFoundException e) {
        } catch (SQLException e) {
        }
        return con;
    }

    public GerarRelatorio() {
    }

    public void fechaConexao() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(GerarRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gerar(String pathOrigem, Map<String, Object> parametros) {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        InputStream caminho = context.getExternalContext().getResourceAsStream(pathOrigem);
        try {
            ServletOutputStream responseStream = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=\"relatorio.pdf\"");
            JasperReport pathReport;
            pathReport = JasperCompileManager.compileReport(caminho);

            JasperPrint preencher = JasperFillManager.fillReport(pathReport, parametros, getConexao());
            //JasperViewer.viewReport(preencher);
            JasperExportManager.exportReportToPdfStream(preencher,responseStream);
            responseStream.flush();
            responseStream.close();
        } catch (Exception ex) {
            Logger.getLogger(GerarRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }


        context.renderResponse();
        context.responseComplete();

        this.fechaConexao();


    }
}