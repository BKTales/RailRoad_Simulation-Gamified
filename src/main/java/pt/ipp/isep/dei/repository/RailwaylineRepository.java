package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.rails.RailType;
import pt.ipp.isep.dei.domain.rails.RailwayLine;
import pt.ipp.isep.dei.domain.rails.TrackType;
import pt.ipp.isep.dei.domain.station.StationType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The {@code RailwaylineRepository} class manages a collection of {@link RailwayLine} objects.
 */
public class RailwaylineRepository implements Serializable {
      private static final long serialVersionUID = 1L;
      private ArrayList<RailwayLine> rails;

      /**
       * Constructs an empty {@code RailwaylineRepository}.
       */
      public RailwaylineRepository() {
            rails = new ArrayList<>();
      }
      //========== Manipulation functions ==========//

      /**
       * Deletes the railway line at index
       *
       * @param index the index of the railway line to delete
       * @throws IndexOutOfBoundsException if the index is out of range
       */
      public void deleteRailwayLine(int index) {
            if (index < 0 || index > rails.size()) {
                  throw new IndexOutOfBoundsException("Index out of bounds");
            }
            rails.remove(index);
      }

      /**
       * Deletes the specified {@link RailwayLine} from the repository.
       *
       * @param rl the railway line to delete
       */
      public void deleteRailwayLine(RailwayLine rl) {
            rails.remove(rl);
      }

      //========== Gets functions ==========//

      /**
       * Returns a list of available {@link RailType} that have a start year less than the current year.
       *
       * @param currentYear the year used to filter available rail types
       * @return an {@link ArrayList} of available {@link RailType}s
       */
      public String getListOfAvailableRailwayLineTypes(int currentYear) {
            StringBuilder s = new StringBuilder();
            int index = 0;
            for (int i = 0; i < (RailType.values().length); i++) {
                  if (currentYear >=RailType.values()[i].startYear){
                        RailType railwayTypes = RailType.values()[i];
                        s.append(" [").append(index).append("] - ").append(railwayTypes.toString()).append("\n");
                        index++;
                  }
            }

            return s.toString();
      }

      /**
       * Returns a string listing all possible railway track types with their indices.
       *
       * @return a formatted string listing track types
       */
      public String getListOfRailwayTrackTypes() {
            StringBuilder s = new StringBuilder();

            for (int i = 0; i < (TrackType.values().length); i++) {
                  TrackType railwayTrackTypes = TrackType.values()[i];
                  s.append(" [").append(i).append("] - ").append(railwayTrackTypes.toString()).append("\n");
            }

            return (s.toString());
      }


      /**
       * Retrieves the list of all railway lines in the repository.
       *
       * @return an {@link ArrayList} of {@link RailwayLine} objects
       */
      public ArrayList<RailwayLine> getRails() {
            return rails;
      }

      //========== Utils functions ==========//

      /**
       * Builds and returns a string listing all railway lines in the repository with an index.
       *
       * @return  string list of railwaylines
       */
      public String listRailwayLines() {
            String s = "";
            int i = 0;
            for (RailwayLine tmp : rails) {
                  s += "[" + i + "] " + tmp.toString();
                  i++;
            }
            return s;
      }

      /**
       * Checks if a railway line already exists between two given stations.
       *
       * @param station1 the first station
       * @param station2 the second station
       * @return true if a railway line exists between the two stations, false otherwise
       */
      public boolean alreadyExists(StationType station1, StationType station2) {
            if (station1 == null || station2 == null) {
                  return false;
            }
            for (RailwayLine tmp : rails) {
                  if (tmp.getStation1().equals(station1) && tmp.getStation2().equals(station2) || tmp.getStation1().equals(station2) && tmp.getStation2().equals(station1)) {
                        return true;
                  }
            }
            return false;
      }



      /**
       * Retrieves the railway line connecting two stations, if it exists.
       *
       * @param a the first station
       * @param b the second station
       * @return the {@link RailwayLine} connecting the two stations or null if none exists
       */
      public RailwayLine getRailwayLineBetween(StationType a, StationType b) {
            for (RailwayLine line : rails) {
                  if ((line.getStation1().equals(a) && line.getStation2().equals(b)) ||
                          (line.getStation1().equals(b) && line.getStation2().equals(a))) {
                        return line;
                  }
            }
            return null;
      }

      public void addRailwayLine(RailwayLine rl) {
            rails.add(rl);
      }

      public void clean() {
            rails.clear();
      }

}
