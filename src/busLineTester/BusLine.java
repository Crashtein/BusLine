package busLineTester;

import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

class BusLine implements BusLineInterface {

	static class LinesPair implements BusLineInterface.LinesPair {
		private final String firstLineName;
		private final String secondLineName;

		public LinesPair(String firstLineName, String secondLineName) {
			this.firstLineName = firstLineName;
			this.secondLineName = secondLineName;
		}

		@Override
		public String getFirstLineName() {
			return firstLineName;
		}

		@Override
		public String getSecondLineName() {
			return secondLineName;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LinesPair other = (LinesPair) obj;
			return (firstLineName == other.firstLineName && secondLineName == other.secondLineName);
		}

		@Override
		public String toString() {
			return "LinePair: [" + firstLineName + ", " + secondLineName + "]";
		}

		@Override
		public int hashCode() {
			return Objects.hash(firstLineName, secondLineName);

		}
	}

	public class CBusLine {
		private final String busLineName;
		private final Position firstPoint;
		private final Position lastPoint;
		private List<LineSegment> segments = new LinkedList<LineSegment>();
		private List<Position> positionList;

		CBusLine(String busLineName, Position firstPoint, Position lastPoint) {
			this.busLineName = busLineName;
			this.firstPoint = firstPoint;
			this.lastPoint = lastPoint;
		}

		String getBusLineName() {
			return busLineName;
		}

		Position getFirstPoint() {
			return firstPoint;
		}

		Position getLastPoint() {
			return lastPoint;
		}

		List<LineSegment> getSegments() {
			return segments;
		}

		/**
		 * Dodaje LineSegment do listy segmentow obiektu linii autobusowej Brak
		 * weryfikacji skutkow dodania kolejnego segmentu Odpowiedzialnosc za poprawne
		 * dodanie segmentow spoczywa na uzytkowniku Dopuszczalne tylko segmenty
		 * (odcinki), ktore sa poziome, pionowe i skosne (co 45 stopni) Skosne segmenty,
		 * ktore nie spelniaja warunkow nie byly uwzgledniane
		 * 
		 * @return
		 */
		void addSegment(LineSegment segment) {
			segments.add(segment);
		}

		void addSegment(int x1, int y1, int x2, int y2) {
			segments.add(new LineSegment(new Position2D(x1, y1), new Position2D(x2, y2)));
		}

		/**
		 * Metoda przygotowuje liste punktow na podstawie przypisanych segmentow
		 * 
		 * @return Zwraca liste z unikalnymi punktami linii autobusowej
		 */
		private List<Position> getPositionList() {
			sortSegments();
			positionList = new LinkedList<Position>();
			positionList.add(firstPoint);
			for (int i = 0; i < segments.size(); i++) {
				Position pos1 = segments.get(i).getFirstPosition();
				Position pos2 = segments.get(i).getLastPosition();
				int pos1Col = pos1.getCol();
				int pos2Col = pos2.getCol();
				int pos1Row = pos1.getRow();
				int pos2Row = pos2.getRow();
				int colSign = (int) Math.signum(pos2Col - pos1Col);
				int rowSign = (int) Math.signum(pos2Row - pos1Row);
				int posCol = pos1Col;
				int posRow = pos1Row;
				Position pos;
				// UWAGA: zapewnienie segmentu trasy, ktorego odcinek na 2 wymiarowej
				// plaszczynie
				// jest pod katem innym niz wielokrotnosc 45stopnii moze spowodowac tutaj
				// nieskooczona petle!!!
				do {
					posCol += colSign;
					posRow += rowSign;
					pos = new Position2D(posCol, posRow);
					positionList.add(pos);
				} while (!pos2.equals(pos));
			}
			return positionList;
		}

		private void sortSegments() {
			var sortedSegments = new LinkedList<LineSegment>();
			LineSegment lastAdded = null;
			for (int i = 0; i < segments.size(); i++) {
				var seg = segments.get(i);
				if (firstPoint.equals(seg.getFirstPosition())) {
					sortedSegments.add(seg);
					lastAdded = seg;
					segments.remove(i);
					break;
				}
			}
			while (segments.size() > 0) {
				for (int i = 0; i < segments.size(); i++) {
					var seg = segments.get(i);
					if (lastAdded.getLastPosition().equals(seg.getFirstPosition())) {
						sortedSegments.add(seg);
						lastAdded = seg;
						segments.remove(i);
						break;
					}
				}
			}
			segments = sortedSegments;
		}
	}

	// Mapa z obiektami linii
	private Map<String, CBusLine> busLines = new HashMap<String, CBusLine>();
	// Mapy z wynikami (skrzyzowaniami)
	private Map<String, List<Position>> lines;
	private Map<String, List<Position>> intersectionPositions;
	private Map<String, List<String>> intersectionsWithLines;
	private Map<BusLineInterface.LinesPair, Set<Position>> intersectionOfLinesPair;

