import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
public class main extends javax.swing.JFrame {

    /**
     * Creates new form test2
     */
    public main() {
        initComponents();
        createMyForm();
    }

    int gridSizerow = 25;
    int gridSizecol = 40;
    int bombcnt = 100;
    int bombnumber[][] = new int[gridSizerow][gridSizecol];
    int isbomb[][] = new int[gridSizerow][gridSizecol];
    int clicked[][] = new int[gridSizerow][gridSizecol];
    int markedbomb[][] = new int[gridSizerow][gridSizecol];
    JButton buttonlist[][] = new JButton[gridSizerow][gridSizecol];
    
    private boolean checkrange(int r, int c) {
        if(r < 0 || r >= gridSizerow || c < 0 || c >= gridSizecol) return false;
        return true;
    }
    private boolean checkwin() {
        for(int r=0; r<gridSizerow; r++){
            for(int c=0; c<gridSizecol; c++) {
                if(isbomb[r][c]==1)continue;
                if(clicked[r][c]==0)return false;
            }
        }
        return true;
    }
    private void click(int r, int c) {
        if(r < 0 || r >= gridSizerow || c < 0 || c >= gridSizecol) return;
        if(clicked[r][c] == 1) return;
        clicked[r][c] = 1;
        if (isbomb[r][c] == 1) {
            for(int i=0; i<gridSizerow; i++){
                for(int j=0; j<gridSizecol; j++) {
                    if(isbomb[i][j]==1){
                        buttonlist[i][j].setText("X");
                        buttonlist[i][j].setBackground(Color.RED);
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Bomb! Game Over.");
            System.exit(0);
        } else {
            if(bombnumber[r][c] == 0) {
                buttonlist[r][c].setText("");
                buttonlist[r][c].setBackground(Color.gray);
                click(r-1, c);
                click(r+1, c);
                click(r, c-1);
                click(r, c+1);
                click(r-1, c-1);
                click(r-1, c+1);
                click(r+1, c-1);
                click(r+1, c+1);
            } else {
                buttonlist[r][c].setText(String.valueOf(bombnumber[r][c]));
                buttonlist[r][c].setBackground(Color.pink);
            }
        }
        if(checkwin()) {
            JOptionPane.showMessageDialog(null, "You Win!");
            System.exit(0);
        }
    }
    private void middleclick(int r, int c) {
        if(bombnumber[r][c]==0 || clicked[r][c] == 0) return;
        int markbombsum=0, bombsum=0;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(!checkrange(r+i, c+j)) continue;
                if(markedbomb[r+i][c+j]==1)markbombsum++;
                if(isbomb[r+i][c+j]==1)bombsum++;
            }
        }
        if(markbombsum!=bombsum)return;
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){
                if(checkrange(r+i, c+j) && markedbomb[r+i][c+j] == 0) click(r+i, c+j);
            }
        }
    }
    private void markbomb(int r, int c) {
        if (clicked[r][c] == 1) {
            return;
        }
        if(markedbomb[r][c] == 1){
            buttonlist[r][c].setText("");
            markedbomb[r][c] = 0;
        } else {
            buttonlist[r][c].setText("🚩");
            markedbomb[r][c] = 1;
        }
    }
    
    private void createMyForm() {
        for(int i=0; i<bombcnt; i++) {
            int r = (int)(Math.random() * gridSizerow);
            int c = (int)(Math.random() * gridSizecol);
            if (isbomb[r][c] == 1) {
                i--;
            } else {
               isbomb[r][c] = 1;
               System.out.println(r+" "+c);
               for(int j=-1;j<=1; j++) {
                   for(int k=-1;k<=1;k++) {
                       if(r+j<0 || r+j>= gridSizerow) continue;
                       if(c+k<0 || c+k>= gridSizecol) continue;
                       bombnumber[r+j][c+k] ++;
                   }
               }
               bombnumber[r][c]--;
            }            
        }
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        contentPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 0));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(gridSizerow, gridSizecol, 0, 0));
        for (int i = 0; i < gridSizerow; i++)
        {
            for (int j = 0; j < gridSizecol; j++)
            {
                JButton button = new JButton("(" + i + ", " + j + ")");
                buttonlist[i][j] = button;
                
                button.setText("");
                button.setMargin(new java.awt.Insets(0, 0, 0, 0));
                button.setPreferredSize(new Dimension(40, 40));
                button.setName(i+","+j);
//                button.setActionCommand("(" + i + ", " + j + ")");
                button.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent mouseEvent) {
                        int modifiers = mouseEvent.getModifiers();
                        JButton but = (JButton) mouseEvent.getSource();
                        String t[]= but.getName().split(",");
                        int r = Integer.parseInt(t[0]);
                        int c = Integer.parseInt(t[1]);
                        
                        if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                            // Mask may not be set properly prior to Java 2
                            // See SwingUtilities.isLeftMouseButton() for workaround
                            System.out.println("Left button pressed.");
                            click(r, c);
                        }
                        if ((modifiers & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
                            System.out.println("Middle button pressed.");
                            middleclick(r, c);
                        }
                        if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
                            System.out.println("Right button pressed.");
                            markbomb(r, c);
                        }
                        System.out.println(but.getName());
                    }
                });
//                button.addActionListener(new ActionListener()
//                {
//                    public void actionPerformed(ActionEvent ae)
//                    {
//                        JButton but = (JButton) ae.getSource();
//                        System.out.println(but.getName());
//                        
////                        positionLabel.setText(
////                            but.getActionCommand());                           
//                    }
//                });
                buttonPanel.add(button);
            }
        }
        contentPane.add(buttonPanel);

        setContentPane(contentPane);
        pack();
        setLocationByPlatform(true);
        setVisible(true);
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
