package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
	throws IllegalArgumentException {
		BigInteger x = new BigInteger();
		char firstchar = integer.charAt(0);
		while(firstchar==' ') {
			int l = integer.length();
			integer = integer.substring(1,l);
			firstchar = integer.charAt(0);
		}
		char lastchar = integer.charAt(integer.length()-1);
		while(lastchar==' ') {
			integer = integer.substring(0,integer.length()-1);
			lastchar = integer.charAt(integer.length()-1);
		}
		if(integer.charAt(0)=='+') {
			x.negative = false;
		}else if(integer.charAt(0)=='-') {
			x.negative = true;
		}
		int firstint = Character.getNumericValue(integer.charAt(0));
		if(integer.charAt(0)=='+'||integer.charAt(0)=='-') {
			firstint = Character.getNumericValue(integer.charAt(1));
		}
		while(firstint==0) {
			if(integer.length()==1 && firstint == 0) {
				integer = "0";
				break;
			}
			int l = integer.length();
			integer = integer.substring(1,l);
			firstint = Character.getNumericValue(integer.charAt(0));
			if(integer.charAt(0)=='+'||integer.charAt(0)=='-') {
				firstint = Character.getNumericValue(integer.charAt(1));
			}
		}
		for(int i = 0;i <=integer.length()-1;i++) {
			if(integer.charAt(i)=='+'||integer.charAt(i)=='-') {
				i++;
			}else if(integer.charAt(0)=='0') {
				x.negative = false;
				x.numDigits = 0;
				x.front = null;
				return x;
			}
			if(Character.isDigit(integer.charAt(i))) {
				int dig = Character.getNumericValue(integer.charAt(i));
				DigitNode n = new DigitNode(dig,x.front);
				x.numDigits++;
				x.front = n;
			}else {
				throw new IllegalArgumentException();
			}
		}
		return x; 
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		BigInteger sum = new BigInteger();
		sum.negative = false;
		sum.numDigits = 0;
		sum.front = null;
		int max = 0;
		int dif = 0;
		if(first.front == null) {
			sum = second;
			return sum;
		}
		if(second.front == null) {
			sum = first;
			return sum;
		}
		if(first.numDigits > second.numDigits) {
			max = first.numDigits;
			dif = max - second.numDigits;
			DigitNode end = second.front;
			for(int x = 1; x<=dif;x++) {
				while ((end.next != null)) {
			           end = end.next;
			       }
				end.next = new DigitNode(0, null);
			}
		}else if(first.numDigits < second.numDigits){
			max = second.numDigits;
			dif = max - first.numDigits;
			DigitNode end = first.front;
			for(int x = 1; x<=dif;x++) {
				while ((end.next != null)) {
			           end = end.next;
			       }
				end.next = new DigitNode(0, null);
			}
		}else {
			max = first.numDigits;
		}
		int carry = 0;
		int sum_dig = 0;
		DigitNode sum_ptr = sum.front;
		DigitNode fir_ptr = first.front;
		DigitNode sec_ptr = second.front;
		if(first.negative^second.negative) {
			for(int i=1; i <= max; i++) {
				if(first.negative) {
					fir_ptr.digit = -1*fir_ptr.digit;
				}else {
					fir_ptr.digit = Math.abs(fir_ptr.digit);
				}
				if(second.negative) {
					sec_ptr.digit = -1*sec_ptr.digit;
				}else {
					sec_ptr.digit = Math.abs(sec_ptr.digit);
				}
				sum_dig =  fir_ptr.digit + sec_ptr.digit;
				if(carry == -1) {
					sum_dig --;
					}
				if(sum_dig < 0) {
					sum_dig += 10;
					carry = -1;
				}else {
					carry = 0;
				}
				if(sum.front == null) {
					sum.front = new DigitNode(sum_dig, null);
					sum_ptr = sum.front;
				}else {
					sum_ptr.next = new DigitNode(sum_dig, null);
					sum_ptr = sum_ptr.next;
				}
				fir_ptr = fir_ptr.next;
				sec_ptr = sec_ptr.next;
				sum.numDigits++;
				if((fir_ptr == null || sec_ptr == null)&& carry == -1) {
					Boolean b = first.negative;
					first.negative = second.negative;
					second.negative = b;
					fir_ptr = first.front;
					sec_ptr = second.front;
					sum.negative = true;
					sum.numDigits = 0;
					sum.front = null;
					carry = 0;
					sum_ptr = sum.front;
					i = 0;
				}
			}
		}else {
			if(first.negative&&second.negative) {
				sum.negative = true;
			}
			for(int j=1; j <= max; j++) {
				sum_dig = fir_ptr.digit + sec_ptr.digit;
				if(carry == 1) {
					sum_dig ++;
				}
				if(sum_dig >= 10) {
					sum_dig -= 10;
					carry = 1;
				}else {
					carry = 0;
				}
				if(sum.front == null) {
					sum.front = new DigitNode(sum_dig, null);
					sum_ptr = sum.front;
				}else {
					sum_ptr.next = new DigitNode(sum_dig, null);
					sum_ptr = sum_ptr.next;
				}
				if(carry == 1 && j == max) {
					sum_ptr.next = new DigitNode(1, null);
					sum.numDigits++;
				}
				fir_ptr = fir_ptr.next;
				sec_ptr = sec_ptr.next;
				sum.numDigits++;
				}
		}
		if(sum.numDigits>1) {
			DigitNode ptr = sum.front.next;
			while (ptr.next!=null) {
				ptr = ptr.next;
			}
			int r = ptr.digit;
			while(r==0 && sum.numDigits>1) {
				DigitNode ptr1 = sum.front;
				DigitNode ptr2 = sum.front.next;
				while (ptr2.next!=null) {
					ptr1 = ptr2;
					ptr2 = ptr2.next;
				}
				r = ptr1.digit;
				ptr1.next = null;
				sum.numDigits--;
			}
		}
		if(sum.numDigits == 1&& sum.front.digit == 0) {
			sum.negative = false;
			sum.numDigits = 0;
			sum.front = null;
		}
		return sum;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		BigInteger product = new BigInteger();
		product.negative = false;
		product.numDigits = 0;
		product.front = null;
		if(first.front == null||second.front == null) {
			return product;
		}
		int firstvalue = first.front.digit;
		if(firstvalue == 1&& first.numDigits ==1) {
			product = second;
			if(first.negative^second.negative) {
				product.negative = true;
			}else {
				product.negative = false;
				}
			return product;
		}
		DigitNode ptr = first.front;
		for(int i = 1;i<=first.numDigits; i++) {
			firstvalue = ptr.digit*(int)Math.pow(10,i-1);
			for(int a= 1;a<=firstvalue;a++) {
				product = add(product,second);
			}
			ptr = ptr.next;
		}
		if(first.negative^second.negative) {
			product.negative = true;
		}else {
			product.negative = false;
			}
		return product; 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
