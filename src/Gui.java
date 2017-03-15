import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private ArrayList<Process> processList;

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public static Gui getInstance(){
        return instance;
    }

    public void timePassed(){
        AnchorPane anchor = new AnchorPane();
    	GridPane p = new GridPane();

        AnchorPane.setTopAnchor(p, 0.0);
        AnchorPane.setBottomAnchor(p, 0.0);
        AnchorPane.setRightAnchor(p, 0.0);
        AnchorPane.setLeftAnchor(p, 0.0);

        anchor.getChildren().add(p);

        BorderPane.setAlignment(anchor, Pos.CENTER);
        BorderPane.setMargin(anchor, new Insets(10,10,10,10));

        try {
            map.drawOn(p);
            for (Driver driver: drivers) {
                driver.drawOn(p);
            }
        } catch (IOException e) {
            //todo: handle exception
            e.printStackTrace();
        }

        root.setCenter(anchor);
    }

    public void addProcess(Process proc){
    	this.processList.add(proc);
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
    	timePassed();
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
        processList = new ArrayList<Process>();
            this.root = FXMLLoader.load(getClass().getResource("gui.fxml"));
            this.timePassed();
            primaryStage.setTitle("Taxi Center");
            primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
            primaryStage.setMaxHeight(650);
            primaryStage.setMaxWidth(650);
            primaryStage.show();
    }

    public void close(){
        try {
        	for(Process p : processList){
        					p.waitFor();
        		}
            this.sock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Platform.exit();
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
