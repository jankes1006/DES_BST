package com.project.bst.des;

import com.project.bst.key.KeysGenerator;
import com.project.bst.permutation.PermutationExecutor;
import com.project.bst.permutation.TYPE_PERMUTATION;
import com.project.bst.round.Round;
import com.project.bst.sBoxes.ReadExpansionPermutationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class DesExecutor {
    @Autowired
    KeysGenerator keysGenerator;
    @Autowired
    PermutationExecutor permutationExecutor;
    @Autowired
    Round round;

    List<Integer> initialPermutationTable = new ArrayList<>();
    List<Integer> finalPermutationTable = new ArrayList<>();

    public DesExecutor(){
        try{
            readInitialPermutation();
            readFinalPermutation();
        }catch(Exception e){
        }
    }

    private void readInitialPermutation() throws ReadInitialPermutationException{
        try {
            Scanner scanner = new Scanner(new File("InitialAndFinalPermutation/initialPermutation.txt"));
            while(scanner.hasNext()){
                initialPermutationTable.add(scanner.nextInt());
            }
        }catch(FileNotFoundException e){
            throw new ReadInitialPermutationException();
        }
    }

    private void readFinalPermutation() throws ReadFinalPermutationException{
        try {
            Scanner scanner = new Scanner(new File("InitialAndFinalPermutation/finalPermutation.txt"));
            while(scanner.hasNext()){
                finalPermutationTable.add(scanner.nextInt());
            }
        }catch(FileNotFoundException e){
            throw new ReadFinalPermutationException();
        }
    }

    private long changePartNumber(long number){
        long result = (number>>32) & 0xFFFFFFFFL;
        result |= (number<<32);
        return result;
    }

    public long useDES(long data, long key,OPTION option) {
        long inputData = permutationExecutor.executePermutation(data, initialPermutationTable, TYPE_PERMUTATION.INITIAL_PERMUTATION);
        List<Long> Keys = keysGenerator.calculateKeys(key);

        if(option.equals(OPTION.DECODE)) {
            for (int i = 15; i >= 0; i--) {
                inputData = round.calculateRound(inputData, Keys.get(i));
            }
        }else{
            for (int i = 0; i < 16; i++) {
                inputData = round.calculateRound(inputData, Keys.get(i));
            }
        }

        inputData = changePartNumber(inputData);

        long result = permutationExecutor.executePermutation(inputData,finalPermutationTable,TYPE_PERMUTATION.FINAL_PERMUTATION);
        return result;
    }
}
