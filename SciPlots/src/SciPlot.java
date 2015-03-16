import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class SciPlot extends Plot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	   
	private String[] yValues;
	private String[] xValues;
	private String[] fillValues;
	private String[] xFacet;
	private String[] yFacet;
	private int numXFacetLev;
	private int numYFacetLev;
	private String[] xFacetLevels;
	private String[] yFacetLevels;
	private int numFill;
	private String[] fillLevels;
	private int numberPlots;
	private int nCol;
	private int nRow;
	private HashMap<String[],String[][]> splittedFacetData = new HashMap<String[],String[][]>();
	
	public SciPlot(String[] yValues, String[] xValues, String[] fillValues, String[] facetX, String[] facetY, int nCol, int nRow) {
	  
	  // checkear los datos y lanzar alguna excepción si no cumplen con los requisitos
	  // xValues,xValues,fillValues, facetX y facetY deben tener le mismo largo.
	  // nCol * nRow debe ser igual o mas grande que el numero de plots.
		
	  this.xValues=xValues;
      this.yValues=yValues;
      this.fillValues=fillValues;
      xFacet=facetX;
      yFacet=facetY;
      xFacetLevels = getLevels(xFacet);
      yFacetLevels = getLevels(yFacet);
      fillLevels = getLevels(fillValues);
      numXFacetLev = getNumberOfLevels(xFacet);
      
      numYFacetLev = getNumberOfLevels(yFacet);
      this.nCol = nCol;
      this.nRow = nRow;
      
      if(numXFacetLev!=0 && numYFacetLev!=0){
    	  numberPlots=numXFacetLev*numYFacetLev;
      }else{
    	  if(numXFacetLev == 0){
    		  numberPlots = numYFacetLev;
    	  }else{
    		  if(numYFacetLev==0){
    			  numberPlots = numXFacetLev;
    		  }else{
    			  if(numYFacetLev==0 && numXFacetLev==0){
    				  numberPlots=1;
    			  }
    		  }
    	  }
      }
      
      numFill = getNumberOfLevels(this.fillValues);
      splittedFacetData = splitFacetData();

      /*Iterator<Entry<String[],String[][]>> it = splittedFacetData.entrySet().iterator();

      while (it.hasNext()) {
    	  Map.Entry<String[],String[][]> e = (Map.Entry<String[],String[][]>)it.next();
    	  String[] tmpFacets = (String[])e.getKey(); 
    	  String[][] tmpValues = (String[][])e.getValue();
    	  for(int i=0;i<tmpValues.length;i++){
    		  if(tmpFacets !=null){
    			  System.out.println(tmpFacets[0]+" "+tmpFacets[1]+" "+tmpValues[i][0]+" "+tmpValues[i][1]);
    		  }else{
    			  System.out.println(tmpValues[i][0]+" "+tmpValues[i][1]);
    		  }
    	  }
    	  System.out.println();
      }*/
	}

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      updateBlocksCoordinates();
      drawPlot(g2);
      drawLegend(g2);
      drawTitle(g2);
      checkScale = false;
   }
   
   private int getNumberOfLevels(String[] array){
	   HashMap<String,Integer> levels = new HashMap<String,Integer>();
	   
	   if(array == null){
		   return 0;
	   }
	   
	   for(int i=0;i<array.length;i++){
		   levels.put(array[i], levels.get(array[i]));
	   }
	   return levels.size();
   }
   
   private String[] getLevels(String[] array){
	   HashMap<String,Integer> levels = new HashMap<String,Integer>();
	   
	   if(array == null){
		   return null;
	   }
	   
	   for(int i=0;i<array.length;i++){
		   levels.put(array[i], levels.get(array[i]));
	   }
	   
	   return levels.keySet().toArray(new String[levels.keySet().size()]);
   }
   
   private HashMap<String,String[][]> splitFillData(){
	   return null;
   }
   
   private void drawTitle(Graphics2D g2){
	   
	   	  double xScale,yScale,wProp,hProp;
	   	  int titleWidth, titleHeight, subtitleHeight,subtitleWidth;
	   	  AffineTransform tx1;
	      g2.setFont(mainFont);
	      g2.setColor(titleColor);
	      int[] titleCoords = blockCoordinates[3];
	      titleWidth = g2.getFontMetrics().stringWidth(mainTitle);
	      titleHeight = (int)g2.getFont().getLineMetrics(mainTitle, g2.getFontRenderContext()).getHeight();
	      
	      xScale=1;
	      yScale=1;
	      
	      if(checkScale){
	    	  xPropTitle = ((double)titleCoords[2]/(double)titleWidth); 
	    	  yPropTitle = ((double)(titleCoords[3]/2)/(double)titleHeight);
	      }
	      
	      wProp = ((double)titleCoords[2]/(double)titleWidth);
	      hProp = ((double)(titleCoords[3]/2)/(double)titleHeight);

	      
	      if(wProp != xPropTitle){
			  xScale = wProp/xPropTitle;
		  }

		  if(hProp != yPropTitle){
			  yScale = hProp/yPropTitle;
		  }
	      
	      tx1 = new AffineTransform();
	      tx1.scale(xScale,yScale);
	      g2.setFont(g2.getFont().deriveFont(tx1));
	      
	      titleWidth = g2.getFontMetrics().stringWidth(mainTitle);
	      titleHeight = (int)g2.getFont().getLineMetrics(mainTitle, g2.getFontRenderContext()).getHeight();
	      
	      g2.drawString(mainTitle, titleCoords[0]+(titleCoords[2]/2)-titleWidth/2, titleCoords[1]+(titleCoords[3]*1/4)+(titleHeight/2));
	      
	      
	      //Draw subtitle
	      g2.setFont(this.mainSubFont);
	      g2.setColor(subTitleColor);
	      subtitleWidth = g2.getFontMetrics().stringWidth(mainSubtitle);
	      subtitleHeight = (int)g2.getFont().getLineMetrics(mainSubtitle, g2.getFontRenderContext()).getHeight();
	      
	      xScale=1;
	      yScale=1;
	      
	      if(checkScale){
	    	  xPropSubtitle = ((double)titleCoords[2]/(double)subtitleWidth);
	    	  yPropSubtitle = ((double)(titleCoords[3]/2)/(double)subtitleHeight);
	      }
	      
	      wProp = ((double)titleCoords[2]/(double)subtitleWidth);
	      hProp = ((double)(titleCoords[3]/2)/(double)subtitleHeight);
	      
	      if(wProp != xPropSubtitle){
			  xScale = wProp/xPropSubtitle;
		  }

		  if(hProp != yPropSubtitle){
			  yScale = hProp/yPropSubtitle;
		  }
	     
	      tx1 = new AffineTransform();
	      tx1.scale(xScale,yScale);
	      g2.setFont(g2.getFont().deriveFont(tx1));
	      
	      subtitleWidth = g2.getFontMetrics().stringWidth(mainSubtitle);
	      subtitleHeight = (int)g2.getFont().getLineMetrics(mainSubtitle, g2.getFontRenderContext()).getHeight();
	      
	      g2.drawString(mainSubtitle, titleCoords[0]+(titleCoords[2]/2)-(subtitleWidth/2), titleCoords[1]+(titleCoords[3]*3/4)+(subtitleHeight/2));
	   
   }
   
   private void drawLegend(Graphics2D g2){
	   
	   int legendTitleHeight,legendTitleWidth, legendTextHeight=0,legendHeight=0, legendWidth=0;
	   double xScale, yScale, wProp, hProp;
	   AffineTransform tx1;
	   FontRenderContext frc = g2.getFontRenderContext();
	   
	   if(isLegendEnabled){
    	  
    	  int[] legendCord=null;
    	  
    	  if(legendSide == LEGEND_TO_RIGHT){
    		  legendCord =  blockCoordinates[2];
    	  }else{
    		  if(legendSide == LEGEND_TO_LEFT){
    			  legendCord =  blockCoordinates[0];
    		  }else{
    			  if(legendSide == LEGEND_TO_BOTTOM){
    				  legendCord =  blockCoordinates[1];
    			  }
    		  }
    	  }
	      
	      g2.setFont(legendTitleFont);
	      legendTitleHeight = (int)g2.getFont().getLineMetrics(legendTitle, frc).getHeight();
	      legendTitleWidth = g2.getFontMetrics().stringWidth(legendTitle);
	      
	      xScale=1;
	      yScale=1;
	      
	      if(checkScale){
	    	  xPropLegendTitle = ((double)legendCord[2]/(double)legendTitleWidth); 
	    	  yPropLengedTitle = ((double)legendCord[3]/2)/((double)legendTitleHeight);
	      }
	      
	      wProp = ((double)legendCord[2]/(double)legendTitleWidth);
	      hProp = ((double)(legendCord[3]/2)/(double)legendTitleHeight);
	    
	      if(wProp != xPropLegendTitle){
			  xScale = wProp/xPropLegendTitle;
		  }

		  if(hProp != yPropLengedTitle){
			  yScale = hProp/yPropLengedTitle;
		  }
		  
		  tx1 = new AffineTransform();
		  tx1.scale(xScale, yScale);
		  
		  g2.setFont(legendTitleFont.deriveFont(tx1));
		  
		  legendTitleHeight = (int)g2.getFont().getLineMetrics(legendTitle, g2.getFontRenderContext()).getHeight();
	      legendTitleWidth = g2.getFontMetrics().stringWidth(legendTitle);
	      
	      if(legendTitleWidth > legendCord[2]){
	      	int nLines = getNumberOfLines(legendTitle,legendCord[2], frc, g2.getFont());
	      	legendTitleHeight = legendTitleHeight*nLines;
	      	legendTitleWidth = legendCord[2];
	      }
	     
	      g2.setFont(legendTextFont.deriveFont(tx1));
	      g2.setColor(legendTextColor);
		  
	      if(legendOrientation==Plot.VERTICAL_LEGEND){
		      int numberOfLines=0;
		      legendTextHeight = (int)g2.getFont().getLineMetrics(fillLevels[0], g2.getFontRenderContext()).getHeight();
		      legendWidth = legendCord[2];
		      int nLinesText[] = new int[fillLevels.length];
		      for(int i=0;i<fillLevels.length;i++){
		    	  int tmp = g2.getFontMetrics().stringWidth(fillLevels[i]);
		    	  if(tmp > (legendWidth - legendTextHeight - (legendTextHeight/2))){
		    		  int nLines = getNumberOfLines(fillLevels[i],(legendWidth - legendTextHeight - (legendTextHeight/2)),frc,g2.getFont());
		    		  numberOfLines= numberOfLines+nLines;
		    		  nLinesText[i] = nLines;
		    	  }else{
		    		  numberOfLines++;
		    		  nLinesText[i]=0;
		    	  }
		      }
		      
		      legendHeight = legendTextHeight*numberOfLines + ((legendTextHeight/2)*(fillLevels.length-1)) + (legendTextHeight/2)+legendTitleHeight;
		      
		      int yPos = legendCord[1]+(legendCord[3]/2)-(legendHeight/2)+(int)(legendTextHeight*1.5) + legendTitleHeight;
		      
		      for(int i=1;i<=fillLevels.length;i++){
		    	  int xPos = legendCord[0]+(int)(legendCord[2]/2)-(int)(legendWidth/2)+(int)(legendTextHeight*1.5);
		    	  int tmp = g2.getFontMetrics().stringWidth(fillLevels[i-1]);
		    	  g2.drawRect(legendCord[0]+(int)(legendCord[2]/2)-legendWidth/2, yPos-legendTextHeight, legendTextHeight, legendTextHeight);
		    	  if(tmp > (legendWidth - legendTextHeight - (legendTextHeight/2))){
		    		  drawWrappedString(fillLevels[i-1], (legendWidth - legendTextHeight - (legendTextHeight/2)), g2,xPos,yPos-legendTextHeight);
		    		  yPos = yPos + (legendTextHeight*nLinesText[i-1]) + (legendTextHeight/2);
		    	  }else{
		    		  g2.drawString(fillLevels[i-1], xPos ,yPos);
		    		  yPos = yPos + (int)(legendTextHeight*1.5); 
		    	  }
		      }
	      }else{
	    	  if(legendOrientation == Plot.HORIZONTAL_LEGEND){
	    		  for(int i=0;i<fillLevels.length;i++){
	    			  int legendTextWidth = g2.getFontMetrics().stringWidth(fillLevels[i]);
	    			  legendWidth=legendWidth + legendTextWidth;
	    		  }
	    		  
	    		  int tmp = (int)g2.getFont().getLineMetrics(fillLevels[0], g2.getFontRenderContext()).getHeight();
	    		  legendHeight = legendTextHeight + 2*legendTitleHeight;
	    		  legendWidth = legendWidth + tmp*fillLevels.length + tmp*(fillLevels.length-1) + tmp/2*fillLevels.length;
	    		  
	    		  int xPos = legendCord[0]+(legendCord[2]/2)-(legendWidth/2);
	    		  int stringPos = xPos + tmp + tmp/2;
    			  g2.drawRect(xPos, legendCord[1]+legendCord[3]/2-(legendHeight/2) + legendHeight - (tmp/2), tmp, tmp);
 		    	  g2.drawString(fillLevels[0], stringPos,legendCord[1]+legendCord[3]/2-(legendHeight/2) + legendHeight + (tmp/2));
	    		  
	    		  for(int i=1;i<=fillLevels.length-1;i++){
	    			  int wordWidth = g2.getFontMetrics().stringWidth(fillLevels[i-1]);
	    			  stringPos = stringPos + wordWidth + 2*tmp + tmp/2;
	    			  g2.drawRect(stringPos - tmp - tmp/2, legendCord[1]+legendCord[3]/2-(legendHeight/2) + legendHeight - (tmp/2), tmp, tmp);
	 		    	  g2.drawString(fillLevels[i], stringPos ,legendCord[1]+legendCord[3]/2-(legendHeight/2) + legendHeight + (tmp/2));
	 		      }
	    		  
	    		  g2.setFont(legendTitleFont.deriveFont(tx1));
	    		  g2.setColor(legendTitleColor);
	    	  }
	      }
	      g2.setFont(legendTitleFont.deriveFont(tx1));
	      g2.setColor(legendTitleColor);
	      float x = legendCord[0]+(legendCord[2]/2)- legendTitleWidth/2;
	      float y = legendCord[1]+legendCord[3]/2-(legendHeight/2);
	      drawWrappedString(legendTitle, legendTitleWidth, g2,x,y);
	   }  
   }
   
   private void drawPlot(Graphics2D g2){
      
	  int xLabelWidth,xLabelHeight,yLabelWidth,yLabelHeight;
	  double  wProp,hProp;
	  double xScale=1;
	  double yScale=1;
	  AffineTransform tx1;
	  GlyphVector gv;
	  FontRenderContext frc;
	   
	  //Draw plot
      int[] plotCoords = blockCoordinates[4];
      
      //Draw x-axis label
      //############################
      g2.setFont(this.xLabelFont); 
      g2.setColor(this.xLabelColor);
      xLabelWidth = g2.getFontMetrics().stringWidth(xLabel);
      xLabelHeight = (int)g2.getFont().getLineMetrics(xLabel, g2.getFontRenderContext()).getHeight();
      
      if(checkScale){
    	  xPropXLabel = ((double)plotCoords[2]/(double)xLabelWidth);
    	  yPropXLabel = ((double)(plotCoords[3]/2)/(double)xLabelHeight);
      }
      
      xScale=1;
      yScale=1;
      wProp = ((double)plotCoords[2]/(double)xLabelWidth);
      hProp = ((double)(plotCoords[3]/2)/(double)xLabelHeight);
      
	  if(wProp != xPropXLabel){
		  xScale = wProp/xPropXLabel;
	  }

	  if(hProp != yPropXLabel){
		  yScale = hProp/yPropXLabel;
	  }
	  
      tx1 = new AffineTransform();
      tx1.scale(xScale,yScale);
      g2.setFont(g2.getFont().deriveFont(tx1));
      
      xLabelWidth = g2.getFontMetrics().stringWidth(xLabel);
      xLabelHeight = (int)g2.getFont().getLineMetrics(xLabel, g2.getFontRenderContext()).getHeight();
      if(this.xAxisSide==Plot.X_AXIS_TO_BOTTOM){
    	  g2.drawString(xLabel, plotCoords[0]+(plotCoords[2]/2)-xLabelWidth/2, plotCoords[1]+ plotCoords[3]-(xLabelHeight/2));
      }else{
    	  g2.drawString(xLabel, plotCoords[0]+(plotCoords[2]/2)-xLabelWidth/2, plotCoords[1]+ xLabelHeight);
      }
      
      //Draw y-axis label
      //#########################
      g2.setFont(yLabelFont); 
      g2.setColor(yLabelColor);
      xScale =1;
      yScale =1;
      int flip;
      
      if(yAxisSide == Plot.Y_AXIS_TO_LEFT){
    	  flip = -90;
      }else{
    	  flip = 90;
      }
      
      tx1 = AffineTransform.getRotateInstance(Math.toRadians(flip));
      g2.setFont((g2.getFont()).deriveFont(tx1));
      
      frc = g2.getFontRenderContext();
      gv = g2.getFont().createGlyphVector(frc, yLabel);
      yLabelWidth = (int) gv.getPixelBounds(null, 0, 0).getHeight();
      yLabelHeight = (int)gv.getPixelBounds(null, 0, 0).getWidth();
     
      if(checkScale){
    	  xProYLabel = (double)(plotCoords[3]/2)/(double)yLabelWidth; 
    	  yProYLabel = (double)plotCoords[2]/(double)yLabelHeight;
      }
      
      wProp = (double)(plotCoords[3]/2)/(double)yLabelWidth; 
      hProp = (double)plotCoords[2]/(double)yLabelHeight;
      
      if(wProp != xProYLabel){
		  yScale = wProp/xProYLabel;
	  }

	  if(hProp != yProYLabel){
		  xScale = hProp/yProYLabel;
	  }
      
      tx1.scale(yScale, xScale);
      g2.setFont(g2.getFont().deriveFont(tx1));
      
      frc = g2.getFontRenderContext();
      gv = g2.getFont().createGlyphVector(frc, yLabel);
      yLabelWidth = (int) gv.getPixelBounds(null, 0, 0).getHeight();
      yLabelHeight = (int)gv.getPixelBounds(null, 0, 0).getWidth();
      
      if(yAxisSide == Plot.Y_AXIS_TO_LEFT){
    	  g2.drawString(yLabel, plotCoords[0]+yLabelHeight, plotCoords[1]+(plotCoords[3]/2)+(yLabelWidth/2));
      }else{
    	  g2.drawString(yLabel, plotCoords[0]+plotCoords[2]-yLabelHeight, plotCoords[1]+(plotCoords[3]/2)-(yLabelWidth/2));
      }
      //################################################################
      int cont=0;
      if(nRow != 0 || nCol!=0){ //Draw the plots
    	  for(int j=0;j<nCol;j++){
    		  for(int i=0;i<nRow;i++){
    			 if(cont<=numberPlots){
    				 int widthPlot = (plotCoords[2]-(yLabelHeight*4))/nRow;
                     int heightPlot =(plotCoords[3]-(xLabelHeight*4))/nCol;
                     int xPlot = (plotCoords[0]+(yLabelHeight*2))+(widthPlot*i);
                     int yPlot = (plotCoords[1]+(xLabelHeight*2))+(heightPlot*j);
                     g2.drawRect(xPlot,yPlot,widthPlot,heightPlot);
    			 }
    			 cont++;
    		  }
    	  }
      }
   }
   
   private HashMap<String[],String[][]> splitFacetData(){
	   HashMap<String[],String[][]> splittedData = new HashMap<String[],String[][]>();
	   HashMap<String,String> tmpHash;
	   ArrayList<String> tmpFill;
	   String [][] tmpArray;
	   if(xFacetLevels != null && yFacetLevels != null){
		   for(int i=0;i<xFacetLevels.length;i++){
			   for(int j=0;j<yFacetLevels.length;j++){
				   tmpHash = new HashMap<String,String>();
				   tmpFill = new ArrayList<String>();
				   for(int k=0;k<xFacet.length;k++){
					   if(xFacet[k]==xFacetLevels[i] && yFacet[k] == yFacetLevels[j]){
						   tmpHash.put(xValues[k],yValues[k]);
						   tmpFill.add(fillValues[k]);
					   }
				   }
				  tmpArray = new String[tmpHash.size()][3];
				  Iterator<Entry<String,String>> it = tmpHash.entrySet().iterator();
				  int cont =0;
			      while(it.hasNext()) {
			    	  Map.Entry<String,String> e = (Map.Entry<String,String>)it.next();
			    	  tmpArray[cont][0]=e.getKey();
			    	  tmpArray[cont][1]=e.getValue();
			    	  tmpArray[cont][2]=tmpFill.get(cont);
			    	  cont++;
			      }			 
			      splittedData.put(new String[]{xFacetLevels[i],yFacetLevels[j]}, tmpArray);
			   }
		   }
	   }else{
		   if(yFacetLevels==null && xFacetLevels!=null){
			   for(int i=0;i<xFacetLevels.length;i++){
				   tmpHash = new HashMap<String,String>();
				   tmpFill = new ArrayList<String>();
				   for(int j=0;j<xFacet.length;j++){
					   if(xFacet[j] == xFacetLevels[i]){
						   tmpHash.put(xValues[j], yValues[j]);
						   tmpFill.add(fillValues[j]);
					   }
				   }
				   
				   tmpArray = new String[tmpHash.size()][3];
				   Iterator<Entry<String,String>> it = tmpHash.entrySet().iterator();
				   int cont =0;
				   while(it.hasNext()) {
					   Map.Entry<String,String> e = (Map.Entry<String,String>)it.next();
					   tmpArray[cont][0]=e.getKey();
					   tmpArray[cont][1]=e.getValue();
					   tmpArray[cont][2]=tmpFill.get(cont);
					   cont++;
				   }			 
				   splittedData.put(new String[]{xFacetLevels[i],null}, tmpArray);
			   }
			   
		   }else{
			   if(xFacetLevels==null && yFacetLevels!=null){
				   for(int i=0;i<yFacetLevels.length;i++){
					   tmpHash = new HashMap<String,String>();
					   tmpFill = new ArrayList<String>();
					   for(int j=0;j<yFacet.length;j++){
						   if(yFacet[j] == yFacetLevels[i]){
							   tmpHash.put(xValues[j], yValues[j]);
							   tmpFill.add(fillValues[j]);
						   }
					   }
					   tmpArray = new String[tmpHash.size()][3];
					   Iterator<Entry<String,String>> it = tmpHash.entrySet().iterator();
					   int cont =0;
					   while(it.hasNext()) {
						   Map.Entry<String,String> e = (Map.Entry<String,String>)it.next();
						   tmpArray[cont][0]=e.getKey();
						   tmpArray[cont][1]=e.getValue();
						   tmpArray[cont][2]=tmpFill.get(cont);
						   cont++;
					   }			 
					   splittedData.put(new String[]{null,yFacetLevels[i]}, tmpArray);
				   }
				   
			   }else{
				   if(xFacetLevels==null && yFacetLevels==null){
					   tmpArray = new String[xValues.length][3];
					   for(int i=0;i<tmpArray.length;i++){
						  tmpArray[i][0]=xValues[i];
						  tmpArray[i][1]=yValues[i];
						  tmpArray[i][2]=fillValues[i];
					   }
					   splittedData.put(null, tmpArray);
				   }
			   }
		   }
	   }
	   return splittedData;
   }
  
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
   	      List<Integer> scores = new ArrayList<Integer>();
	      Random random = new Random();
	      int maxDataPoints = 16;
	      int maxScore = 20;
	      for (int i = 0; i < maxDataPoints ; i++) {
	         scores.add(random.nextInt(maxScore));
	      }
	      
	      String[] testX = new String[]{"Pedro","Juan","Diego","Martina","Lorenzo","Martín","Pepe","Andrés","Maria","Juana","Andrea"};
	      String[] testY = new String[]{"23","50","100","12","12","-10","13","13","34","45","12"};
	      String[] fill = new String[]{"S","S","NS","DS","S","S","NS","NS","S","NS","S"};
	      String[] facetX = new String[]{"M","M","M","F","M","M","M","M","F","F","F"};
	      String[]facetY = new String[]{"Talca","Curico","Linares","Talca","Curico","Talca","Curico","Talca","Linares","Talca","Curico"};
         
	      
	      Plot mainPanel = new SciPlot(testY,testX,fill,facetX,facetY,2,3);
	      mainPanel.setXLabel("Avg of log per million of reads");
	      mainPanel.setYLabel("log2 of fold change");
	      mainPanel.setPlotTitle("DE expression analysis");
	      mainPanel.setLegendSide(Plot.LEGEND_TO_RIGHT);
	      mainPanel.setLegendOrientation(Plot.VERTICAL_LEGEND);
	      mainPanel.setPlotSubtitle("Super subtítulo");
	      mainPanel.setLegendTitle("Titulo");
	      JFrame frame = new JFrame("DrawGraph");
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.getContentPane().add(mainPanel);
	      frame.pack();
	      frame.setLocationByPlatform(true);
	      frame.setVisible(true);
         }
      });
   }
}
