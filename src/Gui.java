import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Gui extends Application {
    public static PrintWriter out;
    public static BufferedReader in;
    public static BorderPane map_pane;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Map map;
        List<String> args = this.getParameters().getUnnamed();
        try (
                Socket socket = new Socket(args.get(0), Integer.parseInt(args.get(1)));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String fromServer;

            Gui.out = out;
            Gui.in = in;

            fromServer = in.readLine();
            map = Map.deserialize(fromServer);

            BorderPane root = FXMLLoader.load(getClass().getResource("gui.fxml"));
            map_pane = (BorderPane) map.getGui();
            root.setCenter(map_pane);
            primaryStage.setTitle("Taxi Center");
            primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            primaryStage.show();
        } catch (Exception e) {
            // handle exception
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
