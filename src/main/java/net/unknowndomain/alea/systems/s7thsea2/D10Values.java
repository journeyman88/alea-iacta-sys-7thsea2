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

/**
 *
 * @author journeyman
 */
public enum D10Values
    {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        // Used to allow the --add parameter
        ELEVEN(11),
        TWELVE(12),
        THIRTEEN(13),
        FOURTEEN(14),
        FIFTEEN(15),
        SIXTEEN(16),
        SEVENTEEN(17),
        EIGHTEEN(18),
        NINETEEN(19),
        TWENTY(20);
        
        private final int value;
        
        private D10Values(int val)
        {
            this.value = val;
        }

        public int getValue()
        {
            return value;
        }
        
        public static D10Values find(int value)
        {
            for (D10Values v : D10Values.values())
            {
                if (value == v.getValue())
                {
                    return v;
                }
            }
            return null;
        }
        
    }
