package ru.GUI.Utilities;

import java.awt.*;

public class HtmlStyler {
    private StringBuffer sb;

    public HtmlStyler(String string) {
        sb = new StringBuffer(string);
    }

    public String getHtmlString() {
        sb.insert(0, "<html>");
        sb.append("</html>");

        return sb.toString();
    }

    //Форматирование по центру
    public void center(){
        sb.insert(0, "<center>");
        sb.append("</center>");
    }

    //Форматирование жирным шрифтом
    public void bold(){
        sb.insert(0, "<b>");
        sb.append("</b>");
    }

    //Форматирование подчеркивание
    public void underlined(){
        sb.insert(0, "<u>");
        sb.append("</u>");
    }

    //Вставка переноса на другую строку
    public void breakLine(int offset){
        sb.insert(offset, "<br>");
    }

    public void fontColor(Color color){
        String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);
        sb.insert(0, "<font color=" + hex + ">");
        sb.append("</font>");
    }

    //Форматирование курсивом
    public void italic(){
        sb.insert(0, "<em>");
        sb.append("</em>");
    }
}
