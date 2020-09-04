import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class AVLTreePane<T extends Comparable> extends Pane {

    final private String PANE_BACKGROUND_STYLE = "-fx-background-color:transparent";
    final private double HEIGHT_OFFSET = 55;

    private Pane parentPane;

    private LeafPane<T> root;

    public AVLTreePane(BorderPane parentPane){

        // Establce cual es el pane donde va a estar.
        this.parentPane = parentPane;

        // Preparar el estilo
        prepareStyle();

        // Anadir al pane donde debe de estar
        parentPane.getChildren().add(this);

    }

    private void prepareStyle() {

        // Cambiar color de background
        this.setStyle(PANE_BACKGROUND_STYLE);

        // Establecer el tamano de este treePane
        setHeight(parentPane.getHeight() - HEIGHT_OFFSET);
        setWidth(parentPane.getWidth());
    }

    public void addLeaf(T number) {
        this.root = insertRec(this.root, number, 0.5, 1, 2, null);
        reDraw();
        magicPrint();
    }

    private LeafPane<T> insertRec(LeafPane<T> root, T number, double xOffset, int yOffset, double multiplicador, LeafPane<T> padre) {
        if (root == null) {
            root = new LeafPane<>(this, number, xOffset, yOffset);
            root.setPadre(padre);
            if (padre!=null) {
                Branch b=new Branch(root.getPadre(), root);
                this.getChildren().add(b);
                b.toBack();
            }
        }

        if (number.compareTo(root.getNumber()) < 0) {
            root.setLeft(insertRec(root.getLeft(), number, xOffset - 1 / (multiplicador*2), yOffset + 1, multiplicador*2, root));
        } else if (number.compareTo(root.getNumber()) > 0) {
            root.setRight(insertRec(root.getRight(), number,xOffset + 1 / (multiplicador*2), yOffset + 1, multiplicador*2, root));
        } else { // Cuando es igual no hace nada.
            return root;
        }

        // Actualiza depth
        root.setDepth(1 + max(calcDepth(root.getLeft()), calcDepth(root.getRight())));

        // Lee el balance
        int balance = getBalance(root);

        // Left Left
        if (balance > 1 && number.compareTo(root.getLeft().getNumber()) < 0) {
            root.resaltar();
            return simpleRightRotate(root);
        }

        // Right right
        if (balance < -1 && number.compareTo(root.getRight().getNumber()) > 0) {
            root.resaltar();
            return simpleLeftRotation(root);
        }

        // Left Rigth case
        if (balance > 1 && number.compareTo(root.getLeft().getNumber()) > 0) {
            root.resaltar();
            root.setLeft(simpleLeftRotation(root.getLeft()));
            return simpleRightRotate(root);
        }

        // RIght Left Case
        if (balance < -1 && number.compareTo(root.getRight().getNumber()) < 0) {
            root.resaltar();
            root.setRight(simpleRightRotate(root.getRight()));
            return simpleLeftRotation(root);
        }

        // El root no cambio
        return root;
    }

    private LeafPane<T> simpleRightRotate(LeafPane<T> root) {
        LeafPane<T> temp1 = root.getLeft();
        LeafPane<T> temp2 = temp1.getRight();

        // Perform rotation
        temp1.setRight(root);
        root.setLeft(temp2);

        root.setDepth(max(calcDepth(root.getLeft()), calcDepth(root.getRight())) + 1);
        temp1.setDepth(max(calcDepth(temp1.getLeft()), calcDepth(temp1.getRight())) + 1);

        return temp1;
    }

    private LeafPane<T> simpleLeftRotation(LeafPane<T> root) {
        LeafPane<T> temp1 = root.getRight();
        LeafPane<T> temp2 = temp1.getLeft();

        // Perform rotation
        temp1.setLeft(root);
        root.setRight(temp2);

        root.setDepth(max(calcDepth(root.getLeft()), calcDepth(root.getRight())) + 1);
        temp1.setDepth(max(calcDepth(temp1.getLeft()), calcDepth(temp1.getRight())) + 1);

        return temp1;
    }

    public void delete(T number) {
        this.root = deleteLeaf(this.root, number);
        reDraw();
        magicPrint();
    }

    // Eliminar recursivo que regresa el nodo del nuevo sub arbol
    private LeafPane<T> deleteLeaf(LeafPane<T> root, T number) {
        if (root == null) {
            return root;
        }

        // Si el numero es menor el valor esta en la izqquierda
        if (number.compareTo(root.getNumber()) < 0) {
            root.setLeft(deleteLeaf(root.getLeft(), number));
        } else if (number.compareTo(root.getNumber()) > 0) {
            // Es mayor entonces esta a la derecha
            root.setRight(deleteLeaf(root.getRight(), number));
        } else {
            // Este es el nodo a eliminar

            // Nodo con un hijo o ninguno
            if (root.getLeft() == null || root.getRight() == null) {
                LeafPane<T> temp = null;

                if (temp == root.getLeft()) {
                    temp = root.getRight();
                } else {
                    temp = root.getLeft();
                }

                // NO Child case
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    // ONe chile
                    root = temp;
                }
            } else {
                // Dos hijos
                LeafPane<T> temp = minLeaf(root.getRight());
                // Problema con dos hijos.
                root.setNumber(temp.getNumber());
                root.setColor(temp.getColor());
                root.updateColorLabel();

                root.setRight(deleteLeaf(root.getRight(), temp.getNumber()));
            }
        }

        // Si el arbol tiene solo un nodo
        if (root == null) {
            return root;
        }

        // Actualiza depth
        root.setDepth(1 + max(calcDepth(root.getLeft()), calcDepth(root.getRight())));

        // Balance
        int balance = getBalance(root);

//        Left Left case
        if (balance > 1 && getBalance(root.getLeft()) >= 0) {
            return simpleRightRotate(root);
        }

        // LEFT RIGHT
        if (balance > 1 && getBalance(root.getLeft()) < 0) {
            root.setLeft(simpleLeftRotation(root.getLeft()));
            return simpleRightRotate(root);
        }

        // Right right
        if (balance < -1 && getBalance(root.getRight()) <= 0) {
            return simpleLeftRotation(root);
        }

        // Right Left
        if (balance < -1 && getBalance(root.getRight()) > 0) {
            root.setRight(simpleRightRotate(root.getRight()));
            return simpleLeftRotation(root);
        }

        return root;
    }

    // Si el arbol no esta empty regresa el menor valor en el arbol
    private LeafPane<T> minLeaf(LeafPane<T> root) {
        LeafPane<T> temp = root;

        while (temp.getLeft() != null) {
            temp = temp.getLeft();
        }

        return temp;
    }

    private int getBalance(LeafPane<T> root) {
        if (root == null) {
            return 0;
        }
        return calcDepth(root.getLeft()) - calcDepth(root.getRight());
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private int calcDepth(LeafPane<T> leaf) {
        if (leaf == null) {
            return 0;
        }
        return leaf.getDepth();
    }

    // Redraw and reposition
    public void reDraw() {
        this.getChildren().clear();
        posDraw(this.root, 0.5, 1, 2, null, this.root.getDepth(), 0);
    }

    private void posDraw(LeafPane<T> root,  double xOffset, int yOffset, double multiplicador, LeafPane<T> padre, int totalDepth, int skips) {
        if (root != null) {
            posDraw(root.getLeft(), xOffset - 1 / (multiplicador*2), yOffset + 1, multiplicador*2, root, totalDepth, skips + 1);
            posDraw(root.getRight(), xOffset + 1 / (multiplicador*2), yOffset + 1, multiplicador*2, root, totalDepth, skips + 1);
            root.updatePosition(xOffset, yOffset, totalDepth);
            root.setPadre(padre);
            if (padre!=null) {
                Branch b=new Branch(root.getPadre(), root);
                this.getChildren().add(b);
                b.toBack();
            }
        }
    }

    // MAGIC PRINT


    public void magicPrint() {
        if (this.root != null) {
            this.root.getDepth();
            int rootHeight = this.root.getDepth();
            int xSizeMax = (int) Math.pow(2, rootHeight);
            String[][] nodeArray = new String[rootHeight + 1][xSizeMax];
            sortNodesRec(nodeArray, this.root, 0, 0);
            printArray(nodeArray);
        }

    }

    private void sortNodesRec(String[][] array, LeafPane<T> temp, int xOffset, int yOffset) {
        if (temp != null) {
            array[yOffset][xOffset] = "" + temp.getNumber();
            sortNodesRec(array, temp.getLeft(), xOffset * 2, yOffset + 1);
            sortNodesRec(array, temp.getRight(), xOffset * 2 + 1, yOffset + 1);
        }
    }

    private void printArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            String result = "";
            result += initialSpaces(i, array.length);
            int xMaxNode = (int) Math.pow(2, i);
            for (int j = 0; j < xMaxNode; j++) {
                if (array[i][j] != null) {
                    // Imprime si hay rama izquierda
                    if (i + 1 < array.length) {
                        boolean hasLeft = array[i + 1][j * 2] != null;
                        result += branches(i, array.length, hasLeft);
                    }
                    // Imprime nodo
                    result += array[i][j];
                    // Imprime si hay rama derecha
                    if (i + 1 < array.length) {
                        boolean hasRight = array[i + 1][j * 2 +  1] != null;
                        result += branches(i, array.length, hasRight);
                    }
                } else {
                    // Imprime espacio si no hay nodo
                    result += branches(i, array.length, false) + "  " + branches(i, array.length, false);
                }
                // Imprime espacio libre.
                result += spacing(i, array.length);
            }
            System.out.println(result);
        }
    }

    private String initialSpaces(int yOffset, int yLength) {
        int spaceNum = (int) Math.pow(2, yLength - yOffset - 1) - 1;
        return chainOfChar(spaceNum, ' ');
    }

    private String branches(int yOffset, int yLength, boolean hasBranch) {
        int spaceNum = (int) Math.pow(2, yLength - yOffset - 1) - 1;
        if (hasBranch) {
            return chainOfChar(spaceNum, '_');
        } else {
            return  chainOfChar(spaceNum, ' ');
        }
    }

    private String spacing(int yOffset, int yLength) {
        int spaceNum = (int) Math.pow(2, yLength - yOffset);
        return chainOfChar(spaceNum, ' ');
    }

    private String chainOfChar(int length, char usingChar) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += usingChar;
        }
        return result;
    }



}
