import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Plot extends JPanel {
	
	public final static int VPOS=1;
	public final static int HPOS=2;
	
	public final static int X_AXIS_TO_TOP=1;
	public final static int X_AXIS_TO_BOTTOM=0;
	
	public final static int Y_AXIS_TO_LEFT=1;
	public final static int Y_AXIS_TO_RIGHT=0;
	
	public final static int VERTICAL_LEGEND=0;
	public final static int HORIZONTAL_LEGEND=1;
	
	public final static int LEGEND_TO_LEFT=0;
	public final static int LEGEND_TO_RIGHT=1;
	public final static int LEGEND_TO_BOTTOM=2;

	//MARGINS
	protected int leftMargin;
	protected int bottomMargin;
	protected int rightMargin;
	protected int topMargin;
	protected int[] margins = new int[]{5,5,5,5};
	protected int[] xLim;
	protected int[] yLim;
	
	//BOOLEANS
	protected boolean gridEnabled=true;
	protected boolean isLegendEnabled=true;
	protected boolean[] paintPlotBorder = new boolean[]{true,true,true,true};
	
	//COLORS
	protected final Color[] defaultColorPallete = new Color[]{new Color(238,64,53),
															  new Color(243,119,54),
															  new Color(253,244,152),
															  new Color(123,192,67),
															  new Color(3,146,207),
															  new Color(229,195,198),
															  new Color(225,233,183),
															  new Color(188,210,208),
															  new Color(208,183,131),
															  new Color(158,189,158),
															  new Color(221,133,92),
															  new Color(241,232,202),
															  new Color(116,81,81),
															  new Color(198,195,134),
															  new Color(195,238,231),
															  new Color(91,57,30),
															  new Color(135,198,195),
															  new Color(102,187,174),
															  new Color(253,207,88),
															  new Color(117,118,118),
															  new Color(242,125,12),
															  new Color(128,9,9),
															  new Color(255,85,136),
															  new Color(255,119,170),
															  };
	protected Color[] legendColors;
	protected Color plotBackground=Color.white;
	protected Color[] borderColors=new Color[]{Color.black,Color.black,Color.black,Color.black};
	protected Color titleColor = Color.black;
	protected Color subTitleColor = Color.black;
	protected Color gridColor = Color.gray;
	protected Color xLabelColor = Color.black;
	protected Color yLabelColor= Color.black;
	protected Color xTextColor = Color.black;
	protected Color yTextColor = Color.black;
	protected Color ticksColor = Color.black;
	protected Color legendTitleColor = Color.black;
	protected Color legendTextColor = Color.black;
	
	//ORIENTATION and LAYOUT
	protected int plotOrientation = VPOS;
	protected int yAxisSide = Y_AXIS_TO_LEFT;
	protected int xAxisSide = X_AXIS_TO_BOTTOM;
    protected int legendSide = LEGEND_TO_LEFT;
	protected int legendOrientation=VERTICAL_LEGEND;
	
	//COORDINATES
	protected int[][] blockCoordinates = new int[5][4];
	protected float[] props = new float[]{0.05f,0.05f,0.15f,0.08f};
	protected int[] legendPos;
	
	//TEXT
	protected String mainTitle;
	protected String mainSubtitle;
	protected String legendTitle;
	protected String yLabel;
	protected String xLabel;
	protected String[] legendLabels;
	
	//FONTS
	protected Font mainFont = new Font("Helvetica", Font.BOLD, 18);
	protected Font mainSubFont = new Font("Helvetica", Font.PLAIN,14);
	protected Font legendTitleFont = new Font("Helvetica", Font.BOLD, 12);
	protected Font legendTextFont = new Font("Helvetica", Font.PLAIN, 12);
	protected Font xTextFont = new Font("Helvetica", Font.PLAIN, 10);
	protected Font yTextFont = new Font("Helvetica", Font.PLAIN, 10);
	protected Font xLabelFont = new Font("Helvetica", Font.PLAIN, 12);
	protected Font yLabelFont = new Font("Helvetica", Font.PLAIN, 12);
	
	//EXTRA
	protected ArrayList<Integer> vLines = new ArrayList<Integer>();
	protected ArrayList<Integer> hLines = new ArrayList<Integer>();
	protected ArrayList<Integer> text = new ArrayList<Integer>();
 	
	//TEXT proportion
	protected double xPropTitle;
	protected double yPropTitle;
	protected double xPropSubtitle;
	protected double yPropSubtitle;
	protected double xPropXLabel;
	protected double yPropXLabel;
	protected double xProYLabel;
	protected double yProYLabel;
	protected double xPropXText;
	protected double yPropXText;
	protected double xPropYText;
	protected double yPropYText;
	protected double xPropLegendTitle;
	protected double yPropLengedTitle;
	protected double xPropLegendText;
	protected double yPropLegendText;
	
	protected boolean checkScale=true;
	
	private static final int PREF_W = 800;
	private static final int PREF_H = 650;
	
	public Plot(){
		setBackground(Color.white);
	}
	
	/**
	 * Set background color of the plot
	 * @param color
	 */
	public void setPlotBackaground(Color color){
		this.plotBackground=color;
	}
	
	/**
	 * Set if the grid has to be drawn 
	 * @param enableGrid
	 */
	public void enableGrid(boolean enableGrid){
		this.gridEnabled=enableGrid;
	}
	
	/**
	 * Set Y-axis label
	 * @param label
	 */
	public void setYLabel(String yLabel){
		this.yLabel=yLabel;
	}
	
	/**
	 * Set X-axis label
	 * @param label
	 */
	public void setXLabel(String xLabel){
		this.xLabel=xLabel;
	}
	
	/**
	 * Set if the borders of the plot have to be drawn
	 * @param flag
	 */
	public void paintPlotBorder(boolean[] flag){
		paintPlotBorder = flag;
	}
	
	/**
	 * Set colors for each border (left, bottom, right, top)
	 * @param borderColors
	 */
	public void setPlotBorderColor(Color[] borderColors){
		this.borderColors = borderColors;
	}
	
	/**
	 * Set the title of the plot
	 * @param title
	 */
	public void setPlotTitle(String title){
		this.mainTitle=title;
	}
	
	/**
	 * Set the font size of the title
	 * @param font
	 */
	public void setTitleSize(Font font){
		this.mainFont=font;
	}
	
	/**
	 * Set a subtitle in the plot
	 * @param subtitle
	 */
	public void setPlotSubtitle(String subtitle){
		this.mainSubtitle= subtitle;
	}
	
	/**
	 * Set font of the subtitle
	 * @param subtitleFont
	 */
	public void setSubtitleFont(Font subtitleFont){
		this.mainSubFont = subtitleFont;
	}
	
	/**
	 * Set the title of the legend
	 * @param title
	 */
	public void setLegendTitle(String title){
		this.legendTitle = title;
	}
	
	/**
	 * Set the side where the legend will be drawn 
	 * @param side
	 */
	public void setLegendSide(int side){
		legendSide = side;
	}
	
	/**
	 * Set font size of title of the legend
	 * @param size
	 */
	public void setLegendTitleSize(Font font){
		this.legendTitleFont = font;
	}
	
	/**
	 * Set the labels of the legend
	 * @param labels
	 */
	public void setLegendLabels(String[] labels){
		this.legendLabels = labels;
	}
	
	/**
	 * Set font size of legend text
	 * @param size
	 */
	public void setLegendTextSize(Font font){
		this.legendTextFont=font;
	}
	
	public void setLegendOrientation(int orientation){
		this.legendOrientation = orientation;
	}
	
	/**
	 * Set the colors of the legend
	 * @param colors
	 */
	public void setLegendColors(Color[] colors){
		this.legendColors = colors;
	}
	
	/**
	 * Set if the legend has to be showed
	 * @param enableLegend
	 */
	public void isLegendEnabled(boolean enableLegend){
		isLegendEnabled = enableLegend;
	}
	
	/**
	 * Set font size of X-axis text
	 * @param size
	 */
	public void setXTextFont(Font font){
		this.xTextFont = font;
	}
	
	/**
	 * Set font size of Y-axis text
	 * @param size
	 */
	public void setYTextFont(Font font){
		this.yTextFont= font;
	}
	
	/**
	 * Set font size of X-axis label
	 * @param size
	 */
	public void setXLabelFont(Font font){
		this.xLabelFont=font;
	}
	
	/**
	 * Set font size of Y-axis label
	 * @param size
	 */
	public void setYLabelFont(Font font){
		this.yLabelFont=font;
	}
	
	/**
	 * Set limits of X-axis
	 * @param xMin
	 * @param xMax
	 */
	public void setXLim(int xMin, int xMax){
		this.xLim[0]=xMin;
		this.yLim[1]=xMax;
	}
	
	/**
	 * Set limits of Y-axis
	 * @param yMin
	 * @param yMax
	 */
	public void setYLim(int yMin, int yMax){
		this.yLim[0]=yMin;
		this.yLim[1]=yMax;
	}
	
	/**
	 * Set coordinates of legend
	 * @param x
	 * @param y
	 */
	public void setLegendPos(int x, int y){
		legendPos[0]=x;
		legendPos[1]=y;
	}
	
	
	/**
	 * Add a vertical line to the plot
	 * @param pos
	 */
	public void setVLine(int pos){
		vLines.add(pos);
	}
	
	/**
	 * Add a horizontal line to the plot
	 * @param pos
	 */
	public void setHLine(int pos){
		hLines.add(pos);
	}
	
	/**
	 * Set if the plot must be plotted horizontally or vertically.
	 * @param orientation
	 */
	public void setOrientation(int orientation){
		this.plotOrientation=orientation;
	}
	
	/**
	 * Set the size of the plot where the Y-axis will be plotted. Accepted values are TEXT_TO_LEFT and TEXT_TO_RIGHT.
	 * @param side
	 */
	protected void setYTextSide(int side){
		this.yAxisSide = side;
	}
	
	/**
	 * Set the size of the plot where the Y-axis will be plotted. Accepted values are TEXT_TO_TOP and TEXT_TO_BOTTOM.
	 * @param side
	 */
	protected void setXTextSide(int side){
		this.xAxisSide = side;
	}
	
	
	/**
	 * Set color to the title
	 * @param color
	 */
	public void setTitleColor(Color color){
		this.titleColor=color;
	}
	
	/**
	 * Set color to the subtitle
	 * @param color
	 */
	public void setSubTitleColor(Color color){
		this.subTitleColor=color;
	}
	
	/**
	 * Set color to the grid
	 * @param color
	 */
	public void setGridColor(Color color){
		this.gridColor=color;
	}
	
	/**
	 * Set color to the X label
	 * @param color
	 */
	public void setXLabelColor(Color color){
		this.xLabelColor=color;
	}
	
	/**
	 * Set color to the Y label
	 * @param color
	 */
	public void setYLabelColor(Color color){
		this.yLabelColor=color;
	}
	
	/**
	 * Set color to the X text
	 * @param color
	 */
	public void setYTextColor(Color color){
		this.yTextColor=color;
	}
	
	/**
	 * Set color to the Y text
	 * @param color
	 */
	public void setXTextColor(Color color){
		this.xTextColor=color;
	}
	
	/**
	 * Set color to the title
	 * @param color
	 */
	public void setTicksColor(Color color){
		this.ticksColor=color;
	}
	
	/**
	 * Set the color of the title of the legend
	 * @param color
	 */
	public void setLegendTitleColor(Color color){
		this.legendTitleColor = color;
	}
	
	/**
	 * Set the color of the text of the legend
	 * @param color
	 */
	public void setLegendTextColor(Color color){
		
	}
	
	/**
	 * Add some label in the plot
	 * @param text
	 * @param coordinates
	 */
	public void setText(String text, int[] coordinates){
		
	}
	
	/**
	 * Upload block coordinates according the to size of the panel.
	 */
	protected void updateBlocksCoordinates(){
		
		blockCoordinates[0][0]=0;
		blockCoordinates[0][1]=0;
		blockCoordinates[0][2]=(int)(getWidth()*props[0]);
		blockCoordinates[0][3]=getHeight();
		
		blockCoordinates[1][0]=0;
		blockCoordinates[1][1]=(int)(getHeight()-(getHeight()*props[1]));
		blockCoordinates[1][2]=getWidth();
		blockCoordinates[1][3]=(int)(getHeight()*props[1]);
		
		blockCoordinates[2][0]= getWidth()- (int)(getWidth()*props[2]);
		blockCoordinates[2][1]= 0;
		blockCoordinates[2][2]=(int)(getWidth()*props[2]);
		blockCoordinates[2][3]=getHeight();
		
		blockCoordinates[3][0]=0;
		blockCoordinates[3][1]=0;
		blockCoordinates[3][2]=getWidth();
		blockCoordinates[3][3]=(int)(getHeight()*props[3]);
		
		blockCoordinates[4][0]=blockCoordinates[0][2];
		blockCoordinates[4][1]=(int)(getHeight()*props[3]);
		blockCoordinates[4][2]= getWidth()- blockCoordinates[0][2] - blockCoordinates[2][2];
		blockCoordinates[4][3]= getHeight() - blockCoordinates[1][3] - (int)(getHeight()*props[3]);
		
	}
	
	/**
	 * Draw wrapped text to certain width
	 * @param string
	 * @param width
	 * @param g2
	 * @param xPos
	 * @param yPos
	 */
	protected void drawWrappedString(String string, int width, Graphics2D g2, float xPos, float yPos){
		AttributedString attTitleLegend = new AttributedString(string);
		attTitleLegend.addAttribute(TextAttribute.FONT, g2.getFont());
		FontRenderContext frc = g2.getFontRenderContext();
		LineBreakMeasurer lineMeasurer = null;
		int paragraphStart = 0;
		int paragraphEnd=0;
  
		if (lineMeasurer == null) {
			AttributedCharacterIterator paragraph = attTitleLegend.getIterator();
			paragraphStart = paragraph.getBeginIndex();
			paragraphEnd = paragraph.getEndIndex();
			lineMeasurer = new LineBreakMeasurer(paragraph, frc);
		}
	   
		lineMeasurer.setPosition(paragraphStart);
		while (lineMeasurer.getPosition() < paragraphEnd) {
			TextLayout layout = lineMeasurer.nextLayout(width);
			yPos += layout.getAscent();
			layout.draw(g2, xPos, yPos);
			yPos += layout.getDescent() + layout.getLeading();
		}
	}
   
	/**
	 * Get the number of lines of a wrapped text 
	 * @param string
	 * @param width
	 * @param frc
	 * @param f
	 * @return
	 */
	protected int getNumberOfLines(String string, int width, FontRenderContext frc, Font f){
		AttributedString attTitleLegend = new AttributedString(string);
		LineBreakMeasurer lineMeasurer = null;
		attTitleLegend.addAttribute(TextAttribute.FONT, f);
		int paragraphStart = 0;
		int paragraphEnd=0;
		int cont=0;
		if (lineMeasurer == null) {
			AttributedCharacterIterator paragraph = attTitleLegend.getIterator();
			paragraphStart = paragraph.getBeginIndex();
			paragraphEnd = paragraph.getEndIndex();
			lineMeasurer = new LineBreakMeasurer(paragraph, frc);
		}
		lineMeasurer.setPosition(paragraphStart);
		while (lineMeasurer.getPosition() < paragraphEnd) {
			lineMeasurer.nextLayout(width);
			cont++;
		}
		return cont;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}
}