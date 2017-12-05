package com.b07.enumerators;

public enum Days {
  SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

  private int day;

  private Days(int day){
    this.day = day;
  }

  public static Days getDay(int day) {
    for (Days currentDay : Days.values()) {
      if (currentDay.day == day) {
        return currentDay;
      }
    }

    return null;
  }}



