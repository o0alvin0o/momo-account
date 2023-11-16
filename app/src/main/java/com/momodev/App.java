package com.momodev;

import com.momodev.constants.Command;
import com.momodev.service.AccountService;
import com.momodev.service.CommandDispatcher;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IllegalAccessException, IOException {
        if (args.length == 0) {
            Command command = new Command();
            command.printCommandList();
            return;
        }
        CommandDispatcher dispatcher = new CommandDispatcher();
        dispatcher.setAccountService(AccountService.getDefault());
        dispatcher.dispatch(args);
    }
}
