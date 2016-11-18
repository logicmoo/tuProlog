package antext.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.tools.ant.taskdefs.CallTarget;

import antext.ConfigureTask;
import antext.Option;

@SuppressWarnings("serial")
public class OptionButton extends JPanel implements MouseListener {
	
	private static Stroke SELECTED_STROKE = new BasicStroke(10);
	
	private ButtonImpl button;
	
	private OptionButton parentButton;
	private ConfigureDialog owner;
	private Option option;
	
	private int row, column;
	
	private ArrayList<Listener> listeners;

	public OptionButton(Option option, OptionButton parentButton, ConfigureDialog owner, int row, int column) {
		super(null);
		
		this.owner = owner;
		this.option = option;
		this.parentButton = parentButton;
		
		this.row = row;
		this.column = column;
		
		listeners = new ArrayList<>();
		
		button = new ButtonImpl();
		button.addMouseListener(this);	
		add(button);
		
		setBounds(
				column * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING), 
				row * BuildOptionsPanel.CELL_SIZE, 
				BuildOptionsPanel.CELL_SIZE, 
				BuildOptionsPanel.CELL_SIZE
		);
		
		setOpaque(false);
		
	}
	
	public boolean addListener(Listener l) {
		return listeners.add(l);
	}
	
	public boolean removeListener(Listener l) {
		return listeners.remove(l);
	}
	
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Option getOption() {
		return option;
	}
	
	public OptionButton getParentButton() {
		return parentButton;
	}
	
	public boolean isSelected() {
		return button.isSelected();
	}
	
	public void setSelected(boolean selected) {
		button.setSelected(selected);
		if(selected)
			fireOptionSelected();
		else
			fireOptionUnselected();
	}
	
	public void toggleSelected() {
		setSelected(!isSelected());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			owner.getConfigureTask().insertOption(option);	
			owner.getConfigureTask().startBuild();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			if(e.isShiftDown()) {
				setSelected(true);
				
				OptionButton next = parentButton;
				
				while(next != null) {
					next.setSelected(true);
					next = next.getParentButton();
				}					
				
			} else {
				toggleSelected();
			}
		}
	}
	
	protected void fireOptionSelected() {
		for(Listener l : listeners)
			l.optionSelected(option);
	}
	
	protected void fireOptionUnselected() {
		for(Listener l : listeners)
			l.optionUnselected(option);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}	
	
	private class ButtonImpl extends JButton {
		
		public ButtonImpl() {
			super(
				"<html><body style=\"text-align: center;\">" +
				option.getDisplay() + 
				"</body></html>"
			);
			
			setBorderPainted(false);
			setFocusPainted(false);
			
			setBackground(option.getPlatform().getColor());
			
			setBounds(
					BuildOptionsPanel.BUTTON_PADDING,
					BuildOptionsPanel.BUTTON_PADDING,
					BuildOptionsPanel.BUTTON_SIZE,
					BuildOptionsPanel.BUTTON_SIZE
			);
			
			setVerticalTextPosition(AbstractButton.BOTTOM);
			setHorizontalTextPosition(AbstractButton.CENTER);
			
			if(option.getIcon() != null) {
				setIcon(option.getIcon());
			}			
			
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			
			if(isSelected()) {
				g2.setColor(Color.black);
				g2.setStroke(SELECTED_STROKE);
				g2.drawRect(0, 0, getWidth(), getHeight());
			}
		}
		
		@Override
		public void setSelected(boolean selected) {
			super.setSelected(selected);
			invalidate();
			repaint();
		}
	}
	
	public static interface Listener {
		public void optionSelected(Option source);
		public void optionUnselected(Option source);
	}
	
	

}