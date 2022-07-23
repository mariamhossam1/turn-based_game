package view;

import java.awt.*;

import javax.swing.*;

public class ArmyWindow extends JFrame {

	JPanel Units;
	JPanel CityTurns;
	
	public JPanel getUnits() {
		return Units;
	}

	public void setUnits(JPanel units) {
		Units = units;
	}

	public JPanel getCityTurns() {
		return CityTurns;
	}

	public void setCityTurns(JPanel cityTurns) {
		CityTurns = cityTurns;
	}
	
	public ArmyWindow() {
		super();
		this.setBounds(300, 100, 750, 450);
		this.setLayout(new GridLayout(2,1));
		this.setVisible(true);
		Units = new JPanel();
		//Units.setPreferredSize(new Dimension(228,306));
		this.getUnits().setLayout(new GridLayout(0,4));
		this.add(Units);
		CityTurns = new JPanel();
		CityTurns.setPreferredSize(new Dimension(228,306));
		this.getCityTurns().setLayout(new FlowLayout());
		this.add(CityTurns);
		this.revalidate();
		this.repaint();
	}
	public static void main(String[] args)
	{
		new ArmyWindow();
	}
}
