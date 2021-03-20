package help;

import cell.Cell;
import javafx.scene.image.Image;

import java.util.List;

public class Fire {

    Image image;
    List<Cell[][]> list;

    public Fire(Image image, List<Cell[][]> list) {
        this.image = image;
        this.list = list;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Cell[][]> getList() {
        return list;
    }

    public void setList(List<Cell[][]> list) {
        this.list = list;
    }
}
