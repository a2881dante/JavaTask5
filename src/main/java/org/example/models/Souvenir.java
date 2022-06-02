package org.example.models;

import org.example.configs.AppConfig;
import org.example.repository.ManufacturerRepository;

import java.text.ParseException;
import java.util.Date;


public class Souvenir extends Model{
    private String name;

    private int manufacturerId;

    private Date createdAt;

    private float price;

    public Souvenir() {}

    public Souvenir(String csvString) {
        String[] arr = csvString.split(";");
        this.id = Integer.parseInt(arr[0]);
        this.name = arr[1];
        this.manufacturerId = Integer.parseInt(arr[2]);
        try {
            this.createdAt = AppConfig.dateFormat.parse(arr[3]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.price = Float.parseFloat(arr[4]);
    }

    @Override
    public String toCSVString() {
        return String.format( "%d;%s;%d;%s;%f",
                this.id,
                this.name,
                this.manufacturerId,
                AppConfig.dateFormat.format(this.createdAt),
                this.price
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Manufacturer getManufacturer() {
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();
        return (Manufacturer) manufacturerRepository.get(this.manufacturerId);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format( "id: %d | name: %s | manufacturer: %s | date: %s | price: %.2fUAH",
                this.id,
                this.name,
                this.getManufacturer().getName(),
                AppConfig.dateFormat.format(this.createdAt),
                this.price
        );
    }
}
