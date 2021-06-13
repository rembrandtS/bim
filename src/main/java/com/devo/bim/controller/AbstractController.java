package com.devo.bim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class AbstractController {
    @Autowired
    private MessageSource messageSource;

    protected String msg(String prop){
        return messageSource.getMessage(prop,null, LocaleContextHolder.getLocale());
    }
}
