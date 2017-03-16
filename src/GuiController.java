import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GuiController{
	// read the gui parts from the fxml file
	@FXML
	BorderPane pane;
	@FXML
	HBox top;
	@FXML
	Button send;
	@FXML
	Button add;
	@FXML
	TextField driver_info;
	@FXML
	TextField send_info;
	@FXML
	Label clock;
	@FXML
	Label error;
	@FXML
	GridPane grid;


	private Gui gui;
	private Integer time;
	private String[] params;
	private Integer numOfDrivers;
	private Integer driverSize;

	/**
	 * constructor
	 */
	public GuiController(){
		gui = Gui.getInstance();

		// get the args from cmd
		List<String> arg = new ArrayList<String>();
		arg.add("./client.out");
		List<String> args = gui.getParameters().getUnnamed();
		arg.add("127.0.0.1");
		arg.add(args.get(1));
		params = new String[3];
		arg.toArray(params);

		time = 0;
		numOfDrivers = 0;
		driverSize = 0;
	}

	/**
	 * if got error print -1
	 */
	public void errorCheck(String msg){
		if(msg.equals("error")){
			error.setText("-1");
		}
	}

	/**
	 * setOnMouseClicked for add & send buttons
	 */
	@FXML
	void initialize(){

		// setOnMouseClicked for add
		add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// clean the error text
				error.setText("");
				if (numOfDrivers > 0) {
					// get input for driver
					PrintWriter pw;
					String driver_srl = driver_info.getCharacters().toString();
					driver_info.deleteText(0, driver_srl.length());

					try {
						// open new client process
						Process driver = Runtime.getRuntime().exec(params);
						// print input to the client
						pw = new PrintWriter(driver.getOutputStream(), true);
						pw.println(driver_srl);
						// if the input is valid add new driver
						if(isValidDriverInput(driver_srl)){
							gui.addDriver(numOfDrivers);
							gui.addProcess(driver);
							--numOfDrivers;
						}
					} catch (IOException e) {}
				}
			}
		});

		// setOnMouseClicked for send
		send.setOnMouseClicked(new EventHandler<MouseEvent>() {
			private int lastMsg = 0;

			@Override
			public void handle(MouseEvent event) {
				// clean the error text
				error.setText("");
				// get input for trip / taxi or option
				String msg = send_info.getCharacters().toString();
				if(lastMsg != 0){
					if (lastMsg == 1) {
						// send the server to add driver
						numOfDrivers = Integer.parseInt(msg);
						driverSize = numOfDrivers;
						send_info.deleteText(0, msg.length());
						gui.send("num of drivers:" + numOfDrivers);
						lastMsg = 0;
					} else if (lastMsg == 2){
						// send the server new trip
						String trip_srl = msg;
						send_info.deleteText(0, msg.length());
						gui.send("trip: " + msg);
						lastMsg = 0;
						try {
							// check for errors
							errorCheck(gui.recieve());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (lastMsg == 3){
						// send the server new taxi
						send_info.deleteText(0, msg.length());
						gui.send("taxi: " + msg);
						lastMsg = 0;
						try {
							//check for errors
							errorCheck(gui.recieve());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else {
					if (msg.equals("1")) { //start getting drivers
						send_info.deleteText(0, msg.length());
						send_info.getCharacters().toString();
						lastMsg = 1;
					} else if (msg.equals("2")) { //send trip
						lastMsg = 2;
						send_info.deleteText(0, msg.length());
					} else if (msg.equals("3")){ // send taxi
						lastMsg = 3;
						send_info.deleteText(0, msg.length());
					} else if (msg.equals("7")) { //end
						send_info.deleteText(0, msg.length());
						// send to server to finish
						gui.send("end");
						// close the gui program
						gui.close();
					} else if (msg.equals("9")) { // time passed
						send_info.deleteText(0, msg.length());
						time++;
						// send the server time passed
						gui.send("time passed");
						String[] location_srl;
						ArrayList<Point> locations = new ArrayList<>();
						try {
							// recieve all drivers location
							location_srl = gui.recieve().split(" ");
							for (int i = 0; i < driverSize; i++) {
								Point loc = Point.deserialize(location_srl[i]);
								locations.add(loc);
							}
							// set location to drivers in the gui
							gui.setLocations(locations);
						} catch (IOException e) {
							//todo : handle exception
							e.printStackTrace();
						}
						// draw the new gui
						gui.timePassed();
						// print time + 1
						clock.setText(time.toString());
					}

				} 
			}
		});
	}
	
	/**
	 * check if the driver input is legal
	 */
	public boolean isValidDriverInput(String driverInput) {
		int commaCounter = 0;
		// flag for reading letter
		boolean readAlpha = false;
		for(int i=0; i< driverInput.length() && commaCounter <= 4; i++){
			if(commaCounter == 2 && !readAlpha){ // letter should appear only once, after the 2nd comma
				if(!Character.isLetter(driverInput.charAt(i))) {
					return false;
				}
				readAlpha = true;
				continue;
			} else { // otherwise, digits or comma should appear
				if(driverInput.charAt(i) == ','){
					commaCounter++;
					continue;
				} else if(!Character.isDigit(driverInput.charAt(i))){
					return false;
				}
			}
		}
		// if all the other tests passed, verify that there are only 4 comma
		return commaCounter == 4;
	}
}
