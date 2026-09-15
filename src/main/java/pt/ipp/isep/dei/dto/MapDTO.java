package pt.ipp.isep.dei.dto;

import pt.ipp.isep.dei.domain.Map;

public class MapDTO {
    public String name;
    public int length;
    public int height;
    public int scale;

    public static MapDTO toDTO(Map map) {
        if (map == null)
            return null;
        MapDTO dto = new MapDTO();
        dto.name = map.getName();
        dto.length = map.getLength();
        dto.height = map.getHeight();
        dto.scale = map.getScale();
        return dto;
    }

    public static Map toEntity(MapDTO dto) {
        if (dto == null)
            return null;
        Map map = new Map(dto.name, dto.length, dto.height, dto.scale);
        return map;
    }
}

