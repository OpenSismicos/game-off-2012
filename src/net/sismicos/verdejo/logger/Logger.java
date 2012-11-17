package net.sismicos.verdejo.logger;

public final class Logger {
	// debug level
    private static int debugLevel = 0;
    
    /** private constructor */
    private Logger() {}
    
    /**
     * Get the debug level.
     * @return Current debug level.
     */
    public static int getDebugLevel() {
            return debugLevel;
    }
    
    /**
     * Set the debug level.
     * @param level New debug level.
     */
    public static void setDebugLevel(int level) {
            debugLevel = level;
    }
    
    /**
     * Print functions.
     * @param m Message.
     * @param level Debug level of the message.
     */
    public static void printErr(String m) {
            print("(EE) " + m);
    }
    public static void printWarn(String m) {
            print("(WW) " + m);
    }
    public static void printInfo(String m) {
            print("(II) " + m);
    }
    public static void printDebug(String m) {printDebug(m, 0);}
    public static void printDebug(String m, int level) {
            if(level <= debugLevel)
                    print("(DD) " + m);
    }       
    
    /**
     * Internal method to print a message.
     * @param m Message.
     */
    private static void print(String m) {
            System.out.println(m);
    }
}
