package com.example.yash1300.smartbin;

/**
 * Created by Yash 1300 on 10-12-2017.
 */

public class BinClass {
    private String bincap, bincolor, binloc, id;

    public BinClass(String bincap, String bincolor, String binloc, String id) {
        this.bincap = bincap;
        this.bincolor = bincolor;
        this.binloc = binloc;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getBincap() {
        return bincap;
    }

    public String getBincolor() {
        return bincolor;
    }

    public String getBinloc() {
        return binloc;
    }
}
