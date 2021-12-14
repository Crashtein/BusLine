package busLineTester;

import java.util.Set;

public class busLineTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Creating Bus Company!");
		BusLine busC = new BusLine();
		//Tworzenie testowych linii oraz dodawanie segmentów
		System.out.println("Creating Bus Line a!");
		busC.addBusLine("a",0,0,7,-1);
		busC.addLineSegment("a",0,0,6,0);
		busC.addLineSegment("a",6,0,2,4);
		busC.addLineSegment("a",2,4,10,4);
		busC.addLineSegment("a",10,4,10,1);
		busC.addLineSegment("a",10,1,1,1);
		busC.addLineSegment("a",1,1,1,7);
		busC.addLineSegment("a",1,7,7,7);
		busC.addLineSegment("a",7,7,7,-1);
		System.out.println("Creating Bus Line b!");
		busC.addBusLine("b",1,16,11,12);
		busC.addLineSegment("b",1,16,4,16);
		busC.addLineSegment("b",4,16,7,16);
		busC.addLineSegment("b",7,16,7,22);
		busC.addLineSegment("b",7,22,4,22);
		busC.addLineSegment("b",4,22,4,16);
		busC.addLineSegment("b",4,16,4,12);
		busC.addLineSegment("b",4,12,11,12);
		System.out.println("Creating Bus Line c!");
		busC.addBusLine("c",5,25,-1,3);
		busC.addLineSegment("c",5,25,5,11);
		busC.addLineSegment("c",5,11,8,11);
		busC.addLineSegment("c",8,11,13,16);
		busC.addLineSegment("c",13,16,13,3);
		busC.addLineSegment("c",13,3,5,3);
		busC.addLineSegment("c",5,3,5,9);
		busC.addLineSegment("c",5,9,-1,3);
		System.out.println("Creating Bus Line d!");
		busC.addBusLine("d",18,5,16,0);
		busC.addLineSegment("d",18,5,16,7);
		busC.addLineSegment("d",16,7,16,10);
		busC.addLineSegment("d",16,10,20,10);
		busC.addLineSegment("d",20,10,20,4);
		busC.addLineSegment("d",20,4,16,0);
		//Wyszukiwanie skrzy¿owañ
		busC.findIntersections();
		System.out.println("Intersection Found!");
		//Przedstawianie wyników
		System.out.println("Get lines: ");
		System.out.println(busC.getLines().toString());
		System.out.println("Get intersection positions: ");
		System.out.println(busC.getIntersectionPositions().toString());
		System.out.println("Get intersection with lines: ");
		System.out.println(busC.getIntersectionsWithLines().toString());
		System.out.println("Get intersection of lines pair: ");
		System.out.println(busC.getIntersectionOfLinesPair().toString());
		
		
		
	}
}
