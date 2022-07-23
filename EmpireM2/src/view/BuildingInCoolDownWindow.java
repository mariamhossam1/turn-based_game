package view;

import javax.swing.*;
import java.awt.*;

public class BuildingInCoolDownWindow extends JFrame{

	public BuildingInCoolDownWindow()
	{
		super();
		this.setBounds(300, 100, 600, 75);
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		JLabel message = new JLabel("BUILDING IN COOLDOWN");
		this.add(message);
		this.revalidate();
		this.repaint();
	}
}
