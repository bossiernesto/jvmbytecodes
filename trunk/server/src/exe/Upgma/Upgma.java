package exe.Upgma;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Random;
import exe.*;

public class Upgma {

	private GAIGSarray distanceMatrix;
	private TreeNode[] nodes;
							//x1,  y1,  x2,   y2,    x1,   y1,   x2,  y2
	double[] matrixBounds = {-0.2, 0.05, 0.45, 0.55, 0.55, 0.05, 1.2, 0.55}; 
	double matrixFontSize = .08;
	private GAIGSphylogenetic_forest trees = new GAIGSphylogenetic_forest("tree", "#888888", 0.0, 0.1, 1.0, 0.9, .09);
	private int species;
	static ShowFile show;
    static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    static XMLtfQuestion tf;
    static XMLmsQuestion ms;
    static XMLmcQuestion mc;
    static XMLfibQuestion fib;
    static int id = 0;
    
    
	/**
	 * Default UPGMA constructor. Will create a matrix of size 4.
	 */
	Upgma() throws IOException	{
		species = 6;	//default species size
		trees.setLeaves(species);
		distanceMatrix = createDistanceMatrix();
		nodes = initializeTreeNodes();
	}
	Upgma(String size) throws IOException{
		species = Integer.parseInt(size);
		trees.setLeaves(species);
		distanceMatrix = createDistanceMatrix();
		nodes = initializeTreeNodes();
	}
	
	public static void main(String[] args) throws IOException{
		args[0] += ".sho";
		Upgma UPGMA;
		UPGMA = new Upgma();
		int totalQuestions = UPGMA.species * 3 -2;
		show = new ShowFile(args[0], totalQuestions, totalQuestions/2);

//		UPGMA.printText();
		while (UPGMA.species >1)
		{
			show.writeSnap("", null, "upgma.php?line=1", UPGMA.distanceMatrix, UPGMA.trees);
			UPGMA.showSmallest();
			UPGMA.matrixFontSize +=.005;
		}
		tf = new XMLtfQuestion(show, id + "");
		id ++;
		if (Math.random()>.5)
		{
		tf.setQuestionText("UPGMA assumes a constant rate of evolution.");
		tf.setAnswer(true);
		}
		else {
			tf.setQuestionText("UPGMA allows for a variable rate of evolution.");
			tf.setAnswer(false);
		}
		show.writeSnap("", null, "upgma.php?line=1", tf, UPGMA.distanceMatrix, UPGMA.trees);
		show.close();
	}
	
