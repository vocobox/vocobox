package org.vocobox.ui.toolkit.list;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/** A list of T that provides a filter widget */
public class JEntityList<T> extends JPanel {
    private static final long serialVersionUID = -3853250328224847935L;
    private JList list;
    private DefaultListModel listModel;

    protected T selectedItem = null;

    public JEntityList() {
        this(null, null);
    }

    public JEntityList(CellRenderer<T> renderer) {
        this(null, renderer);
    }

    public JEntityList(List<T> entities) {
        this(entities, new CellRenderer<T>(){
            @Override
            public String format(T entity) {
                return entity.toString();
            }
        });
    }
    
    public JEntityList(List<T> entities, CellRenderer<T> renderer) {
        super(new BorderLayout());

        listModel = new DefaultListModel();
        add(entities);

        // Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //setupKeyBindings(list);
        
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                ListSelectionModel selection = (DefaultListSelectionModel) arg0.getSource();
                int sel = selection.getMinSelectionIndex();
                
                T newSelection = (T) list.getModel().getElementAt(sel);
                
                if (sel >= 0){
                    newSelection = (T) list.getModel().getElementAt(sel);
                }
                else
                    newSelection = null;
                
                if(selectedItem!=newSelection){
                    selectedItem = newSelection;
                    fireEntitySelected(selectedItem);
                }
            }
        });

        list.setSelectedIndex(0);
        //list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        
        if(renderer!=null)
            list.setCellRenderer(renderer);
        
        JScrollPane listScrollPane = new JScrollPane(list);
        add(listScrollPane, BorderLayout.CENTER);
    }

    
    /*protected void setupKeyBindings(final JList jlist) {
        String delAction = "deleteItems";
        KeyStroke delKey = KeyStroke.getKeyStroke("DELETE"); //"ENTER"
        jlist.getInputMap().put(delKey, delAction);
        jlist.getActionMap().put(delAction, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("delete pressed");
                if (selectedItem != null) {
                    onDeleteEntity(selectedItem);
                }
                // doDelete(jlist);
            }
        });
    }

    protected void onDeleteEntity(T selectedItem) {
        for (int i = 0; i < listModel.getSize(); i++) {
            @SuppressWarnings("unchecked")
            T t = (T) listModel.getElementAt(i);
            
            if (t.equals(selectedItem)) {
                listModel.remove(i);
                fireEntityDeleted(selectedItem);
                i--;
            }
        }
    }*/

    public void add(List<T> content) {
        if (content != null && content.size() > 0)
            for (T h : content)
                listModel.addElement(h);
    }

    public void add(T[] content) {
        if (content != null && content.length > 0)
            for (T h : content)
                listModel.addElement(h);
    }

    public void add(T content) {
        listModel.addElement(content);
    }
    
    public List<T> getEntities(){
        List<T> out = new ArrayList<T>();
        for (int i = 0; i < listModel.size(); i++) {
            @SuppressWarnings("unchecked")
            T elementAt = (T)listModel.getElementAt(i);
            out.add(elementAt);
        }
        return out;
    }
    
    public synchronized void clear() {
        listModel.clear();
    }
    
    /* */
    
    public void addEntityListener(IEntityActionListener<T> listener){
        listeners.add(listener);
    }

    public void removeEntityListener(IEntityActionListener<T> listener){
        listeners.remove(listener);
    }

    protected void fireEntitySelected(T entity){
        for(IEntityActionListener<T> listener: listeners)
            listener.entitySelected(entity);
    }
    
    /*protected void fireEntityDeleted(T entity){
        for(IEntityActionListener<T> listener: listeners)
            listener.entityDeleted(entity);
    }*/
    
    protected List<IEntityActionListener<T>> listeners = new ArrayList<IEntityActionListener<T>>();
    
    public interface IEntityActionListener<T>{
        public void entitySelected(T entity);
        //public void entityDeleted(T entity);
    }
}
