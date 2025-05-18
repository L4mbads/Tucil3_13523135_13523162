# Tugas Kecil 3 IF2211 2024/2025
![main menu](rush_hour_solver.PNG)
### Tentang Projek

**Rush Hour Solver** adalah aplikasi Java berbasis GUI dan CLI yang dirancang untuk menyelesaikan puzzle permainan *Rush Hour* menggunakan algoritma pencarian seperti **Uniform Cost Search (UCS)**, **Greedy Best First Search (GBFS)**, dan **A\***. Program ini dikembangkan untuk memenuhi tugas mata kuliah IF2211 Strategi Algoritma pada Semester 2 Tahun Akademik 2024/2025.

Pengguna dapat memilih algoritma dan heuristik (untuk GBFS dan A\*) lalu memuat file puzzle untuk diselesaikan secara visual atau melalui terminal. Hasil solusi dapat direplay dan disimpan ke file `.txt`.

---

### Persyaratan

* **JDK 21**
* **Gradle (opsional)** untuk compile dengan GUI
* JavaFX sudah disertakan otomatis melalui konfigurasi Gradle

---

### Instalasi

1. **Instal JDK 21**

   * Unduh JDK 21 dari [situs resmi OpenJDK](https://jdk.java.net/21/)
   * Ikuti instruksi instalasi sesuai OS

2. **Clone repository ini**

```bash
git clone https://github.com/username/repo-rush-hour.git
```

---
### Compile CLI (jika tidak memiliki Gradle)
```bash
cd ./path-ke-folder
./build.sh
```
### Compile GUI + CLI
```bash
cd ./path-ke-folder
gradle clean shadowJar
```

### Menjalankan Program

#### GUI


1. **Buka terminal dan masuk ke direktori**

```bash
cd ./path-ke-folder
```

2. **Jalankan program**

```bash
java -jar bin/libs/rush-hour-solver.jar
```

#### CLI

1. **Buka terminal dan masuk ke direktori**

```bash
cd ./path-ke-folder
```

2. **Jalankan program CLI**

```bash
java -jar bin/libs/rush-hour-solver.jar cli
java -jar bin/libs/rush-hour-solver.jar cli path/to/file.txt

```

### Author

* Ahmad Syafiq - 13523135
* Fachriza Ahmad Setyono - 13523162
