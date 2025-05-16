package PersonGui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import PersonGui.Person;
import PersonGui.RegisteredPerson;
import PersonGui.OCCCPerson;
import PersonGui.OCCCDate;

public class MainApplication {
    private JFrame frame;
    private DefaultListModel<Person> personListModel;
    private JList<Person> personList;
    private ArrayList<Person> persons = new ArrayList<>();
    private File currentFile = null;
    private Person selectedPerson = null;

    public MainApplication() {
        frame = new JFrame("Person Management Application");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem helpOptions = new JMenuItem("Help Options");

        helpMenu.add(helpOptions);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        helpOptions.addActionListener(e -> showHelpDialog());

        personListModel = new DefaultListModel<>();
        personList = new JList<>(personListModel);
        personList.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(personList), BorderLayout.CENTER);
 
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Person");
        JButton editButton = new JButton("Edit Person");
        JButton deleteButton = new JButton("Delete Person");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        newItem.addActionListener(e -> clearList());
        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        saveAsItem.addActionListener(e -> saveFileAs());
        exitItem.addActionListener(e -> confirmExit());

        addButton.addActionListener(e -> addPerson());
        editButton.addActionListener(e -> editPerson());
        deleteButton.addActionListener(e -> deletePerson());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        frame.setVisible(true);
    }

    private void clearList() {
        personListModel.clear();
        persons.clear();
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Serialized Files", "ser"));
    
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                persons = (ArrayList<Person>) ois.readObject();
                personListModel.clear();
                persons.forEach(personListModel::addElement);
                JOptionPane.showMessageDialog(frame, "File loaded successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            saveToFile(currentFile);
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            saveToFile(currentFile);
        }
    }

    private void saveToFile(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(persons);
            JOptionPane.showMessageDialog(frame, "File saved successfully.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(frame, "Save before exit?", "Confirm Exit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            saveFile();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void addPerson() {
    PersonEditor editor = new PersonEditor(frame, null);
    editor.setVisible(true);
    if (editor.isConfirmed()) {
        String type = editor.getPersonType();
        Person newPerson;

        if ("RegisteredPerson".equals(type)) {
            newPerson = new RegisteredPerson(
                editor.getFirstName(),
                editor.getLastName(),
                editor.getGovernmentId()
            );
        } else if ("OCCCPerson".equals(type)) {
            RegisteredPerson base = new RegisteredPerson(
                editor.getFirstName(),
                editor.getLastName(),
                editor.getGovernmentId()
            );
            newPerson = new OCCCPerson(
                base,
                editor.getStudentId()
            );
        } else {
            newPerson = new Person(
                editor.getFirstName(),
                editor.getLastName(),
                editor.getBirthDate()
            );
        }

        newPerson.setBirthDate(editor.getBirthDate());

        persons.add(newPerson);
        personListModel.addElement(newPerson);
    }
}


    private void deletePerson() {
        int index = personList.getSelectedIndex();
        if (index >= 0) {
            persons.remove(index);
            personListModel.remove(index);
        }
    }

    private void editPerson() {
        int selectedIndex = personList.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a person to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        Person selectedPerson = personListModel.get(selectedIndex);
    
        PersonEditor editor = new PersonEditor(frame, selectedPerson);
    
        editor.setFirstName(selectedPerson.getFirstName());
        editor.setLastName(selectedPerson.getLastName());
        editor.setBirthDate(selectedPerson.getBirthDate());
    
        if (selectedPerson instanceof RegisteredPerson) {
            editor.setPersonType("RegisteredPerson");
            editor.setGovernmentId(((RegisteredPerson) selectedPerson).getGovernmentId());
        }
    
        if (selectedPerson instanceof OCCCPerson) {
            editor.setPersonType("OCCCPerson");
            editor.setGovernmentId(((OCCCPerson) selectedPerson).getGovernmentId());
            editor.setStudentId(((OCCCPerson) selectedPerson).getStudentId());
        }
    
        editor.setVisible(true);
 
        if (editor.isConfirmed()) {
            selectedPerson.setFirstName(editor.getFirstName());
            selectedPerson.setLastName(editor.getLastName());
            
    Person newPerson = editor.buildPersonFromEditor();
    int index = persons.indexOf(selectedPerson);
    if (index != -1) {
        persons.set(index, newPerson);
    }
    
            selectedPerson.setBirthDate(((PersonEditor) editor).getBirthDate());
    
            if (selectedPerson instanceof RegisteredPerson) {
                ((RegisteredPerson) selectedPerson).setGovernmentId(editor.getGovernmentId());
            }
    
            if (selectedPerson instanceof OCCCPerson) {
                ((OCCCPerson) selectedPerson).setGovernmentId(editor.getGovernmentId());
                ((OCCCPerson) selectedPerson).setStudentId(editor.getStudentId());
            }

            personListModel.set(selectedIndex, selectedPerson);
        }
    }

    private void showHelpDialog() {
        JDialog helpDialog = new JDialog(frame, "Help Options", true);
        helpDialog.setLayout(new BorderLayout());
        helpDialog.setSize(400, 200);
        helpDialog.setLocationRelativeTo(frame);
    
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton aboutButton = new JButton("About");
        aboutButton.addActionListener(e -> JOptionPane.showMessageDialog(helpDialog,
                "Final Java Project - Person Management System\nDeveloped by Rylan Murr",
                "About", JOptionPane.INFORMATION_MESSAGE));
        contentPanel.add(aboutButton);

        JButton instructionsButton = new JButton("Instructions");
        instructionsButton.addActionListener(e -> JOptionPane.showMessageDialog(helpDialog,
                "Instructions:\n1. Use 'Add Person' to add a new record.\n" +
                        "2. Use 'Edit Person' to modify an existing record.\n" +
                        "3. Use 'Delete Person' to remove a selected record.\n" +
                        "4. Use 'Save' and 'Load' from the File menu to manage records.",
                "Instructions", JOptionPane.INFORMATION_MESSAGE));
        contentPanel.add(instructionsButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> helpDialog.dispose());
        contentPanel.add(closeButton);

        helpDialog.add(contentPanel, BorderLayout.CENTER);

        helpDialog.setVisible(true);
    }
    public static void main(String[] args) {
        new MainApplication();
    } 
}