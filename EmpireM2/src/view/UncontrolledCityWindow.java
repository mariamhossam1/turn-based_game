package view;

import java.awt.*;
import javax.swing.*;

public class UncontrolledCityWindow extends JFrame{

	
	public UncontrolledCityWindow()
	{
		super();
		this.setBounds(300, 100, 600, 75);
		this.setLayout(new FlowLayout());
		this.setVisible(true);
		JLabel message = new JLabel("THIS CITY IS NOT UNDER YOUR CONTROL");
		this.add(message);
		
	}
}
