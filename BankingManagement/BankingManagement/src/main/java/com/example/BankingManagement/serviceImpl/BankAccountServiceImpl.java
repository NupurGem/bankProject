package com.example.BankingManagement.serviceImpl;

import com.example.BankingManagement.dto.BankAccountDto;
import com.example.BankingManagement.exception.ResourceNotFoundException;
import com.example.BankingManagement.model.BankAccount;
import com.example.BankingManagement.repository.BankAccountRepository;
import com.example.BankingManagement.service.BankAccountService;
import org.springframework.stereotype.Service;

@Service
public class BankAccountServiceImpl implements BankAccountService {


    private BankAccountRepository bankAccountRepository;


    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {

        return bankAccountRepository.save(bankAccount);
    }


    @Override
    public BankAccountDto getCustomer(long id) throws ResourceNotFoundException
    {
      //  return bankAccountRepository.findProjectedById(id);
        BankAccountDto bankAccountDto = bankAccountRepository.findProjectedById(id);
        if (bankAccountDto == null) {
            throw new ResourceNotFoundException("Bank Account", "id", id);
        }
        return bankAccountDto;
    }

    @Override
    public void deleteCustomer(long id) {
        BankAccount bankAccount= bankAccountRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Bank Account", "id", id));
                 bankAccountRepository.delete(bankAccount);
    }

    @Override
    public BankAccount updateUser(BankAccount user) throws ResourceNotFoundException
    {
        return bankAccountRepository.save(user);
    }

}
