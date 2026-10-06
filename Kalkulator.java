import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleUnaryOperator;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Kalkulator extends JFrame {

    private static final int PX = 3;

    private static final Color BODY          = new Color(210, 235, 252);
    private static final Color PUTIH         = new Color(245, 252, 255);
    private static final Color BAYANG        = new Color(120, 165, 200);
    private static final Color MUKA          = new Color(190, 225, 245);
    private static final Color MUKA_HOVER    = new Color(210, 238, 252);
    private static final Color TEKS_TOMBOL   = new Color(30, 80, 130);
    private static final Color JUDUL         = new Color(70, 150, 220);
    private static final Color JUDUL_TERANG  = new Color(120, 190, 245);
    private static final Color LAYAR_BG      = new Color(255, 250, 205);
    private static final Color LAYAR_TEKS    = new Color(40, 100, 160);
    private static final Color LAYAR_RIWAYAT = new Color(90, 140, 190);
    private static final Color AKSEN_KUNING  = new Color(255, 200, 60);
    private static final Color AKSEN_KUNING_GELAP = new Color(230, 170, 30);

    private static final String KALI = "x";
    private static final String BAGI = "/";
    private static final char CH_KALI = KALI.charAt(0);
    private static final char CH_BAGI = BAGI.charAt(0);
    private static final String OPERATOR = "+-" + KALI + BAGI;
    private static final String GANTI_TANDA = "+/-";
    private static final String BACKSPACE = "DEL";
    private static final int BATAS_KARAKTER = 80;

    private static Font fontDasar = null;
    
    private static boolean fontPiksel = false;

    static {
        try {
            File f = new File("PressStart2P-Regular.ttf");
            if (f.exists()) {
                fontDasar = Font.createFont(Font.TRUETYPE_FONT, f);
                fontPiksel = true;
            }
        } catch (FontFormatException | IOException ignored) {
            fontDasar = null;
            fontPiksel = false;
        }
    }

    static Font font(int ukuran) {
        if (fontPiksel) {
            return fontDasar.deriveFont(Font.PLAIN, Math.max(8f, ukuran * 0.62f));
        }
        return new Font(Font.MONOSPACED, Font.BOLD, ukuran);
    }

    static void tanpaAntialias(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    static void kotakPixel(Graphics2D g, int x, int y, int w, int h, Color c) {
        g.setColor(c);
        g.fillRect(x + PX, y, w - 2 * PX, h);
        g.fillRect(x, y + PX, w, h - 2 * PX);
    }

    private static class BingkaiTimbul extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            g.setColor(PUTIH);
            g.fillRect(x, y, w, PX);
            g.fillRect(x, y, PX, h);
            g.setColor(BAYANG);
            g.fillRect(x, y + h - PX, w, PX);
            g.fillRect(x + w - PX, y, PX, h);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(PX, PX, PX, PX);
        }
    }

    private static class TombolPixel extends JButton {
        TombolPixel(String teks) {
            super(teks);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setRolloverEnabled(true);
            setFont(font(18));
            setForeground(TEKS_TOMBOL);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            tanpaAntialias(g2);
            int w = getWidth(), h = getHeight();
            boolean tekan = getModel().isArmed() && getModel().isPressed();
            Color muka = getModel().isRollover() ? MUKA_HOVER : MUKA;

            kotakPixel(g2, 0, 0, w, h, tekan ? PUTIH : BAYANG);
            kotakPixel(g2, 0, 0, w - PX, h - PX, tekan ? BAYANG : PUTIH);
            kotakPixel(g2, PX, PX, w - 2 * PX, h - 2 * PX, muka);

            g2.setFont(getFont());
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int geser = tekan ? 1 : 0;
            int x = (w - fm.stringWidth(getText())) / 2 + geser;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent() + geser;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    private static class TombolKuning extends TombolPixel {
        TombolKuning(String teks) {
            super(teks);
            setForeground(new Color(120, 80, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            tanpaAntialias(g2);
            int w = getWidth(), h = getHeight();
            boolean tekan = getModel().isArmed() && getModel().isPressed();
            Color muka = getModel().isRollover() ? new Color(255, 220, 100) : AKSEN_KUNING;

            kotakPixel(g2, 0, 0, w, h, tekan ? PUTIH : AKSEN_KUNING_GELAP);
            kotakPixel(g2, 0, 0, w - PX, h - PX, tekan ? AKSEN_KUNING_GELAP : PUTIH);
            kotakPixel(g2, PX, PX, w - 2 * PX, h - 2 * PX, muka);

            g2.setFont(getFont());
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int geser = tekan ? 1 : 0;
            int x = (w - fm.stringWidth(getText())) / 2 + geser;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent() + geser;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    private static class TombolJudul extends JButton {
        private final int jenis;

        TombolJudul(int jenis) {
            this.jenis = jenis;
            setPreferredSize(new Dimension(28, 24));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            tanpaAntialias(g2);
            int w = getWidth(), h = getHeight();
            int cx = w / 2, cy = h / 2;
            boolean hover = getModel().isRollover();

            if (hover) {
                g2.setColor(JUDUL_TERANG);
                g2.fillRect(0, 0, w, h);
            }
            g2.setColor(Color.WHITE);
            switch (jenis) {
                case 0 -> g2.fillRect(cx - 9, cy + 3, 18, PX);
                case 1 -> {
                    g2.fillRect(cx - 6, cy - 6, 12, 12);
                    g2.setColor(hover ? JUDUL_TERANG : JUDUL);
                    g2.fillRect(cx - 3, cy - 3, 6, 6);
                }
                default -> {
                    for (int i = 0; i < 4; i++) {
                        g2.fillRect(cx - 6 + i * PX, cy - 6 + i * PX, PX, PX);
                        g2.fillRect(cx + 3 - i * PX, cy - 6 + i * PX, PX, PX);
                    }
                }
            }
            g2.dispose();
        }
    }

    private static class TombolIkon extends JButton {
        private final String simbol;

        TombolIkon(String simbol) {
            this.simbol = simbol;
            setPreferredSize(new Dimension(28, 24));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setRolloverEnabled(true);
            setToolTipText(simbol);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            tanpaAntialias(g2);
            int w = getWidth(), h = getHeight();
            boolean hover = getModel().isRollover();
            if (hover) {
                g2.setColor(JUDUL_TERANG);
                g2.fillRect(0, 0, w, h);
            }
            g2.setColor(Color.WHITE);
            g2.setFont(font(14));
            FontMetrics fm = g2.getFontMetrics();
            int x = (w - fm.stringWidth(simbol)) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(simbol, x, y);
            g2.dispose();
        }
    }

    private static class LayarPixel extends JPanel {
        private String teks = "0";
        private String riwayat = " ";

        LayarPixel() {
            setOpaque(false);
            setPreferredSize(new Dimension(100, 84));
        }

        void setText(String t) {
            teks = t;
            repaint();
        }

        void setRiwayat(String r) {
            riwayat = r;
            repaint();
        }

        private static String potongKiri(String teks, FontMetrics fm, int maxLebar) {
            if (fm.stringWidth(teks) <= maxLebar) return teks;
            String sisa = teks;
            while (sisa.length() > 1 && fm.stringWidth(".." + sisa) > maxLebar) {
                sisa = sisa.substring(1);
            }
            return ".." + sisa;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            tanpaAntialias(g2);
            int w = getWidth(), h = getHeight();

            kotakPixel(g2, 0, 0, w, h, BAYANG);
            kotakPixel(g2, PX, PX, w - PX, h - PX, PUTIH);
            kotakPixel(g2, PX, PX, w - 2 * PX, h - 2 * PX, LAYAR_BG);

            int padding = 12;
            int maxLebar = w - 2 * PX - 2 * padding;

            if (riwayat != null && !riwayat.trim().isEmpty()) {
                g2.setFont(font(12));
                g2.setColor(LAYAR_RIWAYAT);
                FontMetrics fmr = g2.getFontMetrics();
                String r = potongKiri(riwayat, fmr, maxLebar);
                g2.drawString(r, w - 2 * PX - padding - fmr.stringWidth(r),
                        2 * PX + 6 + fmr.getAscent());
            }

            int ukuran = 32;
            Font f = font(ukuran);
            FontMetrics fm = g2.getFontMetrics(f);
            while (fm.stringWidth(teks) > maxLebar && ukuran > 14) {
                ukuran -= 2;
                f = font(ukuran);
                fm = g2.getFontMetrics(f);
            }
            String tampil = potongKiri(teks, fm, maxLebar);
            g2.setFont(f);
            g2.setColor(LAYAR_TEKS);
            int x = w - 2 * PX - padding - fm.stringWidth(tampil);
            int y = h - 2 * PX - 8 - fm.getDescent();
            g2.drawString(tampil, x, y);
            g2.dispose();
        }
    }

    private static class Penghitung {
        private final String s;
        private int pos = 0;

        private Penghitung(String s) {
            this.s = s;
        }

        static double hitung(String ekspresi) {
            Penghitung p = new Penghitung(ekspresi);
            double hasil = p.tambahKurang();
            if (p.pos != p.s.length()) {
                throw new IllegalArgumentException("Format salah");
            }
            return hasil;
        }

        private char intip() {
            return pos < s.length() ? s.charAt(pos) : '\0';
        }

        private double tambahKurang() {
            double nilai = kaliBagi();
            while (intip() == '+' || intip() == '-') {
                char op = s.charAt(pos++);
                double kanan = kaliBagi();
                nilai = (op == '+') ? nilai + kanan : nilai - kanan;
            }
            return nilai;
        }

        private double kaliBagi() {
            double nilai = unary();
            while (intip() == CH_KALI || intip() == CH_BAGI) {
                char op = s.charAt(pos++);
                double kanan = unary();
                if (op == CH_KALI) {
                    nilai *= kanan;
                } else {
                    if (kanan == 0) {
                        throw new ArithmeticException("Tidak bisa bagi 0");
                    }
                    nilai /= kanan;
                }
            }
            return nilai;
        }

        private double unary() {
            if (intip() == '-') {
                pos++;
                return -unary();
            }
            if (intip() == '+') {
                pos++;
                return unary();
            }
            return persen();
        }

        private double persen() {
            double nilai = dasar();
            while (intip() == '%') {
                pos++;
                nilai /= 100;
            }
            return nilai;
        }

        private double dasar() {
            if (intip() == '(') {
                pos++;
                double nilai = tambahKurang();
                if (intip() != ')') {
                    throw new IllegalArgumentException("Kurung tidak seimbang");
                }
                pos++;
                return nilai;
            }
            int awal = pos;
            while (Character.isDigit(intip()) || intip() == '.') {
                pos++;
            }
            if (awal == pos) {
                throw new IllegalArgumentException("Format salah");
            }
            return Double.parseDouble(s.substring(awal, pos));
        }
    }

    private static class Meteran {
        final String nama;
        final String singkatan;
        final double faktor;

        Meteran(String nama, String singkatan, double faktor) {
            this.nama = nama;
            this.singkatan = singkatan;
            this.faktor = faktor;
        }

        @Override
        public String toString() {
            return nama + " (" + singkatan + ")";
        }
    }

    private static class KonverterKategori {
        final String nama;
        final List<Meteran> meteranList;

        KonverterKategori(String nama, List<Meteran> meteranList) {
            this.nama = nama;
            this.meteranList = meteranList;
        }
    }

    private static class RiwayatItem {
        final String ekspresi;
        final String hasil;

        RiwayatItem(String ekspresi, String hasil) {
            this.ekspresi = ekspresi;
            this.hasil = hasil;
        }

        @Override
        public String toString() {
            return ekspresi + " = " + hasil;
        }
    }

    private final LayarPixel layar = new LayarPixel();
    private String ekspresi = "";
    private boolean sudahHasil = false;
    private final List<RiwayatItem> riwayatList = new ArrayList<>();
    private final JPanel root;

    private JDialog dialogKonverter;
    private JDialog dialogRiwayat;
    private String kategoriAktif = "Area";

    private static final Map<String, KonverterKategori> KATEGORI = new LinkedHashMap<>();

    static {
        KATEGORI.put("Area", new KonverterKategori("Area", List.of(
                new Meteran("Hektar", "ha", 10000),
                new Meteran("Sentimeter persegi", "cm²", 0.0001),
                new Meteran("Meter persegi", "m²", 1),
                new Meteran("Acres", "ac", 4046.8564224),
                new Meteran("Ares", "a", 100),
                new Meteran("Kaki persegi", "ft²", 0.09290304),
                new Meteran("Inci persegi", "in²", 0.00064516)
        )));
        KATEGORI.put("Length", new KonverterKategori("Length", List.of(
                new Meteran("Milimeter", "mm", 0.001),
                new Meteran("Sentimeter", "cm", 0.01),
                new Meteran("Meter", "m", 1),
                new Meteran("Kilometer", "km", 1000),
                new Meteran("Inci", "in", 0.0254),
                new Meteran("Kaki", "ft", 0.3048),
                new Meteran("Yard", "yd", 0.9144),
                new Meteran("Mil", "mi", 1609.344),
                new Meteran("Mil laut", "nmi", 1852),
                new Meteran("Mil (Indonesia)", "mil", 1609.344)
        )));
        KATEGORI.put("Temperature", new KonverterKategori("Temperature", List.of(
                new Meteran("Celsius", "°C", 1),
                new Meteran("Fahrenheit", "°F", 1),
                new Meteran("Kelvin", "K", 1)
        )));
        KATEGORI.put("Volume", new KonverterKategori("Volume", List.of(
                new Meteran("Galon UK", "gal UK", 4.54609),
                new Meteran("Galon US", "gal US", 3.785411784),
                new Meteran("Liter", "L", 1),
                new Meteran("Mililiter", "mL", 0.001),
                new Meteran("Sentimeter kubik", "cm³", 0.001),
                new Meteran("Meter kubik", "m³", 1000),
                new Meteran("Inci kubik", "in³", 0.016387064),
                new Meteran("Kaki kubik", "ft³", 28.316846592)
        )));
        KATEGORI.put("Mass", new KonverterKategori("Mass", List.of(
                new Meteran("Ton metrik", "t", 1000),
                new Meteran("Ton UK", "ton UK", 1016.0469088),
                new Meteran("Ton US", "ton US", 907.18474),
                new Meteran("Pound", "lb", 0.45359237),
                new Meteran("Ounce", "oz", 0.028349523125),
                new Meteran("Kilogram", "kg", 1),
                new Meteran("Gram", "g", 0.001)
        )));
        KATEGORI.put("Data", new KonverterKategori("Data", List.of(
                new Meteran("Bit", "b", 1.0 / 8.0),
                new Meteran("Byte", "B", 1),
                new Meteran("Kilobyte", "KB", 1000),
                new Meteran("Kibibyte", "KiB", 1024),
                new Meteran("Megabyte", "MB", 1000000),
                new Meteran("Mebibyte", "MiB", 1048576),
                new Meteran("Gigabyte", "GB", 1000000000),
                new Meteran("Gibibyte", "GiB", 1073741824),
                new Meteran("Terabyte", "TB", 1000000000000.0),
                new Meteran("Tebibyte", "TiB", 1099511627776.0)
        )));
        KATEGORI.put("Speed", new KonverterKategori("Speed", List.of(
                new Meteran("Meter/detik", "m/s", 1),
                new Meteran("Meter/jam", "m/h", 1.0 / 3600.0),
                new Meteran("Kilometer/detik", "km/s", 1000),
                new Meteran("Kilometer/jam", "km/h", 1.0 / 3.6),
                new Meteran("Inci/detik", "in/s", 0.0254),
                new Meteran("Inci/jam", "in/h", 0.0254 / 3600.0),
                new Meteran("Kaki/detik", "ft/s", 0.3048),
                new Meteran("Kaki/jam", "ft/h", 0.3048 / 3600.0),
                new Meteran("Mil/detik", "mi/s", 1609.344),
                new Meteran("Mil/jam", "mi/h", 1609.344 / 3600.0),
                new Meteran("Knot", "kn", 0.514444444)
        )));
        KATEGORI.put("Time", new KonverterKategori("Time", List.of(
                new Meteran("Milidetik", "ms", 0.001),
                new Meteran("Detik", "s", 1),
                new Meteran("Menit", "min", 60),
                new Meteran("Jam", "h", 3600),
                new Meteran("Hari", "d", 86400),
                new Meteran("Minggu", "wk", 604800),
                new Meteran("Bulan", "mo", 2629746),
                new Meteran("Tahun", "yr", 31556952)
        )));
    }

    public Kalkulator() {
        super("Kalkulator Java");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        root = new JPanel(new BorderLayout());
        root.setBackground(BODY);
        root.setBorder(new BingkaiTimbul());
        setContentPane(root);

        root.add(buatBarJudul(), BorderLayout.NORTH);
        root.add(buatBody(), BorderLayout.CENTER);
        pasangKeyboard();

        setSize(360, 640);
        setMinimumSize(new Dimension(320, 560));
        setLocationRelativeTo(null);
    }

    private JPanel buatBarJudul() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(JUDUL);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(JUDUL_TERANG);
                g.fillRect(0, 0, getWidth(), PX);
            }
        };
        bar.setPreferredSize(new Dimension(100, 34));

        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 5));
        kiri.setOpaque(false);

        TombolIkon btnKonverter = new TombolIkon("⇄");
        btnKonverter.setToolTipText("Buka Converter");
        btnKonverter.addActionListener(e -> bukaDialogKonverter());
        kiri.add(btnKonverter);

        JLabel judul = new JLabel("Kalkulator");
        judul.setForeground(Color.WHITE);
        judul.setFont(font(13));
        judul.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        kiri.add(judul);
        bar.add(kiri, BorderLayout.WEST);

        TombolJudul btnMin = new TombolJudul(0);
        TombolJudul btnMax = new TombolJudul(1);
        TombolJudul btnTutup = new TombolJudul(2);
        btnMin.addActionListener(e -> setState(Frame.ICONIFIED));
        btnMax.addActionListener(e -> setExtendedState(
                getExtendedState() == Frame.MAXIMIZED_BOTH ? Frame.NORMAL : Frame.MAXIMIZED_BOTH));
        btnTutup.addActionListener(e -> dispose());

        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 5));
        kanan.setOpaque(false);

        TombolIkon btnRiwayat = new TombolIkon("🕐");
        btnRiwayat.setToolTipText("Riwayat Perhitungan");
        btnRiwayat.addActionListener(e -> bukaDialogRiwayat());
        kanan.add(btnRiwayat);

        kanan.add(btnMin);
        kanan.add(btnMax);
        kanan.add(btnTutup);
        bar.add(kanan, BorderLayout.EAST);

        final Point[] offset = {new Point()};
        MouseAdapter drag = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                offset[0] = new Point(e.getXOnScreen() - getX(), e.getYOnScreen() - getY());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (getExtendedState() == Frame.NORMAL) {
                    setLocation(e.getXOnScreen() - offset[0].x, e.getYOnScreen() - offset[0].y);
                }
            }
        };
        bar.addMouseListener(drag);
        bar.addMouseMotionListener(drag);
        return bar;
    }

    private JPanel buatBody() {
        JPanel body = new JPanel(new BorderLayout(0, 10));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        body.add(layar, BorderLayout.NORTH);
        body.add(buatGridTombol(), BorderLayout.CENTER);
        return body;
    }

    private JPanel buatGridTombol() {
        String[][] baris = {
                {"(", ")", "%", BACKSPACE},
                {"x²", "x³", "√x", "∛x"},
                {"x^y", "ʸ√x", "1/x", "|x|"},
                {"C", GANTI_TANDA, KALI, BAGI},
                {"7", "8", "9", "-"},
                {"4", "5", "6", "+"},
                {"1", "2", "3", "="},
                {"0"}
        };

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.insets = new Insets(3, 3, 3, 3);

        for (int r = 0; r < baris.length; r++) {
            for (int c = 0; c < baris[r].length; c++) {
                String label = baris[r][c];
                TombolPixel tombol;
                if (label.equals("=")) {
                    tombol = new TombolKuning(label);
                } else {
                    tombol = new TombolPixel(label);
                }
                tombol.addActionListener(e -> proses(label));

                gc.gridx = c;
                gc.gridy = r;
                gc.gridwidth = 1;
                gc.gridheight = 1;

                if (label.equals("0")) {
                    gc.gridwidth = 3;
                }
                if (label.equals("=")) {
                    gc.gridheight = 2;
                }

                panel.add(tombol, gc);
            }
        }
        return panel;
    }

    private void pasangKeyboard() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        for (char c : "0123456789.+-%/=()".toCharArray()) {
            daftarkan(im, am, KeyStroke.getKeyStroke(c), String.valueOf(c));
        }
        daftarkan(im, am, KeyStroke.getKeyStroke('*'), KALI);
        daftarkan(im, am, KeyStroke.getKeyStroke('x'), KALI);
        daftarkan(im, am, KeyStroke.getKeyStroke('X'), KALI);
        daftarkan(im, am, KeyStroke.getKeyStroke('c'), "C");
        daftarkan(im, am, KeyStroke.getKeyStroke('C'), "C");
        daftarkan(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");
        daftarkan(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), BACKSPACE);
        daftarkan(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "C");
    }

    private void daftarkan(InputMap im, ActionMap am, KeyStroke ks, String perintah) {
        String kunci = "aksi_" + perintah + "_" + ks;
        im.put(ks, kunci);
        am.put(kunci, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proses(perintah);
            }
        });
    }

    private void proses(String perintah) {
        if (perintah.matches("[0-9]")) {
            masukkanDigit(perintah);
            return;
        }
        switch (perintah) {
            case "." -> masukkanTitik();
            case "C" -> resetSemua();
            case BACKSPACE -> hapusSatu();
            case GANTI_TANDA -> balikTanda();
            case "%" -> persen();
            case "(" -> kurungBuka();
            case ")" -> kurungTutup();
            case "=" -> hasilAkhir();
            case "x²" -> terapkanUnary("x²", v -> v * v);
            case "x³" -> terapkanUnary("x³", v -> v * v * v);
            case "√x" -> terapkanUnary("√x", Math::sqrt);
            case "∛x" -> terapkanUnary("∛x", Math::cbrt);
            case "1/x" -> terapkanUnary("1/x", v -> {
                if (v == 0) throw new ArithmeticException("Tidak bisa bagi 0");
                return 1.0 / v;
            });
            case "|x|" -> terapkanUnary("|x|", Math::abs);
            case "x^y" -> pilihOperator("^");
            case "ʸ√x" -> pilihOperator("√");
            default -> pilihOperator(perintah);
        }
    }

    private void refresh() {
        layar.setText(ekspresi.isEmpty() ? "0" : ekspresi);
    }

    private void mulaiBaruJikaSudahHasil() {
        if (sudahHasil) {
            ekspresi = "";
            sudahHasil = false;
            layar.setRiwayat(" ");
        }
    }

    private String angkaTerakhir() {
        int i = ekspresi.length();
        while (i > 0 && (Character.isDigit(ekspresi.charAt(i - 1)) || ekspresi.charAt(i - 1) == '.')) {
            i--;
        }
        return ekspresi.substring(i);
    }

    private static int hitungKarakter(String teks, char c) {
        int jumlah = 0;
        for (int i = 0; i < teks.length(); i++) {
            if (teks.charAt(i) == c) jumlah++;
        }
        return jumlah;
    }

    private void masukkanDigit(String d) {
        mulaiBaruJikaSudahHasil();
        if (ekspresi.length() >= BATAS_KARAKTER) return;

        if (!ekspresi.isEmpty()) {
            char akhir = ekspresi.charAt(ekspresi.length() - 1);
            if (akhir == ')' || akhir == '%') ekspresi += KALI;
        }
        if (angkaTerakhir().equals("0")) {
            ekspresi = ekspresi.substring(0, ekspresi.length() - 1);
        }
        ekspresi += d;
        refresh();
    }

    private void masukkanTitik() {
        mulaiBaruJikaSudahHasil();
        if (ekspresi.length() >= BATAS_KARAKTER) return;

        String angka = angkaTerakhir();
        if (angka.contains(".")) return;

        if (!ekspresi.isEmpty()) {
            char akhir = ekspresi.charAt(ekspresi.length() - 1);
            if (akhir == ')' || akhir == '%') ekspresi += KALI;
        }
        ekspresi += angka.isEmpty() ? "0." : ".";
        refresh();
    }

    private void kurungBuka() {
        mulaiBaruJikaSudahHasil();
        if (ekspresi.length() >= BATAS_KARAKTER) return;

        if (!ekspresi.isEmpty()) {
            char akhir = ekspresi.charAt(ekspresi.length() - 1);
            if (Character.isDigit(akhir) || akhir == '.' || akhir == ')' || akhir == '%') {
                ekspresi += KALI;
            }
        }
        ekspresi += "(";
        refresh();
    }

    private void kurungTutup() {
        if (sudahHasil || ekspresi.isEmpty()) return;
        if (hitungKarakter(ekspresi, '(') <= hitungKarakter(ekspresi, ')')) return;
        if (ekspresi.length() >= BATAS_KARAKTER) return;

        char akhir = ekspresi.charAt(ekspresi.length() - 1);
        if (Character.isDigit(akhir) || akhir == ')' || akhir == '%') {
            ekspresi += ")";
            refresh();
        }
    }

    private void pilihOperator(String op) {
        if (sudahHasil) {
            sudahHasil = false;
            layar.setRiwayat(" ");
        }
        if (ekspresi.length() >= BATAS_KARAKTER) return;

        if (!ekspresi.isEmpty()) {
            char akhir = ekspresi.charAt(ekspresi.length() - 1);
            boolean kaliBagi = akhir == CH_KALI || akhir == CH_BAGI;
            if (op.equals("-") && kaliBagi) {
                ekspresi += op;
                refresh();
                return;
            }
            while (!ekspresi.isEmpty() && OPERATOR.indexOf(ekspresi.charAt(ekspresi.length() - 1)) >= 0) {
                ekspresi = ekspresi.substring(0, ekspresi.length() - 1);
            }
        }

        if (ekspresi.isEmpty()) {
            ekspresi = op.equals("-") ? "-" : "0" + op;
        } else if (ekspresi.endsWith("(")) {
            if (op.equals("-")) ekspresi += op;
        } else {
            ekspresi += op;
        }
        refresh();
    }

    private void persen() {
        if (ekspresi.isEmpty() || ekspresi.length() >= BATAS_KARAKTER) return;
        char akhir = ekspresi.charAt(ekspresi.length() - 1);
        if (Character.isDigit(akhir) || akhir == ')' || akhir == '%') {
            if (sudahHasil) {
                sudahHasil = false;
                layar.setRiwayat(" ");
            }
            ekspresi += "%";
            refresh();
        }
    }

    private void balikTanda() {
        if (ekspresi.isEmpty()) return;

        if (sudahHasil) {
            try {
                double nilai = Double.parseDouble(ekspresi);
                if (nilai != 0) ekspresi = format(-nilai);
            } catch (NumberFormatException ignored) {
            }
            refresh();
            return;
        }

        if (ekspresi.matches(".*\\(-[0-9]*\\.?[0-9]+\\)")) {
            int i = ekspresi.lastIndexOf("(-");
            String isi = ekspresi.substring(i + 2, ekspresi.length() - 1);
            ekspresi = ekspresi.substring(0, i) + isi;
        } else {
            String angka = angkaTerakhir();
            if (angka.isEmpty() || angka.endsWith(".")) return;
            int awal = ekspresi.length() - angka.length();
            ekspresi = ekspresi.substring(0, awal) + "(-" + angka + ")";
        }
        refresh();
    }

    private void hapusSatu() {
        if (sudahHasil) {
            sudahHasil = false;
            layar.setRiwayat(" ");
        }
        if (!ekspresi.isEmpty()) {
            ekspresi = ekspresi.substring(0, ekspresi.length() - 1);
        }
        refresh();
    }

    private void resetSemua() {
        ekspresi = "";
        sudahHasil = false;
        layar.setRiwayat(" ");
        refresh();
    }

    private void terapkanUnary(String nama, DoubleUnaryOperator op) {
        if (ekspresi.isEmpty()) return;

        String angka = angkaTerakhir();
        if (angka.isEmpty() || angka.endsWith(".")) return;

        try {
            double nilai = Double.parseDouble(angka);
            double hasil = op.applyAsDouble(nilai);
            if (Double.isNaN(hasil) || Double.isInfinite(hasil)) {
                tampilkanError("Hasil tidak valid");
                return;
            }
            int awal = ekspresi.length() - angka.length();
            String teksHasil = format(hasil);
            ekspresi = ekspresi.substring(0, awal) + teksHasil;
            refresh();
        } catch (NumberFormatException e) {
            tampilkanError("Format salah");
        } catch (ArithmeticException e) {
            tampilkanError(e.getMessage());
        }
    }

    private void hasilAkhir() {
        if (sudahHasil) return;

        String teks = ekspresi;
        while (!teks.isEmpty() && (OPERATOR + "(").indexOf(teks.charAt(teks.length() - 1)) >= 0) {
            teks = teks.substring(0, teks.length() - 1);
        }
        if (teks.isEmpty()) return;

        int kurang = hitungKarakter(teks, '(') - hitungKarakter(teks, ')');
        String lengkap = teks + ")".repeat(Math.max(0, kurang));

        try {
            double hasil = Penghitung.hitung(lengkap);
            if (Double.isNaN(hasil) || Double.isInfinite(hasil)) {
                tampilkanError("Hasil tidak valid");
                return;
            }
            String hasilStr = format(hasil);
            layar.setRiwayat(lengkap + " =");
            ekspresi = hasilStr;
            sudahHasil = true;
            refresh();
            riwayatList.add(new RiwayatItem(lengkap, hasilStr));
        } catch (ArithmeticException e) {
            tampilkanError(e.getMessage());
        } catch (IllegalArgumentException e) {
            tampilkanError("Format salah");
        }
    }

    private void tampilkanError(String pesan) {
        ekspresi = "";
        sudahHasil = false;
        layar.setText("Error");
        layar.setRiwayat(pesan);
    }

    private String format(double nilai) {
        return new BigDecimal(nilai).round(new MathContext(15)).stripTrailingZeros().toPlainString();
    }

    private void bukaDialogRiwayat() {
        if (dialogRiwayat != null && dialogRiwayat.isDisplayable()) {
            dialogRiwayat.dispose();
        }
        dialogRiwayat = new JDialog(this, "Riwayat Perhitungan", false);
        dialogRiwayat.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BODY);
        panel.setBorder(new BingkaiTimbul());

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(JUDUL);
        bar.setPreferredSize(new Dimension(100, 30));
        JLabel judul = new JLabel("  Riwayat");
        judul.setForeground(Color.WHITE);
        judul.setFont(font(13));
        bar.add(judul, BorderLayout.WEST);

        JButton tutup = new JButton("X");
        tutup.setForeground(Color.WHITE);
        tutup.setBackground(JUDUL);
        tutup.setBorderPainted(false);
        tutup.setFocusPainted(false);
        tutup.setFont(font(12));
        tutup.addActionListener(e -> dialogRiwayat.dispose());
        bar.add(tutup, BorderLayout.EAST);
        panel.add(bar, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(LAYAR_BG);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (riwayatList.isEmpty()) {
            JLabel kosong = new JLabel("Belum ada riwayat");
            kosong.setFont(font(12));
            kosong.setForeground(LAYAR_TEKS);
            listPanel.add(kosong);
        } else {
            for (int i = riwayatList.size() - 1; i >= 0; i--) {
                RiwayatItem item = riwayatList.get(i);
                JLabel label = new JLabel((riwayatList.size() - i) + ". " + item);
                label.setFont(font(12));
                label.setForeground(LAYAR_TEKS);
                label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
                listPanel.add(label);
            }
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        JButton hapus = new JButton("Hapus Semua");
        hapus.setFont(font(12));
        hapus.setBackground(AKSEN_KUNING);
        hapus.setForeground(TEKS_TOMBOL);
        hapus.setFocusPainted(false);
        hapus.addActionListener(e -> {
            riwayatList.clear();
            dialogRiwayat.dispose();
            bukaDialogRiwayat();
        });
        panel.add(hapus, BorderLayout.SOUTH);

        dialogRiwayat.setContentPane(panel);
        dialogRiwayat.setSize(320, 400);
        dialogRiwayat.setLocationRelativeTo(this);
        dialogRiwayat.setVisible(true);
    }

    private void bukaDialogKonverter() {
        if (dialogKonverter != null && dialogKonverter.isDisplayable()) {
            dialogKonverter.dispose();
        }
        dialogKonverter = new JDialog(this, "Converter", false);
        dialogKonverter.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BODY);
        panel.setBorder(new BingkaiTimbul());

        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(JUDUL);
        bar.setPreferredSize(new Dimension(100, 30));
        JLabel judul = new JLabel("  Converter");
        judul.setForeground(Color.WHITE);
        judul.setFont(font(13));
        bar.add(judul, BorderLayout.WEST);

        JButton tutup = new JButton("X");
        tutup.setForeground(Color.WHITE);
        tutup.setBackground(JUDUL);
        tutup.setBorderPainted(false);
        tutup.setFocusPainted(false);
        tutup.setFont(font(12));
        tutup.addActionListener(e -> dialogKonverter.dispose());
        bar.add(tutup, BorderLayout.EAST);
        panel.add(bar, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(0, 2, 4, 4));
        menuPanel.setBackground(LAYAR_BG);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (String nama : KATEGORI.keySet()) {
            JButton btn = new JButton(nama);
            btn.setFont(font(11));
            btn.setBackground(nama.equals(kategoriAktif) ? AKSEN_KUNING : MUKA);
            btn.setForeground(TEKS_TOMBOL);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                kategoriAktif = nama;
                dialogKonverter.dispose();
                bukaDialogKonverter();
            });
            menuPanel.add(btn);
        }

        JButton btnTip = new JButton("Tip");
        btnTip.setFont(font(11));
        btnTip.setBackground(kategoriAktif.equals("Tip") ? AKSEN_KUNING : MUKA);
        btnTip.setForeground(TEKS_TOMBOL);
        btnTip.setFocusPainted(false);
        btnTip.addActionListener(e -> {
            kategoriAktif = "Tip";
            dialogKonverter.dispose();
            bukaDialogKonverter();
        });
        menuPanel.add(btnTip);

        JScrollPane scroll = new JScrollPane(menuPanel);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        panel.add(buatPanelKonverter(kategoriAktif), BorderLayout.SOUTH);

        dialogKonverter.setContentPane(panel);
        dialogKonverter.setSize(380, 540);
        dialogKonverter.setLocationRelativeTo(this);
        dialogKonverter.setVisible(true);
    }

    private JPanel buatPanelKonverter(String kategori) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BODY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (kategori.equals("Tip")) {
            panel.add(buatPanelTip(), BorderLayout.CENTER);
            return panel;
        }

        KonverterKategori kk = KATEGORI.get(kategori);
        if (kk == null) return panel;

        JTextField inputField = new JTextField("1");
        inputField.setFont(font(18));
        inputField.setBackground(LAYAR_BG);
        inputField.setForeground(LAYAR_TEKS);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                new BingkaiTimbul(),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        inputField.setHorizontalAlignment(JTextField.RIGHT);

        JComboBox<Meteran> inputCombo = new JComboBox<>(kk.meteranList.toArray(new Meteran[0]));
        inputCombo.setFont(font(11));
        inputCombo.setBackground(MUKA);
        inputCombo.setForeground(TEKS_TOMBOL);

        JTextField outputField = new JTextField("1");
        outputField.setFont(font(18));
        outputField.setBackground(LAYAR_BG);
        outputField.setForeground(LAYAR_TEKS);
        outputField.setBorder(BorderFactory.createCompoundBorder(
                new BingkaiTimbul(),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        outputField.setHorizontalAlignment(JTextField.RIGHT);

        JComboBox<Meteran> outputCombo = new JComboBox<>(kk.meteranList.toArray(new Meteran[0]));
        outputCombo.setFont(font(11));
        outputCombo.setBackground(MUKA);
        outputCombo.setForeground(TEKS_TOMBOL);

        JPanel inputAtas = new JPanel(new BorderLayout(6, 0));
        inputAtas.setOpaque(false);
        inputAtas.add(inputField, BorderLayout.CENTER);
        inputAtas.add(inputCombo, BorderLayout.EAST);

        JPanel outputAtas = new JPanel(new BorderLayout(6, 0));
        outputAtas.setOpaque(false);
        outputAtas.add(outputField, BorderLayout.CENTER);
        outputAtas.add(outputCombo, BorderLayout.EAST);

        JPanel inputContainer = new JPanel(new BorderLayout(0, 4));
        inputContainer.setOpaque(false);
        JLabel labelInput = new JLabel("Input");
        labelInput.setFont(font(11));
        labelInput.setForeground(LAYAR_TEKS);
        inputContainer.add(labelInput, BorderLayout.NORTH);
        inputContainer.add(inputAtas, BorderLayout.CENTER);

        JPanel outputContainer = new JPanel(new BorderLayout(0, 4));
        outputContainer.setOpaque(false);
        JLabel labelOutput = new JLabel("Hasil");
        labelOutput.setFont(font(11));
        labelOutput.setForeground(LAYAR_TEKS);
        outputContainer.add(labelOutput, BorderLayout.NORTH);
        outputContainer.add(outputAtas, BorderLayout.CENTER);

        JPanel tengah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tengah.setOpaque(false);
        JButton tukar = new JButton("⇅ Tukar");
        tukar.setFont(font(11));
        tukar.setBackground(AKSEN_KUNING);
        tukar.setForeground(TEKS_TOMBOL);
        tukar.setFocusPainted(false);
        tukar.addActionListener(e -> {
            Object tmp = inputCombo.getSelectedItem();
            inputCombo.setSelectedItem(outputCombo.getSelectedItem());
            outputCombo.setSelectedItem(tmp);
            String tmpTeks = inputField.getText();
            inputField.setText(outputField.getText());
            outputField.setText(tmpTeks);
        });
        tengah.add(tukar);

        JPanel tengahPanel = new JPanel(new BorderLayout());
        tengahPanel.setOpaque(false);
        tengahPanel.add(inputContainer, BorderLayout.NORTH);
        tengahPanel.add(tengah, BorderLayout.CENTER);
        tengahPanel.add(outputContainer, BorderLayout.SOUTH);

        final boolean[] sedangUpdate = {false};

        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                konversi();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                konversi();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                konversi();
            }

            private void konversi() {
                if (sedangUpdate[0]) return;
                sedangUpdate[0] = true;
                try {
                    Meteran dari = (Meteran) inputCombo.getSelectedItem();
                    Meteran ke = (Meteran) outputCombo.getSelectedItem();
                    if (dari == null || ke == null) return;

                    String teks = inputField.getText().trim();
                    if (teks.isEmpty()) {
                        outputField.setText("");
                        return;
                    }

                    double nilai = Double.parseDouble(teks);
                    double hasil;

                    if (kategori.equals("Temperature")) {
                        hasil = konversiSuhu(nilai, dari.singkatan, ke.singkatan);
                    } else {
                        hasil = nilai * dari.faktor / ke.faktor;
                    }

                    DecimalFormat df = new DecimalFormat("#.##########");
                    outputField.setText(df.format(hasil));
                } catch (NumberFormatException ignored) {
                    outputField.setText("");
                } finally {
                    sedangUpdate[0] = false;
                }
            }
        };

        inputField.getDocument().addDocumentListener(listener);

        final boolean[] sedangUpdate2 = {false};
        DocumentListener listener2 = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                konversiBalik();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                konversiBalik();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                konversiBalik();
            }

            private void konversiBalik() {
                if (sedangUpdate2[0]) return;
                sedangUpdate2[0] = true;
                try {
                    Meteran dari = (Meteran) outputCombo.getSelectedItem();
                    Meteran ke = (Meteran) inputCombo.getSelectedItem();
                    if (dari == null || ke == null) return;

                    String teks = outputField.getText().trim();
                    if (teks.isEmpty()) {
                        inputField.setText("");
                        return;
                    }

                    double nilai = Double.parseDouble(teks);
                    double hasil;

                    if (kategori.equals("Temperature")) {
                        hasil = konversiSuhu(nilai, dari.singkatan, ke.singkatan);
                    } else {
                        hasil = nilai * dari.faktor / ke.faktor;
                    }

                    DecimalFormat df = new DecimalFormat("#.##########");
                    inputField.setText(df.format(hasil));
                } catch (NumberFormatException ignored) {
                    inputField.setText("");
                } finally {
                    sedangUpdate2[0] = false;
                }
            }
        };

        outputField.getDocument().addDocumentListener(listener2);

        inputCombo.addActionListener(e -> listener.insertUpdate(null));
        outputCombo.addActionListener(e -> listener.insertUpdate(null));

        panel.add(tengahPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatPanelTip() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setOpaque(false);

        JTextField subtotalField = new JTextField("0");
        subtotalField.setFont(font(16));
        subtotalField.setBackground(LAYAR_BG);
        subtotalField.setForeground(LAYAR_TEKS);
        subtotalField.setHorizontalAlignment(JTextField.RIGHT);

        JTextField tipPersenField = new JTextField("10");
        tipPersenField.setFont(font(16));
        tipPersenField.setBackground(LAYAR_BG);
        tipPersenField.setForeground(LAYAR_TEKS);
        tipPersenField.setHorizontalAlignment(JTextField.RIGHT);

        JTextField personField = new JTextField("1");
        personField.setFont(font(16));
        personField.setBackground(LAYAR_BG);
        personField.setForeground(LAYAR_TEKS);
        personField.setHorizontalAlignment(JTextField.RIGHT);

        JTextField totalField = new JTextField("0");
        totalField.setFont(font(16));
        totalField.setBackground(LAYAR_BG);
        totalField.setForeground(LAYAR_TEKS);
        totalField.setEditable(false);
        totalField.setHorizontalAlignment(JTextField.RIGHT);

        JTextField perPersonField = new JTextField("0");
        perPersonField.setFont(font(16));
        perPersonField.setBackground(LAYAR_BG);
        perPersonField.setForeground(LAYAR_TEKS);
        perPersonField.setEditable(false);
        perPersonField.setHorizontalAlignment(JTextField.RIGHT);

        Runnable hitung = () -> {
            try {
                double subtotal = Double.parseDouble(subtotalField.getText().trim().isEmpty() ? "0" : subtotalField.getText().trim());
                double tipPersen = Double.parseDouble(tipPersenField.getText().trim().isEmpty() ? "0" : tipPersenField.getText().trim());
                double person = Double.parseDouble(personField.getText().trim().isEmpty() ? "1" : personField.getText().trim());
                if (person <= 0) person = 1;

                double tip = subtotal * tipPersen / 100.0;
                double total = subtotal + tip;
                double perPerson = total / person;

                DecimalFormat df = new DecimalFormat("#.##########");
                totalField.setText(df.format(total));
                perPersonField.setText(df.format(perPerson));
            } catch (NumberFormatException ignored) {
            }
        };

        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                hitung.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                hitung.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                hitung.run();
            }
        };

        subtotalField.getDocument().addDocumentListener(dl);
        tipPersenField.getDocument().addDocumentListener(dl);
        personField.getDocument().addDocumentListener(dl);

        panel.add(buatBarisTip("Subtotal", subtotalField));
        panel.add(buatBarisTip("Tip (%)", tipPersenField));
        panel.add(buatBarisTip("Total", totalField));
        panel.add(buatBarisTip("Per Orang", perPersonField));
        panel.add(buatBarisTip("Jumlah Orang", personField));

        return panel;
    }

    private JPanel buatBarisTip(String label, JTextField field) {
        JPanel baris = new JPanel(new BorderLayout(8, 0));
        baris.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(font(12));
        l.setForeground(LAYAR_TEKS);
        l.setPreferredSize(new Dimension(100, 30));
        baris.add(l, BorderLayout.WEST);
        field.setBorder(BorderFactory.createCompoundBorder(
                new BingkaiTimbul(),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        baris.add(field, BorderLayout.CENTER);
        return baris;
    }

    private double konversiSuhu(double nilai, String dari, String ke) {
        if (dari.equals(ke)) return nilai;

        double celsius;
        switch (dari) {
            case "°C" -> celsius = nilai;
            case "°F" -> celsius = (nilai - 32) * 5.0 / 9.0;
            case "K" -> celsius = nilai - 273.15;
            default -> celsius = nilai;
        }

        switch (ke) {
            case "°C" -> {
                return celsius;
            }
            case "°F" -> {
                return celsius * 9.0 / 5.0 + 32;
            }
            case "K" -> {
                return celsius + 273.15;
            }
            default -> {
                return celsius;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Kalkulator().setVisible(true));
    }
}
