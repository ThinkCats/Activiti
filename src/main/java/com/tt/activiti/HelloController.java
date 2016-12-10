/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tt.activiti;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tt
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String index(){
        return "hello world";
    }
}
