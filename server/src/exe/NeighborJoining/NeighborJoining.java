package exe.NeighborJoining;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;
import exe.*;

public class NeighborJoining {

	final double[] matrixBounds = {-0.25, 0.05, 0.35, 0.55, 0.65, 0.05, 1.4, 0.55, .15, 0.05, 0.85, 0.55};
//	char c = (char)931;
//	final String sigma = String.valueOf(c);
	final String sigma = "Sums";
	double matrixFontSize = .08;
	private GAIGSphylogenetic_forest trees = new GAIGSphylogenetic_forest("tree", "#888888", 0.0, 0.1, 1.0, 0.9, .08);
	
	private GAIGSarray distanceMatrix;
	private GAIGSarray QMatrix;
	private TreeNode[] nodes;
	private GAIGSarray sums;
	private int species;
	static ShowFile show;
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static XMLtfQuestion tf;
    static XMLmsQuestion ms;
    static XMLmcQuestion mc;
    static XMLfibQuestion fib;
    static int id = 0;
    
    /**
	 * Default NeighborJoining constructor. Will create a matrix of size 6.
	 */
	NeighborJoining() throws IOException{
		species = 6;	//default species size
		trees.setLeaves(species);
		distanceMatrix = createDistanceMatrix();
		nodes = initializeTreeNodes();
	}
	NeighborJoining(String size) throws IOException{
		species = Integer.parseInt(size);
		trees.setLeaves(species);
		distanceMatrix = createDistanceMatrix();
		nodes = initializeTreeNodes();
	}
	
