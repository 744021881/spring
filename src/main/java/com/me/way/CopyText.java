package com.me.way;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyText {
    public static boolean copySomething(String text){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); //得到系统剪贴板
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, null);
        return true;
    }
    public static void main(String[] args) {
       boolean rs= copySomething("111");
    }
}
