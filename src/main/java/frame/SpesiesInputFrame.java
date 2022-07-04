package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class SpesiesInputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JComboBox KucingComboBox;
    private JRadioButton tipeARadioButton;
    private JRadioButton tipeBRadioButton;
    private JTextField populasiTextField;
    private ButtonGroup klasifikasiButtonGroup;


    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public SpesiesInputFrame() {
        simpanButton.addActionListener(this::actionPerformed);
        batalButton.addActionListener(e -> {
            dispose();
        });
        kustomisasiKomponen();
        init();
    }

    public void init() {
        setTitle("Input Spesies");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiKomponen() {

        String findSQL = "SELECT * FROM spesies WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
                int kucingId = rs.getInt("kucing_id");
                for (int i = 0; i < KucingComboBox.getItemCount(); i++) {
                    KucingComboBox.setSelectedIndex(i);
                    ComboBoxItem item = (ComboBoxItem) KucingComboBox.getSelectedItem();
                    if(kucingId == item.getValue()){
                        break;
                    }

                }
                String klasifikasi = rs.getString("klasifikasi");
                if(klasifikasi != null){
                    if(klasifikasi.equals("TIPE A")){
                        tipeARadioButton.setSelected(true);
                    } else if (klasifikasi.equals("TIPE B")){
                        tipeBRadioButton.setSelected(true);
                    }
                }
                populasiTextField.setText(rs.getString("populasi"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void kustomisasiKomponen(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM kucing ORDER BY nama";

        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            KucingComboBox.addItem(new ComboBoxItem(0, "Pilih Ras Kucing"));
            while(rs.next()){
                KucingComboBox.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("nama")
                ));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        klasifikasiButtonGroup = new ButtonGroup();
        klasifikasiButtonGroup.add(tipeARadioButton);
        klasifikasiButtonGroup.add(tipeBRadioButton);

        populasiTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        populasiTextField.setText("0");
        populasiTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                populasiTextField.setEditable(
                        (e.getKeyChar() >= '0' && e.getKeyChar() <='9' ||
                                e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                                e.getKeyCode() == KeyEvent.VK_LEFT ||
                                e.getKeyCode() == KeyEvent.VK_RIGHT)
                );
            }
        });
    }

    private void actionPerformed(ActionEvent e) {
        String nama = namaTextField.getText();
        if (nama.equals("")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Lengkapi Data Nama Spesies",
                    "Validasi data kosong",
                    JOptionPane.WARNING_MESSAGE
            );
            namaTextField.requestFocus();
            return;
        }

        ComboBoxItem item = (ComboBoxItem) KucingComboBox.getSelectedItem();
        int kucingId = item.getValue();
        if (kucingId == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Pilih Ras Kucing",
                    "Validasi data kosong",
                    JOptionPane.WARNING_MESSAGE
            );
            KucingComboBox.requestFocus();
            return;
        }
        String klasifikasi = "";
        if(tipeARadioButton.isSelected()){
            klasifikasi = "TIPE A";
        }else if(tipeBRadioButton.isSelected()){
            klasifikasi = "TIPE B";
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Pilih Klasifikasi",
                    "Validasi data kosong",
                    JOptionPane.WARNING_MESSAGE
            );
            KucingComboBox.requestFocus();
            return;
        }

        if(populasiTextField.getText().equals("")){
            populasiTextField.setText("0");
        }
        int populasi = Integer.parseInt(populasiTextField.getText());
        if(populasi == 0){

            JOptionPane.showMessageDialog(
                    null,
                    "Isi Populasi",
                    "Validasi data kosong",
                    JOptionPane.WARNING_MESSAGE
            );
            populasiTextField.requestFocus();
            return;

        }

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            if (this.id == 0) {
                String cekSQL = "SELECT * FROM spesies WHERE nama = ?";
                ps = c.prepareStatement(cekSQL);
                ps.setString(1, nama);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Data Nama Spesies Sudah Ada",
                            "Validasi data sama",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    String insertSQL = "INSERT INTO spesies SET nama = ?, kucing_id = ?, " +
                                        "klasifikasi = ?, populasi = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, kucingId);
                    ps.setString(3, klasifikasi);
                    ps.setInt(4, populasi);

                    ps.executeUpdate();
                    dispose();
                }
            } else {
                String cekSQL = "SELECT * FROM spesies WHERE nama=? AND id!=?";
                ps = c.prepareStatement(cekSQL);
                ps.setString(1, nama);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Nama Kucing Sudah Ada",
                            "Validasi data sama",
                            JOptionPane.WARNING_MESSAGE);
                } else {

                    String updateSQL = "UPDATE spesies SET nama=?, kucing_id = ?," +
                                "klasifikasi = ?, populasi = ?  WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, kucingId);
                    ps.setString(3, klasifikasi);
                    ps.setInt(4, populasi);
                    ps.setInt(5, id);
                    ps.executeUpdate();
                    dispose();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}


