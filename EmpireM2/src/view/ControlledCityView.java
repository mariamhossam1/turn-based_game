package view;
import java.awt.*;

import javax.swing.*;

public class ControlledCityView extends JFrame{
private JPanel build;
private JPanel buildings;
private JPanel NTFG;
private JPanel parentArmy;
private JPanel currArmiesInCity;

 
 public ControlledCityView() {
	 	super();
		this.setSize(500, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1,8));
	 	NTFG=new JPanel();
		NTFG.setLayout(new GridLayout(5,1));
		this.add(NTFG);
		build=new JPanel();
		build.setLayout(new GridLayout(5,1));
		this.add(build);
		this.add(new JPanel());
		buildings=new JPanel();
		buildings.setLayout(new GridLayout(0,1));
		this.add(buildings);
		this.add(new JPanel());
		parentArmy=new JPanel();
		parentArmy.setLayout(new GridLayout(0,1));
		parentArmy.add(new JLabel("City's Defending Army"));
		this.add(parentArmy);
		this.add(new JPanel());
		currArmiesInCity=new JPanel();
		currArmiesInCity.setLayout(new GridLayout(0,1));
		currArmiesInCity.add(new JLabel("Current Armies in The City"));
		this.add(currArmiesInCity);
		this.revalidate();
		this.repaint();
 }

public JPanel getBuild() {
	return build;
}

public void setBuild(JPanel build) {
	this.build = build;
}

public JPanel getBuildings() {
	return buildings;
}

public void setBuildings(JPanel buildings) {
	this.buildings = buildings;
}

public JPanel getNTFG() {
	return NTFG;
}

public void setNTFG(JPanel nTFG) {
	NTFG = nTFG;
}

public JPanel getParentArmy() {
	return parentArmy;
}

public void setParentArmy(JPanel parentArmy) {
	this.parentArmy = parentArmy;
}

public JPanel getCurrArmiesInCity() {
	return currArmiesInCity;
}

public void setCurrArmiesInCity(JPanel currArmiesInCity) {
	this.currArmiesInCity = currArmiesInCity;
}
	
}