	public void printText(){
		for (int i = 0; i < species; i++)
		{
			System.out.print("species "+ i+ "\t");
			for (int j = 0; j < species; j++)
			{
				System.out.print(distanceMatrix.get(i, j) + "\t");
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
		double smallest = 99999;
		int x=0, y=0;
		
		ms = new XMLmsQuestion(show, id + "");
		id++;
		ms.setQuestionText("Which of the entries will be connected next");
		fib = new XMLfibQuestion(show, id + "");
		id++;
		fib.setQuestionText("What will be the value of the edges from the new ancestor to its left most tip/node? Note- Your answer must have 2 decimal places. For example 10.01");
		
		for (int i = 0; i < species; i++)
		{
			ms.addChoice(distanceMatrix.getRowLabel(i));
			for (int j = 0; j < i; j++)
			{
				if (Double.valueOf((String)distanceMatrix.get(i, j)) < smallest)
				{
					x= i;
					y = j;
					smallest =Double.valueOf((String) distanceMatrix.get(i,j));
				}
			}
		}
		ms.setAnswer(x+1);
		ms.setAnswer(y+1);
//		System.out.println("ms answer is "+ x + " "+ y);
		fib.setAnswer(decimalFormat.format(smallest/2.0) + "");
		show.writeSnap("", null, "upgma.php?line=2", ms, distanceMatrix, trees);
		
		distanceMatrix.setColor(x,y,"#FF0000");		
		show.writeSnap("", null, "upgma.php?line=2", fib, distanceMatrix, trees);
//		System.out.println("\n"+ smallest + "\t" + x + " "+ y);
		makeAncestor(x, y, smallest);
		newDistanceMatrix(x, y);
		
	}
	public double getWeightToLeaf(TreeNode curr){
		if (curr.getLeftChild() == null)
			return 0.0;
		return curr.getLeftChild().getLineToParent().getWeight() + getWeightToLeaf(curr.getLeftChild());
	}
	/**
	 * Passed location of smallest entry in the distance matrix.
	 * Creates a new node for the ancestor. New node is placed in the position
	 * of the smaller between x and y.
	 * 
	 * @param x row of smallest entry
	 * @param y column  of smallest entry
	 */
	public void makeAncestor(int x, int y, double smallest)throws IOException{
		trees.colorRoot(x, "#FF0000");
		trees.colorRoot(y,"#FF0000");
//		System.out.println("Making ancestor");
		show.writeSnap("",null, "upgma.php?line=3", distanceMatrix,trees);
		TreeNode ancestor = new TreeNode();
		ancestor.setHexColor("#FF0000");
		TreeNode[] newnodes = new TreeNode[species-1];
		int k = 0;
		double XweightToLeaf = getWeightToLeaf(nodes[x]);
		double YweightToLeaf = getWeightToLeaf(nodes[y]);
		XweightToLeaf = Double.valueOf(decimalFormat.format(smallest/2.0 - XweightToLeaf));
		YweightToLeaf = Double.valueOf(decimalFormat.format(smallest/2.0 - YweightToLeaf));
//		System.out.println(XweightToLeaf);
//		System.out.println(YweightToLeaf);
		//Used for placing the new tree in the "proper" position.
		//new tree goes in the place of the smaller descendant.
		if (x < y)
		{
			ancestor.insertLeftChild(nodes[x]);
			nodes[x].getLineToParent().setWeight(XweightToLeaf);
			ancestor.insertRightChild(nodes[y]);
			nodes[y].getLineToParent().setWeight(YweightToLeaf);
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
			}//for
		}//if
		else
		{
			k = 0;
			ancestor.insertLeftChild(nodes[y]);
			nodes[y].getLineToParent().setWeight(YweightToLeaf);
			ancestor.insertRightChild(nodes[x]);
			nodes[x].getLineToParent().setWeight(XweightToLeaf);
			
			//Sets the newnodes array. 
			//If node[i]== the smaller index of the new ancestor
			//then set the index k to be the ancestor.
			//else, set the index k to be the current node
			//and move on to the next node.
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
			}//for
		}//else
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
		//setting bounds of each tree. being replaced within forest.

		//Need to write trees array to shofile.
		show.writeSnap("",null, "upgma.php?line=3", distanceMatrix, trees);

	}
	/**
	 * Sets the matrix to have 0 in all used cells and 
	 * also sets the column and row labels.
	 * 
	 **/
	
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
		GAIGSarray Matrix = new GAIGSarray(species-1, species-1,"New Distance Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		Matrix = prepareMatrix(row, col, Matrix);
		GAIGSarray BlankMatrix = new GAIGSarray(species-1, species-1,"New Distance Matrix", "#999999", matrixBounds[4],matrixBounds[5],matrixBounds[6],matrixBounds[7], matrixFontSize);
		BlankMatrix = prepareMatrix(row, col, BlankMatrix);
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
		
		GAIGStext text = new GAIGStext(0.0, 0.0, "\\#236B8ENew Distance Matrix d(");
		int questionX=0, questionY=0;
		if (species >2){
		fib = new XMLfibQuestion(show, id + "");
		id++;
		Random rand = new Random();
		questionX = rand.nextInt(species-2)+1;
		questionY = rand.nextInt(questionX);
		
		fib.setQuestionText("What will the value in the new matrix at location ("+Matrix.getRowLabel(questionX)+ " , "+ Matrix.getRowLabel(questionY)+") be? Note- Your answer must have 2 decimal places. For example 10.01");
		
		text.setHalign(1);
//		System.out.println("Text size is "+text.getFontsize());
		text.setFontsize(0.05);
		if (species == 2)
			text.setText(text.getText()+"-)");
		//Snap has old matrix and new matrix with 0.0, - or " " in each spot.
		//show.writeSnap("", null, "upgma.php?line=4", fib,  Matrix, distanceMatrix, trees);
		}
		else{
			text.setHalign(1);
//			System.out.println("Text size is "+text.getFontsize());
			text.setFontsize(0.05);
			if (species == 2)
				text.setText(text.getText()+"-)");
		}
		show.writeSnap("", null, "upgma.php?line=4", BlankMatrix, distanceMatrixCopy, trees);	

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
//					System.out.println("i == row");
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
					val = val + Double.valueOf( (String) Matrix.get(yloc,ancestorIndex));
					Matrix.set(decimalFormat.format(val), yloc, ancestorIndex);
				
					//setup for snap
					if (setOne == false){
						showX = yloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						BlankMatrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 +");

					}
					else if (showX ==yloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"\\#006600 d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000]/2");
				//		show.writeSnap("",null, "upgma.php?line=4", Matrix, distanceMatrix, trees);
					}
				}
				else if (j == col)
				{
//					System.out.println("j == col");
					int xloc = i;
					if (xloc> row && xloc > col)
						xloc--;
					double val = Double.valueOf( (String) distanceMatrix.get(i, j)) /2.0;
					val = val + Double.valueOf( (String)Matrix.get(xloc,ancestorIndex));
					Matrix.set(decimalFormat.format(val), xloc, ancestorIndex);
				
					//setup for snap
					if (setOne == false){
						showX = xloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						BlankMatrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 +");

					}
					else if (showX ==xloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"\\#006600 d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000]/2");
					//	show.writeSnap("",null, "upgma.php?line=4", Matrix, distanceMatrix, trees);
					}
				}
				else if (j == row) 
				{
//					System.out.println("j == row");	
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
					val = val + Double.valueOf( (String)Matrix.get(yloc, ancestorIndex));
					Matrix.set(decimalFormat.format(val), yloc,ancestorIndex);
				
					//setup for snap
					if (setOne == false){
						showX = yloc;
						showY = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						BlankMatrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 +");

					}
					else if (showX ==yloc && showY == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"\\#006600 d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000]/2");
						//show.writeSnap("", null, "upgma.php?line=4",Matrix, distanceMatrix, trees);
					}
				}
				else if (i == col)
				{
//					System.out.println("i == col");
					int xloc = j;
					if (xloc > row && xloc > col)
						xloc--;						
					double val = Double.valueOf((String) distanceMatrix.get(i, j)) /2.0;
					val = val + Double.valueOf((String)Matrix.get(ancestorIndex, xloc));
					Matrix.set(decimalFormat.format(val), ancestorIndex, xloc); 
					
					//setup for snap
					if (setOne == false){
						showY = xloc;
						showX = ancestorIndex;
						setOne = true;
						Matrix.setColor(showX, showY, "#236B8E");
						BlankMatrix.setColor(showX, showY, "#236B8E");
						distanceMatrix.setColor(i, j, "#0000FF");
						text.setText(text.getText() +Matrix.getRowLabel(showX)+","+Matrix.getRowLabel(showY)+")\\#000000 = [\\#0000FFd("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000 +");

					}
					else if (showY ==xloc && showX == ancestorIndex){
						distanceMatrix.setColor(i, j, "#006600");
						text.setText(text.getText() +"\\#006600 d("+distanceMatrix.getRowLabel(i) +","+distanceMatrix.getRowLabel(j)+")\\#000000]/2");
						//show.writeSnap("", null, "upgma.php?line=4", Matrix, distanceMatrix, trees);
					}
				}
				else 
				{
//					System.out.println("Else Case");
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
				
			}//Inner for loop
		}//Outer for loop
		
			GAIGStext copies = new GAIGStext(0.5, 0.0, "Locations that are not related to the new ancestor node are copied straight into the new distance matrix.");
			copies.setHalign(0);
			fib.setAnswer((String)Matrix.get(questionX, questionY));

			show.writeSnap("", null, "upgma.php?line=4", CopiesMatrix, distanceMatrixCopy, trees, copies);
			show.writeSnap("", null, "upgma.php?line=4", fib, CopiesMatrix, distanceMatrix, trees, text);
			show.writeSnap("", null, "upgma.php?line=4",Matrix, distanceMatrix, trees, text);
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
		return Matrix;
		
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
