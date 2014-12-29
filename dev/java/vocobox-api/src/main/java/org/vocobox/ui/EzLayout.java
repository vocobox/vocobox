package org.vocobox.ui;

import java.awt.Component;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class EzLayout {
    public enum Prop{
        GROW
    }
    
    JPanel panel;
    
    
    
    public JPanel getPanel() {
        return panel;
    }

    public EzLayout(String lines, String columns){
        this(new JPanel(), lines, columns);
    }
    
    public EzLayout(JPanel panel, String lines, String columns){
        this.panel = panel;
        panel.setLayout(new MigLayout("", columns, lines));
    }

    public void add(int line, int column, Component component){
        panel.add(component, "cell " + column + " " + line);
    }

    public void add(int line, int column, Component component, Prop property){
        if(Prop.GROW.equals(property))
            panel.add(component, "cell " + column + " " + line + ", grow");
    }
    
    public JFrame newFrame(JPanel panel){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore failure to set default look en feel;
        }
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    
    public static Scrollbar sliderHorizontal(int value, int minScrollWidth, int maxScrollWidth, AdjustmentListener listener) {
        Scrollbar slider = new Scrollbar(Scrollbar.HORIZONTAL, value, 1, minScrollWidth, maxScrollWidth);
        slider.addAdjustmentListener(listener);
        return slider;
    }
    
    public static Scrollbar sliderVertical(int value, int minScrollWidth, int maxScrollWidth, AdjustmentListener listener) {
        Scrollbar slider = new Scrollbar(Scrollbar.VERTICAL, value, 1, minScrollWidth, maxScrollWidth);
        slider.addAdjustmentListener(listener);
        return slider;
    }

}
