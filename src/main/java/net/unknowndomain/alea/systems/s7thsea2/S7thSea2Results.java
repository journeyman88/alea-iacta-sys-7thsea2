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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author journeyman
 */
public class S7thSea2Results extends GenericResult
{
    private final List<SingleResult<Integer>> results;
    private int increments = 0;
    private List<SingleResult<Integer>> leftovers = new LinkedList<>();
    private List<Raise> usedDice = new LinkedList<>();
    private SingleResult<Integer> oldValue;
    private List<SingleResult<Integer>> newValue;
    private S7thSea2Results prev;
    private Locale lang;
    
    public S7thSea2Results(List<SingleResult<Integer>> results)
    {
        List<SingleResult<Integer>> tmp = new ArrayList<>(results.size());
        tmp.addAll(results);
        this.results = Collections.unmodifiableList(tmp);
    }
    
    private void addIncrements(int value, SingleResult<Integer> ... dice)
    {
        this.addIncrements(value, Arrays.asList(dice));
    }
    
    public void addIncrement(SingleResult<Integer> ... dice)
    {
        addIncrements(1, dice);
    }
    
    public void addDoubleIncrement(SingleResult<Integer> ... dice)
    {
        addIncrements(2, dice);
    }
    private void addIncrements(int value, Collection<SingleResult<Integer>> dice)
    {
        increments += value;
        usedDice.add(new Raise(dice));
    }
    
    public void addIncrement(Collection<SingleResult<Integer>> dice)
    {
        addIncrements(1, dice);
    }
    
    public void addDoubleIncrement(Collection<SingleResult<Integer>> dice)
    {
        addIncrements(2, dice);
    }

    public int getIncrements()
    {
        return increments;
    }

    public List<Raise> getUsedDice()
    {
        return usedDice;
    }

    public List<SingleResult<Integer>> getLeftovers()
    {
        return leftovers;
    }

    public List<SingleResult<Integer>> getResults()
    {
        return results;
    }

    public List<SingleResult<Integer>> accessNewValue()
    {
        return newValue;
    }

    public void setNewValue(List<SingleResult<Integer>> newValue)
    {
        this.newValue = newValue;
    }

    public SingleResult<Integer> accessOldValue()
    {
        return oldValue;
    }

    public void setOldValue(SingleResult<Integer> oldValue)
    {
        this.oldValue = oldValue;
    }

    public S7thSea2Results accessPrev()
    {
        return prev;
    }

    public void setPrev(S7thSea2Results prev)
    {
        this.prev = prev;
    }
    
    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        ResourceBundle i18n = ResourceBundle.getBundle("net.unknowndomain.alea.systems.s7thsea2.RpgSystemBundle", lang);
        String indent = getIndent(indentValue);
        messageBuilder.append(indent).append(String.format(i18n.getString("7thsea.results.raises"), getIncrements())).append(" ");
        for (Raise inc : getUsedDice())
        {
            messageBuilder.append(inc.format()).append(" ");
        }
        messageBuilder.appendNewLine();
        messageBuilder.append(indent).append(String.format(i18n.getString("7thsea.results.unusedDice"), getLeftovers().size())).append(" [ ");
        for (SingleResult<Integer> t : getLeftovers())
        {
            messageBuilder.append(t.getValue()).append(" ");
        }
        messageBuilder.append("]\n");
        if (verbose)
        {
            messageBuilder.append(indent).append("Roll ID: ").append(getUuid()).appendNewLine();
            messageBuilder.append(indent).append(i18n.getString("7thsea.results.diceResults")).append(" [ ");
            for (SingleResult<Integer> t : getResults())
            {
                messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                messageBuilder.append(t.getValue()).append(") ");
            }
            messageBuilder.append("]\n");
        }
        if (prev != null)
        {
            
            messageBuilder.append(String.format(i18n.getString("7thsea.results.reroll"), prev));
            messageBuilder.append(" (").append(accessOldValue().getValue()).append(" => ");
            if (accessNewValue().size() > 1)
            {
                messageBuilder.append("[ ");
            }
            for (SingleResult<Integer> t : accessNewValue())
            {
                messageBuilder.append(t.getValue()).append(" ");
            }
            if (accessNewValue().size() > 1)
            {
                messageBuilder.append("] ");
            }
            messageBuilder.append(")").appendNewLine();
            if (verbose)
            {
                messageBuilder.append(i18n.getString("7thsea.results.prevResults")).append("{\n");
                prev.formatResults(messageBuilder, verbose, indentValue + 4);
                messageBuilder.append("}\n");
            }
        }
    }

    public Locale getLang()
    {
        return lang;
    }

    public void setLang(Locale lang)
    {
        this.lang = lang;
    }

}