	public static void main(String[] args) throws IOException{
		args[0] += ".sho";
		show = new ShowFile(args[0]);
		NeighborJoining NJ = new NeighborJoining();
		
		while (NJ.species >=2)
		{
			NJ.showSmallest();
		}
		tf = new XMLtfQuestion(show, id + "");
		id ++;
		if (Math.random()>.5)
		{
		tf.setQuestionText("Neighbor Joining assumes a constant rate of evolution.");
		tf.setAnswer(false);
		}
		else {
			tf.setQuestionText("Neighbor Joining allows for a variable rate of evolution.");
			tf.setAnswer(true);
		}
		show.writeSnap("", null, "NJ.php?line=1", tf, NJ.distanceMatrix, NJ.trees);
		show.close();
	}
	public void printText(GAIGSarray matrix){
		for (int i = 0; i < species; i++)
		{
			System.out.print("species "+ i+ "\t");
			for (int j = 0; j < species; j++)
			{
				System.out.print(matrix.get(i, j) + "\t");
			}
			System.out.println("");
		}
	}
	/**
	 * showSmallest searches the distance matrix for the smallest entry.
	 * After finding the location of the smallest entry it calls makeAncestor 
	 * and passes in the location. Following makeAncestor a call to newDistanceMatrix
	 * is made with the location of the smallest entry.
	 */
	public void showSmallest() throws IOException{
		QMatrix = makeQ("empty");
		show.writeSnap("", null, "NJ.php?line=2", distanceMatrix, sums, QMatrix, trees);

		int questionX=0, questionY=0;
		GAIGStext text = new GAIGStext();
		if (species >2){		
			fib = new XMLfibQuestion(show, id + "");
			id++;
			Random rand = new Random();
			questionX = rand.nextInt(species-2)+1;
			questionY = rand.nextInt(questionX);
			String Lx = QMatrix.getRowLabel(questionX);
			String Ly = QMatrix.getRowLabel(questionY);
			fib.setQuestionText("What will be the value placed in Q("+Lx+","+Ly+")? Note- Your answer must have 2 decimal places. For example 10.01");
			
			
			distanceMatrix.setColor(questionX, questionY, "#FF0000");
			sums.setColor(questionY, "#0000FF");
			sums.setColor(questionX, "#006600");
			QMatrix.setColor(questionX, questionY, "#236B8E");
			text = new GAIGStext(-0.1, 0.0, "\\#236B8EQ("+Lx+","+Ly+")\\#000000 = ("+ species +" -2) * \\#FF0000d("+Lx+","+Ly+")\\#000000 - \\#006600"+ sigma+ "("+Lx+",k)\\#000000 - \\#0000FF"+sigma+ "("+Ly+",k)\\#000000");
			text.setHalign(1);
			show.writeSnap("", null, "NJ.php?line=2", fib, distanceMatrix, QMatrix, trees, text, sums);
			}

		QMatrix = makeQ("fill in");

		if (species > 2)
		{
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)QMatrix.get(questionX, questionY))+.01));
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)QMatrix.get(questionX, questionY))-.01));
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)QMatrix.get(questionX, questionY))));
			text.setText(text.getText()+ "= "+ decimalFormat.format(Double.parseDouble((String)QMatrix.get(questionX, questionY))));
		}
		QMatrix.setColor(questionX,questionY, "#236B8E");
		show.writeSnap("", null, "NJ.php?line=2", distanceMatrix, QMatrix, trees, text, sums);
		sums.setColor(questionY, "#999999");
		sums.setColor(questionX, "#999999");
		distanceMatrix.setColor(questionX,questionY, "#999999");
		QMatrix.setColor(questionX,questionY, "#999999");
	
		double smallest = 99999;
		int x = 0, y = 0;
		
		ms = new XMLmsQuestion(show, id + "");
		id++;
		ms.setQuestionText("Which of the entries will be connected next");


		for (int i = 0; i < species; i++)
		{
			ms.addChoice(distanceMatrix.getRowLabel(i));
			for (int j = 0; j < i; j++)
			{
				if (Double.valueOf((String)QMatrix.get(i, j)) < smallest)
				{
					x = i;
					y = j;
					smallest = Double.valueOf((String) QMatrix.get(i,j));
				}
			}
		}
		ms.setAnswer(x+1);
		ms.setAnswer(y+1);
		System.out.println("ms answer is "+ x + " "+ y);
		show.writeSnap("", null, "NJ.php?line=3", ms, distanceMatrix, sums, QMatrix, trees);
		QMatrix.setColor(x, y, "#FF0000");
		distanceMatrix.setColor(x,y,"#FF0000");
		show.writeSnap("", null, "NJ.php?line=3", distanceMatrix, sums, QMatrix, trees);
		System.out.println("\n" + smallest + "\t" + x + " " + y);
		makeAncestor(x, y);
		newDistanceMatrix(x, y);

	}
	/**
	 * Passed location of smallest entry in the distance matrix.
	 * Creates a new node for the ancestor. New node is placed in the position
	 * of the smaller between x and y.
	 * 
	 * @param x row of smallest entry
	 * @param y column  of smallest entry
	 */
	public void makeAncestor(int x, int y)throws IOException{
		trees.colorRoot(x, "#FF0000");
		trees.colorRoot(y,"#FF0000");
		System.out.println("Making ancestor");

		TreeNode ancestor = new TreeNode();
		ancestor.setHexColor("#FF0000");
		TreeNode[] newnodes = new TreeNode[species-1];
		String Lx = QMatrix.getRowLabel(x);
		String Ly = QMatrix.getRowLabel(y);
		distanceMatrix.setColor(x,y, "#FF0000");
		sums.setColor(x, "#006600");
		sums.setColor(y, "#0000FF");
		GAIGStext text1 = new GAIGStext(-0.1, 0.0, "d("+ Lx+","+Ly + Lx+ ") = .5 * \\#FF0000d("+Lx+","+Ly+")\\#000000 + 1/ [2*("+ species +" -2)] * [\\#006600"+ sigma+ "("+Lx+",k)\\#000000 -\\#0000FF "+sigma+ "("+Ly+",k)\\#000000]");
		GAIGStext text2 = new GAIGStext(-0.1, -0.06, "d("+ Ly+","+Ly + Lx+ ") = .5 * \\#FF0000d("+Lx+","+Ly+")\\#000000 + 1/ [2*("+ species +" -2)] * [\\#0000FF"+ sigma+ "("+Ly+",k)\\#000000 -\\#006600 "+sigma+ "("+Lx+",k)\\#000000]");
		text1.setHalign(1);
		text2.setHalign(1);
		
		fib = new XMLfibQuestion(show, id + "");
		id++;
		boolean left;
		if (Math.random() > .5)	{
			fib.setQuestionText("What will be the value from the RIGHT node highlighted to its soon to be added ancestor? Note- Your answer must have 2 decimal places. For example 10.01");
			left = false;
		}
		else {
			fib.setQuestionText("What will be the value from the LEFT node highlighted to its soon to be added ancestor? Note- Your answer must have 2 decimal places. For example 10.01");
			left = true;
		}
		show.writeSnap("",null, "NJ.php?line=4", fib, distanceMatrix, trees, sums, text1, text2);

		int k = 0;
		//get edge weights.
		int cheapFix = 2;
		if (species ==2)
			cheapFix--;
		double xWeight = Double.parseDouble((String)sums.get(x)) - Double.parseDouble((String)sums.get(y));
		xWeight = xWeight * (1.0/ (2*(species-cheapFix)));
		xWeight = Double.parseDouble(decimalFormat.format(.5 * Double.parseDouble((String)distanceMatrix.get(x, y)) + xWeight));
		text1.setText(text1.getText()+ "= "+decimalFormat.format(xWeight));
		double yWeight = Double.parseDouble((String)sums.get(y)) - Double.parseDouble((String)sums.get(x));
		yWeight = yWeight * (1.0/ (2*(species-cheapFix)));
		yWeight =Double.parseDouble(decimalFormat.format(.5 * Double.parseDouble((String)distanceMatrix.get(x, y)) + yWeight));
		text2.setText(text2.getText()+ "= "+decimalFormat.format(yWeight));
		if (x < y)
		{
			ancestor.insertLeftChild(nodes[x]);
			nodes[x].getLineToParent().setWeight(xWeight);
			ancestor.insertRightChild(nodes[y]);
			nodes[y].getLineToParent().setWeight(yWeight);
			if (left)
			{
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight())));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight()-.01)));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight()+.01)));
			}
			else {
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight())));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight()-.01)));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight()+.01)));		
			}
			
			for (int i = 0; i < nodes.length; i++)
			{
				if (nodes[i] == nodes[x]){
					newnodes[k] = ancestor;
					k++;}
				else if (nodes[i] == nodes[y])
					continue;
				else{
					newnodes[k]= nodes[i];
					k++;}
			}
		}
		else
		{
			k = 0;
			ancestor.insertLeftChild(nodes[y]);
			nodes[y].getLineToParent().setWeight(yWeight);
			ancestor.insertRightChild(nodes[x]);
			nodes[x].getLineToParent().setWeight(xWeight);
			
			if (left)
			{
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight())));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight()-.01)));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[y].getLineToParent().getWeight()+.01)));
			}
			else {
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight())));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight()-.01)));
				fib.setAnswer(String.valueOf(decimalFormat.format(nodes[x].getLineToParent().getWeight()+.01)));		
			}
			
			for (int i = 0; i < nodes.length; i++)
			{
				if (nodes[i] == nodes[y]){
					newnodes[k] = ancestor;
					k++;}
				else if (nodes[i] == nodes[x])
					continue;
				else{
					newnodes[k]= nodes[i];
					k++;}
			}
		}
		trees.colorRoot(x, "#888888");
		trees.colorRoot(y,"#888888");
		ancestor.setHexColor("#000000");
		nodes = newnodes;
		trees.prepareForRefinedTrees();

		
		for (int i = 0; i < nodes.length; i++)
		{
			GAIGSphylogenetic_tree temp = new GAIGSphylogenetic_tree(true);
			temp.setRoot(nodes[i]);
			trees.addElement(temp);
		}
		trees.setTreeBounds();
		
		show.writeSnap("", null, "NJ.php?line=5", distanceMatrix, trees, sums, text1, text2);
		sums.setColor(x, "#999999");
		sums.setColor(y, "#999999");

	}
	
	public GAIGSarray prepareMatrix(int row, int col, GAIGSarray matrix){

		int high= col, lowest = row;
		if (lowest > high)
		{
			lowest = col;
			high = row;
		}
		String label = distanceMatrix.getRowLabel(lowest) + distanceMatrix.getRowLabel(high);
		matrix.setRowLabel(label, lowest);
		matrix.setColLabel(label, lowest);
		for (int i = 0; i < species-1; i++)
		{
			for (int j = 0; j < i; j++)
			{
				matrix.set("0.0",i,j);
				matrix.set(" ", j, i);
			}
			matrix.set("-", i , i);
		}
		
		for ( int i = 0; i < species; i++)
		{
			if (i > high)
			{
				label = distanceMatrix.getRowLabel(i);
				matrix.setRowLabel(label, i-1);
				matrix.setColLabel(label, i-1);
			}
			else if (i == lowest || i == high)
				continue;
			else
				{
				label = distanceMatrix.getRowLabel(i);
				matrix.setRowLabel(label, i);
				matrix.setColLabel(label, i);
				}
		}
		return matrix;
	}
	/**Creates a new GAIGSarray and fills it with values from the old matrix.
	 * After creating the next distance matrix the old matrix is set equal to
	 * the new matrix.
	 * 
	 * @param row
	 * @param col
	 * @throws IOException - Takes snap and can throw an exception.
	 */
	public void newDistanceMatrix(int row, int col) throws IOException{
		int lowest = row;
		if (lowest > col)
			lowest = col;
		
		double rowWeight = Double.parseDouble((String)sums.get(row)) - Double.parseDouble((String)sums.get(col));
		int cheapFix = 2;
		if (species ==2)
			cheapFix--;
		rowWeight = rowWeight * (1.0/ (2.0*(species-cheapFix)));
		rowWeight = .5 * Double.parseDouble((String)distanceMatrix.get(row, col)) + rowWeight;
		double colWeight = Double.parseDouble((String)sums.get(col)) - Double.parseDouble((String)sums.get(row));
		colWeight = colWeight * (1.0/ (2.0*(species-cheapFix)));
		colWeight = .5 * Double.parseDouble((String)distanceMatrix.get(row, col)) + colWeight;
		
		
		GAIGSarray Matrix = new GAIGSarray(species-1, species-1, "New Distance Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		GAIGSarray BlankMatrix = new GAIGSarray(species-1, species-1, "New Distance Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		BlankMatrix = prepareMatrix(row, col, BlankMatrix);
		
		GAIGStext text = new GAIGStext(-0.1, 0.0, "\\#236B8ENew Distance Matrix (");
		text.setHalign(1);
		
		GAIGSarray CopiesMatrix = new GAIGSarray(species-1, species-1,"New Distance Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		CopiesMatrix = prepareMatrix(row, col, CopiesMatrix);
		GAIGSarray distanceMatrixCopy = new GAIGSarray(species, species, "Distance Matrix", "#999999", matrixBounds[0],matrixBounds[1],matrixBounds[2],matrixBounds[3], matrixFontSize);
		
		for (int i = 0; i < species; i ++){
			for(int j = 0; j < species; j++){
				distanceMatrixCopy.set(distanceMatrix.get(i, j), i, j);
			}
			distanceMatrixCopy.setRowLabel(distanceMatrix.getRowLabel(i), i);
			distanceMatrixCopy.setColLabel(distanceMatrix.getRowLabel(i), i);
		}
		distanceMatrixCopy.setColor(row, col, "#FF0000");
		
		
		if (species == 2)
			text.setText(text.getText()+"-)");
		Matrix = prepareMatrix(row, col, Matrix);
		int questionX=0, questionY=0;
		if (species > 2)
		{
			fib = new XMLfibQuestion(show, id + "");
			id++;
			Random rand = new Random();
			questionX = rand.nextInt(species-2)+1;
			questionY = rand.nextInt(questionX);
			fib.setQuestionText("What will the value in the new matrix at location ("+Matrix.getRowLabel(questionX)+ " , "+ Matrix.getRowLabel(questionY)+") be? Note- Your answer must have 2 decimal places. For example 10.01");
		}

		//variables for snaps of building a matrix.
		boolean setOne = false;
		int showX=-99, showY=-99;
		int ancestorIndex = row;
		int higherIndex = col;
		if (row > col){
			ancestorIndex = col;
			higherIndex = row;}
		
		for (int i = 1; i < species; i++)
		{
			for (int j = 0; j < i; j++)
			{
				ancestorIndex = row;
				if (row > col)
					ancestorIndex = col;
				//At smallest value. Doesn't get added in the new matrix.
				if ((i == row && j ==col) || (i ==col && j ==row))
				{
					continue;
				}
				if (i == row) 
				{
					int yloc = j;
					if (yloc < ancestorIndex)
					{
						int temp = yloc;
						yloc = ancestorIndex;
						ancestorIndex = temp;
					}
					if (yloc > row && yloc > col)
						yloc--;
					double val = Double.valueOf( (String) distanceMatrix.get(i, j)) /2.0;
					val = val - rowWeight/2.0;
					val = val + Double.valueOf( (String) Matrix.get(yloc,ancestorIndex));	
					Matrix.set(decimalFormat.format(val), yloc, ancestorIndex);
					
					//setup for snap
					if (setOne == false){
						showX = yloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = .5 [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+Matrix.getRowLabel(showY)+")\\#000000 - d("+ distanceMatrix.getRowLabel(i)+","+Matrix.getRowLabel(showX)+")]");
					}
					else if (showX ==yloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"+ .5 [\\#006600d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 - d("+ distanceMatrix.getRowLabel(i)+","+Matrix.getRowLabel(lowest)+")]");
					}
				}
				else if (j == col)
				{
					int xloc = i;
					if (xloc> row && xloc > col)
						xloc--;	
					
					double val = Double.valueOf( (String) distanceMatrix.get(i, j)) /2.0;
					val = val - colWeight/2.0;
					val = val + Double.valueOf( (String)Matrix.get(xloc,ancestorIndex));
					Matrix.set(decimalFormat.format(val), xloc, ancestorIndex);
					
					//setup for snap
					if (setOne == false){
						showX = xloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = .5 [\\#0000FFd("+distanceMatrix.getRowLabel(j) +","+Matrix.getRowLabel(showX)+")\\#000000 - d("+ distanceMatrix.getRowLabel(j)+","+Matrix.getRowLabel(showY)+")]");

					}
					else if (showX ==xloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"+ .5 [\\#006600d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 - d("+ distanceMatrix.getRowLabel(i)+","+Matrix.getRowLabel(showX)+")]");
					}
				}
				else if (j == row) 
				{
					int yloc = i;

					if (yloc < ancestorIndex)
					{
						int temp = yloc;
						yloc = ancestorIndex;
						ancestorIndex = temp;
					}
					if (yloc > row && yloc > col)
						yloc--;

					double val = Double.valueOf( (String) distanceMatrix.get(i, j)) /2.0;
					val = val - rowWeight/2.0;
					val = val + Double.valueOf( (String)Matrix.get(yloc, ancestorIndex));
					Matrix.set(decimalFormat.format(val), yloc,ancestorIndex);
					
					//setup for snap
					if (setOne == false){
						showX = yloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = .5 [\\#0000FFd("+distanceMatrix.getRowLabel(j) +","+Matrix.getRowLabel(showY)+")\\#000000 - d("+ distanceMatrix.getRowLabel(j)+","+Matrix.getRowLabel(showX)+")]");

					}
					else if (showX ==yloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"+ .5 [\\#006600d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 - d("+ distanceMatrix.getRowLabel(j)+","+Matrix.getRowLabel(showY)+")]");
					}
					
				}
				else if (i == col)
				{
					int xloc = j;
					if (xloc > row && xloc > col)
						xloc--;									
					double val = Double.valueOf((String) distanceMatrix.get(i, j)) /2.0;
					val = val - colWeight/2.0;
					val = val + Double.valueOf((String)Matrix.get(ancestorIndex, xloc));
					Matrix.set(decimalFormat.format(val), ancestorIndex, xloc);
					
					//setup for snap
					if (setOne == false){
						showY = xloc;
						showX = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = .5 [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+Matrix.getRowLabel(showY)+")\\#000000 - d("+ distanceMatrix.getRowLabel(i)+","+Matrix.getRowLabel(showX)+")]");

					}
					else if (showY ==xloc && showX == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"+ .5 [\\#006600d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 - d("+ distanceMatrix.getRowLabel(i)+","+Matrix.getRowLabel(showX)+")]");
					}
				}
				else 
				{
					int xloc = i;
					int yloc = j;
					
					if (xloc > higherIndex)
						xloc--;
					if (yloc > higherIndex)
						yloc--;
					double val = Double.valueOf((String) distanceMatrix.get(i, j));
					Matrix.set(decimalFormat.format(val), xloc, yloc);
					CopiesMatrix.set(decimalFormat.format(val), xloc, yloc);
						
					//no value in common with new matrix.
				}
			}
		}
		GAIGStext copies = new GAIGStext(0.5, 0.0, "Locations that are not related to the new ancestor node are copied straight into the new distance matrix.");
		copies.setHalign(0);
		show.writeSnap("", null, "NJ.php?line=6", BlankMatrix, distanceMatrixCopy, trees, copies, sums);		
		show.writeSnap("", null, "NJ.php?line=6", CopiesMatrix, distanceMatrixCopy, trees, copies, sums);
		
		if (species>2){
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)Matrix.get(questionX, questionY))+.01));
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)Matrix.get(questionX, questionY))-.01));
			fib.setAnswer(decimalFormat.format(Double.parseDouble((String)Matrix.get(questionX, questionY))));
