package help;

import java.util.ArrayList;
import java.util.List;

public class Point {

    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //do strazaka
    public static List<Point> fireman(final int centerX, final int centerY, final int radius) {  //zmiana na swoj algorytm
        int x = 0;
        int y = radius;
        List<Point> list = new ArrayList<>();
        do {
            list.add(new Point(centerX + x, centerY + y));
            list.add(new Point(centerX + x, centerY - y));
            list.add(new Point(centerX - x, centerY + y));
            list.add(new Point(centerX - x, centerY - y));
            list.add(new Point(centerX + y, centerY + x));
            list.add(new Point(centerX + y, centerY - x));
            list.add(new Point(centerX - y, centerY + x));
            list.add(new Point(centerX - y, centerY - x));

            x++;
        } while (x <= y);
        return list;
    }
}
