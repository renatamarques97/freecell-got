package freecell;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainFreeCell extends JFrame {
    private final Jogo modelo;
    private final PaineldeCarta _boardDisplay;
    //private final JCheckBox _autoCompleteCB;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFreeCell();
            }
        });
    }

    public MainFreeCell() {
        //this._autoCompleteCB = new JCheckBox("Auto Complete");
        this.modelo = new Jogo();
        _boardDisplay = new PaineldeCarta(modelo);

        JButton newGameBtn;
        newGameBtn = new JButton("Novo");
        newGameBtn.addActionListener(new ActionNewGame());
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(newGameBtn);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.add(controlPanel, BorderLayout.NORTH);
        content.add(_boardDisplay, BorderLayout.CENTER);

        setContentPane(content);
        setTitle("FreeCell Game of Thrones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fechar no x
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    class ActionNewGame implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            modelo.reset();
        }
    }
    
}