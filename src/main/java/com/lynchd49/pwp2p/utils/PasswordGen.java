package com.lynchd49.pwp2p.utils;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.util.ArrayList;
import java.util.List;

public class PasswordGen {

    public static String getNew(int length, boolean hasLower, boolean hasUpper, boolean hasDigits, boolean hasSpecial) {
        List<CharacterRule> rules = new ArrayList<>();
        if (hasLower) rules.add(new CharacterRule(EnglishCharacterData.LowerCase));
        if (hasUpper) rules.add(new CharacterRule(EnglishCharacterData.UpperCase));
        if (hasDigits) rules.add(new CharacterRule(EnglishCharacterData.Digit));
        if (hasSpecial) {
            // This is used rather than EnglishCharacterData.Special, as that contains certain special characters
            // (chars not found on a keyboard) that don't work with some online password systems, which leads to
            // your saved password not matching the one read in by the system
            rules.add(new CharacterRule(new CharacterData() {
                @Override
                public String getErrorCode() {
                    return "Error getting special characters for rule.";
                }

                @Override
                public String getCharacters() {
                    return "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
                }
            }));
        }
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        return passwordGenerator.generatePassword(length, rules);
    }
}
