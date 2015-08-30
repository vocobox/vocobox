package org.vocobox.apps.wav2synth;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jzy3d.ui.LookAndFeel;
import org.vocobox.ui.EzLayout;
import org.vocobox.ui.EzLayout.Prop;

public class VocoboxAppFile extends JFrame{
    private static final long serialVersionUID = -1028611255347296822L;
    VocoboxPanelsFile panels;
    
    public VocoboxAppFile(){
        super();
        System.out.println("vocobox app file!");
        //setModal(false);
        setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
        LookAndFeel.apply();
        windowExitListener(this);
        show();
        setVisible(true);
    }
    
    public void layout(VocoboxPanelsFile panels) throws IOException{
        JPanel mainPanel = new JPanel();
        String lines = "[20px][900px]";
        String columns = "[200px][500px,grow]";
        EzLayout layout = new EzLayout(mainPanel, lines, columns);
        //addInputRow(0, panels, layout);
        addSynthRow(0, panels, layout);
        //JScrollPane scrollPanel = new JScrollPane();
        //scrollPanel.add(mainPanel);
        add(mainPanel);
        pack();
    }
    
    public void addInputRow(int id, VocoboxPanelsFile panels, EzLayout layout) {
        layout.add(id, 0, new JLabel("Input"));
        layout.add(id+1, 0, panels.inputControl);
        layout.add(id+1, 1, panels.inputCharts, Prop.GROW);
    }

    public void addSynthRow(int id, VocoboxPanelsFile panels, EzLayout layout) {
        layout.add(id, 0, new JLabel("Synthetizer"));
        if(panels.synthControl!= null)
            layout.add(id+1, 0, panels.synthControl);
        layout.add(id+1, 1, panels.synthCharts, Prop.GROW);
    }

    public static void windowExitListener(final JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }
}
