import java.util.Scanner;
import java.util.ArrayList;

public class Karuko {
	public static void main(String[] args){
		// Read from stdin
		Scanner scan = new Scanner(System.in);
		String[] dims = scan.nextLine().split("\\s+");
		int w = Integer.parseInt(dims[0]);
		int h = Integer.parseInt(dims[1]);
		int[][] board = new int[h][w];
		for (int i = 0; i < h; i++){
			for (int j = 0; j < w; j++){
				board[i][j] = 1; // Set defaults to 1
			}
		}

		// Store the hints in a temprary list
		ArrayList<String> sums = new ArrayList<>();
		while (scan.hasNextLine()){
			sums.add(scan.nextLine());
		}
		scan.close();

		// These arrays will be used to check the solution
		int[] sumVals = new int[sums.size()]; // The sums for each hint
		int[][][] coordsVals = new int[sums.size()][Math.max(w,h)][2]; // 1-num of sums, 2-num of squares, 3-coords
		int[] limits = new int[sums.size()]; // NUmber of squares that make up the sum

		// Initialize the arrays above
		for (int i = 0; i < sums.size(); i++){
			String[] parts = sums.get(i).split("\\s+");
			int rowSum = Integer.parseInt(parts[0]);
			sumVals[i] = rowSum;
			for (int j = 1; j < parts.length; j++){
				int[] coords = coordinates(parts[j]);
				coordsVals[i][j-1][0] = coords[0];
				coordsVals[i][j-1][1] = coords[1];
			}
			limits[i] = parts.length-1;
		}

		// The brute force method. Vary brutal. Not for the weak-hearted.
		int stopSquare = 0;
		int i = 0;
		while (true){
			boolean distinct = rowsAndColsAreDistinct(board);
			boolean valid = boardIsValid(board, coordsVals, sumVals, limits);
			if (distinct && valid){
				printBoard(board);
				break;
			}
			int j = 0;
			int[] coords2 = indexToCoords(i+j,h,w);
			board[coords2[1]][coords2[0]]++;
			while (board[coords2[1]][coords2[0]] > 9){
				// Move onto next square
				board[coords2[1]][coords2[0]] = 1;
				j++;
				if (j >= w*h){
					System.out.println("Couldn't find a solution");
					break;
				}
				coords2 = indexToCoords(i+j,h,w);
				board[coords2[1]][coords2[0]]++; // Add 1 to next square
			}
		}
	}

	private static void printBoard(int[][] board){
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[0].length; j++){
				System.out.print(board[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
	}

	private static int[] indexToCoords(int x,int h, int w){
		int[] coords = new int[2];
		coords[0] = x % w;
		coords[1] = (x-coords[0])/w % h;
		return coords;
	}

	private static boolean rowsAndColsAreDistinct(int[][] board){
		// Check rows
		for (int i = 0; i < board.length; i++){
			boolean[] nums = {false,false,false,false,false,false,false,false,false};
			for (int j = 0; j < board[0].length; j++){
				if (!nums[j]){
					nums[j] = true;
				}
				else{
					return false;
				}
			}
		}

		// Check cols
		for (int i = 0; i < board[0].length; i++){
			boolean[] nums = {false,false,false,false,false,false,false,false,false};
			for (int j = 0; j < board.length; j++){
				if (!nums[j]){
					nums[j] = true;
				}
				else{
					return false;
				}
			}
		}

		return true;
	}

	private static boolean boardIsValid(int[][] board, int[][][] coordsVals, int[] sumVals, int[] limits){
		for (int i = 0; i < sumVals.length; i++){
			int sum = 0;
			int limit = limits[i];
			for (int j = 0; j < limit; j++){
				int[] coords = coordsVals[i][j];
				sum += board[coords[1]][coords[0]];
			}
			if (sum != sumVals[i]){
				return false;
			}
		}
		return true;
	}

	private static int[] coordinates(String coord){
		char[] charCoords = coord.toCharArray();
		int x = charCoords[0]-'A';
		int y = charCoords[1]-'1';
		int[] coords = {x,y};
		return coords;
	}
}