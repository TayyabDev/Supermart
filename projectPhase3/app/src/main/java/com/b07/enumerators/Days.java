package com.b07.enumerators;

public enum Days {
  MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

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



