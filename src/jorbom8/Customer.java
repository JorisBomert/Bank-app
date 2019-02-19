package jorbom8;
/**
 * @author jorbom-8
 * Java class that manages the the following customer information:
 * the customers full name, personal number and a list with all the
 * bank accounts belonging to the customer
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Customer {
	private String firstName;
	private String lastName;
	private String personNumber;
	
	public Customer() {
		this.firstName = "Unknown first name";
		this.lastName = "Unknown Surname";
		this.personNumber = "Unknown personal number";
	}
	
	// Setting the final variable personNumber with a constructor
	public Customer(String firstName, String lastName, String personNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.personNumber = personNumber;
	}
	
	// arraylist of objects for savings accounts belonging to the customer
	List<Account> userAccounts = new ArrayList<Account>();
	
	// Changing the first name	
	public void setName(String name) {
		firstName = name;
	}
	
	// Changing the surname
	public void setSurname(String name) {
		lastName = name;
	}
	
	// Returning the first name and the last name to get the full name
	public String getName() {
		return firstName + " " + lastName;
	}
	
	// Returning the person number
	public String getPersonNumber() {
		return personNumber;
	}
	
	// Method for adding accounts to customer
	public void addAccount(SavingsAccount account) {
		userAccounts.add(account);
	}
	
	// Method for saving (printing to a file)
	public void save(PrintWriter out) {
		out.println(this.firstName);
		out.println(this.lastName);
		out.println(this.personNumber);
		
		out.println(this.userAccounts.size());
		
		for (Account acc : userAccounts) {
			out.println(acc.accountType);
			out.println(acc.accountNumber);
			out.println(acc.interest);
			out.println(acc.balance);
			
			out.println(acc.getTransactions().size());
			
			for (String transaction : acc.getTransactions()) {
				out.println(transaction);
			}
		}
	}
	
	public void read(BufferedReader in) {
		try {
			this.firstName = in.readLine();
			this.lastName = in.readLine();
			this.personNumber = in.readLine();
			int amountAccounts = Integer.parseInt(in.readLine());
			
			for (int i = 0; i < amountAccounts; i++) {
				String accountType = in.readLine();
				if (accountType.equals("Sparkonto")) {
					//in.readLine();
					Account newAccount = new SavingsAccount();
					newAccount.accountNumber = Integer.parseInt(in.readLine());
					newAccount.interest = Double.parseDouble(in.readLine());
					newAccount.balance = Double.parseDouble(in.readLine());
					int amountTransactions = Integer.parseInt(in.readLine());
					for (int b = 0; b < amountTransactions; b++) {
						newAccount.transactions.add(in.readLine().toString());
					}
					userAccounts.add(newAccount);
				} else if (accountType.equals("Kreditkonto")) {
					//in.readLine();
					Account newAccount = new CreditAccount();
					newAccount.accountNumber = Integer.parseInt(in.readLine());
					newAccount.interest = Double.parseDouble(in.readLine());
					newAccount.balance = Double.parseDouble(in.readLine());
					int amountTransactions = Integer.parseInt(in.readLine());
					for (int b = 0; b < amountTransactions; b++) {
						newAccount.transactions.add(in.readLine().toString());
					}
					userAccounts.add(newAccount);
				}
			}
		} catch(IOException e) {
			System.out.println("Failed to read from file");
		}
	}
}
