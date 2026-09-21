package ringotel.app;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RingotelTestRunnerApp extends JFrame {

    private static final DateTimeFormatter LOG_FOLDER_FORMAT =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd_HH-mm-ss-SSS"
            );

    private final JTextField domainField =
            new JTextField(
                    "testwebsoftphone",
                    20
            );

    private final List<UserRow> userRows =
            new ArrayList<>();

    private JPanel usersContainer;

    private final JButton addUserButton =
            new JButton(
                    "+ Add User"
            );

    private final JCheckBox loginTest =
            new JCheckBox(
                    "Login"
            );

    private final JCheckBox contactsTest =
            new JCheckBox(
                    "Contacts"
            );

    private final JCheckBox presenceTest =
            new JCheckBox(
                    "Presence"
            );

    private final JCheckBox chatsTest =
            new JCheckBox(
                    "Chats"
            );

    private final JCheckBox messagingAuditTest =
            new JCheckBox(
                    "Messaging Audit"
            );

    private final JCheckBox chatCleanupTest =
            new JCheckBox(
                    "Chat Cleanup"
            );

    private final JCheckBox callsTest =
            new JCheckBox(
                    "Internal Calls"
            );

    private final JCheckBox callControlsTest =
            new JCheckBox(
                    "Call Controls"
            );

    private final JCheckBox realCallTest =
            new JCheckBox(
                    "Real Call"
            );

    private final JCheckBox realSmsTest =
            new JCheckBox(
                    "Real SMS"
            );

    private final JTextField chatRecipientField =
            new JTextField(
                    "Mykola",
                    20
            );

    private final JTextField chatOccurrenceField =
            new JTextField(
                    "1",
                    6
            );

    private final JTextField chatMessageField =
            new JTextField(
                    28
            );

    private final JCheckBox deleteChatAfterSend =
            new JCheckBox(
                    "Delete chat after send",
                    true
            );

    private final JTextField realCallNumberField =
            new JTextField(
                    18
            );

    private final JTextField realSmsNumberField =
            new JTextField(
                    18
            );

    private final JTextField realSmsMessageField =
            new JTextField(
                    28
            );

    private JPanel conditionalOptionsPanel;

    private JPanel chatConfigurationPanel;

    private JPanel realCallConfigurationPanel;

    private JPanel realSmsConfigurationPanel;

    private final JCheckBox chromeBrowser =
            new JCheckBox(
                    "Google Chrome",
                    true
            );

    private final JCheckBox firefoxBrowser =
            new JCheckBox(
                    "Mozilla Firefox"
            );

    private final JCheckBox edgeBrowser =
            new JCheckBox(
                    "Microsoft Edge"
            );

    private final JCheckBox braveBrowser =
            new JCheckBox(
                    "Brave"
            );

    private final JCheckBox operaBrowser =
            new JCheckBox(
                    "Opera"
            );

    private final JCheckBox safariBrowser =
            new JCheckBox(
                    "Safari"
            );

    private final JButton runSelectedButton =
            new JButton(
                    "Run Selected"
            );

    private final JButton selectAllButton =
            new JButton(
                    "Select All Tests"
            );

    private final JButton clearSelectionButton =
            new JButton(
                    "Clear Selection"
            );

    private final JButton exportLogsButton =
            new JButton(
                    "Export Logs"
            );

    private final JButton clearLogsButton =
            new JButton(
                    "Clear Logs"
            );

    private final JTextArea generalLog =
            createLogArea();

    private final JTextArea chromeLog =
            createLogArea();

    private final JTextArea firefoxLog =
            createLogArea();

    private final JTextArea edgeLog =
            createLogArea();

    private final JTextArea braveLog =
            createLogArea();

    private final JTextArea operaLog =
            createLogArea();

    private final JTextArea safariLog =
            createLogArea();

    private final Map<String, JTextArea> browserLogs =
            new LinkedHashMap<>();

    private final Map<JCheckBox, String> testClasses =
            new LinkedHashMap<>();

    private volatile Process runningProcess;

    private Path currentRunDirectory;

    public RingotelTestRunnerApp() {

        super(
                "Ringotel Test Runner"
        );

        initializeMappings();

        initializeWindow();

        createUi();

        bindActions();

        configureSafari();

        updateUserAvailability();

        updateConditionalPanels();
    }

    private void initializeMappings() {

        browserLogs.put(
                "chrome",
                chromeLog
        );

        browserLogs.put(
                "firefox",
                firefoxLog
        );

        browserLogs.put(
                "edge",
                edgeLog
        );

        browserLogs.put(
                "brave",
                braveLog
        );

        browserLogs.put(
                "opera",
                operaLog
        );

        browserLogs.put(
                "safari",
                safariLog
        );

        testClasses.put(
                loginTest,
                "ringotel.tests.LoginTests"
        );

        testClasses.put(
                contactsTest,
                "ringotel.tests.ContactTests"
        );

        testClasses.put(
                presenceTest,
                "ringotel.tests.PresenceTests"
        );

        testClasses.put(
                chatsTest,
                "ringotel.tests.ChatTests"
        );

        testClasses.put(
                messagingAuditTest,
                "ringotel.tests.ContactMessagingAuditTests"
        );

        testClasses.put(
                chatCleanupTest,
                "ringotel.tests.ChatCleanupTests"
        );

        testClasses.put(
                callsTest,
                "ringotel.tests.CallTests"
        );

        testClasses.put(
                callControlsTest,
                "ringotel.tests.CallControlsTests"
        );
    }

    private void initializeWindow() {

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(
                        1180,
                        940
                )
        );

        setLocationRelativeTo(
                null
        );
    }

    private void createUi() {

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                12,
                                12
                        )
                );

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JLabel title =
                new JLabel(
                        "Ringotel Test Runner"
                );

        title.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        26
                )
        );

        root.add(
                title,
                BorderLayout.NORTH
        );

        JPanel configuration =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        configuration.add(
                createUsersPanel(),
                BorderLayout.NORTH
        );

        configuration.add(
                createTestsPanel(),
                BorderLayout.CENTER
        );

        configuration.add(
                createBrowserPanel(),
                BorderLayout.SOUTH
        );

        JScrollPane configurationScroll =
                new JScrollPane(
                        configuration
                );

        configurationScroll.setBorder(
                null
        );

        configurationScroll
                .getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        root.add(
                configurationScroll,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        bottom.add(
                createControlPanel(),
                BorderLayout.NORTH
        );

        bottom.add(
                createLogTabs(),
                BorderLayout.CENTER
        );

        root.add(
                bottom,
                BorderLayout.SOUTH
        );

        setContentPane(
                root
        );

        pack();
    }

    private JPanel createUsersPanel() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        wrapper.setBorder(
                BorderFactory.createTitledBorder(
                        "Test Configuration"
                )
        );

        JPanel domainPanel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints domainConstraints =
                createGridBagConstraints();

        addField(
                domainPanel,
                domainConstraints,
                0,
                "Domain",
                domainField
        );

        wrapper.add(
                domainPanel,
                BorderLayout.NORTH
        );

        usersContainer =
                new JPanel();

        usersContainer.setLayout(
                new BoxLayout(
                        usersContainer,
                        BoxLayout.Y_AXIS
                )
        );

        wrapper.add(
                usersContainer,
                BorderLayout.CENTER
        );

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        actions.add(
                addUserButton
        );

        wrapper.add(
                actions,
                BorderLayout.SOUTH
        );

        addUserRow(
                "4321"
        );

        return wrapper;
    }

    private void addUserRow(
            String extension
    ) {

        UserRow row =
                new UserRow(
                        extension
                );

        userRows.add(
                row
        );

        usersContainer.add(
                row.panel
        );

        reindexUserRows();

        usersContainer.revalidate();

        usersContainer.repaint();

        updateUserAvailability();
    }

    private void removeUserRow(
            UserRow row
    ) {

        int index =
                userRows.indexOf(
                        row
                );

        if (index <= 0) {
            return;
        }

        userRows.remove(
                row
        );

        usersContainer.remove(
                row.panel
        );

        reindexUserRows();

        usersContainer.revalidate();

        usersContainer.repaint();

        updateUserAvailability();
    }

    private void reindexUserRows() {

        for (int i = 0;
             i < userRows.size();
             i++) {

            UserRow row =
                    userRows.get(
                            i
                    );

            row.panel.setBorder(
                    BorderFactory.createTitledBorder(
                            "User "
                                    + (i + 1)
                    )
            );

            row.removeButton.setEnabled(
                    i > 0
                            && runSelectedButton.isEnabled()
            );
        }
    }

    private JPanel createTestsPanel() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        wrapper.setBorder(
                BorderFactory.createTitledBorder(
                        "Scenarios"
                )
        );

        JPanel tests =
                new JPanel(
                        new GridLayout(
                                0,
                                4,
                                8,
                                8
                        )
                );

        tests.add(
                loginTest
        );

        tests.add(
                contactsTest
        );

        tests.add(
                presenceTest
        );

        tests.add(
                chatsTest
        );

        tests.add(
                messagingAuditTest
        );

        tests.add(
                chatCleanupTest
        );

        tests.add(
                callsTest
        );

        tests.add(
                callControlsTest
        );

        tests.add(
                realCallTest
        );

        tests.add(
                realSmsTest
        );

        wrapper.add(
                tests,
                BorderLayout.NORTH
        );

        conditionalOptionsPanel =
                new JPanel();

        conditionalOptionsPanel.setLayout(
                new BoxLayout(
                        conditionalOptionsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        chatConfigurationPanel =
                createChatConfigurationPanel();

        realCallConfigurationPanel =
                createRealCallConfigurationPanel();

        realSmsConfigurationPanel =
                createRealSmsConfigurationPanel();

        conditionalOptionsPanel.add(
                chatConfigurationPanel
        );

        conditionalOptionsPanel.add(
                realCallConfigurationPanel
        );

        conditionalOptionsPanel.add(
                realSmsConfigurationPanel
        );

        wrapper.add(
                conditionalOptionsPanel,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    private JPanel createChatConfigurationPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Chat Configuration"
                )
        );

        GridBagConstraints gbc =
                createGridBagConstraints();

        addField(
                panel,
                gbc,
                0,
                "Specific Recipient",
                chatRecipientField
        );

        addField(
                panel,
                gbc,
                1,
                "Occurrence",
                chatOccurrenceField
        );

        addField(
                panel,
                gbc,
                2,
                "Message",
                chatMessageField
        );

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;

        panel.add(
                deleteChatAfterSend,
                gbc
        );

        return panel;
    }

    private JPanel createRealCallConfigurationPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Real Call Configuration"
                )
        );

        GridBagConstraints gbc =
                createGridBagConstraints();

        addField(
                panel,
                gbc,
                0,
                "Destination Number",
                realCallNumberField
        );

        return panel;
    }

    private JPanel createRealSmsConfigurationPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Real SMS Configuration"
                )
        );

        GridBagConstraints gbc =
                createGridBagConstraints();

        addField(
                panel,
                gbc,
                0,
                "Destination Number",
                realSmsNumberField
        );

        addField(
                panel,
                gbc,
                1,
                "Message",
                realSmsMessageField
        );

        return panel;
    }

    private JPanel createBrowserPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Browsers"
                )
        );

        panel.add(
                chromeBrowser
        );

        panel.add(
                firefoxBrowser
        );

        panel.add(
                edgeBrowser
        );

        panel.add(
                braveBrowser
        );

        panel.add(
                operaBrowser
        );

        panel.add(
                safariBrowser
        );

        return panel;
    }

    private JPanel createControlPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        panel.add(
                runSelectedButton
        );

        panel.add(
                selectAllButton
        );

        panel.add(
                clearSelectionButton
        );

        panel.add(
                exportLogsButton
        );

        panel.add(
                clearLogsButton
        );

        return panel;
    }

    private JTabbedPane createLogTabs() {

        JTabbedPane tabs =
                new JTabbedPane();

        tabs.setPreferredSize(
                new Dimension(
                        1120,
                        300
                )
        );

        tabs.addTab(
                "General",
                new JScrollPane(
                        generalLog
                )
        );

        tabs.addTab(
                "Chrome",
                new JScrollPane(
                        chromeLog
                )
        );

        tabs.addTab(
                "Firefox",
                new JScrollPane(
                        firefoxLog
                )
        );

        tabs.addTab(
                "Edge",
                new JScrollPane(
                        edgeLog
                )
        );

        tabs.addTab(
                "Brave",
                new JScrollPane(
                        braveLog
                )
        );

        tabs.addTab(
                "Opera",
                new JScrollPane(
                        operaLog
                )
        );

        tabs.addTab(
                "Safari",
                new JScrollPane(
                        safariLog
                )
        );

        return tabs;
    }

    private GridBagConstraints createGridBagConstraints() {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        7,
                        5,
                        7
                );

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        return gbc;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String name,
            JTextField field
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        panel.add(
                new JLabel(
                        name + ":"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                field,
                gbc
        );
    }

    private void bindActions() {

        addUserButton.addActionListener(
                e -> addUserRow(
                        ""
                )
        );

        chatsTest.addActionListener(
                e -> updateConditionalPanels()
        );

        realCallTest.addActionListener(
                e -> updateConditionalPanels()
        );

        realSmsTest.addActionListener(
                e -> updateConditionalPanels()
        );

        runSelectedButton.addActionListener(
                e -> startSelectedTests()
        );

        selectAllButton.addActionListener(
                e -> selectAllTests()
        );

        clearSelectionButton.addActionListener(
                e -> clearTestSelection()
        );

        exportLogsButton.addActionListener(
                e -> exportLogs()
        );

        clearLogsButton.addActionListener(
                e -> clearLogs()
        );
    }

    private void configureSafari() {

        safariBrowser.setEnabled(
                isMac()
        );

        if (isMac()) {

            safariBrowser.setText(
                    "Safari"
            );

        } else {

            safariBrowser.setText(
                    "Safari (macOS only)"
            );
        }
    }

    private void updateUserAvailability() {

        boolean enoughUsers =
                userRows.size() >= 2;

        callsTest.setEnabled(
                enoughUsers
                        && runSelectedButton.isEnabled()
        );

        callControlsTest.setEnabled(
                enoughUsers
                        && runSelectedButton.isEnabled()
        );

        if (!enoughUsers) {

            callsTest.setSelected(
                    false
            );

            callControlsTest.setSelected(
                    false
            );
        }
    }

    private void updateConditionalPanels() {

        if (chatConfigurationPanel == null) {
            return;
        }

        boolean interactive =
                runSelectedButton.isEnabled();

        chatConfigurationPanel.setVisible(
                chatsTest.isSelected()
        );

        realCallConfigurationPanel.setVisible(
                realCallTest.isSelected()
        );

        realSmsConfigurationPanel.setVisible(
                realSmsTest.isSelected()
        );

        chatRecipientField.setEnabled(
                interactive
                        && chatsTest.isSelected()
        );

        chatOccurrenceField.setEnabled(
                interactive
                        && chatsTest.isSelected()
        );

        chatMessageField.setEnabled(
                interactive
                        && chatsTest.isSelected()
        );

        deleteChatAfterSend.setEnabled(
                interactive
                        && chatsTest.isSelected()
        );

        realCallNumberField.setEnabled(
                interactive
                        && realCallTest.isSelected()
        );

        realSmsNumberField.setEnabled(
                interactive
                        && realSmsTest.isSelected()
        );

        realSmsMessageField.setEnabled(
                interactive
                        && realSmsTest.isSelected()
        );

        conditionalOptionsPanel.revalidate();

        conditionalOptionsPanel.repaint();
    }

    private void selectAllTests() {

        for (JCheckBox checkBox :
                testClasses.keySet()) {

            if (checkBox.isEnabled()) {

                checkBox.setSelected(
                        true
                );
            }
        }

        realCallTest.setSelected(
                false
        );

        realSmsTest.setSelected(
                false
        );

        updateConditionalPanels();
    }

    private void clearTestSelection() {

        for (JCheckBox checkBox :
                testClasses.keySet()) {

            checkBox.setSelected(
                    false
            );
        }

        realCallTest.setSelected(
                false
        );

        realSmsTest.setSelected(
                false
        );

        updateConditionalPanels();
    }

    private void startSelectedTests() {

        if (runningProcess != null
                && runningProcess.isAlive()) {

            showValidationError(
                    "A test run is already in progress.",
                    null
            );

            return;
        }

        List<String> selectedTests =
                getSelectedTestClasses();

        boolean externalSelected =
                realCallTest.isSelected()
                        || realSmsTest.isSelected();

        if (selectedTests.isEmpty()
                && !externalSelected) {

            showValidationError(
                    "Select at least one scenario.",
                    null
            );

            return;
        }

        if (!validateConfiguration()) {
            return;
        }

        List<String> browsers =
                getSelectedBrowsers();

        if (browsers.isEmpty()) {

            showValidationError(
                    "Select at least one browser.",
                    null
            );

            return;
        }

        if (!confirmDangerousActions()) {
            return;
        }

        if (externalSelected) {

            appendGeneral(
                    "Real Call / Real SMS configuration is ready."
            );

            appendGeneral(
                    "External Selenium scenarios are not connected yet and will be skipped."
            );
        }

        if (selectedTests.isEmpty()) {
            return;
        }

        createRunDirectory();

        writeRunConfiguration(
                browsers,
                selectedTests
        );

        setRunnerEnabled(
                false
        );

        appendGeneral(
                ""
        );

        appendGeneral(
                "========================================"
        );

        appendGeneral(
                "Starting selected test run"
        );

        appendGeneral(
                "Domain: "
                        + domainField
                        .getText()
                        .trim()
        );

        appendGeneral(
                "Users: "
                        + userRows.size()
        );

        for (int i = 0;
             i < userRows.size();
             i++) {

            appendGeneral(
                    "User "
                            + (i + 1)
                            + ": "
                            + userRows
                            .get(i)
                            .extensionField
                            .getText()
                            .trim()
            );
        }

        if (chatsTest.isSelected()) {

            appendGeneral(
                    "Chat recipient: "
                            + chatRecipientField
                            .getText()
                            .trim()
            );
        }

        appendGeneral(
                "Browsers: "
                        + String.join(
                        ", ",
                        browsers
                )
        );

        appendGeneral(
                "Log directory: "
                        + currentRunDirectory
                        .toAbsolutePath()
        );

        appendGeneral(
                "========================================"
        );

        Thread worker =
                new Thread(
                        () -> executeSelectedTests(
                                browsers,
                                selectedTests
                        )
                );

        worker.setDaemon(
                true
        );

        worker.start();
    }

    private boolean validateConfiguration() {

        if (domainField
                .getText()
                .isBlank()) {

            showValidationError(
                    "Domain cannot be empty.",
                    domainField
            );

            return false;
        }

        for (int i = 0;
             i < userRows.size();
             i++) {

            UserRow row =
                    userRows.get(
                            i
                    );

            if (row
                    .extensionField
                    .getText()
                    .isBlank()) {

                showValidationError(
                        "User "
                                + (i + 1)
                                + " extension cannot be empty.",
                        row.extensionField
                );

                return false;
            }

            if (row
                    .passwordField
                    .getPassword()
                    .length == 0) {

                showValidationError(
                        "User "
                                + (i + 1)
                                + " password cannot be empty.",
                        row.passwordField
                );

                return false;
            }
        }

        if ((callsTest.isSelected()
                || callControlsTest.isSelected())
                && userRows.size() < 2) {

            showValidationError(
                    "Internal Calls and Call Controls require at least 2 users.",
                    null
            );

            return false;
        }

        if (chatsTest.isSelected()) {

            if (chatRecipientField
                    .getText()
                    .isBlank()) {

                showValidationError(
                        "Enter a Specific Recipient for Chats.",
                        chatRecipientField
                );

                return false;
            }

            try {

                int occurrence =
                        Integer.parseInt(
                                chatOccurrenceField
                                        .getText()
                                        .trim()
                        );

                if (occurrence < 1) {

                    showValidationError(
                            "Chat recipient occurrence must be 1 or greater.",
                            chatOccurrenceField
                    );

                    return false;
                }

            } catch (NumberFormatException e) {

                showValidationError(
                        "Chat recipient occurrence must be a number.",
                        chatOccurrenceField
                );

                return false;
            }
        }

        if (realCallTest.isSelected()
                && realCallNumberField
                .getText()
                .isBlank()) {

            showValidationError(
                    "Enter a destination number for Real Call.",
                    realCallNumberField
            );

            return false;
        }

        if (realSmsTest.isSelected()) {

            if (realSmsNumberField
                    .getText()
                    .isBlank()) {

                showValidationError(
                        "Enter a destination number for Real SMS.",
                        realSmsNumberField
                );

                return false;
            }

            if (realSmsMessageField
                    .getText()
                    .isBlank()) {

                showValidationError(
                        "Enter a message for Real SMS.",
                        realSmsMessageField
                );

                return false;
            }
        }

        return true;
    }

    private boolean confirmDangerousActions() {

        List<String> warnings =
                new ArrayList<>();

        if (chatCleanupTest.isSelected()) {

            warnings.add(
                    "Chat Cleanup will delete ALL chats for User 1."
            );
        }

        if (realCallTest.isSelected()) {

            warnings.add(
                    "Real Call will place a real call to "
                            + realCallNumberField
                            .getText()
                            .trim()
                            + "."
            );
        }

        if (realSmsTest.isSelected()) {

            warnings.add(
                    "Real SMS will send a real message to "
                            + realSmsNumberField
                            .getText()
                            .trim()
                            + "."
            );
        }

        if (warnings.isEmpty()) {
            return true;
        }

        String message =
                String.join(
                        System.lineSeparator(),
                        warnings
                )
                        + System.lineSeparator()
                        + System.lineSeparator()
                        + "Domain: "
                        + domainField
                        .getText()
                        .trim()
                        + System.lineSeparator()
                        + "Are you sure you want to continue?";

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        message,
                        "Confirm potentially destructive actions",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        return result
                == JOptionPane.YES_OPTION;
    }

    private void executeSelectedTests(
            List<String> browsers,
            List<String> selectedTests
    ) {

        try {

            for (String browser :
                    browsers) {

                appendGeneral(
                        "Starting "
                                + browser
                );

                executeBrowserRun(
                        browser,
                        selectedTests
                );
            }

            appendGeneral(
                    ""
            );

            appendGeneral(
                    "Selected test run completed."
            );

        } finally {

            runningProcess =
                    null;

            SwingUtilities.invokeLater(
                    () -> setRunnerEnabled(
                            true
                    )
            );
        }
    }

    private void executeBrowserRun(
            String browser,
            List<String> selectedTests
    ) {

        JTextArea browserLog =
                browserLogs.get(
                        browser
                );

        appendLog(
                browserLog,
                "========================================"
        );

        appendLog(
                browserLog,
                "Browser: "
                        + browser
        );

        appendLog(
                browserLog,
                "========================================"
        );

        try {

            File projectDirectory =
                    new File(
                            System.getProperty(
                                    "user.dir"
                            )
                    );

            List<String> command =
                    createGradleCommand(
                            browser,
                            selectedTests
                    );

            ProcessBuilder processBuilder =
                    new ProcessBuilder(
                            command
                    );

            processBuilder.directory(
                    projectDirectory
            );

            processBuilder.redirectErrorStream(
                    true
            );

            configureEnvironment(
                    processBuilder
            );

            runningProcess =
                    processBuilder.start();

            Path logFile =
                    currentRunDirectory.resolve(
                            browser
                                    + ".log"
                    );

            try (
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            runningProcess
                                                    .getInputStream(),
                                            StandardCharsets.UTF_8
                                    )
                            );

                    BufferedWriter writer =
                            Files.newBufferedWriter(
                                    logFile,
                                    StandardCharsets.UTF_8,
                                    StandardOpenOption.CREATE,
                                    StandardOpenOption.TRUNCATE_EXISTING,
                                    StandardOpenOption.WRITE
                            )
            ) {

                String line;

                while ((line =
                        reader.readLine())
                        != null) {

                    appendLog(
                            browserLog,
                            line
                    );

                    writer.write(
                            line
                    );

                    writer.newLine();

                    writer.flush();
                }
            }

            int exitCode =
                    runningProcess
                            .waitFor();

            if (exitCode == 0) {

                appendLog(
                        browserLog,
                        "BUILD SUCCESSFUL"
                );

                appendGeneral(
                        browser
                                + ": PASSED"
                );

            } else {

                appendLog(
                        browserLog,
                        "BUILD FAILED"
                );

                appendGeneral(
                        browser
                                + ": FAILED"
                );
            }

        } catch (Exception e) {

            appendLog(
                    browserLog,
                    "Runner error: "
                            + e.getMessage()
            );

            appendGeneral(
                    browser
                            + ": RUNNER ERROR"
            );
        }
    }

    private List<String> createGradleCommand(
            String browser,
            List<String> selectedTests
    ) {

        List<String> command =
                new ArrayList<>();

        if (isWindows()) {

            command.add(
                    "cmd.exe"
            );

            command.add(
                    "/c"
            );

            command.add(
                    "gradlew.bat"
            );

        } else {

            command.add(
                    "./gradlew"
            );
        }

        command.add(
                "clean"
        );

        command.add(
                "test"
        );

        command.add(
                "-Pbrowser="
                        + browser
        );

        for (String test :
                selectedTests) {

            command.add(
                    "--tests"
            );

            command.add(
                    test
            );
        }

        return command;
    }

    private void configureEnvironment(
            ProcessBuilder processBuilder
    ) {

        Map<String, String> environment =
                processBuilder.environment();

        clearRunnerEnvironment(
                environment
        );

        environment.put(
                "RINGOTEL_DOMAIN",
                domainField
                        .getText()
                        .trim()
        );

        environment.put(
                "RINGOTEL_USER_COUNT",
                Integer.toString(
                        userRows.size()
                )
        );

        for (int i = 0;
             i < userRows.size();
             i++) {

            UserRow row =
                    userRows.get(
                            i
                    );

            int index =
                    i + 1;

            environment.put(
                    "RINGOTEL_USER_"
                            + index,
                    row
                            .extensionField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_PASS_"
                            + index,
                    new String(
                            row
                                    .passwordField
                                    .getPassword()
                    )
            );
        }

        environment.put(
                "RINGOTEL_RUN_DIR",
                currentRunDirectory
                        .toAbsolutePath()
                        .toString()
        );

        environment.put(
                "RINGOTEL_RUN_ID",
                currentRunDirectory
                        .getFileName()
                        .toString()
        );

        if (chatsTest.isSelected()) {

            environment.put(
                    "RINGOTEL_CHAT_RECIPIENT",
                    chatRecipientField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_CHAT_OCCURRENCE",
                    chatOccurrenceField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_CHAT_MESSAGE",
                    chatMessageField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_CHAT_DELETE_AFTER_SEND",
                    Boolean.toString(
                            deleteChatAfterSend
                                    .isSelected()
                    )
            );
        }

        if (realCallTest.isSelected()) {

            environment.put(
                    "RINGOTEL_REAL_CALL_NUMBER",
                    realCallNumberField
                            .getText()
                            .trim()
            );
        }

        if (realSmsTest.isSelected()) {

            environment.put(
                    "RINGOTEL_REAL_SMS_NUMBER",
                    realSmsNumberField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_REAL_SMS_MESSAGE",
                    realSmsMessageField
                            .getText()
                            .trim()
            );
        }
    }

    private void clearRunnerEnvironment(
            Map<String, String> environment
    ) {

        List<String> keys =
                new ArrayList<>(
                        environment.keySet()
                );

        for (String key :
                keys) {

            if (key.matches(
                    "RINGOTEL_USER_\\d+"
            )
                    || key.matches(
                    "RINGOTEL_PASS_\\d+"
            )
                    || key.equals(
                    "RINGOTEL_USER_COUNT"
            )
                    || key.equals(
                    "RINGOTEL_CHAT_RECIPIENT"
            )
                    || key.equals(
                    "RINGOTEL_CHAT_OCCURRENCE"
            )
                    || key.equals(
                    "RINGOTEL_CHAT_MESSAGE"
            )
                    || key.equals(
                    "RINGOTEL_CHAT_DELETE_AFTER_SEND"
            )
                    || key.equals(
                    "RINGOTEL_REAL_CALL_NUMBER"
            )
                    || key.equals(
                    "RINGOTEL_REAL_SMS_NUMBER"
            )
                    || key.equals(
                    "RINGOTEL_REAL_SMS_MESSAGE"
            )
                    || key.equals(
                    "RINGOTEL_RUN_DIR"
            )
                    || key.equals(
                    "RINGOTEL_RUN_ID"
            )) {

                environment.remove(
                        key
                );
            }
        }
    }

    private List<String> getSelectedTestClasses() {

        List<String> selected =
                new ArrayList<>();

        for (
                Map.Entry<JCheckBox, String> entry :
                testClasses.entrySet()
        ) {

            if (entry
                    .getKey()
                    .isSelected()) {

                selected.add(
                        entry.getValue()
                );
            }
        }

        return selected;
    }

    private List<String> getSelectedBrowsers() {

        List<String> browsers =
                new ArrayList<>();

        if (chromeBrowser.isSelected()) {
            browsers.add("chrome");
        }

        if (firefoxBrowser.isSelected()) {
            browsers.add("firefox");
        }

        if (edgeBrowser.isSelected()) {
            browsers.add("edge");
        }

        if (braveBrowser.isSelected()) {
            browsers.add("brave");
        }

        if (operaBrowser.isSelected()) {
            browsers.add("opera");
        }

        if (safariBrowser.isSelected()
                && safariBrowser.isEnabled()) {

            browsers.add(
                    "safari"
            );
        }

        return browsers;
    }

    private void createRunDirectory() {

        try {

            Path root =
                    Path.of(
                            "runner_logs"
                    );

            Files.createDirectories(
                    root
            );

            currentRunDirectory =
                    root.resolve(
                            LocalDateTime
                                    .now()
                                    .format(
                                            LOG_FOLDER_FORMAT
                                    )
                    );

            Files.createDirectories(
                    currentRunDirectory
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Could not create runner log directory",
                    e
            );
        }
    }

    private void writeRunConfiguration(
            List<String> browsers,
            List<String> selectedTests
    ) {

        try {

            StringBuilder json =
                    new StringBuilder();

            json.append("{\n");

            json.append(
                    "  \"startedAt\": \""
            );

            json.append(
                    jsonEscape(
                            LocalDateTime
                                    .now()
                                    .toString()
                    )
            );

            json.append(
                    "\",\n"
            );

            json.append(
                    "  \"domain\": \""
            );

            json.append(
                    jsonEscape(
                            domainField
                                    .getText()
                                    .trim()
                    )
            );

            json.append(
                    "\",\n"
            );

            json.append(
                    "  \"users\": [\n"
            );

            for (int i = 0;
                 i < userRows.size();
                 i++) {

                json.append(
                        "    {\"index\": "
                                + (i + 1)
                                + ", \"extension\": \""
                                + jsonEscape(
                                userRows
                                        .get(i)
                                        .extensionField
                                        .getText()
                                        .trim()
                        )
                                + "\"}"
                );

                if (i
                        < userRows.size() - 1) {

                    json.append(
                            ","
                    );
                }

                json.append(
                        "\n"
                );
            }

            json.append(
                    "  ],\n"
            );

            appendJsonStringArray(
                    json,
                    "browsers",
                    browsers
            );

            json.append(
                    ",\n"
            );

            appendJsonStringArray(
                    json,
                    "tests",
                    selectedTests
            );

            json.append(
                    ",\n"
            );

            json.append(
                    "  \"chat\": {"
            );

            json.append(
                    "\"enabled\": "
                            + chatsTest.isSelected()
            );

            if (chatsTest.isSelected()) {

                json.append(
                        ", \"recipient\": \""
                                + jsonEscape(
                                chatRecipientField
                                        .getText()
                                        .trim()
                        )
                                + "\""
                );

                json.append(
                        ", \"occurrence\": "
                                + chatOccurrenceField
                                .getText()
                                .trim()
                );

                json.append(
                        ", \"deleteAfterSend\": "
                                + deleteChatAfterSend
                                .isSelected()
                );
            }

            json.append(
                    "},\n"
            );

            json.append(
                    "  \"realCallEnabled\": "
                            + realCallTest
                            .isSelected()
                            + ",\n"
            );

            json.append(
                    "  \"realSmsEnabled\": "
                            + realSmsTest
                            .isSelected()
                            + "\n"
            );

            json.append(
                    "}\n"
            );

            Files.writeString(
                    currentRunDirectory.resolve(
                            "run-config.json"
                    ),
                    json.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (Exception e) {

            appendGeneral(
                    "Could not write run-config.json: "
                            + e.getMessage()
            );
        }
    }

    private void appendJsonStringArray(
            StringBuilder json,
            String name,
            List<String> values
    ) {

        json.append(
                "  \""
                        + jsonEscape(
                        name
                )
                        + "\": ["
        );

        for (int i = 0;
             i < values.size();
             i++) {

            json.append(
                    "\""
                            + jsonEscape(
                            values.get(
                                    i
                            )
                    )
                            + "\""
            );

            if (i
                    < values.size() - 1) {

                json.append(
                        ", "
                );
            }
        }

        json.append(
                "]"
        );
    }

    private void exportLogs() {

        if (currentRunDirectory == null
                || !Files.exists(
                currentRunDirectory
        )) {

            appendGeneral(
                    "There are no runner logs to export yet."
            );

            return;
        }

        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select folder for exported logs"
        );

        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );

        int result =
                chooser.showSaveDialog(
                        this
                );

        if (result
                != JFileChooser.APPROVE_OPTION) {

            return;
        }

        File destination =
                chooser.getSelectedFile();

        try {

            Path exportFolder =
                    destination
                            .toPath()
                            .resolve(
                                    "RingotelTestRunner_"
                                            + LocalDateTime
                                            .now()
                                            .format(
                                                    LOG_FOLDER_FORMAT
                                            )
                            );

            Files.createDirectories(
                    exportFolder
            );

            try (
                    var paths =
                            Files.walk(
                                    currentRunDirectory
                            )
            ) {

                paths.forEach(
                        source -> {

                            try {

                                Path relative =
                                        currentRunDirectory
                                                .relativize(
                                                        source
                                                );

                                Path target =
                                        exportFolder.resolve(
                                                relative
                                        );

                                if (Files.isDirectory(
                                        source
                                )) {

                                    Files.createDirectories(
                                            target
                                    );

                                } else {

                                    Files.createDirectories(
                                            target.getParent()
                                    );

                                    Files.copy(
                                            source,
                                            target,
                                            StandardCopyOption.REPLACE_EXISTING
                                    );
                                }

                            } catch (Exception e) {

                                throw new RuntimeException(
                                        e
                                );
                            }
                        }
                );
            }

            appendGeneral(
                    "Logs and evidence exported to: "
                            + exportFolder
                            .toAbsolutePath()
            );

        } catch (Exception e) {

            appendGeneral(
                    "Could not export logs: "
                            + e.getMessage()
            );
        }
    }

    private void clearLogs() {

        generalLog.setText("");
        chromeLog.setText("");
        firefoxLog.setText("");
        edgeLog.setText("");
        braveLog.setText("");
        operaLog.setText("");
        safariLog.setText("");
    }

    private void setRunnerEnabled(
            boolean enabled
    ) {

        runSelectedButton.setEnabled(
                enabled
        );

        selectAllButton.setEnabled(
                enabled
        );

        clearSelectionButton.setEnabled(
                enabled
        );

        domainField.setEnabled(
                enabled
        );

        addUserButton.setEnabled(
                enabled
        );

        loginTest.setEnabled(
                enabled
        );

        contactsTest.setEnabled(
                enabled
        );

        presenceTest.setEnabled(
                enabled
        );

        chatsTest.setEnabled(
                enabled
        );

        messagingAuditTest.setEnabled(
                enabled
        );

        chatCleanupTest.setEnabled(
                enabled
        );

        realCallTest.setEnabled(
                enabled
        );

        realSmsTest.setEnabled(
                enabled
        );

        callsTest.setEnabled(
                enabled
                        && userRows.size() >= 2
        );

        callControlsTest.setEnabled(
                enabled
                        && userRows.size() >= 2
        );

        chromeBrowser.setEnabled(
                enabled
        );

        firefoxBrowser.setEnabled(
                enabled
        );

        edgeBrowser.setEnabled(
                enabled
        );

        braveBrowser.setEnabled(
                enabled
        );

        operaBrowser.setEnabled(
                enabled
        );

        safariBrowser.setEnabled(
                enabled
                        && isMac()
        );

        for (int i = 0;
             i < userRows.size();
             i++) {

            UserRow row =
                    userRows.get(
                            i
                    );

            row.extensionField.setEnabled(
                    enabled
            );

            row.passwordField.setEnabled(
                    enabled
            );

            row.removeButton.setEnabled(
                    enabled
                            && i > 0
            );
        }

        updateConditionalPanels();
    }

    private void showValidationError(
            String message,
            Component component
    ) {

        appendGeneral(
                message
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Configuration problem",
                JOptionPane.ERROR_MESSAGE
        );

        if (component != null) {

            component.requestFocusInWindow();
        }
    }

    private void appendGeneral(
            String text
    ) {

        appendLog(
                generalLog,
                text
        );
    }

    private void appendLog(
            JTextArea area,
            String text
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    area.append(
                            text
                                    + System.lineSeparator()
                    );

                    area.setCaretPosition(
                            area
                                    .getDocument()
                                    .getLength()
                    );
                }
        );
    }

    private boolean isWindows() {

        return System
                .getProperty(
                        "os.name",
                        ""
                )
                .toLowerCase()
                .contains(
                        "win"
                );
    }

    private boolean isMac() {

        return System
                .getProperty(
                        "os.name",
                        ""
                )
                .toLowerCase()
                .contains(
                        "mac"
                );
    }

    private String jsonEscape(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\r",
                        "\\r"
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\t",
                        "\\t"
                );
    }

    private static JTextArea createLogArea() {

        JTextArea area =
                new JTextArea();

        area.setEditable(
                false
        );

        area.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        12
                )
        );

        return area;
    }

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    RingotelTestRunnerApp app =
                            new RingotelTestRunnerApp();

                    app.setVisible(
                            true
                    );
                }
        );
    }

    private class UserRow {

        private final JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        private final JTextField extensionField =
                new JTextField(
                        20
                );

        private final JPasswordField passwordField =
                new JPasswordField(
                        20
                );

        private final JButton removeButton =
                new JButton(
                        "Remove"
                );

        private UserRow(
                String extension
        ) {

            extensionField.setText(
                    extension
            );

            GridBagConstraints gbc =
                    createGridBagConstraints();

            addField(
                    panel,
                    gbc,
                    0,
                    "Extension",
                    extensionField
            );

            addField(
                    panel,
                    gbc,
                    1,
                    "Password",
                    passwordField
            );

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.weightx = 1;

            panel.add(
                    removeButton,
                    gbc
            );

            removeButton.addActionListener(
                    e -> removeUserRow(
                            this
                    )
            );
        }
    }
}