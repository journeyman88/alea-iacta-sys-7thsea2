# alea-iacta-sys-7thsea2
A RPG system module for Alea Iacta Est implementing 7th Sea - 2nd Edition

## Description
This command will roll several d10 and will try to group them together to generate the maximum number of Raises, this will work in 2 different modes - Simple Mode and Character Mode - to which can be applied different modifiers.

### Simple Mode
This mode simply roll a certain number of dice and tries to calculate the Raises using basic rules (if no modifier is attached).

### Character Mode
This mode, asides from rolling the dice determined by the sum of the parameters, uses the information provided to auto-activate eventual modifiers (reroll one dice, enable double Raises, enable dice explosion) according to the rules. Some modifiers, like 'Joie de Vivre' only works in this mode.
If a modifier is passed, it will compound with those deducted from the parameters.

### Roll modifiers
Passing these parameters, the associated modifier will be enabled:

* `-v` : Will enable a more verbose mode that will show a detailed version of every result obtained in the roll.
* `-x` : Will enable the dice explosion on a natural 10.
* `-d` : Will enable the double Raises at a 15 or more sum, and calculate the Raises accordingly.
* `-r` : Will enable the reroll of a single dice, if there's one or more leftover dice that are not part of any Raise, the lowest one will be rerolled and the Raises recalculated.
* `-i` : Will increase the cost for each Raise by 5 as per Danger Points rules.
* `-j` : Only work in Character Mode, enables the 'Joie de Vivre' advantage.

## Help print

``
7th Sea 2nd Edition [ 7th-sea-2nd | 7s2 ]

Usage: 7s2 [-dhirvx] -n <diceNumber>
   or: 7s2 [-dhirvx] -t <traitRank> -s <skillRank> [-b <bonusDice>] [-j]

Description:
This command will roll several d10 and will try to group them
together to generate the maximum number of Raises, this will
work in 2 different modes - Simple Mode and Character Mode -
to which can be applied different modifiers.

Options:
  -t, --trait=traitValue    Trait level
  -s, --skill=skillRank     Skill level
  -b, --bonus=bonusDice     Bonus/Malus dice
  -j, --joie                Enable the 'Joie de vivre' advantage
  -n, --number=diceNumber   Roll this number of dice
  -r, --reroll              Force reroll of one dice enabled
  -d, --double              Force 'Double Raise' mode enabled
  -x, --explode             Force dice exposion enabled
  -i, --increase            Increase the 'cost' of a Raise by 5
  -h, --help                Print the command help
  -v, --verbose             Enable verbose output
``vv
