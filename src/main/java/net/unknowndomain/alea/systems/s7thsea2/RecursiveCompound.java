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
import net.unknowndomain.alea.random.SingleResult;

/**
 *
 * @author journeyman
 */
public class RecursiveCompound
{
    private static void sumUpRecursive(List<SingleResult<Integer>> numbers, int target, List<SingleResult<Integer>> partial, List<Group> groups) {
        int s = 0;
        for (SingleResult<Integer> x: partial)
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
            List<SingleResult<Integer>> remaining = new ArrayList<>();
            SingleResult<Integer> n = numbers.get(i);
            for (int j=i+1; j<numbers.size();j++) remaining.add(numbers.get(j));
            List<SingleResult<Integer>> partial_rec = new ArrayList<>(partial);
            partial_rec.add(n);
            sumUpRecursive(remaining,target,partial_rec, groups);
        }
    }
    
    private static List<Group> sumUp(List<SingleResult<Integer>> list, int target) 
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
            for (SingleResult<Integer> v : chosen.getValues())
            {
                list.remove(v);
            }
            retVal.add(chosen);
        }
        return retVal;
    }
    
    public static void calcIncrements(S7thSea2Results results, boolean doubleIncrements, int diffMod)
    {
        List<SingleResult<Integer>> list = results.getResults();
        List<Group> tmp;
        if (doubleIncrements)
        {
            tmp = sumUp(list, 15 + diffMod);
            for (Group g : tmp)
            {
                results.addDoubleIncrement(g.getValues());
            }
        }
        tmp = sumUp(list, 10 + diffMod);
        for (Group g : tmp)
        {
            results.addIncrement(g.getValues());
        }
        for (SingleResult<Integer> l : list)
        {
            results.getLeftovers().add(l);
        }
    }
    
}
