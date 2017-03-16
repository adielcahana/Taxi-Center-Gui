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

/**
 * Created by adi on 04/02/17.
 */
public class Gui extends Application {
	private static Gui instance;

	private ArrayList<Driver> drivers;
	private Map map;
	private BorderPane root;
	private ArrayList<Process> processList;

	private Socket sock;
	private PrintWriter out;
	private BufferedReader in;

	/**
	 * return the gui
	 */
	public static Gui getInstance(){
		return instance;
	}

	/**
	 * draw the gui every time passed
	 */
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

	/**
	 * add process to the process list
	 */
	public void addProcess(Process proc){
		this.processList.add(proc);
	}

	/**
	 * send message to the server
	 */
	public void send(String msg){
		out.println(msg);
	}

	/**
	 * recieve message from the server
	 */
	public String recieve() throws IOException {
		String msg = in.readLine();
		return msg.substring(0,msg.length());
	}

	/**
	 * get to every driver his location (that given by the server)
	 */
	public void setLocations(ArrayList<Point> locations){
		Iterator<Point> nextLoc = locations.iterator();
		for (Driver driver: drivers){
			driver.setLocation(nextLoc.next());
		}
	}

	/**
	 * create new driver add him to the driver list and draw him on the grid
	 */
	public void addDriver(int driverNum){
		Driver driver = new Driver(driverNum);
		this.drivers.add(driver);
		timePassed();
	}

	/**
	 * start the gui running
	 */
	@Override
	public void start(Stage primaryStage) throws Exception{
		instance = this;
		// get list of parameters from the cmd
		List<String> args = this.getParameters().getUnnamed();
		try {
			// create new sock (tcp) to communicate with the server
			this.sock = new Socket(args.get(0), Integer.parseInt(args.get(1)));
			this.out = new PrintWriter(sock.getOutputStream(),true);
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String fromServer;

			// get the map height & width from the server
			fromServer = in.readLine();
			// create the map 
			map = Map.deserialize(fromServer);
		} catch (Exception e) {
			
		}
		drivers = new ArrayList<Driver>();
		processList = new ArrayList<Process>();
		// load the root from thefxml file
		this.root = FXMLLoader.load(getClass().getResource("gui.fxml"));
		this.timePassed();
		// show the gui
		primaryStage.setTitle("Taxi Center");
		primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
		primaryStage.setMaxHeight(650);
		primaryStage.setMaxWidth(650);
		primaryStage.show();
	}

	/**
	 * finish the program
	 */
	public void close(){
		try {
			// wait until all the processes over
			for(Process p : processList){
				p.waitFor();
			}
			// close the socket
			this.sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// exit from the program
		Platform.exit();
		System.exit(0);
	}

	/**
	 * main - run the gui
	 */ 
	public static void main(String[] args) {
		launch(args);
	}
}
