package console;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StaffManagement {
    private Map<String, Staff> staffAccounts = new HashMap<>();	
    private final String filePath = "staff_accounts.txt";
    private BranchManagement branchManagement;

    public StaffManagement(BranchManagement branchManagement) {
        this.branchManagement = branchManagement;
        loadStaffAccounts();
    }

    private void loadStaffAccounts() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No existing staff accounts file found. A new one will be created.");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) { // Ensure there are enough parts to parse
                    String id = parts[0];
                    String password = parts[1];
                    String branchName = parts[2];
                    char gender = parts[3].charAt(0);
                    int age = Integer.parseInt(parts[4]);
                    boolean branchManager = Boolean.parseBoolean(parts[5]);
                    
                    Staff staff = new Staff(id, password, branchName, gender, age, branchManager);
                    staffAccounts.put(id, staff);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Staff accounts file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An error occurred while reading the staff accounts file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("An error occurred while parsing the age of a staff member: " + e.getMessage());
        }
    }


    public boolean registerStaff(String id, String password, String branchName, char gender, int age, boolean branchManager) {
        if (staffAccounts.containsKey(id)) {
            System.out.println("Staff ID already exists.");
            return false;
        }
        Staff newStaff = new Staff(id, password, branchName, gender, age, branchManager);
        staffAccounts.put(id, newStaff);
        saveToFile();
        return true;
    }

    public boolean updateStaffDetails(String id, String branchName, char gender, int age, boolean branchManager) {
    	Staff staff = staffAccounts.get(id);
        if (staff == null) {
            System.out.println("Staff ID not found.");
            return false;
        }
        staff.setBranchName(branchName);
        staff.setGender(gender);
        staff.setAge(age);
        staff.setBranchManager(branchManager);
        saveToFile();
        return true;
    }

    public boolean deleteStaff(String id) {
        if (!staffAccounts.containsKey(id)) {
            System.out.println("Staff ID not found.");
            return false;
        }
        staffAccounts.remove(id);
        saveToFile();
        System.out.println("Staff removed successfully.");
        return true;
    }

    public void displayStaffList() {
        if (staffAccounts.isEmpty()) {
            System.out.println("No staff accounts available.");
        } else {
            System.out.println("Staff List:");
            staffAccounts.forEach((id,staff) -> System.out.println("ID: " + id +  ", BranchName: " + staff.getBranchName() + ", Age: " + staff.getAge() + ", Gender: " + staff.getGender() + ", BranchManager: " + staff.isBranchManager()));
        }
    }

