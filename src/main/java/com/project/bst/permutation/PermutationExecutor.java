package com.project.bst.permutation;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermutationExecutor {
    private long power(long a, int b){
        long result = 1L;
        for(int i=0; i<b; i++) result*=a;
        return result;
    }

    public Long executePermutation(long key, List<Integer> permutationTable,TYPE_PERMUTATION typePermutation){

        int elementNumber = permutationTable.size()-1;
        long result = 0L;

        int numberOfBytes;

        switch (typePermutation) {
            case PC1:
                numberOfBytes = 64;
                break;

            case PC2:
                numberOfBytes = 56;
                break;

            case EXPANSION_SBOX:
                numberOfBytes = 32;
                break;

            case PERMUTATION_SBOX:
                numberOfBytes = 32;
                break;

            case INITIAL_PERMUTATION:
                numberOfBytes = 64;
                break;

            case FINAL_PERMUTATION:
                numberOfBytes = 64;
                break;
            default:
                return 0L;
        }

        for(Integer value : permutationTable){
            long helper = key & power(2,numberOfBytes-value);

            if(helper!=0) {
                result |= power(2,elementNumber);
            }

            elementNumber --;
        }

        return result;
    }
}
