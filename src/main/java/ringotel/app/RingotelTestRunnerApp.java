package ringotel.app;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RingotelTestRunnerApp
        extends JFrame {

    private final JTextField domainField =
            new JTextField(
                    "testwebsoftphone",
                    20
            );

    private final JTextField user1Field =
            new JTextField(
                    "4321",
                    20
            );

    private final JPasswordField pass1Field =
            new JPasswordField(
                    20
            );

    private final JTextField user2Field =
            new JTextField(
                    "1234",
                    20
            );

    private final JPasswordField pass2Field =
            new JPasswordField(
                    20
            );

    private final JTextArea logArea =
            new JTextArea();

    private final JButton loginButton =
            new JButton(
                    "Login"
            );

    private final JButton callsButton =
            new JButton(
                    "Calls"
            );

    private final JButton callControlsButton =
            new JButton(
                    "Call Controls"
            );

    private final JButton contactsButton =
            new JButton(
                    "Contacts"
            );

    private final JButton chatsButton =
            new JButton(
                    "Chats"
            );

    private final JButton presenceButton =
            new JButton(
                    "Presence"
            );

    private final JButton runAllButton =
            new JButton(
                    "RUN ALL"
            );

    private final JButton clearLogButton =
            new JButton(
                    "Clear Log"
            );

    private volatile Process runningProcess;

    public RingotelTestRunnerApp() {

        super(
                "Ringotel Test Runner"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setMinimumSize(
                new Dimension(
                        900,
                        700
                )
        );

        setLocationRelativeTo(
                null
        );

        createUi();

        bindActions();
    }

    private void createUi() {

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
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
                        24
                )
        );

        root.add(
                title,
                BorderLayout.NORTH
        );

        JPanel configurationPanel =
                new JPanel(
                        new GridBagLayout()
                );

        configurationPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Test configuration"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        6,
                        6,
                        6
                );

        gbc.anchor =
                GridBagConstraints.WEST;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        addField(
                configurationPanel,
                gbc,
                0,
                "Domain",
                domainField
        );

        addField(
                configurationPanel,
                gbc,
                1,
                "Extension 1",
                user1Field
        );

        addField(
                configurationPanel,
                gbc,
                2,
                "Password 1",
                pass1Field
        );

        addField(
                configurationPanel,
                gbc,
                3,
                "Extension 2",
                user2Field
        );

        addField(
                configurationPanel,
                gbc,
                4,
                "Password 2",
                pass2Field
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttons.add(
                loginButton
        );

        buttons.add(
                contactsButton
        );

        buttons.add(
                presenceButton
        );

        buttons.add(
                chatsButton
        );

        buttons.add(
                callsButton
        );

        buttons.add(
                callControlsButton
        );

        buttons.add(
                runAllButton
        );

        buttons.add(
                clearLogButton
        );

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        center.add(
                configurationPanel,
                BorderLayout.NORTH
        );

        center.add(
                buttons,
                BorderLayout.CENTER
        );

        root.add(
                center,
                BorderLayout.CENTER
        );

        logArea.setEditable(
                false
        );

        logArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        logArea
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        850,
                        350
                )
        );

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Test log"
                )
        );

        root.add(
                scrollPane,
                BorderLayout.SOUTH
        );

        setContentPane(
                root
        );

        pack();
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

        loginButton.addActionListener(
                e -> runTest(
                        "LoginTests",
                        "ringotel.tests.LoginTests"
                )
        );

        contactsButton.addActionListener(
                e -> runTest(
                        "ContactTests",
                        "ringotel.tests.ContactTests"
                )
        );

        presenceButton.addActionListener(
                e -> runTest(
                        "PresenceTests",
                        "ringotel.tests.PresenceTests"
                )
        );

        chatsButton.addActionListener(
                e -> runTest(
                        "ChatTests",
                        "ringotel.tests.ChatTests"
                )
        );

        callsButton.addActionListener(
                e -> runTest(
                        "CallTests",
                        "ringotel.tests.CallTests"
                )
        );

        callControlsButton.addActionListener(
                e -> runTest(
                        "CallControlsTests",
                        "ringotel.tests.CallControlsTests"
                )
        );

        runAllButton.addActionListener(
                e -> runTest(
                        "All Tests",
                        null
                )
        );

        clearLogButton.addActionListener(
                e -> logArea.setText(
                        ""
                )
        );
    }

    private void runTest(
            String displayName,
            String testClass
    ) {

        if (runningProcess != null
                && runningProcess.isAlive()) {

            appendLog(
                    "Another test is already running."
            );

            return;
        }

        if (!validateInput()) {
            return;
        }

        setButtonsEnabled(
                false
        );

        appendLog(
                ""
        );

        appendLog(
                "========================================"
        );

        appendLog(
                "Starting: "
                        + displayName
        );

        appendLog(
                "Domain: "
                        + domainField
                        .getText()
                        .trim()
        );

        appendLog(
                "Extension 1: "
                        + user1Field
                        .getText()
                        .trim()
        );

        appendLog(
                "Extension 2: "
                        + user2Field
                        .getText()
                        .trim()
        );

        appendLog(
                "========================================"
        );

        Thread worker =
                new Thread(
                        () -> executeGradle(
                                displayName,
                                testClass
                        )
                );

        worker.setDaemon(
                true
        );

        worker.start();
    }

    private boolean validateInput() {

        if (domainField
                .getText()
                .isBlank()) {

            appendLog(
                    "Domain cannot be empty."
            );

            return false;
        }

        if (user1Field
                .getText()
                .isBlank()) {

            appendLog(
                    "Extension 1 cannot be empty."
            );

            return false;
        }

        if (pass1Field
                .getPassword()
                .length == 0) {

            appendLog(
                    "Password 1 cannot be empty."
            );

            return false;
        }

        return true;
    }

    private void executeGradle(
            String displayName,
            String testClass
    ) {

        try {

            File projectDirectory =
                    new File(
                            System.getProperty(
                                    "user.dir"
                            )
                    );

            File gradleWrapper =
                    new File(
                            projectDirectory,
                            "gradlew.bat"
                    );

            if (!gradleWrapper.exists()) {

                appendLog(
                        "gradlew.bat was not found in:"
                );

                appendLog(
                        projectDirectory
                                .getAbsolutePath()
                );

                return;
            }

            ProcessBuilder processBuilder;

            if (testClass == null) {

                processBuilder =
                        new ProcessBuilder(
                                "cmd.exe",
                                "/c",
                                "gradlew.bat",
                                "clean",
                                "test"
                        );

            } else {

                processBuilder =
                        new ProcessBuilder(
                                "cmd.exe",
                                "/c",
                                "gradlew.bat",
                                "clean",
                                "test",
                                "--tests",
                                testClass
                        );
            }

            processBuilder.directory(
                    projectDirectory
            );

            processBuilder.redirectErrorStream(
                    true
            );

            Map<String, String> environment =
                    processBuilder.environment();

            environment.put(
                    "RINGOTEL_DOMAIN",
                    domainField
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_USER_1",
                    user1Field
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_PASS_1",
                    new String(
                            pass1Field
                                    .getPassword()
                    )
            );

            environment.put(
                    "RINGOTEL_USER_2",
                    user2Field
                            .getText()
                            .trim()
            );

            environment.put(
                    "RINGOTEL_PASS_2",
                    new String(
                            pass2Field
                                    .getPassword()
                    )
            );

            runningProcess =
                    processBuilder.start();

            try (
                    BufferedReader reader =
                            new BufferedReader(
                                    new InputStreamReader(
                                            runningProcess
                                                    .getInputStream(),
                                            StandardCharsets.UTF_8
                                    )
                            )
            ) {

                String line;

                while ((line =
                        reader.readLine())
                        != null) {

                    appendLog(
                            line
                    );
                }
            }

            int exitCode =
                    runningProcess
                            .waitFor();

            appendLog(
                    ""
            );

            if (exitCode == 0) {

                appendLog(
                        "PASSED: "
                                + displayName
                );

            } else {

                appendLog(
                        "FAILED: "
                                + displayName
                );
            }

            appendLog(
                    "Exit code: "
                            + exitCode
            );

        } catch (Exception e) {

            appendLog(
                    "Application error: "
                            + e.getMessage()
            );

        } finally {

            runningProcess =
                    null;

            SwingUtilities.invokeLater(
                    () -> setButtonsEnabled(
                            true
                    )
            );
        }
    }

    private void appendLog(
            String text
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    logArea.append(
                            text
                                    + System.lineSeparator()
                    );

                    logArea.setCaretPosition(
                            logArea
                                    .getDocument()
                                    .getLength()
                    );
                }
        );
    }

    private void setButtonsEnabled(
            boolean enabled
    ) {

        loginButton.setEnabled(
                enabled
        );

        contactsButton.setEnabled(
                enabled
        );

        presenceButton.setEnabled(
                enabled
        );

        chatsButton.setEnabled(
                enabled
        );

        callsButton.setEnabled(
                enabled
        );

        callControlsButton.setEnabled(
                enabled
        );

        runAllButton.setEnabled(
                enabled
        );
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
}