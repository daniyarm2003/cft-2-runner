package com.cft;

import com.cft.fighters.FighterFactory;
import com.cft.fighters.FighterFactoryImpl;
import com.cft.skillstates.FighterSkillStateManagerFactory;
import com.cft.skillstates.FighterSkillStateManagerFactoryImpl;
import com.cft.state.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        ObjectMapper jsonMapper = new ObjectMapper();

        File defaultFile = new File("cft2.dat");

        FighterSkillStateManagerFactory skillStateManagerFactory = new FighterSkillStateManagerFactoryImpl(5.0, 2.0);
        FighterFactory fighterFactory = new FighterFactoryImpl(random, skillStateManagerFactory);

        CFTSaveContextSerializer contextSerializer = new GzipJsonCFTSaveContextSerializer(jsonMapper);
        FileCFTStateSaver stateSaver = new FileCFTStateSaver(defaultFile, contextSerializer);

        CFTState cftState = new CFTState(fighterFactory, stateSaver);

        Options cliOptions = createCliOptions();
        HelpFormatter helpFormatter = HelpFormatter.builder().get();

        CommandLineParser cliParser = new DefaultParser();

        try {
            CommandLine commandLine = cliParser.parse(cliOptions, args);

            if(commandLine.hasOption('h')) {
                helpFormatter.printHelp("cft-runner", "Manages the state of a CFT 2 instance", cliOptions, "", true);
                System.exit(0);
            }

            if(commandLine.hasOption('f')) {
                String filePath = commandLine.getOptionValue('f');
                File saveFile = new File(filePath);

                stateSaver.setFile(saveFile);
            }

            System.out.println("Using save file: " + stateSaver.getFile().getAbsolutePath());

            if(!stateSaver.isSaved()) {
                System.out.println("No existing save data found");
            }

            cftState.loadState();

            if(commandLine.hasOption('r')) {
                System.out.println("Running CFT event...");

                cftState.runEvent();
                cftState.saveState();

                System.out.println("CFT event complete!");
                System.exit(0);
            }

            if(commandLine.hasOption('n')) {
                String fighterName = commandLine.getOptionValue('n');

                System.out.printf("Adding fighter %s...\n", fighterName);

                cftState.addFighter(fighterName);
                cftState.saveState();

                System.out.printf("Added fighter %s successfully!\n", fighterName);
                System.exit(0);
            }
        }
        catch(ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
        catch(IOException e) {
            System.err.println("An IO error has occurred: " + e.getLocalizedMessage());
            System.exit(1);
        }
    }

    private static Options createCliOptions() {
        Options cliOptions = new Options();

        Option runEventOption = Option.builder("r")
                .longOpt("run-event")
                .desc("Runs a CFT event and updates fighter statistics")
                .get();

        Option addFighterOption = Option.builder("n")
                .longOpt("new-fighter")
                .hasArg().argName("fighter_name")
                .desc("Adds a new fighter to the CFT with a specified name")
                .get();

        Option helpOption = Option.builder("h")
                .longOpt("help")
                .desc("Displays the help message")
                .get();

        Option saveFileOption = Option.builder("f")
                .longOpt("save-file")
                .hasArg().argName("save_file_location")
                .desc("Sets the location of the save file")
                .get();

        OptionGroup optionGroup = new OptionGroup();

        optionGroup.addOption(runEventOption);
        optionGroup.addOption(addFighterOption);
        optionGroup.addOption(helpOption);

        optionGroup.setRequired(true);

        cliOptions.addOptionGroup(optionGroup);
        cliOptions.addOption(saveFileOption);

        return cliOptions;
    }
}