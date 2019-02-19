package jorbom8;

/**
 * @author jorbom-8
 * Java super class that manages the bank account's balance,
 * interest, account type and account number
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public abstract class Account {
	static protected int lastAccountNumber = 1000;
	protected int accountNumber;
	protected double interest;
	protected double balance;
	protected String accountType;
	
	protected ArrayList<String> transactions = new ArrayList<String>();
	
	public Account(String type, double interest) {
		this.accountType = type;
		this.interest = interest;
		this.balance = 0.0;
		lastAccountNumber++;
		this.accountNumber = lastAccountNumber;
	}

	// Deposit an amount of money to the bank account
	public void deposit(double amount) {
		balance = balance + amount;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		transactions.add(dateForm.format(cal.getTime()) + " " + amount + " " + getBalance());
	}
	
	// Withdraw an amount of money from the bank account, abstract method to be overridden
	public abstract void withdrawal(double amount);
		
	// Get the current balance
	public double getBalance() {
		return balance;
	}
		
	// Get the account number
	public int getAccountNumber() {
		return accountNumber;
	}
	
	// Get the account type
	public String getAccountType() {
		return accountType;
	}
	
	// Get the interest number
	public double getInterestNumber() {
		return interest;
	}
		
	// Calculate the interest
	public double getInterest() {
		return balance * interest / 100;
	}
	
	// Get all the transactions made on this account
	public ArrayList<String> getTransactions() {
		return transactions;
	}
	
	// Get all the account information
	public String getAccountInformation() {
		String accNumberStr = Integer.toString(accountNumber);
		String balanceStr = Double.toString(getBalance());
		String interestStr = Double.toString(interest);
		String accountInfo = accNumberStr + " " + balanceStr + " " + accountType +
				" " + interestStr;
		return accountInfo;
	}
}
