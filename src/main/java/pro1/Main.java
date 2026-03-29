package pro1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // Vnitřní třída reprezentující jeden nakreslený dům
    static class House {
        int x, y;
        double rotation; // v úhlech (stupních)

        public House(int x, int y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }

    // Globální stav aplikace
    private static List<House> houses = new ArrayList<>();
    private static int currentSize = 50;
    private static boolean isRed = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Domácí úkol 2 - Domy");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Okno přes celou obrazovku
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            // --- HLAVNÍ PANEL (Kreslící plocha) ---
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();

                    // Zapnutí vyhlazování hran
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Určení barvy podle checkboxu
                    Color houseColor = isRed ? Color.RED : Color.GRAY;

                    // Vykreslení všech domů
                    for (House h : houses) {
                        Graphics2D g2House = (Graphics2D) g2d.create();

                        // Přesun na pozici kliknutí a rotace
                        g2House.translate(h.x, h.y);
                        g2House.rotate(Math.toRadians(h.rotation));

                        g2House.setColor(houseColor);

                        // Kreslení domu
                        int s = currentSize;
                        int half = s / 2;

                        // Základna (čtverec)
                        g2House.fillRect(-half, -half, s, s);

                        // Střecha (trojúhelník)
                        Polygon roof = new Polygon(
                                new int[]{-half - (s/5), 0, half + (s/5)},
                                new int[]{-half, -half - (s/2), -half},
                                3
                        );
                        g2House.fillPolygon(roof);

                        g2House.dispose();
                    }
                    g2d.dispose();
                }
            };
            mainPanel.setBackground(Color.WHITE);

            // --- LEVÝ PANEL (Ovládací prvky) ---
            JPanel leftPanel = new JPanel();
            leftPanel.setPreferredSize(new Dimension(250, 0)); // Šířka mezi 150 a 450 px
            leftPanel.setBackground(new Color(240, 240, 240));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // 1. Ovládání rotace nového domu
            JLabel rotLabel = new JLabel("Rotace nového domu (°):");
            JSlider rotSlider = new JSlider(0, 360, 0);
            rotSlider.setMajorTickSpacing(90);
            rotSlider.setPaintTicks(true);
            rotSlider.setPaintLabels(true);
            rotSlider.setFocusable(false); // Zabrání kradení fokusu

            // 2. Ovládání velikosti všech domů
            JLabel sizeLabel = new JLabel("Velikost domů:");
            JSlider sizeSlider = new JSlider(20, 200, 50);
            sizeSlider.setFocusable(false); // Zabrání kradení fokusu
            sizeSlider.addChangeListener(e -> {
                currentSize = sizeSlider.getValue();
                mainPanel.repaint();
            });

            // 3. Přepínání barvy
            JCheckBox colorCheck = new JCheckBox("Červená barva (jinak šedá)");
            colorCheck.setFocusable(false); // Zabrání kradení fokusu
            colorCheck.addActionListener(e -> {
                isRed = colorCheck.isSelected();
                mainPanel.repaint();
            });

            // 4. Reset tlačítko
            JButton resetBtn = new JButton("Reset plátna");
            resetBtn.setFocusable(false); // Zabrání kradení fokusu
            resetBtn.addActionListener(e -> {
                houses.clear();
                mainPanel.repaint();
            });

            // Přidání prvků do panelu s mezerami
            leftPanel.add(rotLabel);
            leftPanel.add(rotSlider);
            leftPanel.add(Box.createVerticalStrut(30));
            leftPanel.add(sizeLabel);
            leftPanel.add(sizeSlider);
            leftPanel.add(Box.createVerticalStrut(30));
            leftPanel.add(colorCheck);
            leftPanel.add(Box.createVerticalStrut(30));
            leftPanel.add(resetBtn);

            // --- INTERAKCE S HLAVNÍM PANELEM ---
            mainPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { // Použito mousePressed pro okamžitou reakci
                    mainPanel.requestFocusInWindow(); // Vynutí si pozornost
                    houses.add(new House(e.getX(), e.getY(), rotSlider.getValue()));
                    mainPanel.repaint();
                }
            });

            // Složení okna dohromady
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            // Zobrazení
            frame.setVisible(true);
        });
    }
}