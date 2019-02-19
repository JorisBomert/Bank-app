package jorbom8;
/**
 * @author jorbom-8
 * Java class that manages the list of the existing customers
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BankLogic {
	
	// arraylist of customer objects including the bank accounts
	List<Customer> customerList = new ArrayList<Customer>();
	
	// returning the customer list as a string (full customer name and person number)
	public ArrayList<String> getAllCustomers() {
		ArrayList<String> stringCustomers = new ArrayList<String>();
		for (Customer el : customerList) {
			stringCustomers.add(el.getName() + " " + el.getPersonNumber());
		}
		return stringCustomers;
	}
	
	// Method for creating a new customer, returns true if it's a unique person number, otherwise it returns false
	public boolean createCustomer(String name, String surname, String pNo) {
		for(int i = 0; i < customerList.size(); i++) {
			if(customerList.get(i).getPersonNumber() == pNo) {
				return false;
			}
		}
		Customer newCustomer = new Customer(name, surname, pNo);
		customerList.add(newCustomer);
		return true;
	}
	
	// Request information about a specific customer, by searching on a person number
	public ArrayList<String> getCustomer(String pNo) {
		ArrayList<String> thisCustomer = new ArrayList<String>();
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				thisCustomer.add(customerList.get(i).getName() + " " + customerList.get(i).getPersonNumber());
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					thisCustomer.add(customerList.get(i).userAccounts.get(b).getAccountInformation());
				}
			}
		}
		if (thisCustomer.isEmpty()) {
			thisCustomer.add(null);
		}
		return thisCustomer;
	}
	
	// Method to change the name of a customer, if the person number exists
	public boolean changeCustomerName(String name, String surname, String pNo) {
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				customerList.get(i).setName(name);
				customerList.get(i).setSurname(surname);
				return true;
			}
		}
		return false;
	}
	
	// Removes a customer including all bank accounts belonging to this customer
	public ArrayList<String> deleteCustomer(String pNo) {
		ArrayList<String> thisCustomer = new ArrayList<String>();
		
		Iterator<Customer> it = customerList.iterator();
		
		while (it.hasNext()) {
			Customer i = it.next();
			if (i.getPersonNumber().equals(pNo)) {
				it.remove();
				thisCustomer.add(i.getName() + " " + i.getPersonNumber());
				for (int b = 0; b < i.userAccounts.size(); b++) {
					thisCustomer.add(i.userAccounts.get(b).getAccountNumber() +
							" " + i.userAccounts.get(b).getBalance() +
							" " + i.userAccounts.get(b).getAccountType() +
							" " + i.userAccounts.get(b).getInterestNumber() +
							" " + i.userAccounts.get(b).getInterest());
				}
			}
		}
		
		if (thisCustomer.isEmpty()) {
			thisCustomer.add(null);
		}
		return thisCustomer;
	}
	
	// Create a savings account for a customer
	public int createSavingsAccount(String pNo) {
		int accountNumber = -1;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				Account newAccount = new SavingsAccount();
				customerList.get(i).userAccounts.add((SavingsAccount) newAccount);
				accountNumber = newAccount.getAccountNumber();
			}
		}
		return accountNumber;
	}
	
	// Creates a credit account
	public int createCreditAccount(String pNr) {
		int accountNumber = -1;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNr)) {
				Account newAccount = new CreditAccount();
				customerList.get(i).userAccounts.add((CreditAccount) newAccount);
				accountNumber = newAccount.getAccountNumber();
			}
		}
		return accountNumber;
	}
	
	// Gets the transactions of a specific customers account number
	public ArrayList<String> getTransactions(String pNr, int accountId) {
		ArrayList<String> transactions = new ArrayList<String>();
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNr)) {
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					if (customerList.get(i).userAccounts.get(b).getAccountNumber() == accountId) {
						transactions = customerList.get(i).userAccounts.get(b).getTransactions();
					}
				}
			}
		}
		if (transactions.isEmpty()) {
			return null;
		} else {
			return transactions;
		}
	}
	
	// Gets the information of a certain account 
	public String getAccount(String pNo, int accountId) {
		String thisAccount = null;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					if (customerList.get(i).userAccounts.get(b).getAccountNumber() == accountId) {
						thisAccount = customerList.get(i).userAccounts.get(b).getAccountInformation();
					}
				}
			}
		}
		return thisAccount;
	}
	
	// Deposit a given amount onto an account of choice
	public boolean deposit(String pNo, int accountId, double amount) {
		boolean didDeposit = false;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					if (customerList.get(i).userAccounts.get(b).getAccountNumber() == accountId) {
						customerList.get(i).userAccounts.get(b).deposit(amount);
						didDeposit = true;
					}
				}
			}
		}
		return didDeposit;
	}
	
	// Withdraw a given amount from an account of choice
	public boolean withdraw(String pNo, int accountId, double amount) {
		boolean didWithdraw = false;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					if (customerList.get(i).userAccounts.get(b).getAccountNumber() == accountId) {
						double currentBalance = customerList.get(i).userAccounts.get(b).getBalance();
						customerList.get(i).userAccounts.get(b).withdrawal(amount);
						if (currentBalance > customerList.get(i).userAccounts.get(b).getBalance()) {
							didWithdraw = true;
						}
					}
				}
			}
		}
		return didWithdraw;
	}
	
	// Close a bank account by choice
	public String closeAccount(String pNo, int accountId) {
		String thisAccount = null;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getPersonNumber().equals(pNo)) {
				for (int b = 0; b < customerList.get(i).userAccounts.size(); b++) {
					if (customerList.get(i).userAccounts.get(b).getAccountNumber() == accountId) {
						double interest = customerList.get(i).userAccounts.get(b).getInterest();
						thisAccount = customerList.get(i).userAccounts.get(b).getAccountInformation() + " " + interest;
						customerList.get(i).userAccounts.remove(b);
					}
				}
			}
		}
		return thisAccount;
	}
	

}
