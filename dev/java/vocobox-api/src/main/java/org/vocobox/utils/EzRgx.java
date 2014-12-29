package org.vocobox.utils;

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
}
