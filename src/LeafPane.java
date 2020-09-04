/*
Nodo Circle es un stack pane con el texto del numero insertado y un circulo como background.
Cambia su tamano y posicion de acuerdo a la profundidad del arbol.
 */
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LeafPane<T extends Comparable> extends StackPane {
    
    // region Class properties
    
    final private String COLOR_BACKGROUND = "-fx-background-color: transparent";
    final private String FONT_NAME = "roboto";
    final private Color FONT_COLOR = Color.WHITE;

    private Circle circle;
    private Label lblNumero;
    private Color color;
    private AVLTreePane treePane; // El Pane donde el NodeCircle va a estar

    private T number;
    private LeafPane left;
    private LeafPane right;
    
    public LeafPane getPadre() {
        return padre;
    }
    
    public void setPadre(LeafPane padre) {
        this.padre = padre;
    }
    
    private LeafPane padre;
    private int depth;

    public LeafPane(AVLTreePane treePane, T number, double xOff, double yOff) {

        this.number = number;

        // Establece cual es treePane donde esta
        this.treePane=treePane;

        // Circulo como background
        circle=new Circle();

        this.depth = 1;

        // Label con el numero
        lblNumero = new Label("" + number.toString());

        // Prepara el estylo
        prepareStyle();

        this.getChildren().add(circle);
        this.getChildren().add(lblNumero);

        treePane.getChildren().add(this);

        // Trasladar el panel completo a la mitad.
        double widthTreePane = treePane.getWidth();
        double heightTreePane = treePane.getHeight();
        this.setTranslateX((widthTreePane * xOff) - this.getWidth() / 2);
        this.setTranslateY((heightTreePane - heightTreePane * (1 / (2 * yOff))) - this.getHeight() / 2);

        ScaleTransition scaleTransition=new ScaleTransition();
        scaleTransition.setNode(this);
        scaleTransition.setDuration(Duration.millis(500));
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setByX(.75);
        scaleTransition.setByY(.75);
        scaleTransition.play();
    }

    // endregion

    // region Prepare Methods

    private void prepareStyle() {

        this.setStyle(COLOR_BACKGROUND);
        this.setStyle(COLOR_BACKGROUND);
        this.setWidth(treePane.getHeight()/10);
        this.setHeight(treePane.getHeight()/10);

        // Randomizar el color del circulo de background
        this.color = Color.rgb((int) Math.floor(Math.random()*100) + 50, (int) Math.floor(Math.random()*100) + 50, (int) Math.floor(Math.random()*100) + 50);
        circle.setFill(this.color);
        circle.setRadius(this.getHeight()/2);
        circle.setCenterX(this.getWidth()/2);
        circle.setCenterY(this.getHeight()/2);
    
        //  Estilo para la label.
        lblNumero.setFont(Font.font(FONT_NAME, this.getHeight()/2));
        lblNumero.setTextFill(FONT_COLOR);

    }

    // region Get/Set method

    public T getNumber() { return this.number; }
    public void setNumber(T number) { this.number = number; }

    public LeafPane getLeft() { return this.left; }
    public void setLeft(LeafPane left) { this.left = left; }

    public LeafPane getRight() { return this.right; }
    public void setRight(LeafPane right) { this.right = right; }

    public int getDepth() { return this.depth; }
    public void setDepth(int depth) { this.depth = depth; }

    public Color getColor() { return this.color; }
    public void setColor(Color color) { this.color = color; }

    // endregion

    public void updatePosition(double xOff, double yOff, double depth) {
        double widthTreePane = treePane.getWidth();
        double heightTreePane = treePane.getHeight();
        this.setTranslateX((widthTreePane * xOff) - this.getWidth() / 2);
        this.setTranslateY((heightTreePane * (1 / (depth + 1)) * yOff));
        this.treePane.getChildren().add(this);
    }
    // endregion

    public void updateColorLabel() {
        circle.setFill(this.color);
        lblNumero.setText("" + this.number.toString());
    }
    public void resaltar(){
        FillTransition fillTransition=new FillTransition();
        fillTransition.setShape(circle);
        fillTransition.setDuration(Duration.millis(5000));
        fillTransition.setCycleCount(1);
        fillTransition.setFromValue(Color.RED);
        fillTransition.setToValue(color);
        fillTransition.play();
    }
}
