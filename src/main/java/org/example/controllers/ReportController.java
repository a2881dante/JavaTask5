package org.example.controllers;

import org.example.models.Model;
import org.example.models.Souvenir;
import org.example.repository.ManufacturerRepository;
import org.example.repository.SouvenirRepository;

import java.util.*;

public class ReportController {
    public static void run(Scanner sc) {
        String inLine = "";
        while (!inLine.equals("7")) {
            System.out.println("""
            MAIN MENU
            1) Show souvenirs by manufacturer
            2) Show souvenirs by country
            3) Show manufacturer by max souvenir price
            4) Show all manufacturers with souvenirs
            5) Show manufacturers by year and souvenir
            6) Show all souvenirs by years
            7) Quit this menu
            
            Enter command:""");

            inLine = sc.nextLine();
            switch (inLine) {
                case "1" -> showSouvenirsByManufacturerId(sc);
                case "2" -> showSouvenirsByCountry(sc);
                case "3" -> showManufacturersByMaxPrice(sc);
                case "4" -> showAllManufacturersWithSouvenirs();
                case "5" -> showManufacturersByYearAndSouvenir(sc);
                case "6" -> showAllSouvenirsByYears();
            }
        }
    }

    public static void showSouvenirsByManufacturerId(Scanner sc) {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        System.out.println("Manufacturer id:");
        String line = sc.nextLine();
        souvenirRepository.all().stream()
                .filter(s -> ((Souvenir) s).getManufacturerId() == Integer.parseInt(line))
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void showSouvenirsByCountry(Scanner sc) {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        System.out.println("Country:");
        String line = sc.nextLine();
        souvenirRepository.all().stream()
                .filter(s -> ((Souvenir) s).getManufacturer().getCountry().equals(line))
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void showManufacturersByMaxPrice(Scanner sc) {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        List<Model> manufacturers = manufacturerRepository.all();

        System.out.println("Max price:");
        String line = sc.nextLine();
        souvenirRepository.all().stream()
                .filter(s -> ((Souvenir) s).getPrice() <= Float.parseFloat(line))
                .map(s -> manufacturers.stream()
                        .filter(m -> m.getId() == ((Souvenir) s).getManufacturerId())
                        .findFirst()
                        .get())
                .distinct()
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void showAllManufacturersWithSouvenirs() {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        List<Model> manufacturers = manufacturerRepository.all();
        List<Model> souvenirs = souvenirRepository.all();

        manufacturers.stream()
                .sorted(Comparator.comparing(Model::getId))
                .forEach(m -> {
                    System.out.println(m);
                    souvenirs.stream()
                            .filter(s -> ((Souvenir)s).getManufacturerId() == m.getId())
                            .sorted(Comparator.comparing(Model::getId))
                            .forEach(s1 -> System.out.println("   " + s1));
                });
    }

    public static void showManufacturersByYearAndSouvenir (Scanner sc) {
        System.out.println("Year:");
        String year = sc.nextLine();

        System.out.println("Souvenir:");
        String souvenir = sc.nextLine();

        SouvenirRepository souvenirRepository = new SouvenirRepository();
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        List<Model> manufacturers = manufacturerRepository.all();
        List<Model> souvenirs = souvenirRepository.all();

        souvenirs.stream()
                .filter(s -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(((Souvenir) s).getCreatedAt());
                    return ((Souvenir) s).getName().equals(souvenir)
                        && calendar.get(Calendar.YEAR) == Integer.parseInt(year);
                })
                .map(s -> manufacturers.stream()
                        .filter(m -> m.getId() == ((Souvenir) s).getManufacturerId())
                        .findFirst()
                        .get())
                .distinct()
                .sorted(Comparator.comparing(Model::getId))
                .forEach(System.out::println);
    }

    public static void showAllSouvenirsByYears () {
        SouvenirRepository souvenirRepository = new SouvenirRepository();
        ManufacturerRepository manufacturerRepository = new ManufacturerRepository();

        List<Model> manufacturers = manufacturerRepository.all();
        List<Model> souvenirs = souvenirRepository.all();

        TreeMap<Integer, ArrayList<Souvenir>> souvenirsByYears = new TreeMap<>();

        souvenirs.forEach(s -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(((Souvenir) s).getCreatedAt());
            int sYear = calendar.get(Calendar.YEAR);

            if(souvenirsByYears.containsKey(sYear)){
               souvenirsByYears.get(sYear).add((Souvenir) s);
            } else {
                souvenirsByYears.put(sYear, new ArrayList<>());
                souvenirsByYears.get(sYear).add((Souvenir) s);
            }
        });

        Set<Integer> keys = souvenirsByYears.keySet();
        keys.forEach(k -> {
            System.out.println(k);
            souvenirsByYears.get(k)
                    .stream()
                    .sorted(Comparator.comparing(Model::getId))
                    .forEach(System.out::println);
        });
    }
}
