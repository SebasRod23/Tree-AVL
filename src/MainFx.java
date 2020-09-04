import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainFx extends Application {
	// Propiedades de layout
	final public String NAME_STAGE = "AVL Tree";
	final public String MAIN_COLOR = "-fx-background-color: black";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		
		primaryStage.setResizable(false);
		Rectangle2D screen= Screen.getPrimary().getVisualBounds();
		primaryStage.setHeight(screen.getHeight());
		primaryStage.setWidth(screen.getWidth());
		primaryStage.getIcons().add(new Image("tree.png"));
		// Configurar la ventana principal, Nombre y tamano
		primaryStage.setTitle(NAME_STAGE);

		// El Pane que tiene a todos los otros panes
		BorderPane mainPane = new BorderPane();

		// El pane de control
		ControlsPane<Integer> bottomPane = new ControlsPane<>(mainPane);

		// La escena primero para dar tamano a main pane
		// La escena
		Scene scene = new Scene(mainPane);

		// Establecer la escena.
		primaryStage.setScene(scene);

		// Mostrar el stage
		primaryStage.show();

		// El pane del arbol
		AVLTreePane<Integer> topPane = new AVLTreePane<>(mainPane);

		// Preparar el estilo de los panes.
		preparePanes(mainPane, topPane, bottomPane);


	}

	// region Prepare methods

	public void preparePanes(BorderPane mainPane, AVLTreePane<Integer> topPane, ControlsPane<Integer> bottomPane) {

		mainPane.setStyle(MAIN_COLOR);

		// Anadir los eventos a los bottones del panel de control
		bottomPane.getBttnAdd().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent a) {
				// Anade numero en el text field al arbol
				addToTree(bottomPane, topPane);
			}
		});
		bottomPane.getBttnDelete().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent a) {
				// Remueve el elemento en el panel de control del arbol.
				deleteFromTree(bottomPane, topPane);
			}
		});

		// Habilita enter
        bottomPane.getTxtNumber().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) addToTree(bottomPane,topPane);
        });
	}

	// endregion

	// region Agregar/Remover Metodos

	// Agregar un elemento al arbol
	public void addToTree(ControlsPane<Integer> controlsPane, AVLTreePane<Integer> treePane) {
		Integer number = controlsPane.getNumber();
		treePane.addLeaf(number);
		controlsPane.getTxtNumber().setText("");
	}

	// Remover un elemento del arbol
	public void deleteFromTree(ControlsPane<Integer> controlsPane, AVLTreePane<Integer> treePane) {
		treePane.delete(controlsPane.getNumber());
		controlsPane.getTxtNumber().setText("");
	}

	// endregion
}
