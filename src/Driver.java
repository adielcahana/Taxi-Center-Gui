import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
	private Image image;

	public Driver(int driverNum){
		this.location = new Point(0,0);
		String imageLine = "/taxi" + ((driverNum % 5) + 1) + ".jpg";
		image = new Image(getClass().getResource(imageLine).toExternalForm());
	}

	/**
	 * set the driver location
	 */
	public void setLocation(Point loc){
		this.location = loc;
	}

	/**
	 * draw the gui with the new driver location
	 */
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
		// add the image to the grid
		ImageView iv = new ImageView(image);
		AnchorPane.setTopAnchor(iv, 0.0);
		AnchorPane.setBottomAnchor(iv, 0.0);
		AnchorPane.setRightAnchor(iv, 0.0);
		AnchorPane.setLeftAnchor(iv, 0.0);


		AnchorPane anchor = new AnchorPane();
		anchor.getChildren().add(iv);

		iv.fitWidthProperty().bind(anchor.widthProperty());
		iv.fitHeightProperty().bind(anchor.heightProperty());

		// iv.setPreserveRatio(true);
		iv.setSmooth(true);

		grid.add(anchor, location.getX(), numRows - location.getY() - 1);
	}
}
