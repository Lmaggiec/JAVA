package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LSE_driver {


	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter name of main file: ");
		String fileName =  "docs.txt";
		System.out.println(fileName);
		System.out.println("Enter name of noise file: ");
		String noiseFile = "noisewords.txt";
		System.out.println(noiseFile);
		
		LittleSearchEngine lse = new LittleSearchEngine();
		try {
			lse.makeIndex(fileName,noiseFile);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found");
		}
		System.out.print("keyword 1: ");
		String keyword1 =  scanner.next();
		System.out.print("keyword 2: ");
		String keyword2 = scanner.next();
		scanner.close();
		ArrayList<String> results = lse.top5search(keyword1, keyword2);
		System.out.println(results);
		System.exit(0);
	}

}

