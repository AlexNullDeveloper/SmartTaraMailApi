package ru.smarttara.catalogs;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * class that represents presenter for catalog of emails
 *
 * @author aleksander_talismanov
 * @version alpha 1.00 11.03.2016
 */
public class CatalogOfEmails extends JFrame {
    //TODO make elastic width and height

    /**
     * width of frame
     */
    private static final int FRAME_WIDTH = 800;

    /**
     * height of frame
     */
    private static final int FRAME_HEIGHT = 450;

    /**
     * table of frame
     */
    private static JTable jTableEmails = null;
    /**
     * main JPanel of frame
     */
    private static JPanel pane;

    /**
     * table model for emails
     */
    private static DefaultTableModel tableModelEmail = null;

    /**
     * scroll pane for frame
     */
    private static JScrollPane jscrollPane = null;

    /**
     * names of columns
     */
    private static Vector<String> columnNames;

    /**
     * list of types of columns
     */
    private static Vector<Object> columnTypes = new Vector<Object>();

    /**
     * data stored from database
     */
    private static Vector<Vector<Object>> data;

    /**
     * Constructor
     * конструктор фрейма реестра документов
     * @param nameOfFrame title for frame
     */
    public CatalogOfEmails(String nameOfFrame) {
        super(nameOfFrame);
        //TODO сделать размеры скрина

        pane = (JPanel) getContentPane();
        setResizable(false);
        pack();

        JPanel centerPanel = new JPanel();
        pane.add(centerPanel, BorderLayout.CENTER);

        //скролл с табличкой центральная группа растянутая по всю ширину
        jscrollPane = makeJScrollPane();

        centerPanel.add(jscrollPane);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JButton("заглушка1"));
        topPanel.add(new JButton("заглушка2"));
        topPanel.add(new JButton("заглушка3"));
        pane.add(topPanel, BorderLayout.NORTH);

        //размер фрейма
        setBounds(500, 250, FRAME_WIDTH, FRAME_HEIGHT);
        setVisible(true);

    }

    /**
     * делаем скролл для панели в него добавляем табличку
     */

    public static JScrollPane makeJScrollPane() {
        ResultSet resultSet = CatalogOfEmailsDB.getResultSetFromTableEmails();

        jTableEmails = new JTable(buildTableModelEmail(resultSet));

        //Таблица с выравниванием по ширине
        if (tableModelEmail == null) {
            tableModelEmail = buildTableModelEmail(resultSet);
        }

        if (jTableEmails == null) {
            jTableEmails = new JTable(tableModelEmail) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component component = super.prepareRenderer(renderer, row, column);
                    int rendererWidth = component.getPreferredSize().width;
                    TableColumn tableColumn = getColumnModel().getColumn(column);
                    tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                    return component;
                }
            };
        }

        jTableEmails.setPreferredScrollableViewportSize(new Dimension(700, 250));
        JScrollPane jscrlp = new JScrollPane(jTableEmails) {

            @Override
            public boolean isVisible() {
                return true;
            }

        };
        return jscrlp;
    }

    /**
     * Builds tablemodel from resultset
     *
     * @param rs ResultSet from which populates DefaultTableModel for JTable
     */
    public static DefaultTableModel buildTableModelEmail(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // data of the table
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new DefaultTableModel(data, columnNames);
    }

    /**
     * Sets data source
     *
     * @param rs resultset which helps to revalidate and repaint content
     */
    public static void setDataSource(ResultSet rs) throws Exception {

        System.out.println("setDataSource method");

        // удаляем прежние данные
        data.clear();
        columnNames.clear();
        columnTypes.clear();

        // получаем вспомогательную информацию о столбцах
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            // тип столбца
            Class<?> type = Class.forName(rsmd.getColumnClassName(i + 1));
            columnTypes.add(type);
        }
        // получаем данные

        while (rs.next()) {
            // здесь будем хранить ячейки одной строки
            Vector<Object> row = new Vector<Object>();
            for (int i = 0; i < columnCount; i++) {
                if (columnTypes.get(i) == String.class)
                    row.add(rs.getString(i + 1));
                else
                    row.add(rs.getObject(i + 1));
            }
            synchronized (data) {
                data.add(row);
            }
        }

        tableModelEmail = new DefaultTableModel(data, columnNames) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 6 || column == 7) {
                    return BigDecimal.class;
                }
                return Object.class;
            }
        };


        jTableEmails = new JTable(tableModelEmail);
        jscrollPane = new JScrollPane(jTableEmails) {
            @Override
            public boolean isVisible() {
                return true;
            }

        };


        pane.add(jscrollPane);

        jTableEmails.revalidate();
        jTableEmails.repaint();

        pane.repaint();

        // сообщаем об изменениях в структуре данных
        tableModelEmail.fireTableStructureChanged();

        System.out.println("fireTableStructureChanged()");
        jscrollPane.revalidate();
        jscrollPane.repaint();
        jscrollPane.setVisible(true);
        jscrollPane.revalidate();
        pane.repaint();
        jTableEmails.revalidate();
        jscrollPane.setVisible(true);
    }

    /**
     * testing frame individually
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CatalogOfEmails("Каталог электронный почт");
        });
    }
}