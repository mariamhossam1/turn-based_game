package view;

import javax.swing.*;
import java.awt.*;

public class ChooseArmyWindow extends JFrame {

	public ChooseArmyWindow()
	{
		super();
		this.setBounds(300, 100, 750, 450);
		this.setLayout(new GridLayout(0,1));
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
}
