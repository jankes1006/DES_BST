package com.project.bst.key;

import com.project.bst.permutation.PermutationExecutor;
import com.project.bst.permutation.TYPE_PERMUTATION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class KeysGenerator {

    @Autowired
    private PermutationExecutor permutationExecutor;

    private List<Integer> PC_1 = new ArrayList();
    private List<Integer> PC_2 = new ArrayList<>();

    public KeysGenerator()throws ReadPCException{
        try {
            readPC_1();
            readPC_2();
        }catch(FileNotFoundException e){
            throw new ReadPCException();
        }
    }

    private void readPC_1()throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("Key/PC-1.txt"));
        while(scanner.hasNext()){
            PC_1.add(Integer.valueOf(scanner.next()));
        }
        scanner.close();
    }

    private void readPC_2()throws FileNotFoundException{
        Scanner scanner = new Scanner(new File("Key/PC-2.txt"));
        while (scanner.hasNext()){
            PC_2.add(Integer.valueOf(scanner.next()));
        }
        scanner.close();
    }

    private long readRightLeftPartKey(long key, PART leftRight){
        if(leftRight.equals(PART.LEFT))
            key=key>>28;

        long result = 0L;
        for(int i=27;i>=0; i--){
            result |= key & (1<<i);
        }

        if(leftRight.equals(PART.LEFT))
            return result<<28;
        else
            return result;
    }

    private long rotateRightLeftPartKey(long key, PART leftRight){
        if(leftRight.equals(PART.LEFT))
            key=key>>28;

        long result;
        result = (key<<1);
        if((result & (1<<28))>0) result |= 1;
        result &= ~(1<<28);

        if(leftRight.equals(PART.LEFT))
            return result<<28;
        else
            return result;
    }

    private int howManyBitsToShift(int i){
        if(i==1 || i==2 || i==9 || i==16){
            return 1;
        }
        return 2;
    }

    private long shiftLeftRightPart(long leftRightPart, int i,PART part){
        for(int j=0; j<howManyBitsToShift(i); j++){
            leftRightPart = rotateRightLeftPartKey(leftRightPart,part);
        }
        return leftRightPart;
    }

    public List<Long> calculateKeys(long key){
        List<Long> resultKeys = new ArrayList<>();
        long inputData = permutationExecutor.executePermutation(key,PC_1, TYPE_PERMUTATION.PC1);
        long leftPart = readRightLeftPartKey(inputData,PART.LEFT);
        long rightPart = readRightLeftPartKey(inputData,PART.RIGHT);

        System.out.println("0 "+leftPart+" "+rightPart);

        for(int i=1; i<=16; i++){
            leftPart = shiftLeftRightPart(leftPart,i,PART.LEFT);
            rightPart = shiftLeftRightPart(rightPart,i,PART.RIGHT);
            System.out.println(i+" "+leftPart+" "+rightPart);
            inputData = permutationExecutor.executePermutation((leftPart | rightPart),PC_2,TYPE_PERMUTATION.PC2);
            resultKeys.add(inputData);
        }

        return resultKeys;
    }
}
