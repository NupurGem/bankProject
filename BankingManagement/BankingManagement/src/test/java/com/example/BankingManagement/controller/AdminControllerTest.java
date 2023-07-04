package com.example.BankingManagement.controller;

import com.example.BankingManagement.exception.ResourceNotFoundException;
import com.example.BankingManagement.model.AdminDetails;
import com.example.BankingManagement.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AdminControllerTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminController adminController;


    @BeforeEach
    void setup() {
        // Clear the repository before each test
        adminRepository.deleteAll();
    }


    @Test
    void getCustomerDetails() {
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setId(1L);
        adminDetails.setAccountNo(200101);
        adminDetails.setBalance(1000);

        // Save the AdminDetails object
        adminRepository.save(adminDetails);

        // Call the controller method
        AdminDetails result = adminController.getCustomerDetails(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(adminDetails.getId(), result.getId());
        assertEquals(adminDetails.getAccountNo(), result.getAccountNo());
        assertEquals(adminDetails.getBankAccount(), result.getBankAccount());

    }

    @Test
    void getCustomerDetails_NotFound(){
        long nonExistentCustomerId = 1L;

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> adminController.getCustomerDetails(nonExistentCustomerId));

        // Assert the exception message or any other necessary assertions
        assertEquals("AdminDetails not found with id : '1' ",exception.getMessage());
    }


    @Test
    void deleteAccount() {
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setId(1);
        adminDetails.setAccountNo(123456789);
        adminDetails.setBalance(1000);

        // Save the AdminDetails object
        adminRepository.save(adminDetails);

        // Call the deleteAccount method in the controller
        adminController.deleteAccount(adminDetails.getId());

        // Verify that the account has been deleted
        assertFalse(adminRepository.existsById(adminDetails.getId()));
    }


    @Test
    void depositAmount() {
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setId(1);
        adminDetails.setAccountNo(123456789);
        adminDetails.setBalance(1000);

        // Save the AdminDetails object
        adminRepository.save(adminDetails);

        // Call the depositAmount method in the controller
        adminController.depositAmount(adminDetails.getId(), 500);

        // Fetch the AdminDetails object from the repository
        AdminDetails updatedAdminDetails = adminRepository.findById(adminDetails.getId()).orElse(null);

        // Assert the updated balance
        assertNotNull(updatedAdminDetails);
        assertEquals(1500, updatedAdminDetails.getBalance());
    }

    @Test
    void withdrawAmount() {
        // Create a sample AdminDetails object
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setId(1);
        adminDetails.setAccountNo(123456789);
        adminDetails.setBalance(1000);

        // Save the AdminDetails object
        adminRepository.save(adminDetails);

        // Call the withdrawAmount method in the controller
        adminController.withdrawAmount(adminDetails.getId(), 500);

        // Fetch the AdminDetails object from the repository
        AdminDetails updatedAdminDetails = adminRepository.findById(adminDetails.getId()).orElse(null);
        assertNotNull(updatedAdminDetails);
        assertEquals(500, updatedAdminDetails.getBalance());

    }

    @Test
    void transferAmount() {
        AdminDetails sourceAccount = new AdminDetails();
        sourceAccount.setId(1);
        sourceAccount.setAccountNo(123456789);
        sourceAccount.setBalance(1000);

        // Create a sample AdminDetails object for target account
        AdminDetails targetAccount = new AdminDetails();
        targetAccount.setId(2);
        targetAccount.setAccountNo(987654321);
        targetAccount.setBalance(500);
        adminRepository.save(sourceAccount);
        adminRepository.save(targetAccount);

        // Call the transferAmount method in the controller
        adminController.transferAmount(sourceAccount.getId(), targetAccount.getId(), 500);

        // Fetch the updated AdminDetails objects from the repository
        AdminDetails updatedSourceAccount = adminRepository.findById(sourceAccount.getId()).orElse(null);
        AdminDetails updatedTargetAccount = adminRepository.findById(targetAccount.getId()).orElse(null);

        // Assert the updated balances
        assertNotNull(updatedSourceAccount);
        assertNotNull(updatedTargetAccount);
        assertEquals(500, updatedSourceAccount.getBalance());
        assertEquals(1000, updatedTargetAccount.getBalance());

    }

    @Test
    void getBalance() {
        AdminDetails adminDetails = new AdminDetails();
        adminDetails.setId(1);
        adminDetails.setAccountNo(200101);
        adminDetails.setBalance(1000);

        // Save the AdminDetails object
        adminRepository.save(adminDetails);
        double balance = adminController.getBalance(adminDetails.getAccountNo());

        // Assert the balance
        assertEquals(1000, balance);
    }
}