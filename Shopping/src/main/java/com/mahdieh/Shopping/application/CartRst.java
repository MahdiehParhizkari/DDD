package com.mahdieh.Shopping.application;
/*
  @project DDD
  @Author Mahdieh Parhizkari
  @Date 11/17/21
  @Time 10:14 AM
  Created by Intellije IDEA
  Description: Application Layer
*/

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahdieh.Shopping.domain.entity.Cart;
import com.mahdieh.Shopping.domain.service.CartSrv;
import com.mahdieh.Shopping.infrastructure.resource.PeopleRso;
import com.mahdieh.Shopping.infrastructure.resource.ProductRso;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/shopping")
public class CartRst {
    @Autowired private CartSrv srv;
    @Autowired private ProductRso productRso;
    @Autowired private PeopleRso peopleRso;

    @PostMapping(value = "/who")
    public String whoami(@RequestBody String receiveData) throws IOException {
        return (new ObjectMapper()).writeValueAsString(peopleRso.find(receiveData));
    }

    @PostMapping(value = "/showcart")
    public String find() throws Exception {
        return (new ObjectMapper()).writeValueAsString(srv.showCart());
    }
    @DeleteMapping(value = "/deletefromcart")
    public String delete(@RequestBody String receiveData) throws Exception {
        JSONObject json = new JSONObject(receiveData);
        Integer customerCode= json.optInt("customercode", 0);
        Integer productCode = json.optInt("productcode", 0);
        return srv.deleteFromCart(customerCode, productCode);
    }
    @DeleteMapping(value = "/cancelcart")
    public String cancel(@RequestBody String receivedData) throws Exception {
        JSONObject json = new JSONObject(receivedData);
        Integer code=json.optInt("code",0);
        return srv.cancelCart(code);
    }

    @PutMapping(value = "/addtocart")
    public String save(@Valid @RequestBody Cart obj) throws Exception {
        return srv.addToCart(obj);
    }

    @PostMapping(value = "/closecart")
    public String close(@RequestBody String receivedData) throws Exception {
        JSONObject json = new JSONObject(receivedData);
        Integer customerCode=json.optInt("code",0);
        return srv.closeCart(customerCode);
    }

    @PostMapping(value = "/showproduct")
    public String showProduct(@RequestBody(required = false) String receivedData) throws Exception {
        JSONObject json = new JSONObject(receivedData);
        Integer code=json.optInt("code",0);
        Integer page=json.optInt("page",0);
        String inputValue="{'code':"+code+",'page':"+page+"}";
        return (new ObjectMapper()).writeValueAsString(productRso.find(inputValue));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> generalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.getRootCause(ex).getMessage());
    }
}
