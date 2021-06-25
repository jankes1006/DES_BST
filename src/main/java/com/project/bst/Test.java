package com.project.bst;

import com.project.bst.key.KeysGenerator;
import com.project.bst.key.PART;
import com.project.bst.key.ReadPCException;
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

    @RequestMapping(method = RequestMethod.GET, value = "hello")
    public String sayHello(@RequestParam long key, int value) {
        sbox.calculateSBOX(4037734570L,29699430183026L);
        return "Hello";
    }
}
