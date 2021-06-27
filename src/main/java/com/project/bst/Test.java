package com.project.bst;

import com.project.bst.des.DesExecutor;
import com.project.bst.des.OPTION;
import com.project.bst.key.KeysGenerator;
import com.project.bst.key.PART;
import com.project.bst.key.ReadPCException;
import com.project.bst.round.PARTNUMBER;
import com.project.bst.round.Round;
import com.project.bst.sBoxes.Sbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bst/v1")
public class Test {
    @Autowired
    KeysGenerator keysGenerator;
    @Autowired
    Sbox sbox;
    @Autowired
    Round round;
    @Autowired
    DesExecutor des;

    @RequestMapping(method = RequestMethod.GET, value = "hello")
    public String sayHello() {
        long data = des.useDES(1383827165325090801L,29699430183026L, OPTION.CODE);
        System.out.println(des.useDES(data,29699430183026L,OPTION.DECODE));
        return "Hello";
    }
}
