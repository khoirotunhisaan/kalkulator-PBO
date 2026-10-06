# 🧮 Kalkulator Java — Retro Pixel Style

Kalkulator GUI berbasis **Java Swing** dengan tampilan retro pixel art. Dilengkapi **converter 9 kategori**, **riwayat perhitungan**, **tip calculator**, dan **operasi matematika lengkap** (x², x³, √x, ∛x, x^y, ʸ√x, 1/x, |x|).

---

## ✨ Fitur

### 🎨 Tampilan
- **Retro pixel art** — semua komponen digambar manual pakai `fillRect` tanpa antialias
- **Tema biru muda + kuning** — warna lembut, enak dilihat
- **Custom title bar** — bisa di-drag, minimize, maximize, close
- **Font pixel opsional** — taruh `PressStart2P-Regular.ttf` di folder yang sama untuk tampilan pixel sejati
- **Responsive** — ukuran window bisa di-resize, font layar auto-shrink

### 🔢 Kalkulator
- **Ekspresi lengkap** — ketik semuanya dulu, hasil muncul saat `=`
- **Prioritas operator** — `x` dan `/` didahulukan dari `+` dan `-`
- **Kurung** `( )` — dengan auto-close
- **Persen** `%`
- **Ganti tanda** `+/-`
- **Desimal** `.`
- **DEL** — hapus satu karakter
- **C** — reset semua
- **Input keyboard** — angka, operator, `Enter`, `Backspace`, `Esc`

### 🧪 Operasi Unary (x)
| Tombol | Fungsi |
|--------|--------|
| `x²` | Kuadrat |
| `x³` | Kubik |
| `√x` | Akar kuadrat |
| `∛x` | Akar kubik |
| `x^y` | Pangkat y |
| `ʸ√x` | Akar pangkat y |
| `1/x` | Kebalikan |
| `\|x\|` | Nilai absolut |

### 🔄 Converter (9 Kategori)
| Kategori | Meteran |
|----------|---------|
| **Area** | hektar, cm², m², acres, ares, ft², in² |
| **Length** | mm, cm, m, km, in, ft, yd, mi, nautical mi, mil |
| **Temperature** | Celsius, Fahrenheit, Kelvin |
| **Volume** | UK gallons, US gallons, L, mL, cm³, m³, in³, ft³ |
| **Mass** | tons, UK tons, US tons, pounds, ounces, kg, g |
| **Data** | bits, bytes, KB, KiB, MB, MiB, GB, GiB, TB, TiB |
| **Speed** | m/s, m/h, km/s, km/h, in/s, in/h, ft/s, ft/h, mi/s, mi/h, knots |
| **Time** | ms, s, min, h, d, week, month, year |
| **Tip** | Subtotal, Tip %, Total, Per Orang, Jumlah Orang |

**Fitur converter:**
- **Bidirectional** — input dan output bisa diedit dua-duanya
- **Real-time** — hasil langsung muncul saat mengetik
- **Dropdown meteran** — keterangan panjang + singkatan (contoh: `Meter (m)`)
- **Tombol Tukar** — swap input ⇄ output dengan satu klik

### 🕐 Riwayat
- **Tombol jam** di title bar untuk buka popup riwayat
- **List scrollable** — semua perhitungan tersimpan
- **Hapus semua** — reset riwayat dengan satu klik

---

## 🚀 Cara Menjalankan

### Prasyarat
- **Java JDK 17+** (karena pakai switch rule `->` dan `List.of()`)
- Terminal / Command Prompt

### Langkah
```bash
# 1. Clone repo
git clone https://github.com/username/kalkulator-java.git
cd kalkulator-java

# 2. Compile
javac Kalkulator.java

# 3. Jalankan
java Kalkulator
