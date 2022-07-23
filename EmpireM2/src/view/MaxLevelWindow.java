package view;

import javax.swing.*;
import java.awt.*;

public class MaxLevelWindow extends JFrame {

	public MaxLevelWindow()
	{
		super();
		this.setBounds(300, 100, 600, 75);
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		JLabel message = new JLabel("YOU DON'T HAVE ENOUGH GOLD");
		this.add(message);
		this.revalidate();
		this.repaint();
	}
}