	/**
	 * Metoda dodaje linie autobusowa o podanej nazwie. Wraz z nazwa linii
	 * przekazywane sa informacje o pierwszym i ostatnim punkcie na trasie.
	 * 
	 * @param busLineName nazwa linii
	 * @param firstPoint  pierwszy punkt na trasie
	 * @param lastPoint   ostatni punkt na trasie
	 */
	@Override
	public void addBusLine(String busLineName, Position firstPoint, Position lastPoint) {
		busLines.put(busLineName, new CBusLine(busLineName, firstPoint, lastPoint));
	}

	public void addBusLine(String busLineName, int x1, int y1, int x2, int y2) {
		busLines.put(busLineName, new CBusLine(busLineName, new Position2D(x1, y1), new Position2D(x2, y2)));
	}

	/**
	 * Metoda dodaje odcinek lineSegment do linii autobusowej o nazwie busLineName.
	 * Odcinki nie muszą być dodawane w jakiejkolwiek kolejności. Można z nich
	 * utworzyć całą trasę poprzez uwzględnienie położenia punktów
	 * krańcowych.
	 * 
	 * @param busLineName nazwa linii autobusowej
	 * @param lineSegment odcinek trasy
	 */
	@Override
	public void addLineSegment(String busLineName, LineSegment lineSegment) {
		busLines.get(busLineName).addSegment(lineSegment);
	}

	public void addLineSegment(String busLineName, int x1, int y1, int x2, int y2) {
		busLines.get(busLineName).addSegment(x1, y1, x2, y2);
	}

	/**
	 * Metoda zleca rozpoczęcie poszukiwania skrzyżowań.
	 */
	@Override
	public void findIntersections() {
		lines = new HashMap<String, List<Position>>();
		// Pozyskanie mapy z listami punktow, mapa moze jeszcze zawierac linie, ktore
		// nie maja skrzyoowan
		Set<String> names = busLines.keySet();
		for (String name : names) {
			lines.put(name, busLines.get(name).getPositionList());
		}
//		Set<String> namesSet = busLines.keySet();
//		List<String> names = new ArrayList<>(namesSet);
//		for (int i=names.size()-1;i>=0;i--) {
//			lines.put(names.get(i), busLines.get(names.get(i)).getPositionList());
//		}
		// Inicjalizacja pozostalych map
		intersectionPositions = new HashMap<String, List<Position>>();
		intersectionsWithLines = new HashMap<String, List<String>>();
		intersectionOfLinesPair = new HashMap<BusLineInterface.LinesPair, Set<Position>>();
		Set<String> lineNames = lines.keySet();
		for (String lineName1 : lineNames) {
			List<Position> line1 = lines.get(lineName1);
			for (Position pos1 : line1) {
				for (String lineName2 : lineNames) {
					List<Position> line2 = lines.get(lineName2);
					LinesPair linePair = new LinesPair(lineName1, lineName2);
					addIntersectionOfLinesPair(linePair);
					for (Position pos2 : line2) {
						if (pos1.equals(pos2)) {
							if (checkIntersections(line1, line2, pos1)) {
								addIntersectionPosition(lineName1, pos1);
								addIntersectionsWithLines(lineName1, lineName2);
								addIntersectionOfLinesPair(linePair, pos1);
								break;
							}
						}
					}
				}
			}
		}
		// Usuniecie linii, ktore nie maja zadnych skrzyzowan
		deleteLinesWithNoIntersection();
		// Usuniucie duplikataw z listy skrzyzowan
		// deleteReplicasIntersection();
	}

