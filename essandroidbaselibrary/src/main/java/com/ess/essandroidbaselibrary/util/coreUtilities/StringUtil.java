package com.ess.essandroidbaselibrary.util.coreUtilities;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtil
{
    public static String removeOuter(String in, char f, char l)
    {
        in = in.trim();

        if (!in.isEmpty() && in.charAt(0) == f)
        {
            in = in.substring(1);
        }

        if (!in.isEmpty() && in.charAt(in.length() - 1) == l)
        {
            in = in.substring(0, in.length() - 1);
        }

        return in;
    }
}
