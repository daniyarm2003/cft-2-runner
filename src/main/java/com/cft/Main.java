package com.cft;

import com.cft.fighters.Fighter;
import com.cft.fighters.FighterFactory;
import com.cft.fighters.FighterFactoryImpl;
import com.cft.fighters.FighterHealthClass;
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

        CFTSaveContextSerializer contextSerializer = new JsonCFTSaveContextSerializer(jsonMapper, false);
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
                handleNewFighterCommand(commandLine, cftState, random);
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

    private static void handleNewFighterCommand(CommandLine commandLine, CFTState cftState, Random random) throws IOException {
        String fighterName = commandLine.getOptionValue('n');
        String healthClassName = commandLine.getOptionValue("heart-class");

        FighterHealthClass healthClass;

        if(healthClassName == null) {
            int healthClassIndex = random.nextInt(FighterHealthClass.values().length);
            healthClass = FighterHealthClass.values()[healthClassIndex];
        }
        else {
            healthClass = FighterHealthClass.findHealthClassByName(healthClassName);

            if(healthClass == null) {
                System.err.printf("Error: unknown fighter health class '%s'\n", healthClassName);
                System.exit(1);
            }
        }

        System.out.printf("Adding fighter %s with health class %s (%s)...\n", fighterName, healthClass.getHeartClassName(), healthClass.getShortName());

        Fighter fighter = cftState.addFighter(fighterName, healthClass);
        cftState.saveState();

        System.out.printf("Added fighter %s (ID: %d) successfully!\n", fighterName, fighter.getId());
        System.exit(0);
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

        Option healthClassOption = Option.builder()
                .longOpt("heart-class")
                .hasArg().argName("class_name")
                .desc("Sets the heart class of the fighter")
                .get();

        OptionGroup optionGroup = new OptionGroup();

        optionGroup.addOption(runEventOption);
        optionGroup.addOption(addFighterOption);
        optionGroup.addOption(helpOption);

        optionGroup.setRequired(true);

        cliOptions.addOptionGroup(optionGroup);

        cliOptions.addOption(saveFileOption);
        cliOptions.addOption(healthClassOption);

        return cliOptions;
    }
}