package com.lynchd49.pwp2p.utils


import org.passay.*
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class PasswordGenTest extends Specification {

    def "Password of length #length is generated"() {
        when:
        PasswordValidator validator = new PasswordValidator(
                new LengthRule(length,length),
                new WhitespaceRule()
        )
        String pw = PasswordGen.getNew(length, true, true, true, true)
        RuleResult result = validator.validate(new PasswordData(pw))

        then:
        result.isValid()

        where:
        length | _
        4      | _
        8      | _
        16     | _
        32     | _
        64     | _
        128    | _
        256    | _
        512    | _
        999    | _

    }

    def "Password with only #charType characters is generated"() {
        when:
        PasswordValidator validator = new PasswordValidator(
                new LengthRule(16,16),
                new CharacterRule(characterRule, 16),
                new WhitespaceRule()
        )
        String pw = PasswordGen.getNew(16, lower, upper, digits, special)
        RuleResult result = validator.validate(new PasswordData(pw))

        then:
        result.isValid() == valid

        where:
        charType     | characterRule                   | lower   | upper    | digits   | special  || valid

        "lower case" | EnglishCharacterData.LowerCase  | true    | false    | false    | false    || true
        "lower case" | EnglishCharacterData.LowerCase  | false   | true     | false    | false    || false
        "lower case" | EnglishCharacterData.LowerCase  | false   | false    | true     | false    || false
        "lower case" | EnglishCharacterData.LowerCase  | false   | false    | false    | true     || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | true     | false    | false    || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | false    | true     | false    || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | false    | false    | true     || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | true     | true     | false    || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | true     | false    | true     || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | false    | true     | true     || false
        "lower case" | EnglishCharacterData.LowerCase  | true    | true     | true     | true     || false

        "upper case" | EnglishCharacterData.UpperCase  | true    | false    | false    | false    || false
        "upper case" | EnglishCharacterData.UpperCase  | false   | true     | false    | false    || true
        "upper case" | EnglishCharacterData.UpperCase  | false   | false    | true     | false    || false
        "upper case" | EnglishCharacterData.UpperCase  | false   | false    | false    | true     || false
        "upper case" | EnglishCharacterData.UpperCase  | true    | true     | false    | false    || false
        "upper case" | EnglishCharacterData.UpperCase  | false   | true     | true     | false    || false
        "upper case" | EnglishCharacterData.UpperCase  | false   | true     | false    | true     || false
        "upper case" | EnglishCharacterData.UpperCase  | true    | true     | true     | false    || false
        "upper case" | EnglishCharacterData.UpperCase  | true    | true     | false    | true     || false
        "upper case" | EnglishCharacterData.UpperCase  | true    | false    | true     | true     || false
        "upper case" | EnglishCharacterData.UpperCase  | true    | true     | true     | true     || false

        "digit"      | EnglishCharacterData.Digit      | true    | false    | false    | false    || false
        "digit"      | EnglishCharacterData.Digit      | false   | true     | false    | false    || false
        "digit"      | EnglishCharacterData.Digit      | false   | false    | true     | false    || true
        "digit"      | EnglishCharacterData.Digit      | false   | false    | false    | true     || false
        "digit"      | EnglishCharacterData.Digit      | true    | false    | true     | false    || false
        "digit"      | EnglishCharacterData.Digit      | false   | true     | true     | false    || false
        "digit"      | EnglishCharacterData.Digit      | false   | false    | true     | true     || false
        "digit"      | EnglishCharacterData.Digit      | true    | true     | true     | false    || false
        "digit"      | EnglishCharacterData.Digit      | true    | true     | false    | true     || false
        "digit"      | EnglishCharacterData.Digit      | true    | false    | true     | true     || false
        "digit"      | EnglishCharacterData.Digit      | true    | true     | true     | true     || false

        "special"    | EnglishCharacterData.Special    | true    | false    | false    | false    || false
        "special"    | EnglishCharacterData.Special    | false   | true     | false    | false    || false
        "special"    | EnglishCharacterData.Special    | false   | false    | true     | false    || false
        "special"    | EnglishCharacterData.Special    | false   | false    | false    | true     || true
        "special"    | EnglishCharacterData.Special    | true    | false    | true     | true     || false
        "special"    | EnglishCharacterData.Special    | false   | true     | false    | true     || false
        "special"    | EnglishCharacterData.Special    | false   | false    | true     | true     || false
        "special"    | EnglishCharacterData.Special    | true    | true     | true     | false    || false
        "special"    | EnglishCharacterData.Special    | true    | true     | false    | true     || false
        "special"    | EnglishCharacterData.Special    | true    | false    | true     | true     || false
        "special"    | EnglishCharacterData.Special    | true    | true     | true     | true     || false
    }
}
