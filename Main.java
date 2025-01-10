import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

class Employee implements Comparable<Employee> {
    private String kode;
    private String nama;
    private String jenisKelamin;
    private String jabatan;
    private double gaji;

    public Employee(String kode, String nama, String jenisKelamin, String jabatan, double gaji) {
        this.kode = kode;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.jabatan = jabatan;
        this.gaji = gaji;
    }

    
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    public double getGaji() { return gaji; }
    public void setGaji(double gaji) { this.gaji = gaji; }

    @Override
    public int compareTo(Employee other) {
        return this.nama.compareTo(other.nama);
    }
}

public class Main {
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nPT. Mentol Employee Management System");
            System.out.println("1. Insert Data Karyawan");
            System.out.println("2. View Data Karyawan");
            System.out.println("3. Update Data Karyawan");
            System.out.println("4. Delete Data Karyawan");
            System.out.println("5. Exit");
            System.out.print("Choice [1-5]: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    insertEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    System.out.println("Thank you for using the system!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static String generateKode() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String kode;
        boolean isUnique;
        
        do {
            StringBuilder sb = new StringBuilder();
            
            // Generate 2 random letters
            for (int i = 0; i < 2; i++) {
                sb.append(letters.charAt(random.nextInt(letters.length())));
            }
            
            sb.append("-");
            
            // Generate 4 random numbers
            for (int i = 0; i < 4; i++) {
                sb.append(random.nextInt(10));
            }
            
            kode = sb.toString();
            
            // Check if code is unique using traditional loop
            isUnique = true;
            for (Employee emp : employees) {
                if (emp.getKode().equals(kode)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        
        return kode;
    }

    private static void insertEmployee() {
        
        String kode = generateKode();
        System.out.println("Kode Karyawan: " + kode);

       
        String nama;
        do {
            System.out.print("Input nama karyawan (min 3 huruf): ");
            nama = scanner.nextLine();
        } while (nama.length() < 3 || !nama.matches("[a-zA-Z ]+"));

       
        String jenisKelamin;
        do {
            System.out.print("Input jenis kelamin (Laki-Laki / Perempuan): ");
            jenisKelamin = scanner.nextLine();
        } while (!jenisKelamin.equals("Laki-Laki") && !jenisKelamin.equals("Perempuan"));

       
        String jabatan;
        do {
            System.out.print("Input jabatan (Manager / Supervisor / Admin): ");
            jabatan = scanner.nextLine();
        } while (!jabatan.equals("Manager") && !jabatan.equals("Supervisor") && !jabatan.equals("Admin"));

       
        double gaji;
        if (jabatan.equals("Manager")) {
            gaji = 8000000;
        } else if (jabatan.equals("Supervisor")) {
            gaji = 6000000;
        } else {
            gaji = 4000000;
        }

       
        employees.add(new Employee(kode, nama, jenisKelamin, jabatan, gaji));

        
        int sameJabatanCount = 0;
        for (Employee emp : employees) {
            if (emp.getJabatan().equals(jabatan)) {
                sameJabatanCount++;
            }
        }

        
        if (sameJabatanCount > 1 && sameJabatanCount % 3 == 0) {
            
            double bonusPercentage;
            if (jabatan.equals("Manager")) {
                bonusPercentage = 0.10; 
            } else if (jabatan.equals("Supervisor")) {
                bonusPercentage = 0.075; 
            } else {
                bonusPercentage = 0.05; 
            }

            
            int bonusCount = 0;
            for (Employee emp : employees) {
                if (emp.getJabatan().equals(jabatan) && !emp.getKode().equals(kode)) {
                    double newGaji = emp.getGaji() * (1 + bonusPercentage);
                    emp.setGaji(newGaji);
                    System.out.printf("Bonus sebesar %.1f%% telah diberikan kepada %s!%n", 
                                    bonusPercentage * 100, emp.getNama());
                    bonusCount++;
                    if (bonusCount == sameJabatanCount - 1) break; 
                }
            }
        }
        
        System.out.println("Berhasil menambahkan karyawan baru!");
    }

    private static void checkAndApplyBonus(String jabatan) {
        long count = employees.stream()
                            .filter(e -> e.getJabatan().equals(jabatan))
                            .count();
        
        if (count % 3 == 0) { 
            double bonus = switch (jabatan) {
                case "Manager" -> 0.10; 
                case "Supervisor" -> 0.075; 
                case "Admin" -> 0.05; 
                default -> 0;
            };
            
            
            employees.stream()
                    .filter(e -> e.getJabatan().equals(jabatan))
                    .limit(count - 1) 
                    .forEach(e -> {
                        double newGaji = e.getGaji() * (1 + bonus);
                        e.setGaji(newGaji);
                        System.out.printf("Bonus sebesar %.1f%% telah diberikan kepada %s!%n", 
                                        bonus * 100, e.getNama());
                    });
        }
    }

    private static void viewEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees found!");
            return;
        }

        
        ArrayList<Employee> sortedEmployees = new ArrayList<>(employees);
        Collections.sort(sortedEmployees);

       
        System.out.println("\nDaftar Karyawan PT. Mentol:");
        System.out.println("No. | Kode     | Nama             | Jenis Kelamin | Jabatan    | Gaji");
        System.out.println("--------------------------------------------------------------------");

       
        for (int i = 0; i < sortedEmployees.size(); i++) {
            Employee emp = sortedEmployees.get(i);
            System.out.printf("%-3d | %-8s | %-16s | %-13s | %-10s | Rp %.2f%n",
                    i + 1, emp.getKode(), emp.getNama(), emp.getJenisKelamin(),
                    emp.getJabatan(), emp.getGaji());
        }
    }

    private static void updateEmployee() {
        viewEmployees();
        
        if (employees.isEmpty()) {
            return;
        }

        System.out.print("Input nomor karyawan yang akan diupdate: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        if (index < 1 || index > employees.size()) {
            System.out.println("Invalid employee number!");
            return;
        }

        ArrayList<Employee> sortedEmployees = new ArrayList<>(employees);
        Collections.sort(sortedEmployees);
        Employee oldEmployee = sortedEmployees.get(index - 1);
        
        
        String kode = generateKode();
        System.out.println("Kode Karyawan baru: " + kode);

        
        String nama;
        do {
            System.out.print("Input nama karyawan baru (min 3 huruf): ");
            nama = scanner.nextLine();
        } while (nama.length() < 3 || !nama.matches("[a-zA-Z ]+"));

        
        String jenisKelamin;
        do {
            System.out.print("Input jenis kelamin baru (Laki-Laki / Perempuan): ");
            jenisKelamin = scanner.nextLine();
        } while (!jenisKelamin.equals("Laki-Laki") && !jenisKelamin.equals("Perempuan"));

        
        String jabatan;
        do {
            System.out.print("Input jabatan baru (Manager / Supervisor / Admin): ");
            jabatan = scanner.nextLine();
        } while (!jabatan.equals("Manager") && !jabatan.equals("Supervisor") && !jabatan.equals("Admin"));

        
        double gaji = switch (jabatan) {
            case "Manager" -> 8000000;
            case "Supervisor" -> 6000000;
            case "Admin" -> 4000000;
            default -> 0;
        };

        
        int actualIndex = employees.indexOf(oldEmployee);
        employees.set(actualIndex, new Employee(kode, nama, jenisKelamin, jabatan, gaji));
        
        
        checkAndApplyBonus(jabatan);
        
        System.out.println("Berhasil mengupdate data karyawan!");
    }

    private static void deleteEmployee() {
        viewEmployees();
        
        if (employees.isEmpty()) {
            return;
        }

        System.out.print("Input nomor karyawan yang akan dihapus: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        if (index < 1 || index > employees.size()) {
            System.out.println("Invalid employee number!");
            return;
        }

        ArrayList<Employee> sortedEmployees = new ArrayList<>(employees);
        Collections.sort(sortedEmployees);
        Employee employeeToDelete = sortedEmployees.get(index - 1);
        
        employees.remove(employeeToDelete);
        System.out.println("Berhasil menghapus data karyawan!");
    }
}
