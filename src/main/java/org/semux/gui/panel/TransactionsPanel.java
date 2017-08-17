package org.semux.gui.panel;

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TransactionsPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;

    private String[] columnNames = { "Hash", "Type", "From", "To", "Value", "Time" };

    private Object[][] data = { { "0x11223344", "TRANSFER", "0x11223344", "0x11223344", new Integer(1), new Date() } };

    public TransactionsPanel() {
        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane);

        table = new JTable(data, columnNames);
        scrollPane.setViewportView(table);
    }
}