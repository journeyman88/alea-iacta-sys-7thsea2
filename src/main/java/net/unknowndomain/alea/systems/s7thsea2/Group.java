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

public class Group
{
    private final List<D10Values> values;

    public Group(List<D10Values> values)
    {
        this.values = values;
    }
    
    public int getTotal()
    {
        int sum = 0;
        for (D10Values v : values)
        {
            sum += v.getValue();
        }
        return sum;
    }
    
    public int getSize()
    {
        return values.size();
    }

    public List<D10Values> getValues()
    {
        return values;
    }

    public List<Integer> getDices()
    {
        List<Integer> retVal = new ArrayList<>(values.size());
        for (D10Values d : values)
        {
            retVal.add(d.getValue());
        }
        return retVal;
    }
    
}
