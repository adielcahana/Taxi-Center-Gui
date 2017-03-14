import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Gui extends Application {
    private static Gui instance;

    private ArrayList<Driver> drivers;
    private Map map;
    private BorderPane root;

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
/*
    public Gui(){
        instance = this;
        List<String> args = this.getParameters().getUnnamed();
        try (
                Socket socket = new Socket(args.get(0), Integer.parseInt(args.get(1)));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String fromServer;

            this.out = out;
            this.in = in;

            fromServer = in.readLine();
            map = Map.deserialize(fromServer);
        } catch (Exception e) {
            // todo: handle exception
        }
        drivers = new ArrayList<Driver>();
    }
*/
    public static Gui getInstance(){
        return instance;
    }

    public void timePassed(){
        Node p = root.getCenter();
        try {
            map.drawOn(p);
            for (Driver driver: drivers) {
                driver.drawOn(p);
            }
        } catch (IOException e) {
            //todo: handle exception
            e.printStackTrace();
        }
    }

    public void send(String msg){
        out.println(msg);
    }

    public String recieve() throws IOException {
        String msg = in.readLine();
        return msg.substring(0,msg.length());
    }

    public void setLocations(ArrayList<Point> locations){
        Iterator<Point> nextLoc = locations.iterator();
        for (Driver driver: drivers){
            driver.setLocation(nextLoc.next());
        }
    }
    
    public void addDriver(int driverNum){
    	Driver driver = new Driver(driverNum);
    	this.drivers.add(driver);
    	Node borderPane = this.root.getCenter();
    	driver.drawOn(borderPane);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
    	instance = this;
        List<String> args = this.getParameters().getUnnamed();
        try {
            this.sock = new Socket(args.get(0), Integer.parseInt(args.get(1)));
            this.out = new PrintWriter(sock.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            String fromServer;

            fromServer = in.readLine();
            map = Map.deserialize(fromServer);
        } catch (Exception e) {
            // todo: handle exception
        }
        drivers = new ArrayList<Driver>();
    	
            this.root = FXMLLoader.load(getClass().getResource("gui.fxml"));
            root.setCenter(new GridPane());
            this.map.drawOn(root.getCenter());
            primaryStage.setTitle("Taxi Center");
            primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            primaryStage.sizeToScene();
            primaryStage.show();
    }
    public void close(){
        try {
            this.sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
