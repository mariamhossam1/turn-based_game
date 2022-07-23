package view;

import java.awt.*;

import javax.swing.*;
public class WorldMapView extends JFrame{

	JPanel NTFG;
	JPanel availableCities;
	JPanel armies;
	
	public WorldMapView()
	{
		super();
		this.setSize(500, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1,3));
		NTFG=new JPanel();
		NTFG.setLayout(new GridLayout(5,1));
		this.add(NTFG);
		availableCities = new JPanel();
		availableCities.setLayout(new GridLayout(3,2));
		availableCities.setMaximumSize(new Dimension(800,100));
		this.add(availableCities);
		armies=new JPanel();
		armies.setLayout(new GridLayout(0,1));
		JLabel labelA=new JLabel ("Armies:");
		armies.add(labelA);
		this.add(armies);
		this.revalidate();
		this.repaint();
	}
	
	public JPanel getNTFG() {
		return NTFG;
	}

	public void setNTFG(JPanel nTFG) {
		NTFG = nTFG;
	}

	public JPanel getAvailableCities() {
		return availableCities;
	}

	public void setAvailableCities(JPanel availableCities) {
		this.availableCities = availableCities;
	}

	public JPanel getArmies() {
		return armies;
	}

	public void setArmies(JPanel armies) {
		this.armies = armies;
	}


	public static void main(String[] args)
	{
		new WorldMapView();
	}
}
