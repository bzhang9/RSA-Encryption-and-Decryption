/* Bob Zhang
Users may log in and create accounts. Default file to store account details is 'accounts.txt'

FEATURES
// RSA Encryption and decryption of files
// limited log in attempts
// ability to create new accounts and modify existing users' passwords
// users are loaded and saved every time program is run
// database can hold a custom number of users via editing the value of useramount in hidden.java (default and minimum is max of 50 users)

SECURITY MEASURES
// limited log in attempts. After 5 unsuccessful log in attempts, user is locked out of program.
// a log in combination of 'username' and 'password' is not allowed
// password input is case sensitive
// every username must be unique

files should have line length <100

*/

import java.io.*;
import java.io.BufferedReader;
import java.math.BigInteger;

class LogIn
{
    public static BufferedReader u = new BufferedReader (new InputStreamReader (System.in));
    public static int s = 0;

    public static int lockout = 0; //each incorrect log in attempt increases this value by 1, at 5, user is kicked from program


    public static void main (String[] args)
    {
	new LogIn ();

    }


    public void lockedout ()
    {
	System.out.println ("You have been locked out of the program.");
    }


    public static String getInput ()
    {
	String a = " ";
	try
	{
	    a = u.readLine ();
	}
	catch (IOException e)
	{
	}
	return a;
    }


    public LogIn ()
    {
	String input = " ";
	String userinput, passinput;

	while (true)
	{
	    if (lockout >= 5)
	    {
		lockedout ();
		break;
	    }
	    System.out.println ("Input 'login' to log in.\nInput 'exit' to terminate the program.");
	    input = getInput ();
	    input = input.toLowerCase ();
	    if (input.equals ("login"))
	    {
		while (true)
		{
		    System.out.print ("Username: ");
		    userinput = getInput ();
		    if (userinput.equals ("username"))
		    {
			System.out.println ("Username may not be 'username'");
		    }
		    else
		    {
			break;
		    }
		}
		while (true)
		{
		    System.out.print ("Password: ");
		    passinput = getInput ();
		    if (passinput.equals ("password"))
		    {
			System.out.println ("Password may not be 'password'");
		    }
		    else
		    {
			break;
		    }
		}
		hidden.grantaccess (userinput, passinput);
	    }
	    else if (input.equals ("exit"))
	    {
		System.exit (0);
	    }
	    else
	    {
		System.out.println ("Invalid input.");
	    }
	}
    }
}
