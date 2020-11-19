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
import java.util.List;

/**
 *
 * @author journeyman
 */
public class IterativeCompound
{
    
    public static void calcIncrements(S7thSea2Results results, boolean doubleIncrements, int diffMod)
    {
        int round = 0;
        List<Integer> res = new ArrayList<>();
        res.addAll(results.getResults());
        while(round < res.size())
        {
            round += 1;
            combo(results, res, diffMod, round, doubleIncrements);
        }
        results.getLeftovers().addAll(res);
    }
        
    private static void combo(S7thSea2Results results, List<Integer> numbers, int diffMod, int round, boolean doubleIncrements)
    {
        int checkLevel;
        List<Integer> toAdd;
        if (doubleIncrements)
        {
            List<Integer> doublePool = new ArrayList<>();
            doublePool.addAll(numbers);
            checkLevel = 15 + diffMod;
            while(!doublePool.isEmpty())
            {
                toAdd = new ArrayList<>();
                Integer totSum = 0;
                if (doublePool.size() >= round)
                {
                    for(int g = 0; g < round; g++)
                    {
                        Integer tmpVal = doublePool.remove(0);
                        totSum += tmpVal;
                        toAdd.add(tmpVal);
                    }
                }
                else
                {
                    break;
                }
                if (totSum > 0)
                {
                    for (int ti = doublePool.size() -1; ti >= 0; ti--)
                    {
                        if ((totSum + doublePool.get(ti)) >= checkLevel)
                        {
                            for(int g = 0; g < round; g++)
                            {
                                numbers.remove(0);
                            }
                            numbers.remove(ti);
                            toAdd.add(doublePool.remove(ti));
                            results.addDoubleIncrement(toAdd);
                            break;
                        }
                    }
                }
            }
        }
        checkLevel = 10 + diffMod;
        List<Integer> singlePool = new ArrayList<>();
        singlePool.addAll(numbers);
        while(!singlePool.isEmpty())
        {
            if (round == 1)
            {
                Integer ret = singlePool.get(0);
                if (ret >= checkLevel)
                {
                    singlePool.remove(0);
                    numbers.remove(0);
                    results.addIncrement(ret);
                    continue;
                }
            }
            toAdd = new ArrayList<>();
            Integer totSum = 0;
            if (singlePool.size() >= round)
            {
                for(int g = 0; g < round; g++)
                {
                    Integer tmpVal = singlePool.remove(0);
                    totSum += tmpVal;
                    toAdd.add(tmpVal);
                }
                for (int ti = singlePool.size() -1; ti >= 0; ti--)
                {
                    if ((totSum + singlePool.get(ti)) >= checkLevel)
                    {
                        for(int g = 0; g < round; g++)
                        {
                            numbers.remove(0);
                        }
                        numbers.remove(ti);
                        toAdd.add(singlePool.remove(ti));
                        results.addIncrement(toAdd);
                        break;
                    }
                }
            }
            else
            {
                break;
            }
        }
    }
}
