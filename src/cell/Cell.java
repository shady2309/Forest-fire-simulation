package cell;
//komórka

import javafx.scene.paint.Color;

public class Cell {
    Color color;
    int width;               //szerokosc
    int height;              //wysokosc
    double brightness;       //jasnosc
    int remainingTime;       //czas do destrukcji
    int extinguishingFire;   //gaszenie ognia

    public Cell() {
        this.color = null;
        this.extinguishingFire = -1;
    }

    //gettery, settery
    public int getExtinguishingFire() {
        return extinguishingFire;
    }

    public void setExtinguishingFire(int extinguishingFire) {
        this.extinguishingFire = extinguishingFire;
    }

    public void decTime(double a) {
        this.remainingTime -= a;
    }

    public void decPutOutFire(double a) {
        this.extinguishingFire -= a;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
