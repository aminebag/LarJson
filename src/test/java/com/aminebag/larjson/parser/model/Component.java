package com.aminebag.larjson.parser.model;

import java.util.List;

public interface Component {
    String getType();
    Image getImagePattern();
    String getTitle();
    String getSubtitle();
    List<Redline> getRedlineItems();
    List<Tile> getTiles();
}
