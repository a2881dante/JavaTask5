package org.example.models;

public abstract class Model
{
    protected int id;

    Model() {}

    public abstract String toCSVString();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
