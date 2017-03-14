import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;


/**
 * Created by adi on 04/02/17.
 */
public class Driver {
    private Point location;
    private ImageView iv;
    
    public Driver(int driverNum){
    	this.location = new Point(0,0);
    	String imageLine = "/resources/taxi" + driverNum + ".jpg";
//    	InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(imageLine);
    	this.iv = new ImageView(new Image(imageLine));
    }

    public void setLocation(Point loc){
        this.location = loc;
    }

    public void drawOn(Node node){
        GridPane grid = (GridPane) node;

        //count number of rows
        int numRows = grid.getRowConstraints().size();
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Node child = grid.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }

        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        grid.add(iv, location.getX(), numRows - location.getY() - 1);

    }
}
