package main.java.rushhoursolver.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Utils {
    public static Board readRushHourPuzzleFromFile(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));

        if (!sc.hasNextLine())
            throw new IllegalArgumentException("Format file tidak valid! File kosong.");

        String[] dimLine = sc.nextLine().trim().split("\\s+");
        if (dimLine.length != 2)
            throw new IllegalArgumentException("Format file tidak valid! Baris pertama harus memiliki 2 nilai: rows dan cols. Diberikan: " + Arrays.toString(dimLine));

        int rows, cols;
        try {
            rows = Integer.parseInt(dimLine[0]);
            cols = Integer.parseInt(dimLine[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format file tidak valid! Baris pertama harus berupa angka. Diberikan: " + Arrays.toString(dimLine));
        }

        if (!sc.hasNextLine())
            throw new IllegalArgumentException("Format file tidak valid! Baris kedua (jumlah kendaraan) tidak ditemukan.");

        String jumlahLine = sc.nextLine().trim();
        int jumlahNonP;
        try {
            jumlahNonP = Integer.parseInt(jumlahLine);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format file tidak valid! Baris kedua harus berupa angka. Diberikan: " + jumlahLine);
        }

        List<String> gridLines = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().stripTrailing();
            if (!line.isEmpty()) gridLines.add(line);
        }

        if (gridLines.size() < rows)
            throw new IllegalArgumentException("Format file tidak valid! Expected " + rows + " baris papan, diberikan: " + gridLines.size());

        boolean hasExtraKLine = gridLines.size() == rows + 1;
        int goalRow = -1, goalCol = -1;

        Map<Character, List<int[]>> charToCells = new HashMap<>();
        Set<String> occupied = new HashSet<>();

        for (int i = 0; i < rows; i++) {
            String rowLine = gridLines.get(i);
            if (rowLine.length() > cols + 1)
                throw new IllegalArgumentException("Format file tidak valid! Baris ke-" + i + " terlalu panjang. Expected max " + (cols + 1) + ", diberikan: " + rowLine.length());

            for (int j = 0; j < rowLine.length(); j++) {
                char ch = rowLine.charAt(j);
                if (ch == '.' || ch == ' ') continue;

                if (j >= cols) {
                    if (ch != 'K')
                        throw new IllegalArgumentException("Format file tidak valid! Karakter di luar grid hanya boleh 'K'. Diberikan: '" + ch + "'");
                    if (goalRow != -1)
                        throw new IllegalArgumentException("Format file tidak valid! Duplikat 'K' ditemukan.");
                    goalRow = i;
                    goalCol = j;
                    continue;
                }

                String key = i + "," + j;
                if (occupied.contains(key))
                    throw new IllegalArgumentException("Format file tidak valid! Tumpang tindih kendaraan di posisi (" + i + "," + j + ")");

                if (ch == 'K') {
                    if (goalRow != -1)
                        throw new IllegalArgumentException("Format file tidak valid! Duplikat 'K' ditemukan.");
                    goalRow = i;
                    goalCol = j;
                } else {
                    occupied.add(key);
                    charToCells.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{i, j});
                }
            }
        }

        if (hasExtraKLine) {
            String extra = gridLines.get(rows);
            int idx = extra.indexOf('K');
            if (idx == -1 || goalRow != -1)
                throw new IllegalArgumentException("Format file tidak valid! Baris tambahan tidak valid atau duplikat 'K'.");
            goalRow = rows;
            goalCol = idx;
        }

        if (goalRow == -1 || goalCol == -1)
            throw new IllegalArgumentException("Format file tidak valid! Posisi goal 'K' tidak ditemukan.");

        Map<Character, Car> cars = new HashMap<>();
        char goalCarId = 'P';

        for (Map.Entry<Character, List<int[]>> entry : charToCells.entrySet()) {
            char id = entry.getKey();
            List<int[]> posList = entry.getValue();
            if (posList.size() < 2)
                throw new IllegalArgumentException("Format file tidak valid! Mobil '" + id + "' panjangnya kurang dari 2. Diberikan: " + posList.size());

            posList.sort(Comparator.comparingInt(p -> p[0] * cols + p[1]));
            int[] head = posList.get(0);
            int[] second = posList.get(1);

            boolean isHorizontal = head[0] == second[0];
            if (!isHorizontal && head[1] != second[1])
                throw new IllegalArgumentException("Format file tidak valid! Mobil '" + id + "' tidak lurus horizontal atau vertikal.");

            cars.put(id, new Car(id, posList.size(), isHorizontal, head[0], head[1]));
        }

        if (!cars.containsKey(goalCarId))
            throw new IllegalArgumentException("Format file tidak valid! Mobil utama 'P' tidak ditemukan.");

        Car goalCar = cars.get(goalCarId);
        if ((goalCar.isHorizontal() && goalCar.getRow() != goalRow) ||
                (!goalCar.isHorizontal() && goalCar.getCol() != goalCol)) {
            throw new IllegalArgumentException("Format file tidak valid! Posisi goal (K) tidak searah dengan mobil 'P'. Diharapkan baris: " + goalCar.getRow() + " atau kolom: " + goalCar.getCol() + ", diberikan: (" + goalRow + "," + goalCol + ")");
        }

        return new Board(rows, cols, cars, goalCarId, goalRow, goalCol);
    }
}
