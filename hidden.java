import java.io.*;
import java.io.BufferedReader;
import java.math.BigInteger;

class hidden
{
    // p and q are both prime, e is coprime with (p-1)(q-1). Larger values of p and q mean longer lines of text can be saved and the private key is harder to calculate
    private static BigInteger p = new BigInteger ("656692050181897513638241554199181923922955921760928836766304161790553989228223793461834703506872747071705167995972707253940099469869516422893633357693");
    private static BigInteger q = new BigInteger ("550260068503913816794775756748734957217525557101277352617939473192429642846467658900405045171852174534967724674742228789190327015236678532191013890529");
    public static BigInteger e = new BigInteger ("1234777777777777777777777777"); // encryption key


    // EXAMPLE KEYS:
    // If these keys are used, all files must be rewritten, including account file
    //private static BigInteger p = new BigInteger ("48112959837082048697");
    //private static BigInteger q = new BigInteger ("29497513910652490397");
    //private static BigInteger e = new BigInteger ("65537");


    private static BigInteger n = p.multiply (q); // ciphertext and plaintext are calculated using (mod n)
    private static BigInteger nsub1 = p.subtract (BigInteger.valueOf (1)).multiply (q.subtract (BigInteger.valueOf (1))); // used to calculate decryption key
    private static BigInteger d = e.modInverse (nsub1); // decryption key, should be private
    private static String accountfile = "accounts.txt";
    public static int useramount = 50; // maximum number of users in account file
    public static int currentuser = -1; // 'id' of logged in user, used to change the password of the current user
    private static int access = 0; // gives access to functions of hidden
    private static String input = " "; // user input
    private static String[] [] accounts = returnaccounts (accountfile); // [n] [0] is user [n] [1] is pw

    public static int countlines (String f)  // unused method, returns number of lines in a text file
    {
	int lines = 0;
	try
	{
	    FileReader readfrom = new FileReader (f);
	    BufferedReader reader = new BufferedReader (readfrom);
	    String s = reader.readLine ();
	    while (s != null)
	    {
		lines++;
		s = reader.readLine ();
	    }
	    reader.close ();
	}
	catch (IOException e)
	{
	}
	return lines;
    }


    public static String[] [] returnaccounts (String filename)  // returns accounts data in a two dimensional array
    {
	String[] [] userinfo = new String [useramount] [useramount];
	try
	{
	    int s = 0;
	    // reads lines in filename
	    FileReader fr = new FileReader (filename);
	    BufferedReader b = new BufferedReader (fr);
	    String readfile = b.readLine ();
	    while (readfile != " " && s < useramount)
	    {
		// lines in accountfile are alternating usernames and passwords, starting with the username of user [0]
		//userinfo [s] [0] = readfile; //when reading from plaintext file
		userinfo [s] [0] = decrypt (readfile);
		readfile = b.readLine ();
		//userinfo [s] [1] = readfile;
		userinfo [s] [1] = decrypt (readfile);
		readfile = b.readLine ();
		s = s + 1;
	    }
	    b.close ();
	}
	catch (IOException e)
	{
	}
	for (int a = 0 ; a < useramount ; a++) // each null element in each array is set to a default value that cannot be used to log in ("username" & "password")
	{
	    if (userinfo [a] [0] == null && userinfo [a] [1] == null)
	    {
		userinfo [a] [0] = "username";
		userinfo [a] [1] = "password";
	    }
	}
	return userinfo;
    }


