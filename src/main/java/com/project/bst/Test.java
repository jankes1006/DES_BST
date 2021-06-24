package com.project.bst;

import com.project.bst.key.KeysGenerator;
import com.project.bst.key.PART;
import com.project.bst.key.ReadPCException;
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

    @RequestMapping(method = RequestMethod.GET, value = "hello")
    public String sayHello(@RequestParam long key)throws ReadPCException {
        System.out.println(keysGenerator.calculateKeys(1383827165325090801L));
        return "Hello";
    }
}
