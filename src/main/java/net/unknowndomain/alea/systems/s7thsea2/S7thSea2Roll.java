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
import net.unknowndomain.alea.dice.standard.D10;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public class S7thSea2Roll implements GenericRoll
{
    
    private final DicePool<D10> dicePool;
    private final Set<S7thSea2Modifiers> mods;
    private final int skill;
    private final int addValue;
    
    public S7thSea2Roll(Integer dice, Integer add, S7thSea2Modifiers ... mod)
    {
        this(dice, add, Arrays.asList(mod));
    }
    
    public S7thSea2Roll(Integer trait, Integer skill, Integer bonus, Integer add, S7thSea2Modifiers ... mod)
    {
        this(trait, skill, bonus, add, Arrays.asList(mod));
    }
    
    public S7thSea2Roll(Integer dice, Integer add, Collection<S7thSea2Modifiers> mod)
    {
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        if (mods.contains(S7thSea2Modifiers.EXPLODING_DICE))
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice, 10);
        }
        else
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice);
        }
        skill = 0;
        this.addValue = (add == null) ? (0) : (add);
    }
    
    public S7thSea2Roll(Integer trait, Integer skill, Integer bonus, Integer add, Collection<S7thSea2Modifiers> mod)
    {
        this.mods = new HashSet<>();
        if (mod != null)
        {
            this.mods.addAll(mod);
        }
        if (skill >= 3)
        {
            mods.add(S7thSea2Modifiers.REROLL_LEFTOVER);
        }
        if (skill >= 4)
        {
            mods.add(S7thSea2Modifiers.DOUBLE_INCREMENT);
        }
        if (skill >= 5)
        {
            mods.add(S7thSea2Modifiers.EXPLODING_DICE);
        }
        Integer dice = trait + skill;
        if (bonus != null)
        {
            dice += bonus;
        }
        if (mods.contains(S7thSea2Modifiers.EXPLODING_DICE))
        {
            Set<Integer> exploding = new HashSet<>();
            exploding.add(10);
            if (mods.contains(S7thSea2Modifiers.JOIE_DE_VIVRE))
            {
                for (int i = 1; i <= skill; i++)
                {
                    exploding.add(i);
                }
            }
            this.dicePool = new DicePool<>(D10.INSTANCE, dice, exploding);
        }
        else
        {
            this.dicePool = new DicePool<>(D10.INSTANCE, dice);
        }
        this.skill = skill;
        this.addValue = (add == null) ? (0) : (add);
    }
    
    @Override
    public GenericResult getResult()
    {
        List<Integer> resultsPool = this.dicePool.getResults();
        List<Integer> res = new ArrayList<>();
        res.addAll(resultsPool);
        S7thSea2Results results = buildIncrements(res);
        S7thSea2Results results2 = null;
        if ((!results.getLeftovers().isEmpty()) && mods.contains(S7thSea2Modifiers.REROLL_LEFTOVER))
        {
            Integer ret = res.remove(res.size()-1);
            res = new ArrayList<>();
            DicePool<D10> reroll;
            if (mods.contains(S7thSea2Modifiers.EXPLODING_DICE))
            {
                reroll = new DicePool<>(D10.INSTANCE, 1, 10);
            }
            else
            {
                reroll = new DicePool<>(D10.INSTANCE, 1);
            }
            boolean done = false;
            List<Integer> newRes = reroll.getResults();
            for (Integer tmp : resultsPool)
            {
                if ((!done) && (Objects.equals(tmp, ret)))
                {
                    done = true;
                    res.addAll(newRes);
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
            results.setPrev(results2);
        }
        results.setVerbose(mods.contains(S7thSea2Modifiers.VERBOSE));
        return results;
    }
    
    private S7thSea2Results buildIncrements(List<Integer> res)
    {
        
        if (mods.contains(S7thSea2Modifiers.JOIE_DE_VIVRE))
        {
            List<Integer> tmp = new ArrayList<>(res.size());
            tmp.addAll(res);
            res.clear();
            for (Integer r : tmp)
            {
                if (r <= skill)
                {
                    res.add(10);
                }
                else
                {
                    res.add(r);
                }
            }
        }
        if (addValue > 0)
        {
            List<Integer> tmp = new ArrayList<>(res.size());
            tmp.addAll(res);
            res.clear();
            for (Integer r : tmp)
            {
                res.add(r + addValue);
            }
        }
        res.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        S7thSea2Results results = new S7thSea2Results(res);
        int diffMod = (mods.contains(S7thSea2Modifiers.INCREASED_DIFFICULTY)) ? 5 : 0;
        RecursiveCompound.calcIncrements(results, mods.contains(S7thSea2Modifiers.DOUBLE_INCREMENT), diffMod);
        return results;
    }
    
    
}
