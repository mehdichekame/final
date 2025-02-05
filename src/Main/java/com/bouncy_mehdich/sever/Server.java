package Main.java.com.bouncy_mehdich.sever;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;


import Main.java.com.bouncy_mehdich.sever.DB.UserDAO;
import com.sun.net.httpserver.HttpServer;
import Main.java.com.bouncy_mehdich.sever.Handlers.*;

public class Server {

    public static void main(String[] args){

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

            server.createContext("/user", new UserHandler());
            server.createContext("/login", new UserHandler());

            server.createContext("/profile", new ProfileHandler());
            server.createContext("/post", new PostHandler());
            server.createContext("/following", new FollowsHandler());
            server.createContext("/follows", new FollowsHandler());

            server.createContext("/likes", new LikesHandler());
            server.createContext("/comments", new CommentHandler());
            server.createContext("/search", new UserHandler());
            server.createContext("/jobs", new JobsHandler());






            server.start();


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
