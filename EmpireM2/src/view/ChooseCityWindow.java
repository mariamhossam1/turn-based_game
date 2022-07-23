package view;

import java.awt.*;
import javax.swing.*;

public class ChooseCityWindow extends JFrame{

	public ChooseCityWindow()
	{
		super();
		this.setBounds(300, 100, 750, 450);
		this.setLayout(new GridLayout(0,1));
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
}
