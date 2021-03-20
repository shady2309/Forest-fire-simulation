package fuctions;

import cell.Cell;
import help.Fire;
import help.Point;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Actions {

    public Image binarization(Image image, double threshold) {       //binaryzacja
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                Color color = pixelReader.getColor(w, h);
                if (color.getBrightness() > threshold) {
                    color = color.BLACK;
                } else {
                    color = color.WHITE;
                }
                pixelWriter.setColor(w, h, color);
            }
        }
        return wImage;
    }

    public Image greenBlue(Image image) {           //zmiana z greyscale na zielono-niebieski
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = wImage.getPixelWriter();

        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                Color color = pixelReader.getColor(w, h);
                if (color.equals(Color.WHITE)) {
                    color = Color.BLUE;
                } else {
                    color = Color.GREEN;
                }
                pixelWriter.setColor(w, h, color);
            }
        }
        return wImage;
    }

    private static final int timeOfBurning = 400;
    private static final int extinguishingFire = 80;


    public Cell[][] checkNeighbors(List<Cell[][]> list, int b, int wind, double windF, int season,int climate) {

        double windForce = windF;  //siła wiatru
        if (wind == 0)
            windForce = 1;

        double step;
        double burningSpeed = 1;
        double probability = 1;
        //probability - szansa ze drzewo sie zapali
        //burningSpeed - szybkosc spalenia

        //pory roku
        if (season == 0) {          //wiosna
            probability = 1.4;
            burningSpeed = 0.6;
        } else if (season == 1) {   //lato
            probability = 2.0;
            burningSpeed = 1.4;
        } else if (season == 2) {   //jesien
            probability = 1.0;
            burningSpeed = 0.8;
        } else if (season == 3) {    //zima
            probability = 0.1;
            burningSpeed = 0.5;
        }

        double rainfall=1;
        double humidity=1;
        //klimat & wilgotnosc
        if (climate == 0) {          //rownikowy
            rainfall=2.0;
            humidity=1.5;
        } else if (climate == 1) {   //zwrotnikowy
            rainfall=0.2;
            humidity=0.4;
        } else if (climate == 2) {   //podzwrotnikowy
            rainfall=1.2;
            humidity=0.8;
        } else if (climate == 3) {   //umiarkowany
            rainfall=1.6;
            humidity=0.2;
        }

        //licznik sasiadow, wynik z losowania
        int counter = 0, randomNum;
        Cell[][] cell;

        //komorka
        cell = new Cell[3][];

        //wypelnienie komorki
        for (int i = 0; i < 3; i++) {
            cell[i] = new Cell[3];
            for (int j = 0; j < 3; j++) {
                cell[i][j] = new Cell();
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (list.get(b)[i][j].getColor().equals(Color.GREEN)) {
                    cell[i][j].setExtinguishingFire(list.get(b)[i][j].getExtinguishingFire());
                    cell[i][j].setColor(Color.GREEN);

                } else if (list.get(b)[i][j].getColor().equals(Color.RED)) {
                    if (i == 1 && j == 1) {   //srodek macierzy
                        cell[i][j].setColor(Color.RED);
                        cell[i][j].setRemainingTime(list.get(b)[i][j].getRemainingTime());
                        cell[i][j].setExtinguishingFire(list.get(b)[i][j].getExtinguishingFire());
                        continue;
                    }
                    cell[i][j].setColor(Color.RED);
                    cell[i][j].setRemainingTime(list.get(b)[i][j].getRemainingTime());
                    cell[i][j].setExtinguishingFire(list.get(b)[i][j].getExtinguishingFire());

                    //kierunki wiatru
                    switch (wind) {
                        case 0:
                            counter++;
                            break;
                        case 1:
                            if (i == 0)
                                counter++;
                            break;
                        case 2:
                            if (i == 2)
                                counter++;
                            break;
                        case 3:
                            if (j == 2)
                                counter++;
                            break;
                        case 4:
                            if (j == 0)
                                counter++;
                            break;
                        case 5:
                            if ((i == 0 && j == 2) || (i == 0 && j == 1) || (i == 1 && j == 2))
                                counter++;
                            break;
                        case 6:
                            if (i == 0 && j == 0 || (i == 0 && j == 1) || (i == 1 && j == 0))
                                counter++;
                            break;
                        case 7:
                            if ((i == 2 && j == 2) || (i == 2 && j == 1) || (i == 1 && j == 2))
                                counter++;
                            break;
                        case 8:
                            if ((i == 2 && j == 0) || (i == 2 && j == 1) || (i == 1 && j == 0))
                                counter++;
                            break;
                    }

                } else if (list.get(b)[i][j].getColor().equals(Color.BLACK)) {
                    cell[i][j].setColor(Color.BLACK);
                } else if (list.get(b)[i][j].getColor().equals(Color.BLUE)) {
                    cell[i][j].setColor(Color.BLUE);
                } else if (list.get(b)[i][j].getColor().equals(Color.GRAY)) {
                    cell[i][j].setColor(Color.GRAY);
                } else if (list.get(b)[i][j].getColor().equals(Color.DARKGREEN)) {
                    cell[i][j].setColor(Color.DARKGREEN);
                    cell[i][j].setExtinguishingFire((list.get(b)[i][j].getExtinguishingFire()));
                }
            }
        }

        double humRain = humidity*rainfall;

        if (cell[1][1].getColor().equals(Color.RED)) {       //spalanie drzewa

            if (cell[1][1].getRemainingTime() <= 0) {         //juz spalone - wiec czarne
                cell[1][1].setColor(Color.BLACK);

            } else if (counter == 0) {        //pierwsze podpalone
                step = timeOfBurning / (8.0 / burningSpeed*humRain);    //burningSpeed * wilgotnosc * opady
                cell[1][1].decTime(step);
            } else if (counter == 1 || counter == 2) {        //1 lub 2 sie palą obok
                step = timeOfBurning / (7.0 / burningSpeed*humRain);
                cell[1][1].decTime(step);
            } else if (counter == 3) {
                step = timeOfBurning / (6.0 / burningSpeed*humRain);
                cell[1][1].decTime(step);
            } else if (counter == 4) {
                step = timeOfBurning / (5.0 / burningSpeed*humRain);
                cell[1][1].decTime(step);
            } else if (counter == 5 || counter == 6) {
                step = timeOfBurning / (4.0 / burningSpeed*humRain);
                cell[1][1].decTime(step);
            } else if (counter == 7 || counter == 8) {
                step = timeOfBurning / (3.0 / burningSpeed*humRain);
                cell[1][1].decTime(step);
            }

            //strazak
            if (cell[1][1].getExtinguishingFire() != -1) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
                if (randomNum == 0)
                    cell[1][1].decPutOutFire(20);
                if (cell[1][1].getExtinguishingFire() <= 0) {
                    cell[1][1].setColor(Color.DARKGREEN);
                }
            }
        }

        if (cell[1][1].getColor().equals(Color.GREEN)) {       //podpalanie
            randomNum = ThreadLocalRandom.current().nextInt(0, 1000 + 1);

            if (counter == 1 || counter == 2) {               //plonący sąsiedzi
                if (randomNum < 50 * windForce * probability) {    //szansa na podpalenie
                    cell[1][1].setColor(Color.RED);
                    cell[1][1].setRemainingTime(timeOfBurning);
                }
            }
            if (counter == 3 || counter == 4) {
                if (randomNum < 125 * windForce * probability) {
                    cell[1][1].setColor(Color.RED);
                    cell[1][1].setRemainingTime(timeOfBurning);
                }
            }
            if (counter == 5 || counter == 6) {
                if (randomNum < 200 * windForce * probability) {
                    cell[1][1].setColor(Color.RED);
                    cell[1][1].setRemainingTime(timeOfBurning);
                }
            }
            if (counter == 7 || counter == 8) {
                if (randomNum < 250 * windForce * probability) {
                    cell[1][1].setColor(Color.RED);
                    cell[1][1].setRemainingTime(timeOfBurning);
                }
            }
            if (cell[1][1].getExtinguishingFire() != -1) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
                if (randomNum == 0)
                    cell[1][1].decPutOutFire(20);
                if (cell[1][1].getExtinguishingFire() <= 0) {
                    cell[1][1].setColor(Color.DARKGREEN);
                }
            }
        }
        return cell;
    }

    public List<List<Cell[][]>> cellList(List<Cell[][]> list, int wind, double sW, int season, int climat) {   //listy komorek
        List<List<Cell[][]>> newList = new ArrayList<>();
        List<Cell[][]> nl = null;
        int help = 0;
        for (int i = 0; i < 330; i++) {
            nl = new ArrayList<>();
            for (int j = 0; j < 600; j++) {
                nl.add(checkNeighbors(list, help, wind, sW, season, climat));
                help++;
            }
            newList.add(nl);
        }
        return newList;
    }

    public Fire fire(Image image, List<Cell[][]> list, int wind, double sW, int season, int climat) {   //ogieeeeeń
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        List<List<Cell[][]>> oldList = cellList(list, wind, sW, season, climat);
        List<Cell[][]> newList = new ArrayList<>();
        Cell[][] tab;
        int pomW = -1, pomH = -1, wpos = 0, hpos = 0;
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
                        wpos = w + pomW;
                        hpos = h + pomH;
                        if (wpos == -1 || hpos == -1 || wpos == image.getWidth() || hpos == image.getHeight()) {
                            tab[i][j].setColor(Color.GREEN);      //warunek brzegowy - zielono
                        } else {
                            try {
                                //przekopiowanie danych na nową listę
                                if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.GREEN)) {
                                    tab[i][j].setColor(Color.GREEN);
                                    tab[i][j].setExtinguishingFire(oldList.get(hpos).get(wpos)[1][1].getExtinguishingFire());
                                } else if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.BLUE)) {
                                    tab[i][j].setColor(Color.BLUE);
                                } else if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.RED)) {
                                    tab[i][j].setColor(Color.RED);
                                    tab[i][j].setRemainingTime(oldList.get(hpos).get(wpos)[i][j].getRemainingTime());
                                    tab[i][j].setExtinguishingFire(oldList.get(hpos).get(wpos)[1][1].getExtinguishingFire());
                                } else if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.BLACK)) {
                                    tab[i][j].setColor(Color.BLACK);
                                    tab[i][j].setRemainingTime(oldList.get(hpos).get(wpos)[1][1].getRemainingTime());
                                } else if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.GRAY)) {
                                    tab[i][j].setColor(Color.GRAY);
                                } else if (oldList.get(hpos).get(wpos)[1][1].getColor().equals(Color.DARKGREEN)) {
                                    tab[i][j].setColor(Color.DARKGREEN);
                                    tab[i][j].setExtinguishingFire(oldList.get(hpos).get(wpos)[1][1].getExtinguishingFire());
                                } else {
                                    System.out.println("-");
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                        pomW++;
                    }
                    pomW = -1;
                    pomH++;
                }
                pomH = -1;
                tab[1][1].setWidth(w);
                tab[1][1].setHeight(h);
                pixelWriter.setColor(w, h, tab[1][1].getColor());
                newList.add(tab);
            }
        }
        return new Fire(wImage, newList);
    }

    public static List<Cell[][]> fireFighting(List<Cell[][]> oldList, int power, int x_center, int y_center) {
        List<Point> pointList1;
        for (int k = -1; k <= 1; k++) {
            for (int j = 1; j <= power; j++) {
                pointList1 = Point.fireman(x_center + k, y_center, j);
                for (Point i : pointList1) {
                    try {
                        if ((i.getY() + 1) * i.getX() > 600 * (i.getY() + 1))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLUE))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLACK))
                            continue;

                        else if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.RED)) {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setExtinguishingFire(extinguishingFire);
                            continue;
                        } else {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setExtinguishingFire(extinguishingFire);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }

        for (int k = -1; k <= 1; k++) {
            if (k == 0)
                continue;
            for (int j = 1; j <= power; j++) {
                pointList1 = Point.fireman(x_center, y_center + k, j);
                for (Point i : pointList1) {
                    try {
                        if ((i.getY() + 1) * i.getX() > 600 * (i.getY() + 1))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLUE))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.BLACK))
                            continue;

                        if (oldList.get((600 * i.getY()) + i.getX())[1][1].getColor().equals(Color.RED)) {
                            oldList.get((600 * i.getY()) + i.getX())[1][1].setExtinguishingFire(extinguishingFire);
                            continue;
                        }
                        oldList.get((600 * i.getY()) + i.getX())[1][1].setExtinguishingFire(extinguishingFire);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        oldList.get((600 * y_center) + x_center)[1][1].setColor(Color.GRAY);
        return oldList;
    }

}