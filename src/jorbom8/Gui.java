package jorbom8;

/**
 * @author jorbom-8
 * Java class that manages the GUI of the bank program
 * 
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


@SuppressWarnings("serial")
public class Gui extends JFrame{
	static Gui gui;
	
	private int appWidth = 800;
	private int appHeight = 700;
	private int topButtonHeight = 120;
	
	private BankLogic bank = new BankLogic();
	
	public JPanel middlePanel;
	private JTextArea customersTextArea;
	
	private JButton button;
	
	private String personNumber;
	
	DefaultTableModel model;
	private JTable table;
	private int selection;
	
	// Inner class as ButtonListener
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			String buttonText = e.getActionCommand(); 
			final String youSureMessage = "Are you sure? Any unsaved progress will be lost";
			if (buttonText.equals("New")) {
				int youSure = JOptionPane.showConfirmDialog(null, youSureMessage, "Confirm", JOptionPane.YES_NO_OPTION);
				if (youSure == 0) {
					bank = new BankLogic();
					gui.setVisible(false);
					gui = new Gui();
					gui.setVisible(true);
				}
			} else if (buttonText.equals("Load")) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("./src/jorbom8/jorbom8_Files"));
				fc.setDialogTitle("Open");
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					load(fc.getSelectedFile().getName());
					//getAllCustomers();
				}
				
			} else if (buttonText.equals("Save")) {
				String saveFile = JOptionPane.showInputDialog(null,
						"File name:");
				if (saveFile != null) {
					save(saveFile);
				}
			} else if (buttonText.equals("Exit")) {
				System.exit(0);
			} else if (buttonText.equals("Customers")) {
				removeAllComponents(middlePanel);
				getAllCustomers();
				revalidate();
				repaint();
			} else if (buttonText.equals("New customer")) {
				removeAllComponents(middlePanel);
				newCustomer();
				revalidate();
				repaint();
			} else if (buttonText.equals("Edit customer")) {
				removeAllComponents(middlePanel);
				editCustomer();
				revalidate();
				repaint();
			} else if (buttonText.equals("Delete customer")) {
				removeAllComponents(middlePanel);
				deleteCustomer();
				revalidate();
				repaint();
			} else if (buttonText.equals("User manual")) {
				JTextArea textArea = new JTextArea(readManual());
				textArea.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
				JOptionPane.showMessageDialog(null, scrollPane, "User manual",
												JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	// Constructor
	public Gui() {
		setLayout(new BorderLayout());
		setSize(appWidth, appHeight);
		setTitle("Home Banking");
		setLocationRelativeTo(null);
		buildMenuBar();
		buildTopButtons();
		buildMiddlePanel();
		getAllCustomers();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// Building the menu bar at the top of the program, options like "File" and "Help" will be shown here
	private void buildMenuBar() {
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		// File option
		JMenu file = new JMenu("File");
		menubar.add(file);
		// File - New
		JMenuItem newFile = new JMenuItem("New");
		file.add(newFile);
		newFile.addActionListener(new ButtonListener());
		// File - Load
		JMenuItem loadFile = new JMenuItem("Load");
		file.add(loadFile);
		loadFile.addActionListener(new ButtonListener());
		// File - Save
		JMenuItem saveFile = new JMenuItem("Save");
		file.add(saveFile);
		saveFile.addActionListener(new ButtonListener());
		// File - Exit
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(new ButtonListener());
		
		// Help option
		JMenu help = new JMenu("Help");
		menubar.add(help);
		JMenuItem about = new JMenuItem("User manual");
		help.add(about);
		about.addActionListener(new ButtonListener());
	}
	
	// Creating the top buttons: "Customers", "New customer", "Edit customer", "Delete customer"
	private void buildTopButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 4, 5, 0));
		panel.setPreferredSize(new Dimension(appWidth - 50, topButtonHeight)); // width, height
		
		// Adding Customers button
		button = new JButton("Customers");
		panel.add(button);
		//button.addActionListener(event -> getAllCustomers());
		button.addActionListener(new ButtonListener());
		
		// Adding New customer button
		button = new JButton("New customer");
		button.addActionListener(new ButtonListener());
		panel.add(button);
		
		// Adding Edit customer button
		button = new JButton("Edit customer");
		button.addActionListener(new ButtonListener());
		panel.add(button);
		
		// Adding Delete customer button
		button = new JButton("Delete customer");
		button.addActionListener(new ButtonListener());
		panel.add(button);
		
		add(panel, BorderLayout.NORTH);
	}
	
	// Building the middle panel, this panel is being used as a template for the middle section, middlePanel is then being used by other methods
	private void buildMiddlePanel() {
		middlePanel = new JPanel(new BorderLayout());
		middlePanel.setLayout(new BorderLayout());
		middlePanel.setPreferredSize(new Dimension(appWidth - 30, appHeight - topButtonHeight - 70));
		add(middlePanel, BorderLayout.CENTER);
	}
	
	// Method to show all the customers, will display on button click "Customers"
	private void getAllCustomers() {
				
		// Creating panels: title, buttonPanel
		JPanel custTitle;
		JPanel buttonPanel;
		
		
		// Creating the title
		custTitle = createTitle("Customers");
		
		customersTextArea = new JTextArea();
		customersTextArea.setEditable(false);
		
		// Creating the table
		table = new JTable() {
			public boolean isCellEditable(int row, int column) {                
                return false;               
			};
		};
		
		Object[] columns = { "First name", "Surname", "Personal number" };
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(0, 0, 880, 200);
		
		//customersTextArea.setText("");
		String[] splited;
		
		for (String customer : bank.getAllCustomers()) {
			splited = customer.split("\\s+"); // Spliting the string by space, making each part a array element
			model.addRow(splited);
		}
		
		// Creating button: "Select customer"
		String[] buttonList = { "Select customer" };
		buttonPanel = createBottomButtons(buttonList);
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (table.getSelectedRow() != -1) {
							selection = table.getSelectedRow();
							personNumber = model.getValueAt(selection, 2).toString();
							removeAllComponents(middlePanel);
							getCustomer(personNumber);
							revalidate();
							repaint();
						} else {
							JOptionPane.showMessageDialog(null,
								    "You need to select a customer. If there is no customer to select, you must create one first.");
						}
					}
				});
			}
		}
		
		// Adding everything to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(pane, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Method to create a new customer, will display on button click "New customer"
	private void newCustomer() {
		
		// Creating 3 panels: title, user input, and a button: "Create"
		JPanel custTitle;
		JPanel userInput;
		JPanel buttonPanel;
		
		// Creating the title "New customer"
		custTitle = createTitle("New customer");
		
		// Creating GridBagLayout to the userInput panel
		String[] textLabels = { "First Name:", "Surname:", "Personal Number:" };
		
		userInput = createUserInput(textLabels);
		
		Component[] textFields = userInput.getComponents();
		
		// Assigning the user input text fields to a variable so its possible to get the text
		JTextField fn = (JTextField) textFields[1];
		JTextField sn = (JTextField) textFields[3];
		JTextField pn = (JTextField) textFields[5];
		
		// Creating the "Create" button
		String[] buttonList = { "Create" };
		
		buttonPanel = createBottomButtons(buttonList);
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (fn.getText().equals("") || sn.getText().equals("") || pn.getText().equals("")) {
							JOptionPane.showMessageDialog(null,
								    "Please fill in all the text fields");
						} else {
							if (isCharacter(fn.getText()) && isCharacter(sn.getText()) && isNumber(pn.getText()) && pn.getText().length() ==10) {
								if (uniquePersonNumber(pn.getText())) {
									bank.createCustomer(fn.getText(), sn.getText(), pn.getText());
									removeAllComponents(middlePanel);
									getAllCustomers();
									revalidate();
									repaint();
								} else {
									JOptionPane.showMessageDialog(null,
										    "That personal number is not unique. Please enter a unique personal number.");
								}
							} else {
								JOptionPane.showMessageDialog(null,
									    "Please fill in all the text fields correctly. First Name and Surname has to exist " +
									    		"only of characters A-Z and has to start with a capital letter.\n" + 
									    		"The personal Number has to exist only of numbers and has to " +
									    		"have the length of 10 numbers.");
							}
						}
					}
				});
			}
		}
		
		
		// Adding the title, the user input textfields and the create button to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(userInput, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Method to edit an existing customer
	private void editCustomer() {
		
		// Creating 3 panels: title, middle, which contains the dropbox and user input, and the edit button
		JPanel custTitle;
		JPanel middle = new JPanel();
		JPanel buttonPanel;
		JPanel userInput;
		JComboBox<String> dropBox;
		
		// Creating the title "Edit customer"
		custTitle = createTitle("Edit customer");
		
		// Creating the middle section, containing the dropbox and user input
		String[] textLabels = { "First Name:", "Surname:" };
		
		userInput = createUserInput(textLabels);
		dropBox = createDropDownBox();
		
		middle.add(dropBox);
		middle.add(userInput);
		
		middle.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 80));
		
		// Creating the "Finish editing" button
		String[] buttonList = { "Finish editing" };
		buttonPanel = createBottomButtons(buttonList);
		
		
		// Getting the textfield components
		Component[] textFields = userInput.getComponents();
		
		// Assigning the user input text fields to a variable so its possible to get the text
		JTextField fn = (JTextField) textFields[1];
		JTextField sn = (JTextField) textFields[3];
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (fn.getText().equals("") || sn.getText().equals("")) {
							JOptionPane.showMessageDialog(null,
								    "Please fill in all the text fields");
						} else {
							String selected = (String) dropBox.getSelectedItem();
							if (selected.equals("-Select a customer-")) {
								JOptionPane.showMessageDialog(null,
									    "Please select a customer.");
							} else {
								if (isCharacter(fn.getText()) && isCharacter(sn.getText())) {
									String[] splited = selected.split("\\s+");
									bank.changeCustomerName(fn.getText(), sn.getText(), splited[2]);
									removeAllComponents(middlePanel);
									getAllCustomers();
									revalidate();
									repaint();
								} else {
									JOptionPane.showMessageDialog(null,
										    "Please fill in the text fields correctly. The First Name and the Surname has to exist " +
												"only of characters and has to start with a capital letter.");
								}
							}
						}
					}
				});
			}
		}
		
		// Adding everything to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(middle, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Method to delete an existing customer
	private void deleteCustomer() {
		
		// Creating 3 panels: title, middle, which contains the dropbox, and the edit button
		JPanel custTitle;
		JPanel middle = new JPanel();
		JPanel buttonPanel;
		JComboBox<String> dropBox;
		
		// Creating the title "Edit customer"
		custTitle = createTitle("Delete customer");
		
		// Creating the middle section, containing the dropbox
		middle.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 100));
		dropBox = createDropDownBox();
		middle.add(dropBox);
		
		// Creating the "Finish editing" button
		String[] buttonList = { "Delete customer" };
		buttonPanel = createBottomButtons(buttonList);
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				JButton button = (JButton) c;
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String selected = (String) dropBox.getSelectedItem();
						if (selected.equals("-Select a customer-")) {
							JOptionPane.showMessageDialog(null,
								    "Please select a customer.");
						} else {
							String[] splited = selected.split("\\s+");
							bank.deleteCustomer(splited[2]);
							removeAllComponents(middlePanel);
							getAllCustomers();
							revalidate();
							repaint();
						}
					}
				});
			}
		}
		
		// Adding everything to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(middle, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Method to inspect a specific customer
	private void getCustomer(String pNo) {
		personNumber = pNo;
		
		// creating 3 panels, title, middle and the buttons
		JPanel custTitle;
		JPanel middle = new JPanel();
		JPanel buttonPanel;
		
		ArrayList<String> thisCustomer = bank.getCustomer(pNo);
		// Creating the title "Customer"
		custTitle = createTitle(thisCustomer.get(0));
		
		// Creating the table
		table = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {                
		              return false;               
			};
		};
		
		Object[] columns = { "Account number", "Balance", "Account type", "Interest" };
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane pane = new JScrollPane(table);
		pane.setBounds(0, 0, 880, 200);
		
		String[] splited;
		
		for (String acc : thisCustomer.subList(1, thisCustomer.size())) {
			splited = acc.split("\\s+"); // Spliting the string by space, making each part a array element
			model.addRow(splited);
		}
		
		middle.add(pane);
		
		// Creating buttons
		String[] buttonList = { "Select account", "Create savings account", "Create credit account", "Close account", "Back to customers" };
		buttonPanel = createBottomButtons(buttonList);
		
		JButton button;
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				switch (((JButton) c).getText()) {
					case "Select account":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (table.getSelectedRow() != -1) {
									selection = table.getSelectedRow();
									int intAccId = Integer.valueOf((String) model.getValueAt(selection, 0));
									removeAllComponents(middlePanel);
									getAccount(personNumber, intAccId);
									revalidate();
									repaint();
								} else {
									JOptionPane.showMessageDialog(null,
										    "Please select an account. If there are no accounts to select, you have to create one first.");
								}
							}
						});
						break;
					case "Create savings account":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								removeAllComponents(middlePanel);
								bank.createSavingsAccount(personNumber);
								getCustomer(personNumber);
								revalidate();
								repaint();
							}
						});
						break;
					case "Create credit account":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								removeAllComponents(middlePanel);
								bank.createCreditAccount(personNumber);
								getCustomer(personNumber);
								revalidate();
								repaint();
							}
						});
						break;
					case "Close account":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (table.getSelectedRow() != -1) {
									selection = table.getSelectedRow();
									int intAccId = Integer.valueOf((String) model.getValueAt(selection, 0));
									removeAllComponents(middlePanel);
									bank.closeAccount(personNumber, intAccId);
									getCustomer(personNumber);
									revalidate();
									repaint();
								} else {
									JOptionPane.showMessageDialog(null,
										    "Please select an account. If there are no accounts to select, you have to create one first.");
								}
							}
						});
						break;
					case "Back to customers":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								removeAllComponents(middlePanel);
								getAllCustomers();
								revalidate();
								repaint();
							}
						});
						break;
				}
			}
		}
		
		// Adding everything to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(middle, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Method to get the information of an account
	private void getAccount(String pNo, int accId) {
		personNumber = pNo;
		
		// creating 3 panels, title, middle and the buttons
		JPanel custTitle;
		JPanel middle = new JPanel();
		JPanel buttonPanel;
		JPanel userInput;
		
		String acc = bank.getAccount(pNo, accId);
		String[] splited  = acc.split("\\s+");
		
		// Creating the title "Customer"
		custTitle = createTitle("Account: " + splited[0]);
		
		// Creating the table
		table = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {                
		              return false;               
			};
		};
				
		Object[] columns = { "Account number", "Balance", "Account type", "Interest" };
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane pane = new JScrollPane(table);
		
		model.addRow(splited);
		
		// Creating text label
		String[] textLabels = { "Amount to deposit or withdraw:" };
		
		middle.add(pane);
		userInput = createUserInput(textLabels);
		middle.add(userInput);
		
		// Creating buttons
		String[] buttonList = { "Deposit", "Withdraw", "Transactions", "Back to customer" };
		buttonPanel = createBottomButtons(buttonList);
		
		// Getting the textfield components
		Component[] textFields = userInput.getComponents();
		
		// Assigning the user input text fields to a variable so its possible to get the text
		JTextField givenAmount = (JTextField) textFields[1];
		
		// Getting the button within the panel to add actionlistener
		Component[] components = buttonPanel.getComponents();
		
		for (Component c : components) {
			if (c instanceof JButton) {
				switch (((JButton) c).getText()) {
					case "Deposit":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (givenAmount.getText().equals("")) {
									JOptionPane.showMessageDialog(null,
										    "Please enter an amount to deposit.");
								} else {
									try {
										bank.deposit(personNumber, accId, Double.parseDouble(givenAmount.getText()));
										removeAllComponents(middlePanel);
										getAccount(personNumber, accId);
										revalidate();
										repaint();
									} catch (NumberFormatException n) {
										JOptionPane.showMessageDialog(null,
											    "Please enter a valid number");
									}
								}
							}
						});
						break;
					case "Withdraw":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (givenAmount.getText().equals("")) {
									JOptionPane.showMessageDialog(null,
										    "Please enter an amount to deposit.");
								} else {
									try {
										if (!bank.withdraw(personNumber, accId, Double.parseDouble(givenAmount.getText()))) {
											JOptionPane.showMessageDialog(null,
												    "The amount you want to withdraw is too high.");
										} else {
											bank.withdraw(personNumber, accId, Double.parseDouble(givenAmount.getText()));
											removeAllComponents(middlePanel);
											getAccount(personNumber, accId);
											revalidate();
											repaint();
										}
									} catch (NumberFormatException n) {
										JOptionPane.showMessageDialog(null,
											    "Please enter a valid number");
									}
								}
							}
						});
						break;
					case "Transactions":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (bank.getTransactions(personNumber, accId) == null) {
									JOptionPane.showMessageDialog(null,
										    "There are no transactions to display.");
								} else {
									String output = "Date  -  Time - Amount - Balance \n";
									ArrayList<String> transactions = bank.getTransactions(personNumber, accId);
									for (String t : transactions) {
										output += t + "\n";
									}
									Object[] options = {"Save",
				                    "Close window"};
									int saveOption = JOptionPane.showOptionDialog(null,
										    output + "\n" + "\n" + "Would  you like to save the bank statement to a file?",
										    "Transactions",
										    JOptionPane.YES_NO_OPTION,
										    JOptionPane.QUESTION_MESSAGE,
										    null,
										    options,
										    options[0]);
									if (saveOption == JOptionPane.YES_OPTION) {
										String saveFile = JOptionPane.showInputDialog(null,
												"File name:");
										if (saveFile != null) {
											saveTransactions(saveFile, pNo, accId);
										}
									}
								}
							}
						});
						break;
					case "Back to customer":
						button = (JButton) c;
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								removeAllComponents(middlePanel);
								getCustomer(personNumber);
								revalidate();
								repaint();
							}
						});
						break;
				}
			}
		}
		
		
		// Adding everything to the middlePanel
		middlePanel.add(custTitle, BorderLayout.NORTH);
		middlePanel.add(middle, BorderLayout.CENTER);
		middlePanel.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	// Creating title at the top of the program
	private JPanel createTitle(String title) {
		JPanel titlePanel = new JPanel();
		
		JLabel newTitle = new JLabel(title);
		newTitle.setFont(new Font("Serif", Font.BOLD, 25));
		titlePanel.add(new JLabel(""));
		newTitle.setHorizontalAlignment(JLabel.CENTER);
		titlePanel.add(newTitle);
		titlePanel.setLayout(new GridLayout(2, 1));
		
		return titlePanel;
	}
	
	// Create drop down box to select a customer
	private JComboBox<String> createDropDownBox() {
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		comboBox.addItem("-Select a customer-");
		
		for (String i : bank.getAllCustomers()) {
			comboBox.addItem(i);
		}
		
		
		return comboBox;
	}
	
	// Create user input
	private JPanel createUserInput(String[] labelList) {
		JPanel userInputPanel = new JPanel();
		
		userInputPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; // horizontal
		c.gridy = 0; // vertical
		c.ipady = 20;
		for (String i : labelList) {
			userInputPanel.add(new JLabel(i), c);
			c.gridy++;
			userInputPanel.add(new JTextField(20), c);
			c.gridy++;
		}
		
		return userInputPanel;
	}
	
	// Creating buttons at the bottom of the program
	private JPanel createBottomButtons(String[] buttons) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 1));
		JButton button;
		
		for (String i : buttons) {
			button = new JButton(i);
			button.setMargin(new Insets(10, 1, 10, 1)); // top, left, bottom, right
			buttonPanel.add(button);
		}
		return buttonPanel;
	}
	
	// method to check if the given string contains numbers
	private boolean isNumber(String n) {
		String no = ".*[0-9].*";
		
		return n.matches(no);
	}
	
	// method to check if the given string contains characters
	private boolean isCharacter(String c) {
		String character = ".*[A-Z].*";
		
		return c.matches(character);
	}
	
	private boolean uniquePersonNumber(String pNo) {
		boolean result = true;
		for (Customer c : bank.customerList) {
			if (pNo.equals(c.getPersonNumber())) {
				result = false;
			}
		}
		return result;
	}
	
	// Remove all components from a given JPanel object. Method is called before a layout change through a button
	private void removeAllComponents(JPanel thisPanel) {
		thisPanel.removeAll();
	}
	
	// Method for reading the user manual text file
	private String readManual() {
		String text = "";
		try {
			File file = new File("./src/jorbom8/manual.txt");
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), "Cp1252"));
			String str;
			
			while((str = br.readLine()) != null) {
				text += str + "\n";
			}
			br.close();
			
		} catch (IOException e) {
			text += "File not found";
		}
		return text;
	}
	
	// Method for saving the transactions to a given file. The source where the file will be saved will be in src/jorbom8/jorbom8_Files
	private void saveTransactions(String fileName, String pNo, int accId) {
		try {
			new File("./src/jorbom8/jorbom8_Files").mkdirs();
			PrintWriter out = new PrintWriter(new File("./src/jorbom8/jorbom8_Files", fileName));
			
			ArrayList<String> transactions = bank.getTransactions(pNo, accId);
			
			if (transactions.isEmpty()) {
				// If there are no transactions to show, the file will be created anyway saying "There are no transactions to show"
				out.println("There are no transactions to show.");
			} else {
				// Spacing in between the titles (Date, Time, Amount and Balance). So that the spacing is always equal
				String titleSpacing = "               ";
				// The normal spacing between the data, also to have the spacing equal
				String spacing = "          ";
				out.println("Transactions for account " + accId);
				out.println("");
				out.println("Date" + titleSpacing + "Time" + titleSpacing + "Amount" + titleSpacing + "Balance");
				out.println("");
				
				// Looping through the transactions
				for (int i = 0; i < transactions.size(); i++) {
					// Splitting each transaction by space and created into array so it becomes possible to loop and add spacing
					String[] splited = transactions.get(i).split("\\s+");
					
					// The string used to add each bit of data in the splited array plus the spacing
					String printableTransactions = "";
					
					for (int b = 0; b < splited.length; b++) {
						printableTransactions += splited[b] + spacing;
					}
					out.println(printableTransactions);
					printableTransactions = "";
				}
			}
			out.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to save to " + fileName);
		}
	}
	
	// Method for saving the customers as well as their bank accounts and everything belonging to this. The source where the file will be saved will be in src/jorbom8/jorbom8_Files
	private void save(String fileName) {
		try {
			new File("./src/jorbom8/jorbom8_Files").mkdirs();
			PrintWriter out = new PrintWriter(new File("./src/jorbom8/jorbom8_Files", fileName));
			
			// The first number in the file will be the amount of customers so that it will be easier to read/load it
			out.println(bank.customerList.size());
			
			for (int i = 0; i < bank.customerList.size(); i++) {
				bank.customerList.get(i).save(out);
			}
			out.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Failed to save to " + fileName);
		}
	}
	
	// Method for loading a file with customers and their bank accounts
	private void load(String fileName) {
		
		// Making a copy of the customerList in case a exception comes up and the loading failed
		List<Customer> custList = new ArrayList<>(bank.customerList);
		
		// clearing the original customerList
		bank.customerList.clear();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			// The first number will be the amount of customers, with other words the code is doing "for each customer do the following"
			int size = Integer.parseInt(in.readLine());
			
			for (int i = 0; i < size; i++) {
				// For each customer recreate the customer object and call the function within each customer object called read, this then reads the lines necessary to create the object
				// After the object is created, it gets added back to the customerList arrayList
				Customer cust = new Customer();
				cust.read(in);
				bank.customerList.add(cust);
			}
			
			
		} catch (FileNotFoundException fe) {
			bank.customerList = custList;
			JOptionPane.showMessageDialog(null, "Failed to load file " + fileName);
			
		} catch (IOException e) {
			bank.customerList = custList;
			JOptionPane.showMessageDialog(null, "Failed to load file " + fileName);
		} finally {
			// At last the middlePanel gets cleared from components and getAllCustomers() gets called so the customers gets displayed after they have been loaded
			removeAllComponents(middlePanel);
			getAllCustomers();
			revalidate();
			repaint();
		}
	}
	
	// Main method to make create GUI object and make it visible
	public static void main(String[] args)
	{
		gui = new Gui();
		gui.setVisible(true);
	}
}
