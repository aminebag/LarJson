package com.aminebag.larjson.jsonmapper.model;

import java.util.List;

public interface Component {
    String getType();
    Image getImagePattern();
    String getTitle();
    String getSubtitle();
    List<Redline> getRedlineItems();
    List<Tile> getTiles();
}
