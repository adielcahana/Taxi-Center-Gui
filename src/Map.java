import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by adi on 04/02/17.
 */
public class Map {
    private int width;
    private int length;
    private int obstacles_size;
    private ArrayList<Point> obstacles;

    public Map(int width, int length, ArrayList<Point> obstacles) {
        this.width = width;
        this.length = length;
        this.obstacles = obstacles;
        this.obstacles_size = obstacles.size();
    }

    public Map(int width, int length) {
        this.width = width;
        this.length = length;
        this.obstacles_size = 0;
    }

    public static Map deserialize(String map_srl){
        Point temp;
        int width, length, size;
        ArrayList<Point> obstacles;
        String[] parts = map_srl.split(" ");
        temp = Point.deserialize(parts[0]);
        width = temp.getX();
        length = temp.getY();
        size = Integer.parseInt(parts[1]);
        if (size != 0){
            obstacles = new ArrayList<Point>(size);
            for(int i = 2; i < parts.length; i++){
                temp = Point.deserialize(parts[i]);
                obstacles.add(temp);
            }
            return new Map(width, length, obstacles);
        }
        return new Map(width, length);
    }


    public void drawOn(Node surface) throws IOException {
//        GridPane pane = FXMLLoader.load(getClass().getResource("map.fxml"));
        GridPane pane = (GridPane) surface;

        BackgroundFill blackFill = new BackgroundFill(Color.BLACK,null,null);
        BackgroundFill whiteFill = new BackgroundFill(Color.WHITE,null,null);
        Background black = new Background(blackFill);
        Background white = new Background(whiteFill);
        Label block;
        ColumnConstraints col;
        RowConstraints row;

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < length; j++){
                Point temp = new Point(i,j);
                block = new Label();
                block.setMinSize(50, 50);
                block.setStyle("-fx-border-color: black");
                if(obstacles.contains(temp)){
                    block.setBackground(black);
                    pane.add(block,i,length - 1 - j);
                } else {
                    block.setBackground(white);
                    pane.add(block,i,length - 1 - j);
                }
            }
        }
        for(int j = 0; j< width; j++) {
            col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            pane.getColumnConstraints().add(col);
        }

        for(int j = 0; j< length; j++) {
            row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            pane.getRowConstraints().add(row);
        }
//        pane.setGridLinesVisible(true);
//        return pane;
    }
}
