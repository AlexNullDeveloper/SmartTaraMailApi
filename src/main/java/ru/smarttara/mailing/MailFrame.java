package ru.smarttara.mailing;

import ru.smarttara.appexceptions.AllreadyInFileException;
import ru.smarttara.appexceptions.CantMakeDirsException;
import ru.smarttara.appexceptions.DublicateMailException;
import ru.smarttara.launcher.Launcher;
import ru.smarttara.mainFrame.MainFrame;
import ru.smarttara.util.ProcessFrame;
import ru.smarttara.workers.MailFileWorker;
import ru.smarttara.workers.MailDBWorker;
import ru.smarttara.mailsender.EmailSender;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author a.talismanov on 06.07.2016.
 */
public class MailFrame extends JFrame {
    private JButton addInFileButton;
    private JButton sendMailButton;
    private JButton exitButton;
    private JTextField mailTextField;
    private JButton addInDBButton;
    private JTextField companyTextField;
    private static Dimension screen;
    private static final int windowHeight;
    private static final int windowWidth;
    private ButtonGroup buttonGroup;
    private JComboBox checkBoxEmails;

    static {
        screen = MainFrame.getScreen();
        windowHeight = screen.height * 45 / 100;
        windowWidth = screen.width * 34 / 100;
    }

    public JComboBox getCheckBoxEmails() {
        return checkBoxEmails;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public MailFrame(String nameOfFrame) {
        super(nameOfFrame);
        init();

        setBounds(MainFrame.getLeftPointX(), MainFrame.getTopPointY(), windowWidth, windowHeight);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void init() {

        Launcher.logger.debug("initializing components of ru.smarttara.mainFrame");
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JRadioButton radioButtonTesting = new JRadioButton("Тестовая отправка");
        radioButtonTesting.setSelected(true);
        JRadioButton radioButtonReal = new JRadioButton("Боевая отправка");
        topPanel.add(radioButtonTesting);
        topPanel.add(radioButtonReal);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonTesting);
        buttonGroup.add(radioButtonReal);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(10, 2, 5, 5));
        //отступы слева справа по 100
        centerPanel.setBorder(new EmptyBorder(100, 20, 20, 100));

        JLabel mailLabel = new JLabel("Новая почта");
        mailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mailTextField = new JTextField(20);
        centerPanel.add(mailLabel);
        centerPanel.add(mailTextField);

        JLabel companyLabel = new JLabel("Название компании");
        companyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        companyTextField = new JTextField(20);
        centerPanel.add(companyLabel);
        centerPanel.add(companyTextField);


        JLabel testEmailLabel = new JLabel("Тестовая почта");
        testEmailLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        //TODO убрать в базу
        String[] testEmailsListing = {
                "talismanoff1990@yandex.ru",
                "mslide@mail.ru",
                "smarttara@yandex.ru",
                "alexandr.shagin@gmail.com"
        };
        checkBoxEmails = new JComboBox<>(testEmailsListing);

        centerPanel.add(testEmailLabel);
        centerPanel.add(checkBoxEmails);

        //декорация для нормального отображения
        for (int i = 0; i < 8; i++) {
            centerPanel.add(new JLabel());
        }

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        addInFileButton = new JButton("добавить в файл");
        southPanel.add(addInFileButton);
        addInFileButton.addActionListener(ae -> {
            Launcher.logger.debug("addInFileButton pressed");

            if (!StringUtils.isBlank(mailTextField.getText())) {

                try {
                    if (MailFileWorker.addMailIntoFile(mailTextField.getText().trim())) {
                        JOptionPane.showMessageDialog(MailFrame.this, "почта успешно добавлена в файл",
                                "Успех", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MailFrame.this, "ошибка при добавлении почты" + mailTextField.getText(),
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (AllreadyInFileException e) {
                    Launcher.logger.error("ошибка! данная почта "
                            + mailTextField.getText() + " уже есть в файле", e);
                    JOptionPane.showMessageDialog(MailFrame.this, "ошибка! данная почта "
                                    + mailTextField.getText() + " уже есть в файле",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (CantMakeDirsException e) {
                    Launcher.logger.fatal("ошибка! неудалось создать создать используемый в программе путь", e);
                    JOptionPane.showMessageDialog(MailFrame.this, "ошибка! неудалось создать создать используемый в программе путь",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(MailFrame.this, "Введите почту для добавления",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        addInDBButton = new JButton("записать в базу");
        southPanel.add(addInDBButton);
        addInDBButton.addActionListener(ae -> {
            if (!StringUtils.isBlank(mailTextField.getText())) {
                try {
                    MailDBWorker.addMailInDataBase(mailTextField.getText().trim(),
                            companyTextField.getText().trim());
                    JOptionPane.showMessageDialog(MailFrame.this, "почта успешно записана в базу",
                            "Успех", JOptionPane.INFORMATION_MESSAGE);
                } catch (DublicateMailException e) {
                    Launcher.logger.error("Почта дублируется", e);
                    JOptionPane.showMessageDialog(MailFrame.this, "Почта дублируется",
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(MailFrame.this, "Введите почту для добавления",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });


        sendMailButton = new JButton("отправить почту");
        southPanel.add(sendMailButton);
        sendMailButton.addActionListener(ae -> {
            ProcessFrame processFrame = new ProcessFrame("Процесс выполнения");

            //TODO обдумать некорректное завершение методов и соответствующие JOptionPane
            if (radioButtonTesting.isSelected()) {
                EmailSender.sendTestEmail(this);
                JOptionPane.showMessageDialog(MailFrame.this, "Тестовое сообщение успешно отправлено",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);
            } else {
                new Thread(()->EmailSender.sendRealEmail(this, processFrame)).start();
            }
        });

        exitButton = new JButton("Выход");
        southPanel.add(exitButton);
        exitButton.addActionListener(ae -> dispose());

        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }



}
