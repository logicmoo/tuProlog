package antext.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import antext.Option;
import antext.Platform;
import antext.res.ResourceLoader;

@SuppressWarnings("serial")
public class BuildOptionsPanel extends JPanel implements OptionButton.Listener, ActionListener, ComponentListener {
	
	public static final int CELL_SIZE = 120;
	public static final int BUTTON_SIZE = 100;
	public static final int BUTTON_PADDING = (CELL_SIZE - BUTTON_SIZE) / 2;
	public static final int COLUMN_SPACING = 60;
	
	public static final Border BORDER_BTN_RUNSELECTED = new LineBorder(Color.BLACK, 2);
	
	private ConfigureDialog owner;
	private JButton btnRunSelected;
	
	private JPanel pnlCenter;
	
	private int maxRow, maxColumn;
	
	private ArrayList<OptionButton> optionButtons;
	private ArrayList<Option> selectedOptions;
	
	
	public BuildOptionsPanel(ConfigureDialog owner) {
		
		setLayout(new BorderLayout());
		
		this.owner = owner;

		optionButtons = new ArrayList<>();
		selectedOptions = new ArrayList<>();		
		
		pnlCenter = new JPanel((LayoutManager)null);
		
		btnRunSelected = new JButton("Run selected");
		btnRunSelected.setVisible(false);
		btnRunSelected.addActionListener(this);
		btnRunSelected.setSize(120, 70);
		btnRunSelected.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnRunSelected.setHorizontalTextPosition(SwingConstants.CENTER);
		btnRunSelected.setIcon(ResourceLoader.getImage("run"));		
		pnlCenter.add(btnRunSelected, pnlCenter.getComponentCount());
		
		initOptionButtons(0, 0, owner.getConfigureTask().getBuildOptions().getPlatforms());
		
		pnlCenter.setBounds(
			0, 0,
			(maxColumn + 1) * CELL_SIZE + maxColumn * COLUMN_SPACING,
			(maxRow + 1) * CELL_SIZE
		);
		
		pnlCenter.setPreferredSize(
			new Dimension(
				(maxColumn + 1) * CELL_SIZE + maxColumn * COLUMN_SPACING,
				(maxRow + 1) * CELL_SIZE
			)
		);
		
		
		addComponentListener(this);
		add(new JScrollPane(pnlCenter), BorderLayout.CENTER);
		
	}
	
	public void runSelectedOptions() {
		
		Collections.sort(selectedOptions, new Option.NestingLevelComparator());
		
		for(Option opt : selectedOptions) {
			owner.getConfigureTask().insertOption(opt);
		}
		
		owner.getConfigureTask().startBuild();
	}
	
	private int initOptionButtons(int currentRow, int currentColumn, List<Platform> platforms) {
		for(Platform p : platforms) {
			currentRow = Math.max(
					initOptionButtons(currentRow, currentColumn, null, p.getRootOptions()), 
					currentRow + 1
			);
		}
		return currentRow;
	}
	
	private int initOptionButtons(int currentRow, int currentColumn, OptionButton parent, List<Option> options) {
		
		for(Option o : options) {
			OptionButton btn = new OptionButton(o, parent, owner, currentRow, currentColumn);
			optionButtons.add(btn);
			btn.addListener(this);
			pnlCenter.add(btn);

			
			if(parent != null) {
				pnlCenter.add(new Arrow(parent.getRow(), parent.getColumn(), currentRow, currentColumn));
			}
			
			
			maxRow = currentRow > maxRow ? currentRow : maxRow;
			maxColumn = currentColumn > maxColumn ? currentColumn : maxColumn;
			
			currentRow = Math.max(
				currentRow + 1,
				initOptionButtons(currentRow, currentColumn + 1, btn, o.getChildrenOptions())
			);
		}
		
		return currentRow;
	}

	@Override
	public void optionSelected(Option source) {
		if(!selectedOptions.contains(source)) {
			selectedOptions.add(source);
			btnRunSelected.setVisible(true);
		}	
	}

	@Override
	public void optionUnselected(Option source) {
		selectedOptions.remove(source);
		if(selectedOptions.size() == 0)
			btnRunSelected.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnRunSelected)
			runSelectedOptions();
	}
	
	



	@Override
	public void componentResized(ComponentEvent e) {
		if(e.getSource() == this) {
			btnRunSelected.setBounds(
					getWidth() - btnRunSelected.getWidth() - BUTTON_PADDING * 2,
					BUTTON_PADDING,
					btnRunSelected.getWidth(),
					btnRunSelected.getHeight()
			);
		}
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}
}
