package com.project.bst.round;

import com.project.bst.sBoxes.Sbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Round {
    @Autowired
    Sbox sbox;

    private long divisionNumber(long number, PARTNUMBER part){
        long result;
        if(part.equals(PARTNUMBER.RIGHT))
            result = number & 0xFFFFFFFFL;
        else
            result = (number>>32) & 0xFFFFFFFFL;
        return result;
    }

    private long shiftToLeft(long number){
        return (number<<32);
    }

    public long calculateRound(long data, long key){
        long rightPart = divisionNumber(data,PARTNUMBER.RIGHT);
        long leftPart = divisionNumber(data,PARTNUMBER.LEFT);
        long functionFResult = sbox.calculateSBOX(rightPart,key);

        long fXorLeft = functionFResult ^ leftPart;
        long result = shiftToLeft(rightPart);
        result |= fXorLeft;
        return result;
    }
}
