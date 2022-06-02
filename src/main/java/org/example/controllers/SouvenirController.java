package org.example.controllers;

import org.example.configs.AppConfig;
import org.example.models.Souvenir;
import org.example.models.Model;
import org.example.repository.SouvenirRepository;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Scanner;

public class SouvenirController {
    public static void run(Scanner sc) {
        String inLine = "";
        while (!inLine.equals("6")) {
            System.out.println("""
            MAIN MENU
            1) Show all souvenirs
            2) Get souvenir
            3) Add new souvenir
            4) Edit souvenir
            5) Delete souvenir
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
        Souvenir souvenir = new Souvenir();
        SouvenirRepository souvenirRepository = new SouvenirRepository();

        System.out.println("Souvenir name:");
        souvenir.setName(sc.nextLine());
        System.out.println("Manufacturer id:");
        souvenir.setManufacturerId(Integer.parseInt(sc.nextLine()));
        System.out.println("Souvenir created at (in format " + AppConfig.DATE_FORMAT + "):");
        try {
            String line = sc.nextLine();
            souvenir.setCreatedAt(AppConfig.dateFormat.parse(line));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Souvenir price:");
        souvenir.setPrice(Float.parseFloat(sc.nextLine()));
        
        souvenir.setId(souvenirRepository.getMaxId() + 1);
        souvenirRepository.store(souvenir);
    }

    public static void delete(Scanner sc) {
        System.out.println("Souvenir id:");
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        souvenirRepository.delete(Integer.parseInt(sc.nextLine()));
    }

    public static void edit(Scanner sc) {
        SouvenirRepository souvenirRepository = new SouvenirRepository();

        System.out.println("Souvenir id:");
        Souvenir souvenir = (Souvenir) souvenirRepository.get(Integer.parseInt(sc.nextLine()));
        if(souvenir == null) {
            System.out.println("Record not found");
            return;
        }

        System.out.println("Souvenir name ('" + souvenir.getName() + "'):");
        String line = sc.nextLine();
        if(!line.isEmpty()) {
            souvenir.setName(line);
        }
        System.out.println("Manufacturer id ('" + souvenir.getManufacturerId() + "'):");
        line = sc.nextLine();
        if(!line.isEmpty()) {
            souvenir.setManufacturerId(Integer.parseInt(line));
        }
        System.out.println("Souvenir created at ('" + AppConfig.dateFormat.format(souvenir.getCreatedAt())
                + "') (in format \" + AppConfig.DATE_FORMAT + \"):");
        line = sc.nextLine();
        if(!line.isEmpty()) {
            try {
                souvenir.setCreatedAt(AppConfig.dateFormat.parse(line));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Souvenir price ('" + souvenir.getPrice() + "'):");
        line = sc.nextLine();
        if(!line.isEmpty()) {
            souvenir.setPrice(Float.parseFloat(line));
        }

        souvenirRepository.update(souvenir.getId(), souvenir);
    }

    public static void showAll() {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        souvenirRepository.all().stream()
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void show(Scanner sc) {
        System.out.println("Souvenir id:");
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        Souvenir souvenir = (Souvenir) souvenirRepository.get(Integer.parseInt(sc.nextLine()));
        if(souvenir != null) {
            System.out.println(souvenir);
        } else {
            System.out.println("Record not found");
        }
    }
}