	/**
	 * Sprawdza czy na przekazanej liscie istnieje obiekt Position o tych samych
	 * koordynatach
	 * 
	 * @return true jesli znaleziono taki sam obiekt, false jezli nie znaleziono
	 */
	private boolean checkIfPositionInList(Position pos, List<Position> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(pos)) {
				// znaleziono taka sama pozycje
				return true;
			}
		}
		return false;
	}

	/**
	 * Sprawdza czy na przekazanych liniach istnieje skrzyzowanie w podanym punkcie
	 * 
	 * @return true jezli warunki skrzyzowania spelnione, false jezli punkt wspolny
	 *         nie spelnia warunkow bycia skrzyzowaniem
	 */
	private boolean checkIntersections(List<Position> line1, List<Position> line2, Position pos) {
		int posCol = pos.getCol();
		int posRow = pos.getRow();

		int lineLength1 = line1.size();
		int lineLength2 = line2.size();

		for (int k = 0; k < lineLength1 - 2; k++) {
			for (int l = 0; l < lineLength2 - 2; l++) {
				if (pos.equals(line1.get(k + 1)) && pos.equals(line2.get(l + 1)) && !(line1.equals(line2) && k == l)) {
					Position pos1_1 = line1.get(k);
					Position pos1_2 = line1.get(k + 2);
					Position pos2_1 = line2.get(l);
					Position pos2_2 = line2.get(l + 2);

					int diff1_col = Math.abs(pos1_1.getCol() - pos1_2.getCol());
					int diff1_row = Math.abs(pos1_1.getRow() - pos1_2.getRow());
					int diff2_col = Math.abs(pos2_1.getCol() - pos2_2.getCol());
					int diff2_row = Math.abs(pos2_1.getRow() - pos2_2.getRow());
					
					if (diff1_col  == diff2_row && diff1_row  == diff2_col) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Metoda zwraca mapę, której kluczem jest nazwa linii autobusowej zaś
	 * wartością lista położeń wszystkich punktów należących do danej linii.
	 * Mapa nie zawiera wśród kluczy nazw linii, które nie mają żadnego
	 * skrzyżowania.
	 * 
	 * @return mapa z przebiegiem tras linii autobusowych
	 */
	@Override
	public Map<String, List<Position>> getLines() {
		return lines;
	}

	/**
	 * Metoda zwraca mapę, której kluczem jest nazwa linii autobusowej a
	 * wartością lista kolejnych skrzyżowań na trasie linii. Mapa nie zawiera
	 * wśród kluczy nazw linii, które nie mają żadnego skrzyżowania.
	 * 
	 * @return mapa skrzyżowań dla poszczególnych linii.
	 */
	@Override
	public Map<String, List<Position>> getIntersectionPositions() {
		return intersectionPositions;
	}

	/**
	 * Metoda zwraca mapę, której kluczem jest nazwa linii autobusowej a
	 * wartością lista nazw kolejnych linii, z którymi linia ta ma skrzyżowania.
	 * Zbiór kluczy nie zawiera linii, które nie mają skrzyżowania.
	 * 
	 * @return mapa skrzyżowań pomiędzy liniami
	 */
	@Override
	public Map<String, List<String>> getIntersectionsWithLines() {
		return intersectionsWithLines;
	}

	/**
	 * Metoda zwraca mapę, której kluczem jest para nazw linii a wartością
	 * zbiór położeń, w których para linii ma skrzyżowania. Jeśli linie nie
	 * mają żadnego skrzyżowania, to mapa zawiera pusty zbiór pozycji
	 * skrzyżowań
	 * 
	 * @return mapa skrzyżowań dla par linii autobusowych
	 */
	@Override
	public Map<BusLineInterface.LinesPair, Set<Position>> getIntersectionOfLinesPair() {
		return intersectionOfLinesPair;
	}

	/**
	 * Metoda dodaje do mapy linii pozycje skrzyzowan
	 * 
	 * @return
	 */
	private void addIntersectionPosition(String lineName, Position pos) {
		List<Position> _intersections = intersectionPositions.get(lineName);
		if (_intersections == null) { // Brak listy z skrzyzowaniami dla danej linii
			var list = new LinkedList<Position>();
			list.add(pos);
			intersectionPositions.put(lineName, list);
		} else {
			_intersections.add(pos);
			intersectionPositions.replace(lineName, _intersections);
		}
	}

	/**
	 * Metoda dodaje aktualizuje mape o nazwy linii do listy linii, z ktorymi ma
	 * skrzyzowanie linia klucza
	 * 
	 * @return mapa skrzyzowan pomiedzy liniami
	 */
	private void addIntersectionsWithLines(String lineName1, String lineName2) {
		List<String> _intersectionsWithLine1 = intersectionsWithLines.get(lineName1);
		if (_intersectionsWithLine1 == null) { // Brak listy z nazwami linii
			_intersectionsWithLine1 = new LinkedList<String>();
			_intersectionsWithLine1.add(lineName2);
			intersectionsWithLines.put(lineName1, _intersectionsWithLine1);
		} else { // dodanie do istniejacej listy kolejnego elementu
			_intersectionsWithLine1.add(lineName2);
			intersectionsWithLines.replace(lineName1, _intersectionsWithLine1);
		}
	}

	/**
	 * Metoda dodaje pozycje skrzyzowan dla pary linii
	 * 
	 * @return
	 */
	private void addIntersectionOfLinesPair(LinesPair linePair, Position pos) {
		var set = intersectionOfLinesPair.get(linePair);
		if (set == null || set.isEmpty()) {
			set = new HashSet<>();
		}
		set.add(pos);
		intersectionOfLinesPair.put(linePair, set);
	}

	private void addIntersectionOfLinesPair(LinesPair linePair) {
		var set = intersectionOfLinesPair.get(linePair);
		if (set == null || set.isEmpty()) {
			set = new HashSet<>();
		}
		intersectionOfLinesPair.put(linePair, set);
	}

	/**
	 * Metoda usuwa z mapy lines linie, ktore nie maja zadnych skrzyzowan
	 * 
	 * @return
	 */
	private void deleteLinesWithNoIntersection() {
		Iterator<Map.Entry<String, List<Position>>> iterator = lines.entrySet().iterator();
		List<String> toDelete = new LinkedList<String>();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Position>> entry = iterator.next();
			if (intersectionPositions.get(entry.getKey()) == null
					|| intersectionPositions.get(entry.getKey()).isEmpty()) {
				toDelete.add(entry.getKey());
			}
		}
		for (String keyToDelete : toDelete) {
			lines.remove(keyToDelete);
			intersectionPositions.remove(keyToDelete);
		}
	}

}