//			show.writeSnap("", null, "NJ.php?line=6", fib, BlankMatrix, distanceMatrix, trees, sums, text);
			CopiesMatrix.setColor(showX, showY, "#236B8E");
			show.writeSnap("", null, "NJ.php?line=6", fib, CopiesMatrix, distanceMatrix, trees, text, sums);
			text.setText(text.getText() + "= "+ decimalFormat.format(Double.parseDouble((String)Matrix.get(showX, showY))));


		}
		show.writeSnap("", null, "NJ.php?line=6", Matrix, distanceMatrix, trees, sums,text);
		species--;
		distanceMatrix = new GAIGSarray(species, species, "Distance Matrix", "#999999", matrixBounds[0],matrixBounds[1],matrixBounds[2],matrixBounds[3], matrixFontSize);
		for (int i = 0; i < species; i ++){
			for(int j = 0; j < species; j++){
				distanceMatrix.set(Matrix.get(i, j), i, j);
			}
			distanceMatrix.setRowLabel(Matrix.getRowLabel(i), i);
			distanceMatrix.setColLabel(Matrix.getRowLabel(i), i);
		}				
	}
	/**
	 * 
	 * @returns
	 * @throws IOException -makes a snap and so needs to throw IOException.
	 */
	public GAIGSarray createDistanceMatrix() throws IOException {
		
		GAIGSarray Matrix = new GAIGSarray(species, species, "Distance Matrix", "#999999", matrixBounds[0],matrixBounds[1],matrixBounds[2],matrixBounds[3], matrixFontSize);
		Random rand = new Random();
		
		char name = 'A';
		for (int i = 0; i < species; i++){
			
			for (int j = 0; j <i; j++){
				double val = rand.nextDouble() * 100;
				Matrix.set(decimalFormat.format(val),i,j);
				Matrix.set(" ", j, i);
			}
			Matrix.setRowLabel(String.valueOf(name), i);
			Matrix.setColLabel(String.valueOf(name), i);
			name++;
			Matrix.set("-", i, i);
		}
		/*
		Matrix.set("20.00",1,0);
		Matrix.set("21.00",2,0);
		Matrix.set("16.00",3,0);
		Matrix.set("18.00",4,0);
		Matrix.set("21.00",2,1);
		Matrix.set("14.00",3,1);
		Matrix.set("14.00",4,1);
		Matrix.set("17.00",3,2);
		Matrix.set("18.00",4,2);
		Matrix.set("13.00",4,3);
		
		Matrix.set(" ",0,1);
		Matrix.set(" ",0,2);
		Matrix.set(" ",0,3);
		Matrix.set(" ",0,4);
		Matrix.set(" ",1,2);
		Matrix.set(" ",1,3);
		Matrix.set(" ",1,4);
		Matrix.set(" ",2,3);
		Matrix.set(" ",2,4);
		Matrix.set(" ",3,4);
		*/
		
		
		return Matrix;
	}

	/**
	 * 
	 * @param dist
	 * @return
	 * @throws IOException
	 */
	public GAIGSarray makeQ(String flag) throws IOException
	{
		GAIGSarray q = new GAIGSarray(species, species, "Q Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		
		//create qmatrix filling it with empty values.
		if (flag.equalsIgnoreCase("empty"))
		{
			sums = getDistanceSummations();
			for (int i = 0; i < species; i++){
				for (int j = 0; j < i; j++)
				{
					q.set(decimalFormat.format(0.00), i, j);
					q.set(" ", j, i);
				}
				q.set("-", i, i);
				q.setRowLabel(distanceMatrix.getRowLabel(i), i);
				q.setColLabel(distanceMatrix.getRowLabel(i), i);
			}
			show.writeSnap("", null, "NJ.php?line=1", distanceMatrix, sums, trees);
		}
		
		//create q matrix filling it with correct values.
		else {
			for (int i = 0; i < species; i++)
			{
				for (int j = 0; j < i; j++)
				{
					double val = (species-2)* Double.parseDouble((String)distanceMatrix.get(i, j));
					val = val - Double.parseDouble((String)sums.get(i)) - Double.parseDouble((String)sums.get(j)); 
					q.set(decimalFormat.format(val), i, j);
					q.set(" ", j, i);
				}
				q.set("-", i, i);
				q.setRowLabel(distanceMatrix.getRowLabel(i), i);
				q.setColLabel(distanceMatrix.getRowLabel(i), i);
			}
		}		
		return q;
	}
	/**
	 * fills an array with the summation data
	 * to be used later by neighbor joining.
	 * @return
	 */
	public GAIGSarray getDistanceSummations(){
		GAIGSarray sums = new GAIGSarray(species, false, sigma, "#999999",matrixBounds[8], matrixBounds[9] ,matrixBounds[10], matrixBounds[11], matrixFontSize);
		//set sums with default values.
		for (int i = 0; i < species; i ++)
			sums.set("0.0", i);
		for (int i = 0; i < species; i++){
			for (int j = 0; j < i; j++){

				sums.set(decimalFormat.format(Double.parseDouble((String)sums.get(i)) + Double.parseDouble((String)distanceMatrix.get(i, j))), i);
				sums.set(decimalFormat.format(Double.parseDouble((String)sums.get(j)) + Double.parseDouble((String)distanceMatrix.get(i, j))), j);
			}
		}
		return sums;
	}
	
	public TreeNode[] initializeTreeNodes(){
		TreeNode[] nodes = new TreeNode[species];

		char name = 'A';
		for (int i = 0; i < species; i ++)
		{
			nodes[i]= new TreeNode(i + "");
			nodes[i].setHexColor("#000000");
			GAIGSphylogenetic_tree temp = new GAIGSphylogenetic_tree(true);
			temp.setRoot(nodes[i]);
			temp.getRoot().setValue(String.valueOf(name));
			name++;
			trees.addElement(temp);
		}
		return nodes;
	}
}
