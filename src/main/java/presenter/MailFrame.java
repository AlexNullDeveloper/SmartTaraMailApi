package presenter;

/**
 * @author on 06.07.2016.
 */

import ApplicationExceptions.AllreadyInFileException;
import ApplicationExceptions.CantMakeDirsException;
import controller.MailAdder;
import mailsender.EmailSender;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MailFrame extends JFrame {
    private JButton addInFileButton;
    private JButton sendMailButton;
    private JButton exitButton;
    private JTextField mailTextField;
    private boolean showingSendingButton;

    public JComboBox getCheckBoxEmails() {
        return checkBoxEmails;
    }

    private JComboBox checkBoxEmails;

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    private ButtonGroup buttonGroup;

    public MailFrame(String nameOfFrame, boolean showSendingButton) {
        super(nameOfFrame);
        showingSendingButton = showSendingButton;
        init();
        setBounds(400, 200, 640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void init() {

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
//        centerPanel.setLayout(new FlowLayout());
        centerPanel.setBorder(new EmptyBorder(100, 20, 20, 100));

        JLabel mailLabel = new JLabel("Новая почта");
        mailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mailTextField = new JTextField(20);
        centerPanel.add(mailLabel);
        centerPanel.add(mailTextField);

        JLabel testEmailLabel = new JLabel("Тестовая почта");
        testEmailLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        String[] testEmailsListing = {
                "talismanoff1990@yandex.ru",
                "mslide@mail.ru"
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

            if (!StringUtils.isBlank(mailTextField.getText())) {

                try {
                    if (MailAdder.addMailIntoFile(mailTextField.getText().trim())) {
                        JOptionPane.showMessageDialog(MailFrame.this, "почта успешно добавлена в файл",
                                "Успех", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MailFrame.this, "ошибка при добавлении почты" + mailTextField.getText(),
                                "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (AllreadyInFileException e) {
                    JOptionPane.showMessageDialog(MailFrame.this, "ошибка! данная почта "
                                    + mailTextField.getText() + " уже есть в файле",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (CantMakeDirsException e) {
                    JOptionPane.showMessageDialog(MailFrame.this, "ошибка! неудалось создать создать используемый в программе путь",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(MailFrame.this, "Введите почту для добавления",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });


        sendMailButton = new JButton("отправить почту");
        southPanel.add(sendMailButton);
        sendMailButton.addActionListener(ae -> {
            if (radioButtonTesting.isSelected()) {
                EmailSender.sendTestEmail(this);
            } else {
                EmailSender.sendRealEmail(this);
            }
        });

        exitButton = new JButton("Выход");
        southPanel.add(exitButton);
        exitButton.addActionListener(ae -> dispose());
        if (!showingSendingButton) {
            sendMailButton.hide();
        }

        getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

}
