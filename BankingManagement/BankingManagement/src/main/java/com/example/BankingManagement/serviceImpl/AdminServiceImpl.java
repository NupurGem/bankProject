package com.example.BankingManagement.serviceImpl;

import com.example.BankingManagement.exception.ResourceNotFoundException;
import com.example.BankingManagement.model.AdminDetails;
import com.example.BankingManagement.repository.AdminRepository;
import com.example.BankingManagement.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {

        this.adminRepository = adminRepository;
    }


    @Override
    public AdminDetails create(AdminDetails adminDetails) {

        return adminRepository.save(adminDetails);
    }

    @Override
    public Double getBalance(long accountNo) {
        Double account = adminRepository.getAccountByNumber(accountNo);
        if (account != null) {
            return account;
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }

    @Override
    public void depositAmount(long id, double transactionAmount) {
        adminRepository.saveTransactionAmountByID(id, transactionAmount);
    }

    @Override
    public void updateBalance(long Account_id, double newBalance) {
        Optional<AdminDetails> optionalAdmin = adminRepository.findById(Account_id);
        if (optionalAdmin.isPresent()) {
            AdminDetails admin = optionalAdmin.get();
            admin.setBalance(newBalance);
            adminRepository.save(admin);
        } else {
            throw new IllegalArgumentException("Account not found.");
        }
    }


    @Override
    public void withdrawAmount(long id, double transactionAmount) {
        Optional<AdminDetails> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            AdminDetails admin = optionalAdmin.get();
            double currentBalance = admin.getBalance();
            if (currentBalance >= transactionAmount) {
                double newBalance = currentBalance - transactionAmount;
                updateBalance(id, newBalance);
            } else {
                throw new IllegalArgumentException("Insufficient funds.");
            }
        } else {
            throw new IllegalArgumentException("Account not found.");
        }

    }

    @Override
    public double transferAmount(long sourceId, long targetId, double transactionAmount) {
        Optional<AdminDetails> optionalSender = adminRepository.findById(sourceId);
        Optional<AdminDetails> optionalRecipient = adminRepository.findById(targetId);

        if (optionalSender.isPresent() && optionalRecipient.isPresent()) {
            AdminDetails sender = optionalSender.get();
            AdminDetails recipient = optionalRecipient.get();

            double senderBalance = sender.getBalance();
            if (senderBalance >= transactionAmount) {
                double recipientBalance = recipient.getBalance();

                double senderNewBalance = senderBalance - transactionAmount;
                double recipientNewBalance = recipientBalance + transactionAmount;
                sender.setBalance(senderNewBalance);
                recipient.setBalance(recipientNewBalance);

                adminRepository.save(sender);
                adminRepository.save(recipient);

                System.out.println("Funds transferred successfully!");
                System.out.println("Balance: " + senderNewBalance);
                return senderNewBalance; // Return the sender's new balance
            } else {
                throw new IllegalArgumentException("Insufficient funds.");
            }
        } else {
            throw new IllegalArgumentException("One or both accounts not found.");
        }
    }


    @Override
    public void deleteAccount(long id) {

        adminRepository.deleteById(id);
    }

    @Override
    public AdminDetails getCustomerDetails(long id) {
//        Optional<AdminDetails> adminDetailsOptional= adminRepository.findById(id);
//        if(adminDetailsOptional.isPresent())
//            return adminDetailsOptional.get();
//        else {
//            throw new IllegalArgumentException("Account not found.");
//
//        }
//    }
        Optional<AdminDetails> adminDetailsOptional = adminRepository.findById(id);
        return adminDetailsOptional.orElseThrow(() ->
                new ResourceNotFoundException("AdminDetails", "id", id));
    }
}

