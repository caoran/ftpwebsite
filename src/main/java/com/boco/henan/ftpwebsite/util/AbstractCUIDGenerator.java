package com.boco.henan.ftpwebsite.util;

public abstract class AbstractCUIDGenerator
{
    private static final int IP;

    static
    {
        int ipadd;
        try
        {
            int result = 0;
            for (int i = 0; i < 4; i++) {
                result = (result << 8) - -128 + java.net.InetAddress.getLocalHost().getAddress()[i];
            }
            ipadd = result;
        }
        catch (Exception e)
        {
            ipadd = 0;
        }
        IP = ipadd;
    }

    private static short counter = 0;
    private static final int JVM = (int)(System.currentTimeMillis() >>> 8);

    protected int getJVM()
    {
        return JVM;
    }

    protected short getCount()
    {
        synchronized (AbstractCUIDGenerator.class)
        {
            if (counter < 0) {
                counter = 0;
            }
            return counter++;
        }
    }

    protected int getIP()
    {
        return IP;
    }

    protected short getHiTime()
    {
        return (short)(int)(System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime()
    {
        return (int)System.currentTimeMillis();
    }
}
