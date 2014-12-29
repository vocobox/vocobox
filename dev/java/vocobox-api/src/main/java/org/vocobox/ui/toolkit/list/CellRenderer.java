package org.vocobox.ui.toolkit.list;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public abstract class CellRenderer<T> implements ListCellRenderer {

    public static class ToString extends CellRenderer<Object> {
        @Override
        public String format(Object entity) {
            return entity.toString();
        }
    }

    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        return new JPanel() {
            @Override
            @SuppressWarnings("unchecked")
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                String text = format((T) value);
                FontMetrics fm = g.getFontMetrics(g.getFont());
                g.setColor(isSelected ? list.getSelectionBackground() : list.getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(isSelected ? list.getSelectionForeground() : list.getForeground());
                g.drawString(text, 0, fm.getAscent());
            }

            @Override
            @SuppressWarnings("unchecked")
            public Dimension getPreferredSize() {
                String text = format((T) value);
                Graphics g = getGraphics();
                FontMetrics fm = g.getFontMetrics(g.getFont());
                return new Dimension(fm.stringWidth(text), fm.getHeight());
            }

            private static final long serialVersionUID = 5734185172924977082L;
        };
    }

    public abstract String format(T entity);
}
