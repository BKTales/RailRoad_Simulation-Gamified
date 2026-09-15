package pt.ipp.isep.dei.domain;

public interface Generatable {

    String updateProduction(long currentTime);

    boolean removeFromProduction(int quantity);
}
