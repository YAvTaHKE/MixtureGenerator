package ru.GUI.Utilities;

public class HtmlStyler {
    private StringBuffer sb;

    public HtmlStyler(String string) {
        sb = new StringBuffer(string);
    }

    public String getHtmlString() {
        sb.insert(0, "<html>");
        sb.append("</html >");

        return sb.toString();
    }

    //Форматирование по центру
    public void center(){
        sb.insert(0, "<center>");
        sb.append("</center >");
    }

    //Форматирование жирным шрифтом
    public void bold(){
        sb.insert(0, "<b>");
        sb.append("</b >");
    }

}
