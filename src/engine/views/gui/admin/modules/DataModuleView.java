
package engine.views.gui.admin.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import engine.models.Role;
import engine.views.GUIView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public abstract class DataModuleView extends GUIView implements ActionListener
{
    protected JTable dataTable;
    protected DefaultTableModel dataModel;
    protected JPanel header;
    protected JPanel tableWrapper;
    protected JPanel dataControls;
    protected JButton add, remove, edit;
    protected JLabel statusLabel;
    protected String[] columnHeaders;
    protected String[] columnNames;
    
    
    @Override
    protected void initComponents() 
    {
        panel                   =   new JPanel(new BorderLayout());
        dataControls            =   new JPanel();
        dataModel               =   new DefaultTableModel();
        dataTable               =   new JTable(dataModel);
        add                     =   new JButton("Add");
        remove                  =   new JButton("Remove");
        edit                    =   new JButton("Edit");
        statusLabel             =   new JLabel();
        header           =   new JPanel(new GridLayout(2, 1));
        tableWrapper     =   new JPanel();   
        JPanel statusWrapper    =   new JPanel();
        
        add.setIcon(new ImageIcon(addSmallImage));
        remove.setIcon(new ImageIcon(removeSmallImage));
        edit.setIcon(new ImageIcon(editSmallImage));
        
        dataControls.add(add);
        dataControls.add(remove);
        dataControls.add(edit);
        statusWrapper.add(statusLabel);
        header.add(dataControls);
        header.add(statusWrapper);
        
        initColumns();
        setColumns();
        
        JScrollPane tableScroller               =   new JScrollPane(dataTable);
        tableWrapper.add(tableScroller);
        
        tableWrapper.setPreferredSize(new Dimension(390, 280));
        tableScroller.setPreferredSize(new Dimension(390, 280));
        header.setPreferredSize(new Dimension(1, 100));
        
        dataControls.setBackground(Color.WHITE);
        tableWrapper.setBackground(Color.WHITE);
        panel.setBackground(Color.WHITE);
        header.setBackground(Color.WHITE);
        statusWrapper.setBackground(Color.WHITE);
        tableScroller.setBackground(Color.WHITE);
        dataTable.setBackground(Color.WHITE);
        
        panel.add(header, BorderLayout.NORTH);
        panel.add(tableWrapper, BorderLayout.CENTER);
        loadData();
    }

    @Override
    protected void initResources()
    {
    }

    @Override
    protected void initListeners() 
    {
        add.addActionListener(this);
        remove.addActionListener(this);
        edit.addActionListener(this);
    }
    
    protected abstract JsonArray getData();
    
    protected abstract void add();
    
    protected abstract void remove();
    
    protected abstract void edit();
   
    protected abstract void initColumns();
    
    protected void loadData()
    {
        JsonArray results   =   getData();
        if(results != null && results.size() > 0)
        {
            SwingUtilities.invokeLater(()->
            {
                dataModel.setRowCount(0);
                for(int i = 1; i < results.size(); i++)
                {
                    JsonObject jObj     =   results.get(i).getAsJsonObject();
                    dataModel.addRow(getDataFromResults(jObj));
                }
            });
        }
    }
    
    protected Object[] getDataFromResults(JsonObject jObj)
    {
        if(columnNames == null) return new Object[] {};
        
        List<Object> rowData    =   new ArrayList<>();
        for (String columnName : columnNames)
        {
            Object current = jObj.get(columnName.toUpperCase());
            if(current != null)
                rowData.add(current.toString().replace("\"", ""));
        }
        
        return rowData.toArray();
    }
    
    protected void setColumns()
    {
        DefaultTableCellRenderer renderer   =   new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (String columnHeader : columnHeaders) 
            dataModel.addColumn(columnHeader);
        
        
        for(int i = 0; i < dataTable.getColumnCount(); i++)
            dataTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }
    
    protected void showResponseLabel(String message, boolean result)
    {
        statusLabel.setText(message);
        if(result)
            statusLabel.setIcon(new ImageIcon(successImage));
        else
            statusLabel.setIcon(new ImageIcon(failImage));
        
        statusLabel.setVisible(true);
        Timer responseTimer =   new Timer(2000, (ActionEvent e)->
        {
            statusLabel.setVisible(false);
        });
        
        responseTimer.setRepeats(false);
        responseTimer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src  =   e.getSource();
        
        if(src == add)
            add();

        else if(src == remove)
            remove();
        
        else if(src == edit)
            edit();
    }
}
