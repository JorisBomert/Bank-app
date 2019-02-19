package jorbom8;

/**
 * @author jorbom-8
 * Java subclass extending the super class Account
 * CreditAccount.java
 */

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class CreditAccount extends Account{
	final double maxCredit = -5000.00;
	final static double interest = 0.5;
	final double debtInterest = 7.0;
	final static String accountType = "Kreditkonto";
	
	// Constructor
	public CreditAccount() {
		super(accountType, interest);
	}
	
	// Withdraw an amount of money from the bank account
	public void withdrawal(double amount) {
		if (super.balance - amount >= this.maxCredit) {
			super.balance = super.balance - amount;
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			super.transactions.add(dateForm.format(cal.getTime()) + " -" + amount + " " + getBalance());
		}
	}
	// overriding method from super to add the variable debtInterest
	public String getAccountInformation() {
		String interestStr;
		String accNumberStr = Integer.toString(super.accountNumber);
		String balanceStr = Double.toString(getBalance());
		if (getBalance() < 0.0) {
			interestStr = Double.toString(debtInterest);
		} else {
			interestStr = Double.toString(interest);
		}
		String accountInfo = accNumberStr + " " + balanceStr + " " + accountType +
				" " + interestStr;
		return accountInfo;
	}
	
	// Get the interest number
		public double getInterestNumber() {
			if (getBalance() < 0.0) {
				return debtInterest;
			} else {
				return interest;
			}
		}
	
	// overriding method to add the increase interest rate if the balance is less than 0.0
	public double getInterest() {
		if (getBalance() < 0.0) {
			return balance * debtInterest / 100;
		} else {
			return balance * interest / 100;
		}
	}
	
}
