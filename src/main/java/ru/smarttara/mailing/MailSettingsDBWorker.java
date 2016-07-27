package ru.smarttara.mailing;

import ru.smarttara.mainFrame.MainFrame;
import ru.smarttara.mainFrame.Parameters;
import ru.smarttara.util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by ZXCASD on 27.07.2016.
 */
public class MailSettingsDBWorker {

    private MailSettingsDBWorker() {
    }

    static void populateFieldsWithData(MailSettingsFrame mailSettingsFrame) {
        Map<String, String> parametersMap = MainFrame.getApplicationParametersMap();
        Connection connection = JdbcHelper.getConnection();
        String sql = "SELECT PARAM_TEXT_VALUE FROM APP_DATA WHERE PARAM_NAME = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,Parameters.EMAIL_HEADER_PARAM);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String valueOfEmailHeader = resultSet.getString(1);
                mailSettingsFrame.setMailSubjectTextFieldString(valueOfEmailHeader);
                parametersMap.put(Parameters.EMAIL_HEADER_PARAM, valueOfEmailHeader);
            }

            preparedStatement.setString(1, Parameters.MAIL_TEXT_PARAM);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String valueOfMailBody = resultSet.getString(1);
                mailSettingsFrame.setTextOfMailTextAreaString(valueOfMailBody);
                parametersMap.put(Parameters.MAIL_TEXT_PARAM,valueOfMailBody);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
