package busLineTester;

import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public  class BusLine implements BusLineInterface {
	public static class LinesPair implements BusLineInterface.LinesPair {
		private final String firstLineName;
		private final String secondLineName;
		
		public LinesPair(String firstLineName, String secondLineName) {
//			if(firstLineName.compareTo(secondLineName)<0) {
//				this.firstLineName = firstLineName;
//				this.secondLineName = secondLineName;
//			} else {
//				this.firstLineName = secondLineName;
//				this.secondLineName = firstLineName;
//			}
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
		CBusLine(String busLineName, Position firstPoint, Position lastPoint){
			this.busLineName = busLineName;
			this.firstPoint = firstPoint;
			this.lastPoint = lastPoint;
		}
		String getBusLineName(){
			return busLineName;
		}
		Position getFirstPoint() {
			return firstPoint;
		}
		Position getLastPoint() {
			return lastPoint;
		}
		List<LineSegment> getSegments(){
			return segments;
		}
		/**
		 * Dodaje LineSegment do listy segment�w obiektu linii autobusowej
		 * Brak weryfikacji skutk�w dodania kolejnego segmentu
		 * Odpowiedzialno�� za poprawne dodanie segment�w spoczywa na u�ytkowniku
		 * Dopuszczalne tylko segmenty (odcinki), kt�re s� poziome, pionowe i sko�ne (co 45 stopni)
		 * Sko�ne segmenty, kt�re nie spe�niaj� warunk�w nie by�y uwzgl�dniane
		 * 
		 * @return
		 */
		void addSegment(LineSegment segment) {
			segments.add(segment);
		}
		void addSegment(int x1,int y1, int x2, int y2) {
			segments.add(new LineSegment(new Position2D(x1,y1), new Position2D(x2,y2)));
		}
		/**
		 * Metoda przygotowuje list� punkt�w na podstawie przypisanych segment�w
		 * 
		 * @return Zwraca list� z unikalnymi punktami linii autobusowej
		 */
		private List<Position> getPositionList(){
			positionList = new LinkedList<Position>();
			int i = 0;
	        while (i < segments.size()) {
	        	Position pos1=segments.get(i).getFirstPosition();
	        	Position pos2=segments.get(i).getLastPosition();
	        	int pos1Col=pos1.getCol();
	        	int pos2Col=pos2.getCol();
	        	int pos1Row=pos1.getRow();
	        	int pos2Row=pos2.getRow();
	        	int colSign=(int)Math.signum(pos2Col-pos1Col);
	        	int rowSign=(int)Math.signum(pos2Row-pos1Row);
	        	int posCol = pos1Col;
	        	int posRow = pos1Row;
	        	Position pos;
	        	//UWAGA: zapewnienie segmentu trasy, kt�rego odcinek na 2 wymiarowej p�aszczynie 
	        	//jest pod k�tem innym ni� wielokrotno�� 45stopnii mo�e spowodowa� tutaj niesko�czon� p�tl�!!! 
	        	do {
	        		pos = new Position2D(posCol,posRow);
	        		//Sprawdzanie, czy punkt o takich samych wsp�rz�dnych jest ju� na li�cie
	        		boolean posDuplicated=false;
	        		int k=0;
	        		while(k!=positionList.size() && !posDuplicated){
	        			posDuplicated=positionList.get(k).equals(pos);
	        			k++;
	    	        }
	        		//Nie dodawaj duplikatu
	        		if(!posDuplicated) {
	        			positionList.add(pos);
	        		}
	        		posCol+=colSign;
	        		posRow+=rowSign;
	        	} while(!pos2.equals(pos));
	            i++;
	        }
			return positionList;
		}
	}
	//Mapa z obiektami linii
	private Map<String, CBusLine> busLines = new HashMap<String, CBusLine>();
	//Mapy z wynikami (skrzy�owaniami)
	private Map<String, List<Position>> lines;
	private Map<String, List<Position>> intersectionPositions;
	private Map<String, List<String>> intersectionsWithLines;
	private Map<BusLineInterface.LinesPair, Set<Position>> intersectionOfLinesPair;
	
	/**
	 * Metoda dodaje lini� autobusow� o podanej nazwie. Wraz z nazw� linii
	 * przekazywane s� informacje o pierwszym i ostatnim punkcie na trasie.
	 * 
	 * @param busLineName nazwa linii
	 * @param firstPoint  pierwszy punkt na trasie
	 * @param lastPoint   ostatni punkt na trasie
	 */
	@Override
	public void addBusLine(String busLineName, Position firstPoint, Position lastPoint) {
		busLines.put(busLineName, new CBusLine(busLineName, firstPoint, lastPoint));
	}

	public void addBusLine(String busLineName,  int x1,int y1, int x2, int y2) {
		busLines.put(busLineName, new CBusLine(busLineName, new Position2D(x1,y1), new Position2D(x2,y2)));
	}
	/**
	 * Metoda dodaje odcinek lineSegment do linii autobusowej o nazwie busLineName.
	 * Odcinki nie musz� by� dodawane w jakiejkolwiek kolejno�ci. Mo�na z nich
	 * utworzy� ca�� tras� poprzez uwzgl�dnienie po��czenia punkt�w kra�cowych.
	 * 
	 * @param busLineName nazwa linii autobusowej
	 * @param lineSegment odcinek trasy
	 */
	@Override
	public void addLineSegment(String busLineName, LineSegment lineSegment) {
		busLines.get(busLineName).addSegment(lineSegment);
	}
	
	public void addLineSegment(String busLineName, int x1,int y1, int x2, int y2) {
		busLines.get(busLineName).addSegment(x1,y1,x2,y2);
	}

	/**
	 * Metoda zleca rozpocz�cie poszukiwania skrzy�owa�
	 */
	@Override
	public void findIntersections() {
		lines = new HashMap<String, List<Position>>();
		//Pozyskanie mapy z listami punkt�w, mapa mo�e jeszcze zawiera� linie, kt�re nie maj� skrzy�owa�
		for (Map.Entry<String, CBusLine> buslinesEntry : busLines.entrySet()) {
			lines.put(buslinesEntry.getKey(), buslinesEntry.getValue().getPositionList());
	    }
		//Inicjalizacja pozosta�ych map
		intersectionPositions = new HashMap<String, List<Position>>();
		intersectionsWithLines = new HashMap<String, List<String>>();
		intersectionOfLinesPair = new HashMap<BusLineInterface.LinesPair, Set<Position>>();
		
		//Dwa iteratory po obiektach mapy do
		Iterator<Map.Entry<String, List<Position>>> linesIterator1 = lines.entrySet().iterator();
		Iterator<Map.Entry<String, List<Position>>> linesIterator2;
		int mapIterator=0;	//Ustawi� na 1 aby nie sprawdza� skrzy�owa� w�asnych
	    while (linesIterator1.hasNext()) {
	    	Map.Entry<String, List<Position>> linesEntry1 = linesIterator1.next();
    		String lineName1 = linesEntry1.getKey();
    		
    		linesIterator2 = lines.entrySet().iterator();
    		for(int i=0;i<mapIterator;i++) {
    			linesIterator2.next();
    		}
    		mapIterator++;
	    	while (linesIterator2.hasNext()) {
	    		Map.Entry<String, List<Position>> linesEntry2 = linesIterator2.next();
	    		String lineName2 = linesEntry2.getKey();
	    		
	    		List<Position> line1=linesEntry1.getValue();
	    		List<Position> line2=linesEntry2.getValue();
	    		LinesPair linePair = new LinesPair(lineName1, lineName2);
	    		//Sprawdzana para linii autobusowych
	    		List<Position> intersections = findIntersectionPositions(line1,line2);
	    		//Dodanie do mapy Nazwa Linii::Lista pozycji skrzy�owa�  znalezione nowe skrzy�owania dla obu linii
        		addIntersectionPositions(lineName1, intersections);
        		addIntersectionPositions(lineName2, intersections);
        		if(intersections.size()>0) {
        			//Dodanie do mapy Para Linii::Set Pozycji skrzy�owa�
            		addIntersectionOfLinesPair(linePair, intersections);
            		//Dodanie do mapy Nazwa Linii::Lista Nazw Linii nazw linii, z kt�rymi s� skrzy�owania
            		addIntersectionsWithLines(lineName1, lineName2);
            		addIntersectionsWithLines(lineName2, lineName1);
        		}
	    	}
	    }
	    //Usuni�cie linii, kt�re nie maj� �adnych skrzy�owa�
	    deleteLinesWithNoIntersection();
	    //Usuni�cie duplikat�w z listy skrzy�owa�
	    deleteReplicasIntersection();
	}
	/**
	 * Metoda poszukuje pozycji skrzy�owa� na przekazanych listach pozycji dw�ch linii autobusowych
	 * 
	 * @return Lista z pozycjami uznanymi za skrzy�owania
	 */
	private List<Position> findIntersectionPositions(List<Position> line1,List<Position> line2){
		List<Position> intersections = new LinkedList<Position>();
		//Poszukiwanie punkt�w wsp�lnych
		for(int i=0;i<line1.size();i++){
			for(int j=0;j<line2.size();j++){
				Position pos1 = line1.get(i);
				Position pos2 = line2.get(j);
				if(pos1.equals(pos2)) {
					//znaleziono takie same punkty w obu liniach, sprawdzanie warunku skrzy�owania pod k�tem prostym
					if(checkIntersections(line1,line2,pos1)) {
						if(!intersections.contains(pos1)) {
							intersections.add(pos1);
						}
					}
				}
	        }
        }
		return intersections;
	}
	
	/**
	 * Sprawdza czy na przekazanej li�cie istnieje obiekt Position o tych samych koordynatach
	 * 
	 * @return true je�li znaleziono taki sam obiekt, false je�li nie znaleziono
	 */
	private boolean checkIfPositionInList(Position pos,List<Position> list) {
		for(int i=0;i<list.size();i++){
			if(list.get(i).equals(pos)) {
				//znaleziono tak� sam� pozycj�
				return true;
			}
        }
		return false;
	}
	/**
	 * Sprawdza czy na przekazanych liniach istnieje skrzy�owanie w podanym punkcie
	 * 
	 * @return true je�li warunki skrzy�owania spe�nione, false je�li punkt wsp�lny nie spe�nia warunk�w bycia skrzy�owaniem
	 */
	private boolean checkIntersections(List<Position> line1,List<Position> line2, Position pos) {
		int posCol = pos.getCol();
		int posRow = pos.getRow();
		for(int i=0;i<=1;i++) {
			for(int j=0;j<=1;j++) {
				if((i!=0 || j!=0) && checkIfPositionInList(new Position2D(posCol+i,posRow+j),line1)
						&& checkIfPositionInList(new Position2D(posCol-i,posRow-j),line1)
						&& checkIfPositionInList(new Position2D(posCol+j,posRow-i),line2)
						&& checkIfPositionInList(new Position2D(posCol-j,posRow+i),line2)) {
					//znaleziono skrzy�owanie
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metoda zwraca map�, kt�rej kluczem jest nazwa linii autobusowej za� warto�ci�
	 * lista po�o�e� wszystkich punkt�w nale��cych do danej linii. Mapa nie zawiera
	 * warto�ci kluczy nazw linii, kt�re nie maj� �adnego skrzy�owania.
	 * 
	 * @return mapa z przebiegiem tras linii autobusowych
	 */
	@Override
	public Map<String, List<Position>> getLines(){
		return lines;
	}

	/**
	 * Metoda zwraca mape, kt�rej kluczem jest nazwa linii autobusowej a warto�ci�
	 * lista kolejnych skrzy�owa� na trasie linii. Mapa nie zawiera warto�ci kluczy
	 * nazw linii, kt�re nie maj� �adnego skrzy�owania.
	 * 
	 * @return mapa skrzy�owa� dla poszczeg�lnych linii.
	 */
	@Override
	public Map<String, List<Position>> getIntersectionPositions(){
		return intersectionPositions;
	}

	/**
	 * Metoda zwraca map�, kt�rej kluczem jest nazwa linii autobusowej a waro�ci�
	 * lista nazw kolejnych linii, z kt�rymi linia ta ma skrzy�owania. Zbi�r kluczy
	 * nie zawiera linii, kt�re nie maj� skrzy�owania.
	 * 
	 * @return mapa skrzy�owa� pomi�dzy liniami
	 */
	@Override
	public Map<String, List<String>> getIntersectionsWithLines(){
		return intersectionsWithLines;
	}

	/**
	 * Metoda zwraca map�, kt�rej kluczem jest para nazw linii a warto�ci� zbi�r
	 * po�o�e�, w kt�rych para linii ma skrzy�owania. Je�eli linie nie maj� �adnego
	 * skrzy�owania, to mapa zawiera pusty zbi�r pozycji skrzy�owa�
	 * 
	 * @return mapa skrzy�owa� dla par linii autobusowych
	 */
	@Override
	public Map<BusLineInterface.LinesPair, Set<Position>> getIntersectionOfLinesPair(){
		return intersectionOfLinesPair;
	}
	
	/**
	 * Metoda dodaje do mapy linii pozycje skrzy�owa�
	 * 
	 * @return
	 */
	private void addIntersectionPositions(String lineName,List<Position> intersections){
		List<Position> _intersections = intersectionPositions.get(lineName);
		if(_intersections==null) {	//Brak listy z skrzy�owaniami dla danej linii
			intersectionPositions.put(lineName,intersections);
		} else {
			for(Position pos : intersections) {
	            if(!_intersections.contains(pos));	//sprawdzenie, czy lista nie zawiera ju� takiej pozycji
	            	_intersections.add(pos);
			}
			intersectionPositions.replace(lineName,_intersections);
		}
	}

	/**
	 * Metoda dodaje aktualizuje map� o nazw� linii do listy linii, z kt�rymi ma skrzy�owanie linia klucza
	 * 
	 * @return mapa skrzy�owa� pomi�dzy liniami
	 */
	private void addIntersectionsWithLines(String lineName1, String lineName2){
		List<String> _intersectionsWithLine1 = intersectionsWithLines.get(lineName1);
		if(_intersectionsWithLine1==null) {						//Brak listy z nazwami linii
			_intersectionsWithLine1= new LinkedList<String>();
			_intersectionsWithLine1.add(lineName2);
			intersectionsWithLines.put(lineName1,_intersectionsWithLine1);
		} else {	//dodanie do istniej�cej listy kolejnego elementu
	        if(!_intersectionsWithLine1.contains(lineName2)) {	//sprawdzenie, czy nie ma ju� takiej linii na li�cie
	        	_intersectionsWithLine1.add(lineName2);
	        	intersectionsWithLines.replace(lineName1,_intersectionsWithLine1);
	        }
		}
	}

	/**
	 * Metoda dodaje pozycje skrzy�owa� dla pary linii
	 * 
	 * @return
	 */
	private void addIntersectionOfLinesPair(LinesPair linePair,List<Position> intersections){
		intersectionOfLinesPair.put(linePair, new HashSet<>(intersections));
	}
	
	/**
	 * Metoda usuwa z mapy lines linie, kt�re nie maj� �adnych skrzy�owa�
	 * 
	 * @return
	 */
	private void deleteLinesWithNoIntersection(){
		Iterator<Map.Entry<String, List<Position>>> iterator = lines.entrySet().iterator();
		List<String> toDelete = new LinkedList<String>();
		while(iterator.hasNext()) {
			Map.Entry<String, List<Position>> entry = iterator.next();
			if(intersectionPositions.get(entry.getKey()).isEmpty()) {
				toDelete.add(entry.getKey());
			}
		}
		for (String keyToDelete : toDelete) {
			lines.remove(keyToDelete);
			intersectionPositions.remove(keyToDelete);
		}
	}
	
	private void deleteReplicasIntersection() {
		Iterator<Map.Entry<String, List<Position>>> iterator = intersectionPositions.entrySet().iterator();
		while(iterator.hasNext()) {
			List<Position> intersectionNoDuplicates = new LinkedList<Position>();
			Map.Entry<String, List<Position>> entry = iterator.next();
			for (Position position : entry.getValue()) {
				if(!intersectionNoDuplicates.contains(position)) {
					intersectionNoDuplicates.add(position);
				}
			}
			intersectionPositions.replace(entry.getKey(), intersectionNoDuplicates);
		}
	}
}
