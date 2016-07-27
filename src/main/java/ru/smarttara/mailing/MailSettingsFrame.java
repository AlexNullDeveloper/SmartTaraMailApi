package ru.smarttara.mailing;

import ru.smarttara.mainFrame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by ZXCASD on 27.07.2016.
 */
public class MailSettingsFrame extends JFrame {
    private static final int windowWidth;
    private static final int windowHeight;
    private JPanel panel;
    private JTextField mailSubjectTextField;
    private JTextArea textOfMailTextArea;
    private static int timesLoaded;

    static {
        Dimension screen = MainFrame.getScreen();
        windowHeight = screen.height * 45 / 100;
        windowWidth = screen.width * 34 / 100;
    }

    public MailSettingsFrame(String title) {
        super(title);

        timesLoaded++;
        System.out.println(timesLoaded);
        init();

        setBounds(MainFrame.getLeftPointX(), MainFrame.getTopPointY(), windowWidth, windowHeight);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);

    }

    public JTextField getMailSubjectTextField() {
        return mailSubjectTextField;
    }

    public void setMailSubjectTextFieldString(String mailSubjectString) {
        this.mailSubjectTextField.setText(mailSubjectString);
    }

    public JTextArea getTextOfMailTextArea() {
        return textOfMailTextArea;
    }

    public void setTextOfMailTextAreaString(String textOfMailString) {
        this.textOfMailTextArea.setText(textOfMailString);
    }

    private void init() {
        panel = (JPanel) this.getContentPane();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        topPanel.add(new Label(""));
//        topPanel.add(new JButton("будущее"));
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel topOfCenterPanel = new JPanel();
        topOfCenterPanel.setLayout(new FlowLayout());
        topOfCenterPanel.add(new JLabel("Тема письма"));
        mailSubjectTextField = new JTextField(65);
        topOfCenterPanel.add(mailSubjectTextField);

        JPanel centerOfCenterPanel = new JPanel();
        centerOfCenterPanel.setLayout(new GridLayout(1,2,10,10));

        JLabel textOfMailLabel = new JLabel("Текст письма");
        textOfMailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        centerOfCenterPanel.add(textOfMailLabel);
        textOfMailTextArea = new JTextArea(20, 40);
        textOfMailTextArea.setFont(new Font("Times New Roman",Font.BOLD,12));
        JScrollPane scrollPane = new JScrollPane(textOfMailTextArea);
        centerOfCenterPanel.add(scrollPane);
//        centerPanel.setBorder(new EmptyBorder(100, 100, 100, 100));

        centerPanel.add(topOfCenterPanel, BorderLayout.NORTH);
        centerPanel.add(centerOfCenterPanel,BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton saveButton = new JButton("Сохранить");
        //TODO проверять на то, что поля были изменены по успеху окошко поднимать
        saveButton.addActionListener(ae -> {
            MailSettingsDBWorker.saveSettingsInDB(this);
            JOptionPane.showMessageDialog(MailSettingsFrame.this, "новые параметры письма успешно сохранены в базу",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
        });
        bottomPanel.add(saveButton);

        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(ae -> dispose());
        bottomPanel.add(exitButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        if (timesLoaded == 1) {
            Map<String, String> map = MainFrame.getApplicationParametersMap();
            setMailSubjectTextFieldString(map.get("Заголовок письма"));
            setTextOfMailTextAreaString(map.get("Текст письма"));
        } else {
            MailSettingsDBWorker.populateFieldsWithData(this);
        }
    }
}
