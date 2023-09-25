package com.oxahex.stockdividendapi.model.constants;

public enum Month {
    JAN("Jan", 1),
    FEB("Feb", 2),
    MAR("Mar", 3),
    APR("Apr", 4),
    MAY("May", 5),
    JUN("Jun", 6),
    JUL("Jul", 7),
    AUG("Aug", 8),
    SEP("Sep", 9),
    OCT("Oct", 10),
    NOV("Nov", 11),
    DEC("Dec", 12);

    private final String mo;
    private final int number;

    Month(String mo, int number) {
        this.mo = mo;
        this.number = number;
    }

    public static int strToNumber(String s) {
        for (Month month : Month.values()) {
            if (month.mo.equals(s)) return month.number;
        }

        return -1;
    }
}