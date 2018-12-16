package com.github.tamnguyenbbt.dom;

import java.util.ArrayList;

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

    protected Position getParentPosition()
    {
        int size = this.size();
        return size > 1 ? new Position((Position)this.subList(0, size-2)) : null;
    }
}
