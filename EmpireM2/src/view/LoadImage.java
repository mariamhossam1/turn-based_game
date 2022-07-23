package view;

import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadImage extends JPanel {

	private Image image;

	  public LoadImage() {
	    super(new GridLayout());
	    try {
	      image = ImageIO.read(new URL("https://ppe.151amapa.pw/img/witcher-3-quest-map.jpg"));
	    } catch (Exception ex) {
	      ex.printStackTrace(System.err);
	    }
	    int w = image.getWidth(null) / 2;
	    int h = image.getHeight(null) / 2;
	    this.add(new JLabel(new ImageIcon(image.getScaledInstance(w, h,
	        Image.SCALE_SMOOTH))));
	  }
}
