package org.example.models;


public class Manufacturer extends Model {
    private String name;

    private String country;

    public Manufacturer() {}

    public Manufacturer(String csvString) {
        String[] arr = csvString.split(";");
        this.id = Integer.parseInt(arr[0]);
        this.name = arr[1];
        this.country = arr[2];
    }

    @Override
    public String toCSVString() {
        return String.format( "%d;%s;%s",
                this.id,
                this.name,
                this.country
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format( "id: %d | name: %s | country: %s",
                this.id,
                this.name,
                this.country
        );
    }
}
