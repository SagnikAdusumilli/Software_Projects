package manager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

import object.PlayerControl;

public class MouseInput extends MouseAdapter{
	
	private JTextField field1;
	private JTextField field2;
	private JButton[][] buttons;
	
	
	public MouseInput(PlayerControl control1, PlayerControl control2, JButton[][] buttons){
		this.field1 = control1.getTextField();
		this.field2 = control2.getTextField();
		this.buttons = buttons;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e){
		for(int i = 0; i< 9; i++){
			for(int j = 0; j< 9; j++){
				if(buttons[i][j]==e.getSource()){
					field1.setText(i+","+j);
					field2.setText(i+","+j);
					return;
				}
			}
		}
	}

}
