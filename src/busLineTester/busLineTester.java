package busLineTester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class busLineTester {

	public static void main(String[] args) {
		System.out.println("Test #1!");
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
		//Wyszukiwanie skrzyżowań
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
		
		
		
		System.out.println("Test #2!");
		// TODO Auto-generated method stub
		System.out.println("Creating Bus Company!");
		BusLine busLineCompany = new BusLine();
		//Tworzenie testowych linii oraz dodawanie segment�w
		System.out.println("Creating Bus Line 'a'!");
		busLineCompany.addBusLine("a",1,1,2,6);
		busLineCompany.addLineSegment("a",new LineSegment(new Position2D(1,1), new Position2D(7,7)));
		busLineCompany.addLineSegment("a",new LineSegment(new Position2D(7,1), new Position2D(2,6)));
		busLineCompany.addLineSegment("a",new LineSegment(new Position2D(7,7), new Position2D(7,1)));
		System.out.println("Creating Bus Line 'b'!");
		busLineCompany.addBusLine("b",4,7,7,7);
		busLineCompany.addLineSegment("b",new LineSegment(new Position2D(4,7), new Position2D(7,7)));
		System.out.println("Creating Bus Line 'c'!");
		busLineCompany.addBusLine("c",1,1,4,2);
		busLineCompany.addLineSegment("c",new LineSegment(new Position2D(8,4), new Position2D(8,2)));
		busLineCompany.addLineSegment("c",new LineSegment(new Position2D(1,4), new Position2D(4,4)));
		busLineCompany.addLineSegment("c",new LineSegment(new Position2D(8,2), new Position2D(4,2)));
		busLineCompany.addLineSegment("c",new LineSegment(new Position2D(1,1), new Position2D(1,4)));
		busLineCompany.addLineSegment("c",new LineSegment(new Position2D(4,4), new Position2D(8,4)));
		
		//Wyszukiwanie skrzy�owa�
		busLineCompany.findIntersections();
		
		//Przedstawianie wynik�w
		System.out.println("Get lines: ");
		var lines=busLineCompany.getLines();
		System.out.println(lines.toString());
		Map<String, List<Position>> expLines = new HashMap<>();
		expLines.put("a", List.of(
                new Position2D(1, 1),
                new Position2D(2, 2),
                new Position2D(3, 3),
                new Position2D(4, 4),
                new Position2D(5, 5),
                new Position2D(6, 6),
                new Position2D(7, 7),
                new Position2D(7, 6),
                new Position2D(7, 5),
                new Position2D(7, 4),
                new Position2D(7, 3),
                new Position2D(7, 2),
                new Position2D(7, 1),
                new Position2D(6, 2),
                new Position2D(5, 3),
                new Position2D(4, 4),
                new Position2D(3, 5),
                new Position2D(2, 6)
        ));
        expLines.put("c", List.of(
                new Position2D(1, 1),
                new Position2D(1, 2),
                new Position2D(1, 3),
                new Position2D(1, 4),
                new Position2D(2, 4),
                new Position2D(3, 4),
                new Position2D(4, 4),
                new Position2D(5, 4),
                new Position2D(6, 4),
                new Position2D(7, 4),
                new Position2D(8, 4),
                new Position2D(8, 3),
                new Position2D(8, 2),
                new Position2D(7, 2),
                new Position2D(6, 2),
                new Position2D(5, 2),
                new Position2D(4, 2)
        ));
        System.out.println(expLines.toString());
        System.out.println(expLines.equals(lines));
        
		System.out.println("Get intersection positions: ");
		var intersectionPositions=busLineCompany.getIntersectionPositions();
		System.out.println(intersectionPositions.toString());
		Map<String, List<Position>> expPositions = new HashMap<>();
		expPositions.put("a",List.of(new Position2D(4, 4), new Position2D(7, 4), new Position2D(7, 2), new Position2D(4, 4)));
        expPositions.put("c", List.of(new Position2D(7, 4), new Position2D(7, 2)));
        System.out.println(expPositions.toString());
        System.out.println(expPositions.equals(intersectionPositions));
        
		System.out.println("Get intersection with lines: ");
		var intersectionsWithLines=busLineCompany.getIntersectionsWithLines();
		System.out.println(intersectionsWithLines.toString());
		Map<String, List<String>> expInter = new HashMap<String, List<String>>();
		expInter.put("a", List.of("a", "c", "c", "a"));
        expInter.put("c", List.of("a", "a"));
        System.out.println(expInter.toString());
        System.out.println(expInter.equals(intersectionsWithLines));
		
		System.out.println("Get intersection of lines pair: ");
		var intersectionOfLinesPair = busLineCompany.getIntersectionOfLinesPair();
		System.out.println(intersectionOfLinesPair.toString());
		Map<BusLineInterface.LinesPair,Set<Position>> expPair = new HashMap<>();
		expPair.put(new BusLine.LinesPair("b", "a"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("c", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "a"), new HashSet<>(List.of(new Position2D(4, 4))));
        expPair.put(new BusLine.LinesPair("b", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("c", "c"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "b"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("b", "c"), new HashSet<>(List.of()));
        expPair.put(new BusLine.LinesPair("a", "c"), new HashSet<>(List.of(new Position2D(7, 2), new Position2D(7, 4))));
        expPair.put(new BusLine.LinesPair("c", "a"), new HashSet<>(List.of(new Position2D(7, 2), new Position2D(7, 4))));
        System.out.println(expPair.toString());
        System.out.println(expPair.equals(intersectionOfLinesPair));
	}
}
