package com.example;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.bson.Document;

import static com.example.Cameramen.createDataCameramen;
import static com.example.Cameramen.deleteDataCameramen;
import static com.example.Cameramen.displayDataCameramen;
import static com.example.Cameramen.editDataCameramen;
import static com.example.Cameramen.findDataCameramen;
import static com.example.Fulltimer.createDataFulltimer;
import static com.example.Fulltimer.deleteDataFulltimer;
import static com.example.Fulltimer.displayDataFulltimer;
import static com.example.Fulltimer.editDataFulltimer;
import static com.example.Fulltimer.findDataFulltimer;
import static com.example.ICT.createDataICT;
import static com.example.ICT.deleteDataICT;
import static com.example.ICT.displayDataICT;
import static com.example.ICT.editDataICT;
import static com.example.ICT.findDataICT;
import static com.example.LiveProduction.createDataLiveProduction;
import static com.example.LiveProduction.deleteDataLiveProduction;
import static com.example.LiveProduction.displayDataLiveProduction;
import static com.example.LiveProduction.editDataLiveProduction;
import static com.example.LiveProduction.findDataLiveProduction;
import static com.example.TS.createDataTS;
import static com.example.TS.deleteDataTS;
import static com.example.TS.displayDataTS;
import static com.example.TS.editDataTS;
import static com.example.TS.findDataTS;
import static com.example.Volunteer.createDataVolunteer;
import static com.example.Volunteer.deleteDataVolunteer;
import static com.example.Volunteer.displayDataVolunteer;
import static com.example.Volunteer.editDataVolunteer;
import static com.example.Volunteer.findDataVolunteer;
import static com.example.JadwalPelayanan.createDataJadwalPelayanan;
import static com.example.JadwalPelayanan.deleteDataJadwalPelayanan;
import static com.example.JadwalPelayanan.displayDataJadwalPelayanan;
import static com.example.JadwalPelayanan.editDataJadwalPelayanan;
import static com.example.JadwalPelayanan.findDataJadwalPelayanan;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {
    static MongoCollection<Document> collectionFulltimer;
    static MongoCollection<Document> collectionVolunteer;
    static MongoCollection<Document> collectionCameramen;
    static MongoCollection<Document> collectionLiveProduction;
    static MongoCollection<Document> collectionICT;
    static MongoCollection<Document> collectionTS;
    static MongoCollection<Document> collectionJadwalPelayanan;
    
    static Fulltimer fulltimerLoggedIn;
    public static void main(String[] args) {
        System.setProperty("java.util.logging.config.file", "path/to/logging.properties");
        Scanner sc = new Scanner(System.in);
            MongoClient client = new MongoClient("localhost", 27017);
            MongoDatabase db = client.getDatabase("pelayanan_multimedia_db");

            collectionFulltimer = db.getCollection("fulltimer");
            collectionVolunteer = db.getCollection("volunteer");
            collectionCameramen = db.getCollection("cameramen");
            collectionLiveProduction = db.getCollection("live_production");
            collectionICT = db.getCollection("ict");
            collectionTS = db.getCollection("technical_support");
            collectionJadwalPelayanan = db.getCollection("jadwal_pelayanan");
        
        header();
        boolean isLoggedIn = false;
        
        while (!isLoggedIn) {
            System.out.println();
            System.out.println("================== LOGIN ====================");

            System.out.print("Enter FullName > ");
            String fulltimerNamaLengkap = sc.nextLine();
            System.out.print("Enter NIF > ");
            int fulltimerNIF = sc.nextInt();
            sc.nextLine(); 
            
            fulltimerLoggedIn = new Fulltimer(fulltimerNamaLengkap, fulltimerNIF);

            boolean login = Fulltimer.login(collectionFulltimer, fulltimerLoggedIn.getNamaLengkap(), fulltimerLoggedIn.getNIF());

            if (login != false) {
                System.out.println("");
                System.out.println("============== LOGIN SUCCESSFUL =============");

                while(login == true){
                isLoggedIn = true; 
                System.out.println("");
                int homeChoice = home(sc);

                    switch(homeChoice){
                        case 1 -> {
                            fulltimerMenu(sc);
                        }

                        case 2 -> {
                            volunteerMenu(sc);
                        }

                        case 3 -> {
                            cameramenMenu(sc);
                        }

                        case 4 -> {
                            liveProductionMenu(sc);
                        }

                        case 5 -> {
                            ictMenu(sc);
                        }

                        case 6 -> {
                            tsMenu(sc);
                        }

                        case 7 -> {
                            jadwalPelayananMenu(sc);
                        }

                        case 8 -> {
                            isLoggedIn = false;
                            login = false;
                        }

                        case 9 -> {
                            System.out.println();
                            System.out.println("Thank you, " + fulltimerLoggedIn.getNamaLengkap());
                            login = false;
                        }
                    }
                } 
            }
            else {
                System.out.println();
                System.out.println("Wrong Fullname or NIF\n");
                enter(sc);
            }
        }
    }

    public static void header(){
        System.out.println("=============================================");
        System.out.println("======= Management Information System =======");
        System.out.println("============ Pelayanan Multimedia ===========");
        System.out.println("=============================================");
        System.out.println("========= By Cornelius Louis Nathan =========");
        System.out.println("=========== TI 23 C - 23051204085 ===========");
        System.out.println("=============================================");
    }

    public static void enter(Scanner sc){
        sc.nextLine();
    }

    public static int home(Scanner sc){
        System.out.println();
        System.out.println("Welcome Home, " + fulltimerLoggedIn.getNamaLengkap());
        System.out.println("");
        String[] homeOptions = {
            "1. Access Data Fulltimer",
            "2. Access Data Volunteer",
            "3. Access Data Cameramen",
            "4. Access Data Live Production",
            "5. Access Data ICT",
            "6. Access Data Technical Support",
            "7. Access Data Service schedule",
            "8. Logout",
            "9. Exit",
        };
        for (String option : homeOptions) {
            System.out.println(option);
        }
        System.out.print("Enter your choice (1 - " + (homeOptions.length) + ") > ");
        
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine());
            if(choice < 1 || choice > homeOptions.length) {
                throw new InputMismatchException();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number between 1 and " + homeOptions.length);
            return home(sc);
        }
        return choice;
    }
    
    public static void fulltimerMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("============== FULLTIMER MENU ===============");
            String[] fulltimerOptions = {
                "1. See all fulltimers data",
                "2. Find fulltimer data",
                "3. Add new fulltimer data",
                "4. Update fulltimer data",
                "5. Delete fulltimer data",
                "6. Back",
            };
            
            for(String option : fulltimerOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (fulltimerOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > fulltimerOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataFulltimer(collectionFulltimer,sc);
                    case 2 -> findDataFulltimer(collectionFulltimer, sc);
                    case 3 -> createDataFulltimer(collectionFulltimer, sc);
                    case 4 -> editDataFulltimer(collectionFulltimer, sc);
                    case 5 -> deleteDataFulltimer(collectionFulltimer, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + fulltimerOptions.length);
                    fulltimerMenu(sc);
                }
        }
    }  

    public static void volunteerMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("============== VOLUNTEER MENU ===============");
            String[] volunteerOptions = {
                "1. See all volunteers data",
                "2. Find volunteer data",
                "3. Add new volunteer data",
                "4. Update volunteer data",
                "5. Delete volunteer data",
                "6. Back",
            };
            
            for(String option : volunteerOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (volunteerOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > volunteerOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataVolunteer(collectionVolunteer,sc);
                    case 2 -> findDataVolunteer(collectionVolunteer, sc);
                    case 3 -> createDataVolunteer(collectionVolunteer, sc);
                    case 4 -> editDataVolunteer(collectionVolunteer, sc);
                    case 5 -> deleteDataVolunteer(collectionVolunteer, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + volunteerOptions.length);
                    volunteerMenu(sc);
                }
        }
    }  

    public static void cameramenMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("============== CAMERAMEN MENU ===============");
            String[] cameramenOptions  = {
                "1. See all cameramen data",
                "2. Find cameraman data",
                "3. Add new cameraman data",
                "4. Update cameraman data",
                "5. Delete cameraman data",
                "6. Back",
            };
            
            for(String option : cameramenOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (cameramenOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > cameramenOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataCameramen(collectionCameramen,sc);
                    case 2 -> findDataCameramen(collectionCameramen, sc);
                    case 3 -> createDataCameramen(collectionCameramen, sc);
                    case 4 -> editDataCameramen(collectionCameramen, sc);
                    case 5 -> deleteDataCameramen(collectionCameramen, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + cameramenOptions.length);
                    cameramenMenu(sc);
                }
        }
    }  

    public static void liveProductionMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("=========== LIVE PRODUCTION MENU ============");
            String[] liveProductionOptions  = {
                "1. See all live productions data",
                "2. Find live production data",
                "3. Add new live production data",
                "4. Update live production data",
                "5. Delete live production data",
                "6. Back",
            };
            
            for(String option : liveProductionOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (liveProductionOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > liveProductionOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataLiveProduction(collectionLiveProduction,sc);
                    case 2 -> findDataLiveProduction(collectionLiveProduction, sc);
                    case 3 -> createDataLiveProduction(collectionLiveProduction, sc);
                    case 4 -> editDataLiveProduction(collectionLiveProduction, sc);
                    case 5 -> deleteDataLiveProduction(collectionLiveProduction, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + liveProductionOptions.length);
                    liveProductionMenu(sc);
                }
        }
    }  

    public static void ictMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("================== ICT MENU =================");
            String[] ictOptions  = {
                "1. See all ICT data",
                "2. Find ICT data",
                "3. Add new ICT data",
                "4. Update ICT data",
                "5. Delete ICT data",
                "6. Back",
            };
            
            for(String option :ictOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (ictOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > ictOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataICT(collectionICT,sc);
                    case 2 -> findDataICT(collectionICT, sc);
                    case 3 -> createDataICT(collectionICT, collectionFulltimer, collectionVolunteer, sc);
                    case 4 -> editDataICT(collectionICT, sc);
                    case 5 -> deleteDataICT(collectionICT, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + ictOptions.length);
                    liveProductionMenu(sc);
                }
        }
    }  

    public static void tsMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("=================== TS MENU =================");
            String[] ictOptions  = {
                "1. See all TS data",
                "2. Find TS data",
                "3. Add new TS data",
                "4. Update TS data",
                "5. Delete TS data",
                "6. Back",
            };
            
            for(String option :ictOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (ictOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > ictOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataTS(collectionTS,sc);
                    case 2 -> findDataTS(collectionTS, sc);
                    case 3 -> createDataTS(collectionTS, sc);
                    case 4 -> editDataTS(collectionTS, sc);
                    case 5 -> deleteDataTS(collectionTS, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + ictOptions.length);
                    liveProductionMenu(sc);
                }
        }
    } 

    public static void jadwalPelayananMenu(Scanner sc){
        while(true){
            System.out.println("");
            System.out.println("=========== SERVICE SCHEDULE MENU ===========");
            String[] ictOptions  = {
                "1. See all service schedules data",
                "2. Find service schedule data",
                "3. Add new service schedule data",
                "4. Update service schedule data",
                "5. Delete service schedule data",
                "6. Back",
            };
            
            for(String option :ictOptions) {
                System.out.println(option);
            }
            System.out.print("Enter your choice (1 - " + (ictOptions.length) + ") > ");
            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > ictOptions.length) {
                    throw new InputMismatchException();
                }

                switch(choice) {
                    case 1 -> displayDataJadwalPelayanan(collectionJadwalPelayanan,sc);
                    case 2 -> findDataJadwalPelayanan(collectionJadwalPelayanan, sc);
                    case 3 -> createDataJadwalPelayanan(collectionJadwalPelayanan, collectionFulltimer, collectionVolunteer, sc);
                    case 4 -> editDataJadwalPelayanan(collectionJadwalPelayanan, sc);
                    case 5 -> deleteDataJadwalPelayanan(collectionJadwalPelayanan, sc);
                    case 6 -> {
                        return;
                    }
                }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and " + ictOptions.length);
                    liveProductionMenu(sc);
                }
        }
    } 
}
