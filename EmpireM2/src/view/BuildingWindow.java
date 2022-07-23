package view;

import javax.swing.*;
import java.awt.*;

public class BuildingWindow extends JFrame{

	private JPanel info;
	private JPanel uprec;
	
	public BuildingWindow()
	{
		super();
		this.setBounds(300, 100, 750, 250);
		this.setLayout(new GridLayout(2,1));
		info=new JPanel();
		info.setLayout(new GridLayout(1,4));
		uprec=new JPanel();
		this.add(info);
		this.add(uprec);
	}

	public JPanel getInfo() {
		return info;
	}

	public void setInfo(JPanel info) {
		this.info = info;
	}

	public JPanel getUprec() {
		return uprec;
	}

	public void setUprec(JPanel uprec) {
		this.uprec = uprec;
	}
}
