package busLineTester;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class busLineTester {

	public static void main(String[] args) {
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
