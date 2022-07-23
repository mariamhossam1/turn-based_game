package view;

import javax.swing.*;
import java.awt.*;

public class UnitWindow extends JFrame{

	private JPanel info;
	private JPanel inirel;
	
	public UnitWindow()
	{
		super();
		this.setBounds(300, 100, 750, 250);
		this.setLayout(new GridLayout(2,1));
		this.setVisible(true);
		info=new JPanel();
		info.setLayout(new GridLayout(1,4));
		inirel=new JPanel();
		this.add(info);
		this.add(inirel);
		this.revalidate();
		this.repaint();
	}
	public JPanel getInfo() {
		return info;
	}
	public void setInfo(JPanel info) {
		this.info = info;
	}
	public JPanel getInirel() {
		return inirel;
	}
	public void setInirel(JPanel inirel) {
		this.inirel = inirel;
	}
}
