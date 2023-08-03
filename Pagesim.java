import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.InputMismatchException;

class Pagesim {
	static final int V_PG = 10; // number of virtual pages (labeled from 0 to V_PG - 1)
	static final int P_PG = 7; // max number of physical pages (labeled from 0 to PG -1)

	public static void main(String[] args) {
		String[] strAr2 = { "3" };
		int numOfPhysicalFrames = readArg(strAr2);
		System.out.println("Number of page frames set to " + numOfPhysicalFrames + ".");
		Scanner in = new Scanner(System.in);
		String line;
		RefString rs = null;
		MemorySim sim;

		while (true) {
			System.out.println();
			System.out.println("Please choose from the following options:");
			System.out.println("0 - Exit");
			System.out.println("1 - Read reference string");
			System.out.println("2 - Generate reference string");
			System.out.println("3 - Display current reference string");
			System.out.println("4 - Simulate FIFO");
			System.out.println("5 - Simulate OPT");
			System.out.println("6 - Simulate LRU");
			System.out.println("7 - Simulate LFU");
			System.out.println();
			line = in.next();
			in.nextLine();
			switch (line) {
				case "0":
					System.out.println("Goodbye.");
					System.exit(0);
					break;
				case "1":
					rs = readRefString(in);
					stringConfirm(rs);
					break;
				case "2":
					System.out.println("How long do you want the reference string to be?");
					int stringSize = getStringSize(in);
					rs = generateString(stringSize, V_PG);
					stringConfirm(rs);
					break;
				case "3":
					if (rs != null) {
						System.out.print("Current reference string: ");
						rs.print();
						System.out.print(".");
					} else {
						System.out.println("Error: no reference string entered.");
					}
					break;
				case "4":
					if (rsIsSet(rs)) {
						sim = new MemorySim(rs, numOfPhysicalFrames, V_PG);
						sim.generate("FIFO");
						sim.print();
					}
					break;
				case "5":
					if (rsIsSet(rs)) {
						sim = new MemorySim(rs, numOfPhysicalFrames, V_PG);
						sim.generate("OPT");
						sim.print();
					}
					break;
				case "6":
					if (rsIsSet(rs)) {
						sim = new MemorySim(rs, numOfPhysicalFrames, V_PG);
						sim.generate("LRU");
						sim.print();
					}
					break;
				case "7":
					if (rsIsSet(rs)) {
						sim = new MemorySim(rs, numOfPhysicalFrames, V_PG);
						sim.generate("LFU");
						sim.print();
					}
					break;
				default:
					break;
			}
		}
	}

	private static int readArg(String[] args) {
		if (args.length < 1) {
			System.out.println("Error: need to specify exactly 1 argument for number of physical frames.");
			System.exit(-1);
		}
		if (args.length > 1) {
			System.out.println("Warning: too many arguments. Every argument after the 1st will be ignored.");
		}
		int n = -1;

		try {
			n = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Error: argument must be an integer.");
			System.exit(-1);
		}

		if (n < 1 || n > P_PG) {
			System.out.println("Error: must be between 1 and " + (P_PG) + " physical frames.");
			System.exit(-1);
		}
		return n;
	}

	static RefString readRefString(Scanner in) {
		System.out.println("Enter a series of numbers: ");
		ArrayList<Integer> al = new ArrayList<Integer>();

		RefString rs = null;

		do {
			String line = in.nextLine();
			Scanner lineScanner = new Scanner(line);
			String temp;
			int tempInt = -1;
			boolean isInt;
			while (lineScanner.hasNext()) {
				temp = lineScanner.next();
				isInt = false;
				try {
					tempInt = Integer.parseInt(temp);
					isInt = true;
				} catch (NumberFormatException e) {
					System.out.println("Warning: you entered a non-integer; \"" + temp + "\" ignored.");
				}
				if (isInt && (tempInt < 0 || tempInt >= V_PG)) {
					System.out.println(
							"Warning: numbers must be between 0 and " + (V_PG - 1) + "; \"" + temp + "\" ignored.");
				} else if (isInt) {
					al.add(tempInt);
				}
			}
			if (al.size() < 1) {
				System.out.println("Error: you must enter at least 1 valid integer between 0 and 9. Please try again.");
			}
		} while (al.size() < 1);
		rs = new RefString(al);

		return rs;
	}

	static int getStringSize(Scanner in) {
		int stringSize = 0;
		while (stringSize < 1) {
			try {
				stringSize = in.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("You must enter an integer.");
			}
			in.nextLine();
			if (stringSize < 1) {
				System.out.println("You must enter a positive integer.");
			}
		}
		return stringSize;
	}

	static RefString generateString(int n, int max) {

		if (n < 1) {
			System.out.println("Error: cannot create a reference string shorter than 1.");
			return null;
		}
		Random rand = new Random();

		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			ar.add(rand.nextInt(max));
		}

		RefString rs = new RefString(ar);
		return rs;
	}

	static void stringConfirm(RefString rs) {
		if (rs != null) {
			System.out.print("Valid reference string saved: ");
			rs.print();
			System.out.print(".");
		} else {
			System.out.println("Invalid reference string. Please try again.");
		}
	}

	static boolean rsIsSet(RefString rs) {
		if (rs != null) {
			return true;
		}
		System.out.println("Error: reference string not yet entered/generated!");
		return false;
	}
}
