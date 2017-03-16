import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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

    /**
	 * constructor
	 */
    public Map(int width, int length, ArrayList<Point> obstacles) {
        this.width = width;
        this.length = length;
        this.obstacles = obstacles;
        this.obstacles_size = obstacles.size();
    }

    /**
	 * constructor
	 */
    public Map(int width, int length) {
        this.width = width;
        this.length = length;
        this.obstacles_size = 0;
    }

    /**
	 * create map from string of height & width
	 */
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

    /**
	 * draw the map - grid pane
	 */
    public void drawOn(Node surface) throws IOException {
//        GridPane pane = FXMLLoader.load(getClass().getResource("map.fxml"));
        GridPane pane = (GridPane) surface;

        BackgroundFill blackFill = new BackgroundFill(Color.BLACK,null,null);
        BackgroundFill whiteFill = new BackgroundFill(Color.WHITE,null,null);
        Background black = new Background(blackFill);
        Background white = new Background(whiteFill);
        Label block;
        AnchorPane anchor;
        ColumnConstraints col;
        RowConstraints row;

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < length; j++){
                Point temp = new Point(i,j);
                block = new Label();
                anchor = new AnchorPane();

                AnchorPane.setTopAnchor(block, 0.0);
                AnchorPane.setBottomAnchor(block, 0.0);
                AnchorPane.setRightAnchor(block, 0.0);
                AnchorPane.setLeftAnchor(block, 0.0);

                anchor.getChildren().add(block);
                block.setStyle("-fx-border-color: black");

                if(obstacles.contains(temp)){
                    block.setBackground(black);
                    pane.add(anchor,i,length - 1 - j);
                } else {
                    block.setBackground(white);
                    pane.add(anchor,i,length - 1 - j);
                }
            }
        }
        for(int j = 0; j< width; j++) {
            col = new ColumnConstraints();
            col.setPercentWidth(100/width);
            pane.getColumnConstraints().add(col);
        }

        for(int j = 0; j< length; j++) {
            row = new RowConstraints();
            row.setPercentHeight(100/length);
            pane.getRowConstraints().add(row);
        }
    }
}