    public static void grantaccess (String user, String pass) // determines whether or not user has access to methods in hidden.java
    {
	access = 0; // when set 1, user has access to account creation, password changing, file read/write, etc.
	currentuser = -1;
	String pwcheck = " ";
	for (int b = 0 ; b < useramount ; b++) // checks if username and password match an account
	{
	    if (user.toLowerCase ().equals (accounts [b] [0].toLowerCase ()) && pass.equals (accounts [b] [1]))
	    {
		currentuser = b; // 'id number' of user that is logged in
		access = 1; // log in successful
	    }
	}
	if (access == 1)
	{
	    System.out.println ("Access granted");
	    while (true)
	    {
		System.out.print ("Enter 'logout' to logout, 'modify pass' to change password, or 'create' to create a new account\nEnter 'encrypt' or 'files' to encrypt or view files\nEnter 'exit' to terminate program\n");
		input = LogIn.getInput ();
		input = input.toLowerCase ();
		if (!input.equals ("logout") && !input.equals ("modify pass") && !input.equals ("exit") && !input.equals ("create") && !input.equals ("encrypt") && !input.equals ("files"))
		{
		    System.out.println ("Invalid input"); // new input required
		}
		else if (input.equals ("logout"))
		{
		    LogIn.lockout = 0;
		    break;
		}
		else if (input.equals ("modify pass"))
		{
		    while (true)
		    {
			System.out.println ("Please input your new password");
			input = LogIn.getInput ();
			if (input.equals ("password"))
			{
			    System.out.println ("Password may not be 'password'");
			}
			else
			{
			    break;
			}
		    }
		    while (true)
		    {
			System.out.println ("Please confirm your new password");
			pwcheck = LogIn.getInput ();
			if (pwcheck.equals (input))
			{
			    changepass (input, currentuser);
			    break;
			}
			else
			{
			    System.out.println ("Password unsuccessfully changed.");
			    break;
			}
		    }
		}
		else if (input.equals ("create"))
		{
		    int freevalue = -1;
		    int c = 0;
		    while (freevalue == -1 && c < useramount)
		    {
			if ((accounts [c] [0]).equals ("username") && (accounts [c] [1]).equals ("password") && freevalue == -1)
			{
			    freevalue = c;
			    c = useramount;
			}
			c++;
		    }
		    if (freevalue != -1)
		    {
			createacc (freevalue);
		    }
		    else
		    {
			System.out.println ("User database full\nCannot create a new account");
		    }
		}
		else if (input.equals ("encrypt"))
		{
		    System.out.println ("Input 'exit' at any time to cancel account creation");
		    while (true)
		    {
			System.out.println ("Please input the name of the file you would like to create, with '.txt' added to the end");
			input = LogIn.getInput ();
			if (input.toLowerCase ().equals ("exit"))
			{
			    break;
			}
			File f = new File (input);
			if (f.isFile ())
			{
			    System.out.println ("File already exists");
			}
			else
			{
			    createFile (input);
			    System.out.println ("File created\n");
			}
		    }
		}
		else if (input.equals ("files"))
		{
		    while (true)
		    {
			System.out.println ("Please input the name of the file you would like to view");
			System.out.println ("Input 'exit' to cancel file viewing");
			input = LogIn.getInput ().toLowerCase ();
			if (input.equals ("exit"))
			{
			    break;
			}
			else if (input.equals (accountfile))
			{
			    System.out.println ("Cannot open " + accountfile);
			}
			File f = new File (input);
			if (f.isFile () && f.canRead ())
			{
			    try
			    {
				System.out.println ("Outputting file\n\n");
				FileReader p = new FileReader (input);
				BufferedReader br = new BufferedReader (p);
				String line = br.readLine ();
				while (line != null)
				{
				    line = decrypt (line);
				    System.out.println (line);
				    line = br.readLine ();
				}
				br.close ();
				System.out.print ("\nEnd of File\n");
			    }
			    catch (IOException e)
			    {
			    }
			}
			else
			{
			    System.out.println ("File does not exist or cannot be read");
			}
		    }
		}
		else if (input.equals ("exit"))
		{
		    System.exit (0);
		}
	    }
	}
	else
	{
	    System.out.println ("Invalid credentials");
	    LogIn.lockout = LogIn.lockout + 1;
	}
    }


    private static void changepass (String newpass, int user)  // changes the current user's password
    {
	accounts [user] [1] = newpass;
	saveAccounts (accountfile);
	System.out.println ("Password successfully changed");
    }


