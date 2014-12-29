package org.vocobox.io;

import java.util.regex.Pattern;

public class EzRgx {
    // music
    public static String alteration = "[#|b]{0,1}";

    public static String letter = "\\w";
    public static String word = "\\w+";
    public static String letter_and_nums = "[\\w|\\d]+";
    public static String word_list = "[\\w|,]+";
    public static String number = "\\d";
    public static String numbers = "\\d+";

    public static String space = "\\s+";
    public static String space_or_not = "\\s*";
    public static String spnt = space_or_not;
    public static String non_space = "\\S+";

    public static String any = ".*";
    public static String no_greedy_any = ".*?";

    public static String antislash = "\\\\";
    public static String slash = "\\/";
    public static String path = "[\\w|\\d|\\-|\\/\\\\|\\.]+";

    public static String start_with = "^";
    public static String end_with = "$";

    public String pattern;

    public EzRgx() {
        pattern = "";
    }

    public static EzRgx newRgx() {
        return new EzRgx();
    }

    public EzRgx(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public Pattern compilePattern() {
        return Pattern.compile(pattern);
    }

    /**
     * Append a pattern to current pattern, and return this
     * 
     * @param pattern
     * @return
     */
    public EzRgx group(String pattern) {
        this.pattern = this.pattern + "(" + pattern + ")";
        return this;
    }

    public EzRgx letter() {
        return group(letter);
    }

    public EzRgx letter(String occurence) {
        return group(letter, occurence);
    }

    public EzRgx letter(String before, String after, String occurence) {
        return group(before + letter + after, occurence);
    }

    public EzRgx groupNumbers() {
        return group(numbers);
    }

    public EzRgx number() {
        return group(number);
    }
    
    public EzRgx optionNumbers(){
        return group("-" + numbers, "0,1");
    }
    
    public EzRgx optionWord(){
        return group("-" + word, "0,1");
    }
    
    public EzRgx optionLetter(String before, String after) {
        return letter(before, after, "0,1");
    }

    public EzRgx optionTwoLetters(String before) {
        return optionLetter(before, "{0,2}-");
    }


    public EzRgx alteration() {
        return group(alteration);
    }

    public EzRgx note() {
        return letter().alteration().number();
    }

    /**
     * 
     * @param pattern
     * @param occurence
     *            "0", "0,1", "0,4"
     * @return
     */
    public EzRgx group(String pattern, String occurence) {
        this.pattern = this.pattern + "(" + pattern + "){" + occurence + "}";
        return this;
    }

    public EzRgx append(String pattern) {
        this.pattern = this.pattern + pattern;
        return this;
    }

    /**
     * Appends an extension .ext("wav") adds ".wav" to pattern
     * 
     * @param pattern
     * @return
     */
    public EzRgx ext(String pattern) {
        this.pattern = this.pattern + "." + pattern;
        return this;
    }

    public EzRgx extWav() {
        return ext("wav");
    }
}
