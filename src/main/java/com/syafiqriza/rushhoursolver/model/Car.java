package main.java.com.syafiqriza.rushhoursolver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Merepresentasikan sebuah mobil dalam puzzle Rush Hour.
 * Setiap mobil memiliki ID, posisi (baris, kolom), panjang, dan orientasi (horizontal/vertikal).
 */
public class Car {
    private final char id; // ID unik dari mobil (contoh: "A", "B", "P")
    private final int length; // Jumlah sel yang ditempati oleh mobil
    private final boolean isHorizontal; // Orientasi mobil: true jika horizontal
    private int row, col; // Posisi kepala mobil (sel paling kiri/atas)

    /**
     * Konstruktor untuk membuat objek mobil baru.
     * @param id ID unik mobil
     * @param length Panjang mobil (jumlah sel yang ditempati)
     * @param isHorizontal Orientasi mobil (true jika horizontal)
     * @param row Baris awal (kepala mobil)
     * @param col Kolom awal (kepala mobil)
     */
    public Car(char id, int length, boolean isHorizontal, int row, int col) {
        this.id = id;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.row = row;
        this.col = col;
    }

    public char getId() { return id; }
    public int getLength() { return length; }
    public boolean isHorizontal() { return isHorizontal; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    /**
     * Menggerakkan mobil sejauh offset dalam arah orientasinya.
     * @param offset Jumlah sel untuk bergerak (positif atau negatif)
     */
    public void move(int offset) {
        if (isHorizontal) col += offset;
        else row += offset;
    }

    /**
     * Membatalkan gerakan sebelumnya dengan arah yang berlawanan.
     * @param offset Offset yang sama dengan yang digunakan di move()
     */
    public void undoMove(int offset) {
        move(-offset);
    }

    /**
     * Mendapatkan daftar semua sel yang ditempati oleh mobil ini.
     * @return List berisi pasangan [baris, kolom] untuk setiap sel yang ditempati
     */
    public List<int[]> getOccupiedCells() {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            cells.add(isHorizontal ? new int[]{row, col + i} : new int[]{row + i, col});
        }
        return cells;
    }

    /**
     * Membuat salinan baru dari mobil ini.
     * @return Objek Car baru dengan nilai properti yang sama
     */
    public Car copy() {
        return new Car(id, length, isHorizontal, row, col);
    }
}
