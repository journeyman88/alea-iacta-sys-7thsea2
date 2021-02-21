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

import java.util.Locale;
import java.util.Optional;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import net.unknowndomain.alea.systems.RpgSystemOptions;
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
    protected Optional<GenericRoll> safeCommand(RpgSystemOptions options, Locale lang)
    {
        
        Optional<GenericRoll> retVal;
        if (options.isHelp() || !(options instanceof S7thSea2Options) )
        {
            retVal = Optional.empty();
        }
        else
        {
            S7thSea2Options opt = (S7thSea2Options) options;
            S7thSea2Roll roll;
            if (opt.isCharacterMode())
            {
                roll = new S7thSea2Roll(opt.getTrait(), opt.getSkill(), opt.getBonus(), opt.getModifiers());
            }
            else
            {
                roll = new S7thSea2Roll(opt.getNumber(), opt.getModifiers());
            }
            retVal = Optional.of(roll);
        }
        return retVal;
    }

    @Override
    public RpgSystemOptions buildOptions()
    {
        return new S7thSea2Options();
    }
    
}
