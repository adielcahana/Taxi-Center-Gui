import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

//    public Pane getGui(){
//        TilePane pane = new TilePane();
//        BackgroundFill blackFill = new BackgroundFill(Color.BLACK,null,null);
//        BackgroundFill whiteFill = new BackgroundFill(Color.WHITE,null,null);
//        Background black = new Background(blackFill);
//        Background white = new Background(whiteFill);
//        Label block;
//
//        pane.setMaxSize( 300, 300);
//        pane.setPrefRows(width - 1);
//        pane.setPadding(new Insets(5, 5, 5, 5));
//        pane.setVgap(15);
//        pane.setHgap(4);
//        pane.setStyle("-fx-background-color: DAE6F3;");
//
//        for(int i = 0; i <width; i++){
//            for(int j = 0; j<length; j++){
//                Point temp = new Point(i,j);
//                block = new Label();
//                block.setRotate(90);
//                block.setStyle("-fx-border-color: black");
//                block.setText(Integer.toString(i) + "," + Integer.toString(j));
//                if(obstacles.contains(temp)){
//                    block.setBackground(black);
//                    pane.getChildren().add(block);
//                } else {
//                    block.setBackground(white);
//                    StackPane sp = new StackPane();
//                    sp.getChildren().add(block);
//                    pane.getChildren().add(sp);
//                }
//            }
//        }
//        pane.setRotate(-90);
//        return pane;
//    }

    public Pane getGui(){
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        BackgroundFill blackFill = new BackgroundFill(Color.BLACK,null,null);
        BackgroundFill whiteFill = new BackgroundFill(Color.WHITE,null,null);
        Background black = new Background(blackFill);
        Background white = new Background(whiteFill);
        Label block;

        pane.setPadding(new Insets(25, 25, 25, 25));
        pane.setVgap(10);
        pane.setHgap(10);
//        pane.setStyle("-fx-background-color: DAE6F3;");

        for(int i = 0; i <width; i++){
            for(int j = 0; j<length; j++){
                Point temp = new Point(i,j);
                block = new Label();
                block.setRotate(90);
                block.setStyle("-fx-border-color: black");
                block.setText(Integer.toString(i) + "," + Integer.toString(j));
                if(obstacles.contains(temp)){
                    block.setBackground(black);
                    pane.add(block,i,j);
                } else {
                    block.setBackground(white);
                    StackPane sp = new StackPane();
                    sp.getChildren().add(block);
                    pane.add(sp,i,j);
                }
            }
        }
        pane.setRotate(-90);
        pane.setGridLinesVisible(true);
        return pane;
    }
}
