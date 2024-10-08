package controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
//import java.util.Scanner;

import models.Branch;
import models.MenuItem;
import models.Order;
import models.PaymentMethod;
import models.Staff;

public class DataController {
//	private Scanner scanner = new Scanner(System.in);
	private static FileInputStream fis;
	private static ObjectInputStream ois;
	private static  FileOutputStream fos; 
	private static  ObjectOutputStream oos;
	
	public static void openFileToWrite(String fileName) {
		try {
			ensureDirectoryExists(fileName);
			fos = new FileOutputStream(fileName); // overwrite mode
			oos = new ObjectOutputStream(fos);
		} catch (IOException e) {
			System.err.println("An error occurred while opening the file to write: " + e.getMessage());
		}
	}
	
//	public static void openFileToAppend(String fileName) {
//		try {
//			ensureDirectoryExists(fileName);
//			fos = new FileOutputStream(fileName, true); // append mode
//			oos = new ObjectOutputStream(fos);
//		} catch (IOException e) {
//			System.err.println("An error occurred while opening the file to append: " + e.getMessage());
//		}
//	}
	
	public static void openFileToRead(String fileName) {
		try {
			ensureDirectoryExists(fileName);
			fis = new FileInputStream(fileName);
			ois = new ObjectInputStream(fis);
		} catch (IOException e) {
			System.err.println("An error occurred while opening the file to read: " + e.getMessage());
		}
	}
	
	private static void ensureDirectoryExists(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }
	
	public static void closeFileAfterWrite() {
		if(oos != null){
			try {
				oos.close();
			} catch (IOException e) {
				System.err.println("An error occurred while closing the file after writing: " + e.getMessage());
			}
	    } 
	}
	
	public static void closeFileAfterRead() {
		if (ois != null) {
            try {
                ois.close();
            } catch (IOException e) {
                System.err.println("An error occurred while closing the file after reading: " + e.getMessage());
            }
        }
	}
	
	public static void writeStaffAccountsToFile(List<Staff> staffAccounts, String fileName) {
		try {
            openFileToWrite(fileName);
            oos.writeObject(staffAccounts);
        } catch (IOException e) {
            System.err.println("An error occurred while saving staff accounts to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<Staff> readStaffAccountsFromFile(String fileName) {
		List<Staff> staffAccounts = null;
		try {
			openFileToRead(fileName);
			staffAccounts = (List<Staff>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading staff accounts from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return staffAccounts;
        }
	}
	
	public static void writeMenuToFile(List<MenuItem> menu, String fileName) {
        try {
            openFileToWrite(fileName);
            oos.writeObject(menu);
        } catch (IOException e) {
            System.err.println("An error occurred while saving menu to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<MenuItem> readMenuFromFile(String fileName) {
		List<MenuItem> menu = null;
		try {
			openFileToRead(fileName);
			menu = (List<MenuItem>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading menu from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return menu;
        }
	}
	
	public static void writePaymentMethodsToFile(List<PaymentMethod> paymentMethods, String fileName) {
        try {
            openFileToWrite(fileName);
            oos.writeObject(paymentMethods);
        } catch (IOException e) {
            System.err.println("An error occurred while saving payment methods to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<PaymentMethod> readPaymentMethodsFromFile(String fileName) {
		List<PaymentMethod> paymentMethods = null;
		try {
			openFileToRead(fileName);
			paymentMethods = (List<PaymentMethod>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading payment methods from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return paymentMethods;
        }
	}
	
	public static void writeBranchListToFile(List<Branch> branchList, String fileName) {
        try {
            openFileToWrite(fileName);
            oos.writeObject(branchList);
        } catch (IOException e) {
            System.err.println("An error occurred while saving branch list to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<Branch> readBranchListFromFile(String fileName) {
		List<Branch> branchList = null;
		try {
			openFileToRead(fileName);
			branchList = (List<Branch>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading branch list from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return branchList;
        }
	}
	
	public static void writeOrdersToFile(List<Order> orders, String fileName) {
        try {
            openFileToWrite(fileName);
            oos.writeObject(orders);
        } catch (IOException e) {
            System.err.println("An error occurred while saving orders to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<Order> readOrdersFromFile(String fileName) {
		List<Order> orders = null;
		try {
			openFileToRead(fileName);
			orders = (List<Order>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading orders from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return orders;
        }
	}
	
	public static void writePendingRequestsToFile(List<String> requests, String fileName) {
        try {
            openFileToWrite(fileName);
            oos.writeObject(requests);
        } catch (IOException e) {
            System.err.println("An error occurred while saving pending requests to file: " + e.getMessage());
        } finally {
        	closeFileAfterWrite();
        }
	}
	
	@SuppressWarnings("finally")
	public static List<String> readPendingRequestsFromFile(String fileName) {
		List<String> requests = null;
		try {
			openFileToRead(fileName);
			requests = (List<String>) ois.readObject();
        } catch (IOException e) {
            System.err.println("An error occurred while reading pending requests from file: " + e.getMessage());
        } finally {
        	closeFileAfterRead();
        	return requests;
        }
	}
}