    public static void createacc (int accountnum)  // create an account, data stored in accounts [accountnum] [0] and accounts [accountnum] [1]
    {
	int check = 0;
	String desireduname = "username";
	System.out.println ("Input 'exit' at any time to cancel account creation");
	while (true)
	{
	    check = 0;
	    System.out.println ("Please input your desired username");
	    input = LogIn.getInput ();
	    if (input.toLowerCase ().equals ("exit")) // sets check to useramount to avoid next loop
	    {
		check = useramount;
		break;
	    }
	    desireduname = input;
	    while (check < useramount)
	    {
		if (input.toLowerCase ().equals ("username"))
		{
		    System.out.println ("Invalid username");
		    createacc (accountnum);
		}
		else if (input.toLowerCase ().equals (accounts [check] [0].toLowerCase ())) // username already exists
		{
		    System.out.println ("Username taken");
		    input = "username";
		    createacc (accountnum);
		}
		else
		{
		    check++;
		}
	    }
	    if (!desireduname.toLowerCase ().equals ("username"))
	    {
		break;
	    }
	}
	if (!input.toLowerCase ().equals ("exit"))
	{
	    while (true)
	    {
		System.out.println ("Please input your desired password");
		input = LogIn.getInput ();
		if (input.toLowerCase ().equals ("exit")) // exits account creation
		{
		    break;
		}
		else if (input.toLowerCase ().equals ("password")) // invalid password
		{
		    System.out.println ("Password may not be 'password'");
		}
		else // valid password
		{
		    // given info added to accounts [] [] and encrypted in accountfile
		    accounts [accountnum] [0] = desireduname;
		    accounts [accountnum] [1] = input;
		    saveAccounts (accountfile);
		    System.out.println ("Account with the username " + desireduname + " created");
		    break;
		}
	    }
	}
	else
	{
	}
    }


    public static void createFile (String title)  // creates a file with the filename 'title'
    {
	try
	{
	    FileWriter fw = new FileWriter (title);
	    PrintWriter print = new PrintWriter (fw);
	    String line;
	    System.out.println ("Input 'end' to save the file");
	    do
	    {
		System.out.println ("Please input the next line of the file to be encrypted");
		line = LogIn.getInput ();
		if (line.toLowerCase ().equals ("end")) // end data input and print nothing if 'end' is given
		{
		    break;
		}
		else // otherwise print the encrypted line
		{
		    print.println (encrypt (line));
		}
	    }
	    while (!line.toLowerCase ().equals ("end"));
	    print.close ();
	    System.out.println ("File successfully created");
	}
	catch (IOException e)
	{
	}
    }


    public static String encrypt (String plain)  // encrypts the plaintext 'plain'
    {
	String cipher = ""; // ciphertext
	// converts each character in the plaintext into its ascii (integer) representation,
	// appends each representation onto cipher
	while (plain.length () > 0)
	{
	    char convert = plain.charAt (0);
	    plain = plain.substring (1);
	    String toadd = Integer.toString ((int) convert);
	    if (toadd.length () == 2) // Ensures every 3 integers in the string represents 1 character
	    {
		toadd = "0".concat (toadd);
	    }
	    else
	    {
	    }
	    cipher = cipher.concat (toadd);
	}
	BigInteger BigIntcipher = new BigInteger (cipher); //cipher in BigInteger form
	cipher = BigIntcipher.modPow (e, n).toString (); //cipher gets encrypted as a BigInteger, then parsed to a string
	return cipher;
    }


    public static String decrypt (String cipher)  // decrypts the ciphertext 'cipher'
    {
	String plain = ""; // plaintext
	char toappend = 'a'; // char to be appended to plain
	String toappendstring = "a"; // 'toappend' but as a string
	BigInteger number = new BigInteger (cipher);
	// Numerical value of cipher gets decrypted and parsed to String
	BigInteger numberdecrypt = number.modPow (d, n);
	String numberstring = numberdecrypt.toString ();
	// If the first character in the numerical representation of the plaintext < 100,
	// It is represented as a two digit integer. It must be represented as a three digit integer
	if ((numberstring.length ()) % 3 != 0)
	{
	    numberstring = "0".concat (numberstring);
	}
	else
	{
	}
	// Converts the message from its ascii representation into a String of characters
	while (numberstring.length () > 0)
	{
	    toappend = (char) (Integer.parseInt (numberstring.substring (0, 3)));
	    toappendstring = Character.toString (toappend);
	    numberstring = numberstring.substring (3);
	    plain = plain.concat (toappendstring);
	}
	return plain;
    }


    public static void saveAccounts (String filename)  //writes all accounts to file 'filename'
    {
	try
	{
	    FileWriter f = new FileWriter (filename);
	    PrintWriter p = new PrintWriter (f);
	    int x = 0;
	    while (x < useramount)
	    {
		//p.println (accounts [x] [0]); // prints unencrypted lines
		//p.println (accounts [x] [1]);
		p.println (encrypt (accounts [x] [0])); //encrypted user
		p.println (encrypt (accounts [x] [1])); //encrypted pass
		x = x + 1;
	    }
	    p.close ();
	}
	catch (IOException e)
	{
	}
    }
}


