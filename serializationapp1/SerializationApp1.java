package serializationapp1;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 *
 * @author NADHIF
 */
class NoteData implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String text;

    public NoteData(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

public class SerializationApp1 {

    private final JFrame frame;
    private final JTextArea textArea;
    private final JButton saveSerButton;
    private final JButton loadSerButton;
    private final JButton saveTxtButton;
    private final JButton loadTxtButton;

    public SerializationApp1() {
        frame = new JFrame("Aplikasi Serialization & File");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        saveSerButton = new JButton("Simpan .ser");
        loadSerButton = new JButton("Muat .ser");
        saveTxtButton = new JButton("Simpan .txt");
        loadTxtButton = new JButton("Muat .txt");

        buttonPanel.add(saveSerButton);
        buttonPanel.add(loadSerButton);
        buttonPanel.add(saveTxtButton);
        buttonPanel.add(loadTxtButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event tombol .ser (Serialization)
        saveSerButton.addActionListener(e -> saveSerialized());
        loadSerButton.addActionListener(e -> loadSerialized());

        // Event tombol .txt (Plain Text)
        saveTxtButton.addActionListener(e -> saveTextFile());
        loadTxtButton.addActionListener(e -> loadTextFile());

        frame.setVisible(true);
    }

    // Simpan teks ke file .ser (Serialization)
    private void saveSerialized() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("data.ser"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                NoteData data = new NoteData(textArea.getText());
                out.writeObject(data);
                JOptionPane.showMessageDialog(frame, "Data berhasil disimpan (serialization).");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal menyimpan data.");
            }
        }
    }

    // Muat teks dari file .ser (Deserialization)
    private void loadSerialized() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                NoteData data = (NoteData) in.readObject();
                textArea.setText(data.getText());
                JOptionPane.showMessageDialog(frame, "Data berhasil dimuat (deserialization).");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal memuat data.");
            }
        }
    }

    // Simpan teks biasa ke file .txt
    private void saveTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("data.txt"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                Files.write(Paths.get(fileChooser.getSelectedFile().getAbsolutePath()), textArea.getText().getBytes());
                JOptionPane.showMessageDialog(frame, "Teks berhasil disimpan.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal menyimpan teks.");
            }
        }
    }

    // Muat teks biasa dari file .txt
    private void loadTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(fileChooser.getSelectedFile().getAbsolutePath())));
                textArea.setText(content);
                JOptionPane.showMessageDialog(frame, "Teks berhasil dimuat.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Gagal memuat teks.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SerializationApp1::new);
    }
}
