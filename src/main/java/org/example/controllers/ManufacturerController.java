package org.example.controllers;

import org.example.models.Manufacturer;
import org.example.models.Model;
import org.example.repository.ManufacturerRepository;

import java.util.Comparator;
import java.util.Scanner;

public class ManufacturerController {
    public static void run(Scanner sc) {
        String inLine = "";
        while (!inLine.equals("6")) {
            System.out.println("""
            MAIN MENU
            1) Show all manufacturers
            2) Get manufactures
            3) Add new manufacturer
            4) Edit manufacturer
            5) Delete manufacturer and related souvenirs
            6) Quit this menu
            
            Enter command:""");

            inLine = sc.nextLine();
            switch (inLine) {
                case "1" -> showAll();
                case "2" -> show(sc);
                case "3" -> create(sc);
                case "4" -> edit(sc);
                case "5" -> delete(sc);
            }
        }
    }

    public static void create(Scanner sc) {
        Manufacturer manufacturer = new Manufacturer();
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        System.out.println("Manufacturer name:");
        manufacturer.setName(sc.nextLine());
        System.out.println("Manufacturer country:");
        manufacturer.setCountry(sc.nextLine());
        manufacturer.setId(manufacturerRepository.getMaxId() + 1);
        manufacturerRepository.store(manufacturer);
    }

    public static void delete(Scanner sc) {
        System.out.println("Manufacturer id:");
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();
        manufacturerRepository.delete(Integer.parseInt(sc.nextLine()));
    }

    public static void edit(Scanner sc) {
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        System.out.println("Manufacturer id:");
        Manufacturer manufacturer = (Manufacturer) manufacturerRepository.get(Integer.parseInt(sc.nextLine()));
        if(manufacturer == null) {
            System.out.println("Record not found");
            return;
        }

        System.out.println("Manufacturer name ('" + manufacturer.getName() + "'):");
        String line = sc.nextLine();
        if(!line.isEmpty()) {
            manufacturer.setName(line);
        }
        System.out.println("Manufacturer country('" + manufacturer.getCountry() + "'):");
        line = sc.nextLine();
        if(!line.isEmpty()) {
            manufacturer.setCountry(line);
        }

        manufacturerRepository.update(manufacturer.getId(), manufacturer);
    }

    public static void showAll() {
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();
        manufacturerRepository.all().stream()
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void show(Scanner sc) {
        System.out.println("Manufacturer id:");
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();
        Manufacturer manufacturer = (Manufacturer) manufacturerRepository.get(Integer.parseInt(sc.nextLine()));
        if(manufacturer != null) {
            System.out.println(manufacturer);
        } else {
            System.out.println("Record not found");
        }
    }
}
