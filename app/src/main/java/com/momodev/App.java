package com.momodev;

import com.momodev.constants.Command;
import com.momodev.service.CommandDispatcher;

public class App {
    public static void main(String[] args) throws IllegalAccessException {
        if (args.length == 0) {
            Command command = new Command();
            command.printCommandList();
            return;
        }
        CommandDispatcher dispatcher = new CommandDispatcher();
        dispatcher.dispatch(args);
    }
}
