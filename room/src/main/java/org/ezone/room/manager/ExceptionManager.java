package org.ezone.room.manager;

public class ExceptionManager {
    public static void iserror(boolean bool)throws Exception{
        if(!bool){throw new Exception();}
    }
}
