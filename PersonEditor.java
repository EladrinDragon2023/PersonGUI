package PersonGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A two-step wizard editor for Person types.
 * 1. Prompt for type via dialog
 * 2. Display form with fields based on selected type
 */
public class PersonEditor extends JDialog {
    private String personType;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField birthDayField;
    private JTextField birthMonthField;
    private JTextField birthYearField;
    private JTextField governmentIdField;
    private JTextField studentIdField;
    private boolean confirmed = false;

    /**
     * Create editor in 'Add' mode.
     */
    public PersonEditor(JFrame parent) {
        super(parent, "Add Person", true);
        wizard(parent, null);
    }

    /**
     * Create editor in 'Edit' mode, pre-filling fields.
     */
    public PersonEditor(JFrame parent, Person toEdit) {
        super(parent, "Edit Person", true);
        wizard(parent, toEdit);
    }

    /**
     * Two-step wizard: select type then build form.
     */
    private void wizard(JFrame parent, Person person) {
        String[] types = {"Person", "RegisteredPerson", "OCCCPerson"};
        if (person == null) {
            personType = (String) JOptionPane.showInputDialog(
                parent,
                "Select person type:",
                "Person Type",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]
            );
            if (personType == null) { dispose(); return; }
        } else {
            if (person instanceof OCCCPerson)       personType = "OCCCPerson";
            else if (person instanceof RegisteredPerson) personType = "RegisteredPerson";
            else                                      personType = "Person";
        }
        buildForm();
        if (person != null) fillFields(person);
    }

    /**
     * Build the Swing form with fields according to personType.
     */
    private void buildForm() {
        int rows = 3; // first name, last name, birth date
        if (!personType.equals("Person")) rows++;      // gov ID
        if (personType.equals("OCCCPerson")) rows++;  // student ID
        rows++; // buttons

        getContentPane().removeAll();
        getContentPane().setLayout(new GridLayout(rows, 2, 5, 5));
        setTitle((personType.equals("Person") ? "" : personType + " ") + "Person Editor");

        add(new JLabel("First Name:"));
        firstNameField = new JTextField(); add(firstNameField);

        add(new JLabel("Last Name:"));
        lastNameField = new JTextField(); add(lastNameField);

        add(new JLabel("Birth Date (DD/MM/YYYY):"));
        JPanel datePanel = new JPanel(new GridLayout(1,3,2,2));
        birthDayField   = new JTextField();
        birthMonthField = new JTextField();
        birthYearField  = new JTextField();
        datePanel.add(birthDayField);
        datePanel.add(birthMonthField);
        datePanel.add(birthYearField);
        add(datePanel);

        if (!personType.equals("Person")) {
            add(new JLabel("Government ID:"));
            governmentIdField = new JTextField();
            add(governmentIdField);
        }

        if (personType.equals("OCCCPerson")) {
            add(new JLabel("Student ID:"));
            studentIdField = new JTextField();
            add(studentIdField);
        }

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());
        add(ok);
        add(cancel);

        pack();
        setLocationRelativeTo(getParent());
    }

    /**
     * Prefill the form with the existing person's data.
     */
    private void fillFields(Person p) {
        firstNameField.setText(p.getFirstName());
        lastNameField.setText(p.getLastName());
        OCCCDate bd = p.getBirthDate();
        birthDayField.setText(String.valueOf(bd.getDay()));
        birthMonthField.setText(String.valueOf(bd.getMonth()));
        birthYearField.setText(String.valueOf(bd.getYear()));
        if (p instanceof RegisteredPerson) {
            governmentIdField.setText(((RegisteredPerson)p).getGovernmentId());
        }
        if (p instanceof OCCCPerson) {
            studentIdField.setText(((OCCCPerson)p).getStudentId());
        }
    }

    /**
     * Validate inputs and close if OK.
     */
    private void onOk() {
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name required"); return;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last Name required"); return;
        }
        if (getBirthDate() == null) return;
        if (!personType.equals("Person") && governmentIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Government ID required"); return;
        }
        if (personType.equals("OCCCPerson") && studentIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID required"); return;
        }
        confirmed = true;
        dispose();
    }

    // Getters
    public boolean isConfirmed()       { return confirmed; }
    public String getPersonType()      { return personType; }
    public String getFirstName()       { return firstNameField.getText(); }
    public String getLastName()        { return lastNameField.getText(); }
    public OCCCDate getBirthDate() {
        try {
            int d = Integer.parseInt(birthDayField.getText());
            int m = Integer.parseInt(birthMonthField.getText());
            int y = Integer.parseInt(birthYearField.getText());
            OCCCDate date = new OCCCDate(d, m, y);
            if (date.getDay()!=d || date.getMonth()!=m || date.getYear()!=y)
                throw new Exception();
            return date;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Date!");
            birthDayField.setText(""); birthMonthField.setText(""); birthYearField.setText("");
            return null;
        }
    }
    public String getGovernmentId()    { return governmentIdField!=null ? governmentIdField.getText() : ""; }
    public String getStudentId()       { return studentIdField!=null ? studentIdField.getText() : ""; }

    // Setters for external prefill
    public void setPersonType(String type)          { this.personType = type; }
    public void setFirstName(String name)           { firstNameField.setText(name); }
    public void setLastName(String name)            { lastNameField.setText(name); }
    public void setBirthDate(OCCCDate date) {
        if (date!=null) {
            birthDayField.setText(String.valueOf(date.getDay()));
            birthMonthField.setText(String.valueOf(date.getMonth()));
            birthYearField.setText(String.valueOf(date.getYear()));
        }
    }
    public void setGovernmentId(String gov)          { if (governmentIdField!=null) governmentIdField.setText(gov); }
    public void setStudentId(String sid)             { if (studentIdField!=null) studentIdField.setText(sid); }

    /** Build the Person instance from form data. */
    public Person buildPersonFromEditor() {
        Person p;
        if (personType.equals("RegisteredPerson")) {
            RegisteredPerson rp = new RegisteredPerson(); rp.setGovernmentId(getGovernmentId()); rp.setFirstName(getFirstName()); rp.setLastName(getLastName()); rp.setBirthDate(getBirthDate());
            return rp;
        } else if (personType.equals("OCCCPerson")) {
            OCCCPerson oc = new OCCCPerson(); oc.setGovernmentId(getGovernmentId()); oc.setStudentId(getStudentId()); oc.setFirstName(getFirstName()); oc.setLastName(getLastName()); oc.setBirthDate(getBirthDate());
            return oc;
        } else p = new Person();
        p.setFirstName(getFirstName()); p.setLastName(getLastName()); p.setBirthDate(getBirthDate());
        return p;
    }

    /** Update an existing Person with form data. */
    public void updatePerson(Person p) {
        p.setFirstName(getFirstName()); p.setLastName(getLastName()); p.setBirthDate(getBirthDate());
        if (p instanceof RegisteredPerson) ((RegisteredPerson)p).setGovernmentId(getGovernmentId());
        if (p instanceof OCCCPerson)     ((OCCCPerson)p).setStudentId(getStudentId());
    }
}
