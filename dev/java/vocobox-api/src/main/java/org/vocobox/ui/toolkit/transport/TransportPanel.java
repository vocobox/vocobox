package org.vocobox.ui.toolkit.transport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;


public class TransportPanel extends JPanel{
    private static final long serialVersionUID = -7203231436876496053L;
    
    public interface On{
        public void play();
        public void stop();
    }
    
    public TransportPanel(final On listener){
        String lines = "[30px]";
        String columns = "[30px][30px]";
        setLayout(new MigLayout("", columns, lines));
        
        JButton play = new JButton("P");
        JButton stop = new JButton("S");
        add(play, "cell 0 0");
        add(stop, "cell 1 0");
        
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.play();
            }
        });
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.stop();
            }
        });        
    }
}
