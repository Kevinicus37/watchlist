package org.launchcode.watchlist.Enums;

public enum PageSize {
    SHORT(10),
    DEFAULT(20),
    LONGER(50),
    LONGEST(100);

    private int size;

    // getter method
    public int getSize()
    {
        return this.size;
    }

    // enum constructor - cannot be public or protected
    private PageSize(int size)
    {
        this.size = size;
    }
}
