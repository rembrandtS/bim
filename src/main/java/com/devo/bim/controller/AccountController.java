package com.devo.bim.controller;

import com.devo.bim.model.Account;
import com.devo.bim.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("locale", LocaleContextHolder.getLocale());
        return "account/login";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(String email) {
        Account account = accountRepository
                .findByEmail(email)
                .map(savedAccount->{
                    savedAccount.setPassword(passwordEncoder.encode("1111"));
            return accountRepository.save(savedAccount);
        }).orElseGet(Account::new);

        return "account/login";
    }
}
