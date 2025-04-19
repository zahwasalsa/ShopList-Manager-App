/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aplikasibuku;

/**
 *
 * @author USER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class AplikasiBuku extends JFrame {
    private JTextField txtJudul, txtPenulis;
    private JTextArea txtOutput;
    private ArrayList<Buku> daftarBuku = new ArrayList<>();

    public AplikasiBuku() {
        setTitle("📚 Aplikasi Manajemen Buku");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center
        setResizable(false);

        // Panel utama
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label dan input
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Judul Buku:"), gbc);
        txtJudul = new JTextField(25);
        gbc.gridx = 1;
        panel.add(txtJudul, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Penulis Buku:"), gbc);
        txtPenulis = new JTextField(25);
        gbc.gridx = 1;
        panel.add(txtPenulis, gbc);

        // Tombol
        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnTambah = new JButton("➕ Tambah");
        JButton btnSimpan = new JButton("💾 Simpan");
        JButton btnBaca = new JButton("📂 Baca");
        tombolPanel.add(btnTambah);
        tombolPanel.add(btnSimpan);
        tombolPanel.add(btnBaca);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(tombolPanel, gbc);

        // Output area
        txtOutput = new JTextArea(10, 40);
        txtOutput.setEditable(false);
        txtOutput.setBorder(BorderFactory.createTitledBorder("📖 Daftar Buku"));
        JScrollPane scroll = new JScrollPane(txtOutput);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(scroll, gbc);

        // Event
        btnTambah.addActionListener(e -> tambahBuku());
        btnSimpan.addActionListener(e -> simpanKeFile());
        btnBaca.addActionListener(e -> bacaDariFile());

        // Tambah panel ke frame
        add(panel);
        setVisible(true);
    }

    private void tambahBuku() {
        String judul = txtJudul.getText();
        String penulis = txtPenulis.getText();

        if (!judul.isEmpty() && !penulis.isEmpty()) {
            daftarBuku.add(new Buku(judul, penulis));
            txtOutput.append("✅ " + judul + " oleh " + penulis + "\n");
            txtJudul.setText("");
            txtPenulis.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "📌 Isi judul dan penulis dulu ya!");
        }
    }

    private void simpanKeFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.ser"))) {
            oos.writeObject(daftarBuku);
            JOptionPane.showMessageDialog(this, "✅ Daftar buku berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "❌ Gagal menyimpan data!");
        }
    }

    private void bacaDariFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.ser"))) {
            daftarBuku = (ArrayList<Buku>) ois.readObject();
            txtOutput.setText("");
            for (Buku b : daftarBuku) {
                txtOutput.append("📘 " + b.toString() + "\n");
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "❌ Gagal membaca data!");
        }
    }

    static class Buku implements Serializable {
        private String judul;
        private String penulis;

        public Buku(String judul, String penulis) {
            this.judul = judul;
            this.penulis = penulis;
        }

        public String toString() {
            return judul + " oleh " + penulis;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AplikasiBuku());
    }
}
