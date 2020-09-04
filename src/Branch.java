/*
Branch es una linea que calcula las dos esquinas que tiene que unir
 */
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Shadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Branch extends Line {

	// region Class Properties

	final private double LINE_WIDTH = 2;

	public Branch(LeafPane l1, LeafPane l2){

		// Set el ancho de la linea
		setStrokeWidth(LINE_WIDTH);
		this.setStroke(Color.WHITE);

		// Posicionar la linea
		this.startXProperty().bind(l1.translateXProperty().add(l1.widthProperty().divide(2)));
		this.startYProperty().bind(l1.translateYProperty().add(l1.heightProperty().divide(2)));
		this.endXProperty().bind(l2.translateXProperty().add(l2.widthProperty().divide(2)));
		this.endYProperty().bind(l2.translateYProperty().add(l2.heightProperty().divide(2)));

		// Transiicon
		FadeTransition fadeTransition = new FadeTransition();
		fadeTransition.setNode(this);
		fadeTransition.setCycleCount(Animation.INDEFINITE);
		fadeTransition.setDuration(Duration.millis(1000));
		fadeTransition.setToValue(0.1);
		fadeTransition.play();
	}

	// endregion
}
