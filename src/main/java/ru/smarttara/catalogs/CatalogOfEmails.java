package ru.smarttara.catalogs;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Фрейм для реестра документов
 *
 * @author aleksander_talismanov
 * @version alpha 1.00 11.03.2016
 */
public class CatalogOfEmails extends JFrame {

    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 450;

    private static JTable jTableEmails = null;
    private static JPanel pane;
    private static TableColumnModel tableColumnModel = null;
    private static DefaultTableModel tableModelEmail = null;
    private static JScrollPane jscrollPane = null;

    //

    // здесь мы будем хранить названия столбцов
    private static Vector<String> columnNames;
    // список типов столбцов
    private static Vector<Object> columnTypes = new Vector<Object>();
    // хранилище для полученных данных из базы данных
    private static Vector<Vector<Object>> data;

    private static DefaultTableModel tableModel;

    private JButton buttonDelete;

    public static DefaultTableModel getTableModel() {
        return tableModel;
    }

    public static JScrollPane getJScrollPane() {
        return jscrollPane;
    }


    /**
     * конструктор фрейма реестра документов
     */
    public CatalogOfEmails(String nameOfFrame) {
        super(nameOfFrame);
        //берем размеры скрина на будущее

        pane = (JPanel) getContentPane();
        setResizable(false);
        pack();


        JPanel centerPanel = new JPanel();
        pane.add(centerPanel, BorderLayout.CENTER);

        //скролл с табличкой центральная группа растянутая по всю ширину
        jscrollPane = makeJScrollPane();


        //баг jscrollPane.getVerticalScrollBar().addAdjustmentListener(new MyAdjustmentListener());
//        jscrollPane.setBounds(2,25,790,200);
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
     * Делаем модель таблички EMAILS
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

    // получение данных из объекта ResultSet
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

    public static void main(String[] args) {
        CatalogOfEmails catalogOfEmails = new CatalogOfEmails("Каталог электронный почт");
    }
}