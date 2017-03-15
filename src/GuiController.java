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

	public GuiController(){
		gui = Gui.getInstance();

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

	public void errorCheck(String msg){
		if(msg.equals("error")){
			error.setText("ERROR!");
		}
	}

	@FXML
	void initialize(){

		add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				error.setText("");
				//todo: handle case of multiple drivers
				if (numOfDrivers > 0) {
					PrintWriter pw;
					String driver_srl = driver_info.getCharacters().toString();
					driver_info.deleteText(0, driver_srl.length());

					try {
						Process driver = Runtime.getRuntime().exec(params);
						pw = new PrintWriter(driver.getOutputStream(), true);
						pw.println(driver_srl);
						if(isValidDriverInput(driver_srl)){
							gui.addDriver(numOfDrivers);
							gui.addProcess(driver);
							--numOfDrivers;
						}
					} catch (IOException e) {
						//todo: handle exception
					}
				}
			}
		});

		send.setOnMouseClicked(new EventHandler<MouseEvent>() {
			private int lastMsg = 0;

			@Override
			public void handle(MouseEvent event) {
				error.setText("");
				String msg = send_info.getCharacters().toString();
				if(lastMsg != 0){
					if (lastMsg == 1) {
						numOfDrivers = Integer.parseInt(msg);
						driverSize = numOfDrivers;
						send_info.deleteText(0, msg.length());
						gui.send("num of drivers:" + numOfDrivers);
						lastMsg = 0;
					} else if (lastMsg == 2){
						String trip_srl = msg;
						send_info.deleteText(0, msg.length());
						gui.send("trip: " + msg);
						lastMsg = 0;
						try {
							errorCheck(gui.recieve());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (lastMsg == 3){
						send_info.deleteText(0, msg.length());
						gui.send("taxi: " + msg);
						lastMsg = 0;
						try {
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
					} else if (msg.equals("3")){
						lastMsg = 3;
						send_info.deleteText(0, msg.length());
					} else if (msg.equals("7")) { //end
						send_info.deleteText(0, msg.length());
						gui.send("end");
						gui.close();
					} else if (msg.equals("9")) { // time passed
						send_info.deleteText(0, msg.length());
						time++;
						gui.send("time passed");
						String[] location_srl;
						ArrayList<Point> locations = new ArrayList<>();
						try {
							location_srl = gui.recieve().split(" ");
							for (int i = 0; i < driverSize; i++) {
								Point loc = Point.deserialize(location_srl[i]);
								locations.add(loc);
							}
							gui.setLocations(locations);
						} catch (IOException e) {
							//todo : handle exception
							e.printStackTrace();
						}
						gui.timePassed();
						clock.setText(time.toString());
					}

				} 
			}
		});
	}
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
