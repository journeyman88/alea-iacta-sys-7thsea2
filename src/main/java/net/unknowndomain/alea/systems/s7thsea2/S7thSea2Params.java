/*
 * Copyright 2021 Marco Bignami.
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

import net.unknowndomain.alea.systems.RpgSystemParams;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

/**
 *
 * @author journeyman
 */
@Command(
        description = "7th Sea 2nd Edition",
        resourceBundle = "net.unknowndomain.alea.systems.s7thsea2.RpgSystemBundle",
        sortOptions = false
)
public class S7thSea2Params extends RpgSystemParams {
    
    @ArgGroup(exclusive = true, multiplicity = "1", order = 1)
    Mode selectedMode;
    @ArgGroup(validate = false, headingKey = "7thsea.sections.rollModifiers", order = 2)
    Modifiers modifiers;
    
    static class Mode {
        @ArgGroup(exclusive = false, multiplicity = "1", headingKey = "7thsea.sections.characterMode", order = 1)
        CharacterMode charData;
        @ArgGroup(exclusive = false, multiplicity = "1", headingKey = "7thsea.sections.simpleMode", order = 2)
        SimpleMode simpleMode;
    }
    
    static class CharacterMode {
        @Option(names = { "-t", "--trait"}, descriptionKey = "7thsea.options.trait", paramLabel = "traitValue", required = true)
        private Integer trait;
        @Option(names = { "-s", "--skill"}, descriptionKey = "7thsea.options.skill", paramLabel = "skillRank", required = true)
        private Integer skill;
        @Option(names = { "-b", "--bonus"}, descriptionKey = "7thsea.options.bonus", paramLabel = "bonusDice")
        private Integer bonus;   
        @Option(names = { "-j", "--joie"}, descriptionKey = "7thsea.options.joie")
        private boolean joie; 
    }
    
    static class SimpleMode {
        @Option(names = { "-n", "--number"}, descriptionKey = "7thsea.options.number", paramLabel = "diceNumber", required = true)
        private Integer number; 
    } 
    
    static class Modifiers {
        @Option(names = { "-r", "--reroll"}, descriptionKey = "7thsea.options.reroll")
        private boolean reroll;  
        @Option(names = { "-d", "--double"}, descriptionKey = "7thsea.options.double")
        private boolean doubleRaise;  
        @Option(names = { "-x", "--explode"}, descriptionKey = "7thsea.options.explode")
        private boolean explode;  
        @Option(names = { "-i", "--increase"}, descriptionKey = "7thsea.options.increase")
        private boolean increase; 
    }
    
    
}
