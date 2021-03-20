package gui;

import cell.Cell;
import fuctions.Actions;
import fuctions.InitialMatrix;
import help.Fire;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class MainWindow implements Initializable {
    @FXML
    public AnchorPane rootPane;
    @FXML
    public Canvas canvas;
    @FXML
    TextField textField;
    @FXML
    TextField textField1;
    @FXML
    public ChoiceBox choiceBoxwind;
    @FXML
    public ChoiceBox choiceBoxSeason;
    @FXML
    public ChoiceBox choiceBoxClimat;
    @FXML
    public RadioButton radio1;
    @FXML
    public RadioButton radio2;


    Actions actions = new Actions();
    InitialMatrix initialMatrix = new InitialMatrix();
    GraphicsContext gc;


    private String imagePath = "obrazekDark.bmp";
    private Image image;
    private boolean byteCheck;
    WritableImage wImage;
    PixelWriter pixelWriter;
    PixelReader pixelReader;
    private boolean startStop = true;
    private int climate, wind, season, timeOfBurning = 400, radioFlag = 0, extinguishingFire = 80,x_center, y_center,r;
    double x = -5, y = -5;
    List<Cell[][]> oldList;


    public void dilatation() {     //dylatacja
        gc.clearRect(0, 0, 600, 330);
        if (byteCheck == false) {
            image = actions.binarization(image, Double.parseDouble(textField.getText()));
        }
        List<Cell[][]> list = initialMatrix.makeMatrixList(image);
        image = initialMatrix.filter(image, list);
        gc.drawImage(image, 0, 0);
        byteCheck = true;
    }

    public void makeGreenBlueWithDilatationX2() {
        gc.clearRect(0, 0, 600, 330);
        dilatation();
        dilatation();
        image = actions.greenBlue(image);
        oldList = initialMatrix.idealMatrix(image);
        gc.drawImage(image, 0, 0);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        textField.setText("0.3");
        textField.setVisible(false);
        textField1.setText("1");
        image = new Image((imagePath));
        gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        byteCheck = false;
        choiceBoxwind.setValue("No wind");
        choiceBoxSeason.setValue("Spring");
        choiceBoxClimat.setValue("Continental");
        final ToggleGroup group = new ToggleGroup();
        radio1.setToggleGroup(group);
        radio1.setSelected(true);
        radio2.setToggleGroup(group);

        //wybranie akcji dla kliknięcia myszką
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (radio1 == group.getSelectedToggle())
                    radioFlag = 0;
                else if (radio2 == group.getSelectedToggle())
                    radioFlag = 1;
                System.out.println(radioFlag);
            }
        });

        canvas.setOnMouseClicked(event -> {
            wImage = new WritableImage(
                    (int) image.getWidth(),
                    (int) image.getHeight());

            pixelWriter = wImage.getPixelWriter();
            pixelReader = image.getPixelReader();
            x = event.getX();
            y = event.getY();

            if (radioFlag == 0) {
                for (int hi = 0; hi < image.getHeight(); hi++) {
                    for (int wi = 0; wi < image.getWidth(); wi++) {
                        pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
                    }
                }
                pixelWriter.setColor((int) x, (int) y, Color.RED);
                image = wImage;

                if(pixelReader.getColor((int) x, (int) y).equals(Color.BLUE))
                    System.err.println("river");
                else
                    System.out.println("fire");

                    oldList.get((600 * (int) y) + (int) x)[1][1].setColor(Color.RED);
                    oldList.get((600 * (int) y) + (int) x)[1][1].setRemainingTime(timeOfBurning);

            }
            else if (radioFlag== 1){
                x_center = (int) x;
                y_center = (int) y;

                for (int hi = 0; hi < image.getHeight(); hi++) {
                    for (int wi = 0; wi < image.getWidth(); wi++) {
                        pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
                    }
                }

                pixelWriter.setColor((int) x, (int) y, Color.GRAY);
                image = wImage;
                if(pixelReader.getColor((int) x, (int) y).equals(Color.BLUE))
                    System.err.println("river");
                else
                    System.out.println("put firefighter");

                oldList = Actions.fireFighting(oldList,15,x_center,y_center);
            }
            gc.drawImage(image, 0, 0);
        });
    }

    public void start() {

        startStop = true;

        Thread thread = new Thread(() -> {
            Fire fire;
            while (startStop) {
                fire = actions.fire(image, oldList, wind, Double.parseDouble(textField1.getText()), season, climate);
                image = fire.getImage();
                oldList = fire.getList();
                gc.drawImage(image, 0, 0);
            }
        });
        thread.start();
    }

    public void stop() {
        startStop = false;
    }

    public void random() {      //randomowy pozar
        wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());

        pixelWriter = wImage.getPixelWriter();
        pixelReader = image.getPixelReader();
        int x, y, random;
        random = ThreadLocalRandom.current().nextInt(5, 20 + 1);

        for (int hi = 0; hi < image.getHeight(); hi++) {
            for (int wi = 0; wi < image.getWidth(); wi++) {
                pixelWriter.setColor(wi, hi, pixelReader.getColor(wi, hi));
            }
        }
        for (int i = 0; i < random; i++) {
            x = ThreadLocalRandom.current().nextInt(0, 599 + 1);
            y = ThreadLocalRandom.current().nextInt(0, 330 + 1);
            if (!pixelReader.getColor(x, y).equals(Color.BLUE) && !pixelReader.getColor(x, y).equals(Color.BLACK) && !pixelReader.getColor(x, y).equals(Color.DARKGREEN)) {
                pixelWriter.setColor(x, y, Color.RED);
                oldList.get((600 *  y) +  x)[1][1].setColor(Color.RED);
                oldList.get((600 *  y) +  x)[1][1].setRemainingTime(timeOfBurning);
                System.out.println("Fire");
            }
        }
        image = wImage;
        gc.drawImage(image, 0, 0);
    }

    public void reset() {
        gc.clearRect(0, 0, 600, 330);
        image = new Image(imagePath);
        wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        pixelWriter = wImage.getPixelWriter();
        gc.drawImage(image, 0, 0);
        byteCheck = false;
        oldList = null;
    }

    public void checkChoiceBoxWind() {
        choiceBoxwind.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            wind = t1.intValue();
        });
    }

    public void checkChoiceBoxSeason() {
        choiceBoxSeason.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {
            season = t1.intValue();

        });
    }

    public void checkChoiceBoxClimat(){
        choiceBoxClimat.getSelectionModel().selectedIndexProperty().addListener(((observableValue, number, t1) ->
                climate =t1.intValue()));
    }

    public void airplane() {
        for (int j = 0; j < 600; j++) {
            for (int i = 150; i < 170; i++) {      // poziomo
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
            for (int i = 50; i < 70; i++) {
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
            for (int i = 250; i < 270; i++) {
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
        }
    }
    public void airplane2() {      //pionowo
        for (int j = 290; j < 310; j++) {
            for (int i = 1; i < 330; i++){
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
        }
        for (int j = 90; j < 110; j++) {
            for (int i = 1; i < 330; i++){
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
        }
        for (int j = 490; j <510; j++) {
            for (int i = 1; i < 330; i++){
                if (!oldList.get((600 * i) + j)[1][1].getColor().equals(Color.BLUE))
                    oldList.get((600 * i) + j)[1][1].setColor(Color.DARKGREEN);
            }
        }
    }
}
