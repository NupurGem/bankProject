package com.example.BankingManagement.controller;

import com.example.BankingManagement.dto.BankAccountDto;
import com.example.BankingManagement.model.BankAccount;
import com.example.BankingManagement.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class BankAccountController {


    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService)
    {

        this.bankAccountService= bankAccountService;
    }


    @DeleteMapping("/customer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable long id) {

        bankAccountService.deleteCustomer(id);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<BankAccountDto> getCustomer(@PathVariable long id){

        //return bankAccountService.getCustomer(id);
        BankAccountDto bankAccountDto = bankAccountService.getCustomer(id);

        if (bankAccountDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(bankAccountDto);
    }

    @PostMapping("/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount createCustomer(@RequestBody BankAccount bankAccount){
       return  bankAccountService.create(bankAccount);
    }




}

