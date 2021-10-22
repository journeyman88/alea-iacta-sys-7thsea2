/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unknowndomain.alea.systems.s7thsea2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;


/**
 *
 * @author journeyman
 */
@RpgSystemData(bundleName = "net.unknowndomain.alea.systems.s7thsea2.RpgSystemBundle", groupsName = {"character","simple"}, groupsDesc = {"Character Mode", "Simple Mode"})
public class S7thSea2Options extends RpgSystemOptions
{
    @RpgSystemOption(name = "trait", shortcode = "t", description = "7thsea.options.trait", argName = "traitValue", groupName = "character", groupRequired = true)
    private Integer trait;
    @RpgSystemOption(name = "skill", shortcode = "s", description = "7thsea.options.skill", argName = "skillRank", groupName = "character", groupRequired = true)
    private Integer skill;
    @RpgSystemOption(name = "bonus", shortcode = "b", description = "7thsea.options.bonus", argName = "bonusDice", groupName = "character")
    private Integer bonus;
    @RpgSystemOption(name = "joie", shortcode = "j", description = "7thsea.options.joie", groupName = "character")
    private boolean joie;
    @RpgSystemOption(name = "number", shortcode = "n", description = "7thsea.options.number", argName = "diceNumber", groupName = "simple", groupRequired = true)
    private Integer number;
    @RpgSystemOption(name = "reroll", shortcode = "r", description = "7thsea.options.reroll")
    private boolean reroll;
    @RpgSystemOption(name = "double", shortcode = "d", description = "7thsea.options.double")
    private boolean doubleIncrement;
    @RpgSystemOption(name = "explode", shortcode = "x", description = "7thsea.options.explode")
    private boolean explode;
    @RpgSystemOption(name = "increase", shortcode = "i", description = "7thsea.options.increase")
    private boolean increase;
    @RpgSystemOption(name = "add", shortcode = "a", description = "7thsea.options.addValue", argName = "value")
    private Integer addValue;
    
    @Override
    public boolean isValid()
    {
        return !(isHelp() || ((number != null) && (trait != null || skill != null || bonus != null || joie)) || ((trait != null) ^ (skill != null)));
    }
    
    public boolean isCharacterMode()
    {
        return (trait != null && skill != null);
    }
    
    public boolean isSimpleMode()
    {
        return (number != null);
    }

    public Integer getTrait()
    {
        return trait;
    }

    public Integer getSkill()
    {
        return skill;
    }

    public Integer getBonus()
    {
        return bonus;
    }

    public Integer getNumber()
    {
        return number;
    }

    public boolean isReroll()
    {
        return reroll;
    }

    public boolean isDoubleIncrement()
    {
        return doubleIncrement;
    }

    public boolean isExplode()
    {
        return explode;
    }

    public boolean isIncrease()
    {
        return increase;
    }

    public boolean isJoie()
    {
        return joie;
    }

    public Collection<S7thSea2Modifiers> getModifiers()
    {
        Set<S7thSea2Modifiers> mods = new HashSet<>();
        if (isVerbose())
        {
            mods.add(S7thSea2Modifiers.VERBOSE);
        }
        if (isReroll())
        {
            mods.add(S7thSea2Modifiers.REROLL_LEFTOVER);
        }
        if (isDoubleIncrement())
        {
            mods.add(S7thSea2Modifiers.DOUBLE_INCREMENT);
        }
        if (isExplode())
        {
            mods.add(S7thSea2Modifiers.EXPLODING_DICE);
        }
        if (isJoie())
        {
            mods.add(S7thSea2Modifiers.JOIE_DE_VIVRE);
        }
        if (isIncrease())
        {
            mods.add(S7thSea2Modifiers.INCREASED_DIFFICULTY);
        }
        return mods;
    }

    public Integer getAddValue()
    {
        return addValue;
    }

    public void setAddValue(Integer addValue)
    {
        this.addValue = addValue;
    }
    
}