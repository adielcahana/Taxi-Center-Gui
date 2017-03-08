import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.PrintWriter;

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
    TextField info;
    @FXML
    TextField msgNum;
    @FXML
    Label clock;
    @FXML
    Label error;

    private Integer time;
    private Integer numOfDrivers;

    public GuiController(){
        time = 0;
        numOfDrivers = 0;
    }

    @FXML
    void initialize(){
        add.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//                add.setVisible(false);
                if (numOfDrivers > 0) {
                    PrintWriter pw;
                    String driver_srl = info.getCharacters().toString();
                    info.deleteText(0, driver_srl.length());

                    String[] params = new String[3];
                    params[0] = "Client.out";
                    params[1] = "127.0.0.1";
                    params[2] = "7654";

                    try {
                        Process driver = Runtime.getRuntime().exec(params);
                        pw = new PrintWriter(driver.getOutputStream(), true);
                        pw.println(driver_srl);
                    } catch (IOException e) {
                        //todo: handle exception
                    }
                    --numOfDrivers;
                    if (numOfDrivers == 0) {
                        msgNum.setVisible(true);
                        send.setVisible(true);
                    }
                }
            }
        });
        send.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    private int last_msg = 0;
                    @Override
                    public void handle(MouseEvent event) {
                        String msg = msgNum.getCharacters().toString();
                        if (msg.equals("1")) {
                            msgNum.deleteText(0, msg.length());
                            last_msg = 1;
                        }
                        else if (last_msg == 1){
                            numOfDrivers = Integer.parseInt(msg);
                            msgNum.deleteText(0, msg.length());
                            Gui.out.println("num of drivers:" + numOfDrivers);
                            msgNum.setVisible(false);
                            send.setVisible(false);
                        }
                        if (msg.equals("9")){
                            time++;
                            clock.setText(time.toString());
                        }
                    }
                });
    }
}
