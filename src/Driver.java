import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by adi on 04/02/17.
 */
public class Driver {
    private Point location;
    private Image image;

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

        ImageView iv = new ImageView(image);
        iv.setFitWidth(100);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);

        grid.add(iv, location.getX(), numRows - location.getY() - 1);

    }
}
