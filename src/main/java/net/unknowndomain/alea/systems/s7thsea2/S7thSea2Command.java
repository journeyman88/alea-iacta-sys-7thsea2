/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.s7thsea2;

import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.command.HelpWrapper;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class S7thSea2Command extends RpgSystemCommand
{
    private static final Logger LOGGER = LoggerFactory.getLogger(S7thSea2Command.class);
    private static final RpgSystemDescriptor DESC = new RpgSystemDescriptor("7th Sea 2nd Edition", "7s2", "7th-sea-2nd");
    
    private static final String TRAIT_PARAM = "trait";
    private static final String SKILL_PARAM = "skill";
    private static final String BONUS_PARAM = "bonus";
    private static final String NUMBER_PARAM = "number";
    private static final String REROLL_PARAM = "reroll";
    private static final String DOUBLE_PARAM = "double";
    private static final String EXPLODE_PARAM = "explode";
    private static final String INCREASE_PARAM = "increase";
    private static final String JOIE_PARAM = "joie";
    
    private static final Options CMD_OPTIONS;
    
    static {
        CMD_OPTIONS = new Options();
        CMD_OPTIONS.addOption(
                Option.builder("t")
                        .longOpt(TRAIT_PARAM)
                        .desc("[Char mode] Trait level, requires --skill")
                        .hasArg()
                        .argName("traitValue")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("s")
                        .longOpt(SKILL_PARAM)
                        .desc("[Char mode] Skill level, requires --trait")
                        .hasArg()
                        .argName("skillRank")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("b")
                        .longOpt( BONUS_PARAM )
                        .desc( "[Char mode] Bonus/Malus dice")
                        .hasArg()
                        .argName("bonusDice")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("n")
                        .longOpt( NUMBER_PARAM )
                        .desc( "[Simple mode] Roll this number of dice")
                        .hasArg()
                        .argName("diceNumber")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("r")
                        .longOpt(REROLL_PARAM)
                        .desc("Force reroll of one dice enabled")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("d")
                        .longOpt(DOUBLE_PARAM)
                        .desc("Force 'Double Raise' mode enabled")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("x")
                        .longOpt(EXPLODE_PARAM)
                        .desc("Force dice exposion enabled")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("i")
                        .longOpt(INCREASE_PARAM)
                        .desc("Increase the 'cost' of a Raise by 5")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("j")
                        .longOpt(JOIE_PARAM)
                        .desc("[Char mode] Enable the 'Joie de vivre' advantage")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("h")
                        .longOpt( CMD_HELP )
                        .desc( "Print help")
                        .hasArg(false)
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("v")
                        .longOpt(CMD_VERBOSE)
                        .desc("Enable verbose output")
                        .build()
        );
    }
    
    public S7thSea2Command()
    {
        
    }
    
    @Override
    public RpgSystemDescriptor getCommandDesc()
    {
        return DESC;
    }

    @Override
    protected Logger getLogger()
    {
        return LOGGER;
    }
    
    @Override
    protected ReturnMsg safeCommand(String cmdName, String cmdParams)
    {
        ReturnMsg retVal;
        if (cmdParams == null || cmdParams.isEmpty())
        {
            return HelpWrapper.printHelp(cmdName, CMD_OPTIONS, true);
        }
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(CMD_OPTIONS, cmdParams.split(" "));
            if (
                    cmd.hasOption(CMD_HELP) || 
                    (cmd.hasOption(NUMBER_PARAM) && ( cmd.hasOption(TRAIT_PARAM) || cmd.hasOption(SKILL_PARAM) || cmd.hasOption(BONUS_PARAM) || cmd.hasOption(JOIE_PARAM))) || 
                    (cmd.hasOption(TRAIT_PARAM) ^ cmd.hasOption(SKILL_PARAM)) 
                )
            {
                return HelpWrapper.printHelp(cmdName, CMD_OPTIONS, true);
            }

            Set<S7thSea2Roll.Modifiers> mods = new HashSet<>();

            if (cmd.hasOption(REROLL_PARAM))
            {
                mods.add(S7thSea2Roll.Modifiers.REROLL_LEFTOVER);
            }
            if (cmd.hasOption(DOUBLE_PARAM))
            {
                mods.add(S7thSea2Roll.Modifiers.DOUBLE_INCREMENT);
            }
            if (cmd.hasOption(EXPLODE_PARAM))
            {
                mods.add(S7thSea2Roll.Modifiers.EXPLODING_DICE);
            }
            if (cmd.hasOption(INCREASE_PARAM))
            {
                mods.add(S7thSea2Roll.Modifiers.INCREASED_DIFFICULTY);
            }
            if (cmd.hasOption(JOIE_PARAM))
            {
                mods.add(S7thSea2Roll.Modifiers.JOIE_DE_VIVRE);
            }
            if (cmd.hasOption(CMD_VERBOSE))
            {
                mods.add(S7thSea2Roll.Modifiers.VERBOSE);
            }
            GenericRoll roll;
            if (cmd.hasOption(NUMBER_PARAM))
            {
                String d = cmd.getOptionValue(NUMBER_PARAM);
                roll = new S7thSea2Roll(Integer.parseInt(d), mods);
            }
            else
            {
                String t = cmd.getOptionValue(TRAIT_PARAM);
                String s = cmd.getOptionValue(SKILL_PARAM);
                String b = cmd.getOptionValue(BONUS_PARAM);
                int bonus = 0;
                if ((b != null) && (!b.isEmpty()))
                {
                    bonus = Integer.parseInt(b);
                }
                roll = new S7thSea2Roll(Integer.parseInt(t), Integer.parseInt(s), bonus, mods);
            }
            retVal = roll.getResult();
        } 
        catch (ParseException | NumberFormatException ex)
        {
            retVal = HelpWrapper.printHelp(cmdName, CMD_OPTIONS, true);
        }
        return retVal;
    }
    
}
