
/*
 * Name: Elliott Rahming
 * Student ID: 1263495
 * 
 * To compile: javac DiscussionBoard.java
 * To run: java DiscussionBoard
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DiscussionBoard extends JFrame implements ActionListener {

    // GUI components
    private JPanel mainPanel, registerPanel, messagesPanel, cardPanel, createPostPanel, searchPanel;
    private JMenuBar menuBar;
    private JMenu optionsMenu;
    private JMenuItem createPostMenuItem, searchMenuItem, registerMenuItem;
    private JLabel nameLabel, usernameLabel1, usernameLabel2, usernameLabel3, postBodyLabel, postTitleLabel;
    private JTextField nameField, usernameField1, usernameField2, usernameField3, postTitleField;
    private JTextArea messagesArea, postArea;
    private JButton registerButton, createButton, searchButton;

    // Card layout for panel switching
    private CardLayout cardLayout;

    private static List<User> users = new ArrayList<>();
    private static List<TextPost> posts = new ArrayList<>();
    private static HashMap<String, List<Integer>> userPosts = new HashMap<>();
    private static Set<Integer> postIds = new HashSet<>();

    public DiscussionBoard() {
        // Set up the GUI
        setTitle("Discussion Board");
        setFont(new Font("Arial", Font.BOLD, 14));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Create the main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Create the card panel
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        
        // Initialize panels
        initializeMessagesPanel();
        initializeRegisterPanel();
        initializeCreatePostPanel();
        initializeSearchUsernamePanel();
        
        // Add panels to cardPanel
        cardPanel.add(registerPanel, "Register");
        cardPanel.add(createPostPanel, "Create Post");
        cardPanel.add(searchPanel, "Search");
        
        // Create the menu bar
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        optionsMenu.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create menu items and add to options menu
        registerMenuItem = new JMenuItem("Register a User");
        registerMenuItem.setFont(new Font("Arial", Font.BOLD, 14));
        registerMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "Register"));
        optionsMenu.add(registerMenuItem);
        
        createPostMenuItem = new JMenuItem("Create a Text Post");
        createPostMenuItem.setFont(new Font("Arial", Font.BOLD, 14));
        createPostMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "Create Post"));
        optionsMenu.add(createPostMenuItem);
        
        searchMenuItem = new JMenuItem("Search by Username");
        searchMenuItem.setFont(new Font("Arial", Font.BOLD, 14));
        searchMenuItem.addActionListener(e -> cardLayout.show(cardPanel, "Search"));
        optionsMenu.add(searchMenuItem);
        
        // Add menu bar and options
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        
        // Add components to mainPanel
        mainPanel.add(cardPanel, BorderLayout.CENTER); // Main content in center
        mainPanel.add(messagesPanel, BorderLayout.EAST); // Messages consistently on the right
        
        // Set mainPanel to frame
        add(mainPanel);
    }

    public void initializeMessagesPanel(){
        // Create the messages section
        messagesPanel = new JPanel(new BorderLayout());
        messagesPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
        messagesPanel.setFont(new Font("Arial", Font.BOLD, 14));
        messagesArea = new JTextArea(15, 30);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        messagesArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        messagesArea.setMargin(new Insets(5, 5, 1, 1));
        messagesArea.setFont(new Font("Verdana", Font.ITALIC, 12));
        messagesArea.setEditable(false);
        messagesPanel.add(new JScrollPane(messagesArea), BorderLayout.CENTER);
        messagesPanel.setPreferredSize(new Dimension(325, 400));
        

        mainPanel.add(messagesPanel, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        // Handle button clicks
        if (action.equals("Register")) {
            // Get the name and username
            String name = nameField.getText().trim();
            String username = usernameField1.getText().trim();
            
            if (name.isEmpty()){
                messagesArea.append("Please enter your name.\n");
                return;
            }
            
            User user = isRegistered(!username.isEmpty() ? username : name.split("\\s+")[0].toLowerCase());
            if (user != null){
                messagesArea.append("Username already registered.\n");
                nameField.setText("");
                usernameField1.setText("");
            }
            else {
                try {
                    // Register the user
                    user = new User(name, username);
                    users.add(user);
                    messagesArea.append("**User registered successfully**\n");
                }
                catch (IllegalArgumentException ex){
                    messagesArea.append(ex.getMessage() + "\n");
                }
                // Clear the text fields
                nameField.setText("");
                usernameField1.setText("");
            }
        }
        else if (action.equals("Create Post")) {
            // Get the username and post body
            String username = usernameField2.getText().trim();
            String title = postTitleField.getText().trim();
            String postBody = postArea.getText().trim();
            
            if (username.isEmpty()){
                messagesArea.append("Please enter a username\n");
                return;
            }
            else if (title.isEmpty()){
                messagesArea.append("Post must have a title.\n");
                return;
            }
            else if (postBody.isEmpty()){
                messagesArea.append("Post cannot be blank.\n");
                return;
            }
            
            User user = isRegistered(username);
            if (user == null){
                messagesArea.append("Username not registered.\n");
                return;
            }
            else {
                try {
                    // Create the post
                    TextPost post = new TextPost(user, title, postBody, generateId());
                    userPosts.putIfAbsent(user.getUsername(), new ArrayList<>());
                    userPosts.get(user.getUsername()).add(posts.size());
                    posts.add(post);
                }
                catch (IllegalArgumentException ex){
                    messagesArea.append(ex.getMessage() + "\n");
                    return;
                }
                messagesArea.append("**Post created successfully**\n");
            }
            // Clear the text fields
            usernameField2.setText("");
            postArea.setText("");
            postTitleField.setText("");
        }
        else if (action.equals("Search")) {
            // Get the username
            String username = usernameField3.getText().trim();
            
            if (username.isEmpty()){
                messagesArea.append("Please enter a username.\n");
                return;
            }
            
            User user = isRegistered(username);
            if (user == null){
                messagesArea.append("Username not registered.\n");
                return;
            }
            else {
                // Display the posts
                List<Integer> postIds = userPosts.get(username);
                if (postIds == null || postIds.isEmpty()){
                    messagesArea.append(username + " has not made any posts.\n");
                }
                else{
                    messagesArea.append("\n**Posts by " + username + "**\n");
                    for (int index : postIds){
                        messagesArea.append(posts.get(index).toString() + "\n");
                    }
                }
            }
            // Clear the text field
            usernameField3.setText("");
        }
    }

    private void initializeRegisterPanel() {
        registerPanel = new JPanel(new BorderLayout()); // BorderLayout for the main panel
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register User"));
        registerPanel.setFont(new Font("Arial", Font.BOLD, 14));
    
        // Create a form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 10, 10);
    
        // Add Full Name label and text field
        nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(nameLabel, gbc); // Add to formPanel, not registerPanel
    
        nameField = new JTextField(20);
        nameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);
    
        // Add Username label and text field
        usernameLabel1 = new JLabel("Username");
        usernameLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 2;
        formPanel.add(usernameLabel1, gbc);
    
        usernameField1 = new JTextField(20);
        usernameField1.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usernameField1.setPreferredSize(new Dimension(200, 35));
        gbc.gridy = 3;
        formPanel.add(usernameField1, gbc);
    
        // Add Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setBackground(new Color(49, 95, 128));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(200, 75));
        gbc.gridy = 4;
        formPanel.add(registerButton, gbc);
        registerButton.setFocusPainted(false); // Remove focus border
        registerButton.setActionCommand("Register"); // Set action command
        registerButton.addActionListener(this);
        registerButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
    
        // Add formPanel to the center of registerPanel
        registerPanel.add(formPanel, BorderLayout.CENTER);
    }
    

    private void initializeCreatePostPanel(){
        // Create the create post section with GridBagLayout
        createPostPanel = new JPanel(new BorderLayout());
        createPostPanel.setBorder(BorderFactory.createTitledBorder("Create a Text Post"));
        createPostPanel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill the entire cell
        gbc.insets = new Insets(2, 10, 10, 10); // Add padding

        // Create a label for the username
        usernameLabel2 = new JLabel("Username");
        usernameLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(usernameLabel2, gbc);

        // Create a text field for the username
        usernameField2 = new JTextField(20);
        usernameField2.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usernameField2.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(usernameField2, gbc);

        // Create a label for the post title
        postTitleLabel = new JLabel("Post Title");
        postTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(postTitleLabel, gbc);

        // Create a text field for the post title
        postTitleField = new JTextField(20);
        postTitleField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        postTitleField.setFont(new Font("Arial", Font.PLAIN, 14));
        postTitleField.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(postTitleField, gbc);

        // Create a label for the post body
        postBodyLabel = new JLabel("Post Body");
        postBodyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(postBodyLabel, gbc);

        // Create a text field for the post body
        postArea = new JTextArea(20, 20);
        postArea.setFont(new Font("Arial", Font.PLAIN, 14));
        postArea.setMinimumSize(new Dimension(200, 100));
        postArea.setLineWrap(true);
        postArea.setWrapStyleWord(true);
        postArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(postArea, gbc);

        // Create a create button
        createButton = new JButton("Create Post");
        createButton.setFont(new Font("Arial", Font.BOLD, 20));
        createButton.setPreferredSize(new Dimension(200, 40));
        createButton.setBackground(new Color(49, 95, 128));
        createButton.setForeground(Color.WHITE);
        createButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(createButton, gbc);
        createButton.setFocusPainted(false); // Remove focus border
        createButton.setActionCommand("Create Post");
        createButton.addActionListener(this);

        // Add formPanel to the center of createPostPanel
        createPostPanel.add(formPanel, BorderLayout.CENTER);
    }

    private void initializeSearchUsernamePanel(){
        // Create the search username section with GridBagLayout
        searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search posts by Username"));
        searchPanel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill the entire cell
        gbc.insets = new Insets(2, 10, 10, 10); // Add padding

        // Create a label for the name
        usernameLabel3 = new JLabel("Username");
        usernameLabel3.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(usernameLabel3, gbc);

        // Create a text field for the name
        usernameField3 = new JTextField(20);
        usernameField3.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField3.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usernameField3.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(usernameField3, gbc);

        // Create a search button
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(200, 75));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        searchButton.setBackground(new Color(49, 95, 128));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 20));
        searchButton.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        formPanel.add(searchButton, gbc);
        searchButton.setFocusPainted(false); // Remove focus border
        searchButton.setActionCommand("Search");
        searchButton.addActionListener(this);

        // Add formPanel to the center of searchPanel
        searchPanel.add(formPanel, BorderLayout.CENTER);
    }
    
    // Method to check if a username is registered - returns the user object if registered, null otherwise
    public static User isRegistered(String username){
        for (User user : users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    // Method to generate a unique poll ID
    private static int generateId(){
        // The hash set will ensure that we don't have duplicate poll IDs
        int id = (int)(Math.random() * 1000);
        while (postIds.contains(id)){
            id = (int)(Math.random() * 1000);
        }
        postIds.add(id);
        return id;
    }

    public static void main(String[] args) {
        new DiscussionBoard();
    }
}
