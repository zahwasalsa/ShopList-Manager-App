/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package shoplist.manager.app;

/**
 *
 * @author USER
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShopListManagerApp {
    private JFrame frame;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private List<String> daftarBelanja;
    private JTextField inputField;
    private File filePenyimpanan;

    public ShopListManagerApp() {
        daftarBelanja = new ArrayList<>();
        filePenyimpanan = new File("daftar_belanja.dat");
        muatData();
        buatGUI();
    }

    private void buatGUI() {
        frame = new JFrame("Manajer Daftar Belanja");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel atas untuk input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        JButton addButton = new JButton("Tambah");
        addButton.addActionListener(e -> tambahItem());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // Panel tengah untuk daftar item
        listModel = new DefaultListModel<>();
        perbaruiListModel();
        itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);

        // Panel bawah untuk tombol aksi
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton removeButton = new JButton("Hapus Terpilih");
        removeButton.addActionListener(e -> hapusItem());
        
        JButton clearButton = new JButton("Bersihkan Semua");
        clearButton.addActionListener(e -> bersihkanDaftar());
        
        JButton saveButton = new JButton("Simpan Daftar");
        saveButton.addActionListener(e -> simpanDaftar());

        actionPanel.add(removeButton);
        actionPanel.add(clearButton);
        actionPanel.add(saveButton);

        // Tambahkan komponen ke frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(actionPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void tambahItem() {
        String item = inputField.getText().trim();
        if (!item.isEmpty()) {
            daftarBelanja.add(item);
            perbaruiListModel();
            inputField.setText("");
        }
    }

    private void hapusItem() {
        int selectedIndex = itemList.getSelectedIndex();
        if (selectedIndex != -1) {
            daftarBelanja.remove(selectedIndex);
            perbaruiListModel();
        }
    }

    private void bersihkanDaftar() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Apakah Anda yakin ingin menghapus semua item?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            daftarBelanja.clear();
            perbaruiListModel();
        }
    }

    private void perbaruiListModel() {
        listModel.clear();
        for (String item : daftarBelanja) {
            listModel.addElement(item);
        }
    }

    private void simpanDaftar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePenyimpanan))) {
            oos.writeObject(daftarBelanja);
            JOptionPane.showMessageDialog(frame, "Daftar belanja berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal menyimpan daftar belanja: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void muatData() {
        if (filePenyimpanan.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(filePenyimpanan))) {
                daftarBelanja = (List<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(frame, 
                    "Gagal memuat data: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShopListManagerApp());
    }
}