package busLineTester;

import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

public class CBusLineCompany implements BusLineInterface {
	public class CLinesPair implements LinesPair {
		private final String firstLineName;
		private final String secondLineName;
		
		public CLinesPair(String firstLineName, String secondLineName) {
			if(firstLineName.compareTo(secondLineName)<0) {
				this.firstLineName = firstLineName;
				this.secondLineName = secondLineName;
			} else {
				this.firstLineName = secondLineName;
				this.secondLineName = firstLineName;
			}
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
			CLinesPair other = (CLinesPair) obj;
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
		 * Dodaje LineSegment do listy segmentów obiektu linii autobusowej
		 * Brak weryfikacji skutków dodania kolejnego segmentu
		 * Odpowiedzialnoœæ za poprawne dodanie segmentów spoczywa na u¿ytkowniku
		 * Dopuszczalne tylko segmenty (odcinki), które s¹ poziome, pionowe i skoœne (co 45 stopni)
		 * Skoœne segmenty, które nie spe³niaj¹ warunków nie by³y uwzglêdniane
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
		 * Metoda przygotowuje listê punktów na podstawie przypisanych segmentów
		 * 
		 * @return Zwraca listê z unikalnymi punktami linii autobusowej
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
	        	//UWAGA: zapewnienie segmentu trasy, którego odcinek na 2 wymiarowej p³aszczynie 
	        	//jest pod k¹tem innym ni¿ wielokrotnoœæ 45stopnii mo¿e spowodowaæ tutaj nieskoñczon¹ pêtlê!!! 
	        	do {
	        		pos = new Position2D(posCol,posRow);
	        		//Sprawdzanie, czy punkt o takich samych wspó³rzêdnych jest ju¿ na liœcie
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
	//Mapy z wynikami (skrzy¿owaniami)
	private Map<String, List<Position>> lines;
	private Map<String, List<Position>> intersectionPositions;
	private Map<String, List<String>> intersectionsWithLines;
	private Map<BusLineInterface.LinesPair, Set<Position>> intersectionOfLinesPair;
	
	/**
	 * Metoda dodaje liniê autobusow¹ o podanej nazwie. Wraz z nazw¹ linii
	 * przekazywane s¹ informacje o pierwszym i ostatnim punkcie na trasie.
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
	 * Odcinki nie musz¹ byæ dodawane w jakiejkolwiek kolejnoœci. Mo¿na z nich
	 * utworzyæ ca³¹ trasê poprzez uwzglêdnienie po³¹czenia punktów krañcowych.
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
	 * Metoda zleca rozpoczêcie poszukiwania skrzy¿owañ
	 */
	@Override
	public void findIntersections() {
		lines = new HashMap<String, List<Position>>();
		//Pozyskanie mapy z listami punktów, mapa mo¿e jeszcze zawieraæ linie, które nie maj¹ skrzy¿owañ
		for (Map.Entry<String, CBusLine> buslinesEntry : busLines.entrySet()) {
			lines.put(buslinesEntry.getKey(), buslinesEntry.getValue().getPositionList());
	    }
		//Inicjalizacja pozosta³ych map
		intersectionPositions = new HashMap<String, List<Position>>();
		intersectionsWithLines = new HashMap<String, List<String>>();
		intersectionOfLinesPair = new HashMap<BusLineInterface.LinesPair, Set<Position>>();
		
		//Dwa iteratory po obiektach mapy do
		Iterator<Map.Entry<String, List<Position>>> linesIterator1 = lines.entrySet().iterator();
		Iterator<Map.Entry<String, List<Position>>> linesIterator2;
		int mapIterator=0;	//Ustawiæ na 1 aby nie sprawdzaæ skrzy¿owañ w³asnych
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
	    		CLinesPair linePair = new CLinesPair(lineName1, lineName2);
	    		//Sprawdzana para linii autobusowych
	    		List<Position> intersections = findIntersectionPositions(line1,line2);
	    		//Dodanie do mapy Nazwa Linii::Lista pozycji skrzy¿owañ  znalezione nowe skrzy¿owania dla obu linii
        		addIntersectionPositions(lineName1, intersections);
        		addIntersectionPositions(lineName2, intersections);
        		if(intersections.size()>0) {
        			//Dodanie do mapy Para Linii::Set Pozycji skrzy¿owañ
            		addIntersectionOfLinesPair(linePair, intersections);
            		//Dodanie do mapy Nazwa Linii::Lista Nazw Linii nazw linii, z którymi s¹ skrzy¿owania
            		addIntersectionsWithLines(lineName1, lineName2);
            		addIntersectionsWithLines(lineName2, lineName1);
        		}
	    	}
	    }
	    //Usuniêcie linii, które nie maj¹ ¿adnych skrzy¿owañ
	    deleteLinesWithNoIntersection();
	    //Usuniêcie duplikatów z listy skrzy¿owañ
	    deleteReplicasIntersection();
	}
	/**
	 * Metoda poszukuje pozycji skrzy¿owañ na przekazanych listach pozycji dwóch linii autobusowych
	 * 
	 * @return Lista z pozycjami uznanymi za skrzy¿owania
	 */
	private List<Position> findIntersectionPositions(List<Position> line1,List<Position> line2){
		List<Position> intersections = new LinkedList<Position>();
		//Poszukiwanie punktów wspólnych
		for(int i=0;i<line1.size();i++){
			for(int j=0;j<line2.size();j++){
				Position pos1 = line1.get(i);
				Position pos2 = line2.get(j);
				if(pos1.equals(pos2)) {
					//znaleziono takie same punkty w obu liniach, sprawdzanie warunku skrzy¿owania pod k¹tem prostym
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
	 * Sprawdza czy na przekazanej liœcie istnieje obiekt Position o tych samych koordynatach
	 * 
	 * @return true jeœli znaleziono taki sam obiekt, false jeœli nie znaleziono
	 */
	private boolean checkIfPositionInList(Position pos,List<Position> list) {
		for(int i=0;i<list.size();i++){
			if(list.get(i).equals(pos)) {
				//znaleziono tak¹ sam¹ pozycjê
				return true;
			}
        }
		return false;
	}
	/**
	 * Sprawdza czy na przekazanych liniach istnieje skrzy¿owanie w podanym punkcie
	 * 
	 * @return true jeœli warunki skrzy¿owania spe³nione, false jeœli punkt wspólny nie spe³nia warunków bycia skrzy¿owaniem
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
					//znaleziono skrzy¿owanie
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metoda zwraca mapê, której kluczem jest nazwa linii autobusowej zaœ wartoœci¹
	 * lista po³o¿eñ wszystkich punktów nale¿¹cych do danej linii. Mapa nie zawiera
	 * wartoœci kluczy nazw linii, które nie maj¹ ¿adnego skrzy¿owania.
	 * 
	 * @return mapa z przebiegiem tras linii autobusowych
	 */
	@Override
	public Map<String, List<Position>> getLines(){
		return lines;
	}

	/**
	 * Metoda zwraca mape, której kluczem jest nazwa linii autobusowej a wartoœci¹
	 * lista kolejnych skrzy¿owañ na trasie linii. Mapa nie zawiera wartoœci kluczy
	 * nazw linii, które nie maj¹ ¿adnego skrzy¿owania.
	 * 
	 * @return mapa skrzy¿owañ dla poszczególnych linii.
	 */
	@Override
	public Map<String, List<Position>> getIntersectionPositions(){
		return intersectionPositions;
	}

	/**
	 * Metoda zwraca mapê, której kluczem jest nazwa linii autobusowej a waroœci¹
	 * lista nazw kolejnych linii, z którymi linia ta ma skrzy¿owania. Zbiór kluczy
	 * nie zawiera linii, które nie maj¹ skrzy¿owania.
	 * 
	 * @return mapa skrzy¿owañ pomiêdzy liniami
	 */
	@Override
	public Map<String, List<String>> getIntersectionsWithLines(){
		return intersectionsWithLines;
	}

	/**
	 * Metoda zwraca mapê, której kluczem jest para nazw linii a wartoœci¹ zbiór
	 * po³o¿eñ, w których para linii ma skrzy¿owania. Je¿eli linie nie maj¹ ¿adnego
	 * skrzy¿owania, to mapa zawiera pusty zbiór pozycji skrzy¿owañ
	 * 
	 * @return mapa skrzy¿owañ dla par linii autobusowych
	 */
	@Override
	public Map<LinesPair, Set<Position>> getIntersectionOfLinesPair(){
		return intersectionOfLinesPair;
	}
	
	/**
	 * Metoda dodaje do mapy linii pozycje skrzy¿owañ
	 * 
	 * @return
	 */
	private void addIntersectionPositions(String lineName,List<Position> intersections){
		List<Position> _intersections = intersectionPositions.get(lineName);
		if(_intersections==null) {	//Brak listy z skrzy¿owaniami dla danej linii
			intersectionPositions.put(lineName,intersections);
		} else {
			for(Position pos : intersections) {
	            if(!_intersections.contains(pos));	//sprawdzenie, czy lista nie zawiera ju¿ takiej pozycji
	            	_intersections.add(pos);
			}
			intersectionPositions.replace(lineName,_intersections);
		}
	}

	/**
	 * Metoda dodaje aktualizuje mapê o nazwê linii do listy linii, z którymi ma skrzy¿owanie linia klucza
	 * 
	 * @return mapa skrzy¿owañ pomiêdzy liniami
	 */
	private void addIntersectionsWithLines(String lineName1, String lineName2){
		List<String> _intersectionsWithLine1 = intersectionsWithLines.get(lineName1);
		if(_intersectionsWithLine1==null) {						//Brak listy z nazwami linii
			_intersectionsWithLine1= new LinkedList<String>();
			_intersectionsWithLine1.add(lineName2);
			intersectionsWithLines.put(lineName1,_intersectionsWithLine1);
		} else {	//dodanie do istniej¹cej listy kolejnego elementu
	        if(!_intersectionsWithLine1.contains(lineName2)) {	//sprawdzenie, czy nie ma ju¿ takiej linii na liœcie
	        	_intersectionsWithLine1.add(lineName2);
	        	intersectionsWithLines.replace(lineName1,_intersectionsWithLine1);
	        }
		}
	}

	/**
	 * Metoda dodaje pozycje skrzy¿owañ dla pary linii
	 * 
	 * @return
	 */
	private void addIntersectionOfLinesPair(CLinesPair linePair,List<Position> intersections){
		intersectionOfLinesPair.put(linePair, new HashSet<>(intersections));
	}
	
	/**
	 * Metoda usuwa z mapy lines linie, które nie maj¹ ¿adnych skrzy¿owañ
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
