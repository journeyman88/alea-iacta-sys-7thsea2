/*
 * Copyright 2021 m.bignami.
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.unknowndomain.alea.random.SingleResult;

/**
 *
 * @author m.bignami
 */
public class Raise
{
    private final List<SingleResult<Integer>> dice;

    public Raise(Collection<SingleResult<Integer>> dice)
    {
        List<SingleResult<Integer>> tmp = new ArrayList<>(dice.size());
        tmp.addAll(dice);
        this.dice = Collections.unmodifiableList(tmp);
    }

    public List<SingleResult<Integer>> getDice()
    {
        return dice;
    }
    
    public String format()
    {
        StringBuilder sb = new StringBuilder("(");
        for (SingleResult<Integer> d : dice)
        {
            if (sb.length() >= 2)
            {
                sb.append("+");
            }
            sb.append(d.getValue());
        }
        sb.append(")");
        return sb.toString();
    }
}
