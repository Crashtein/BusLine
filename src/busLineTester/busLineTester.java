package busLineTester;

import java.util.Set;

public class busLineTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Creating Bus Company!");
		BusLine busLineCompany = new BusLine();
		//Tworzenie testowych linii oraz dodawanie segmentów
		System.out.println("Creating Bus Line 1!");
		busLineCompany.addBusLine("Linia 1",0,0,7,-1);
		busLineCompany.addLineSegment("Linia 1",0,0,6,0);
		busLineCompany.addLineSegment("Linia 1",6,0,2,4);
		busLineCompany.addLineSegment("Linia 1",2,4,10,4);
		busLineCompany.addLineSegment("Linia 1",10,4,10,1);
		busLineCompany.addLineSegment("Linia 1",10,1,1,1);
		busLineCompany.addLineSegment("Linia 1",1,1,1,7);
		busLineCompany.addLineSegment("Linia 1",1,7,7,7);
		busLineCompany.addLineSegment("Linia 1",7,7,7,-1);
		System.out.println("Creating Bus Line 2!");
		busLineCompany.addBusLine("Linia 2",1,16,11,12);
		busLineCompany.addLineSegment("Linia 2",1,16,4,16);
		busLineCompany.addLineSegment("Linia 2",4,16,7,16);
		busLineCompany.addLineSegment("Linia 2",7,16,7,22);
		busLineCompany.addLineSegment("Linia 2",7,22,4,22);
		busLineCompany.addLineSegment("Linia 2",4,22,4,16);
		busLineCompany.addLineSegment("Linia 2",4,16,4,12);
		busLineCompany.addLineSegment("Linia 2",4,12,11,12);
		System.out.println("Creating Bus Line 3!");
		busLineCompany.addBusLine("Linia 3",5,25,-1,3);
		busLineCompany.addLineSegment("Linia 3",5,25,5,11);
		busLineCompany.addLineSegment("Linia 3",5,11,8,11);
		busLineCompany.addLineSegment("Linia 3",8,11,13,16);
		busLineCompany.addLineSegment("Linia 3",13,16,13,3);
		busLineCompany.addLineSegment("Linia 3",13,3,5,3);
		busLineCompany.addLineSegment("Linia 3",5,3,5,9);
		busLineCompany.addLineSegment("Linia 3",5,9,-1,3);
		System.out.println("Creating Bus Line 4!");
		busLineCompany.addBusLine("Linia 4",18,5,16,0);
		busLineCompany.addLineSegment("Linia 4",18,5,16,7);
		busLineCompany.addLineSegment("Linia 4",16,7,16,10);
		busLineCompany.addLineSegment("Linia 4",16,10,20,10);
		busLineCompany.addLineSegment("Linia 4",20,10,20,4);
		busLineCompany.addLineSegment("Linia 4",20,4,16,0);
		//Wyszukiwanie skrzy¿owañ
		busLineCompany.findIntersections();
		System.out.println("Intersection Found!");
		//Przedstawianie wyników
		System.out.println("Get lines: ");
		System.out.println(busLineCompany.getLines().toString());
		System.out.println("Get intersection positions: ");
		System.out.println(busLineCompany.getIntersectionPositions().toString());
		Set<String> keys = busLineCompany.getIntersectionPositions().keySet();
		for (String key : keys) {
			System.out.println(busLineCompany.getIntersectionPositions().get(key).size());
		}
		System.out.println("Get intersection with lines: ");
		System.out.println(busLineCompany.getIntersectionsWithLines().toString());
		System.out.println("Get intersection of lines pair: ");
		System.out.println(busLineCompany.getIntersectionOfLinesPair().toString());
	}
}
