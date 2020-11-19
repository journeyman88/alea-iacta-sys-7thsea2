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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.unknowndomain.alea.dice.D10;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericRoll;
import org.javacord.api.entity.message.MessageBuilder;

/**
 *
 * @author journeyman
 */
public class S7thSea2Roll implements GenericRoll
{
    
    public enum Modifiers
    {
        VERBOSE,
        REROLL_LEFTOVER,
        DOUBLE_INCREMENT,
        EXPLODING_DICE
    }
    
    private final DicePool<D10> dicePool;
    private final Set<Modifiers> mods;
    
    public S7thSea2Roll(Integer dice, Modifiers ... mod)
    {
        this(dice, Arrays.asList(mod));
    }
    
    public S7thSea2Roll(Integer trait, Integer skill, Integer bonus, Modifiers ... mod)
    {
        this(trait, skill, bonus, Arrays.asList(mod));
    }
    
    public S7thSea2Roll(Integer dice, Collection<Modifiers> mod)
    {
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        if (mods.contains(Modifiers.EXPLODING_DICE))
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice, 10);
        }
        else
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice);
        }
    }
    
    public S7thSea2Roll(Integer trait, Integer skill, Integer bonus, Collection<Modifiers> mod)
    {
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        if (skill >= 3)
        {
            mods.add(Modifiers.REROLL_LEFTOVER);
        }
        if (skill >= 4)
        {
            mods.add(Modifiers.DOUBLE_INCREMENT);
        }
        if (skill >= 5)
        {
            mods.add(Modifiers.EXPLODING_DICE);
        }
        Integer dice = trait + skill + bonus;
        if (mods.contains(Modifiers.EXPLODING_DICE))
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice, 10);
        }
        else
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice);
        }
    }
    
    @Override
    public MessageBuilder getResult()
    {
        List<Integer> resultsPool = this.dicePool.getResults();
        List<Integer> res = new ArrayList<>();
        res.addAll(resultsPool);
        S7thSea2Results results = buildIncrements(res);
        S7thSea2Results results2 = null;
        if (!res.isEmpty() && mods.contains(Modifiers.REROLL_LEFTOVER))
        {
            Integer ret = res.remove(res.size()-1);
            res = new ArrayList<>();
            DicePool<D10> reroll;
            if (mods.contains(Modifiers.EXPLODING_DICE))
            {
                reroll = new DicePool<>(D10.INSTANCE, 1, 10);
            }
            else
            {
                reroll = new DicePool<>(D10.INSTANCE, 1);
            }
            boolean done = false;
            Integer newRes = reroll.getResults().get(0);
            for (Integer tmp : resultsPool)
            {
                if ((!done) && (Objects.equals(tmp, ret)))
                {
                    done = true;
                    res.add(newRes);
                }
                else
                {
                    res.add(tmp);
                }
            }
            results2 = results;
            results = buildIncrements(res);
            results.setNewValue(newRes);
            results.setOldValue(ret);
            results.getLeftovers().addAll(res);
        }
        return formatResults(results, results2);
    }
    
    private MessageBuilder formatResults(S7thSea2Results results, S7thSea2Results results2)
    {
        MessageBuilder mb = new MessageBuilder();
        mb.append("Increments: ").append(results.getIncrements()).append(" ");
        mb.append(results.getUsedDice()).appendNewLine();
        mb.append("Leftovers: ").append(results.getLeftovers().size()).append(" [ ");
        for (Integer t : results.getLeftovers())
        {
            mb.append(t).append(" ");
        }
        mb.append("]\n");
        if (mods.contains(Modifiers.VERBOSE))
        {
            mb.append("Results: ").append(" [ ");
            for (Integer t : results.getResults())
            {
                mb.append(t).append(" ");
            }
            mb.append("]\n");
        }
        if (results2 != null)
        {
            
            mb.append("Reroll: true (").append(results.getOldValue()).append(" => ").append(results.getNewValue()).append(")").appendNewLine();
            if (mods.contains(Modifiers.VERBOSE))
            {
                mb.append("Prev : {\n");
                mb.append("    Increments: ").append(results2.getIncrements()).append(" ");
                mb.append(results2.getUsedDice()).appendNewLine();
                mb.append("    Leftovers: ").append(results2.getLeftovers().size()).append(" [ ");
                for (Integer t : results2.getLeftovers())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]\n");
                mb.append("    Results: ").append(" [ ");
                for (Integer t : results2.getResults())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]\n");
                mb.append("}\n");
            }
        }
        return mb;
    }
    
    private S7thSea2Results buildIncrements(List<Integer> res)
    {
        res.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        S7thSea2Results results = new S7thSea2Results(res);
        RecursiveCompound.calcIncrements(results, mods.contains(Modifiers.DOUBLE_INCREMENT), 0);
        return results;
    }
    
    
}
