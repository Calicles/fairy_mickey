package com.antoine.model;

public class Exit extends Shape {
    int next_decor_id;

    public Exit(int next_decor_id ) {
        super();
        this.next_decor_id = next_decor_id;
    }

    public int getNext_decor_id() { return this.next_decor_id; }
}
