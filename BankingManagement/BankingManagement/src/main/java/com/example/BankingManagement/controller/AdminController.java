package com.example.BankingManagement.controller;

import com.example.BankingManagement.exception.ResourceNotFoundException;
import com.example.BankingManagement.model.AdminDetails;
import com.example.BankingManagement.service.AdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "Admin")
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

    @GetMapping("/transaction/{id}")
    public AdminDetails getCustomerDetails(@PathVariable long id){

        Optional<AdminDetails> adminDetails = Optional.ofNullable(adminService.getCustomerDetails(id));
        if (adminDetails.isPresent()) {
            return adminDetails.get();
        } else {
            throw new ResourceNotFoundException("AdminDetails", "id", id);
        }
    }

    @DeleteMapping("/transaction/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable long id) {

        adminService.deleteAccount(id);
    }

    @PutMapping("/transaction/{id}/deposit/{transactionAmount}")
    public void depositAmount(@PathVariable long id , @PathVariable long transactionAmount)
    {
        adminService.depositAmount(id, transactionAmount);
    }


    @PutMapping("/transaction/{id}/withdraw/{transactionAmount}")
    public void withdrawAmount(@PathVariable long id, @PathVariable double transactionAmount)
    {

        adminService.withdrawAmount(id, transactionAmount);
    }

    @PutMapping("/transaction/{sourceId}/transfer/{targetId}/{transactionAmount}")
    public ResponseEntity<Double> transferAmount(@PathVariable long sourceId,@PathVariable long targetId, @PathVariable long transactionAmount)
    {
        try {
            double senderBalance = adminService.transferAmount(sourceId, targetId, transactionAmount);
            return ResponseEntity.ok(senderBalance);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);

            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/admin/{accountNo}/balance")
    public double getBalance(@PathVariable long accountNo) {

        return adminService.getBalance(accountNo);
    }



}

