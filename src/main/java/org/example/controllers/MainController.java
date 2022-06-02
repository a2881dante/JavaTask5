package org.example.controllers;

import java.util.Scanner;

public class MainController {
    public static void run() {
        Scanner sc = new Scanner(System.in);
        String inLine = "";
        while (!inLine.equals("4")) {
            System.out.println("""
            MAIN MENU
            1) Manufacturer menu
            2) Souvenir menu
            3) Report menu
            4) Quit app
            
            Enter command:""");

            inLine = sc.nextLine();
            switch (inLine) {
                case "1" -> ManufacturerController.run(sc);
                case "2" -> SouvenirController.run(sc);
                case "3" -> ReportController.run(sc);
            }
        }
        System.out.println("Good bye! Have a nice day!");
    }
}
