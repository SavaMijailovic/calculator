import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Calculator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int BACKSPACE = 8;
    private static final int DEL = 127;

    static final double fontSize = 18;
    static final double tfWidth = 200;
    static final double tfHeight = 40;
    static Expression expression = Expression.get();

    private static TextField tfx;
    private static Label lbxUserMessage;

    private static TextField tfy;
    private static Label lbyUserMessage;

    private static TextField tfResult;
    private static TextField tfTmp;
    private static Label lbTmp;

    private static void setButtons(Button... buttons) {
        for (Button bt : buttons) {
            bt.setPrefSize(50, 50);
            bt.setFont(Font.font(fontSize));
            bt.setFocusTraversable(false);
            char c = bt.getText().charAt(0);
            if (Character.isDigit(c) || c == 'C' || c == '.') {
                bt.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(2), new Insets(1))));
            }
            else {
                bt.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(2), new Insets(1))));
            }
        }
    }

    private static void setButtonsOnAction(Button... buttons) {
        for (Button bt : buttons) {
            bt.setOnAction(e -> action(bt));
        }
    }

    private static void action(Button button) {
        if (tfTmp.getLength() > 300) return;
        tfTmp.insertText(tfTmp.getCaretPosition(), button.getText());
        lbTmp.setVisible(false);
    }

    private static void performOperation(Operation op) {
        tfResult.clear();
        lbxUserMessage.setVisible(false);
        lbyUserMessage.setVisible(false);
        boolean correct = true;

        expression.setOp(op);
        try {
            expression.setX(Double.parseDouble(tfx.getText()));
        }
        catch (NullPointerException | NumberFormatException e) {
            lbxUserMessage.setVisible(true);
            correct = false;
        }
        if (op != Operation.SQRT) {
            try {
                expression.setY(Double.parseDouble(tfy.getText()));
            }
            catch (NullPointerException | NumberFormatException e) {
                lbyUserMessage.setVisible(true);
                correct = false;
            }
        }
        else {
            tfy.clear();
        }

        if (!correct) return;

        if (op == Operation.EXP) {
            if (expression.getX() < 0 && !Expression.isInt(expression.getY())) {
                lbxUserMessage.setVisible(true);
                return;
            }
        }
        else if (op == Operation.SQRT) {
            tfy.clear();
            if (expression.getX() < 0) {
                lbxUserMessage.setVisible(true);
                return;
            }
        }
        else if (op == Operation.DIVISION) {
            if (expression.getY() == 0) {
                lbyUserMessage.setVisible(true);
                return;
            }
        }

        else if (op == Operation.DIV || op == Operation.MOD) {
            if (!Expression.isInt(expression.getX())) {
                lbxUserMessage.setVisible(true);
                correct = false;
            }
            if (!Expression.isInt(expression.getY()) || expression.getY() == 0) {
                lbyUserMessage.setVisible(true);
                correct = false;
            }
        }

        if (!correct) return;
        tfResult.setText(expression.toString());
    }

    public static void setInputLine(TextField tf, Label lb) {
        tf.setEditable(false);
        lb.setFont(Font.font(fontSize+5));
        lb.setPrefWidth(30);
        lb.setAlignment(Pos.CENTER_RIGHT);
        tf.setPrefSize(tfWidth, tfHeight);
        tf.setFont(Font.font(fontSize));
    }

    public static void setErrorLine(HBox hb, Label lb) {
        lb.setVisible(false);
        lb.setTextFill(Color.RED);
        hb.setPadding(new Insets(0, 0, 0 , 30));
        hb.setAlignment(Pos.CENTER);
    }

    private static void checkChar(char c) {
        if (!Character.isDigit(c) && c != '-' && c != '.' && c != BACKSPACE && c != DEL) {
            return;
        }
        if (Character.isDigit(c)) {
            lbTmp.setVisible(false);
            tfTmp.appendText("" + c);
        }
        else if (c == '-' || c == '.') {
            lbTmp.setVisible(false);
            if (tfTmp.getLength() == 0) {
                tfTmp.appendText("" + c);
                return;
            }

            if (c == '.') {
                if (!tfTmp.getText().substring(0, tfTmp.getLength()).contains(".")) {
                    tfTmp.appendText("" + c);
                }
            }
        }
        else if (c == BACKSPACE && tfTmp.getLength() > 0) {
            tfTmp.setText(tfTmp.getText(0, tfTmp.getLength() - 1));
        }
        else if (c == DEL && tfTmp.getLength() > 0) {
            tfTmp.setText(tfTmp.getText(1, tfTmp.getLength()));
        }
    }

    /*
    private static void checkChar(char c) {
        lbTmp.setVisible(false);
        if (!Character.isDigit(c) && c != '-' && c != '.') {
            if (c >= 32 && c != 127) {
                tfTmp.deletePreviousChar();
            }
            return;
        }
        if (Character.isDigit(c)) return;
        if (c == '-' || c == '.') {
            if (tfTmp.getLength() == 1) return;

            if (c == '-') {
                if (tfTmp.getCaretPosition() == 1 && !tfTmp.getText().substring(1, tfTmp.getLength()).contains("-")) return;
            }

            if (c == '.') {
                String str = tfTmp.getText().substring(0, tfTmp.getCaretPosition() - 1);
                str += (tfTmp.getText().substring(tfTmp.getCaretPosition(), tfTmp.getLength()));
                if (!str.contains(".")) return;
            }
        }
        tfTmp.deletePreviousChar();
    }
    */

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(0));

        HBox hbTop = new HBox();

        VBox vbLeft = new VBox(30);
        VBox vbRight = new VBox();

        VBox vbx = new VBox();
        HBox hbx = new HBox();
        Label lbx = new Label("x:");
        tfx = new TextField();
        setInputLine(tfx, lbx);
        hbx.getChildren().addAll(lbx, tfx);

        lbxUserMessage = new Label("Incorrect input.");
        HBox hbxUserMessage = new HBox();
        setErrorLine(hbxUserMessage, lbxUserMessage);
        hbxUserMessage.getChildren().add(lbxUserMessage);
        vbx.getChildren().addAll(hbx, hbxUserMessage);

        VBox vby = new VBox();
        HBox hby = new HBox();
        Label lby = new Label("y:");
        tfy = new TextField();
        setInputLine(tfy, lby);
        hby.getChildren().addAll(lby, tfy);

        HBox hbyUserMessage = new HBox();
        lbyUserMessage = new Label("Incorrect input.");
        setErrorLine(hbyUserMessage, lbyUserMessage);
        hbyUserMessage.getChildren().add(lbyUserMessage);
        vby.getChildren().addAll(hby, hbyUserMessage);

        HBox hbDelete = new HBox();
        Button btDelete = new Button("Delete");
        btDelete.setFocusTraversable(false);
        hbDelete.setPadding(new Insets(0, 0, 0, 30));
        hbDelete.setAlignment(Pos.CENTER);
        hbDelete.getChildren().add(btDelete);
        vbLeft.setPadding(new Insets(30, 0, 0, 0));
        vbLeft.getChildren().addAll(vbx, vby, hbDelete);

        HBox hbButtons1 = new HBox();
        hbButtons1.setPadding(new Insets(0, 0, 0, 20));
        Button bt1 = new Button("1");
        Button bt2 = new Button("2");
        Button bt3 = new Button("3");
        Button btAdd = new Button("+");
        setButtons(bt1, bt2, bt3, btAdd);
        hbButtons1.getChildren().addAll(bt1, bt2, bt3, btAdd);

        HBox hbButtons2 = new HBox();
        hbButtons2.setPadding(new Insets(0, 0, 0, 20));
        Button bt4 = new Button("4");
        Button bt5 = new Button("5");
        Button bt6 = new Button("6");
        Button btSub = new Button("-");
        setButtons(bt4, bt5, bt6, btSub);
        hbButtons2.getChildren().addAll(bt4, bt5, bt6, btSub);

        HBox hbButtons3 = new HBox();
        hbButtons3.setPadding(new Insets(0, 0, 0, 20));
        Button bt7 = new Button("7");
        Button bt8 = new Button("8");
        Button bt9 = new Button("9");
        Button btMul = new Button("×");
        setButtons(bt7, bt8, bt9, btMul);
        hbButtons3.getChildren().addAll(bt7, bt8, bt9, btMul);

        HBox hbButtons4 = new HBox();
        hbButtons4.setPadding(new Insets(0, 0, 0, 20));
        Button btClear = new Button("C");
        Button bt0 = new Button("0");
        Button btDot = new Button(".");
        Button btDivision = new Button("÷");
        setButtons(btClear, bt0, btDot, btDivision);
        hbButtons4.getChildren().addAll(btClear, bt0, btDot, btDivision);

        HBox hbButtons5 = new HBox();
        hbButtons5.setPadding(new Insets(0, 0, 0, 20));
        Button btExp = new Button("xʸ");
        Button btSqrt = new Button("√x");
        Button btDiv = new Button("/");
        Button btMod = new Button("%");
        setButtons(btAdd, btSub, btMul, btDivision, btExp, btSqrt, btDiv, btMod);
        hbButtons5.getChildren().addAll(btExp, btSqrt, btDiv, btMod);

        vbRight.getChildren().addAll(hbButtons1, hbButtons2, hbButtons3, hbButtons4, hbButtons5);
        hbTop.getChildren().addAll(vbLeft, vbRight);

        HBox hbBottom = new HBox();
        hbBottom.setPadding(new Insets(0, 0, 20, 0));
        hbBottom.setAlignment(Pos.CENTER);

        tfResult = new TextField(); tfResult.setPrefSize(2*tfWidth, tfHeight); tfResult.setFont(Font.font(fontSize));
        tfResult.setEditable(false);

        hbBottom.getChildren().addAll(tfResult);

        root.getChildren().addAll(hbTop, hbBottom);

        tfTmp = tfx;
        lbTmp = lbxUserMessage;

        //Actions
        tfx.setOnMouseReleased(e -> {
            tfTmp = tfx;
            lbTmp = lbxUserMessage;
        });

        tfy.setOnMouseReleased(e -> {
            tfTmp = tfy;
            lbTmp = lbyUserMessage;
        });

        tfx.setOnKeyReleased(e -> {
            if (tfx.isFocused()) {
                tfTmp = tfx;
            }
            if (!e.getText().isEmpty()) {
                checkChar(e.getText().charAt(0));
            }
        });

        tfy.setOnKeyReleased(e -> {
            if (tfy.isFocused()) {
                tfTmp = tfy;
            }
            if (!e.getText().isEmpty()) {
                checkChar(e.getText().charAt(0));
            }
        });

        btClear.setOnAction(e -> {
            tfx.clear();
            tfy.clear();
            lbxUserMessage.setVisible(false);
            lbyUserMessage.setVisible(false);
            tfResult.clear();
        });

        btDelete.setOnAction(e -> {
            if (tfTmp.getLength() > 0) {
                tfTmp.setText(tfTmp.getText(0, tfTmp.getLength() - 1));
                lbTmp.setVisible(false);
            }
        });

        setButtonsOnAction(bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0);
        btDot.setOnAction(e -> {
            if (tfTmp != null && !tfTmp.getText().contains(".")) {
                action(btDot);
            }
        });

        btAdd.setOnAction(e -> performOperation(Operation.ADD));
        btSub.setOnAction(e -> {
            if (tfTmp.getLength() == 0 || tfTmp.getCaretPosition() == 0 && (tfTmp.getLength() > 0 && tfTmp.getText().charAt(0) != '-')) {
                action(btSub);
                return;
            }
            performOperation(Operation.SUB);
        });
        btMul.setOnAction(e -> performOperation(Operation.MUL));
        btDivision.setOnAction(e -> performOperation(Operation.DIVISION));
        btExp.setOnAction(e -> performOperation(Operation.EXP));
        btSqrt.setOnAction(e -> performOperation(Operation.SQRT));
        btMod.setOnAction(e -> performOperation(Operation.MOD));
        btDiv.setOnAction(e -> performOperation(Operation.DIV));

        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Calculator");
        stage.show();
    }
}
