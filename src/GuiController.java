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

    public GuiController(){
        gui = Gui.getInstance();

        List<String> arg = new ArrayList<String>();
        arg.add("Client.out");
        List<String> args = gui.getParameters().getUnnamed();
        arg.add(args.get(0));
        arg.add(args.get(1));
        params = new String[3];
        arg.toArray(params);

        time = 0;
        numOfDrivers = 0;
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
                    gui.addDriver(numOfDrivers);

                    try {
                        Process driver = Runtime.getRuntime().exec(params);
                        pw = new PrintWriter(driver.getOutputStream(), true);
                        pw.println(driver_srl);
                    } catch (IOException e) {
                        //todo: handle exception
                    }
                    --numOfDrivers;
                }
            }
        });

        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
            private Boolean getNUmOfDrivers = false;

            @Override
            public void handle(MouseEvent event) {
                error.setText("");
                String msg = send_info.getCharacters().toString();
                if (getNUmOfDrivers == true) {
                    numOfDrivers = Integer.parseInt(msg);
                    send_info.deleteText(0, msg.length());
                    gui.send("num of drivers:" + numOfDrivers);
                    getNUmOfDrivers = false;
                } else {
                    if (msg.equals("1")) { //start getting drivers
                        send_info.deleteText(0, msg.length());
                        send_info.getCharacters().toString();
                        getNUmOfDrivers = true;

                    } else if (msg.equals("2")) { //send trip
                        send_info.deleteText(0, msg.length());
                        String trip_srl = driver_info.getCharacters().toString();
                        driver_info.deleteText(0, trip_srl.length());
                        gui.send("trip: " + trip_srl);
                        try {
                            errorCheck(gui.recieve());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (msg.equals("3")) { //send taxi
                        send_info.deleteText(0, msg.length());
                        String taxi_srl = send_info.getCharacters().toString();
                        send_info.deleteText(0, taxi_srl.length());
                        gui.send("taxi: " + taxi_srl);
                        try {
                            errorCheck(gui.recieve());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (msg.equals("7")) { //send taxi
                        send_info.deleteText(0, msg.length());
                        gui.send("end");

                    } else if (msg.equals("9")) { // time passed
                        send_info.deleteText(0, msg.length());
                        time++;
                        gui.send("9");
                        String[] location_srl;
                        ArrayList<Point> locations = new ArrayList<>();
                        try {
                            location_srl = gui.recieve().split(" ");
                            for (int i = 0; i < numOfDrivers; i++) {
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
}
