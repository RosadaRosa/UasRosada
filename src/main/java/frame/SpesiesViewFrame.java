package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class  SpesiesViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public SpesiesViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM spesies WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        cariButton.addActionListener(e -> {
            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM spesies WHERE nama like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs =ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        tambahButton.addActionListener(e->{
            SpesiesInputFrame inputFrame = new SpesiesInputFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            SpesiesInputFrame inputFrame = new SpesiesInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setTitle("Data Spesies");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT K.*,B.nama AS ras_kucing FROM spesies AS K\n" +
                "LEFT JOIN kucing AS B ON K.kucing_id = B.id";
        try {
            Statement s = c.createStatement();
            ResultSet rs =s.executeQuery(selectSQL);

            String header[] = {"Id","Nama Spesies","Ras Kucing","Klasifikasi","Populasi"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[5];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("ras_kucing");
                row[3] = rs.getString("klasifikasi");
                row[4] = rs.getString("populasi");
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
