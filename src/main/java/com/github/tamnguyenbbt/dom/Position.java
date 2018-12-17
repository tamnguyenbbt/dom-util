package com.github.tamnguyenbbt.dom;

import java.util.ArrayList;
import java.util.List;

final class Position extends ArrayList<Integer>
{
    protected Position()
    {
        super();
    }

    protected Position(Position position)
    {
        super(position);
    }

    private Position(List<Integer> position)
    {
        super(position);
    }

    protected Position getParentPosition()
    {
        int size = this.size();
        return size > 1 ? new Position(this.subList(0, size-1)) : null;
    }
}
