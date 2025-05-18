package com.syafiqriza.rushhoursolver.model;

import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
        int goalRow = -2, goalCol = -2;

        Map<Character, List<int[]>> charToCells = new HashMap<>();
        Set<String> occupied = new HashSet<>();

        boolean leftK = false;

        for (int i = 0, finali = 0; i < rows; i++, finali++) {
            String rowLine = gridLines.get(finali);
            if (rowLine.length() > cols + 1)
                throw new IllegalArgumentException("Format file tidak valid! Baris ke-" + i + " terlalu panjang. Expected max " + (cols + 1) + ", diberikan: " + rowLine.length());

            if(hasExtraKLine) {
                if(i == 0) {
                    int idx = rowLine.indexOf('K');
                    if (idx != -1) {
                        goalRow = -1;
                        goalCol = idx;
                        finali++;
                        rowLine = gridLines.get(finali);
                    }
                }
            }

            for (int j = 0; j < rowLine.length(); j++) {
                char ch = rowLine.charAt(j);
                if(!hasExtraKLine && j == 0 && ch == 'K') {
                    leftK = true;
                    if (goalRow != -2)
                        throw new IllegalArgumentException("Format file tidak valid! Duplikat 'K' ditemukan.");
                    goalRow = i;
                    goalCol = j-1;
                    continue;
                } else if (!hasExtraKLine && j == 0 && ch == ' ') {
                    leftK = true;
                    continue;
                } else if (ch == '.') {
                    continue;
                }

                int finalj = leftK ? j-1 : j;
                if (finalj >= cols) {
                    if (ch != 'K')
                        throw new IllegalArgumentException("Format file tidak valid! Karakter di luar grid hanya boleh 'K'. Diberikan: '" + ch + "'" + finalj);
                    if (goalRow != -2)
                        throw new IllegalArgumentException("Format file tidak valid! Duplikat 'K' ditemukan.");
                    goalRow = i;
                    goalCol = finalj;
                    continue;
                }


                String key = i + "," + finalj;
                System.out.println(ch+key);

                if(ch == 'K') {
                    throw new IllegalArgumentException("Format file tidak valid! Mobil tidak boleh memiliki simbol K");
                }

                if (occupied.contains(key))
                    throw new IllegalArgumentException("Format file tidak valid! Tumpang tindih kendaraan di posisi (" + i + "," + finalj + ")");

                occupied.add(key);
                charToCells.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{i, finalj});
            }
        }

        if (goalRow == -2 && hasExtraKLine) {
            String extra = gridLines.get(rows);
            int idx = extra.indexOf('K');
            if ((idx == -1 && goalRow == -2) || (idx != -1 && goalRow != -2))
                throw new IllegalArgumentException("Format file tidak valid! Baris tambahan tidak valid atau duplikat 'K'.");
            goalRow = rows;
            goalCol = idx;
        }

        if (goalRow == -2 || goalCol == -2)
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

            // cek gap
            if (isHorizontal) {
                for (int i = 1; i < posList.size(); i++) {
                    if (posList.get(i)[1] != posList.get(i - 1)[1] + 1 || posList.get(i)[0] != posList.get(i-1)[0]) {
                        throw new IllegalArgumentException("Format file tidak valid! Mobil '" + id + "' memiliki celah pada kolom.");
                    }
                }
            } else {
                for (int i = 1; i < posList.size(); i++) {
                    if (posList.get(i)[0] != posList.get(i - 1)[0] + 1 || posList.get(i)[1] != posList.get(i-1)[1])  {
                        throw new IllegalArgumentException("Format file tidak valid! Mobil '" + id + "' memiliki celah pada baris.");
                    }
                }
            }

            cars.put(id, new Car(id, posList.size(), isHorizontal, head[0], head[1]));
        }

        if(jumlahNonP != cars.size() - 1) {
            throw new IllegalArgumentException("Format file tidak valid! Jumlah mobil non-utama berjumlah " + (cars.size() - 1) + " dari " + jumlahNonP);
        }

        if (!cars.containsKey(goalCarId))
            throw new IllegalArgumentException("Format file tidak valid! Mobil utama 'P' tidak ditemukan.");

        Car goalCar = cars.get(goalCarId);
        if ((goalCar.isHorizontal() && goalCar.getRow() != goalRow) ||
                (!goalCar.isHorizontal() && goalCar.getCol() != goalCol)) {
            throw new IllegalArgumentException("Format file tidak valid! Posisi goal (K) tidak searah dengan mobil 'P'. Diharapkan baris: " + goalCar.getRow() + " atau kolom: " + goalCar.getCol() + ", diberikan: (" + goalRow + "," + goalCol + ")");
        }

        return new Board(rows, cols, cars, goalCarId, goalRow, goalCol, "Start state");
    }

    public static String formatSolutionOutput(Algorithm.SolutionData solutionData) {
        StringBuilder outputBuilder = new StringBuilder();

        if (solutionData.states != null) {
            outputBuilder.append("Solusi:\n");
            for (State state : solutionData.states) {
                outputBuilder.append(state.getBoard().getDetail()).append("\n");
                outputBuilder.append(state.getBoard().toString()).append("\n\n");
            }
            outputBuilder.append("Solusi ditemukan\n");
        } else {
            outputBuilder.append("Solusi tidak ditemukan\n");
        }

        outputBuilder.append("\nWaktu (ms)      : ").append(solutionData.timeElapsedMs).append("\n");
        outputBuilder.append("Node dikunjungi : ").append(solutionData.nodeCount).append("\n");
        if (solutionData.states != null)
            outputBuilder.append("Jumlah langkah  : ").append(solutionData.states.length - 1).append("\n");

        return outputBuilder.toString();
    }

    public static void saveSolutionToFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

}
