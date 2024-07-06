package Main.java.com.bouncy_mehdich.sever.Handlers;

import Main.java.com.bouncy_mehdich.sever.Controllers.MessageController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.sql.SQLException;

public class MessageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        MessageController messageController = null;
        try {
            messageController = new MessageController();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String response = "";
        String[] splittedPath = path.split("/");
        switch (method) {
            case "GET":
                if (splittedPath.length == 3) {
                    try {
                        response = messageController.getMessagesReceived(splittedPath[2], 20);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                try {
                    response = messageController.getMessages(splittedPath[2], splittedPath[3]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "POST":
                InputStream requestBody = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                requestBody.close();
                String newMessage = body.toString();
                JSONObject jsonObject = new JSONObject(newMessage);
                try {
                    messageController.addMessage(jsonObject.getString("senderID"), jsonObject.getString("receiverID"), jsonObject.getString("message"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "DELETE":
                if (splittedPath.length == 3) {
                    try {
                        messageController.deleteMessage(splittedPath[2]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else response = "";
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
