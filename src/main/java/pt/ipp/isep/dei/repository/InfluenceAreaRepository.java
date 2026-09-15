package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.position.InfluenceArea;

import java.io.Serializable;
import java.util.ArrayList;

public class InfluenceAreaRepository implements Serializable {
    private static final long serialVersionUID = 1L;
      private ArrayList<InfluenceArea> influenceAreas;

      public InfluenceArea createInfluenceArea(String name, int height, int length) {
          return null;
      }

      public void deleteInfluenceArea(String name) {

      }

      public InfluenceArea getInfluenceArea(String name) {
        return null;
      }

    public ArrayList<InfluenceArea> getInfluenceArea(){ return influenceAreas; }

    public void clean(){
          influenceAreas.clear();
      }
}
