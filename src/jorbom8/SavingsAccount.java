package jorbom8;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author jorbom-8
 * Java subclass extending the super class Account
 * SavingsAccount.java
 */

public class SavingsAccount extends Account{
	
	final static double interest = 1.0; // interest in percentage
	final static String accountType = "Sparkonto";
	
	Date today; // today's date, gets set in the method getDate()
	Date nextYear; // useful for having 1 free withdraw per year, also gets set in getDate()

	Calendar cal; // variable for declaring the next year to check free yearly withdraw
	
	// Constructor
	public SavingsAccount() {
		super(accountType, interest);
	}
	
	
	// Gets the current date and sets the variable today
	public void getDate() {
		cal = Calendar.getInstance();
		today = cal.getTime();
	}
	
	// Gets the next year to check if the withdraw are supposed to be the yearly free withdraw or not
	public Date setNextYear() {
		cal.add(Calendar.YEAR, 1);
		nextYear = cal.getTime();
		return nextYear;
	}
		
	
	// Withdraw an amount of money from the bank account
	// Checks if its the first yearly withdraw or not, bases amount withdrawn on that
	// Will only withdraw if the amount is less than the total balance
	public void withdrawal(double amount) {
		getDate(); // setting variables today
		if (nextYear != null) { // nextYear has been set, meaning it is not a free withdraw
			if (today.before(nextYear)) { // checking if the withdraw is within a year
				double newAmount = 1.02*amount;
				if (newAmount <= getBalance()) {
					super.balance = super.balance - newAmount;
					SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					super.transactions.add(dateForm.format(today) + " -" + newAmount + " " + getBalance());
				}
			} else {
				nextYear = null; // resets the variable. Yearly transition and a free withdraw
				withdrawal(amount);
			}
		} else { // nextYear is null, meaning it will be a free withdraw
			if (amount <= getBalance()) {
				setNextYear();
				super.balance = super.balance - amount;
				SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				super.transactions.add(dateForm.format(today) + " -" + amount + " " + getBalance());
			}
		}
	}
}
