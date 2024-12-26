package com.example.service;

import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.dto.AccInfoDto;
import com.example.entity.Account;
import com.example.entity.AuthDto;
import com.example.entity.Follow;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.AccountInfoException;
import com.example.exception.InvalidUsernamePasswordException;
import com.example.repository.AccountRepository;
import com.example.repository.FollowRepository;
import com.example.util.AccountIdStruct;

import org.springframework.security.crypto.bcrypt.BCrypt;

import io.jsonwebtoken.JwtException;

@Service
@Transactional
public class AccountService {

    AccountRepository accountRepository;
    FollowRepository followRepository;
    JwtService jwtService;

    @Autowired
    public AccountService(AccountRepository accountRepository, JwtService jwtService, FollowRepository followRepository) {
        this.accountRepository = accountRepository;
        this.jwtService = jwtService;
        this.followRepository = followRepository;
    }

    public Account createAccount(Account account) throws AccountAlreadyExistsException, InvalidUsernamePasswordException{
        Optional<Account> existingAcc = accountRepository.findByUsername(account.getUsername());
        if(existingAcc.isPresent()) {
            throw new AccountAlreadyExistsException();
        } else if (account.getUsername().length() < 1 || account.getPassword().length() < 4){
            throw new InvalidUsernamePasswordException();
        }
        String pw_hash = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(pw_hash);
        return accountRepository.save(account);
    }

    public AuthDto login(Account account) throws AccountDoesNotExistException, InvalidUsernamePasswordException{
        Optional<Account> matchedAccOpt = accountRepository.findByUsername(account.getUsername());
        if(!matchedAccOpt.isPresent()){
            throw new AccountDoesNotExistException();
        }
        Account matchedAcc = matchedAccOpt.get();
        System.err.println("heres the new account passes: " + matchedAcc.getPassword() + " " + account.getPassword());
        if(BCrypt.checkpw(account.getPassword(), matchedAcc.getPassword())) {
            String token = jwtService.generateAccountToken(matchedAcc);
            AuthDto authDto = new AuthDto(true, token);
            return authDto;
        }
        throw new InvalidUsernamePasswordException();
    }

    public AccInfoDto editAccountInfo(Integer accountId, Account account) throws AccountDoesNotExistException{

        Optional<Account> accCurrentOpt = accountRepository.findByAccountId(accountId);
        Account accCurrent = accCurrentOpt.get();
        if(accCurrent != null){
            if(account.getUsername() != null) {
                if(account.getUsername().compareTo(accCurrent.getUsername()) != 0){
                    accCurrent.setUsername(account.getUsername());
                }
            }
            if(account.getPassword() != null){
                if(account.getPassword().compareTo(accCurrent.getPassword()) != 0){
                    String pw_hash = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
                    accCurrent.setPassword(pw_hash);
                }
            }
            accountRepository.save(accCurrent);
            return new AccInfoDto(true, accCurrent);
        }
        throw new AccountDoesNotExistException();

    }

    public Account getAccountInfo(String token) throws JwtException, AccountInfoException{
        Integer accountId = jwtService.returnAccountIdFromToken(token);
        Account account = accountRepository.findByAccountId(accountId).get();
        System.out.println(account);
        return account;
    }

    public AuthDto validateAndRefresh(String token) throws JwtException{
        AccountIdStruct ais = jwtService.validateAndRefresh(token);
        Account acc = accountRepository.findByAccountId(ais.getAccountId()).get();
        String newToken = jwtService.generateAccountToken(acc);
        return new AuthDto(true, newToken);
    }

    public AccInfoDto getAccountByAccountId(Integer accountId) throws AccountDoesNotExistException{
        Optional<Account> accOpt = accountRepository.findByAccountId(accountId);
        if(accOpt.isPresent()){
            return new AccInfoDto(true, accOpt.get());
        }
        throw new AccountDoesNotExistException();
    }

    public void addFollow(Follow follow){
        followRepository.save(follow);
    }

  
    public void removeFollow(Follow follow){
        Follow removedFollow = followRepository.findByFollowingAccountIdAndFollowedAccountId(follow.getFollowingAccountId(), follow.getFollowedAccountId());
        followRepository.delete(removedFollow);
    }
}
