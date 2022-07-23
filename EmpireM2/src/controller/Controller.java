package controller;

import exceptions.*;
import buildings.*;
import java.util.*;
import java.io.IOException;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import view.*;
import engine.*;
import units.*;

public class Controller implements ActionListener{
	GameView gameView;
	WorldMapView worldMapView = new WorldMapView();
	ControlledCityView controlledCityView= new ControlledCityView();
	UncontrolledCityWindow uncontrolledCityWindow;
	BattleView battleView;
	Game game;
	JButton start;
	String playerName;
	String chosenCity;
	ArmyWindow armyWindow;
	City currentCity;
	Building currentBuilding;
	BuildingWindow buildingWindow;
	UnitWindow unitWindow;
	Unit currentUnit;
	Unit currentOppUnit;
	Army currentArmy;
	Army currentOppArmy;
	ChooseCityWindow chooseCityWindow;
	ChooseArmyWindow chooseArmyWindow;
	Unit attackingUnit;
	Unit defUnit;
	City underSeigeForThreeTurns;
	JLabel battleLog=new JLabel();
	
	public Controller()
	{
		gameView=new GameView();
		start=new JButton("Start Game");
		start.setVisible(false);
		start.addActionListener(this);
		start.setFont(new Font("High Tower Text",Font.PLAIN,18));
		start.setFont(start.getFont().deriveFont(start.getFont().getStyle() | Font.BOLD));
		gameView.getEnterName().add(start);
		Icon icon1 = new ImageIcon("images/cairo2.png");
		JButton test1=new JButton(icon1);
		test1.addActionListener(this);
		test1.setActionCommand("Cairo");
		Icon icon2 = new ImageIcon("images/sparta2.png");
		JButton test2=new JButton(icon2);
		test2.addActionListener(this);
		test2.setActionCommand("Sparta");
		test2.setPreferredSize(new Dimension(133,100));
		Icon icon3 = new ImageIcon("images/rome2.png");
		JButton test3=new JButton(icon3);
		test3.addActionListener(this);
		test3.setActionCommand("Rome");
		test3.setPreferredSize(new Dimension(133,100));
		gameView.getChooseCity().add(test1,new FlowLayout());
		gameView.getChooseCity().add(test2,BorderLayout.CENTER);
		gameView.getChooseCity().add(test3,BorderLayout.EAST);
		
		battleLog.setLayout(new GridLayout(0,1));
		
		gameView.revalidate();
		gameView.repaint();
		
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JButton b=(JButton)e.getSource();
		if((b.getActionCommand().equals("Sparta") || b.getActionCommand().equals("Cairo") || b.getActionCommand().equals("Rome")) && gameView.getPlayerName().getText().equals(""))
		{
			chosenCity=b.getActionCommand();
			JOptionPane.showMessageDialog(new JFrame(),"Please enter your name");
		}
		if((b.getActionCommand().equals("Sparta") || b.getActionCommand().equals("Cairo") || b.getActionCommand().equals("Rome")) && !gameView.getPlayerName().getText().equals(""))
			{ 	start.setVisible(true);
				chosenCity=b.getActionCommand();
				playerName=gameView.getPlayerName().getText();
			}
		else if (b.getActionCommand().equals("Start Game"))
		{
			try {
			game=new Game(playerName,chosenCity);
			}catch(IOException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			gameView.setVisible(false);
			 createWorldMapView();
			
		}
		else if(b.getActionCommand().equals("World Map View"))
		{
			controlledCityView.setVisible(false);
			createWorldMapView();
		}
		else if(b.getActionCommand().substring(1).equals("outsideArmy") )
		{
			createOutsideArmyWindow(""+b.getActionCommand().charAt(0));	
		}
		else if(b.getActionCommand().equals("Visit Cairo")||b.getActionCommand().equals("Visit Rome")||b.getActionCommand().equals("Visit Sparta"))
		{
			String city=b.getActionCommand().substring(6);
			for(City c: game.getPlayer().getControlledCities())
				if(c.getName().equalsIgnoreCase(city))
				{
					worldMapView.setVisible(false);
					currentCity=c;
					createControlledCityView(c);
					return;
				}
			for(City c: game.getAvailableCities())
				if(c.getName().equals(city))
					new UncontrolledCityWindow();					
		}
		else if(b.getActionCommand().equals("Continue Siege"))
		{
			armyWindow.setVisible(false);
		}
		else if(b.getActionCommand().equals("Attack"))
		{
			armyWindow.setVisible(false);
			for(City x:game.getAvailableCities())
			{
				if(currentArmy.getCurrentLocation().equalsIgnoreCase(x.getName()))
					{
						x.setTurnsUnderSiege(-1);
						x.setUnderSiege(false);
						currentOppArmy=x.getDefendingArmy();
					}
			}
			createBattleView();
		}
		else if(b.getActionCommand().equals("Auto Resolve"))
		{ 
			try{
			for(City x:game.getAvailableCities())
			{
				if(currentArmy.getCurrentLocation().equalsIgnoreCase(x.getName()))
					{
						currentOppArmy=x.getDefendingArmy();
						x.setTurnsUnderSiege(-1);
						x.setUnderSiege(false);
					}
			}
			
				game.autoResolve(currentArmy,currentOppArmy);
				armyWindow.setVisible(false);
				if(currentArmy.getUnits().size()==0)
				{
					JOptionPane.showMessageDialog(new JFrame(),"Your army lost the battle");
					game.getPlayer().getControlledArmies().remove(currentArmy);
				}
				else if(currentOppArmy.getUnits().size()==0)
					{
						JOptionPane.showMessageDialog(new JFrame(),"Your army won the battle");
						game.occupy(currentArmy,currentOppArmy.getCurrentLocation());
					}
				worldMapView.setVisible(false);
				createWorldMapView();
			}catch(FriendlyFireException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			
			
		}
		else if (b.getActionCommand().length()>7 && b.getActionCommand().substring(0,7).equals("choosea"))
		{
			Army tmp=game.getPlayer().getControlledArmies().get(Integer.parseInt(""+b.getActionCommand().charAt(8)));
			try{
				tmp.relocateUnit(currentUnit);
				chooseArmyWindow.setVisible(false);
				unitWindow.setVisible(false);
				controlledCityView.setVisible(false);
				createControlledCityView(currentCity);
			}
			catch(MaxCapacityException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
		}
		else if(b.getActionCommand().length()>17&&b.getActionCommand().substring(0,13).equals("chooseDefArmy"))
		{
			for(City x:game.getPlayer().getControlledCities())
			{
				if(x.getName().equals(b.getActionCommand().substring(13)))
					try{
						x.getDefendingArmy().relocateUnit(currentUnit);
						chooseArmyWindow.setVisible(false);
						unitWindow.setVisible(false);
						controlledCityView.setVisible(false);
						createControlledCityView(currentCity);
					}
						catch(MaxCapacityException e3)
						{
							JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
						}
			}
		}
		else if(b.getActionCommand().equals("Lay Siege"))
		{
			for(City c:game.getAvailableCities())
				if(c.getName().equalsIgnoreCase(currentArmy.getCurrentLocation()))
					{
					try{
						
						game.getPlayer().laySiege(currentArmy, c);
						armyWindow.setVisible(false);
						worldMapView.setVisible(false);
						createWorldMapView();
					}
					catch(TargetNotReachedException e3)
					{
						JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
					}
					catch(FriendlyCityException e3)
					{
						JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
					}
					}
		}
		else if(b.getActionCommand().length()>5 && b.getActionCommand().substring(0,5).equals("Build")) {
			try {
			game.getPlayer().build(b.getActionCommand().substring(6), currentCity.getName());
			controlledCityView.setVisible(false);
			createControlledCityView(currentCity);
			}
			catch(NotEnoughGoldException e3) {
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
		}
		else if(b.getActionCommand().length()>17 && b.getActionCommand().substring(9, 17).equals("building"))
		{
			createBuildingWindow(b.getActionCommand().substring(0,8),""+b.getActionCommand().charAt(17));
		}
		else if(b.getActionCommand().equals("Upgrade"))
		{
			try{
				game.getPlayer().upgradeBuilding(currentBuilding);
				buildingWindow.setVisible(false);
				controlledCityView.setVisible(false);
				createControlledCityView(currentCity);
			}
			catch(NotEnoughGoldException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			catch(BuildingInCoolDownException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			catch(MaxLevelException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
		}
		else if (b.getActionCommand().equals("Recruit"))
		{
			 try
			 {
				if(currentBuilding instanceof ArcheryRange)
					game.getPlayer().recruitUnit("archer",currentCity.getName());
				else if (currentBuilding instanceof Barracks)
					game.getPlayer().recruitUnit("infantry",currentCity.getName());
				else
					game.getPlayer().recruitUnit("cavalry",currentCity.getName());
				buildingWindow.setVisible(false);
				controlledCityView.setVisible(false);
				createControlledCityView(currentCity);
				
			}catch(NotEnoughGoldException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			catch(BuildingInCoolDownException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			 catch(MaxRecruitedException e3)
			 {
				 JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			 }
		}
		else if(b.getActionCommand().substring(0,4).equals("Unit"))
		{
			createInsideUnitWindow(""+b.getActionCommand().charAt(4));
		}
		else if(b.getActionCommand().equals("Initiate Army"))
		{
			game.getPlayer().initiateArmy(currentCity, currentUnit);
			unitWindow.setVisible(false);
			controlledCityView.setVisible(false);
			createControlledCityView(currentCity);
		}
		else if(b.getActionCommand().equals("Relocate Unit")) 
		{
			createChooseArmyWindow();
		}
		else if(b.getActionCommand().length()>4 &&b.getActionCommand().substring(0,4).equals("Army"))
		{
			createInsideArmyWindow(""+b.getActionCommand().charAt(4));
		}
		else if (b.getActionCommand().equals("choose city"))
		{
			createChooseCityWindow();
		}
		else if(b.getActionCommand().length()>7 && b.getActionCommand().substring(0,7).equals("choosec"))
		{
			game.targetCity(currentArmy, b.getActionCommand().substring(8));
			chooseCityWindow.setVisible(false);
			armyWindow.setVisible(false);
		}
		else if(b.getActionCommand().length()>16 && b.getActionCommand().substring(0,16).equals("AttackUnitBattle"))
		{
			createOutsideUnitWindow(""+b.getActionCommand().charAt(16));
		}
		else if(b.getActionCommand().length()>13 && b.getActionCommand().substring(0,13).equals("DefUnitBattle"))
		{
			createDefUnitWindow(""+b.getActionCommand().charAt(13));
		}
		else if(b.getActionCommand().equals("Choose as attacker"))
		{
			attackingUnit=currentUnit;
			unitWindow.setVisible(false);
		}
		else if(b.getActionCommand().equals("Attack this unit"))
		{
			defUnit=currentOppUnit;
			unitWindow.setVisible(false);
		}
		else if(b.getActionCommand().equals("Start Attack") && (attackingUnit==null || defUnit==null))
		{
			JOptionPane.showMessageDialog(new JFrame(),"Please choose an attacking unit and a unit to attack");
		}
		else if(b.getActionCommand().equals("Start Attack"))
		{
			try {
				int d1 =defUnit.getCurrentSoldierCount();
				attackingUnit.attack(defUnit);
				int lost=d1-defUnit.getCurrentSoldierCount();
				battleLog.add(new JLabel("Attacked Unit from city lost "+lost+" soldiers"));
				attackingUnit=null;
				defUnit=null;
				Unit unit1 = currentArmy.getUnits().get((int) (Math.random() * currentArmy.getUnits().size()));
				Unit unit2 = currentOppArmy.getUnits().get((int) (Math.random() * currentOppArmy.getUnits().size()));
				int a1=unit1.getCurrentSoldierCount();
				unit2.attack(unit1);
				int lost2=a1-unit1.getCurrentSoldierCount();
				battleLog.add(new JLabel("Attacked Unit from your units lost "+lost2+" soldiers"));
				battleView.setVisible(false);
				createBattleView();
			}
			catch(FriendlyFireException e3)
			{
				JOptionPane.showMessageDialog(new JFrame(),e3.getMessage());
			}
			
		}
		else if(b.getActionCommand().equals("End Turn"))
		{
			tryToEndTurn();
		}
	}
	public void createWorldMapView()
	{
		worldMapView = new WorldMapView();
		worldMapView.setVisible(true);
		Icon icon1 = new ImageIcon("images/cairo2.png");
		JButton visitcairo=new JButton(icon1);
		visitcairo.addActionListener(this);
		visitcairo.setActionCommand("Visit Cairo");
		visitcairo.setPreferredSize(new Dimension(50,50));
		Icon icon2 = new ImageIcon("images/sparta2.png");
		JButton visitsparta=new JButton(icon2);
		visitsparta.addActionListener(this);
		visitsparta.setActionCommand("Visit Sparta");
		visitsparta.setPreferredSize(new Dimension(50,50));
		Icon icon3 = new ImageIcon("images/rome2.png");
		JButton visitrome=new JButton(icon3);
		visitrome.addActionListener(this);
		visitrome.setActionCommand("Visit Rome");
		visitrome.setPreferredSize(new Dimension(50,50));
		worldMapView.getAvailableCities().add(visitcairo);
		worldMapView.getAvailableCities().add(new JLabel("       CAIRO"));
		worldMapView.getAvailableCities().add(visitsparta);
		worldMapView.getAvailableCities().add(new JLabel("       SPARTA"));
		worldMapView.getAvailableCities().add(visitrome);
		worldMapView.getAvailableCities().add(new JLabel("        ROME"));
		JLabel name=new JLabel("Name: "+playerName);
		worldMapView.getNTFG().add(name);
		JLabel turn=new JLabel("Turn Count: "+ game.getCurrentTurnCount());
		worldMapView.getNTFG().add(turn);
		JLabel food=new JLabel("Food: "+ game.getPlayer().getFood());
		worldMapView.getNTFG().add(food);
		JLabel gold=new JLabel("Gold : "+ game.getPlayer().getTreasury());
		worldMapView.getNTFG().add(gold);
		JButton endturn=new JButton("End Turn");
		endturn.addActionListener(this);
		worldMapView.getNTFG().add(endturn);
		int c=0;
		for(Army a:game.getPlayer().getControlledArmies())
		{
			if(a.getCurrentStatus()==Status.IDLE )
			{
				Boolean out=true;
				for(City x:game.getPlayer().getControlledCities())
					if (a.getCurrentLocation().equals(x.getName()))
						out=false;
				if (out)
				{
						JButton idle=new JButton("Army"+c);
						idle.addActionListener(this);
						idle.setActionCommand(c+"outsideArmy");
						worldMapView.getArmies().add(idle);
				}
			}
			if(a.getCurrentStatus()==Status.BESIEGING)
			{
				JButton bes=new JButton("Army"+c);
				bes.addActionListener(this);
				bes.setActionCommand(c+"outsideArmy");
				worldMapView.getArmies().add(bes);
			}
			if(a.getCurrentStatus()==Status.MARCHING)
			{
				JButton marching=new JButton("Army"+c);
				marching.addActionListener(this);
				marching.setActionCommand(c+"outsideArmy");
				worldMapView.getArmies().add(marching);
		    }
			c++;
		}
		worldMapView.revalidate();
		worldMapView.repaint();
	}
	public void createArmyWindow(String index)
	{
		currentArmy=game.getPlayer().getControlledArmies().get(Integer.parseInt(index));
	    armyWindow=new ArmyWindow();
		for(Unit u:currentArmy.getUnits())
		{
			if(u instanceof Archer)
				armyWindow.getUnits().add(new JLabel("Type: Archer"));
			else if(u instanceof Cavalry)
				armyWindow.getUnits().add(new JLabel("Type: Cavalry"));
			else {
				armyWindow.getUnits().add(new JLabel("Type: Infantry"));
			}
			armyWindow.getUnits().add(new JLabel("Level: "+u.getLevel()));
			armyWindow.getUnits().add(new JLabel("Current Soldier Count: "+u.getCurrentSoldierCount()));
			armyWindow.getUnits().add(new JLabel("Max Soldier Count: "+u.getMaxSoldierCount()));
		}
		if(currentArmy.getCurrentStatus()!=Status.IDLE)
		{
			if(currentArmy.getCurrentStatus()==Status.BESIEGING)
			{
				armyWindow.getCityTurns().add(new JLabel("CityUnderSeige: "+currentArmy.getCurrentLocation()));
				for(City c:game.getAvailableCities())
					if(c.getName().equalsIgnoreCase(currentArmy.getCurrentLocation()))
						armyWindow.getCityTurns().add(new JLabel("Turns under siege: "+c.getTurnsUnderSiege()));
			}
			else
			{
				armyWindow.getCityTurns().add(new JLabel("Target City: "+currentArmy.getTarget()));
				armyWindow.getCityTurns().add(new JLabel ("Turns left till target: "+currentArmy.getDistancetoTarget()));
			}
		}
		armyWindow.revalidate();
		armyWindow.repaint();
	}
	public void createInsideArmyWindow(String index)
	{
		createArmyWindow(index);
		JButton setTarget=new JButton("setTarget");
		setTarget.addActionListener(this);
		setTarget.setActionCommand("choose city");
		armyWindow.getCityTurns().add(setTarget);
		armyWindow.revalidate();
		armyWindow.repaint();
		
	}
	public void createControlledCityView(City c) {
		
		currentCity=c;
		controlledCityView = new ControlledCityView();
		controlledCityView.setVisible(true);
		Boolean e=true;
		for(EconomicBuilding x:c.getEconomicalBuildings()) {
			if(x instanceof Farm)
				e=false;
		}
		if(e) {
		JButton buildFarm=new JButton("Build Farm");
		buildFarm.addActionListener(this);
		controlledCityView.getBuild().add(buildFarm);
		}
		e=true;
		for(EconomicBuilding x:c.getEconomicalBuildings()) {
			if(x instanceof Market)
				e=false;
		}
		if(e) {
			JButton buildMarket=new JButton("Build Market");
			buildMarket.addActionListener(this);
			controlledCityView.getBuild().add(buildMarket);
		}
		e=true;
		for(MilitaryBuilding x:c.getMilitaryBuildings()) {
			if(x instanceof ArcheryRange)
				e=false;
		}
		if(e) {
			JButton buildArchery=new JButton("Build ArcheryRange");
			buildArchery.addActionListener(this);
			controlledCityView.getBuild().add(buildArchery);
		}
		e=true;
		for(MilitaryBuilding x:c.getMilitaryBuildings()) {
			if(x instanceof Stable)
				e=false;
		}
		if(e) {
			JButton buildStable=new JButton("Build Stable");
			buildStable.addActionListener(this);
			controlledCityView.getBuild().add(buildStable);
		}
		e=true;
		for(MilitaryBuilding x:c.getMilitaryBuildings()) {
			if(x instanceof Barracks)
				e=false;
		}
		if(e) {
			JButton buildBarracks=new JButton("Build Barracks");
			buildBarracks.addActionListener(this);
			controlledCityView.getBuild().add(buildBarracks);
		}
		int m=0;
		int n=0;
		for(MilitaryBuilding b:c.getMilitaryBuildings() ) {
			JButton militaryBuilding=new JButton("Military building"+m);
			militaryBuilding.addActionListener(this);
			controlledCityView.getBuildings().add(militaryBuilding);
			m++;
		}
		for(EconomicBuilding b:c.getEconomicalBuildings() ) {
			JButton economicBuilding=new JButton("Economic building"+n);
			economicBuilding.addActionListener(this);
			controlledCityView.getBuildings().add(economicBuilding);
			n++;
		}
		JLabel name=new JLabel("Name: "+playerName);
		controlledCityView.getNTFG().add(name);
		JLabel turn=new JLabel("Turn Count: "+ game.getCurrentTurnCount());
		controlledCityView.getNTFG().add(turn);
		JLabel food=new JLabel("Food: "+ game.getPlayer().getFood());
		controlledCityView.getNTFG().add(food);
		JLabel gold=new JLabel("Gold : "+ game.getPlayer().getTreasury());
		controlledCityView.getNTFG().add(gold);
		JPanel buttons=new JPanel();
		buttons.setLayout(new FlowLayout());
		controlledCityView.getNTFG().add(buttons);
		JButton returnToWorldMapView=new JButton("World Map View");
		returnToWorldMapView.addActionListener(this);
		buttons.add(returnToWorldMapView);
		JButton endturn=new JButton("End Turn");
		endturn.addActionListener(this);
		buttons.add(endturn);
		
		 int f=0;
		for(Unit u:c.getDefendingArmy().getUnits()) {
			JButton unit=new JButton("Unit"+f);
			unit.addActionListener(this);
			controlledCityView.getParentArmy().add(unit);
			f++;
		}
		
		int i=0;
		for(Army a:game.getPlayer().getControlledArmies())
		{
			if(a.getCurrentLocation().equals(currentCity.getName()))
			{
				JButton army=new JButton("Army"+i);
				army.addActionListener(this);
				controlledCityView.getCurrArmiesInCity().add(army);
			}
			i++;
		}
		controlledCityView.revalidate();
		controlledCityView.repaint();
	}
	public void createBuildingWindow(String type,String index)
	{
		 buildingWindow=new BuildingWindow();
		 buildingWindow.setVisible(true);
		if(type.equalsIgnoreCase("Military"))
		{
		 MilitaryBuilding temp=currentCity.getMilitaryBuildings().get(Integer.parseInt(index));
		 currentBuilding=temp;
		 if(temp instanceof ArcheryRange) 
		 {
			 JLabel typeLabel = new JLabel("Type: ArcheryRange");
			 JLabel levelLabel = new JLabel("Level: "+temp.getLevel());
			 buildingWindow.getInfo().add(typeLabel);
			 buildingWindow.getInfo().add(levelLabel);
		 }
		 else if(temp instanceof Stable) 
		 {
			 JLabel typeLabel = new JLabel("Type: Stable");
			 JLabel levelLabel = new JLabel("Level: "+temp.getLevel());
			 buildingWindow.getInfo().add(typeLabel);
			 buildingWindow.getInfo().add(levelLabel);
		 }
		 else  if(temp instanceof Barracks) 
		 {
			 JLabel typeLabel = new JLabel("Type: Barracks");
			 JLabel levelLabel = new JLabel("Level: "+temp.getLevel());
			 buildingWindow.getInfo().add(typeLabel);
			 buildingWindow.getInfo().add(levelLabel);
		 }
		 JLabel recost=new JLabel("Recruitment cost: "+temp.getRecruitmentCost());
		 buildingWindow.getInfo().add(recost);	
		 JLabel upcost=new JLabel("Upgrade cost: "+temp.getUpgradeCost());
		 buildingWindow.getInfo().add(upcost);	
		 JButton upgrade=new JButton("Upgrade");
		 upgrade.addActionListener(this);
		 buildingWindow.getUprec().add(upgrade);	
		 JButton recruit=new JButton("Recruit");
		 recruit.addActionListener(this);
		 buildingWindow.getUprec().add(recruit);
		}
		if(type.equalsIgnoreCase("Economic"))
		{
		 EconomicBuilding temp=currentCity.getEconomicalBuildings().get(Integer.parseInt(index));
		 currentBuilding=temp;
		 if(temp instanceof Farm) 
		 {
			 JLabel typeLabel = new JLabel("Type: Farm");
			 JLabel levelLabel = new JLabel("Level: "+temp.getLevel());
			 buildingWindow.getInfo().add(typeLabel);
			 buildingWindow.getInfo().add(levelLabel);
		 }
		 else if(temp instanceof Market) 
		 {
			 JLabel typeLabel = new JLabel("Type: Market");
			 JLabel levelLabel = new JLabel("Level: "+temp.getLevel());
			 buildingWindow.getInfo().add(typeLabel);
			 buildingWindow.getInfo().add(levelLabel);
		 }
		 JLabel upcost=new JLabel("Upgrade cost: "+temp.getUpgradeCost());
		 buildingWindow.getInfo().add(upcost);	
		 JButton upgrade=new JButton("Upgrade");
		 upgrade.addActionListener(this);
		 buildingWindow.getUprec().add(upgrade);	
		}
		buildingWindow.revalidate();
		buildingWindow.repaint();
         	
	}
	
	
	public void createInsideUnitWindow(String index)
	{
		unitWindow=new UnitWindow();
		Unit u = currentCity.getDefendingArmy().getUnits().get(Integer.parseInt(index));
		currentUnit=u;
			if(u instanceof Archer)
				unitWindow.getInfo().add(new JLabel("Type: Archer"));
			else if(u instanceof Cavalry)
				unitWindow.getInfo().add(new JLabel("Type: Cavalry"));
			else {
				unitWindow.getInfo().add(new JLabel("Type: Infantry"));
			}
			unitWindow.getInfo().add(new JLabel("Level: "+u.getLevel()));
			unitWindow.getInfo().add(new JLabel("Current Soldier Count: "+u.getCurrentSoldierCount()));
			unitWindow.getInfo().add(new JLabel("Max Soldier Count: "+u.getMaxSoldierCount()));
		
		JButton initiate = new JButton("Initiate Army");
		initiate.addActionListener(this);
		unitWindow.getInirel().add(initiate);
		JButton relocate = new JButton("Relocate Unit");
		relocate.addActionListener(this);
		unitWindow.getInirel().add(relocate);
		unitWindow.revalidate();
		unitWindow.repaint();
	}
	public void createOutsideUnitWindow(String index)
	{
		unitWindow=new UnitWindow();
		Unit u = currentArmy.getUnits().get(Integer.parseInt(index));
		currentUnit=u;
			if(u instanceof Archer)
				unitWindow.getInfo().add(new JLabel("Type: Archer"));
			else if(u instanceof Cavalry)
				unitWindow.getInfo().add(new JLabel("Type: Cavalry"));
			else {
				unitWindow.getInfo().add(new JLabel("Type: Infantry"));
			}
		unitWindow.getInfo().add(new JLabel("Level: "+u.getLevel()));
		unitWindow.getInfo().add(new JLabel("Current Soldier Count: "+u.getCurrentSoldierCount()));
		unitWindow.getInfo().add(new JLabel("Max Soldier Count: "+u.getMaxSoldierCount()));
		JButton attackingunit=new JButton("Choose as attacker");
		attackingunit.addActionListener(this);
		unitWindow.getInirel().add(attackingunit);
		unitWindow.revalidate();
		unitWindow.repaint();
	}
	public void createDefUnitWindow(String index)
	{
		unitWindow=new UnitWindow();
		currentOppUnit=currentOppArmy.getUnits().get(Integer.parseInt(index));
		if(currentOppUnit instanceof Archer)
			unitWindow.getInfo().add(new JLabel("Type: Archer"));
		else if(currentOppUnit instanceof Cavalry)
			unitWindow.getInfo().add(new JLabel("Type: Cavalry"));
		else {
			unitWindow.getInfo().add(new JLabel("Type: Infantry"));
		}
		unitWindow.getInfo().add(new JLabel("Level: "+currentOppUnit.getLevel()));
		unitWindow.getInfo().add(new JLabel("Current Soldier Count: "+currentOppUnit.getCurrentSoldierCount()));
		unitWindow.getInfo().add(new JLabel("Max Soldier Count: "+currentOppUnit.getMaxSoldierCount()));
		JButton attack=new JButton("Attack this unit");
		attack.addActionListener(this);
		unitWindow.getInirel().add(attack);
		unitWindow.revalidate();
		unitWindow.repaint();
	}
	public void createOutsideArmyWindow(String index)
	{
		createArmyWindow(index);
		currentArmy=game.getPlayer().getControlledArmies().get(Integer.parseInt(index));
		System.out.println(currentArmy.getCurrentStatus());
		if(currentArmy.getCurrentStatus()==Status.IDLE)
		{
			JButton laySiege=new JButton("Lay Siege");
			laySiege.addActionListener(this);
			armyWindow.getCityTurns().add(laySiege);
			JButton attack=new JButton("Attack");
			attack.addActionListener(this);
			armyWindow.getCityTurns().add(attack);
			JButton autoResolve=new JButton("Auto Resolve");
			autoResolve.addActionListener(this);
			armyWindow.getCityTurns().add(autoResolve);
		}
		else if(currentArmy.getCurrentStatus()==Status.BESIEGING)
		{
			City tmp=null;
			String cname=currentArmy.getCurrentLocation();
			for(City c:game.getAvailableCities())
				if(c.getName().equals(cname))
					tmp=c;
			if(tmp.getTurnsUnderSiege()<3)
			{
				JButton continueSiege=new JButton("Continue Siege");
				continueSiege.addActionListener(this);
				armyWindow.getCityTurns().add(continueSiege);
			}
			JButton attack=new JButton("Attack");
			attack.addActionListener(this);
			armyWindow.getCityTurns().add(attack);
			JButton autoResolve=new JButton("Auto Resolve");
			autoResolve.addActionListener(this);
			armyWindow.getCityTurns().add(autoResolve);
		}
		armyWindow.revalidate();
		armyWindow.repaint();
	}
	public void createChooseCityWindow()
	{
		chooseCityWindow=new ChooseCityWindow();
		for(City x:game.getAvailableCities())
		{
			if(!game.getPlayer().getControlledCities().contains(x))
			{
				JButton city=new JButton(x.getName());
				city.addActionListener(this);
				city.setActionCommand("choosec "+x.getName());
				chooseCityWindow.add(city);		
			}
		}
		chooseCityWindow.revalidate();
		chooseCityWindow.repaint();
	}
	public void createChooseArmyWindow()
	{
		if(game.getPlayer().getControlledArmies().size()==0 && game.getPlayer().getControlledCities().size()==1)
			JOptionPane.showMessageDialog(new JFrame(), "There are no other armies");
		else {
			
			chooseArmyWindow=new ChooseArmyWindow();
			int c=0;
			for(City x:game.getPlayer().getControlledCities())
			{
				if(!x.getName().equals(currentCity.getName()))
				{
					JButton defArmy=new JButton("DefendingArmy"+x.getName());
					defArmy.addActionListener(this);
					defArmy.setActionCommand("chooseDefArmy"+x.getName());
					chooseArmyWindow.add(defArmy);
				}
			}
		
			for(Army a:game.getPlayer().getControlledArmies())
			{
				JButton army=new JButton("Army"+c);
				army.addActionListener(this);
				army.setActionCommand("choosea "+c);
				chooseArmyWindow.add(army);		
				c++;
			}
			chooseArmyWindow.revalidate();
			chooseArmyWindow.repaint();
		}
	}
	public void createBattleView()
	{
		if(currentArmy.getUnits().size()==0)
		{
			game.getPlayer().getControlledArmies().remove(currentArmy);
			JOptionPane.showMessageDialog(new JFrame(),"You lost the battle"); 
			worldMapView.setVisible(false);
			createWorldMapView();
		}
		else if(currentOppArmy.getUnits().size()==0)
		{
			game.occupy(currentArmy,currentOppArmy.getCurrentLocation());
			JOptionPane.showMessageDialog(new JFrame(),"You won the battle"); 
			worldMapView.setVisible(false);
			createWorldMapView();
		}else
		{
			battleView=new BattleView();
			JLabel name=new JLabel("Name: "+playerName);
			battleView.getNTFG().add(name);
			JLabel turn=new JLabel("Turn Count: "+ game.getCurrentTurnCount());
			battleView.getNTFG().add(turn);
			JLabel food=new JLabel("Food: "+ game.getPlayer().getFood());
			battleView.getNTFG().add(food);
			JLabel gold=new JLabel("Gold : "+ game.getPlayer().getTreasury());
			battleView.getNTFG().add(gold);
			JButton start=new JButton("Start Attack");
			start.addActionListener(this);
			battleView.getNTFG().add(start);
		
			int c=0;
			for(Unit u: currentArmy.getUnits())
			{
				JButton unit=new JButton("Unit"+c);
				unit.setActionCommand("AttackUnitBattle"+c);
				unit.addActionListener(this);
				battleView.getAttackingArmy().add(unit);
				c++;
			}
			int i=0;
			for(Unit u:currentOppArmy.getUnits())
			{
				
				JButton unit=new JButton("Unit"+i);
				unit.addActionListener(this);
				unit.setActionCommand("DefUnitBattle"+i);
				battleView.getDefendingArmy().add(unit);
				i++;
			}
			battleView.add(battleLog);
			battleView.revalidate();
			battleView.repaint();
		}
			
	}
	public void tryToEndTurn()
	{
		underSeigeForThreeTurns=null;
		for(City c:game.getAvailableCities()) 
		{
			if(c.getTurnsUnderSiege()==3)
				underSeigeForThreeTurns=c;
			}
		if(underSeigeForThreeTurns!=null)
		{
			JOptionPane.showMessageDialog(new JFrame(), underSeigeForThreeTurns.getName()+" has been under siege for more than three turns, you have to attack");
		}
		else
		{
			game.endTurn();
			if(game.isGameOver())
			{
				if(game.getAvailableCities().size()==game.getPlayer().getControlledCities().size())
				{
					worldMapView.setVisible(false);;
					controlledCityView.setVisible(false);
					JOptionPane.showMessageDialog(new JFrame(),"You won");
				}
				else if(game.getCurrentTurnCount()>game.getMaxTurnCount())
				{
					worldMapView.setVisible(false);;
					controlledCityView.setVisible(false);
					JOptionPane.showMessageDialog(new JFrame(),"You lost");
				}
			}
			else
			{
				worldMapView.setVisible(false);;
				controlledCityView.setVisible(false);
				createWorldMapView();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new Controller();
	}

}
