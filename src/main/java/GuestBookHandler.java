import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GuestBookHandler implements HttpHandler {


    public void handle(HttpExchange httpExchange) throws IOException {

        String method = httpExchange.getRequestMethod();
        if(method.equals("POST")){
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String formData = br.readLine();
            Map inputs = parseFormData(formData);
            String message = (String) inputs.get("message");
            String author = (String) inputs.get("name");
            LocalDateTime date = LocalDateTime.now();
            saveNote(message, author, date);
        }

        List<Note> notes = getNotes();
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/template.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("notes", notes);
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private List<Note> getNotes(){
        List<Note> notes = new ArrayList<>();
        try (Connection con = C3P0DataSource.getInstance().getConnection(); Statement stmt = con.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM Notes ");
            while (rs.next()) {
                int id = rs.getInt("id");
                String message = rs.getString("message");
                String author = rs.getString("author");
                String date = rs.getString("note_date");

                Note note = new Note(id, message, author, date);
                notes.add(note);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
            return notes;
    }


    private void saveNote(String message, String author, LocalDateTime date){
        try (Connection con = C3P0DataSource.getInstance().getConnection()){
            PreparedStatement stmt = null;
            stmt = con.prepareStatement("INSERT INTO Notes(message, author, note_date) VALUES (?, ?, ?)");
            stmt.setString(1, message);
            stmt.setString(2, author);
            String formattedString = date.toString();
            stmt.setString(3, formattedString);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        String[] pairs = formData.split("&");
        for(String pair : pairs){
            String[] keyValue = pair.split("=");
            // We have to decode the value because it's urlencoded. see: https://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms
            String value = new URLDecoder().decode(keyValue[1], "UTF-8");
            map.put(keyValue[0], value);
        }
        return map;
    }
}
