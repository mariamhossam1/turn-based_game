package view;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame{
	
	private JPanel enterName;
	private JPanel chooseCity;
	private JPanel panel3;
	private JTextField playerName;
	
	
	public JPanel getEnterName() {
		return enterName;
	}
	public void setEnterName(JPanel name) {
		this.enterName = enterName;
	}
	public JPanel getChooseCity() {
		return chooseCity;
	}
	public void setChooseCity(JPanel chooseCity) {
		this.chooseCity = chooseCity;
	}
	public JPanel getPanel3() {
		return panel3;
	}
	public void setPanel3(JPanel panel3) {
		this.panel3 = panel3;
	}
	public JTextField getPlayerName() {
		return playerName;
	}
	public void setPlayerName(JTextField playerName) {
		this.playerName = playerName;
	}
	
	public GameView()
	{
		super();
		this.setSize(500, 800);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		enterName=new JPanel();
		enterName.setLayout(new FlowLayout());
		enterName.setPreferredSize(new Dimension(100,100));
		this.enterName.setPreferredSize(new Dimension(100,200));
		this.add(enterName,BorderLayout.NORTH);
		JLabel nameLabel=new JLabel("Enter your name:");
		nameLabel.setFont(new Font("High Tower Text",Font.PLAIN,20));
		nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getStyle() | Font.BOLD));
		nameLabel.setForeground(Color.BLACK);
		playerName=new JTextField();
		playerName.setText("");
		playerName.setPreferredSize(new Dimension(200,20));
		enterName.add(nameLabel);
		enterName.add(playerName);
		panel3=new JPanel();
		panel3.setPreferredSize(new Dimension(100,200));
		panel3.setLayout(new GridLayout(1,3));
		JLabel city1=new JLabel("                                                      CAIRO");
		JLabel city2=new JLabel("                                                      SPARTA");
		JLabel city3=new JLabel("                                                      ROME");
		panel3.add(city1);
		panel3.add(city2);
		panel3.add(city3);
		this.add(panel3,BorderLayout.SOUTH);
		chooseCity=new JPanel();
		chooseCity.setLayout(new GridLayout(0,3));
		chooseCity.setPreferredSize(new Dimension(100,200));
		this.add(chooseCity,BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	public static void main(String[] args)
	{
		new GameView();
	}
}
