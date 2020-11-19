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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author journeyman
 */
public class S7thSea2Results
{
    private final List<Integer> results;
    private int increments = 0;
    private List<Integer> leftovers = new ArrayList<>();
    private List<String> usedDice = new ArrayList<>();
    private Integer oldValue;
    private Integer newValue;
    
    public S7thSea2Results(List<Integer> results)
    {
        List<Integer> tmp = new ArrayList<>(results.size());
        tmp.addAll(results);
        this.results = Collections.unmodifiableList(tmp);
    }
    
    private void addIncrements(int value, Integer ... dice)
    {
        this.addIncrements(value, Arrays.asList(dice));
    }
    
    public void addIncrement(Integer ... dice)
    {
        addIncrements(1, dice);
    }
    
    public void addDoubleIncrement(Integer ... dice)
    {
        addIncrements(2, dice);
    }
    private void addIncrements(int value, Collection<Integer> dice)
    {
        increments += value;
        StringBuilder sb = new StringBuilder("(");
        for (Integer d : dice)
        {
            if (sb.length() >= 2)
            {
                sb.append("+");
            }
            sb.append(d);
        }
        sb.append(")");
        usedDice.add(sb.toString());
    }
    
    public void addIncrement(Collection<Integer> dice)
    {
        addIncrements(1, dice);
    }
    
    public void addDoubleIncrement(Collection<Integer> dice)
    {
        addIncrements(2, dice);
    }

    public int getIncrements()
    {
        return increments;
    }

    public List<String> getUsedDice()
    {
        return usedDice;
    }

    public List<Integer> getLeftovers()
    {
        return leftovers;
    }

    public List<Integer> getResults()
    {
        return results;
    }

    public Integer getNewValue()
    {
        return newValue;
    }

    public void setNewValue(Integer newValue)
    {
        this.newValue = newValue;
    }

    public Integer getOldValue()
    {
        return oldValue;
    }

    public void setOldValue(Integer oldValue)
    {
        this.oldValue = oldValue;
    }

}
