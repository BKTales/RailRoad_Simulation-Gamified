package pt.ipp.isep.dei.utils;

import java.util.List;

public interface DisplayListControllerInterface {
    void initialize( List<String> list);
    void onItemSelected(int index);
}