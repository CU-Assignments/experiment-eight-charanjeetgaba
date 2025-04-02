#EASY
  import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if ("admin".equals(username) && "password123".equals(password)) {
            out.println("<h2>Welcome, " + username + "!</h2>");
        } else {
            out.println("<h2>Invalid login. Please try again.</h2>");
        }
        out.close();
    }
}

#MEDIUM
  import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "password");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<form action='EmployeeServlet' method='post'>Enter Employee ID: <input type='text' name='empId'><br><input type='submit' value='Search'></form>");
        
        try (Connection con = getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            out.println("<h2>Employee List</h2><table border='1'><tr><th>ID</th><th>Name</th><th>Department</th></tr>");
            while (rs.next()) out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("department") + "</td></tr>");
            out.println("</table>");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
        out.close();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String empId = request.getParameter("empId");
        
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            ps.setInt(1, Integer.parseInt(empId));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) out.println("<h2>Employee Details</h2>ID: " + rs.getInt("id") + "<br>Name: " + rs.getString("name") + "<br>Department: " + rs.getString("department") + "<br>");
            else out.println("<h2>No Employee found with ID " + empId + "</h2>");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
        out.close();
    }
}

#HARD
  import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/student_portal", "root", "password");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String studentId = request.getParameter("studentId");
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)");) {
            ps.setInt(1, Integer.parseInt(studentId));
            ps.setString(2, date);
            ps.setString(3, status);
            
            int result = ps.executeUpdate();
            if (result > 0) out.println("<h2>Attendance recorded successfully.</h2>");
            else out.println("<h2>Failed to record attendance.</h2>");
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
        out.close();
    }
}
