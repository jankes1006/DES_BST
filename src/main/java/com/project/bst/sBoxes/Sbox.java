package com.project.bst.sBoxes;

import com.project.bst.permutation.PermutationExecutor;
import com.project.bst.permutation.TYPE_PERMUTATION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Sbox {
    @Autowired
    PermutationExecutor permutationExecutor;

    private List<Integer> ExpansionTable = new ArrayList<>();
    private List<Integer> PermutationTable = new ArrayList<>();
    private List<Integer> s1Table = new ArrayList<>();
    private List<Integer> s2Table = new ArrayList<>();
    private List<Integer> s3Table = new ArrayList<>();
    private List<Integer> s4Table = new ArrayList<>();
    private List<Integer> s5Table = new ArrayList<>();
    private List<Integer> s6Table = new ArrayList<>();
    private List<Integer> s7Table = new ArrayList<>();
    private List<Integer> s8Table = new ArrayList<>();
    private List<List<Integer>> sTables = new ArrayList<>();

    public Sbox() {
        sTables.add(s1Table);
        sTables.add(s2Table);
        sTables.add(s3Table);
        sTables.add(s4Table);
        sTables.add(s5Table);
        sTables.add(s6Table);
        sTables.add(s7Table);
        sTables.add(s8Table);
        try {
            readSBoxTable();
            readExpansionPermutation();
        } catch (Exception e) {
        }
    }

    private void readExpansionPermutation() throws ReadExpansionPermutationException {
        try {
            Scanner scanner = null;
            for (int i = 0; i < 2; i++) {
                if (i == 0)
                    scanner = new Scanner(new File("Box/ExpansionE.txt"));
                else
                    scanner = new Scanner(new File("Box/PermutationP.txt"));

                while (scanner.hasNext()) {
                    if (i == 0)
                        ExpansionTable.add(scanner.nextInt());
                    else
                        PermutationTable.add(scanner.nextInt());
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new ReadExpansionPermutationException();
        }
    }

    private void readSBoxTable() throws ReadSBoxException { //sprawdz czy odczytalo poprawnie dane
        Scanner scanner;
        try {
            for (int i = 1; i <= 8; i++) {
                String fileName = "Box/sbox" + i + ".txt";
                scanner = new Scanner(new File(fileName));

                while (scanner.hasNext())
                    sTables.get(i - 1).add(scanner.nextInt());
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            throw new ReadSBoxException();
        }
    }

    public long preparePartShiftRight(long data, int which) {
        int shift = (8 - which) * 6;
        return ((data >> shift) & 0b111111);
    }

    public long preparePartShiftLeft(long data, int which) {
        int shift = (8 - which) * 4;
        return (data << shift);
    }

    private long calculateRow(long data) {
        long result = 0L;
        if ((data & 0b100000) > 0) result = 2;
        if ((data & 0b1) > 0) result |= 1;

        return result;
    }

    private long calculateColumn(long data) {
        return ((data & 0b011110) >> 1);
    }

    public long calculateSBOX(long data, long key) {
        long input = permutationExecutor.executePermutation(data, ExpansionTable, TYPE_PERMUTATION.EXPANSION_SBOX);
        input = (input ^ key) & (0xFFFFFFFFFFFFL);

        long result = 0L;
        for (int i = 0; i < 8; i++) {
            long temp = preparePartShiftRight(input, i + 1);
            long column = calculateColumn(temp);
            long row = calculateRow(temp);
            int index = Math.toIntExact(row * 16 + column);
            result |= preparePartShiftLeft(sTables.get(i).get(index), i + 1);
        }

        result = permutationExecutor.executePermutation(result, PermutationTable, TYPE_PERMUTATION.EXPANSION_SBOX);
        return result;
    }
}
