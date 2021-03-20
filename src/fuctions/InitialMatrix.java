package fuctions;

import cell.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class InitialMatrix {

    Cell[][] tab;
    List<Cell[][]> list;
    int helperW, helperH, widthPos, heightPos, timeOfBurning, extinguishingFire;

    public InitialMatrix() {
        tab = null;
        list = null;
        helperW = -1;
        helperH = -1;
        widthPos = 0;
        heightPos = 0;
        timeOfBurning = 400;         //czas palenia
        extinguishingFire = 80;      //czas gaszenia
    }

    public List<Cell[][]> makeMatrixList(Image image) {
        list = new ArrayList();
        PixelReader pixelReader = image.getPixelReader();

        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                tab = new Cell[3][];
                for (int i = 0; i < 3; i++) {
                    tab[i] = new Cell[3];
                    for (int j = 0; j < 3; j++) {
                        tab[i][j] = new Cell();
                    }
                }
                for (int i = 0; i < tab[0].length; i++) {
                    for (int j = 0; j < tab.length; j++) {
                        if (w + helperW == -1 || h + helperH == -1 || w + helperW == image.getWidth() || h + helperH == image.getHeight()) {
                            tab[i][j].setBrightness(0);      //zera na brzegach, bo zielony
                        } else {
                            tab[i][j].setBrightness(pixelReader.getColor(w + helperW, h + helperH).getBrightness());
                        }
                        helperW++;
                    }
                    helperH++;
                    helperW = -1;
                }
                helperH = -1;
                tab[1][1].setWidth(w);
                tab[1][1].setHeight(h);
                list.add(tab);
            }
        }
        return list;
    }

    public List<Cell[][]> idealMatrix(Image image) {
        list = new ArrayList();
        PixelReader pixelReader = image.getPixelReader();

        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {

                tab = new Cell[3][];
                for (int i = 0; i < 3; i++) {
                    tab[i] = new Cell[3];
                    for (int j = 0; j < 3; j++) {
                        tab[i][j] = new Cell();
                    }
                }

                for (int i = 0; i < tab[0].length; i++) {
                    for (int j = 0; j < tab.length; j++) {

                        widthPos = w + helperW;
                        heightPos = h + helperH;

                        if (widthPos == -1 || heightPos == -1 || widthPos == image.getWidth() || heightPos == image.getHeight()) {
                            tab[i][j].setColor(Color.GREEN);
                        } else {
                            if (pixelReader.getColor(widthPos, heightPos).equals(Color.GREEN)) {
                                tab[i][j].setColor(Color.GREEN);
                            } else if (pixelReader.getColor(widthPos, heightPos).equals(Color.BLUE)) {
                                tab[i][j].setColor(Color.BLUE);
                            } else if (pixelReader.getColor(widthPos, heightPos).equals(Color.WHITE)) {
                                tab[i][j].setColor(Color.WHITE);
                                tab[i][j].setExtinguishingFire(extinguishingFire);
                            } else {
                                System.out.println(pixelReader.getColor(widthPos, heightPos));
                            }
                        }
                        helperW++;
                    }
                    helperW = -1;
                    helperH++;
                }
                helperH = -1;
                tab[1][1].setWidth(w);
                tab[1][1].setHeight(h);
                list.add(tab);
            }
        }
        return list;
    }

    public double dilatation(int b, List<Cell[][]> list) {        //DYLATACJA
        for (int j = 0; j < tab[0].length; j++) {
            for (int p = 0; p < tab.length; p++) {
                if (list.get(b)[j][p].getBrightness() == 1) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public Image filter(Image image, List<Cell[][]> list) {   //filtr
        int p = 0;
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        double m = 0;
        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                    m = dilatation(p, list);
                Color color;
                try {
                    color = new Color(m, m, m, 1);
                } catch (Exception e) {
                    if (m < 0)
                        color = new Color(0, 0, 0, 1);
                    else
                        color = new Color(1, 1, 1, 1);
                }
                pixelWriter.setColor(w, h, color);
                p += 1;
            }
        }
        return wImage;
    }
}
