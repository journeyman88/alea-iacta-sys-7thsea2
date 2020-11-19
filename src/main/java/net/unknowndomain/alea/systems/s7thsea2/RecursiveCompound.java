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
public class RecursiveCompound
{
    private static void sumUpRecursive(List<D10Values> numbers, int target, List<D10Values> partial, List<Group> groups) {
        int s = 0;
        for (D10Values x: partial)
        {
            s += x.getValue();
        }
        if (s >= target)
        {
            groups.add(new Group(partial));
            return;
        }
        for(int i=0;i<numbers.size();i++)
        {
            List<D10Values> remaining = new ArrayList<>();
            D10Values n = numbers.get(i);
            for (int j=i+1; j<numbers.size();j++) remaining.add(numbers.get(j));
            List<D10Values> partial_rec = new ArrayList<>(partial);
            partial_rec.add(n);
            sumUpRecursive(remaining,target,partial_rec, groups);
        }
    }
    
    private static List<Group> sumUp(List<D10Values> list, int target) 
    {
        List<Group> retVal = new ArrayList<>();
        while(true)
        {
            List<Group> gList = new ArrayList<>();
            sumUpRecursive(list,target,new ArrayList<>(),gList);
            if (gList.isEmpty())
            {
                break;
            }
            Group chosen = null;
            for (Group g : gList)
            {
                if (
                        (chosen == null) || 
                        (g.getTotal() < chosen.getTotal()) || 
                        ((g.getTotal() == chosen.getTotal()) && (g.getSize() < chosen.getSize()))
                    )
                {
                    chosen = g;
                }
            }
            for (D10Values v : chosen.getValues())
            {
                list.remove(v);
            }
            retVal.add(chosen);
        }
        return retVal;
    }
    
    public static void calcIncrements(S7thSea2Results results, boolean doubleIncrements, int diffMod)
    {
        List<D10Values> list = new ArrayList<>();
        for (Integer i : results.getResults())
        {
            list.add(D10Values.find(i));
        }
        List<Group> tmp;
        if (doubleIncrements)
        {
            tmp = sumUp(list, 15 + diffMod);
            for (Group g : tmp)
            {
                results.addDoubleIncrement(g.getDices());
            }
        }
        tmp = sumUp(list, 10 + diffMod);
        for (Group g : tmp)
        {
            results.addIncrement(g.getDices());
        }
        for (D10Values l : list)
        {
            results.getLeftovers().add(l.getValue());
        }
    }
    
}