//    public boolean validateStaffLogin(String id, String password) {
//        Staff staff = staffAccounts.get(id);
//        if (staff != null && staff.getpassword().equals(password)) {
//            System.out.println("Login successful.");
//            return true;
//        } else {
//            //System.out.println("Login failed. Incorrect ID or password.");
//            return false;
//        }
//    }
    
    public String validateStaffLogin(String id, String password) {
        // Admin login shortcut for demonstration purposes
        if ("admin".equalsIgnoreCase(id) && "admin".equals(password)) {
            return "ADMIN";
        }
        
        // Example lookup in a hypothetical staff list/database
        Staff staff = staffAccounts.get(id);
        if (staff != null && staff.getpassword().equals(password)) {
            // Assuming StaffMember class has a getRole() method
        	if(staff.isBranchManager() == true) {
        		return "MANAGER"; // Returns "MANAGER" or "STAFF"
        	}
        	else {
        		return "STAFF";
        	}
        }

        // Login failed
        return null;
    }

    
    public Staff getStaff(String id) {
        return staffAccounts.get(id);
    }


    private void saveToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Staff> entry : staffAccounts.entrySet()) {
                Staff staff = entry.getValue();
                // Assuming you're storing passwords externally, you might need to adjust how you handle them.
                String line = entry.getKey() + "," + staff.getpassword() + "," + staff.getBranchName() + "," + staff.getGender() + "," + staff.getAge() + "," + staff.isBranchManager();
                out.println(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving staff accounts: " + e.getMessage());
        }
    }
    
    public void assignManagers() {
        // Logic to assign managers based on the branch size.
        // You might need to track branch sizes and current manager counts.
    }

    public boolean promoteToManager(String staffId) {
        Staff staff = getStaff(staffId);
        if (staff == null) {
            System.out.println("Staff member not found.");
            return false;
        }
        
        if (staff.isBranchManager()) {
            System.out.println("Staff member is already a manager.");
            return false;
        }

        // Fetch the branch using the branch name from the staff member
        Branch branch = branchManagement.getBranchByName(staff.getBranchName());
        if (branch == null) {
            System.out.println("Branch not found.");
            return false;
        }

        Menu menu = null;
        // Check if the branch is an instance of MenuBranch to get its menu
        if (branch instanceof MenuBranch) {
            menu = ((MenuBranch) branch).getMenu();
        }

        // If the branch does not have a menu (not a MenuBranch), handle accordingly
        if (menu == null) {
            System.out.println("This branch does not have a specific menu. Manager promotion requires a branch with a menu.");
            return false;
        }

        // Create a manager with the branch-specific menu
        Manager manager = new Manager(staff.getid(), staff.getpassword(), staff.getBranchName(), staff.getGender(), staff.getAge(), menu);
        // Update the staff entry to be a manager
        staffAccounts.put(staffId, manager); // Assuming this map is declared and accessible in your StaffManagement
        System.out.println("Staff member " + staffId + " promoted to manager with access to the branch-specific menu.");

        return true;
    }

    public boolean demoteToStaff(String id) {
        Staff staff = getStaff(id);
        if (staff != null && staff.isBranchManager()) {
            // Directly update the isBranchManager flag without checking for instanceof Manager
            staff.setBranchManager(false);
            
            // Save the updated staff information to the file
            saveToFile();
            return true;
        }
        return false;
    }





    public boolean transferStaff(String id, String newBranch) {
        Staff staff = getStaff(id);
        if (staff != null) {
            staff.setBranchName(newBranch);
            saveToFile(); // Save changes
            return true;
        }
        return false;
    }
    
    public int countStaffInBranch(String branchName) {
        return (int) staffAccounts.values().stream()
                .filter(staff -> staff.getBranchName().equals(branchName))
                .count();
    }
    
    public void assignManagersAutomatically() {
        for (Branch branch : branchManagement.getOpenBranches()) {
            String branchName = branch.getName();
            int staffCount = countStaffInBranch(branchName);
            int requiredManagers = calculateRequiredManagers(staffCount);
            int currentManagers = countCurrentManagersInBranch(branchName);

            adjustManagersForBranch(branchName, requiredManagers - currentManagers);
        }
    }
    
    private int calculateRequiredManagers(int staffCount) {
        if (staffCount <= 4) return 1;
        else if (staffCount <= 8) return 2;
        else return 3; // Assuming 9-15 staff members require 3 managers
    }
    
    private int countCurrentManagersInBranch(String branchName) {
        // Iterate through staffAccounts, count how many managers are in the specified branch
        int managerCount = 0;
        for (Staff staff : staffAccounts.values()) {
            if (staff.getBranchName().equals(branchName) && staff.isBranchManager()) {
                managerCount++;
            }
        }
        return managerCount;
    }

    private void adjustManagersForBranch(String branchName, int managersNeeded) {
        if (managersNeeded > 0) {
            promoteStaffToManagers(branchName, managersNeeded);
        } else if (managersNeeded < 0) {
            demoteManagersToStaff(branchName, -managersNeeded);
        }
    }
    
    private void promoteStaffToManagers(String branchName, int numberToPromote) {
        for (Staff staff : staffAccounts.values()) {
            if (!staff.isBranchManager() && staff.getBranchName().equals(branchName) && numberToPromote > 0) {
                staff.setBranchManager(true);
                numberToPromote--;
                System.out.println("Promoted " + staff.getid() + " to manager in branch: " + branchName);
            }
            if (numberToPromote == 0) break;
        }
        saveToFile(); // Assuming this method saves the current state to file
    }

    private void demoteManagersToStaff(String branchName, int numberToDemote) {
        for (Staff staff : staffAccounts.values()) {
            if (staff.isBranchManager() && staff.getBranchName().equals(branchName) && numberToDemote > 0) {
                staff.setBranchManager(false);
                numberToDemote--;
                System.out.println("Demoted " + staff.getid() + " to staff in branch: " + branchName);
            }
            if (numberToDemote == 0) break;
        }
        saveToFile(); // Assuming this method saves the current state to file
    }
    
    public void setBranchStatus(String branchName, boolean isOpen) {
        // Implementation depends on how branches are managed in your system.
    }

    
}
