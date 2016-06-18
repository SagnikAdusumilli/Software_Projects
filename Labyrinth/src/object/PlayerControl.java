package object;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ai.AI;

public class PlayerControl extends JPanel{

	private Player player, other;
	private JLabel[][] playerCell;
	private JTextField textField;

	public PlayerControl(Player player, Player other, Solver solver, JLabel[][] playerCell, int x, int y, int width, int height,Color color, AI ai){
		this.player = player;
		this.other = other;
		this.playerCell = playerCell;

		setBounds(x, y, width, height);
		setLayout(new GridLayout(0,1));

		JPanel target = new JPanel();
		target.setBackground(color);
		JPanel controls = new JPanel();
		controls.setBackground(color);
		controls.setLayout(new GridLayout(0,1));

		JLabel label1 = new JLabel("Target:");
		
		
		target.add(label1);
		for(JButton eachButton : player.getButtons()){
			target.add(eachButton);
		}
		
		JLabel indication = new JLabel("Position (\"row, column\"):");
		textField = new JTextField();
		JButton go = new JButton("Go");
		JButton check = new JButton("Calculate");

		controls.add(indication);
		controls.add(textField);
		controls.add(go);
		controls.add(check);

		add(target);
		add(controls);

		go.addActionListener(e->{
			try{
				solver.solve();
				String position = textField.getText().trim();
				String[] positions = position.split(",");
				if(solver.getGoodPosition().contains(position)){
					player.updatePosition(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]));
					updatePlayerImage();
				}else{
					textField.setText("Unable to Persue!");
				}
			}catch(Exception e1){
				e1.printStackTrace();
				textField.setText("Invalid Input!");
			}
		});

		check.addActionListener(e->{
			ai.calculate();
		});
	}


	private void updatePlayerImage(){
		for(int i = 0;i<27;i++){
			for(int j = 0;j<27;j++){
				if(i==player.getX() &&j==player.getY())
					playerCell[i][j].setIcon(player.getPlayerIcon());
				else if(i==other.getX() &&j==other.getY())
					playerCell[i][j].setIcon(other.getPlayerIcon());
				else
					playerCell[i][j].setIcon(null);
			}
		}		
	}
	
	public JTextField getTextField(){
		return this.textField;
	}


}
