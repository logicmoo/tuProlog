package antext.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;

public class Arrow extends JComponent {
	
	private static GeneralPath TRIANGLE_PATH;
	private static Stroke LINE_STROKE;
	private static Color LINE_COLOR;
	
	private static final int PADDING = 200;
	
	static {
		
		LINE_COLOR = Color.GRAY;
		
		LINE_STROKE = new BasicStroke(2);
		
		TRIANGLE_PATH = new GeneralPath();
		TRIANGLE_PATH.moveTo(-5, 0);
		TRIANGLE_PATH.lineTo(5, -5);
		TRIANGLE_PATH.lineTo(5, 5);
		TRIANGLE_PATH.closePath();
	}
	
	protected int startCol, startRow, endCol, endRow;
	protected Point[] points;
	
	public Arrow(int startRow, int startCol, int endRow, int endCol) {
		this.startCol = startCol;
		this.startRow = startRow;
		this.endCol = endCol;
		this.endRow = endRow;
		
		if(startRow == endRow) {
			points = new Point[2];
			points[0] = new Point(
					startCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING) + BuildOptionsPanel.CELL_SIZE,
					(int)((startRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);
			points[1] = new Point(
				endCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING),
				(int)((endRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);
			
		} else {
			points = new Point[4];
			points[0] = new Point(
					startCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING) + BuildOptionsPanel.CELL_SIZE,
					(int)((startRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);
			points[1] = new Point(
					startCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING) + BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING / 2,
					(int)((startRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);		
			
			points[2] = new Point(
					startCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING) + BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING / 2,
					(int)((endRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);				
			
			points[3] = new Point(
				endCol * (BuildOptionsPanel.CELL_SIZE + BuildOptionsPanel.COLUMN_SPACING),
				(int)((endRow + 0.5) * BuildOptionsPanel.CELL_SIZE)
			);			
			
			
		}
		
		setBounds(0, 0, 2000, 2000);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setStroke(LINE_STROKE);
		g2.setColor(LINE_COLOR);
		
		for(int i = 0; i < points.length - 1; i++)
			g2.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
		
		g2.translate(points[0].x, points[0].y);
		g2.fill(TRIANGLE_PATH);
		
		
	}
}
