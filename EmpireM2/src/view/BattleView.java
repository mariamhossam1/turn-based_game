package view;

import javax.swing.*;
import java.awt.*;

public class BattleView extends JFrame{

	private JPanel NTFG;
	private JPanel attackingArmy;
	private JPanel defendingArmy;
	private JPanel log;
	
	public BattleView()
	{
		super();
		this.setSize(500, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1,4));
		this.setVisible(true);
	 	NTFG=new JPanel();
		NTFG.setLayout(new GridLayout(5,1));
		this.add(NTFG);
		attackingArmy=new JPanel();
		attackingArmy.setLayout(new GridLayout(0,1));
		this.add(attackingArmy);
		defendingArmy=new JPanel();
		defendingArmy.setLayout(new GridLayout(0,1));
		this.add(defendingArmy);
		log=new JPanel();
		log.setLayout(new GridLayout(0,1));
		this.add(log);
		this.revalidate();
		this.repaint();
	}

	public JPanel getNTFG() {
		return NTFG;
	}

	public void setNTFG(JPanel nTFG) {
		NTFG = nTFG;
	}

	public JPanel getAttackingArmy() {
		return attackingArmy;
	}

	public void setAttackingArmy(JPanel attackingArmy) {
		this.attackingArmy = attackingArmy;
	}

	public JPanel getDefendingArmy() {
		return defendingArmy;
	}

	public void setDefendingArmy(JPanel defendingArmy) {
		this.defendingArmy = defendingArmy;
	}

	public JPanel getLog() {
		return log;
	}

	public void setLog(JPanel log) {
		this.log = log;
	}

}
